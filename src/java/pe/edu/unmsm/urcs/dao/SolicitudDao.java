/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.edu.unmsm.urcs.dao;

import java.util.List;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import pe.edu.unmsm.urcs.interfaces.ISolicitudDao;
import pe.edu.unmsm.urcs.modelo.Solicitud;

/**
 *
 * @author Cristian1312
 */
public class SolicitudDao implements ISolicitudDao{      

    @Override
    public List<Solicitud> getAll(Session session) throws Exception {
        List<Solicitud> solicitudes = session.createCriteria(Solicitud.class).list();
        for (Solicitud sol : solicitudes) {
            Hibernate.initialize(sol.getUsuario());
            Hibernate.initialize(sol.getEstado());
            Hibernate.initialize(sol.getServicio());
        }
        
        return session.createCriteria(Solicitud.class).list();
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
