package com.mealmatch.mealmatchapi.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.NonFinal;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "organization", schema = "public")
public class Organization {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @NonFinal
    private Long id;
    private String name;
    private String phone_number;
    private String city;
    private String street;
    private String postal_code;
    private String type;
    private String description;
    @OneToOne
    @JoinColumn(name = "contact_person")
    private User contact_person;
    @OneToMany(mappedBy = "organizationId")
    @JsonManagedReference
    private List<HoursOfOperation> hoursOfOperations;
}
