package com.digis01.DRosasAguilarDamianNCapasProject.DAO;

import com.digis01.DRosasAguilarDamianNCapasProject.JPA.Municipio;
import com.digis01.DRosasAguilarDamianNCapasProject.ML.Result;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class MunicipioJPADAOImplementation implements IMunicipioJPADAO {

    @Autowired
    private EntityManager entityManager;

    @Transactional
    @Override
    public Result MunicipioByidEstado(int IdEstado) {
        Result result = new Result();
        try {
            TypedQuery<com.digis01.DRosasAguilarDamianNCapasProject.JPA.Municipio> queryMunicipio
                    = entityManager.createQuery("FROM Municipio m WHERE m.Estado.IdEstado = :idEstado ORDER BY m.Nombre",
                            com.digis01.DRosasAguilarDamianNCapasProject.JPA.Municipio.class).setParameter("idEstado", IdEstado);
            
            List<com.digis01.DRosasAguilarDamianNCapasProject.JPA.Municipio> municipios = queryMunicipio.getResultList();
            
            result.objects = new ArrayList<>();
            
            for (Municipio municipio : municipios){
            
                result.objects.add(new com.digis01.DRosasAguilarDamianNCapasProject.ML.Municipio(municipio));
            
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
