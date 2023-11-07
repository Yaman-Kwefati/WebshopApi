package com.mealmatch.mealmatchapi.dao.organization;

import com.mealmatch.mealmatchapi.model.Organization;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class OrganizationDAO {
    private final OrganizationRepository organizationRepository;

    public OrganizationDAO(OrganizationRepository organizationRepository) {
        this.organizationRepository = organizationRepository;
    }

    public List<Organization> getAllOrganizations(){
        return this.organizationRepository.findAll();
    }

    public Optional<Organization> getById(Long organizationId){
        return Optional.of(this.organizationRepository.findById(organizationId).get());
    }

    public Organization saveNewOrganization(Organization organization){
        Organization existingOrganization = this.organizationRepository.findByNameAndStreet(organization.getName(), organization.getStreet());
        if (existingOrganization != null){
            return existingOrganization;
        }
        return this.organizationRepository.save(organization);
    }

}
