/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.estanciasestrajeroweb.sistema.repositorios;

import com.estanciasestrajeroweb.sistema.entidades.Casa;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CasaRepositorio extends JpaRepository<Casa, String> {
    @Query("SELECT m FROM Casa m WHERE m.familia.id= :id")
    public List<Casa> buscarCasaPorUsuario(@Param("id")String id);
   
}
