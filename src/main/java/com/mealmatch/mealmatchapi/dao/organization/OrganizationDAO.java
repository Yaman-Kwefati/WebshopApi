package com.mealmatch.mealmatchapi.dao.organization;

import com.mealmatch.mealmatchapi.model.Organization;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class OrganizationDAO {
    private final OrganizationRepository organizationRepository;

    public OrganizationDAO(OrganizationRepository organizationRepository) {
        this.organizationRepository = organizationRepository;
    }

    public Optional<Organization> getById(Long organizationId){
        return Optional.of(this.organizationRepository.findById(organizationId).get());
    }

    public void saveNewOrganization(Organization organization){
        this.organizationRepository.save(organization);
    }

}
