
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

        // aquí haces el find
        Usuario usuario = entityManager.find(Usuario.class, IdUsuario);
        Colonia colonia = entityManager.find(Colonia.class, direccionML.getColonia().getIdColonia());

        direccionJPA.setUsuario(usuario);
        direccionJPA.setColonia(colonia);

        entityManager.persist(direccionJPA);
        entityManager.flush();

        direccionML.setIdDireccion(direccionJPA.getIdDireccion());
        result.object = direccionML;
        result.correct = true;
        
       // direccionJPA.setUsuario(usuario);
     //   direccionJPA.setColonia(entityManager.find(Colonia.class, direccionML.getColonia().getIdColonia()));

       // entityManager.persist(direccionJPA);
      //  entityManager.flush();

      //  direccionML.setIdDireccion(direccionJPA.getIdDireccion());
    //    result.object = direccionML;
      //  result.correct = true;

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
        // 1) Cargar la dirección actual (managed)
        com.digis01.DRosasAguilarDamianNCapasProject.JPA.Direccion direccionBD =
            entityManager.find(
                com.digis01.DRosasAguilarDamianNCapasProject.JPA.Direccion.class,
                direccionML.getIdDireccion()
            );

        if (direccionBD == null) {
            result.correct = false;
            result.errorMessage = "No existe la dirección con Id " + direccionML.getIdDireccion();
            return result;
        }

        // 2) Construir el detached desde ML (campos simples)
        com.digis01.DRosasAguilarDamianNCapasProject.JPA.Direccion direccionJPA =
            new com.digis01.DRosasAguilarDamianNCapasProject.JPA.Direccion(direccionML);

        // 3) Forzar UPDATE, no INSERT
        direccionJPA.setIdDireccion(direccionML.getIdDireccion());

        // 4) Asociaciones:
        //    - Usuario: conservar SIEMPRE el que ya tiene en BD (no dependas del ML)
        direccionJPA.setUsuario(direccionBD.getUsuario());

        //    - Colonia: si viene en ML, referenciar; si no, conservar la actual
        if (direccionML.getColonia() != null && direccionML.getColonia().getIdColonia() > 0) {
            direccionJPA.setColonia(
                entityManager.getReference(
                    com.digis01.DRosasAguilarDamianNCapasProject.JPA.Colonia.class,
                    direccionML.getColonia().getIdColonia()
                )
            );
        } else {
            direccionJPA.setColonia(direccionBD.getColonia());
        }

        // 5) Persistir
        entityManager.merge(direccionJPA);
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
