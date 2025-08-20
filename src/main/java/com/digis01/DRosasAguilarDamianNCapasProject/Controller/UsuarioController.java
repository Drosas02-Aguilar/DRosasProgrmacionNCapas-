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
import com.digis01.DRosasAguilarDamianNCapasProject.ML.ErrorCM;
import com.digis01.DRosasAguilarDamianNCapasProject.ML.Estado;
import com.digis01.DRosasAguilarDamianNCapasProject.ML.Municipio;
import com.digis01.DRosasAguilarDamianNCapasProject.ML.Pais;
import com.digis01.DRosasAguilarDamianNCapasProject.ML.Result;
import com.digis01.DRosasAguilarDamianNCapasProject.ML.Rol;
import com.digis01.DRosasAguilarDamianNCapasProject.ML.Usuario;

import jakarta.validation.Valid;
import java.io.BufferedReader;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.io.InputStreamReader;

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
    public String Add(@Valid Usuario usuario, BindingResult br, Model model, @RequestParam("userFotoInput") MultipartFile imagen) {

        if (br.hasErrors()) {
            Result rolesRs = rolDAOImplementation.GetAllRol();
            Result paisesRs = paisDAOImplementation.GetAllPais();
            model.addAttribute("roles", rolesRs.correct ? rolesRs.objects : Collections.emptyList());
            model.addAttribute("paises", paisesRs.correct ? paisesRs.objects : Collections.emptyList());
            model.addAttribute("mode", "full");
            model.addAttribute("action", "add");
            model.addAttribute("Usuario", usuario);
            return "UsuarioForm";
        } else {

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
    public String Update(@Valid @ModelAttribute("Usuario") Usuario usuario, BindingResult bindingResult, Model model, @RequestParam("userFotoInput") MultipartFile imagen) {
        if (bindingResult.hasErrors()) {
            Result result = rolDAOImplementation.GetAllRol();
            model.addAttribute("roles", result.correct ? result.objects : Collections.emptyList());
            model.addAttribute("mode", "usuario");
            model.addAttribute("action", "edit");
            model.addAttribute("Usuario", usuario);
            return "UsuarioForm";
        } else {
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
            usuarioDAOImplementation.update(usuario);
            return "redirect:/usuario/editarUsuario?idUsuario=" + usuario.getIdUsuario();

        }
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
    
    // ============= CARGA MASIVA DE DATOS ======================
    
     @GetMapping("cargamasiva")
    public String CargaMasiva(){
        return "CargaMasiva";
    }
    
    @PostMapping("cargamasiva")
    public String CargaMasiva(@RequestParam("archivo") MultipartFile file, Model model){
    
        if(file.getOriginalFilename().split("\\.")[1].equals("txt")){
        List<Usuario> usuarios = ProcesarTXT(file);
        List<ErrorCM> errores = ValidarDatos(usuarios);
        
        if (errores.isEmpty()) {
                model.addAttribute("listaErrores", errores);
                model.addAttribute("archivoCorrecto", true);
            } else {
                model.addAttribute("listaErrores", errores);
                model.addAttribute("archivoCorrecto", false);
            }
        
        } else{
        
        
        }
   
        return "CargaMasiva";
    }

    private List<Usuario> ProcesarTXT(MultipartFile file) {
    try {
        InputStream inputStream = file.getInputStream();
        BufferedReader bufferedReader = new BufferedReader( new InputStreamReader(inputStream));

        String linea = "";
        List<Usuario> usuarios = new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        sdf.setLenient(false);

        while ((linea = bufferedReader.readLine()) != null) {

            String[] campos = linea.split("\\|");

            Usuario usuario = new Usuario();
            usuario.setNombre(campos[0]);
            usuario.setApellidopaterno(campos[1]);
            usuario.setApellidomaterno(campos[2]);
            usuario.setUsername(campos[3]);
            usuario.setEmail(campos[4]);
            usuario.setPassword(campos[5]);

            // Fecha Nacimiento (yyyy-MM-dd)
            try {
                if (campos[6] != null && !campos[6].trim().isEmpty()) {
                    usuario.setFechaNacimiento(sdf.parse(campos[6].trim()));
                }
            } catch (Exception ex) {
            }

            usuario.setSexo(campos[7]);
            usuario.setTelefono(campos[8]);
            usuario.setCelular(campos[9]);
            usuario.setCurp(campos[10]);
            usuario.setTiposangre(campos[11]);

            // Rol
            Rol rol = new Rol();
            try {
                rol.setIdRol(Integer.parseInt(campos[12]));
            } catch (Exception ex) {
                rol.setIdRol(0);
            }
            usuario.setRol(rol);

            // Dirección (una por registro)
            Direccion direccion = new Direccion();
            direccion.setCalle(campos[13]);
            direccion.setNumeroExterior(campos[14]);
            direccion.setNumeroInterior(campos[15]);

            Colonia colonia = new Colonia();
            try {
                colonia.setIdColonia(Integer.parseInt(campos[16]));
            } catch (Exception ex) {
                colonia.setIdColonia(0);
            }
            direccion.setColonia(colonia);

            List<Direccion> direcciones = new ArrayList<>();
            direcciones.add(direccion);
            usuario.setDirecciones(direcciones);

            usuarios.add(usuario);
        }
        return usuarios;
    } catch (Exception ex){
        System.out.println("error");
        return null;
    }
}

    private List<ErrorCM> ValidarDatos(List<Usuario> usuarios) {

       
    List<ErrorCM> errores = new ArrayList<>();
    if (usuarios == null) return errores;

    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    sdf.setLenient(false);

    int linea = 1;
    for (Usuario u : usuarios) {

        // Obligatorios básicos
        if (u.getNombre() == null || u.getNombre() == "") {
            ErrorCM errorCM = new ErrorCM(linea, u.getNombre(), "Campo obligatorio: Nombre");
            errores.add(errorCM);
        }
        if (u.getApellidopaterno() == null || u.getApellidopaterno() == "") {
            ErrorCM errorCM = new ErrorCM(linea, u.getApellidopaterno(), "Campo obligatorio: ApellidoPaterno");
            errores.add(errorCM);
        }
        if (u.getApellidomaterno() == null || u.getApellidomaterno() == "") {
            ErrorCM errorCM = new ErrorCM(linea, u.getApellidomaterno(), "Campo obligatorio: ApellidoMaterno");
            errores.add(errorCM);
        }
        if (u.getUsername() == null || u.getUsername() == "") {
            ErrorCM errorCM = new ErrorCM(linea, u.getUsername(), "Campo obligatorio: Username");
            errores.add(errorCM);
        }
        if (u.getEmail() == null || u.getEmail() == "") {
            ErrorCM errorCM = new ErrorCM(linea, u.getEmail(), "Campo obligatorio: Email");
            errores.add(errorCM);
        } else {
            String e = u.getEmail();
            if (e.indexOf('@') == -1 || e.lastIndexOf('.') < e.indexOf('@') + 1) {
                ErrorCM errorCM = new ErrorCM(linea, u.getEmail(), "Formato inválido: Email");
                errores.add(errorCM);
            }
        }
        if (u.getPassword() == null || u.getPassword() == "") {
            ErrorCM errorCM = new ErrorCM(linea, u.getPassword(), "Campo obligatorio: Password");
            errores.add(errorCM);
        }

        if (u.getFechaNacimiento() != null) {
            try {
                sdf.format(u.getFechaNacimiento());
            } catch (Exception ex) {
                ErrorCM errorCM = new ErrorCM(linea, "", "Fecha inválida (yyyy-MM-dd)");
                errores.add(errorCM);
            }
        }

        // Sexo
        if (u.getSexo() == null || u.getSexo() == "") {
            ErrorCM errorCM = new ErrorCM(linea, u.getSexo(), "Campo obligatorio: Sexo");
            errores.add(errorCM);
        } else {
            String sx = u.getSexo();
            if (!"M".equalsIgnoreCase(sx) && !"F".equalsIgnoreCase(sx)) {
                ErrorCM errorCM = new ErrorCM(linea, u.getSexo(), "Sexo debe ser 'M' o 'F'");
                errores.add(errorCM);
            }
        }

        // Teléfono y Celular (numéricos si vienen)
        if (u.getTelefono() != null && u.getTelefono() != "") {
            try { Long.parseLong(u.getTelefono()); }
            catch (Exception ex) {
                ErrorCM errorCM = new ErrorCM(linea, u.getTelefono(), "Teléfono debe ser numérico");
                errores.add(errorCM);
            }
        }
        if (u.getCelular() != null && u.getCelular() != "") {
            try { Long.parseLong(u.getCelular()); }
            catch (Exception ex) {
                ErrorCM errorCM = new ErrorCM(linea, u.getCelular(), "Celular debe ser numérico");
                errores.add(errorCM);
            }
        }

        // CURP (opcional, solo valida longitud si viene)
        if (u.getCurp() != null && u.getCurp() != "") {
            if (u.getCurp().length() != 18) {
                ErrorCM errorCM = new ErrorCM(linea, u.getCurp(), "CURP debe tener 18 caracteres");
                errores.add(errorCM);
            }
        }

        // Rol
        if (u.getRol() == null || u.getRol().getIdRol() <= 0) {
            String rolVal = (u.getRol() == null) ? "" : String.valueOf(u.getRol().getIdRol());
            ErrorCM errorCM = new ErrorCM(linea, rolVal, "Campo obligatorio: IdRol > 0");
            errores.add(errorCM);
        }

        // Dirección
        if (u.getDirecciones() == null || u.getDirecciones().isEmpty()) {
            ErrorCM errorCM = new ErrorCM(linea, "", "Debe existir al menos una dirección");
            errores.add(errorCM);
        } else {
            Direccion d = u.getDirecciones().get(0);
            if (d.getCalle() == null || d.getCalle() == "") {
                ErrorCM errorCM = new ErrorCM(linea, d.getCalle(), "Campo obligatorio: Calle");
                errores.add(errorCM);
            }
            if (d.getNumeroExterior() == null || d.getNumeroExterior() == "") {
                ErrorCM errorCM = new ErrorCM(linea, d.getNumeroExterior(), "Campo obligatorio: NumeroExterior");
                errores.add(errorCM);
            }
            if (d.getColonia() == null || d.getColonia().getIdColonia() <= 0) {
                String colVal = (d.getColonia() == null) ? "" : String.valueOf(d.getColonia().getIdColonia());
                ErrorCM errorCM = new ErrorCM(linea, colVal, "Campo obligatorio: IdColonia > 0");
                errores.add(errorCM);
            }
        }

        linea++;
    }

    return errores;
    }

    

}
