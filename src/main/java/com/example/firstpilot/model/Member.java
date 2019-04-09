package com.example.firstpilot.model;

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


@Entity
public class Member  { //implements UserDetails
    @Id
    @GeneratedValue //(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    @Getter
    private Long memberId;

    @Column(name = "email", nullable = false)
    @Getter
    @Setter
    private String email;

    @Column(name = "password", nullable = false)
    @Getter
    @Setter
    private String password;

    @Column(name = "nickname")
    @Getter
    @Setter
    private String nickname;

    @Column(name = "nickname_change_date")
    @Getter
    @Setter
    private LocalDateTime nickChange_date;

    @Getter
    @Setter
    private String role;
   /* @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "email")
    private List<MemberRole> role;*/


    /* 시큐리티 제공
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
        return authorities;
    }

    @Override
    public String getPassword() {
        return this.getPassword();
    }

    @Override
    public String getUsername() {
        return this.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
    */
}


