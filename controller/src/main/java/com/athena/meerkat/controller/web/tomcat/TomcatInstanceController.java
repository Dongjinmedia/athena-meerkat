/* 
 * Athena Peacock Dolly - DataGrid based Clustering 
 * 
 * Copyright (C) 2014 Open Source Consulting, Inc. All rights reserved by Open Source Consulting, Inc.
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 *
 * Revision History
 * Author			Date				Description
 * ---------------	----------------	------------
 * Bong-Jin Kwon	2015. 1. 9.		First Draft.
 */
package com.athena.meerkat.controller.web.tomcat;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.beanutils.PropertyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.athena.meerkat.controller.MeerkatConstants;
import com.athena.meerkat.controller.web.common.model.GridJsonResponse;
import com.athena.meerkat.controller.web.common.model.SimpleJsonResponse;
import com.athena.meerkat.controller.web.entities.DataSource;
import com.athena.meerkat.controller.web.entities.DomainTomcatConfiguration;
import com.athena.meerkat.controller.web.entities.Server;
import com.athena.meerkat.controller.web.entities.Session;
import com.athena.meerkat.controller.web.entities.TomcatApplication;
import com.athena.meerkat.controller.web.entities.TomcatConfigFile;
import com.athena.meerkat.controller.web.entities.TomcatDomain;
import com.athena.meerkat.controller.web.entities.TomcatInstConfig;
import com.athena.meerkat.controller.web.entities.TomcatInstance;
import com.athena.meerkat.controller.web.provisioning.TomcatProvisioningService;
import com.athena.meerkat.controller.web.resources.services.DataSourceService;
import com.athena.meerkat.controller.web.resources.services.ServerService;
import com.athena.meerkat.controller.web.tomcat.services.TomcatConfigFileService;
import com.athena.meerkat.controller.web.tomcat.services.TomcatDomainService;
import com.athena.meerkat.controller.web.tomcat.services.TomcatInstanceService;
import com.athena.meerkat.controller.web.tomcat.viewmodels.TomcatInstanceViewModel;

/**
 * <pre>
 * 
 * </pre>
 * 
 * @author Bong-Jin Kwon
 * @version 2.0
 */
@RestController
@RequestMapping("/tomcat/instance")
public class TomcatInstanceController {

	@Autowired
	private TomcatInstanceService service;
	@Autowired
	private ServerService machineService;
	@Autowired
	private TomcatDomainService domainService;
	@Autowired
	private DataSourceService dsService;
	@Autowired
	private TomcatConfigFileService tomcatConfigFileService;

	@Autowired
	private TomcatProvisioningService proviService;
	@Autowired
	private ServerService serverService;

	public TomcatInstanceController() {
	}

	@RequestMapping(value = "/start", method = RequestMethod.POST)
	@ResponseBody
	public SimpleJsonResponse startTomcat(SimpleJsonResponse json, int id) {
		TomcatInstance tomcat = service.findOne(id);

		if (tomcat.getState() == MeerkatConstants.TOMCAT_STATUS_RUNNING) {
			json.setSuccess(false);
			json.setMsg("Tomcat is already started.");
		} else {
			proviService.startTomcatInstance(id, null);
			json.setData(MeerkatConstants.TOMCAT_STATUS_RUNNING);
			json.setMsg("Tomcat is started.");
		}

		return json;
	}

	@RequestMapping(value = "/restart", method = RequestMethod.POST)
	@ResponseBody
	public SimpleJsonResponse restartTomcat(SimpleJsonResponse json, int id) {
		stopTomcat(json, id);

		json = new SimpleJsonResponse();

		startTomcat(json, id);
		return json;
	}

	@RequestMapping(value = "/stop", method = RequestMethod.POST)
	@ResponseBody
	public SimpleJsonResponse stopTomcat(SimpleJsonResponse json, int id) {
		TomcatInstance tomcat = service.findOne(id);

		if (tomcat.getState() == MeerkatConstants.TOMCAT_STATUS_SHUTDOWN) {
			json.setSuccess(false);
			json.setMsg("Tomcat is already stopped.");
		} else {
			proviService.stopTomcatInstance(id, null);
			json.setData(MeerkatConstants.TOMCAT_STATUS_SHUTDOWN);

			json.setMsg("Tomcat is stopped.");
		}

		return json;
	}

