/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.estanciasestrajeroweb.sistema.controladores;

import com.estanciasestrajeroweb.sistema.entidades.Casa;
import com.estanciasestrajeroweb.sistema.entidades.Familia;
import com.estanciasestrajeroweb.sistema.entidades.Foto;
import com.estanciasestrajeroweb.sistema.entidades.Usuario;
import com.estanciasestrajeroweb.sistema.excepciones.ErrorService;
import com.estanciasestrajeroweb.sistema.servicios.CasaServicio;
import com.estanciasestrajeroweb.sistema.servicios.FamiliaServicio;
import com.estanciasestrajeroweb.sistema.servicios.UsuarioServicio;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@PreAuthorize("hasAnyRole('ROLE_FAMILIA')")
@Controller
@RequestMapping("/casa")
public class CasaControlador {

    @Autowired
    private FamiliaServicio famS;

    @Autowired
    private UsuarioServicio uS;

    @Autowired
    private CasaServicio cS;

    @GetMapping("/cargar")
    public String irARegistrarCasa(HttpSession session, @RequestParam(required = false) String id, ModelMap modelo) throws ErrorService {

        Usuario login = (Usuario) session.getAttribute("usuariosession");
        if (login == null) {
            return "redirect:/inicio";
        }
        if (id != null && !id.isEmpty()) {
            Familia familia = famS.buscarFamiliaPorId(id);
            if (familia != null) {
                Casa casa = new Casa();
                modelo.addAttribute("casa", casa);
            }
            return "formularioCasa.html";

        }
        return "redirect:/inicio";
//        modelo.put("nombre", familia.getNombre());
    }

    @PostMapping("/actualizar-perfil")
    public String cargarCasa(ModelMap modelo, HttpSession session, MultipartFile archivo,
            @RequestParam(required = false) String calle,
            @RequestParam(required = false) Integer numero,
            @RequestParam(required = false) String codPostal,
            @RequestParam(required = false) String ciudad,
            @RequestParam(required = false) String pais,
            @RequestParam(required = false) String fechaDesde,
            @RequestParam(required = false) String fechaHasta,
            @RequestParam(required = false) Integer minDias,
            @RequestParam(required = false) Integer maxDias,
            @RequestParam(required = false) Double precio,
            @RequestParam(required = false) String tipoVivienda
    ) throws ErrorService {

        Usuario login = (Usuario) session.getAttribute("usuariosession");
        if (login == null) {
            return "redirect:/inicio";
        }
        try {
            Date date1 = new SimpleDateFormat("yyyy-MM-dd").parse(fechaDesde);
            Date date2 = new SimpleDateFormat("yyyy-MM-dd").parse(fechaHasta);
            Familia familia = famS.traerFamiliasPorUsuario(login.getId());
            if (familia != null) {
                cS.guardarCasa(calle, numero, codPostal, ciudad, pais, date1, date2, minDias, maxDias, precio, tipoVivienda, archivo, familia.getId());
                modelo.put("titulo", "Casa cargada");
                modelo.put("mensaje", "¡Tu Casa se cargó correctamente!");
            }

            return "aviso.html";

        } catch (Exception e) {
            Casa casa = new Casa();
            casa.setCalle(calle);
            casa.setCiudad(ciudad);
            casa.setPais(pais);
            casa.setNumero(numero);
            casa.getCodPostal();
            casa.setMinDias(minDias);
            casa.setMaxDias(maxDias);
            casa.setPrecio(precio);
            casa.setTipoVivienda(tipoVivienda);

            modelo.put("error", e.getMessage());
            modelo.put("calle", calle);
            modelo.put("ciudad", ciudad);
            modelo.put("pais", pais);
            modelo.put("numero", numero);
            modelo.put("fechaDesde", fechaDesde);
            modelo.put("fechaHasta", fechaHasta);
            modelo.put("codPostal", codPostal);
            modelo.put("minDias", minDias);
            modelo.put("maxDias", maxDias);
            modelo.put("precio", precio);
            modelo.put("tipoVivienda", tipoVivienda);
            modelo.put("archivo", archivo);
            return "formularioCasa.html";
        }

    }
@PreAuthorize("hasAnyRole('ROLE_USUARIO','ROLE_FAMILIA')")
    @GetMapping("/editar")
    public String irModificarCasa(HttpSession session, @RequestParam(required = false) String id, ModelMap modelo) throws ErrorService {

        Usuario login = (Usuario) session.getAttribute("usuariosession");
        if (login == null) {
            return "redirect:/inicio";
        }
        Casa casa = cS.buscarCasaPorId(id);
        if(casa!=null){
            modelo.put("casa", casa);
             return "formularioModificacionCasa.html";
        }    
       return "redirect:/inicio";

        
//        modelo.put("nombre", familia.getNombre());
    }

