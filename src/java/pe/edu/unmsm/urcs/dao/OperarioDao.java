/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.edu.unmsm.urcs.dao;

import java.util.List;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import pe.edu.unmsm.urcs.interfaces.IOperarioDao;
import pe.edu.unmsm.urcs.modelo.Operario;

public class OperarioDao implements IOperarioDao {

    @Override
    public void insertarOperario(Session session, Operario operario) throws Exception {
        session.save(operario);
        System.out.println("inserto operario");
    }

    @Override
    public List<Operario> getAll(Session session) throws Exception {
        List<Operario> operarios = session.createCriteria(Operario.class).list();
        for (Operario ope : operarios) {
            Hibernate.initialize(ope.getIdOperario());
            Hibernate.initialize(ope.getApMaterno());
            Hibernate.initialize(ope.getAnexo());
            Hibernate.initialize(ope.getNombre());
            Hibernate.initialize(ope.getTelefono());
            Hibernate.initialize(ope.getArea().getDescripcion());

        }

        return session.createCriteria(Operario.class).list();
    }
}
