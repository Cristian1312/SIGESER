/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.edu.unmsm.urcs.dao;

import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import pe.edu.unmsm.urcs.interfaces.IAreaServicioDao;
import pe.edu.unmsm.urcs.modelo.Area;
import pe.edu.unmsm.urcs.modelo.Servicio;

/**
 *
 * @author Cristian1312
 */
public class AreaServicioDao implements IAreaServicioDao{

    @Override
    public List<Area> findAllArea(Session session) throws Exception {
        return session.createCriteria(Area.class).list();
    }

    @Override
    public List<Servicio> getServiciosByArea(Session session, String idArea) throws Exception {
        Criteria criteria = session.createCriteria(Servicio.class);
        criteria.add(Restrictions.eq("area.idArea", new Integer(idArea)));
        return criteria.list();
    }
}
