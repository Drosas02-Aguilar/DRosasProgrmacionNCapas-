package com.digis01.DRosasAguilarDamianNCapasProject.Configuration;

import com.digis01.DRosasAguilarDamianNCapasProject.DAO.UserDetailsJPAService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SpringSecurityConfig {

    private final UserDetailsJPAService userDetailsJPAService;

    public SpringSecurityConfig(UserDetailsJPAService userDetailsJPAService) {
        this.userDetailsJPAService = userDetailsJPAService;
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(auth -> auth
//                .requestMatchers(
//                    "/usuario/login",
//                    "/favicon.ico",
//                    "/css/**", "/js/**", "/img/**", "/images/**", "/webjars/**"
//                ).permitAll()

                // --- ADMIN solo ---
                .requestMatchers(HttpMethod.GET,
                    "/usuario/add",
                    "/usuario/editarUsuario",
                    "/usuario/editarInfo",
                    "/usuario/direccion/**"
                ).hasRole("ADMIN")
                .requestMatchers(HttpMethod.POST,
                    "/usuario/add",
                    "/usuario/update",
                    "/usuario/direccion/**"
                ).hasRole("ADMIN")
                .requestMatchers(HttpMethod.PATCH, "/usuario/*/status").hasRole("ADMIN")
                .requestMatchers(HttpMethod.GET,
                    "/usuario/eliminar",
                    "/usuario/direccion/delete"
                ).hasRole("ADMIN")
                .requestMatchers(
                    "/usuario/cargamasiva",
                    "/usuario/cargamasiva/**"
                ).hasRole("ADMIN")

                // Listado/búsqueda (Scrum Master=ADMIN, Desarrollador Backend=USER)
                .requestMatchers("/usuario/**").hasAnyRole("ADMIN","USER")

                .anyRequest().authenticated()
            )
            .formLogin(form -> form
                .loginPage("/usuario/login")
                .loginProcessingUrl("/usuario/login")
                .defaultSuccessUrl("/usuario", true)
                .failureUrl("/usuario/login?error")
                .permitAll()
            )
            .logout(lo -> lo
                .logoutUrl("/logout")
                .logoutSuccessUrl("/usuario/login?logout")
                .invalidateHttpSession(true)
                .clearAuthentication(true)
                .deleteCookies("JSESSIONID")
                .permitAll()
            )
            .exceptionHandling(ex -> ex.accessDeniedPage("/usuario/403"))
            // Usa explícitamente el provider (asegura PasswordEncoder correcto)
                .userDetailsService(userDetailsJPAService);
        return http.build();
    }
}
