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
import pe.edu.unmsm.urcs.interfaces.IOperarioDao;
import pe.edu.unmsm.urcs.modelo.Operario;

public class OperarioDao implements IOperarioDao {

    @Override
    public void insertarOperario(Session session, Operario operario) throws Exception {
        session.save(operario);
    }

    @Override
    public void modificarOperario(Session session, Operario operario) throws Exception {
        session.update(operario);
    }

    @Override
    public void eliminarOperario(Session session, Operario operario) throws Exception {
        session.delete(operario);
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
            Hibernate.initialize(ope.getCorreo());
            Hibernate.initialize(ope.getArea().getDescripcion());
        }

        return session.createCriteria(Operario.class).list();
    }

    @Override
    public Operario verificarOperario(Session session, Operario operario) throws Exception {
        Operario oper = null;
        String hql = "from Operario where correo = '" + operario.getCorreo() + "'";
        Query query = session.createQuery(hql);
        if (!query.list().isEmpty()) {
            oper = (Operario) query.uniqueResult();
        }
        return oper;
    }

    @Override
    public int getIdOperarioMenorCargaTrabajo(Session session, int idArea) {
        int idOperario = 0;
        Query query = session.createSQLQuery("select X.idOperario from (select "
                + "idOperario, count(*) from Solicitud A inner Join Operario B "
                + "on A.Operario_idOperario = B.idOperario where "
                + "B.Area_idArea = " + idArea + " group by B.idOperario "
                + "order by count(*) asc) as X LIMIT 1");
        if (!query.list().isEmpty()) {
            idOperario = (int) query.uniqueResult();
        }
        return idOperario;
    }
}
