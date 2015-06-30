/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.edu.unmsm.urcs.interfaces;

import java.util.List;
import org.hibernate.Session;
import pe.edu.unmsm.urcs.modelo.Area;
import pe.edu.unmsm.urcs.modelo.Servicio;

/**
 *
 * @author Cristian1312
 */
public interface IAreaServicioDao {
    public List<Area> findAllArea(Session session) throws Exception;
    public List<Servicio> getServiciosByArea(Session session, String idArea) throws Exception;
}
