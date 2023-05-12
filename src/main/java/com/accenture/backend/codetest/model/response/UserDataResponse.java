package com.accenture.backend.codetest.model.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class UserDataResponse implements Serializable {
    private int id;
    private String ssn;
    private String firstName;
    private String lastName;
    private String birthDate;
    private String createdTime;
    private String updatedTime;
    private String createdBy;
    private String updatedBy;
    private boolean isActive;
}
