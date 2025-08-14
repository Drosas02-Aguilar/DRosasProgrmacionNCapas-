package com.digis01.DRosasAguilarDamianNCapasProject.DAO;

import com.digis01.DRosasAguilarDamianNCapasProject.ML.Estado;
import com.digis01.DRosasAguilarDamianNCapasProject.ML.Municipio;
import com.digis01.DRosasAguilarDamianNCapasProject.ML.Result;
import java.sql.ResultSet;
import java.util.ArrayList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.CallableStatementCallback;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository

public class MunicipioDAOImplementation implements IMunicipioDAO {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public Result MunicipioByidEstado(int IdEstado) {

        Result result = new Result();

        try {
            jdbcTemplate.execute("CALL MunicipioGetByIdEstado(?,?)", (CallableStatementCallback<Boolean>) callableStatement -> {

                callableStatement.registerOutParameter(2, java.sql.Types.REF_CURSOR);

                callableStatement.setInt(1, IdEstado);

                callableStatement.execute();

                ResultSet resultSet = (ResultSet) callableStatement.getObject(2);

                result.objects = new ArrayList<>();

                while (resultSet.next()) {

                    Municipio municipio = new Municipio();

                    municipio.setIdMunicipio(resultSet.getInt("IdMunicipio"));

                    municipio.setNombre(resultSet.getString("Nombre"));

                    result.objects.add(municipio);

                }

                result.correct = true;

                return true;

            });
        } catch (Exception ex) {

            result.correct = true;
            result.errorMessage = ex.getLocalizedMessage();
            result.ex = ex;

        }

        return result;

    }

}
