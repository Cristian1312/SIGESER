/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.edu.unmsm.urcs.dao;

import org.hibernate.Session;
import pe.edu.unmsm.urcs.interfaces.IOperarioDao;
import pe.edu.unmsm.urcs.modelo.Operario;

public class OperarioDao implements IOperarioDao {

    @Override
    public void insertarOperario(Session session, Operario operario) throws Exception {
        session.save(operario);
        System.out.println("inserto operario");
    }
}
