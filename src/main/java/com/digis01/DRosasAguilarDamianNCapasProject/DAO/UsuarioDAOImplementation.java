package com.digis01.DRosasAguilarDamianNCapasProject.DAO;

import com.digis01.DRosasAguilarDamianNCapasProject.ML.Usuario;
import com.digis01.DRosasAguilarDamianNCapasProject.ML.Colonia;
import com.digis01.DRosasAguilarDamianNCapasProject.ML.Direccion;
import com.digis01.DRosasAguilarDamianNCapasProject.ML.Estado;
import com.digis01.DRosasAguilarDamianNCapasProject.ML.Municipio;
import com.digis01.DRosasAguilarDamianNCapasProject.ML.Pais;
import com.digis01.DRosasAguilarDamianNCapasProject.ML.Rol;
import org.springframework.jdbc.core.CallableStatementCallback;
import com.digis01.DRosasAguilarDamianNCapasProject.ML.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@Repository
public class UsuarioDAOImplementation implements IUsuarioDAO {

    @Autowired

    private JdbcTemplate jdbcTemplate;

    @Override
public Result GetAll(Usuario usuario) {
    Result result = new Result();

    try {
        jdbcTemplate.execute("{CALL UsuarioDireccionGetAll(?,?,?,?,?)}",
            (CallableStatementCallback<Integer>) callableStatement -> {

                // IN params
                callableStatement.setString(1, usuario.getNombre());
                callableStatement.setString(2, usuario.getApellidopaterno());
                callableStatement.setString(3, usuario.getApellidomaterno());
                int idRol = (usuario.getRol() != null) ? usuario.getRol().getIdRol() : 0;
                callableStatement.setInt(4, idRol);

                // OUT cursor en el 5º parámetro
                callableStatement.registerOutParameter(5, java.sql.Types.REF_CURSOR);
                // Si tu driver no soporta REF_CURSOR, usa: oracle.jdbc.OracleTypes.CURSOR

                callableStatement.execute();
                ResultSet resultSet = (ResultSet) callableStatement.getObject(5);

                // Agrupar por IdUsuario para no duplicar y poder acumular direcciones
                Map<Integer, Usuario> mapa = new HashMap<>();
                result.objects = new ArrayList<>();

                while (resultSet.next()) {
                    int idUsuario = resultSet.getInt("IdUsuario");
                    Usuario usuarioBD = mapa.get(idUsuario);

                    // Si no existe aún en el mapa, créalo y mapéalo
                    if (usuarioBD == null) {
                        usuarioBD = new Usuario();
                        usuarioBD.setIdUsuario(idUsuario);
                        usuarioBD.setNombre(resultSet.getString("NombreUsuario"));
                        usuarioBD.setApellidopaterno(resultSet.getString("ApellidoPaterno"));
                        usuarioBD.setApellidomaterno(resultSet.getString("ApellidoMaterno"));
                        usuarioBD.setUsername(resultSet.getString("UserName"));
                        usuarioBD.setEmail(resultSet.getString("Email"));
                        usuarioBD.setPassword(resultSet.getString("Password"));
                        usuarioBD.setFechaNacimiento(resultSet.getDate("FechaNacimiento"));
                        usuarioBD.setSexo(resultSet.getString("Sexo"));
                        usuarioBD.setTelefono(resultSet.getString("Telefono"));
                        usuarioBD.setCelular(resultSet.getString("Celular"));
                        usuarioBD.setCurp(resultSet.getString("Curp"));
                        usuarioBD.setTiposangre(resultSet.getString("TipoSangre"));
                        usuarioBD.setImagen(resultSet.getString("Imagen"));

                        // Rol
                        int idRolOut = resultSet.getInt("IdRol");
                        if (!resultSet.wasNull() && idRolOut != 0) {
                            Rol rol = new Rol();
                            rol.setIdRol(idRolOut);
                            rol.setNombre(resultSet.getString("NombreRol"));
                            usuarioBD.setRol(rol);
                        } else {
                            usuarioBD.setRol(null);
                        }

                        usuarioBD.setDirecciones(new ArrayList<>());
                        mapa.put(idUsuario, usuarioBD);
                    }

                    // Direcciones (opcionales)
                    int idDireccion = resultSet.getInt("IdDireccion");
                    if (!resultSet.wasNull() && idDireccion != 0) {
                        Direccion direccion = new Direccion();
                        direccion.setIdDireccion(idDireccion);
                        direccion.setCalle(resultSet.getString("Calle"));
                        direccion.setNumeroInterior(resultSet.getString("NumeroInterior"));
                        direccion.setNumeroExterior(resultSet.getString("NumeroExterior"));

                        // Colonia (opcional)
                        int idColonia = resultSet.getInt("IdColonia");
                        if (!resultSet.wasNull() && idColonia != 0) {
                            Colonia colonia = new Colonia();
                            colonia.setIdColonia(idColonia);
                            colonia.setNombre(resultSet.getString("NombreColonia"));
                            colonia.setCodigoPostal(resultSet.getString("CodigoPostal"));

                            // Municipio (opcional)
                            int idMunicipio = resultSet.getInt("IdMunicipio");
                            if (!resultSet.wasNull() && idMunicipio != 0) {
                                Municipio municipio = new Municipio();
                                municipio.setIdMunicipio(idMunicipio);
                                municipio.setNombre(resultSet.getString("NombreMunicipio"));

                                // Estado (opcional)
                                int idEstado = resultSet.getInt("IdEstado");
                                if (!resultSet.wasNull() && idEstado != 0) {
                                    Estado estado = new Estado();
                                    estado.setIdEstado(idEstado);
                                    estado.setNombre(resultSet.getString("NombreEstado"));

                                    // País (opcional)
                                    int idPais = resultSet.getInt("IdPais");
                                    if (!resultSet.wasNull() && idPais != 0) {
                                        Pais pais = new Pais();
                                        pais.setIdPais(idPais);
                                        pais.setNombre(resultSet.getString("NombrePais"));
                                        estado.Pais = pais;
                                    }
                                    municipio.Estado = estado;
                                }
                                colonia.Municipio = municipio;
                            }
                            direccion.Colonia = colonia;
                        }

                        // Agregar dirección al usuario del mapa
                        mapa.get(idUsuario).getDirecciones().add(direccion);
                    }
                }

                // Pasar valores finales al result
                result.objects = new ArrayList<>(mapa.values());
                result.correct = true;
                return 1;
            });

    } catch (Exception ex) {
        result.correct = false;
        result.errorMessage = ex.getLocalizedMessage();
        result.ex = ex;
    }
    return result;
}


