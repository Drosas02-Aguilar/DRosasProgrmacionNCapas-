package com.digis01.DRosasAguilarDamianNCapasProject.Controller;

import com.digis01.DRosasAguilarDamianNCapasProject.DAO.ColoniaDAOImplementation;
import com.digis01.DRosasAguilarDamianNCapasProject.DAO.EstadoDAOImplementation;
import com.digis01.DRosasAguilarDamianNCapasProject.DAO.MunicipioDAOImplementation;
import com.digis01.DRosasAguilarDamianNCapasProject.DAO.PaisDAOImplementation;
import com.digis01.DRosasAguilarDamianNCapasProject.DAO.RolDAOImplementation;
import com.digis01.DRosasAguilarDamianNCapasProject.DAO.UsuarioDAOImplementation;
import com.digis01.DRosasAguilarDamianNCapasProject.DAO.DireccionDAOImplementation;
import java.util.Base64;
import com.digis01.DRosasAguilarDamianNCapasProject.ML.Colonia;
import com.digis01.DRosasAguilarDamianNCapasProject.ML.Direccion;
import com.digis01.DRosasAguilarDamianNCapasProject.ML.Estado;
import com.digis01.DRosasAguilarDamianNCapasProject.ML.Municipio;
import com.digis01.DRosasAguilarDamianNCapasProject.ML.Pais;
import com.digis01.DRosasAguilarDamianNCapasProject.ML.Result;
import com.digis01.DRosasAguilarDamianNCapasProject.ML.Usuario;

import jakarta.validation.Valid;
import java.util.ArrayList;
import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("usuario")
public class UsuarioController {

    @Autowired
    private RolDAOImplementation rolDAOImplementation;
    @Autowired
    private UsuarioDAOImplementation usuarioDAOImplementation;
    @Autowired
    private PaisDAOImplementation paisDAOImplementation;
    @Autowired
    private EstadoDAOImplementation estadoDAOImplementation;
    @Autowired
    private MunicipioDAOImplementation municipioDAOImplementation;
    @Autowired
    private ColoniaDAOImplementation coloniaDAOImplementation;
    @Autowired
    private DireccionDAOImplementation direccionDAOImplementation;

    // ========================= LISTADO =========================
    @GetMapping
    public String Index(Model model) {
        Result result = usuarioDAOImplementation.GetAll();
        model.addAttribute("usuarios", result.correct ? result.objects : null);
        return "UsuarioIndex";
    }

    // ========================= NUEVO USUARIO (FORM COMPLETO) =========================
    @GetMapping("add")
    public String add(Model model) {
        Result rolesRs = rolDAOImplementation.GetAllRol();
        Result paisesRs = paisDAOImplementation.GetAllPais();

        Usuario usuario = new Usuario();
//        usuario.setDirecciones(new ArrayList<>());
//
//        Direccion direccion = new Direccion();
//        Colonia colonia = new Colonia();
//        Municipio municipio = new Municipio();
//        Estado estado = new Estado();
//        Pais pais = new Pais();
//
//        estado.setPais(pais);
//        municipio.setEstado(estado);
//        colonia.setMunicipio(municipio);
//        direccion.setColonia(colonia);
//
//        usuario.getDirecciones().add(direccion);

        model.addAttribute("Usuario", usuario);
        model.addAttribute("roles", rolesRs.correct ? rolesRs.objects : Collections.emptyList());
        model.addAttribute("paises", paisesRs.correct ? paisesRs.objects : Collections.emptyList());
        model.addAttribute("mode", "full");
        model.addAttribute("action", "add");
        return "UsuarioForm";
    }

    // ========================= GUARDAR NUEVO USUARIO =========================
    @PostMapping("add")
    public String Add(@Valid Usuario usuario, BindingResult br, Model model,@RequestParam("userFotoInput") MultipartFile imagen) {
        
        
        if (br.hasErrors()) {
            Result rolesRs = rolDAOImplementation.GetAllRol();
            Result paisesRs = paisDAOImplementation.GetAllPais();
            model.addAttribute("roles", rolesRs.correct ? rolesRs.objects : Collections.emptyList());
            model.addAttribute("paises", paisesRs.correct ? paisesRs.objects : Collections.emptyList());
            model.addAttribute("mode", "full");
            model.addAttribute("action", "add");
            model.addAttribute("Usuario", usuario);
            return "UsuarioForm";
        }else{
        
          if (imagen != null) {
                String nombre = imagen.getOriginalFilename();
                //archivo.jpg
                //[archivo,jpg]
                String extension = nombre.split("\\.")[1];
                if (extension.equals("jpg")) {
                    try {
                        byte[] bytes = imagen.getBytes();
                        String base64Image = Base64.getEncoder().encodeToString(bytes);
                        usuario.setImagen(base64Image);
                    } catch (Exception ex) {
                        System.out.println("Error");
                    }

                }
        
        }
        usuarioDAOImplementation.Add(usuario);
        return "redirect:/usuario";
    }
        
    }

