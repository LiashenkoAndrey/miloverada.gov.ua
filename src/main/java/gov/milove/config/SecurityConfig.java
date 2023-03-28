package gov.milove.config;


import gov.milove.services.SimpleUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@EnableWebSecurity
@EnableMethodSecurity
@Configuration
public class SecurityConfig {

    @Bean
    DaoAuthenticationProvider authProvider() {
       var provider = new DaoAuthenticationProvider();
       provider.setPasswordEncoder(bCryptPasswordEncoder());
       provider.setUserDetailsService(userDetailsService());
       return provider;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        String[] staticResources  =  {
                "/styles/**",
                "/img/**",
                "/scripts/**",
        };

        http
                .authorizeHttpRequests()
                .requestMatchers(staticResources).permitAll()
                .anyRequest().permitAll()
                .and()
                .formLogin().failureUrl("/admin/login?error=true")
                .defaultSuccessUrl("/")
                .loginProcessingUrl("/login")
                .loginPage("/admin/login")
                .permitAll()
                .and()
                .logout().logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                .permitAll();
        http.cors().and().csrf().disable();
        return http.build();
    }


    @Bean
    public SimpleUserDetailsService userDetailsService() {
        return new SimpleUserDetailsService();
    }


    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
