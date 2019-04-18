package com.example.firstpilot.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Getter;
import lombok.Setter;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;

import java.time.LocalDateTime;

import java.util.List;
import java.util.ArrayList;
import java.util.Collection;

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

    @Column(name = "nickname")
    private String nickname;

    @Column(name = "updated_date")
    private LocalDateTime updatedDate;

    private String role;
   /* @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "email")
    private List<MemberRole> role;*/

    /*@OneToMany(mappedBy = "member")
    //@JsonManagedReference
    @OrderBy("createdDate DESC ")
    private List<Board> boards = new ArrayList<>();
    */
}
