package edu.ucmo.fightingmongeese.pinapp.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;


@Configuration
public class SpringSecurityConfig extends WebSecurityConfigurerAdapter {

    /**
     * Configures basic security for the testing/demo frontend.
     *
     * @param http
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.headers().frameOptions().sameOrigin();
        http.csrf().disable()
                .authorizeRequests()
                .antMatchers("/", "/api/**", "/test/**", "/h2", "/img**").permitAll()
                .antMatchers("/static/**", "/webjars/**").permitAll()
                .antMatchers("/pins**").hasAnyRole("USER", "ADMIN")
                .anyRequest().authenticated()
                .and()
                .formLogin()
                .loginPage("/login")
                .permitAll()
                .and()
                .logout()
                .permitAll();
    }

    /**
     * Pre-seeds the in-memory authentication database with two users for testing
     * purposes.
     *
     * @param auth
     */
    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {

        auth.inMemoryAuthentication()
                .withUser("user").password("password").roles("USER")
                .and()
                .withUser("admin").password("password").roles("ADMIN");
    }
}
