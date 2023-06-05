package com.altiparmakov.userservice.domain;

import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang.StringUtils;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

@Getter
@Setter
@MappedSuperclass
public class BaseProfile {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String firstName;
    private String lastName;
    private String email;

    private Long userId;

    @Enumerated(EnumType.STRING)
    private Role role;

    public String getFullName() {
        String fullName = firstName;
        if (!StringUtils.isEmpty(lastName)) {
            fullName += " " + lastName;
        }
        return fullName;
    }
}
