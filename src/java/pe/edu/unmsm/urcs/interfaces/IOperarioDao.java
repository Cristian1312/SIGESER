/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.edu.unmsm.urcs.interfaces;

import java.util.List;
import org.hibernate.Session;
import pe.edu.unmsm.urcs.modelo.Operario;

/**
 *
 * @author SILVIA
 */
public interface IOperarioDao {
    public List<Operario> getAll(Session session) throws Exception;
    public void insertarOperario(Session session, Operario operario) throws Exception;
    public Operario verificarOperario(Session session, Operario operario)throws Exception;
    public void modificarOperario(Session session, Operario operario) throws Exception;
    public void eliminarOperario(Session session, Operario operario) throws Exception;
    public int getIdOperarioMenorCargaTrabajo(Session session, int idArea);
}
