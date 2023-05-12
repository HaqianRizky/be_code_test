package com.accenture.backend.codetest.model.request;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.NumberFormat;
import org.springframework.validation.annotation.Validated;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@Validated
public class UserModRequest {

    @NotNull
    @NumberFormat
    private String ssn;

    @NotNull
    @Size(min = 3, max = 100)
    private String firstName;

    @NotNull
    @Size(min = 3, max = 100)
    private String lastName;

    @NotNull
    @DateTimeFormat
    private Date birthDate;
}
