package com.mealmatch.mealmatchapi.dao.organization;

import com.mealmatch.mealmatchapi.model.Organization;
import com.mealmatch.mealmatchapi.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrganizationRepository extends JpaRepository<Organization, Long> {
//    public User getByContact_person(Long contactPersonId);
}
