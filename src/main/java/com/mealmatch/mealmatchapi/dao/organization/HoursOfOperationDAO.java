package com.mealmatch.mealmatchapi.dao.organization;

import com.mealmatch.mealmatchapi.model.HoursOfOperation;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class HoursOfOperationDAO {
    private final HoursOfOperationRepository hoursOfOperationRepository;

    public HoursOfOperationDAO(HoursOfOperationRepository hoursOfOperationRepository) {
        this.hoursOfOperationRepository = hoursOfOperationRepository;
    }

//    public List<HoursOfOperation> getOrganizationsHoursOfOperation(Long organizationId){
//        return this.hoursOfOperationRepository.findByOrganizationId(organizationId);
//    }

    public HoursOfOperation saveOrganizationsHoursOfOperation(HoursOfOperation hoursOfOperation){
        return this.hoursOfOperationRepository.save(hoursOfOperation);
    }
}