    // ========================= EDITAR USUARIO (VISTA DETALLE/EDICIÓN) =========================
    @GetMapping("editarUsuario")
    public String EditarUsuario(@RequestParam("idUsuario") int idUsuario, Model model) {
        Result result = usuarioDAOImplementation.DireccionesByIdUsuario(idUsuario);
        if (!result.correct || result.object == null) {
            return "Error";
        }

        Usuario usuario = (Usuario) result.object;

        Result rolesRs = rolDAOImplementation.GetAllRol();
        model.addAttribute("roles", rolesRs.correct ? rolesRs.objects : Collections.emptyList());
        model.addAttribute("usuario", usuario);

        if (usuario.getDirecciones() == null || usuario.getDirecciones().isEmpty()) {
            Result paisesRs = paisDAOImplementation.GetAllPais();

            Direccion direccion = new Direccion();
            Colonia colonia = new Colonia();
            Municipio municipio = new Municipio();
            Estado estado = new Estado();
            Pais pais = new Pais();

            estado.setPais(pais);
            municipio.setEstado(estado);
            colonia.setMunicipio(municipio);
            direccion.setColonia(colonia);

            model.addAttribute("usuario", usuario);
            model.addAttribute("direccion", direccion);
            model.addAttribute("paises", paisesRs.correct ? paisesRs.objects : Collections.emptyList());
            model.addAttribute("mode", "direccion");
            model.addAttribute("action", "add");
            return "UsuarioForm";
        }
        return "EditarUsuario";
    }

    // ========================= EDITAR SOLO INFO DE USUARIO =========================
    @GetMapping("editarInfo")
public String EditarInfoUsuario(@RequestParam("idUsuario") int idUsuario, Model model) {
    Result result = usuarioDAOImplementation.GetByiDUsuario(idUsuario);
    if (!result.correct || result.object == null) {
        return "Error";
    }

    Usuario usuario = (Usuario) result.object;
    Result rolesRs = rolDAOImplementation.GetAllRol();

    model.addAttribute("Usuario", usuario);
    model.addAttribute("roles", rolesRs.correct ? rolesRs.objects : Collections.emptyList());
    model.addAttribute("mode", "usuario");
    model.addAttribute("action", "edit");
    return "UsuarioForm";
}


    // ========================= ACTUALIZAR INFO DE USUARIO =========================
    @PostMapping("update")
public String Update(@Valid @ModelAttribute("Usuario") Usuario usuario, BindingResult bindingResult, Model model) {
    if (bindingResult.hasErrors()) {
        Result result = rolDAOImplementation.GetAllRol();
        model.addAttribute("roles", result.correct ? result.objects : Collections.emptyList());
        model.addAttribute("mode", "usuario");
        model.addAttribute("action", "edit");
        model.addAttribute("Usuario", usuario);
        return "UsuarioForm";
    }
    usuarioDAOImplementation.update(usuario);
    return "redirect:/usuario/editarUsuario?idUsuario=" + usuario.getIdUsuario();
}

    // ========================= AGREGAR DIRECCIÓN A USUARIO (FORM) =========================
    @GetMapping("direccion/add")
    public String DireccionAddForm(@RequestParam("idUsuario") int idUsuario, Model model) {
        Result rs = usuarioDAOImplementation.DireccionesByIdUsuario(idUsuario);
        if (!rs.correct || rs.object == null) {
            return "Error";
        }

        Usuario usuario = (Usuario) rs.object;
        Result paisesRs = paisDAOImplementation.GetAllPais();

        Direccion direccion = new Direccion();
        Colonia colonia = new Colonia();
        Municipio municipio = new Municipio();
        Estado estado = new Estado();
        Pais pais = new Pais();

        estado.setPais(pais);
        municipio.setEstado(estado);
        colonia.setMunicipio(municipio);
        direccion.setColonia(colonia);

        model.addAttribute("usuario", usuario);
        model.addAttribute("direccion", direccion);
        model.addAttribute("paises", paisesRs.correct ? paisesRs.objects : Collections.emptyList());
        model.addAttribute("mode", "direccion");
        model.addAttribute("action", "add");
        return "UsuarioForm";
    }

