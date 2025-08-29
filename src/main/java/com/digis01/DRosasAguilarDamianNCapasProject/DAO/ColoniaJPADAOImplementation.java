package com.digis01.DRosasAguilarDamianNCapasProject.DAO;

import com.digis01.DRosasAguilarDamianNCapasProject.JPA.Colonia;
import com.digis01.DRosasAguilarDamianNCapasProject.ML.Result;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository

public class ColoniaJPADAOImplementation implements IColoniaJPADAO {

    @Autowired
    private EntityManager entityManager;

    @Override
    public Result ColoniaByMunicipio(int IdMunicipio) {

        Result result = new Result();

        try {
            
            TypedQuery<com.digis01.DRosasAguilarDamianNCapasProject.JPA.Colonia> queryColonia = entityManager.createQuery(
   
            
    "FROM Colonia c WHERE c.Municipio.IdMunicipio = :idMunicipio ORDER BY c.Nombre",
                    com.digis01.DRosasAguilarDamianNCapasProject.JPA.Colonia.class).setParameter("idMunicipio", IdMunicipio);
            
            List<com.digis01.DRosasAguilarDamianNCapasProject.JPA.Colonia> colonias = queryColonia.getResultList();
            
            result.objects = new ArrayList<>();
            
            for (Colonia colonia : colonias){
            
                result.objects.add(new com.digis01.DRosasAguilarDamianNCapasProject.ML.Colonia(colonia));
            
            }
                result.correct = true;
            
            
        } catch (Exception ex) {
            result.correct = false;
            result.errorMessage = ex.getLocalizedMessage();
            result.ex = ex;
        }
        return result;
    }

}
