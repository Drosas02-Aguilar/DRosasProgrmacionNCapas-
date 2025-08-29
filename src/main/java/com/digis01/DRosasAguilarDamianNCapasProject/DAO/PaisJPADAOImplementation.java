package com.digis01.DRosasAguilarDamianNCapasProject.DAO;

import com.digis01.DRosasAguilarDamianNCapasProject.JPA.Pais;
import com.digis01.DRosasAguilarDamianNCapasProject.ML.Result;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class PaisJPADAOImplementation implements IPaisJPADAO {

    @Autowired
    private EntityManager entityManager;

    @Override
    public Result GetAllPais() {

        Result result = new Result();

        try {

            TypedQuery<Pais> queryPais = entityManager.createQuery(
                    "FROM Pais  ORDER BY IdPais", Pais.class);
            
            List<Pais> paises = queryPais.getResultList();
            
            result.objects = new ArrayList();
            
            for (Pais pais : paises){
                
                result.objects.add(new com.digis01.DRosasAguilarDamianNCapasProject.ML.Pais(pais));  
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
