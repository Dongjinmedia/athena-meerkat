package com.athena.meerkat.controller.web.tomcat.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.athena.meerkat.controller.web.entities.DomainAlertSetting;

@Repository
public interface DomainAlertSettingRepository extends JpaRepository<DomainAlertSetting, Integer> {

}
