package com.athena.meerkat.controller.web.resources.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.athena.meerkat.controller.MeerkatConstants;
import com.athena.meerkat.controller.ServiceResult;
import com.athena.meerkat.controller.ServiceResult.Status;
import com.athena.meerkat.controller.common.SSHManager;
import com.athena.meerkat.controller.web.entities.Server;
import com.athena.meerkat.controller.web.resources.repositories.ServerRepository;

/**
 * This is an service that is used for processing machine information
 * 
 * @author Tran Ho
 */

@Service
public class ServerService {
	@Autowired
	private ServerRepository machineRepo;

	public ServerService() {

	}

	/**
	 * Add new machine. Based on the provided SSH information, machine info will
	 * be collected.
	 * 
	 * @param m
	 * @return
	 */
	public ServiceResult add(String name, String description, String sshAddr,
			int sshPort, String sshUserName, String sshPassword) {
		// check existing
		// List<Server> existingMachines =
		// machineRepo.findByNameOrSshIPAddr(name,
		// sshAddr);
		// if (existingMachines != null) {
		// if (existingMachines.size() > 0) {
		// return new ServiceResult(Status.FAILED, "Duplicate machine");
		// }
		// }

		try {
			// Server m = new Server(name, sshAddr, sshUserName, sshPassword,
			// sshPort, description);
			Server m = new Server();
			ServiceResult result = getMachineInfoBySSH(m);
			if (result.getStatus() != Status.DONE) {
				return result;
			}
			machineRepo.save((Server) result.getReturnedVal());
		} catch (Exception e) {
			return new ServiceResult(Status.ERROR, e.getMessage());
		}
		return new ServiceResult(Status.DONE, "Machine is added.");
	}

	/**
	 * Retrieve list of machines
	 * 
	 * @param page
	 *            : page number
	 * @param size
	 *            : Number of records will be retrieved
	 * @return ServiceStatus
	 */
	public ServiceResult retrieve(int page, int size) {
		Page<Server> machines = machineRepo
				.findAll(new PageRequest(page, size));
		return new ServiceResult(Status.DONE, "Done", machines);
	}

	// public ServiceResult retrieve(int id) {
	// return new ServiceResult(Status.DONE, "", machineRepo.findOne(id));
	// }
	public Server retrieve(int id) {
		return machineRepo.findOne(id);
	}

	public List<Server> getListByType(int type) {

		//List<Server> list = machineRepo.findByMachineServerType(type);
		//return list;
		return null;
	}

	/**
	 * Collect information of machine using SSH component
	 * 
	 * @param sshIPAddr
	 *            machine ip address
	 * @param sshPort
	 *            ssh port
	 * @param sshUserName
	 *            ssh username
	 * @param sshPassword
	 *            ssh password
	 */

	private ServiceResult getMachineInfoBySSH(Server m) {
		// String sshCommand = "";
		// String sshOutput = "";
		//
		// // test connection
		// SSHManager sshMng = new SSHManager(m.getSshUsername(),
		// m.getSshPassword(), m.getSSHIPAddr(), "", m.getSshPort());
		//
		// if (sshMng.connect() != null) {
		// return new ServiceResult(Status.FAILED,
		// "SSH Connection is failed.", null);
		// }
		// // parse machine information
		// // get hostname info
		// sshCommand = "hostname";
		// sshOutput = sshMng.sendCommand(sshCommand).trim();
		// m.setHostName(sshOutput);
		// // get os name and version
		// sshCommand = "lsb_release -i | sed -e 's/^[a-z A-Z]*://'";
		// sshOutput = sshMng.sendCommand(sshCommand).trim();
		// m.setOsName(sshOutput);
		//
		// sshCommand = "lsb_release -r | sed -e 's/^[a-z A-Z]*://'";
		// sshOutput = sshMng.sendCommand(sshCommand).trim();
		// m.setOsVersion(sshOutput);
		// // get number of cpu
		// sshCommand = "nproc";
		// sshOutput = sshMng.sendCommand(sshCommand).trim();
		// m.setCpuCore(Integer.parseInt(sshOutput));
		// // get cpu speed
		// sshCommand = "lscpu | grep 'CPU MHz' | sed -e 's/^[a-z A-Z :]*//'";
		// sshOutput = sshMng.sendCommand(sshCommand).trim();
		// m.setCpuClockSpeed(Float.parseFloat(sshOutput));
		// // cpu clock measurement unit
		// m.setCpuClockUnit(MeerkatConstants.DEFAULT_CPU_MEASUREMENT_UNIT);
		// // total memory
		// sshCommand =
		// "cat /proc/meminfo | grep 'MemTotal' | sed -e 's/^[a-z A-Z]*://' -e 's/[a-z A-Z]*$//'";
		// sshOutput = sshMng.sendCommand(sshCommand).trim();
		// m.setMemorySize(Float.parseFloat(sshOutput));
		// // memory measurement unit
		// sshCommand =
		// "cat /proc/meminfo | grep 'MemTotal' | sed -e 's/^[a-z A-Z: 0-9]*[ ]//'";
		// sshOutput = sshMng.sendCommand(sshCommand).trim();
		// m.setMemoryUnit(sshOutput);
		// // OS architecture
		// sshCommand = "arch";
		// sshOutput = sshMng.sendCommand(sshCommand).trim();
		// m.setOsArch(sshOutput);
		// // check whether it is VM or not.
		//
		// sshCommand = "virt-what";
		// sshOutput = sshMng.sendCommand(sshCommand).trim();
		// m.isVM(sshOutput.equals("") ? false : true);
		//
		// // check java version
		// sshCommand =
		// "java -version 2>&1 | grep version | sed -e 's/java version \"//' -e 's/.$//'";
		// sshOutput = sshMng.sendCommand(sshCommand).trim();
		// // get network interfaces info
		// // sshCommand =
		// //
		// "ifconfig | sed  -e 's/[ ].*HWaddr/|/' -e 's/inet addr:/|/' -e 's/Bcast:.*Mask:/|/' -e 's/inet6 addr: /|/' -e 's/Scope:.*/|/'";
		// // sshOutput = sshMng.sendCommand(sshCommand);
		// // extract information

		// return new ServiceResult(Status.DONE, "", m);
		return null;
	}

