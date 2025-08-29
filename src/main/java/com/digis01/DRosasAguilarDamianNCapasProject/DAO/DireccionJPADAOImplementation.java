
package com.digis01.DRosasAguilarDamianNCapasProject.DAO;

import com.digis01.DRosasAguilarDamianNCapasProject.JPA.Colonia;
import com.digis01.DRosasAguilarDamianNCapasProject.JPA.Direccion;
import com.digis01.DRosasAguilarDamianNCapasProject.JPA.Usuario;
import com.digis01.DRosasAguilarDamianNCapasProject.ML.Result;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;






@Repository
public class DireccionJPADAOImplementation implements IDireccionJPADAO {

    @Autowired
    private EntityManager entityManager;
    
    @Transactional
    @Override
  public Result AddDireccion(int IdUsuario, com.digis01.DRosasAguilarDamianNCapasProject.ML.Direccion direccionML) {
    Result result = new Result();
    try {
        Direccion direccionJPA = new Direccion(direccionML);

        Usuario usuario = entityManager.find(Usuario.class, IdUsuario);
        
        direccionJPA.setUsuario(usuario);
        direccionJPA.setColonia(entityManager.find(Colonia.class, direccionML.getColonia().getIdColonia()));

        entityManager.persist(direccionJPA);
        entityManager.flush();

        direccionML.setIdDireccion(direccionJPA.getIdDireccion());
        result.object = direccionML;
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
    public Result GetByIdDireccion(int IdDireccion) {
        Result result = new Result();
        
        try{
        
        Direccion direccionJPA = entityManager.find(Direccion.class, IdDireccion);
        
        if (direccionJPA != null) {
                result.object = new com.digis01.DRosasAguilarDamianNCapasProject.ML.Direccion(direccionJPA);
                result.correct = true;
            } else {
                result.correct = false;
                result.errorMessage = "Usuario no encontrado con Id: " + IdDireccion;
            }
        
        }catch (Exception ex){
        
            result.correct = false;
            result.errorMessage = ex.getLocalizedMessage();
            result.ex = ex;
        }
        
        return result;

    }



    @Transactional
    @Override
    public Result Delete(int IdDireccion) {
        Result result = new Result();
        
        try{
            
            Direccion direccionJPA =  entityManager.find(Direccion.class, 
                    IdDireccion);
            entityManager.remove(direccionJPA);
            
            result.correct = true;
            
        }catch(Exception ex){
            result.correct = false;
            result.errorMessage = ex.getLocalizedMessage();
            result.ex = ex;
        }
      return result;
    }

    @Transactional
@Override
public Result Update(com.digis01.DRosasAguilarDamianNCapasProject.ML.Direccion direccionML) {
    Result result = new Result();
    try {
        com.digis01.DRosasAguilarDamianNCapasProject.JPA.Direccion direccionJPA =
            new com.digis01.DRosasAguilarDamianNCapasProject.JPA.Direccion(direccionML);

        entityManager.merge(direccionJPA);
        // opcional, si necesitas confirmar que persista antes de salir:
         entityManager.flush();

        result.correct = true;
    } catch (Exception ex) {
        result.correct = false;
        result.errorMessage = ex.getLocalizedMessage();
        result.ex = ex;
    }
    return result;
}

    
}
