package org.llc.webcast.config

import groovy.util.logging.Log
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.builders.WebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.config.annotation.web.servlet.configuration.EnableWebMvcSecurity
import org.springframework.security.config.http.SessionCreationPolicy

/**
 * Created by steve on 11/1/2015.
 */
@Configuration
@EnableWebMvcSecurity
@Log
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Value("\${webcast.username}")
    String username

    @Value("\${webcast.password}")
    String password

    @Override
    public void configure(WebSecurity webSecurity) throws Exception {
        webSecurity
                .ignoring()
                .antMatchers(HttpMethod.GET, "/**")
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .httpBasic().and()
                .authorizeRequests()
                .antMatchers("/resources/**", '/').permitAll()
                .anyRequest()
                .authenticated()
                .and()
                .formLogin()
                .loginPage("/login")
                .permitAll()
                .and()
                .logout()
                .permitAll()
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
                .and()
                .csrf().disable()
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .inMemoryAuthentication()
                .withUser(username).password(password).roles("USER");
    }
}
