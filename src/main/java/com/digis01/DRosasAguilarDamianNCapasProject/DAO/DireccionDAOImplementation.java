package com.digis01.DRosasAguilarDamianNCapasProject.DAO;

import com.digis01.DRosasAguilarDamianNCapasProject.ML.Colonia;
import com.digis01.DRosasAguilarDamianNCapasProject.ML.Direccion;
import com.digis01.DRosasAguilarDamianNCapasProject.ML.Estado;
import com.digis01.DRosasAguilarDamianNCapasProject.ML.Municipio;
import com.digis01.DRosasAguilarDamianNCapasProject.ML.Pais;
import com.digis01.DRosasAguilarDamianNCapasProject.ML.Result;

import java.sql.ResultSet;
import java.sql.Types;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.CallableStatementCallback;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class DireccionDAOImplementation implements IDireccionDAO {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public Result addToUsuario(int idUsuario, Direccion direccion) {
        Result result = new Result();
        try {
            jdbcTemplate.execute("CALL UsuarioAdddDireccion(?, ?, ?, ?, ?, ?)",
                (CallableStatementCallback<Boolean>) callablestatement -> {
                    callablestatement.setInt(1, idUsuario);
                    callablestatement.setString(2, direccion.getCalle());
                    callablestatement.setString(3, direccion.getNumeroInterior());
                    callablestatement.setString(4, direccion.getNumeroExterior());
                    callablestatement.setInt(5, direccion.getColonia().getIdColonia());
                    callablestatement.registerOutParameter(6, Types.INTEGER);

                    callablestatement.execute();

                    int nuevoId = callablestatement.getInt(6);
                    direccion.setIdDireccion(nuevoId);

                    result.correct = true;
                    result.object = direccion;
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
    public Result updateDireccion(Direccion direccion) {
        Result result = new Result();
        try {
            jdbcTemplate.execute("CALL DIRECCIONUPDATE(?, ?, ?, ?, ?)",
                (CallableStatementCallback<Boolean>) callablestatement -> {
                    callablestatement.setInt(1, direccion.getIdDireccion());
                    callablestatement.setString(2, direccion.getCalle());
                    callablestatement.setString(3, direccion.getNumeroInterior());
                    callablestatement.setString(4, direccion.getNumeroExterior());
                    callablestatement.setInt(5, direccion.getColonia().getIdColonia());

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
    public Result delete(int idDireccion) {
        Result result = new Result();
        try {
            jdbcTemplate.execute("CALL DireccionDeleteById(?)",
                (CallableStatementCallback<Boolean>) callablestatement -> {
                    callablestatement.setInt(1, idDireccion);
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
    public Result getbyid(int idDireccion) {
        Result result = new Result();
        try {
            jdbcTemplate.execute("CALL GetByyIdDireccion(?, ?)",
                (CallableStatementCallback<Boolean>) callablestatement -> {
                    callablestatement.setInt(1, idDireccion);
                    callablestatement.registerOutParameter(2, Types.REF_CURSOR);
                    callablestatement.execute();

                    ResultSet resultSet = (ResultSet) callablestatement.getObject(2);
                    if (resultSet != null && resultSet.next()) {
                        Direccion direccion = new Direccion();
                        direccion.setIdDireccion(resultSet.getInt("IdDireccion"));
                        direccion.setCalle(resultSet.getString("Calle"));
                        direccion.setNumeroInterior(resultSet.getString("NumeroInterior"));
                        direccion.setNumeroExterior(resultSet.getString("NumeroExterior"));

                        Colonia colonia = new Colonia();
                        colonia.setIdColonia(resultSet.getInt("IdColonia"));
                        colonia.setNombre(resultSet.getString("Colonia"));
                        colonia.setCodigoPostal(resultSet.getString("CodigoPostal"));

                        Municipio municipio = new Municipio();
                        municipio.setIdMunicipio(resultSet.getInt("IdMunicipio"));
                        municipio.setNombre(resultSet.getString("Municipio"));

                        Estado estado = new Estado();
                        estado.setIdEstado(resultSet.getInt("IdEstado"));
                        estado.setNombre(resultSet.getString("Estado"));

                        Pais pais = new Pais();
                        pais.setIdPais(resultSet.getInt("IdPais"));
                        pais.setNombre(resultSet.getString("Pais"));

                        estado.setPais(pais);
                        municipio.setEstado(estado);
                        colonia.setMunicipio(municipio);
                        direccion.setColonia(colonia);

                        result.object = direccion;
                        result.correct = true;
                    } else {
                        result.correct = false;
                    }
                    return true;
                });
        } catch (Exception ex) {
            result.correct = false;
            result.errorMessage = ex.getLocalizedMessage();
            result.ex = ex;
        }
        return result;
    }
}
