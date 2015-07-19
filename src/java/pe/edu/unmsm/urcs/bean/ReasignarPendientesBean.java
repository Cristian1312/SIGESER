/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.edu.unmsm.urcs.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Date;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import org.hibernate.Session;
import org.hibernate.Transaction;
import pe.edu.unmsm.urcs.dao.OperarioDao;
import pe.edu.unmsm.urcs.dao.SolicitudDao;
import pe.edu.unmsm.urcs.interfaces.IOperarioDao;
import pe.edu.unmsm.urcs.interfaces.ISolicitudDao;
import pe.edu.unmsm.urcs.modelo.Estado;
import pe.edu.unmsm.urcs.modelo.Operario;
import pe.edu.unmsm.urcs.modelo.Solicitud;
import pe.edu.unmsm.urcs.modelo.Usuario;
import pe.edu.unmsm.urcs.persistencia.NewHibernateUtil;

/**
 *
 * @author Cristian1312
 */
@ManagedBean
@SessionScoped
public class ReasignarPendientesBean implements Serializable {

    Session session;
    Transaction transaction;
    Usuario usuario;

    private String idOperario;
    private List<SelectItem> selectItemsOneOperario;
    private List<String> idsSolicitud;
    private List<Solicitud> solicitudes;
    private Solicitud solicitud;

    public ReasignarPendientesBean() {
        this.usuario = (Usuario) FacesContext.getCurrentInstance().getExternalContext()
                .getSessionMap().get("usuario");
        this.solicitud = new Solicitud();
    }

    public Solicitud getSolicitud() {
        return solicitud;
    }

    public void setSolicitud(Solicitud solicitud) {
        this.solicitud = solicitud;
    }

    public List<Solicitud> getSolicitudesPendientesReasignacion() {
        this.session = null;
        this.transaction = null;

        try {
            this.session = NewHibernateUtil.getSessionFactory().openSession();
            ISolicitudDao solicitudDao = new SolicitudDao();
            this.transaction = this.session.beginTransaction();
            this.solicitudes = solicitudDao.getSolicitudesPendientesReasignacion(this.session);
            this.transaction.commit();
            return this.solicitudes;
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

    public List<String> getIdsSolicitud() {
        this.session = null;
        this.transaction = null;

        try {
            this.session = NewHibernateUtil.getSessionFactory().openSession();
            this.idsSolicitud = new ArrayList<>();
            ISolicitudDao solicitudDao = new SolicitudDao();
            this.transaction = this.session.beginTransaction();
            List<Solicitud> solicitudesTemp = solicitudDao.getAll(this.session);
            for (Solicitud solicitud : solicitudesTemp) {
                this.idsSolicitud.add(String.valueOf(solicitud.getIdSolicitud()));
            }
            this.transaction.commit();
            System.out.println("Size: " + this.idsSolicitud.size());
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

    public List<SelectItem> getSelectItemsOneOperario() {
        this.session = null;
        this.transaction = null;

        try {
            this.session = NewHibernateUtil.getSessionFactory().openSession();
            this.selectItemsOneOperario = new ArrayList<>();
            IOperarioDao operarioDao = new OperarioDao();
            this.transaction = this.session.beginTransaction();
            //List<Operario> operarios = areaServicioDao.findAllArea(this.session);
            List<Operario> operarios = operarioDao.getAll(this.session);
            selectItemsOneOperario.clear();
            for (Operario operario : operarios) {
                SelectItem selectItem = new SelectItem(operario.getIdOperario(), operario.getNombre() + " " + operario.getApPaterno());
                this.selectItemsOneOperario.add(selectItem);
            }
            this.transaction.commit();

            return selectItemsOneOperario;
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

    public void setSelectItemsOneOperario(List<SelectItem> selectItemsOneOperario) {
        this.selectItemsOneOperario = selectItemsOneOperario;
    }

    public void reasignarSolicitud() {
        if (solicitud.getFechaProceso() == null) {
            Date fechaproceso = new Date();
            this.solicitud.setFechaProceso(fechaproceso);
            Estado estadoSol = new Estado();
            estadoSol.setIdEstado(1);
            this.solicitud.setEstado(estadoSol);
            System.out.println("Id operario: " + this.getIdOperario());
            Operario oper = new Operario();
            oper.setIdOperario(Integer.parseInt(this.idOperario));
            this.solicitud.setOperario(oper);
        }
        this.modificarServicio();
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

    public String getIdOperario() {
        return idOperario;
    }

    public void setIdOperario(String idOperario) {
        this.idOperario = idOperario;
    }
}
