/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.edu.unmsm.urcs.bean;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import org.hibernate.Session;
import org.hibernate.Transaction;
import pe.edu.unmsm.urcs.dao.SolicitudDao;
import pe.edu.unmsm.urcs.interfaces.ISolicitudDao;
import pe.edu.unmsm.urcs.modelo.Estado;
import pe.edu.unmsm.urcs.modelo.Operario;
import pe.edu.unmsm.urcs.modelo.Solicitud;
import pe.edu.unmsm.urcs.modelo.Usuario;
import pe.edu.unmsm.urcs.persistencia.NewHibernateUtil;

/**
 *
 * @author SILVIA
 */
@ManagedBean
@SessionScoped
public class SolicitudesCursoBean implements Serializable {

    Session session;
    Transaction transaction;
    Operario operario;
    Usuario usuario;
    private String descripcion;
    private List<Solicitud> solicitudesCurso;
    private Solicitud solicitud;
    
    public SolicitudesCursoBean() {
        this.usuario = (Usuario) FacesContext.getCurrentInstance().getExternalContext()
                .getSessionMap().get("usuario");
    }

    public List<Solicitud> getSolicitudesCurso() {
        this.session = null;
        this.transaction = null;
        try {
            this.session = NewHibernateUtil.getSessionFactory().openSession();
            ISolicitudDao solicitudDao = new SolicitudDao();
            this.transaction = this.session.beginTransaction();
            this.solicitudesCurso = solicitudDao.getSolicitudesCurso(this.session,
                    this.usuario.getEmail());
            this.transaction.commit();
            return solicitudesCurso;
        } catch (Exception ex) {
            if (this.transaction != null) {
                transaction.rollback();
            }
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(
                    FacesMessage.SEVERITY_FATAL, "Error", ex.getMessage()));
            return null;
        } finally {
            if (this.session != null) {
                this.session.close();
            }
        }
    }

    public void setSolicitudesCurso(List<Solicitud> solicitudesCurso) {
        this.solicitudesCurso = solicitudesCurso;
    }

    /**
     * @return the descripcion
     */
    public String getDescripcion() {
        return descripcion;
    }

    /**
     * @param descripcion the descripcion to set
     */
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public void guardardescripcion() {
        this.session = null;
        this.transaction = null;

        try {
            this.session = NewHibernateUtil.getSessionFactory().openSession();
            ISolicitudDao solicitudDao = new SolicitudDao();
            this.transaction = this.session.beginTransaction();
            Estado estado = new Estado();
            estado.setIdEstado(5);
            this.getSolicitud().setEstado(estado);
            this.getSolicitud().setInforme(this.descripcion);
            this.getSolicitud().setFechaFinalizado(new Date());
            solicitudDao.modificarSolicitud(this.session, this.getSolicitud());
            this.transaction.commit();
            this.solicitud = new Solicitud();
            this.setDescripcion("");

        } catch (Exception ex) {
            if (this.transaction != null) {
                transaction.rollback();
            }
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(
                    FacesMessage.SEVERITY_FATAL, "Error", ex.getMessage()));
        } finally {
            if (this.session != null) {
                this.session.close();
            }
        }
    }

    /**
     * @return the solicitud
     */
    public Solicitud getSolicitud() {
        return solicitud;
    }

    /**
     * @param solicitud the solicitud to set
     */
    public void setSolicitud(Solicitud solicitud) {
        this.solicitud = solicitud;
    }
}