    @Override
    public Result DireccionesByIdUsuario(int idUsuario) {
        Result result = new Result();

        try {
            jdbcTemplate.execute("{CALL DireccioonGetByIdUsuario(?,?)}", (CallableStatementCallback<Integer>) callableStatement -> {
                callableStatement.setInt(1, idUsuario);
                callableStatement.registerOutParameter(2, java.sql.Types.REF_CURSOR);

                callableStatement.execute();

                ResultSet resultSet = (ResultSet) callableStatement.getObject(2);

                if (resultSet.next()) {
                    Usuario usuario = new Usuario();

                    usuario.setIdUsuario(idUsuario);
                    usuario.setNombre(resultSet.getString("NombreUsuario"));
                    usuario.setApellidopaterno(resultSet.getString("Apellidopaterno"));
                    usuario.setApellidomaterno(resultSet.getString("Apellidomaterno"));
                    usuario.setUsername(resultSet.getString("Username"));
                    usuario.setEmail(resultSet.getString("Email"));
                    usuario.setPassword(resultSet.getString("Password"));
                    usuario.setTelefono(resultSet.getString("Telefono"));
                    usuario.setCurp(resultSet.getString("Curp"));
                    usuario.setCelular(resultSet.getString("Celular"));
                    usuario.setSexo(resultSet.getString("Sexo"));
                    usuario.setTiposangre(resultSet.getString("Tiposangre"));
                    usuario.setFechaNacimiento(resultSet.getDate("FechaNacimiento"));
                    usuario.Rol = new Rol();
                    usuario.Rol.setIdRol(resultSet.getInt("idrol"));
                    usuario.Rol.setNombre(resultSet.getString("NombreRol"));
                    usuario.setImagen(resultSet.getString("Imagen"));

                    int idDireccion;
                    if ((idDireccion = resultSet.getInt("IdDireccion")) != 0) {
                        usuario.direcciones = new ArrayList<>();

                        do {
                            Direccion direccion = new Direccion();
                            direccion.setIdDireccion(resultSet.getInt("IdDireccion"));
                            direccion.setCalle(resultSet.getString("Calle"));
                            direccion.setNumeroInterior(resultSet.getString("NumeroInterior"));
                            direccion.setNumeroExterior(resultSet.getString("NumeroExterior"));

                            direccion.Colonia = new Colonia();
                            direccion.Colonia.setIdColonia(resultSet.getInt("IdColonia"));
                            direccion.Colonia.setNombre(resultSet.getString("NombreColonia"));
                            direccion.Colonia.setCodigoPostal(resultSet.getString("CodigoPostal"));

                            direccion.Colonia.Municipio = new Municipio();
                            direccion.Colonia.Municipio.setIdMunicipio(resultSet.getInt("IdMunicipio"));
                            direccion.Colonia.Municipio.setNombre(resultSet.getString("NombreMunicipio"));

                            direccion.Colonia.Municipio.Estado = new Estado();
                            direccion.Colonia.Municipio.Estado.setIdEstado(resultSet.getInt("IdEstado"));
                            direccion.Colonia.Municipio.Estado.setNombre(resultSet.getString("NombreEstado"));

                            direccion.Colonia.Municipio.Estado.Pais = new Pais();
                            direccion.Colonia.Municipio.Estado.Pais.setIdPais(resultSet.getInt("IdPais"));
                            direccion.Colonia.Municipio.Estado.Pais.setNombre(resultSet.getString("NombrePais"));

                            usuario.direcciones.add(direccion);

                        } while (resultSet.next());
                    }

                    result.object = usuario;
                }

                result.correct = true;
                return 1;
            });

        } catch (Exception ex) {
            result.correct = false;
            result.errorMessage = ex.getLocalizedMessage();
            result.ex = ex;
        }

        return result;
    }

