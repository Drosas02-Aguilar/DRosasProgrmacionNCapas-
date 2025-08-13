package com.digis01.DRosasAguilarDamianNCapasProject.Controller;

import com.digis01.DRosasAguilarDamianNCapasProject.DAO.PaisDAOImplementation;
import com.digis01.DRosasAguilarDamianNCapasProject.DAO.RolDAOImplementation;
import com.digis01.DRosasAguilarDamianNCapasProject.DAO.UsuarioDAOImplementation;
import com.digis01.DRosasAguilarDamianNCapasProject.ML.Colonia;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import com.digis01.DRosasAguilarDamianNCapasProject.ML.Result;
import com.digis01.DRosasAguilarDamianNCapasProject.ML.Rol;
import com.digis01.DRosasAguilarDamianNCapasProject.ML.Usuario;
import jakarta.validation.Valid;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("usuario")
public class UsuarioController {

    @Autowired
    private RolDAOImplementation rolDAOImplementation;
    
    @Autowired
    private UsuarioDAOImplementation usuarioDAOImplementation;
    
    @Autowired
    private PaisDAOImplementation paisDAOImplementation;

    @GetMapping
    public String Index(Model model) {

        Result result = usuarioDAOImplementation.GetAll();

        if (result.correct) {
            model.addAttribute("usuarios", result.objects);
        } else {
            model.addAttribute("usuarios", null);
        }

        return "UsuarioIndex";
    }

    @GetMapping("editarUsuario/{idUsuario}")
    public String EditarUsuario(@PathVariable int idUsuario, Model model) {
        Result result = usuarioDAOImplementation.DireccionesByIdUsuario(idUsuario);
        if (result.correct) {
            model.addAttribute("usuario", result.object);

        } else {
            return "Error";
        }
        return "EditarUsuario";
    }

    @GetMapping("add") // localhost:8081/alumno/add
    public String add(Model model) {

        model.addAttribute("roles",rolDAOImplementation.GetAllRol().objects);
        model.addAttribute("Usuario", new Usuario());
        model.addAttribute("paises", paisDAOImplementation.GetAllPais().objects);

        return "UsuarioForm";

    }

    @PostMapping("add")
    public String Add(@Valid @ModelAttribute("Usuario") Usuario usuario,
            BindingResult bindingResult,
            Model model) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("Usuario", usuario);
            return "UsuarioForm";
        } else {
            Result result = usuarioDAOImplementation.Add(usuario);

            return "redirect:/usuario";
        }

    }

}
