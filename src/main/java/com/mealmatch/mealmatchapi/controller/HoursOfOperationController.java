package com.mealmatch.mealmatchapi.controller;

import com.mealmatch.mealmatchapi.dao.organization.HoursOfOperationDAO;
import com.mealmatch.mealmatchapi.dao.organization.OrganizationDAO;
import com.mealmatch.mealmatchapi.model.ApiResponse;
import com.mealmatch.mealmatchapi.model.HoursOfOperation;
import com.mealmatch.mealmatchapi.model.Organization;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
@RequestMapping(value = "/api/v1/hours-of-operations")

public class HoursOfOperationController {
    private final HoursOfOperationDAO hoursOfOperationDAO;
    private final OrganizationDAO organizationDAO;

    public HoursOfOperationController(HoursOfOperationDAO hoursOfOperationDAO,
                                      OrganizationDAO organizationDAO) {
        this.hoursOfOperationDAO = hoursOfOperationDAO;
        this.organizationDAO = organizationDAO;
    }

    @PostMapping(value = "/save")
    @ResponseBody
    ApiResponse<HoursOfOperation> saveOrganisationsHoursOfOperation(@RequestBody HoursOfOperation hoursOfOperation){
        try {
            Optional<Organization> organization = organizationDAO.getById(hoursOfOperation.getOrganizationId().getId());
            hoursOfOperation.setOrganizationId(organization.get());
            HoursOfOperation savedHoursOfOperation = this.hoursOfOperationDAO.saveOrganizationsHoursOfOperation(hoursOfOperation);
            return new ApiResponse<>(HttpStatus.ACCEPTED, savedHoursOfOperation);
        } catch (Exception e){
            return new ApiResponse<>(HttpStatus.BAD_REQUEST, "Couldn't save the hours.");
        }
    }

}
