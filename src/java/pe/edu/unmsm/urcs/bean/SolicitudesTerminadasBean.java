/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.edu.unmsm.urcs.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import org.hibernate.Session;
import org.hibernate.Transaction;
import pe.edu.unmsm.urcs.dao.AreaServicioDao;
import pe.edu.unmsm.urcs.dao.SolicitudDao;
import pe.edu.unmsm.urcs.interfaces.IAreaServicioDao;
import pe.edu.unmsm.urcs.interfaces.ISolicitudDao;
import pe.edu.unmsm.urcs.modelo.Area;
import pe.edu.unmsm.urcs.modelo.Solicitud;
import pe.edu.unmsm.urcs.modelo.Usuario;
import pe.edu.unmsm.urcs.persistencia.NewHibernateUtil;

/**
 *
 * @author Diego
 */
@ManagedBean
@SessionScoped
public class SolicitudesTerminadasBean implements Serializable {

    Session session;
    Transaction transaction;
    Usuario usuario;
    private List<String> areas;

    private List<Solicitud> solicitudesTerminadas;

    public SolicitudesTerminadasBean() {
        this.usuario = (Usuario) FacesContext.getCurrentInstance().getExternalContext()
                .getSessionMap().get("usuario");
    }

    public List<Solicitud> getSolicitudesTerminadas() {
        this.session = null;
        this.transaction = null;

        try {
            this.session = NewHibernateUtil.getSessionFactory().openSession();
            ISolicitudDao solicitudDao = new SolicitudDao();
            this.transaction = this.session.beginTransaction();
            this.solicitudesTerminadas = solicitudDao.getsolicitudesTerminadas(this.session);
            this.transaction.commit();
            
            return this.solicitudesTerminadas;
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
    
    public void setSolicitudesTerminadas(List<Solicitud> solicitudesTerminadas) {
        this.solicitudesTerminadas = solicitudesTerminadas;
    }
    
    public List<String> getAreas() {
        this.session = null;
        this.transaction = null;
        
        try {
            this.session = NewHibernateUtil.getSessionFactory().openSession();
            this.areas = new ArrayList<>();
            IAreaServicioDao areaServicioDao = new AreaServicioDao();
            this.transaction = this.session.beginTransaction();
            List<Area> areasTemp = areaServicioDao.findAllArea(this.session);
            for (Area area : areasTemp) {
                this.areas.add(area.getDescripcion());
            }
            this.transaction.commit();
            
            return this.areas;
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
}
