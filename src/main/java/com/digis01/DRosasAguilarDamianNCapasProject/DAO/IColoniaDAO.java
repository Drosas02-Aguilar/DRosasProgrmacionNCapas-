package com.digis01.DRosasAguilarDamianNCapasProject.DAO;

import com.digis01.DRosasAguilarDamianNCapasProject.ML.Result;

public interface IColoniaDAO {
    Result ColoniaByMunicipio(int IdMunicipio);
}
