package com.example.firstpilot.model;

import lombok.Getter;
import lombok.Setter;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.*;

@Getter
@Setter
@Entity
@EqualsAndHashCode(of = "role_id")
@ToString
public class MemberRole {
    @Id
    @GeneratedValue
    private Long role_id;

    private String roleName;
}
