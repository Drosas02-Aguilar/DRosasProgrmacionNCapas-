package com.digis01.DRosasAguilarDamianNCapasProject.DAO;

import com.digis01.DRosasAguilarDamianNCapasProject.ML.Result;
import com.digis01.DRosasAguilarDamianNCapasProject.ML.Rol;
import java.sql.ResultSet;
import java.util.ArrayList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.CallableStatementCallback;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class RolDAOImplementation implements IRolDAO {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public Result GetAllRol() {
        Result result = new Result();
        try {
           result.correct = jdbcTemplate.execute("{CALL rolGetall(?)}",
                    (CallableStatementCallback<Boolean>) callableStatement -> {
                        callableStatement.registerOutParameter(1, java.sql.Types.REF_CURSOR);

                        callableStatement.execute();
                        ResultSet resultSet = (ResultSet) callableStatement.getObject(1);

                        result.objects = new ArrayList<>();

                        while (resultSet.next()) {

                            Rol rol = new Rol(
                                    resultSet.getInt("IdRol"),
                                    resultSet.getString("Nombre"));

                            result.objects.add(rol);

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