    @Override
    public Result Add(Usuario usuario) {

        Result result = new Result();

        try {
            result.correct = jdbcTemplate.execute(
                    "CALL USUARIODIRECCIONADD(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)",
                    (CallableStatementCallback<Boolean>) callablestatement -> {

                        // Datos de USUARIO
                        callablestatement.setString(1, usuario.getNombre());
                        callablestatement.setString(2, usuario.getApellidopaterno());
                        callablestatement.setString(3, usuario.getApellidomaterno());
                        callablestatement.setString(4, usuario.getSexo());
                        callablestatement.setString(5, usuario.getTiposangre());
                        callablestatement.setDate(6, new java.sql.Date(usuario.getFechaNacimiento().getTime()));
                        callablestatement.setString(7, usuario.getUsername());
                        callablestatement.setString(8, usuario.getEmail());
                        callablestatement.setString(9, usuario.getPassword());
                        callablestatement.setString(10, usuario.getCurp());
                        callablestatement.setString(11, usuario.getCelular());
                        callablestatement.setString(12, usuario.getTelefono());
                        callablestatement.setInt(13, usuario.getRol().getIdRol());

                        // Nuevo parámetro IMAGEN (CLOB)
                        if (usuario.getImagen() != null) {
                            callablestatement.setString(14, usuario.getImagen()); // Base64
                        } else {
                            callablestatement.setNull(14, java.sql.Types.CLOB);
                        }

                        // Datos de DIRECCIÓN
                        callablestatement.setString(15, usuario.getDirecciones().get(0).getCalle());
                        callablestatement.setString(16, usuario.getDirecciones().get(0).getNumeroInterior());
                        callablestatement.setString(17, usuario.getDirecciones().get(0).getNumeroExterior());
                        callablestatement.setInt(18, usuario.getDirecciones().get(0).getColonia().getIdColonia());

                        int isCorrect = callablestatement.executeUpdate();

                        return isCorrect == -1;
                    });

        } catch (Exception ex) {
            result.correct = false;
            result.errorMessage = ex.getLocalizedMessage();
            result.ex = ex;
        }
        return result;
    }

