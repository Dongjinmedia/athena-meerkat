package com.athena.meerkat.controller.web.tomcat;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.athena.meerkat.controller.MeerkatConstants;
import com.athena.meerkat.controller.web.common.model.SimpleJsonResponse;
import com.athena.meerkat.controller.web.entities.TaskHistory;
import com.athena.meerkat.controller.web.entities.TomcatApplication;
import com.athena.meerkat.controller.web.entities.TomcatDomain;
import com.athena.meerkat.controller.web.tomcat.services.ApplicationService;
import com.athena.meerkat.controller.web.tomcat.services.TaskHistoryService;
import com.athena.meerkat.controller.web.tomcat.services.TomcatDomainService;

@Controller
@RequestMapping("application")
public class ApplicationController {

	@Autowired
	private ApplicationService appService;

	@Autowired
	private TomcatDomainService domainService;

	@Autowired
	private TaskHistoryService taskService;

	@RequestMapping(value = "/deploy", method = RequestMethod.POST)
	@ResponseBody
	public SimpleJsonResponse deploy(SimpleJsonResponse json, TomcatApplication app, boolean update) {

		TomcatApplication dbApp = appService.getApplication(app.getContextPath(), app.getTomcatDomain().getId());
		if (!update) {
			if (dbApp != null) {
				json.setMsg("Application exists.");
				json.setData(app);
				json.setSuccess(false);
				return json;
			}
		}
		if (dbApp != null) {
			dbApp.setDeployedTime(new Date());
			dbApp.setWarFile(app.getWarFile());
		} else {
			dbApp = app;
		}
		
		TaskHistory task = taskService.createTasks(app.getTomcatDomain().getId(), MeerkatConstants.TASK_CD_WAR_DEPLOY);

		dbApp.setTaskHistoryId(task.getId());
		TomcatApplication tApp = appService.saveFileAndData(dbApp);
		

		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("applicationId", tApp.getId());
		resultMap.put("task", task);

		json.setData(resultMap);

		return json;
	}

	@RequestMapping("/undeploy")
	@ResponseBody
	public SimpleJsonResponse undeploy(SimpleJsonResponse json, int Id) {
		TomcatApplication app = appService.getApplication(Id);

		if (app == null) {
			json.setSuccess(false);
			json.setMsg("Application does not exist.");

		} else {
			appService.delete(app);

		}
		return json;
	}

	@RequestMapping("/updateTask")
	@ResponseBody
	public SimpleJsonResponse updateTask(SimpleJsonResponse json, int applicationId, int taskCdId) {
		TomcatApplication app = appService.getApplication(applicationId);

		TaskHistory task = taskService.createTasks(app.getTomcatDomain().getId(), taskCdId);

		app.setTaskHistoryId(task.getId());
		appService.update(app);

		json.setData(task);

		return json;
	}

	/*
		@RequestMapping("/start")
		public @ResponseBody SimpleJsonResponse start(SimpleJsonResponse json,
				int id) {
			TomcatApplication app = appService.getApplication(id);
			if (app == null) {
				json.setSuccess(false);
				json.setMsg("Application does not exist.");
				return json;
			}

			if (app.getState() == MeerkatConstants.APP_STATE_STARTED) {
				json.setSuccess(false);
				json.setMsg("Application has been already started.");
				return json;
			}

			if (appService.start(app)) {
				json.setSuccess(true);
				json.setData(MeerkatConstants.APP_STATE_STARTED);
			}
			return json;
		}

		@RequestMapping("/stop")
		public @ResponseBody SimpleJsonResponse stop(SimpleJsonResponse json, int id) {
			TomcatApplication app = appService.getApplication(id);
			if (app == null) {
				json.setSuccess(false);
				json.setMsg("Application does not exist.");
				return json;
			}

			if (app.getState() == MeerkatConstants.APP_STATE_STOPPED) {
				json.setSuccess(false);
				json.setMsg("Application has been already stopped.");
				return json;
			}

			if (appService.stop(app)) {
				json.setSuccess(true);
				json.setData(MeerkatConstants.APP_STATE_STOPPED);
			}
			return json;
		}

		@RequestMapping("/restart")
		public @ResponseBody SimpleJsonResponse restart(SimpleJsonResponse json,
				int id) {
			TomcatApplication app = appService.getApplication(id);
			if (app == null) {
				json.setSuccess(false);
				json.setMsg("Application does not exist.");
				return json;
			}

			if (app.getState() == MeerkatConstants.APP_STATE_STOPPED) {
				json.setSuccess(false);
				json.setMsg("Application has been already stopped.");
				return json;
			} else {
				if (appService.stop(app)) {
					if (appService.start(app)) {
						json.setSuccess(true);
						json.setData(MeerkatConstants.APP_STATE_STARTED);
					}
				}
			}

			return json;
		}
	*/
	@RequestMapping("/list")
	public @ResponseBody SimpleJsonResponse getAppListByDomain(SimpleJsonResponse json, int domainId) {
		TomcatDomain domain = domainService.getDomain(domainId);
		if (domain == null) {
			json.setSuccess(false);
			json.setMsg("Domain does not exist");
		}
		// List<TomcatInstance> tomcats = domain.getTomcats();
		// if (tomcats.size() > 0) {
		// json.setSuccess(true);
		// // json.setData(tomcats.get(0).getApplications());
		// }
		return json;
	}
}
