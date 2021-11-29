/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.estanciasestrajeroweb.sistema.repositorios;

import com.estanciasestrajeroweb.sistema.entidades.Usuario;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UsuarioRepositorio extends JpaRepository<Usuario, String>{
    
    @Query("SELECT a FROM Usuario a WHERE a.alias= :nombre")
    public Usuario buscarPorAlias(@Param("nombre") String nombre);
    
    @Query("SELECT c FROM Usuario c WHERE c.email = :email")
    public Usuario buscarPorMail(@Param("email") String mail);
    
    @Query("SELECT c FROM Usuario c WHERE c.id = :id")
    public List<Usuario> buscarPorId(@Param("id") String id);
    
}
