package com.athena.meerkat.controller.web.domain;

import java.util.List;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.athena.meerkat.controller.ServiceResult;
import com.athena.meerkat.controller.ServiceResult.Status;
import com.athena.meerkat.controller.web.application.Application;
import com.athena.meerkat.controller.web.common.model.SimpleJsonResponse;
import com.athena.meerkat.controller.web.datagridserver.DataGridServerService;
import com.athena.meerkat.controller.web.datagridserver.DatagridServerGroup;
import com.athena.meerkat.controller.web.tomcat.instance.TomcatInstance;
import com.athena.meerkat.controller.web.tomcat.instance.TomcatInstanceService;
import com.athena.meerkat.controller.web.user.UserController;

@Controller
@RequestMapping("/domain")
public class DomainController {
	private static final Logger LOGGER = LoggerFactory
			.getLogger(DomainController.class);
	@Autowired
	private DomainService domainService;
	@Autowired
	private DataGridServerService datagridService;
	@Autowired
	private TomcatInstanceService tomcatService;

	@RequestMapping("/save")
	@Transactional
	public @ResponseBody
	SimpleJsonResponse save(SimpleJsonResponse json, Domain domain,
			int datagridServerGroupId) {
		boolean isEdit = !(domain.getId() == 0);
		try {
			List<Domain> existingDomains = domainService.getDomainByName(domain
					.getName());
			if (existingDomains.size() > 0) {
				if (!isEdit) { // add new domain
					json.setSuccess(false);
					json.setMsg("Domain name is already used.");
					return json;
				} else {
					if (domain.getId() != existingDomains.get(0).getId()) {
						json.setSuccess(false);
						json.setMsg("Domain name is already used.");
						return json;
					}
				}
			} else {
				if (isEdit) { // edit domain
					json.setSuccess(false);
					json.setMsg("Domain does not exist.");
					return json;
				}
			}
			if (!domain.getIsClustering()) {
				if (isEdit) { // set domain for associated server is null
					DatagridServerGroup group = existingDomains.get(0)
							.getServerGroup();
					if (group != null) {
						group.setDomain(null);
					}
					datagridService.saveGroup(group);
				}
				domain.setServerGroup(null);
				domainService.save(domain);

			} else {
				DatagridServerGroup group = datagridService
						.getGroup(datagridServerGroupId);
				if (group == null) {
					json.setSuccess(false);
					json.setMsg("Datagrid server group does not exist.");
					return json;
				}
				if (group.getDomain() != null) {// already map to domain
					json.setSuccess(false);
					json.setMsg("Datagrid server group has already mapped to another domain.");
					return json;
				}
				domain.setServerGroup(group);
				domainService.save(domain);

				// update on server group
				group.setDomain(domainService.getDomainByName(domain.getName())
						.get(0));
				datagridService.saveGroup(group);
			}
		} catch (Exception ex) {
			LOGGER.debug(ex.getMessage());
		}
		// Domain domain = domainService.getDomainByName(name);
		// if (domain != null) {
		// if (domain.getId() != id) {// domain exist but not in edit
		// // case.
		// return false;
		// }
		// }
		// if (id > 0) { // edit
		// domain = domainService.getDomain(id);
		// if (domain == null) {
		// return false;
		// }
		// domain.setName(name);
		// domain.setClustering(isClustering);
		// } else {
		// domain = new Domain(name, isClustering);
		// }
		//
		// DatagridServerGroup group = datagridService
		// .getGroup(datagridServerGroupId);
		// if (group == null) {
		// return false;
		// }
		// domain.setServerGroup(group);
		// domainService.save(domain);
		// // update on server group
		// group.setDomain(domainService.getDomainByName(name));
		// datagridService.saveGroup(group);
		json.setSuccess(true);
		return json;
	}

	@RequestMapping("/edit")
	public @ResponseBody
	SimpleJsonResponse edit(SimpleJsonResponse json, int id) {
		Domain domain = domainService.getDomain(id);
		if (domain == null) {
			json.setSuccess(false);
			json.setMsg("Domain does not exist.");
		} else {
			json.setSuccess(true);
			json.setData(domain);
		}
		return json;
	}

	@RequestMapping("/list")
	public @ResponseBody
	SimpleJsonResponse getDomainList(SimpleJsonResponse json) {
		List<Domain> result = domainService.getAll();
		json.setData(result);
		json.setSuccess(true);
		return json;
	}

	@RequestMapping(value = "/get", method = RequestMethod.GET)
	public @ResponseBody
	SimpleJsonResponse getDomain(SimpleJsonResponse json, int id) {
		Domain result = domainService.getDomain(id);
		json.setSuccess(true);
		json.setData(result);
		return json;
	}

	@RequestMapping("/tomcatlist")
	public @ResponseBody
	SimpleJsonResponse getTomcatInstanceByDomain(SimpleJsonResponse json,
			int domainId) {
		// ServiceResult result =
		// tomcatService.getTomcatListByDomainId(domainId);
		// if (result.getStatus() == Status.DONE) {
		// List<TomcatInstance> tomcats = (List<TomcatInstance>) result
		// .getReturnedVal();
		// return tomcats;
		// }
		// return null;
		json.setData(tomcatService.getTomcatListByDomainId(domainId));
		json.setSuccess(true);
		return json;
	}

	@RequestMapping("/applications")
	public @ResponseBody
	SimpleJsonResponse getApplicationsByDomain(SimpleJsonResponse json,
			int domainId) {
		List<Application> apps = domainService
				.getApplicationListByDomain(domainId);
		if (apps != null) {
			json.setSuccess(true);
			json.setData(apps);
		} else {
			json.setSuccess(false);
			json.setMsg("Domain does not exist.");
		}
		return json;
	}

	/*
	 * @RequestMapping("/clustering/config/save") public @ResponseBody boolean
	 */
	@RequestMapping("/delete")
	public @ResponseBody
	SimpleJsonResponse delete(SimpleJsonResponse json, int domainId) {
		if (domainService.delete(domainId)) {
			json.setMsg("Domain is deleted successfully.");
			json.setSuccess(true);
		} else {
			json.setSuccess(false);
			json.setMsg("Domain does not exist.");
		}
		return json;
	}
}