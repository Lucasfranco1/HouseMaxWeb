/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.estanciasestrajeroweb.sistema.servicios;

import com.estanciasestrajeroweb.sistema.entidades.Casa;
import com.estanciasestrajeroweb.sistema.entidades.Cliente;
import com.estanciasestrajeroweb.sistema.entidades.Reserva;
import com.estanciasestrajeroweb.sistema.excepciones.ErrorService;
import com.estanciasestrajeroweb.sistema.repositorios.ReservaRepositorio;
import java.util.Date;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ReservaServicio {

    @Autowired
    private ReservaRepositorio rR;

    @Autowired
    private CasaServicio cS;

    @Autowired
    private ClienteServicio clS;

   
    /*
    private String huesped;
    
    @Temporal(TemporalType.DATE)
    private Date fechaDesde;
    
    @Temporal(TemporalType.DATE)
    private Date fechaHasta;
    
    @ManyToOne
    private Cliente cliente;
    
    @OneToOne
    private Casa casa;
     */
    @Transactional
    public void crearReserva(String huesped, Date fechaDesde, Date fechaHasta, String idCliente,
            String idCasa) throws ErrorService {
        validar(huesped, fechaDesde, fechaHasta);
        Cliente cliente = clS.buscarClientePorId(idCliente);
        Casa casa = cS.buscarCasaPorId(idCasa);

        Reserva reserva = new Reserva();
        reserva.setHuesped(huesped);
        reserva.setFechaDesde(fechaDesde);
        reserva.setFechaHasta(fechaHasta);
        reserva.setCliente(cliente);
        reserva.setCasa(casa);

        rR.save(reserva);
       
    }

    @Transactional
    public void modificarReserva(String id, String huesped, Date fechaDesde, Date fechaHasta,
            String idCliente, String idCasa) throws ErrorService {
        validar(huesped, fechaDesde, fechaHasta);
        Cliente cliente = clS.buscarClientePorId(idCliente);
        Casa casa = cS.buscarCasaPorId(idCasa);
        Optional<Reserva> resp = rR.findById(id);
        if (resp.isPresent()) {
            Reserva reserva = resp.get();
            reserva.setHuesped(huesped);
            reserva.setFechaDesde(fechaDesde);
            reserva.setFechaHasta(fechaHasta);
            reserva.setCliente(cliente);
            reserva.setCasa(casa);

            rR.save(reserva);
            
        } else {
            throw new ErrorService("Error al modificar la reserva, no se encontró el id");
        }
    }

    @Transactional
    public void eliminarReserva(String id) throws ErrorService {
        Optional<Reserva> resp = rR.findById(id);
        if (resp.isPresent()) {
            rR.deleteById(id);
            
        } else {
            throw new ErrorService("Error al eliminar la reserva");
        }
    }

    private void validar(String huesped, Date fechaDesde, Date fechaHasta) throws ErrorService {
        if (huesped == null || huesped.isEmpty()) {
            throw new ErrorService("El húesped no puede ser nulo, ni estar vacío");
        }
        if (fechaDesde.compareTo(fechaHasta) >= 0) {
            throw new ErrorService("La fecha de finalización no puede ser anterior ni igual a la fecha de inicio de la reserva.");
        }
    }
}
