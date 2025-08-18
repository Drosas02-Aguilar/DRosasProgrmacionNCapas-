package com.digis01.DRosasAguilarDamianNCapasProject.DAO;

import com.digis01.DRosasAguilarDamianNCapasProject.ML.Result;
import com.digis01.DRosasAguilarDamianNCapasProject.ML.Usuario;
public interface IUsuarioDAO {
    
Result GetAll();
Result DireccionesByIdUsuario(int idUsuario);
Result Add(Usuario usuario); 
Result update(Usuario usuario);
Result deleteById(int IdUsuario);
Result GetByiDUsuario(int IdUsuario);

}
