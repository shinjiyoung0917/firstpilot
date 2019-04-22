package com.example.firstpilot.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "member")
public class Member { //implements UserDetails
    @Id
    @GeneratedValue //(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long memberId;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "nickname", nullable = false)
    private String nickname;

    @Column(name = "updated_date")
    private String updatedDate;

    private String role;
   /* @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "email")
    private List<MemberRole> role;*/
}
