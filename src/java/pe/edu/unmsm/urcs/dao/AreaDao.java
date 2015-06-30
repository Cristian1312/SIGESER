/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.edu.unmsm.urcs.dao;

import java.util.List;
import org.hibernate.Session;
import pe.edu.unmsm.urcs.interfaces.IAreaDao;
import pe.edu.unmsm.urcs.modelo.Area;

/**
 *
 * @author Cristian1312
 */
public class AreaDao implements IAreaDao {

    @Override
    public List<Area> findAll(Session session) throws Exception {
        return session.createCriteria(Area.class).list();
    }
}
