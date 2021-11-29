/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.estanciasestrajeroweb.sistema.controladores;

import com.estanciasestrajeroweb.sistema.entidades.Cliente;
import com.estanciasestrajeroweb.sistema.entidades.Familia;
import com.estanciasestrajeroweb.sistema.entidades.Usuario;
import com.estanciasestrajeroweb.sistema.excepciones.ErrorService;
import com.estanciasestrajeroweb.sistema.servicios.ClienteServicio;
import com.estanciasestrajeroweb.sistema.servicios.FamiliaServicio;
import com.estanciasestrajeroweb.sistema.servicios.UsuarioServicio;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;


@PreAuthorize("hasAnyRole('ROLE_USUARIO','ROLE_CLIENTE')")
@Controller
@RequestMapping("/cliente")
public class ClienteControlador {
    @Autowired
    private ClienteServicio cS;
    
    @Autowired
    private UsuarioServicio uS;
    
    @Autowired 
    private FamiliaServicio famS;
    
    
    
    @GetMapping("/editar-perfil")
    public String irARegistrarFamilia(HttpSession session, @RequestParam(required = false) String id, @RequestParam(required = false) String action, ModelMap modelo) {
       
        if(action==null){
            action="Crear";
        }
        
        Usuario login=(Usuario)session.getAttribute("usuariosession");
        if(login==null){
            return "redirect:/inicio";
        }
        Cliente cliente = new Cliente();
        if (id != null && !id.isEmpty()) {
            
           try {
               cliente = cS.buscarClientePorId(id);
               
           } catch (ErrorService ex) {
               Logger.getLogger(ClienteControlador.class.getName()).log(Level.SEVERE, null, ex);
           }            
        }
        modelo.put("perfil", cliente);
        modelo.put("action", action);
//
        return "formularioCliente.html";

    }

    @PostMapping("/cargar-perfil")
    public String cargarFamilia(ModelMap modelo, HttpSession session, MultipartFile archivo,
            @RequestParam(required = false) String nombre,
            @RequestParam(required = false) String calle,
            @RequestParam(required = false) Integer numero,
            @RequestParam(required = false) String codPostal,
            @RequestParam(required = false) String ciudad,
            @RequestParam(required = false) String pais,
            @RequestParam(required = false) String email,
            @RequestParam String id
    ) {

        Usuario login = (Usuario) session.getAttribute("usuariosession");
         if(login==null){
            return "redirect:/inicio";
        }
        try {
            if (id == null || id.isEmpty()) {

                cS.crearCliente(archivo, nombre, calle, numero, codPostal, ciudad, pais, email, login.getId());
                modelo.put("titulo", "Cliente cargado");
            modelo.put("mensaje", "¡Tu perfil de Cliente se cargó correctamente!");
            } else {
                cS.modificarCliente(id, archivo, nombre, calle, numero, codPostal, ciudad, pais, email, login.getId());
                modelo.put("titulo", "Cliente modificada");
            modelo.put("mensaje", "¡Tu perfil de Cliente se modificó correctamente!");
            modelo.put("action", "Actualizar");
            }

            
            return "aviso.html";

        } catch (Exception e) {
            Cliente cliente = new Cliente();
            cliente.setNombre(nombre);
            cliente.setCalle(calle);
            cliente.setNumero(numero);
            cliente.setCodPostal(codPostal);
            cliente.setCiudad(ciudad);
            cliente.setPais(pais);
            cliente.setEmail(email);
            modelo.put("error", e.getMessage());
            modelo.put("nombre", nombre);
            modelo.put("calle", calle);
            modelo.put("numero", numero);
            modelo.put("codPostal", codPostal);
            modelo.put("ciudad", ciudad);
            modelo.put("pais", pais);
            modelo.put("email", email);
            modelo.put("perfil", cliente);
            return "formularioCliente.html";
        }

    }
    @GetMapping("/listarClientes")
    public String listarClientes(HttpSession session, ModelMap model) {
        
        Usuario login = (Usuario) session.getAttribute("usuariosession");
         if(login==null){
            return "redirect:/inicio";
        }

        List<Cliente>todos=cS.listarClientes(login.getId());
        model.addAttribute("clientes", todos);
        
        return "carga_cliente.html";
        
                
    }
    @PostMapping("/eliminar-perfil")
    public String eliminar(HttpSession session, @RequestParam String id){
        try {
            Usuario login = (Usuario) session.getAttribute("usuariosession");
            famS.eliminar(login.getId(),id);
            
        } catch (ErrorService ex) {
            Logger.getLogger(FamiliaControlador.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "redirect:/familia/mi-familia";
    }
    
    
}
