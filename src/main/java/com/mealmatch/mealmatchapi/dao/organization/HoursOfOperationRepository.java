package com.mealmatch.mealmatchapi.dao.organization;

import com.mealmatch.mealmatchapi.model.HoursOfOperation;
import com.mealmatch.mealmatchapi.model.Organization;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HoursOfOperationRepository extends JpaRepository<HoursOfOperation, Long> {
//    List<HoursOfOperation> findByOrganizationId(Long organizationId);
}
