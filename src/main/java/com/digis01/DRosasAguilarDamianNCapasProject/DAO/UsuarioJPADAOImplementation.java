package com.digis01.DRosasAguilarDamianNCapasProject.DAO;

import com.digis01.DRosasAguilarDamianNCapasProject.ML.Result;
import com.digis01.DRosasAguilarDamianNCapasProject.ML.Usuario;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 *
 * @author digis
 */

    @Repository
public class UsuarioJPADAOImplementation implements IUsuarioJPADAO {

    @Autowired
    private EntityManager entityManager;

    @Override
    public Result GetAll() {
        Result result = new Result();

        try {
            // JPQL: consulta sobre la entidad Usuario
            TypedQuery<Usuario> query = entityManager.createQuery(
                    "SELECT u FROM Usuario u JOIN FETCH u.Rol r LEFT JOIN FETCH u.direcciones d LEFT JOIN FETCH d.Colonia c LEFT JOIN FETCH c.Municipio m LEFT JOIN FETCH m.Estado e LEFT JOIN FETCH e.Pais p",
                    Usuario.class);

            List<Usuario> usuarios = query.getResultList();

            result.objects = new ArrayList<>(usuarios);
            result.correct = true;

        } catch (Exception ex) {
            result.correct = false;
            result.errorMessage = ex.getMessage();
            result.ex = ex;
        }

        return result;
    }
}


