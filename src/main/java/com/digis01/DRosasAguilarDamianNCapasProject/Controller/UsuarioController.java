package com.digis01.DRosasAguilarDamianNCapasProject.Controller;

import com.digis01.DRosasAguilarDamianNCapasProject.DAO.ColoniaDAOImplementation;
import com.digis01.DRosasAguilarDamianNCapasProject.DAO.EstadoDAOImplementation;
import com.digis01.DRosasAguilarDamianNCapasProject.DAO.MunicipioDAOImplementation;
import com.digis01.DRosasAguilarDamianNCapasProject.DAO.PaisDAOImplementation;
import com.digis01.DRosasAguilarDamianNCapasProject.DAO.RolDAOImplementation;
import com.digis01.DRosasAguilarDamianNCapasProject.DAO.UsuarioDAOImplementation;
import com.digis01.DRosasAguilarDamianNCapasProject.DAO.DireccionDAOImplementation;
import com.digis01.DRosasAguilarDamianNCapasProject.DAO.UsuarioJPADAOImplementation;
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
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Path;

import jakarta.validation.Valid;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

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
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.ui.Model;
import jakarta.servlet.http.HttpSession;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Controller
@RequestMapping("usuario")
public class UsuarioController {

    @Autowired
    private RolDAOImplementation rolDAOImplementation;
    @Autowired
    private UsuarioDAOImplementation usuarioDAOImplementation;
    
    @Autowired
    private UsuarioJPADAOImplementation usuarioJPADAOImplementation;
    
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
//    Result result = usuarioDAOImplementation.GetAll(new Usuario("", "", "", new Rol()));

        Result result = usuarioJPADAOImplementation.GetAll();

   model.addAttribute("usuarios", result.correct ? result.objects : null);
        Usuario filtro = new Usuario("", "", "", new Rol());
        filtro.getRol().setIdRol(0);
        model.addAttribute("usuariobusqueda", filtro);

        Result rolesRs = rolDAOImplementation.GetAllRol();
        model.addAttribute("roles", rolesRs.correct ? rolesRs.objects : Collections.emptyList());

