package com.accenture.backend.codetest.entity;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Data
@Entity
@Table(name = "users")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class User implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(nullable = false, unique = true)
    @Size(min = 16, max = 16)
    private String ssn;

    @Column(nullable = false)
    @Size(min = 3, max = 100)
    private String firstName;
    @Size(min = 3, max = 100)
    private String middleName;

    @Column(nullable = false)
    @Size(min = 3, max = 100)
    private String familyName;

    private Date birthDate;

    @Column(nullable = false)
    private LocalDateTime createdTime;

    @Column(nullable = false)
    private LocalDateTime updateTime;

    @Column(nullable = false)
    @Size(max = 100)
    private String createdBy = "SYSTEM";

    @Column(nullable = false)
    @Size(max = 100)
    private String updatedBy = "SYSTEM";
    @Column(nullable = false)
    private Boolean isActive = true;

    private LocalDateTime deletedTime;

    @OneToMany()
    private List<UserSetting> userSetting;
}
