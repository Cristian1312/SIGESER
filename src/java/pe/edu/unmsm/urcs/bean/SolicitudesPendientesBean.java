/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.edu.unmsm.urcs.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.primefaces.context.RequestContext;
import pe.edu.unmsm.urcs.dao.SolicitudDao;
import pe.edu.unmsm.urcs.interfaces.ISolicitudDao;
import pe.edu.unmsm.urcs.modelo.Estado;
import pe.edu.unmsm.urcs.modelo.Solicitud;
import pe.edu.unmsm.urcs.modelo.Usuario;
import pe.edu.unmsm.urcs.persistencia.NewHibernateUtil;

/**
 *
 * @author Cristian1312
 */
@ManagedBean
@SessionScoped
public class SolicitudesPendientesBean implements Serializable {

    Session session;
    Transaction transaction;
    Usuario usuario;
    private List<String> idsSolicitud;
    private Solicitud solicitud;

    private List<Solicitud> solicitudesPendientes;

    public SolicitudesPendientesBean() {
        this.usuario = (Usuario) FacesContext.getCurrentInstance().getExternalContext()
                .getSessionMap().get("usuario");
        this.solicitud = new Solicitud();
    }

    public void pedirReasignacionSolicitud() {
        Date fechapendreasig = new Date();
        this.solicitud.setFechaPendienteReasig(fechapendreasig);
        Estado estadoReasig = new Estado();
        estadoReasig.setIdEstado(3);
        this.solicitud.setEstado(estadoReasig);
        this.modificarServicio();
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(
                    FacesMessage.SEVERITY_INFO, "Exito", "Solicitud reasignada correctamente"));
        RequestContext.getCurrentInstance().update("servicioForm:mensajeGeneral");
    }
    
    public void atenderSolicitud() {
        Date fechaProceso = new Date();
        this.solicitud.setFechaProceso(fechaProceso);
        Estado estadoEnProceso = new Estado();
        estadoEnProceso.setIdEstado(2);
        this.solicitud.setEstado(estadoEnProceso);
        this.modificarServicio();
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(
                    FacesMessage.SEVERITY_INFO, "Exito", "Solicitud atendida correctamente"));
        RequestContext.getCurrentInstance().update("servicioForm:mensajeGeneral");
    }
    

    public void modificarServicio() {
        this.session = null;
        this.transaction = null;

        try {
            this.session = NewHibernateUtil.getSessionFactory().openSession();
            ISolicitudDao solicitudDao = new SolicitudDao();
            this.transaction = this.session.beginTransaction();
            solicitudDao.modificarSolicitud(this.session, this.solicitud);
            this.transaction.commit();
            this.solicitud = new Solicitud();
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

    public List<Solicitud> getSolicitudesPendientes() {
        this.session = null;
        this.transaction = null;

        try {
            this.session = NewHibernateUtil.getSessionFactory().openSession();
            ISolicitudDao solicitudDao = new SolicitudDao();
            this.transaction = this.session.beginTransaction();
            this.solicitudesPendientes = solicitudDao.getsolicitudesPendientes(this.session,
                    this.usuario.getEmail());
            this.transaction.commit();
            return this.solicitudesPendientes;
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

    public void setSolicitudesPendientes(List<Solicitud> solicitudesPendientes) {
        this.solicitudesPendientes = solicitudesPendientes;
    }

    public List<String> getIdsSolicitud() {
        this.session = null;
        this.transaction = null;

        try {
            this.session = NewHibernateUtil.getSessionFactory().openSession();
            this.idsSolicitud = new ArrayList<>();
            ISolicitudDao solicitudDao = new SolicitudDao();
            this.transaction = this.session.beginTransaction();
            List<Solicitud> solicitudesTemp = solicitudDao.getsolicitudesPendientes(this.session,
                    this.usuario.getEmail());
            for (Solicitud solicitud : solicitudesTemp) {
                this.idsSolicitud.add(String.valueOf(solicitud.getIdSolicitud()));
            }
            this.transaction.commit();
            return this.idsSolicitud;
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

    public void setIdsSolicitud(List<String> idsSolicitud) {
        this.idsSolicitud = idsSolicitud;
    }

    public Solicitud getSolicitud() {
        return solicitud;
    }

    public void setSolicitud(Solicitud solicitud) {
        this.solicitud = solicitud;
    }
}
