package com.mealmatch.mealmatchapi.controller;

import com.mealmatch.mealmatchapi.dao.organization.OrganizationDAO;
import com.mealmatch.mealmatchapi.model.ApiResponse;
import com.mealmatch.mealmatchapi.model.Organization;
import com.mealmatch.mealmatchapi.model.auth.RegisterRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
@RequestMapping(value = "/api/v1/organizations")
public class OrganizationController {
    private final OrganizationDAO organizationDAO;

    public OrganizationController(OrganizationDAO organizationDAO) {
        this.organizationDAO = organizationDAO;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/{organizationId}")
    @ResponseBody
    ApiResponse<Organization> getOrganization(@PathVariable Long organizationId){
        try {
            Optional<Organization> organization = this.organizationDAO.getById(organizationId);
            return new ApiResponse<>(HttpStatus.ACCEPTED, organization.get());
        } catch (Exception e){
            return new ApiResponse<>(HttpStatus.NOT_FOUND, "Not found.");
        }
    }

    @RequestMapping(method = RequestMethod.POST, value = "/register")
    @ResponseBody
    ApiResponse<Organization> registerNewOrganization(
            @RequestBody Organization organization
    ){
        try {
            Organization organization1 = this.organizationDAO.saveNewOrganization(organization);
            return new ApiResponse<>(HttpStatus.ACCEPTED, organization1);
        } catch (Exception e){
            return new ApiResponse<>(HttpStatus.BAD_REQUEST, "Couldn't register organization.");
        }
    }
}