    @PreAuthorize("hasAnyRole('ROLE_USUARIO','ROLE_FAMILIA')")
    @PostMapping("/actualizando-perfil/")
    public String ActualizarCasa(ModelMap modelo, HttpSession session, MultipartFile archivo,
            @RequestParam(required = false) String calle,
            @RequestParam(required = false) Integer numero,
            @RequestParam(required = false) String codPostal,
            @RequestParam(required = false) String ciudad,
            @RequestParam(required = false) String pais,
            @RequestParam(required = false) String fechaDesde,
            @RequestParam(required = false) String fechaHasta,
            @RequestParam(required = false) Integer minDias,
            @RequestParam(required = false) Integer maxDias,
            @RequestParam(required = false) Double precio,
            @RequestParam(required = false) String tipoVivienda,
            @RequestParam(required=false) String id
    ) throws ErrorService {

        Usuario login = (Usuario) session.getAttribute("usuariosession");
        if (login == null) {
            return "redirect:/inicio";
        }
        try {
            Date date1 = new SimpleDateFormat("yyyy-MM-dd").parse(fechaDesde);
            Date date2 = new SimpleDateFormat("yyyy-MM-dd").parse(fechaHasta);
            Casa casa = cS.buscarCasaPorId(id);
            if (casa != null) {
                cS.actualizarCasa(id, calle, numero, codPostal, ciudad, pais, date1, date2, minDias, maxDias, precio, tipoVivienda, archivo);
                modelo.put("titulo", "Familia modificada");
                modelo.put("mensaje", "¡Tu familia se modificó correctamente!");
                modelo.put("action", "Actualizar");
            }

            return "aviso.html";

        } catch (Exception e) {
            Casa casa = new Casa();
            casa.setCalle(calle);
            casa.setCiudad(ciudad);
            casa.setPais(pais);
            casa.setNumero(numero);
            casa.getCodPostal();
            casa.setMinDias(minDias);
            casa.setMaxDias(maxDias);
            casa.setPrecio(precio);
            casa.setTipoVivienda(tipoVivienda);

            modelo.put("error", e.getMessage());
            modelo.put("calle", calle);
            modelo.put("ciudad", ciudad);
            modelo.put("pais", pais);
            modelo.put("numero", numero);
            modelo.put("fechaDesde", fechaDesde);
            modelo.put("fechaHasta", fechaHasta);
            modelo.put("codPostal", codPostal);
            modelo.put("minDias", minDias);
            modelo.put("maxDias", maxDias);
            modelo.put("precio", precio);
            modelo.put("tipoVivienda", tipoVivienda);
            modelo.put("archivo", archivo);
            return "formularioModificacionCasa.html";
        }

    }

    @PreAuthorize("hasAnyRole('ROLE_USUARIO','ROLE_FAMILIA')")
    @GetMapping("/listar-casas")
    public String ListarCasa(HttpSession session, ModelMap model) {

        Usuario login = (Usuario) session.getAttribute("usuariosession");
        if (login == null) {
            return "redirect:/inicio";
        }
        try {
            
            List<Casa> todos = cS.listarCasasPorImagenes();
            model.addAttribute("casas", todos);

        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
        }
        return "home";

    }
     @PreAuthorize("hasAnyRole('ROLE_USUARIO','ROLE_FAMILIA')")
    @GetMapping("/listar")
    public String Listar(HttpSession session, ModelMap model) {

        Usuario login = (Usuario) session.getAttribute("usuariosession");
        if (login == null) {
            return "redirect:/inicio";
        }
        try {
             Familia familia = famS.traerFamiliasPorUsuario(login.getId());
             
            List<Casa> todos = cS.listarCasaPorUsuario(familia.getId());
            model.addAttribute("casas", todos);
            
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
        }
        return "carga_casa";

    }
    @PreAuthorize("hasAnyRole('ROLE_USUARIO','ROLE_FAMILIA')")
    @GetMapping("/mostrar/{id}/")
    public String muestra(HttpSession session, ModelMap model, @PathVariable String id) {

        Usuario login = (Usuario) session.getAttribute("usuariosession");
        if (login == null) {
            return "redirect:/inicio";
        }
        try {
            Casa casa=cS.buscarCasaPorId(id);
             
            List<Casa> todos = cS.listarCasaPorUsuario(id);
            model.addAttribute("casas", todos);
            model.put("casa", casa);
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
        }
        return "casa_muestra";

    }
    
    @PreAuthorize("hasAnyRole('ROLE_USUARIO','ROLE_FAMILIA')")
    @GetMapping("/casa-info/{id}")
    public String Casainfo(HttpSession session, ModelMap model, @PathVariable String id) throws ErrorService {
        Usuario login = (Usuario) session.getAttribute("usuariosession");
        if (login == null) {
            return "redirect:/inicio";
        }

        Casa c = cS.buscarCasaPorId(id);
        if (c != null) {
            model.addAttribute("casa", c);
            return "casa_info";
        } else {
            return "redirect:/casa/listar-casas";
        }

    }
    
    @PreAuthorize("hasAnyRole('ROLE_USUARIO','ROLE_FAMILIA')")
    @GetMapping("/eliminar/{id}")
    public String Eliminar(HttpSession session, ModelMap model, @PathVariable String id) throws ErrorService {
        Usuario login = (Usuario) session.getAttribute("usuariosession");
        if (login == null) {
            return "redirect:/inicio";
        }
         cS.eliminarCasa(id);        
            return "redirect:/casa/listar-casas";
        }

    
}