        return "UsuarioIndex";
    }

    //=========== SEARCH BUSCADO INNDEX =====================================
    @PostMapping
    public String Index(Model model, @ModelAttribute("usuariobusqueda") Usuario usuariobusqueda) {

        if (usuariobusqueda.getRol() == null) {
            usuariobusqueda.setRol(new Rol());
        }

        if (usuariobusqueda.getRol().getIdRol() == 0) {
            usuariobusqueda.getRol().setIdRol(0);
        }
        Result result = usuarioDAOImplementation.GetAll(usuariobusqueda);

        Result rolesRs = rolDAOImplementation.GetAllRol();

        // --- Modelo para la vista ---
        model.addAttribute("usuariobusqueda", usuariobusqueda);
        model.addAttribute("usuarios", result.correct ? result.objects : null);
        model.addAttribute("roles", rolesRs.correct ? rolesRs.objects : Collections.emptyList());

        return "UsuarioIndex";
    }

    // ========================= NUEVO USUARIO (FORM COMPLETO) =========================
    @GetMapping("add")
    public String add(Model model) {
        Result rolesRs = rolDAOImplementation.GetAllRol();
        Result paisesRs = paisDAOImplementation.GetAllPais();

        Usuario usuario = new Usuario();

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
         //   usuarioDAOImplementation.Add(usuario);
           
         Result result = usuarioJPADAOImplementation.Add(usuario);
         return "redirect:/usuario";
        }

    }

    // ========================= EDITAR USUARIO (VISTA DETALLE/EDICIÓN) =========================
    @GetMapping("editarUsuario")
    public String EditarUsuario(@RequestParam("idUsuario") int idUsuario, Model model) {
        //Result result = usuarioDAOImplementation.DireccionesByIdUsuario(idUsuario);
        
        Result result = usuarioJPADAOImplementation.DireccionesByIdUsuario(idUsuario);
        
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
        //Result result = usuarioDAOImplementation.GetByiDUsuario(idUsuario);
        Result result = usuarioJPADAOImplementation.GetByIdUsuario(idUsuario);
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
            //usuarioDAOImplementation.update(usuario);
            Result result = usuarioJPADAOImplementation.Update(usuario);
            return "redirect:/usuario/editarUsuario?idUsuario=" + usuario.getIdUsuario();

        }
    }

    // ========================= AGREGAR DIRECCIÓN A USUARIO (FORM) =========================
    @GetMapping("direccion/add")
    public String DireccionAddForm(@RequestParam("idUsuario") int idUsuario, Model model) {
        //Result rs = usuarioDAOImplementation.DireccionesByIdUsuario(idUsuario);
        Result result = usuarioJPADAOImplementation.DireccionesByIdUsuario(idUsuario);
        
        if (!result.correct || result.object == null) {
            return "Error";
        }

        Usuario usuario = (Usuario) result.object;
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
            //Result result = usuarioDAOImplementation.DireccionesByIdUsuario(idUsuario);
             Result result = usuarioJPADAOImplementation.DireccionesByIdUsuario(idUsuario);
            
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
        //Result result = usuarioDAOImplementation.DireccionesByIdUsuario(idUsuario);
        Result result = usuarioJPADAOImplementation.DireccionesByIdUsuario(idUsuario);
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
            //Result result = usuarioDAOImplementation.DireccionesByIdUsuario(idUsuario);
            Result result = usuarioJPADAOImplementation.DireccionesByIdUsuario(idUsuario);
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
       // usuarioDAOImplementation.deleteById(idUsuario);
        Result result = usuarioJPADAOImplementation.Delete(idUsuario);
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
    public String CargaMasiva() {
        return "CargaMasiva";
    }

    @PostMapping("cargamasiva")
    public String CargaMasiva(@RequestParam("archivo") MultipartFile file, Model model, HttpSession session) {

        model.addAttribute("archivoCorrecto", false);

        String root = System.getProperty("user.dir");
        String rutaArchivo = "/src/main/resources/archivos/";

        // usa segundos correctos 'ss'
        String fechaSubida = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        String rutaFinal = root + rutaArchivo + fechaSubida + "_" + file.getOriginalFilename();

        try {
            // asegúrate de que exista el directorio
            File dir = new File(root + rutaArchivo);
            if (!dir.exists()) {
                dir.mkdirs();
            }

            file.transferTo(new File(rutaFinal));
        } catch (Exception ex) {
            System.out.println("Error al guardar archivo: " + ex.getMessage());
            model.addAttribute("listaErrores", List.of(new ErrorCM(0, "", "No se pudo guardar el archivo.")));
            return "CargaMasiva";
        }

        List<Usuario> usuarios;
        List<ErrorCM> errores;

        // Detecta extensión de forma segura
        String[] parts = file.getOriginalFilename().split("\\.");
        String ext = parts.length > 1 ? parts[parts.length - 1].toLowerCase() : "";

        if ("txt".equals(ext)) {
            usuarios = ProcesarTXT(new File(rutaFinal));
        } else if ("xlsx".equals(ext)) {
            usuarios = ProcesarExcel(new File(rutaFinal));
        } else {
            model.addAttribute("listaErrores", List.of(new ErrorCM(0, ext, "Extensión no soportada")));
            return "CargaMasiva";
        }

        errores = ValidarDatos(usuarios);

        if (errores.isEmpty()) {
            model.addAttribute("listaErrores", errores);
            model.addAttribute("archivoCorrecto", true);

            session.setAttribute("path", rutaFinal);

        } else {
            model.addAttribute("listaErrores", errores);
            model.addAttribute("archivoCorrecto", false);
            // por seguridad, limpia el path si hay errores
            session.removeAttribute("path");
        }

        return "CargaMasiva";
    }

    @GetMapping("cargamasiva/procesar")
    public String CargaMasivaProcesar(HttpSession session) {
        try {
            Object pathObj = session.getAttribute("path");
            if (pathObj == null) {
                // No hay archivo listo para procesar (probablemente no pasaste validación o no se guardó el path)
                return "redirect:/usuario/cargamasiva";
            }

            String ruta = pathObj.toString();
            List<Usuario> usuarios;

            String ext = ruta.contains(".") ? ruta.substring(ruta.lastIndexOf('.') + 1).toLowerCase() : "";

            if ("txt".equals(ext)) {
                usuarios = ProcesarTXT(new File(ruta));
            } else if ("xlsx".equals(ext)) {
                usuarios = ProcesarExcel(new File(ruta));
            } else {
                // extensión inesperada
                return "redirect:/usuario/cargamasiva";
            }

            if (usuarios != null) {
                for (Usuario usuario : usuarios) {
                   // usuarioDAOImplementation.Add(usuario);
            
                   Result result = usuarioJPADAOImplementation.Add(usuario);
                }
            }

            // Limpia la sesión al terminar
            session.removeAttribute("path");

        } catch (Exception ex) {
            System.out.println("Error en procesar: " + ex.getLocalizedMessage());
        }

        return "redirect:/usuario";
    }

    private List<Usuario> ProcesarTXT(File file) {
        try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8))) {

            String linea;
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
                    usuario.setFechaNacimiento(null);
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

                // Dirección
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
        } catch (Exception ex) {
            System.out.println("Error al procesar archivo: " + ex.getMessage());
            return null;
        }
    }

    private List<Usuario> ProcesarExcel(File file) {
        List<Usuario> usuarios = new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        sdf.setLenient(false);

        try (FileInputStream fis = new FileInputStream(file); XSSFWorkbook workbook = new XSSFWorkbook(fis)) {

            Sheet sheet = workbook.getSheetAt(0);
            DataFormatter fmt = new DataFormatter();

            boolean primeraFila = true;
            for (Row row : sheet) {
                if (row == null) {
                    continue;
                }
                if (primeraFila) {
                    primeraFila = false;
                    continue;
                } // salta encabezados

                Usuario usuario = new Usuario();
                usuario.setNombre(fmt.formatCellValue(row.getCell(0)));
                usuario.setApellidopaterno(fmt.formatCellValue(row.getCell(1)));
                usuario.setApellidomaterno(fmt.formatCellValue(row.getCell(2)));
                usuario.setUsername(fmt.formatCellValue(row.getCell(3)));
                usuario.setEmail(fmt.formatCellValue(row.getCell(4)));
                usuario.setPassword(fmt.formatCellValue(row.getCell(5)));

                // Fecha de nacimiento (col 6)
                Cell cFecha = row.getCell(6);
                if (cFecha != null) {
                    try {
                        if (cFecha.getCellType() == CellType.NUMERIC && DateUtil.isCellDateFormatted(cFecha)) {
                            usuario.setFechaNacimiento(cFecha.getDateCellValue());
                        } else {
                            String v = fmt.formatCellValue(cFecha).trim();
                            if (!v.isEmpty()) {
                                usuario.setFechaNacimiento(sdf.parse(v));
                            }
                        }
                    } catch (Exception ignore) {
                        usuario.setFechaNacimiento(null);
                    }
                }

                usuario.setSexo(fmt.formatCellValue(row.getCell(7)));
                usuario.setTelefono(fmt.formatCellValue(row.getCell(8)));
                usuario.setCelular(fmt.formatCellValue(row.getCell(9)));
                usuario.setCurp(fmt.formatCellValue(row.getCell(10)));
                usuario.setTiposangre(fmt.formatCellValue(row.getCell(11)));

                // Rol (col 12)
                Rol rol = new Rol();
                try {
                    Cell cRol = row.getCell(12);
                    int idRol = 0;
                    if (cRol != null) {
                        if (cRol.getCellType() == CellType.NUMERIC) {
                            idRol = (int) cRol.getNumericCellValue();
                        } else {
                            String s = fmt.formatCellValue(cRol).trim();
                            if (!s.isEmpty()) {
                                idRol = Integer.parseInt(s);
                            }
                        }
                    }
                    rol.setIdRol(idRol);
                } catch (Exception ignore) {
                    rol.setIdRol(0);
                }
                usuario.setRol(rol);

                // Dirección (13..16)
                Direccion direccion = new Direccion();
                direccion.setCalle(fmt.formatCellValue(row.getCell(13)));
                direccion.setNumeroInterior(fmt.formatCellValue(row.getCell(14)));
                direccion.setNumeroExterior(fmt.formatCellValue(row.getCell(15))); // <-- corregido

                Colonia colonia = new Colonia();
                try {
                    Cell cCol = row.getCell(16);
                    int idCol = 0;
                    if (cCol != null) {
                        if (cCol.getCellType() == CellType.NUMERIC) {
                            idCol = (int) cCol.getNumericCellValue();
                        } else {
                            String s = fmt.formatCellValue(cCol).trim();
                            if (!s.isEmpty()) {
                                idCol = Integer.parseInt(s);
                            }
                        }
                    }
                    colonia.setIdColonia(idCol);
                } catch (Exception ignore) {
                    colonia.setIdColonia(0);
                }
                direccion.setColonia(colonia); // <-- usar setter, no campo público

                List<Direccion> direcciones = new ArrayList<>();
                direcciones.add(direccion);
                usuario.setDirecciones(direcciones);

                usuarios.add(usuario);
            }

            return usuarios;

        } catch (Exception ex) {
            System.out.println("error: " + ex.getMessage());
            return null;
        }
    }

    private List<ErrorCM> ValidarDatos(List<Usuario> usuarios) {

        List<ErrorCM> errores = new ArrayList<>();
        if (usuarios == null) {
            return errores;

        }

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
                try {
                    Long.parseLong(u.getTelefono());
                } catch (Exception ex) {
                    ErrorCM errorCM = new ErrorCM(linea, u.getTelefono(), "Teléfono debe ser numérico");
                    errores.add(errorCM);
                }
            }
            if (u.getCelular() != null && u.getCelular() != "") {
                try {
                    Long.parseLong(u.getCelular());
                } catch (Exception ex) {
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
