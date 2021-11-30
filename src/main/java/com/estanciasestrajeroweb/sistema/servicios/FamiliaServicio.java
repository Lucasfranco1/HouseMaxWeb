/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.estanciasestrajeroweb.sistema.servicios;

import com.estanciasestrajeroweb.sistema.entidades.Casa;
import com.estanciasestrajeroweb.sistema.entidades.Familia;
import com.estanciasestrajeroweb.sistema.entidades.Foto;
import com.estanciasestrajeroweb.sistema.entidades.Usuario;
import com.estanciasestrajeroweb.sistema.enumeraciones.TipoUsuario;
import com.estanciasestrajeroweb.sistema.excepciones.ErrorService;
import com.estanciasestrajeroweb.sistema.repositorios.FamiliaRepositorio;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
public class FamiliaServicio {

    @Autowired
    private FamiliaRepositorio fR;

    @Autowired
    private CasaServicio cS;

    @Autowired
    private FotoServicio fS;

    @Autowired
    private UsuarioServicio uS;

    Integer count=0;
    /*
    private String nombre;
    
    private Integer edadMin;
    
    private Integer edadMax;
    
    private Integer numHijos;
    
    private String email;
    @OneToOne
    private Foto foto;
    
    @OneToOne
    private Casa casa;

     */
    @Transactional
    public void crearFamilia(MultipartFile archivo, String nombre, Integer edadMin, Integer edadMax, Integer numHijos, String email,
            String idUsuario) throws ErrorService {
        
        validar(nombre, edadMin, edadMax, numHijos, email);
        Familia familia = new Familia();

        familia.setNombre(nombre);
        familia.setEdadMin(edadMin);
        familia.setEdadMax(edadMax);
        familia.setNumHijos(numHijos);
        familia.setEmail(email);
        Foto foto = fS.guardar(archivo);
        familia.setFoto(foto);

        Usuario usuario = uS.buscarUsuarioPorId(idUsuario);
        if (usuario != null) {
            usuario.setTipoUsuario(TipoUsuario.FAMILIA);
            count++;
            familia.setUsuario(usuario);
            if(count==2){
                throw new ErrorService("Sólo puedes crear una familia");
            }
        } else {
            throw new ErrorService("Ese usuario no existe");
        }

       

        fR.save(familia);

    }

    @Transactional
    public void modificarFamilia(String id, MultipartFile archivo, String nombre, Integer edadMin, Integer edadMax, Integer numHijos, String email, String idUsuario) throws ErrorService {
        validar(nombre, edadMin, edadMax, numHijos, email);
        Optional<Familia> respuesta = fR.findById(id);
        if (respuesta.isPresent()) {
            Familia familia = respuesta.get();
//            if (familia.getUsuario().getId().equals(idUsuario)) {
                familia.setNombre(nombre);
                familia.setEdadMin(edadMin);
                familia.setEdadMax(edadMax);
                familia.setNumHijos(numHijos);
                familia.setEmail(email);

                String idFoto = null;
                if (familia.getFoto() != null) {
                    idFoto = familia.getFoto().getId();
                }
                Foto foto = fS.actualizar(idFoto, archivo);
                familia.setFoto(foto);

                fR.save(familia);
//            } else {
//                throw new ErrorService("No tienes permisos suficientes para realizar la operación");
//            }

        } else {
            throw new ErrorService("No exite una familia con el id solicitado.");
        }

    }

    @Transactional
    public void eliminarFamilia(String id) throws ErrorService {
        Optional<Familia> resp = fR.findById(id);
        if (resp.isPresent()) {
            fR.deleteById(id);
        } else {
            throw new ErrorService("Error al eliminar familia");
        }
    }

    private void validar(String nombre, Integer edadMin, Integer edadMax, Integer numHijos, String email) throws ErrorService {
        if (nombre == null || nombre.isEmpty()) {
            throw new ErrorService("El nombre no puede ser nulo, ni estar vacío");
        }
        if (edadMin == null || edadMin <= 0 || edadMin > edadMax) {
            throw new ErrorService("La edad mínima no puede ser nula, ni ser menor o igual a 0, tampoco ser mayor que la edad máxima");
        }
        if (edadMax == null || edadMax <= 0 || edadMax < edadMin) {
            throw new ErrorService("La edad máxima no puede ser nula, ni ser menor o igual a 0, tampoco la ser menor que la edad mínima");
        }
        if (numHijos == null) {
            throw new ErrorService("El número de hijos no puede ser nulo");
        }
        if (email == null || email.isEmpty()) {
            throw new ErrorService("El mail no puede ser nulo, ni estar vacío");
        }

    }

    public Familia buscarFamiliaPorId(String id) throws ErrorService {
        Optional<Familia> resp = fR.findById(id);
        if (resp.isPresent()) {
            return fR.findById(id).get();
        } else {
            throw new ErrorService("No se encontró el id.");
        }
    }
    @Transactional
    public List<Familia> listarFamiliasPorUsuario(String id){
        return fR.buscarFamiliaPorUsuario(id);
    }
     @Transactional
    public Familia traerFamiliasPorUsuario(String id){
        return fR.buscarFamiliaUsuario(id);
    }
    @Transactional
    public void eliminar(String idUsuario, String idFamilia) throws ErrorService{
        Optional<Familia> respuesta=fR.findById(idFamilia);
        if(respuesta.isPresent()){
            Familia familia=respuesta.get();
             if(familia.getUsuario().getId().equals(idUsuario)){                 
                 fR.deleteById(idFamilia);
             }
        }else{
            throw new ErrorService("No exite una mascota con el id solicitado.");
        }     
    }
    public void buscarUsuario(String id) throws ErrorService {
        Usuario resp = uS.buscarUsuarioPorId(id);
       
    }
}
