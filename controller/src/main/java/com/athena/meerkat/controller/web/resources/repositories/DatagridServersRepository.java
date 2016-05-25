package com.athena.meerkat.controller.web.resources.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.athena.meerkat.controller.web.entities.DatagridServer;
import com.athena.meerkat.controller.web.entities.DatagridServerPK;
import com.athena.meerkat.controller.web.entities.Server;

/**
 * DatagridServersRepository
 *
 * @author Bongjin Kwon
 * @version 1.0
 */
@Repository
public interface DatagridServersRepository extends JpaRepository<DatagridServer, DatagridServerPK> {

	List<DatagridServer> findByDatagridServerGroupId(Integer datagridServerGroupId);

}