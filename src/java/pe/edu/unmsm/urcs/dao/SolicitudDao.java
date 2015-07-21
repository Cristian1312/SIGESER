/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.edu.unmsm.urcs.dao;

import java.util.List;
import org.hibernate.Hibernate;
import org.hibernate.Query;
import org.hibernate.Session;
import pe.edu.unmsm.urcs.interfaces.ISolicitudDao;
import pe.edu.unmsm.urcs.modelo.Solicitud;

/**
 *
 * @author Cristian1312
 */
public class SolicitudDao implements ISolicitudDao {

    @Override
    public List<Solicitud> getAll(Session session) throws Exception {
        List<Solicitud> solicitudes = session.createCriteria(Solicitud.class).list();
        for (Solicitud sol : solicitudes) {
            Hibernate.initialize(sol.getUsuario());
            Hibernate.initialize(sol.getEstado());
            Hibernate.initialize(sol.getServicio());
        }

        return solicitudes;
    }

    @Override
    public List<Solicitud> getSolicitudesFinalizadas(Session session) throws Exception {
        List<Solicitud> solicitudesFinalizadas = session.createQuery("from Solicitud where estado.idEstado = 5").
                list();
        for (Solicitud sol : solicitudesFinalizadas) {
            Hibernate.initialize(sol.getUsuario());
            Hibernate.initialize(sol.getEstado());
            Hibernate.initialize(sol.getServicio());
        }
        return solicitudesFinalizadas;
    }
    
    @Override
    public List<Solicitud> getsolicitudesTerminadas(Session session) throws Exception {
        List<Solicitud> solicitudesTerminadas = session.createQuery("from "
                + "Solicitud where estado.idEstado = 5 and monthname(curdate()) "
                + "= monthname(fechaFinalizado)").list();
        for (Solicitud sol : solicitudesTerminadas) {
            Hibernate.initialize(sol.getUsuario());
            Hibernate.initialize(sol.getOperario());
            Hibernate.initialize(sol.getEstado());
            Hibernate.initialize(sol.getServicio().getArea());
            
        }
        return solicitudesTerminadas;
    }
    
    @Override
    public List<Solicitud> getsolicitudesPendientes(Session session, String correo) throws Exception {
        List<Solicitud> solicitudesPendientes = session.createQuery("from Solicitud where estado.idEstado = 1 "
                + "and operario.correo = '" + correo + "'").list();
        for (Solicitud sol : solicitudesPendientes) {
            Hibernate.initialize(sol.getUsuario());
            Hibernate.initialize(sol.getOperario());
            Hibernate.initialize(sol.getEstado());
            Hibernate.initialize(sol.getServicio());
        }
        return solicitudesPendientes;
    }

    @Override
    public List<Solicitud> getSolicitudesAtendidas(Session session, String correo) throws Exception {
        List<Solicitud> solicitudesAtendidas = session.createQuery("from Solicitud where estado.idEstado = 5 "
                + "and operario.correo = '" + correo + "'").list();
        for (Solicitud sol : solicitudesAtendidas) {
            Hibernate.initialize(sol.getUsuario());
            Hibernate.initialize(sol.getOperario());
            Hibernate.initialize(sol.getEstado());
            Hibernate.initialize(sol.getServicio());
        }
        return solicitudesAtendidas;
    }

    @Override
    public List<Solicitud> getSolicitudAtendidaById(Session session, String correo, int id) throws Exception {
        List<Solicitud> solicitudAtendida = session.createQuery("from Solicitud where estado.idEstado = 5 "
                + "and operario.correo = '" + correo + "' and idSolicitud = " + id).list();
        for (Solicitud sol : solicitudAtendida) {
            Hibernate.initialize(sol.getUsuario());
            Hibernate.initialize(sol.getOperario());
            Hibernate.initialize(sol.getEstado());
            Hibernate.initialize(sol.getServicio());
        }
        return solicitudAtendida;
    }
    
    @Override
    public List<Solicitud> getSolicitudFinalizadaById(Session session, int id) throws Exception {
        List<Solicitud> solicitudFinalizada = session.createQuery("from Solicitud where estado.idEstado = 5 "
                + "and idSolicitud = " + id).list();
        for (Solicitud sol : solicitudFinalizada) {
            Hibernate.initialize(sol.getUsuario());
            Hibernate.initialize(sol.getOperario());
            Hibernate.initialize(sol.getEstado());
            Hibernate.initialize(sol.getServicio());
        }
        return solicitudFinalizada;
    }

    @Override
    public List<Solicitud> getSolicitudesPendientesReasignacion(Session session) throws Exception {
        List<Solicitud> solicitudes = session.createQuery("from Solicitud where estado.idEstado = 3 ").list();
        for (Solicitud sol : solicitudes) {
            Hibernate.initialize(sol.getIdSolicitud());
            Hibernate.initialize(sol.getUsuario());
            Hibernate.initialize(sol.getEstado());
            Hibernate.initialize(sol.getServicio());
            Hibernate.initialize(sol.getOperario());
        }
        return solicitudes;
    }

    @Override
    public List<Solicitud> getSolicitudesCurso(Session session, String email) throws Exception {
        List<Solicitud> solicitudesCurso = session.createQuery("from Solicitud where estado.idEstado = 2 "
                + "and operario.correo = '" + email + "'").list();
        for (Solicitud sol : solicitudesCurso) {
            Hibernate.initialize(sol.getUsuario());
            Hibernate.initialize(sol.getOperario());
            Hibernate.initialize(sol.getEstado());
            Hibernate.initialize(sol.getServicio());
        }
        return solicitudesCurso;
    }

    @Override
    public void insertarSolicitud(Session session, Solicitud solicitud) throws Exception {
        session.save(solicitud);
    }

    @Override
    public void modificarSolicitud(Session session, Solicitud solicitud) throws Exception {
        session.update(solicitud);
    }

    @Override
    public void eliminarSolicitud(Session session, Solicitud solicitud) throws Exception {
        session.delete(solicitud);
    }
}
