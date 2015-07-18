/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.edu.unmsm.urcs.bean;

import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import org.hibernate.Session;
import org.hibernate.Transaction;
import pe.edu.unmsm.urcs.dao.SolicitudDao;
import pe.edu.unmsm.urcs.interfaces.ISolicitudDao;
import pe.edu.unmsm.urcs.modelo.Solicitud;
import pe.edu.unmsm.urcs.modelo.Usuario;
import pe.edu.unmsm.urcs.persistencia.NewHibernateUtil;

/**
 *
 * @author Cristian1312
 */
@ManagedBean
@SessionScoped
public class SolicitudesAtendidasBean {

    Session session;
    Transaction transaction;
    Usuario usuario;
    
    private List<Solicitud> solicitudesAtendidas;
    
    public SolicitudesAtendidasBean() {
        this.usuario = (Usuario) FacesContext.getCurrentInstance().getExternalContext()
                .getSessionMap().get("usuario");
    }

    public List<Solicitud> getSolicitudesAtendidas() {
        this.session = null;
        this.transaction = null;
        
        try {
            this.session = NewHibernateUtil.getSessionFactory().openSession();
            ISolicitudDao solicitudDao = new SolicitudDao();
            this.transaction = this.session.beginTransaction();
            this.solicitudesAtendidas = solicitudDao.getSolicitudesAtendidas(this.session,
                    this.usuario.getIdUsuario());
            this.transaction.commit();
            return solicitudesAtendidas;
        } catch (Exception ex) {
            if(this.transaction != null) {
                transaction.rollback();
            }
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(
                    FacesMessage.SEVERITY_FATAL, "Error", ex.getMessage()));
            return null;
        } finally {
            if(this.session != null) {
                this.session.close();
            }
        }
    }

    public void setSolicitudesAtendidas(List<Solicitud> solicitudesAtendidas) {
        this.solicitudesAtendidas = solicitudesAtendidas;
    }
}
