/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.edu.unmsm.urcs.interfaces;

import org.hibernate.Session;
import pe.edu.unmsm.urcs.modelo.Operario;

/**
 *
 * @author SILVIA
 */
public interface IOperarioDao {

    public void insertarOperario(Session session, Operario operario) throws Exception;
}
