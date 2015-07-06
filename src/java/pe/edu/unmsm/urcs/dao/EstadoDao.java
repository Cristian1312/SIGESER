/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.edu.unmsm.urcs.dao;

import java.util.List;
import org.hibernate.Session;
import pe.edu.unmsm.urcs.interfaces.IEstadoDao;
import pe.edu.unmsm.urcs.modelo.Estado;

/**
 *
 * @author Cristian1312
 */
public class EstadoDao implements IEstadoDao {

    @Override
    public List<Estado> getAll(Session session) throws Exception {
        return session.createCriteria(Estado.class).list();
    }
}
