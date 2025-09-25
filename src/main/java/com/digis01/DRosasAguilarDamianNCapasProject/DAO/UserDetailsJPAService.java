/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.digis01.DRosasAguilarDamianNCapasProject.DAO;

import com.digis01.DRosasAguilarDamianNCapasProject.JPA.Usuario;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;



@Service
public class UserDetailsJPAService implements UserDetailsService{

    private final IUsuarioRepository iUsuarioRepository;

    public UserDetailsJPAService(IUsuarioRepository iUsuarioRepository1) {
        this.iUsuarioRepository = iUsuarioRepository1;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Usuario usuario = iUsuarioRepository.findByUsername(username);

        // === Mapeo a rol Spring ===
        String nombreRol = (usuario != null && usuario.Rol != null) ? usuario.Rol.getNombre() : null;
        String springRole =
                (nombreRol != null &&
                 (nombreRol.equalsIgnoreCase("Scrum Master")
               || nombreRol.equalsIgnoreCase("Ingeniero de Datos")))
                ? "ADMIN" : "USER";

        return User.withUsername(usuario.getUsername())
                .password(usuario.getPassword())
                .roles(springRole) // genera ROLE_ADMIN o ROLE_USER
                .accountLocked(!(usuario.getStatus() == 1))
                .disabled(!(usuario.getStatus() == 1))
                .build();
    }
}
