package com.example.firstpilot.security;

import com.example.firstpilot.service.MemberService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import org.springframework.security.provisioning.InMemoryUserDetailsManager;

import javax.sql.DataSource;

@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    /*@Autowired
    UserDetailsService userDetailsService;*/
    @Autowired
    MemberService memberService;
    //private CustomUserDetailsService customUserDetailsService;

    @Autowired
    PasswordEncoder passwordEncoder;

    /* 인코더 등록 */
    @Bean
    public BCryptPasswordEncoder emailAndPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /* 인증방식 */
    //@Autowired
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .userDetailsService(memberService)
                .passwordEncoder(emailAndPasswordEncoder());
    }

    /* Secure 패턴 등록 */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                // 아래 함수는 개발시에만 사용?
                .csrf().disable()
                // 인증 요청
                .authorizeRequests()
                    .antMatchers("/", "/main", "/members/**", "/auth", "/login/**", "/board", "/**").permitAll()
                    .anyRequest().authenticated()
                    .and()
                .formLogin()
                    .loginProcessingUrl("/login")
                    .loginPage("/login")
                    .defaultSuccessUrl("/main")
                    .failureUrl("/login?error=true")
                    //.usernameParameter("email")
                    //.passwordParameter("password")
                    //.permitAll()
                    .and()
                .logout()
                    .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                    .logoutSuccessUrl("/")
                    .invalidateHttpSession(true)
                    .and()
                .exceptionHandling();
    }

    /* Security 제외 패턴 등록 */
    @Override
    public void configure(WebSecurity web) throws Exception {
        web
                .ignoring()
                .antMatchers("/resource/**", "/static/**", "/css/**", "/js/**", "/images/**");
    }
}
