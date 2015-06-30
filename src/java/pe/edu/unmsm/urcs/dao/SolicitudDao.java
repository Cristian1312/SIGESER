/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.edu.unmsm.urcs.dao;

import org.hibernate.Session;
import pe.edu.unmsm.urcs.interfaces.ISolicitudDao;
import pe.edu.unmsm.urcs.modelo.Solicitud;

/**
 *
 * @author Cristian1312
 */
public class SolicitudDao implements ISolicitudDao{

    @Override
    public void insertarSolicitud(Session session, Solicitud solicitud) throws Exception {
        session.save(solicitud);
    }        
}