    // ========================= AGREGAR DIRECCIÓN (POST) =========================
    @PostMapping("direccion/add")
    public String DireccionAdd(@RequestParam("idUsuario") int idUsuario,
            @Valid Direccion direccion, BindingResult br, Model model) {
        if (br.hasErrors()) {
            Result result = usuarioDAOImplementation.DireccionesByIdUsuario(idUsuario);
            Result paises = paisDAOImplementation.GetAllPais();

            model.addAttribute("usuario", result.correct ? result.object : null);
            model.addAttribute("direccion", direccion);
            model.addAttribute("paises", paises.correct ? paises.objects : Collections.emptyList());
            model.addAttribute("mode", "direccion");
            model.addAttribute("action", "add");
            return "UsuarioForm";
        }
        direccionDAOImplementation.addToUsuario(idUsuario, direccion);
        return "redirect:/usuario/editarUsuario?idUsuario=" + idUsuario;
    }

    // ========================= EDITAR DIRECCIÓN (FORM) =========================
    @GetMapping("direccion/edit")
    public String DireccionEditForm(@RequestParam("idUsuario") int idUsuario,
            @RequestParam("idDireccion") int idDireccion,
            Model model) {
        Result result = usuarioDAOImplementation.DireccionesByIdUsuario(idUsuario);
        if (!result.correct || result.object == null) {
            return "Error";
        }

        Result resultd = direccionDAOImplementation.getbyid(idDireccion);
        if (!resultd.correct || resultd.object == null) {
            return "Error";
        }

        Result paises = paisDAOImplementation.GetAllPais();

        model.addAttribute("usuario", result.object);
        model.addAttribute("direccion", resultd.object);
        model.addAttribute("paises", paises.correct ? paises.objects : Collections.emptyList());
        model.addAttribute("mode", "direccion");
        model.addAttribute("action", "edit");
        return "UsuarioForm";
    }

    // ========================= ACTUALIZAR DIRECCIÓN (POST) =========================
    @PostMapping("direccion/update")
    public String DireccionUpdate(@RequestParam("idUsuario") int idUsuario,
            @Valid Direccion direccion, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            Result result = usuarioDAOImplementation.DireccionesByIdUsuario(idUsuario);
            Result paisesr = paisDAOImplementation.GetAllPais();

            model.addAttribute("usuario", result.correct ? result.object : null);
            model.addAttribute("direccion", direccion);
            model.addAttribute("paises", paisesr.correct ? paisesr.objects : Collections.emptyList());
            model.addAttribute("mode", "direccion");
            model.addAttribute("action", "edit");
            return "UsuarioForm";
        }
        direccionDAOImplementation.updateDireccion(direccion);
        return "redirect:/usuario/editarUsuario?idUsuario=" + idUsuario;
    }

    // ========================= ELIMINAR USUARIO =========================
    @GetMapping("eliminar")
    public String Eliminar(@RequestParam("id") int idUsuario) {
        usuarioDAOImplementation.deleteById(idUsuario);
        return "redirect:/usuario";
    }

    // ========================= CASCADAS (CATÁLOGOS) =========================
    @GetMapping("getEstadosByPais")
    @ResponseBody
    public Result EstadoByidPais(@RequestParam("IdPais") int IdPais) {
        return estadoDAOImplementation.EstadoByidPais(IdPais);
    }

    @GetMapping("MunicipiosGetByIdEstado")
    @ResponseBody
    public Result municipioByIdEstado(@RequestParam("IdEstado") int IdEstado) {
        return municipioDAOImplementation.MunicipioByidEstado(IdEstado);
    }

    @GetMapping("ColoniasGetByIdMunicipio")
    @ResponseBody
    public Result ColoniaGetByIdMunicipio(@RequestParam("IdMunicipio") int IdMunicipio) {
        return coloniaDAOImplementation.ColoniaByMunicipio(IdMunicipio);
    }

    // ========================= ELIMINAR DIRECCIÓN =========================
    @GetMapping("direccion/delete")
    public String DireccionDelete(@RequestParam int idDireccion,
            @RequestParam int idUsuario) {
        direccionDAOImplementation.delete(idDireccion);
        return "redirect:/usuario/editarUsuario?idUsuario=" + idUsuario;
    }

}
