/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.estanciasestrajeroweb.sistema.servicios;

import com.estanciasestrajeroweb.sistema.entidades.Cliente;
import com.estanciasestrajeroweb.sistema.entidades.Familia;
import com.estanciasestrajeroweb.sistema.entidades.Foto;
import com.estanciasestrajeroweb.sistema.entidades.Usuario;
import com.estanciasestrajeroweb.sistema.enumeraciones.TipoUsuario;
import com.estanciasestrajeroweb.sistema.excepciones.ErrorService;
import com.estanciasestrajeroweb.sistema.repositorios.UsuarioRepositorio;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;


@Service
public class UsuarioServicio implements UserDetailsService{

    @Autowired
    private UsuarioRepositorio uR;

    @Autowired
    private CasaServicio cS;

    @Autowired
    private ClienteServicio clS;

    @Autowired
    private FotoServicio fS;

    @Autowired
    private FamiliaServicio famS;

   
    /*
    private String alias;
    
    private String email;
    
    private String clave;
    
    @OneToOne
    private Cliente cliente;
    
    @OneToOne
    private Familia familia;
    
    @Temporal(TemporalType.DATE)
    private Date fechaAlta;
    
    @Temporal(TemporalType.DATE)
    private Date fechaBaja;
    
    @Enumerated(EnumType.STRING)
    private TipoUsuario tipoUsuario;
    
     @OneToOne
    private Foto foto;
     */
    @Transactional
    public void registrarUsuario(MultipartFile archivo, String alias, String email, String clave) throws ErrorService {

        validar(alias, email, clave);

        Usuario usuario = new Usuario();
        usuario.setAlias(alias);
        usuario.setEmail(email);
        String encriptada=new BCryptPasswordEncoder().encode(clave);
        usuario.setClave(encriptada);
        usuario.setFechaAlta(new Date());
        usuario.setTipoUsuario(TipoUsuario.USUARIO);

        Foto foto = fS.guardar(archivo);
        usuario.setFoto(foto);
        uR.save(usuario);
       
        

    }
    @Transactional
    public void modificarUsuario(String id, MultipartFile archivo, String alias, String email, String clave) throws ErrorService {
        validarActualizar(alias, email, clave);
        Optional<Usuario> resp = uR.findById(id);
        if (resp.isPresent()) {
            Usuario usuario = resp.get();
                   
            usuario.setAlias(alias);                               
            
            usuario.setEmail(email);
            
            
            
            String encriptada=new BCryptPasswordEncoder().encode(clave);
            usuario.setClave(encriptada);
            
            String idFoto=null;
            if(usuario.getFoto()!=null){
            idFoto=usuario.getFoto().getId();
            }
            Foto foto=fS.actualizar(idFoto, archivo);
            
            usuario.setFoto(foto);
            
            uR.save(usuario);
                        
        }else{
            throw new ErrorService("Error al modificar el usuario, no se encontró el id.");
        }
    }

    private void validar(String alias, String email, String clave) throws ErrorService {
        if (alias == null || alias.isEmpty()) {
            throw new ErrorService("El alias no puede ser nulo, ni estar vacío.");
        }
        Usuario ali = uR.buscarPorAlias(alias);
        if (ali != null) {
            throw new ErrorService("Alguien más tiene ese alias, elije otro");
        }
        if (email == null || email.isEmpty()) {
            throw new ErrorService("El mail no puede ser nulo, ni estar vacío");
        }
        Usuario em = uR.buscarPorMail(email);
        if (em != null) {
            throw new ErrorService("Ya existe ese email en nuestra base de datos, elije otra.");
        }
        if (clave == null || clave.isEmpty()) {
            throw new ErrorService("La contraseña no puede ser nula, ni estar vacía.");
        }

    }
    private void validarActualizar(String alias, String email, String clave) throws ErrorService {
        if (alias == null || alias.isEmpty()) {
            throw new ErrorService("El alias no puede ser nulo, ni estar vacío.");
        }
       
        if (email == null || email.isEmpty()) {
            throw new ErrorService("El mail no puede ser nulo, ni estar vacío");
        }
      
        if (clave == null || clave.isEmpty()) {
            throw new ErrorService("La contraseña no puede ser nula, ni estar vacía.");
        }
    }
    @Transactional
    public Usuario deshabilitarUsuario(String id) throws ErrorService{
        Optional<Usuario> respuesta = uR.findById(id);
        if (respuesta.isPresent()) {
            Usuario usuario = respuesta.get();
            usuario.setFechaBaja(new Date());

           return uR.save(usuario);
        } else {
            throw new ErrorService("No se encontró el usuario solicitado");
        }
    }
     @Transactional
    public Usuario habilitarUsuario(String id) throws ErrorService{
        Optional<Usuario> respuesta = uR.findById(id);
        if (respuesta.isPresent()) {
            Usuario usuario = respuesta.get();
            usuario.setFechaBaja(null);
            usuario.setFechaAlta(new Date());

           return uR.save(usuario);
        } else {
            throw new ErrorService("No se encontró el usuario solicitado");
        }
    }
     public Usuario buscarUsuarioPorId(String id) throws ErrorService{
        Optional<Usuario> resp=uR.findById(id);
        if(resp.isPresent()){
            return uR.findById(id).get();
        }else{
            throw new ErrorService("No se encontró el id de este Usuario.");
        }
    }
     public void cambiarTipodeRol(String id) throws ErrorService{
         Optional<Usuario>resp=uR.findById(id);
         if(resp.isPresent()){
             Usuario usuario=resp.get();
             usuario.setTipoUsuario(TipoUsuario.FAMILIA);
             
         }else{
             throw new ErrorService("No se encontró el id");
         }
     }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
      Usuario usuario=uR.buscarPorMail(username);
      if(usuario!=null){
          List<GrantedAuthority>permisos=new ArrayList<>();
          GrantedAuthority p1 = new SimpleGrantedAuthority("ROLE_"+ usuario.getTipoUsuario());
            permisos.add(p1);
         
            //Esto me permite guardar el OBJETO USUARIO LOG, para luego ser utilizado
            ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
            HttpSession session = attr.getRequest().getSession(true);
            
            session.setAttribute("usuariosession", usuario); // llave + valor

          User user=new User(usuario.getEmail(), usuario.getClave(), permisos);
          
          return user;
      }else{
          return null;
      }
    
    } 
    @Transactional
    public List<Usuario> listarUsuarios(){
        return uR.findAll();
    }
    
   
}
