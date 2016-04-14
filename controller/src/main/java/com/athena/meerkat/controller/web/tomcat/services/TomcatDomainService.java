package com.athena.meerkat.controller.web.tomcat.services;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.athena.meerkat.controller.web.common.code.CommonCodeRepository;
import com.athena.meerkat.controller.web.entities.ClusteringConfiguration;
import com.athena.meerkat.controller.web.entities.ClusteringConfigurationVersion;
import com.athena.meerkat.controller.web.entities.DataSource;
import com.athena.meerkat.controller.web.entities.DomainTomcatConfiguration;
import com.athena.meerkat.controller.web.entities.TomcatApplication;
import com.athena.meerkat.controller.web.entities.TomcatDomain;
import com.athena.meerkat.controller.web.entities.TomcatDomainDatasource;
import com.athena.meerkat.controller.web.resources.repositories.DataSourceRepository;
import com.athena.meerkat.controller.web.tomcat.repositories.ApplicationRepository;
import com.athena.meerkat.controller.web.tomcat.repositories.ClusteringConfigurationReposiroty;
import com.athena.meerkat.controller.web.tomcat.repositories.ClusteringConfigurationVersionRepository;
import com.athena.meerkat.controller.web.tomcat.repositories.DomainRepository;
import com.athena.meerkat.controller.web.tomcat.repositories.DomainTomcatConfigurationRepository;
import com.athena.meerkat.controller.web.tomcat.repositories.TomcatDomainDatasourceRepository;
import com.athena.meerkat.controller.web.tomcat.repositories.TomcatInstanceRepository;
import com.athena.meerkat.controller.web.tomcat.viewmodels.ClusteringConfComparisionViewModel;

@Service
public class TomcatDomainService {
	private static final Logger LOGGER = LoggerFactory
			.getLogger(TomcatDomainService.class);
	@Autowired
	private DomainRepository domainRepo;
	@Autowired
	private TomcatInstanceRepository tomcatRepo;
	@Autowired
	private CommonCodeRepository commonRepo;
	@Autowired
	private DomainTomcatConfigurationRepository domainTomcatConfRepo;
	@Autowired
	private ApplicationRepository appRepo;
	@Autowired
	private TomcatDomainDatasourceRepository tdDatasoureRepo;
	@Autowired
	private DataSourceRepository dsRepo;

	@Transactional
	public TomcatDomain save(TomcatDomain domain) {
		return domainRepo.save(domain);
	}

	@Transactional
	public DomainTomcatConfiguration saveWithConfig(TomcatDomain domain,
			DomainTomcatConfiguration config) {
		domainRepo.save(domain);

		config.setTomcatDomain(domain);

		return saveDomainTomcatConfig(config);
	}

	@Transactional
	public void saveDatasources(List<TomcatDomainDatasource> datasources) {
		tdDatasoureRepo.save(datasources);
	}

	public boolean delete(int domainId) {
		TomcatDomain domain = domainRepo.findOne(domainId);
		if (domain == null) {
			return false;
		}
		// delete all associated tomcats
		// tomcatRepo.delete(domain.getTomcats());
		// delete relation between domain and datagridgroup
		// if (domain.getServerGroup() != null) {
		// domain.getServerGroup().setDomain(null);
		// }
		domainRepo.delete(domain);
		return true;
	}

	public List<TomcatDomain> getAll() {
		return domainRepo.findAll();
	}

	public List<TomcatApplication> getApplicationListByDomain(int domainId) {

		return appRepo.findByTomcatDomain_Id(domainId);
	}

	public TomcatDomain getDomain(int id) {
		TomcatDomain domain = domainRepo.findOne(id);
		return domain;
	}

	public List<TomcatDomain> getDomainByName(String name) {
		return domainRepo.findByName(name);
	}

	public DomainTomcatConfiguration getTomcatConfig(int domainId) {
		return domainTomcatConfRepo.findByTomcatDomain_Id(domainId);
	}

	public DomainTomcatConfiguration saveDomainTomcatConfig(
			DomainTomcatConfiguration conf) {
		return domainTomcatConfRepo.save(conf);

	}

	public List<DataSource> getDatasourceByDomainId(Integer domainId) {
		return dsRepo.getDatasourcesByDomainId(domainId);
	}
}