    @Override
    public Result update(Usuario usuario) {
        Result result = new Result();
        try {
            jdbcTemplate.execute("CALL USUARIOUPDATE(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,?)",
                    (CallableStatementCallback<Boolean>) callablestatement -> {
                        callablestatement.setInt(1, usuario.getIdUsuario());
                        callablestatement.setString(2, usuario.getNombre());
                        callablestatement.setString(3, usuario.getApellidopaterno());
                        callablestatement.setString(4, usuario.getApellidomaterno());
                        callablestatement.setString(5, usuario.getUsername());
                        callablestatement.setString(6, usuario.getEmail());
                        callablestatement.setString(7, usuario.getPassword());
                        callablestatement.setDate(8, new java.sql.Date(usuario.getFechaNacimiento().getTime()));
                        callablestatement.setString(9, usuario.getSexo());
                        callablestatement.setString(10, usuario.getTelefono());
                        callablestatement.setString(11, usuario.getCelular());
                        callablestatement.setString(12, usuario.getCurp());
                        callablestatement.setString(13, usuario.getTiposangre());
                        callablestatement.setInt(14, usuario.Rol.getIdRol());
                        callablestatement.setString(15, usuario.getImagen());

                        callablestatement.execute();

                        result.correct = true;
                        return true;
                    });

        } catch (Exception ex) {
            result.correct = false;
            result.errorMessage = ex.getLocalizedMessage();
            result.ex = ex;
        }
        return result;
    }

    @Override
    public Result deleteById(int IdUsuario) {
        Result result = new Result();
        try {
            jdbcTemplate.execute("CALL UsuarioDeleteById(?)",
                    (CallableStatementCallback<Boolean>) cs -> {
                        cs.setInt(1, IdUsuario);
                        cs.execute();

                        result.correct = true;
                        return true;
                    });

        } catch (Exception ex) {
            result.correct = false;
            result.errorMessage = ex.getLocalizedMessage();
            result.ex = ex;
        }
        return result;
    }

    @Override
    public Result GetByiDUsuario(int IdUsuario) {

        Result result = new Result();

        try {
            jdbcTemplate.execute("{CALL USUARIOGETID(?, ?)}", (CallableStatementCallback<Boolean>) callableStatement -> {
                callableStatement.setInt(1, IdUsuario);
                callableStatement.registerOutParameter(2, java.sql.Types.REF_CURSOR);

                callableStatement.execute();

                try (ResultSet resultSet = (ResultSet) callableStatement.getObject(2)) {
                    if (resultSet.next()) {
                        Usuario usuario = new Usuario();

                        usuario.setIdUsuario(resultSet.getInt("IdUsuario"));
                        usuario.setNombre(resultSet.getString("Nombre"));
                        usuario.setApellidopaterno(resultSet.getString("ApellidoPaterno"));
                        usuario.setApellidomaterno(resultSet.getString("ApellidoMaterno"));
                        usuario.setUsername(resultSet.getString("UserName"));
                        usuario.setEmail(resultSet.getString("Email"));
                        usuario.setPassword(resultSet.getString("Password"));
                        usuario.setFechaNacimiento(resultSet.getDate("FechaNacimiento"));
                        usuario.setSexo(resultSet.getString("Sexo"));
                        usuario.setTelefono(resultSet.getString("Telefono"));
                        usuario.setCelular(resultSet.getString("Celular"));
                        usuario.setCurp(resultSet.getString("Curp"));
                        usuario.setTiposangre(resultSet.getString("TipoSangre"));

                        usuario.Rol = new Rol();
                        usuario.Rol.setIdRol(resultSet.getInt("IdRol"));

                        usuario.setImagen(resultSet.getString("Imagen"));

                        result.object = usuario;
                        result.correct = true;
                    } else {
                        result.correct = false;
                        result.errorMessage = "No se encontró el usuario con Id " + IdUsuario;
                    }
                }

                return null;
            });

        } catch (Exception ex) {
            result.correct = false;
            result.errorMessage = ex.getLocalizedMessage();
            result.ex = ex;
        }

        return result;
    }

}
