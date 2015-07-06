/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.edu.unmsm.urcs.interfaces;

import org.hibernate.Session;
import pe.edu.unmsm.urcs.modelo.Usuario;

/**
 *
 * @author Cristian1312
 */
public interface IUsuarioDao {
    public Usuario verificarUsuario(Session session, Usuario usuario) throws Exception;
    public Usuario verificarCorreo(Session session, Usuario usuario) throws Exception;
    public void modificarUsuario(Session session, Usuario usuario) throws Exception;
}
