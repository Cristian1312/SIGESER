/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.edu.unmsm.urcs.interfaces;

import java.util.List;
import org.hibernate.Session;
import pe.edu.unmsm.urcs.modelo.Solicitud;

/**
 *
 * @author Cristian1312
 */
public interface ISolicitudDao {

    public List<Solicitud> getAll(Session session) throws Exception;

    public void insertarSolicitud(Session session, Solicitud solicitud) throws Exception;

    public void modificarSolicitud(Session session, Solicitud solicitud) throws Exception;

    public void eliminarSolicitud(Session session, Solicitud solicitud) throws Exception;

    public List<Solicitud> getSolicitudesPendientesReasignacion(Session session) throws Exception;

    public List<Solicitud> getsolicitudesPendientes(Session session, String correo) throws Exception;

    public List<Solicitud> getSolicitudesAtendidas(Session session, String correo) throws Exception;

    public List<Solicitud> getSolicitudAtendidaById(Session session, String correo, int id) throws Exception;

    public List<Solicitud> getSolicitudesCurso(Session session, String email) throws Exception;
}