	/**
	 * Edit machine information. Update name, description and using SSH account
	 * to re-collect machine info.
	 * 
	 * @param machineId
	 *            Machine Id need to be edited
	 * @param name
	 *            New name of machine
	 * @param description
	 *            New description
	 * 
	 * @param sshAddr
	 *            SSH IP address
	 * @param sshPort
	 *            SSH port
	 * 
	 * @param sshUserName
	 *            SSH username
	 * @param sshPassword
	 *            SSH password
	 * @return
	 */
	public ServiceResult edit(int machineId, String name, String description,
			String sshAddr, int sshPort, String sshUserName, String sshPassword) {
		// boolean isChanged = false;
		// Server m = machineRepo.getOne(machineId);
		// if (m == null) {
		// return new ServiceResult(Status.FAILED, "Machine does not exist.");
		// }
		// if (!m.getName().equals(name)) {
		// isChanged = true;
		// m.setName(name);
		// }
		// if (!m.getDescription().equals(description)) {
		// isChanged = true;
		// m.setDescription(description);
		// }
		// if (m.getSshPort() != sshPort) {
		// isChanged = true;
		// m.setSshPort(sshPort);
		// }
		// if (!m.getSshUsername().equals(sshUserName)) {
		// isChanged = true;
		// m.setSshUsername(sshUserName);
		// }
		// if (!m.getSshPassword().equals(sshPassword)) {
		// isChanged = true;
		// m.setSshPassword(sshPassword);
		// }
		// if (!m.getSSHIPAddr().equals(sshAddr)) {
		// m.setSSHIPAddr(sshAddr);
		// try {
		// return getMachineInfoBySSH(m);
		// } catch (Exception e) {
		// return new ServiceResult(Status.ERROR, e.getMessage());
		// }
		// }
		//
		// if (!isChanged) {
		// return new ServiceResult(Status.FAILED, "Nothing is changed.");
		// }
		// return new ServiceResult(Status.DONE, "Updated", m);
		return null;

	}

	/**
	 * Remove machine out of machine list
	 * 
	 * @param machineId
	 *            Machine Id
	 * @return Service Result
	 */
	public ServiceResult remove(int machineId) {
		Server m = machineRepo.getOne(machineId);
		if (m == null) {
			return new ServiceResult(Status.FAILED, "Machine does not exist");
		}
		machineRepo.delete(m);
		return new ServiceResult(Status.DONE, "Removed");
	}

	/**
	 * Count machine in db
	 */
	public ServiceResult count() {
		return new ServiceResult(Status.DONE, "", machineRepo.count());
	}

	public List<Server> getList() {

		List<Server> list = machineRepo.findAll();
		return list;
	}

	public List<Server> getSimpleMachineList() {
		List<Server> list = getList();
		List<Server> simpleList = new ArrayList<Server>();
		for (Server m : list) {
			final String _name = m.getName();
			final int _id = m.getId();
			// simpleList.add(new Server(_id, _name));
		}
		return simpleList;
	}

	/**
	 * <pre>
	 * 
	 * </pre>
	 * 
	 * @param machine
	 */
	public Server save(Server machine) {
		// TODO Auto-generated method stub
		return machineRepo.save(machine);
	}

}