	@RequestMapping(value = "/get", method = RequestMethod.GET)
	@ResponseBody
	public SimpleJsonResponse getTomcat(SimpleJsonResponse json, int id) {
		TomcatInstance tomcat = service.findOne(id);
		if (tomcat != null) {
			TomcatInstanceViewModel viewmodel = new TomcatInstanceViewModel(tomcat);
			// get configurations that are different to domain tomcat config
			List<TomcatInstConfig> changedConfigs = service.getTomcatInstConfigs(tomcat.getId());
			DomainTomcatConfiguration domainConf = domainService.getTomcatConfig(tomcat.getDomainId());
			if (domainConf != null) {
				viewmodel.setHttpPort(domainConf.getHttpPort());
				viewmodel.setAjpPort(domainConf.getAjpPort());
				viewmodel.setRedirectPort(domainConf.getRedirectPort());
				viewmodel.setTomcatVersion(domainConf.getTomcatVersionNm());
				viewmodel.setJmxEnable(domainConf.isJmxEnable());
			}
			if (changedConfigs != null) {
				for (TomcatInstConfig c : changedConfigs) {
					if (c.getConfigName().equals("httpPort")) {
						viewmodel.setHttpPort(Integer.parseInt(c.getConfigValue()));
					} else if (c.getConfigName().equals("ajpPort")) {
						viewmodel.setAjpPort(Integer.parseInt(c.getConfigValue()));
					} else if (c.getConfigName().equals("redirectPort")) {
						viewmodel.setRedirectPort(Integer.parseInt(c.getConfigValue()));
					} else if (c.getConfigName().equals("tomcatVersion")) {
						viewmodel.setTomcatVersion(c.getConfigValue());
					} else if (c.getConfigName().equals("jmxEnable")) {
						viewmodel.setJmxEnable(Boolean.parseBoolean(c.getConfigValue()));
					}
				}
			}
			// set latest Config file id
			TomcatConfigFile latestServerXml = tomcatConfigFileService.getLatestConfVersion(null, tomcat, MeerkatConstants.CONFIG_FILE_TYPE_SERVER_XML_CD);
			TomcatConfigFile latestContextXml = tomcatConfigFileService.getLatestConfVersion(null, tomcat, MeerkatConstants.CONFIG_FILE_TYPE_CONTEXT_XML_CD);
			if (latestServerXml != null) {
				viewmodel.setLatestServerXmlConfigFileId(latestServerXml.getId());
			}
			if (latestContextXml != null) {
				viewmodel.setLatestContextXmlConfigFileId(latestContextXml.getId());
			}
			json.setData(viewmodel);
		}
		return json;
	}

	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	@ResponseBody
	public SimpleJsonResponse deleteTomcat(SimpleJsonResponse json, int id) {
		TomcatInstance tomcat = service.findOne(id);
		if (tomcat != null) {
			service.delete(tomcat);
			//TODO kwonbj delete provisoning
		}
		return json;
	}

	@RequestMapping(value = "/{id}/conf", method = RequestMethod.GET)
	@ResponseBody
	public SimpleJsonResponse getTomcatConf(SimpleJsonResponse json, @PathVariable Integer id) {

		DomainTomcatConfiguration conf = service.getTomcatConfig(id);

		json.setData(conf);

		return json;
	}

	@RequestMapping(value = "/conf/save", method = RequestMethod.POST)
	@ResponseBody
	public SimpleJsonResponse saveTomcatConf(SimpleJsonResponse json, DomainTomcatConfiguration conf) {
		TomcatInstance tomcat = service.findOne(conf.getTomcatInstanceId());
		if (tomcat != null) {
			//update catalina home
			String cataHome = conf.getCatalinaHome();
			conf.setCatalinaHome(cataHome + conf.getTomcatVersionNm());
			service.saveTomcatConfig(tomcat, conf);
		}

		proviService.updateTomcatInstanceConfig(tomcat, null);
		return json;
	}

	/*
	@RequestMapping(value = "/{serverId}/conf2", method = RequestMethod.GET)
	@ResponseBody
	public SimpleJsonResponse getTomcatConf(SimpleJsonResponse json, @PathVariable int serverId) {

		List<DomainTomcatConfiguration> list = service.findInstanceConfigs(serverId);

		json.setData(list);

		return json;
	}
	*/

	@RequestMapping("/list")
	public @ResponseBody SimpleJsonResponse getTomcatInstance(SimpleJsonResponse json) {
		List<TomcatInstance> tomcats = service.getAll();
		json.setData(tomcats);
		return json;
	}

	@RequestMapping("/listbydomain")
	public @ResponseBody SimpleJsonResponse getTomcatInstances(SimpleJsonResponse json, int domainId) {
		TomcatDomain domain = domainService.getDomain(domainId);
		if (domain != null) {
			json.setData(service.getTomcatListByDomainId(domain.getId()));
		}
		return json;
	}

	@RequestMapping(value = "/{id}/ds", method = RequestMethod.GET)
	public @ResponseBody GridJsonResponse getDatasourceByTomcat(GridJsonResponse json, @PathVariable Integer id) {
		TomcatInstance tomcat = service.findOne(id);
		List<DataSource> datasources = domainService.getDatasources(tomcat.getDomainId());
		json.setList(datasources);
		json.setTotal(datasources.size());

		return json;
	}

