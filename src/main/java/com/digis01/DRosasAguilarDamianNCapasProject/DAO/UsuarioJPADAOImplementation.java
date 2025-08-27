package com.digis01.DRosasAguilarDamianNCapasProject.DAO;

import com.digis01.DRosasAguilarDamianNCapasProject.ML.Result;
import com.digis01.DRosasAguilarDamianNCapasProject.JPA.Usuario;
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
            TypedQuery<Usuario> queryUsuario = entityManager.createQuery(
                    "FROM Usuario ORDER BY  IdUsuario", Usuario.class);
            List<Usuario> usuarios = queryUsuario.getResultList();
            
            result.objects = new ArrayList();
            
            for (Usuario usuario : usuarios){
            

                result.objects.add(new com.digis01.DRosasAguilarDamianNCapasProject.ML.Usuario(usuario));
                
            }
                        result.correct = true;
            

            System.out.println("");

        } catch (Exception ex) {
            result.correct = false;
            result.errorMessage = ex.getMessage();
            result.ex = ex;
        }

        return result;
    }
}
