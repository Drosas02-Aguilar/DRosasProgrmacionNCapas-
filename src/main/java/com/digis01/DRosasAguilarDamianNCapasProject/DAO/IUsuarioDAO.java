package com.digis01.DRosasAguilarDamianNCapasProject.DAO;

import com.digis01.DRosasAguilarDamianNCapasProject.ML.Result;
import com.digis01.DRosasAguilarDamianNCapasProject.ML.Usuario;
public interface IUsuarioDAO {
    
Result GetAll();// metodo abstracto, es decir, no lleva implementación
Result DireccionesByIdUsuario(int idUsuario);
Result Add(Usuario usuario); 

}