	@RequestMapping(value = "/{id}/apps", method = RequestMethod.GET)
	public @ResponseBody GridJsonResponse getAppsByTomcat(GridJsonResponse json, @PathVariable Integer id) {
		TomcatInstance tomcat = service.findOne(id);
		if (tomcat != null) {
			/* example */
			// TODO idkjwon loading
			List<TomcatApplication> domainApps = domainService.getApplicationListByDomain(tomcat.getDomainId());
			List<TomcatApplication> tomcatApps = service.getApplicationByTomcat(tomcat.getId());
			tomcatApps.addAll(domainApps);
			json.setList(tomcatApps);
			json.setTotal(tomcatApps.size());
		}
		return json;
	}

	@RequestMapping(value = "/{id}/sessions", method = RequestMethod.GET)
	public @ResponseBody GridJsonResponse getSessionsByTomcat(GridJsonResponse json, @PathVariable Integer id) {
		TomcatInstance tomcat = service.findOne(id);
		if (tomcat != null) {
			// TODO idkjwon get from clustering session server
			List<Session> sessions = new ArrayList<Session>();
			Session e = new Session();
			e.setId(1);
			e.setKey("Session 1");
			e.setValue("Value session asdasdkjad kajdah dk");
			e.setLocation("Server 1");
			sessions.add(0, e);

			json.setList(sessions);
			json.setTotal(sessions.size());
		}
		return json;
	}

	@RequestMapping(value = "/saveList", method = RequestMethod.POST)
	@ResponseBody
	public SimpleJsonResponse saveList(SimpleJsonResponse json, @RequestBody List<TomcatInstance> tomcats) {
		boolean isUniqueCataBase = false;
		for (TomcatInstance tomcatInstance : tomcats) {

			//checkUnique tomcat name within domain
			if (!checkUniqueTomcatName(tomcatInstance, tomcatInstance.getDomainId())) {
				json.setSuccess(false);
				json.setMsg(String.format("Tomcat instance (%s) name is duplicated.", tomcatInstance.getName()));
				return json;
			}
			TomcatDomain domain = new TomcatDomain();
			domain.setId(tomcatInstance.getDomainId());

			Server server = serverService.getServer(tomcatInstance.getServerId());
			if (server == null) {
				break;
			}
			if (isExistingServerOnDomain(server, tomcatInstance.getDomainId())) {
				json.setSuccess(false);
				json.setMsg(String.format("Server of tomcat instance \"%s\" has been used in this domain", tomcatInstance.getName()));
				return json;
			}
			List<DomainTomcatConfiguration> configs = service.findInstanceConfigs(server.getId());
			isUniqueCataBase = checkUniqueCatalinaBase(configs);
			if (isUniqueCataBase) {
				tomcatInstance.setTomcatDomain(domain);
				tomcatInstance.setServer(server);
			} else {
				json.setSuccess(false);
				json.setMsg(String.format("Catalina base is duplicated in server (%s).", server.getName()));
				return json;
			}
		}
		if (isUniqueCataBase) {
			service.saveList(tomcats);
		}

		return json;
	}

	@RequestMapping(value = "/saveEdit", method = RequestMethod.POST)
	@ResponseBody
	public SimpleJsonResponse saveEditTomcat(SimpleJsonResponse json, TomcatInstance tomcat) {
		TomcatInstance dbTomcat = service.findOne(tomcat.getId());
		if (dbTomcat != null) {
			if (checkUniqueTomcatName(tomcat, dbTomcat.getDomainId())) {
				dbTomcat.setName(tomcat.getName());
				service.save(dbTomcat);
			} else {
				json.setSuccess(false);
				json.setMsg(String.format("Tomcat instance (%s) name is duplicated.", tomcat.getName()));
			}
		}
		return json;
	}

	private boolean checkUniqueCatalinaBase(List<DomainTomcatConfiguration> configs) {
		for (int i = 0; i < configs.size() - 1; i++) {
			for (int j = i; j < configs.size(); j++) {
				if (!configs.get(i).getCatalinaBase().equals(configs.get(j).getCatalinaBase())) {
					return false;
				}
			}
		}
		return true;
	}

	private boolean checkUniqueTomcatName(TomcatInstance tomcat, int domainId) {
		List<TomcatInstance> tomcats = service.findByNameAndDomain(tomcat.getName(), domainId);
		if (tomcats == null || tomcats.size() <= 0) {
			return true;
		}
		if (tomcats.size() == 1 && tomcats.get(0).getId() == tomcat.getId()) {
			return true;
		}
		return false;
	}

	private boolean isExistingServerOnDomain(Server s, int domainId) {
		List<TomcatInstance> tomcats = service.findByDomain(domainId);
		for (TomcatInstance tc : tomcats) {
			if (s.getId() == tc.getServerId()) {
				return true;
			}
		}
		return false;
	}
}
