package com.digis01.DRosasAguilarDamianNCapasProject.Configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SpringSecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
            .authorizeHttpRequests(auth -> auth
                // Público: login y estáticos
                .requestMatchers(
                    "/usuario/login",
                    "/favicon.ico",
                    "/css/**", "/js/**", "/img/**", "/images/**", "/webjars/**"
                ).permitAll()

                // ===== SOLO ADMIN (bloqueo fino por endpoints) =====
                // Vistas/acciones de alta y edición
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

                // Cambiar estatus (PATCH)
                .requestMatchers(HttpMethod.PATCH, "/usuario/*/status").hasRole("ADMIN")

                // Eliminar y borrar dirección (en tu controlador son GET)
                .requestMatchers(HttpMethod.GET,
                    "/usuario/eliminar",
                    "/usuario/direccion/delete"
                ).hasRole("ADMIN")

                // Carga masiva
                .requestMatchers(
                    "/usuario/cargamasiva",
                    "/usuario/cargamasiva/**"
                ).hasRole("ADMIN")

                // ===== BASELINE: listado/búsqueda para ADMIN o USER =====
                .requestMatchers("/usuario/**").hasAnyRole("ADMIN","USER")

                // Lo demás, autenticado
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
            // Redirige a /403 si no tiene permisos
            .exceptionHandling(ex -> ex.accessDeniedPage("/usuario/403"));

        return http.build();
    }

    @Bean
    public InMemoryUserDetailsManager inMemoryUserDetailsManager() {
        UserDetails user = User.builder()
            .username("user")
            .password("{noop}test123")
            .roles("USER")
            .build();

        UserDetails admin = User.builder()
            .username("admin")
            .password("{noop}test123")
            .roles("ADMIN")
            .build();

        return new InMemoryUserDetailsManager(user, admin);
    }
}
