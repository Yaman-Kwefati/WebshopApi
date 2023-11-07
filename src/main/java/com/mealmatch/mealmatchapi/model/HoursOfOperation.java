package com.mealmatch.mealmatchapi.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.NonFinal;

import java.sql.Time;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "hours_of_operation", schema = "public")
public class HoursOfOperation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @NonFinal
    private Long id;
    private String day_of_the_week;
    private Time opening_time;
    private Time closing_time;
    @ManyToOne
    @JsonBackReference
    @JoinColumn(name = "organization_id")
    private Organization organizationId;
}
