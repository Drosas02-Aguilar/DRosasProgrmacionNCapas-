package com.digis01.DRosasAguilarDamianNCapasProject.DAO;

import com.digis01.DRosasAguilarDamianNCapasProject.ML.Result;
import com.digis01.DRosasAguilarDamianNCapasProject.JPA.Usuario;
import com.digis01.DRosasAguilarDamianNCapasProject.JPA.Direccion;
import com.digis01.DRosasAguilarDamianNCapasProject.JPA.Rol;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;


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

            for (Usuario usuario : usuarios) {

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

    @Transactional
    @Override
    public Result Add(com.digis01.DRosasAguilarDamianNCapasProject.ML.Usuario usuarioML) {

        Result result = new Result();

        try {

            Usuario usuarioJPA = new Usuario(usuarioML);

            entityManager.persist(usuarioJPA);

            result.correct = true;

        } catch (Exception ex) {
            result.correct = false;
            result.errorMessage = ex.getLocalizedMessage();
            result.ex = ex;
        }
        return result;
    }

    @Transactional
    @Override
    public Result Update(com.digis01.DRosasAguilarDamianNCapasProject.ML.Usuario usuarioML) {

        Result result = new Result();

        try {
        Usuario usuarioBD = entityManager.find(Usuario.class, usuarioML.getIdUsuario());

        // 2) Mapear campos simples desde ML
        Usuario usuarioJPA = new Usuario(usuarioML);

        // 3) Asegurar que es UPDATE y no INSERT
        usuarioJPA.setIdUsuario(usuarioML.getIdUsuario());

        if (usuarioBD != null && usuarioBD.direcciones != null) {
            usuarioJPA.direcciones = usuarioBD.direcciones;
        }

        entityManager.merge(usuarioJPA);
    ;

        result.correct = true;
        } catch (Exception ex) {
            result.correct = false;
            result.errorMessage = ex.getLocalizedMessage();
            result.ex = ex;
        }
        return result;

    }

    @Transactional
    @Override
    public Result GetByIdUsuario(int IdUsuario) {
        Result result = new Result();

        try {
            Usuario usuarioJPA = entityManager.find(Usuario.class, IdUsuario);

            if (usuarioJPA != null) {
                result.object = new com.digis01.DRosasAguilarDamianNCapasProject.ML.Usuario(usuarioJPA);
                result.correct = true;
            } else {
                result.correct = false;
                result.errorMessage = "Usuario no encontrado con Id: " + IdUsuario;
            }

        } catch (Exception ex) {
            result.correct = false;
            result.errorMessage = ex.getMessage();
            result.ex = ex;
        }
        return result;
    }

    @Transactional
    @Override
    public Result DireccionesByIdUsuario(int IdUsuario) {
        Result result = new Result();

        try {

            Usuario usuario = entityManager.find(Usuario.class, IdUsuario);
            /*convertir a ML*/
            com.digis01.DRosasAguilarDamianNCapasProject.ML.Usuario usuarioML
                    = new com.digis01.DRosasAguilarDamianNCapasProject.ML.Usuario(usuario);

            result.object = new com.digis01.DRosasAguilarDamianNCapasProject.ML.Usuario(usuario);
            result.correct = true;

        } catch (Exception ex) {
            result.correct = false;
            result.errorMessage = ex.getMessage();
            result.ex = ex;
        }

        return result;
    }

    @Transactional
    @Override
    public Result Delete(int IdUsuario) {
        Result result = new Result();

        try {

            Usuario usuarioJPA = entityManager.find(Usuario.class,
                    IdUsuario);
            entityManager.remove(usuarioJPA);

            result.correct = true;

        } catch (Exception ex) {
            result.correct = false;
            result.errorMessage = ex.getLocalizedMessage();
            result.ex = ex;
        }

        return result;
    }
}
