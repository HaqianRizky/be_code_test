package com.accenture.backend.codetest.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Entity
@Table(name = "user_setting")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserSetting implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Size(min = 3, max = 100)
    @Column(name = "setting_key", nullable = false)
    private String key;
    @Size(min = 3, max = 100)
    @Column(name = "setting_value", nullable = false)
    private String value;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id", nullable = false, referencedColumnName = "id")
    private User userId;
}
