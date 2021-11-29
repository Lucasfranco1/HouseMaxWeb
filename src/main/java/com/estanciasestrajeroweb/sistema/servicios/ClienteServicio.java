/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.estanciasestrajeroweb.sistema.servicios;

import com.estanciasestrajeroweb.sistema.entidades.Cliente;
import com.estanciasestrajeroweb.sistema.entidades.Foto;
import com.estanciasestrajeroweb.sistema.entidades.Usuario;
import com.estanciasestrajeroweb.sistema.enumeraciones.TipoUsuario;
import com.estanciasestrajeroweb.sistema.excepciones.ErrorService;
import com.estanciasestrajeroweb.sistema.repositorios.ClienteRepositorio;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
public class ClienteServicio {

    @Autowired
    private ClienteRepositorio ClR;

    @Autowired
    private FotoServicio fS;

    @Autowired
    private UsuarioServicio uS;

    Integer count = 0;

    /*
     private String nombre;
    
    private String calle;
    
    private Integer numero;
    
    private String codPostal;
    
    private String ciudad;
    
    private String pais;
    
    private String email;
     */
    @Transactional
    public void crearCliente(MultipartFile archivo, String nombre, String calle, Integer numero, String codPostal,
            String ciudad, String pais, String email, String idUsuario) throws ErrorService {
        validar(nombre, calle, numero, codPostal, ciudad, pais, email);
        Cliente cliente = new Cliente();
        cliente.setNombre(nombre);
        cliente.setCalle(calle);
        cliente.setNumero(numero);
        cliente.setCodPostal(codPostal);
        cliente.setCiudad(ciudad);
        cliente.setPais(pais);
        Foto foto = fS.guardar(archivo);
        cliente.setEmail(email);

        Usuario usuario = uS.buscarUsuarioPorId(idUsuario);
        if (usuario != null) {
            usuario.setTipoUsuario(TipoUsuario.CLIENTE);
            count++;
            cliente.setUsuario(usuario);
            if (count == 2) {
                throw new ErrorService("Sólo puedes crear un cliente");
            }
        } else {
            throw new ErrorService("Ese usuario no existe");
        }

        ClR.save(cliente);
    }

    @Transactional
    public void modificarCliente(String id, MultipartFile archivo, String nombre, String calle, Integer numero, String codPostal,
            String ciudad, String pais, String email, String idUsuario) throws ErrorService {
        validar(nombre, calle, numero, codPostal, ciudad, pais, email);
        Optional<Cliente> resp = ClR.findById(id);
        if (resp.isPresent()) {
            Cliente cliente = resp.get();
            cliente.setNombre(nombre);
            cliente.setCalle(calle);
            cliente.setNumero(numero);
            cliente.setCodPostal(codPostal);
            cliente.setCiudad(ciudad);
            cliente.setPais(pais);
            cliente.setEmail(email);

            String idFoto = null;
            if (cliente.getFoto() != null) {
                idFoto = cliente.getFoto().getId();
            }
            Foto foto = fS.actualizar(idFoto, archivo);
            cliente.setFoto(foto);

            ClR.save(cliente);
        }
    }

    public void eliminarCliente(String id) throws ErrorService {
        Optional<Cliente> cliente = ClR.findById(id);
        if (cliente.isPresent()) {
            ClR.deleteById(id);
        } else {
            throw new ErrorService("Error al eliminar cliente");
        }
    }

    private void validar(String nombre, String calle, Integer numero, String codPostal, String ciudad, String pais, String email) throws ErrorService {
        if (nombre == null || nombre.isEmpty()) {
            throw new ErrorService("El nombre no puede ser nulo, ni estar vacío");
        }
        if (calle == null || calle.isEmpty()) {
            throw new ErrorService("La calle no puede ser nula, ni estar vacía");
        }
        if (numero == null || numero < 0) {
            throw new ErrorService("El número no puede ser nulo, ni menor a 0");
        }
        if (codPostal == null || codPostal.isEmpty()) {
            throw new ErrorService("El código postal no puede ser nulo, ni estar vacío");
        }
        if (ciudad == null || ciudad.isEmpty()) {
            throw new ErrorService("La ciudad no puede ser nula, ni estar vacía");
        }
        if (pais == null || pais.isEmpty()) {
            throw new ErrorService("El país no puede ser nulo, ni estar vacío.");
        }
        if (email == null || email.isEmpty()) {
            throw new ErrorService("El mail no puede ser nulo, ni estar vacío");
        }
    }

    public Cliente buscarClientePorId(String id) throws ErrorService {
        Optional<Cliente> resp = ClR.findById(id);
        if (resp.isPresent()) {
            return ClR.findById(id).get();
        } else {
            throw new ErrorService("No se encontró el id.");
        }
    }

}
