/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.estanciasestrajeroweb.sistema.servicios;

import com.estanciasestrajeroweb.sistema.entidades.Foto;
import com.estanciasestrajeroweb.sistema.excepciones.ErrorService;
import com.estanciasestrajeroweb.sistema.repositorios.FotoRepositorio;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class FotoServicio {

    @Autowired
    private FotoRepositorio fR;

    public Foto guardar(MultipartFile archivo)throws ErrorService {
        if (archivo != null && !archivo.isEmpty()) {
            try {
                Foto foto = new Foto();
                foto.setMime(archivo.getContentType());
                foto.setNombre(archivo.getName());
                foto.setContenido(archivo.getBytes());
                return fR.save(foto);
            } catch (IOException e) {
                System.err.println(e.getMessage());

            }

        }
        return null;

    }
    public Foto actualizar(String idFoto, MultipartFile archivo)throws ErrorService{
        if(archivo !=null){
            try {
          
                Foto foto=new Foto();
                if(idFoto!=null){
                   Optional<Foto> respuesta = fR.findById(idFoto);
                   if(respuesta.isPresent()){
                       foto=respuesta.get();
                   }
                   
                }
                
                foto.setMime(archivo.getContentType());
                foto.setNombre(archivo.getName());
                foto.setContenido(archivo.getBytes());
                
                return fR.save(foto);
            } catch (Exception e) {
                 System.err.println(e.getMessage());
            }
        }
        
        return null;
        
    }
    
}
