package com.digis01.DRosasAguilarDamianNCapasProject.DAO;

import com.digis01.DRosasAguilarDamianNCapasProject.ML.Colonia;
import com.digis01.DRosasAguilarDamianNCapasProject.ML.Estado;
import com.digis01.DRosasAguilarDamianNCapasProject.ML.Result;
import java.sql.ResultSet;
import java.util.ArrayList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.CallableStatementCallback;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository

public class ColoniaDAOImplementation implements IColoniaDAO {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public Result ColoniaByMunicipio(int IdMunicipio) {
        Result result = new Result();

        try {

            jdbcTemplate.execute("CALL ColoniaGetByIdMunicipio(?,?)", (CallableStatementCallback<Boolean>) callableStatement -> {

                callableStatement.registerOutParameter(2, java.sql.Types.REF_CURSOR);

                callableStatement.setInt(1, IdMunicipio);

                callableStatement.execute();

                ResultSet resultSet = (ResultSet) callableStatement.getObject(2);

                result.objects = new ArrayList<>();

                while (resultSet.next()) {

                    Colonia colonia = new Colonia();

                    colonia.setIdColonia(resultSet.getInt("IdColonia"));

                    colonia.setNombre(resultSet.getString("Nombre"));
                    
                    colonia.setCodigoPostal(resultSet.getString("CodigoPostal"));

                    result.objects.add(colonia);

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
