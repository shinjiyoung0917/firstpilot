package com.example.firstpilot.security;

import com.example.firstpilot.service.MemberService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.CorsUtils;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    MemberService memberService;

    /* 인코더 등록 */
    @Bean
    public PasswordEncoder PasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /* 인증방식 */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .userDetailsService(memberService)
                .passwordEncoder(PasswordEncoder());
    }

    /* Secure 패턴 등록 */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                // 아래 함수는 개발시에만 사용?
                .csrf().disable()
                /*.csrf()
                    .ignoringAntMatchers("/boards/**", "/dashboards/**")
                    .and()*/
                // 인증 요청
                .authorizeRequests()
                    .antMatchers("/", "/members/**", "/auth", "/login").permitAll()
                    .anyRequest().authenticated()
                    .requestMatchers(CorsUtils::isPreFlightRequest).permitAll()
                    .and()
                .cors()
                    .and()
                .formLogin()
                    .loginProcessingUrl("/login")
                    .loginPage("/login")
                    .defaultSuccessUrl("/")
                    //.failureUrl("http://localhost:8080/login")
                    .and()
                .logout()
                    .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                    .logoutSuccessUrl("/")
                    .invalidateHttpSession(true)
                    //.deleteCookies(JSESSIONID)
                    .and()
                .exceptionHandling();
    }

    /* Security 제외 패턴 등록 */
    @Override
    public void configure(WebSecurity web) {
        web
                .ignoring()
                .antMatchers("/resource/**", "/static/**", "/css/**", "/js/**", "/images/**");
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.setAllowedOrigins(Arrays.asList("*")); //"http://localhost:8080"
        corsConfiguration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS", "HEAD"));
        corsConfiguration.setAllowedHeaders(Arrays.asList("Origin", "Accept,X-Requested-With", "Content-Type", "Access-Control-Request-Method", "Access-Control-Request-Headers", "Authorization"));
        corsConfiguration.setAllowCredentials(true);
        corsConfiguration.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource configurationSource = new UrlBasedCorsConfigurationSource();
        configurationSource.registerCorsConfiguration("/**", corsConfiguration.applyPermitDefaultValues());

        return configurationSource;
    }
}
