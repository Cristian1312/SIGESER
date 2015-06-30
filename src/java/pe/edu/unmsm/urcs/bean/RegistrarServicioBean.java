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
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.servlet.http.HttpServletRequest;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.primefaces.context.RequestContext;
import pe.edu.unmsm.urcs.dao.AreaServicioDao;
import pe.edu.unmsm.urcs.dao.SolicitudDao;
import pe.edu.unmsm.urcs.interfaces.IAreaServicioDao;;
import pe.edu.unmsm.urcs.interfaces.ISolicitudDao;
import pe.edu.unmsm.urcs.modelo.Area;
import pe.edu.unmsm.urcs.modelo.Servicio;
import pe.edu.unmsm.urcs.modelo.Solicitud;
import pe.edu.unmsm.urcs.modelo.SolicitudId;
import pe.edu.unmsm.urcs.modelo.Usuario;
import pe.edu.unmsm.urcs.persistencia.NewHibernateUtil;

/**
 *
 * @author Cristian1312
 */
@ManagedBean
@ViewScoped
public class RegistrarServicioBean implements Serializable {

    Session session;
    Transaction transaction;
    Usuario usuario;
    
    private String idArea;
    private String idServicio;
    private String nroAnexo;
    private List<SelectItem> selectItemsOneArea;
    private List<SelectItem> selectItemsOneServicio;
    private final HttpServletRequest httpServletRequest;
    private final FacesContext facesContext;
    
    public RegistrarServicioBean() {
        facesContext = FacesContext.getCurrentInstance();
        httpServletRequest = (HttpServletRequest) facesContext.getExternalContext().getRequest();
        usuario = (Usuario) httpServletRequest.getSession().getAttribute("usuario");
    }

    public String getIdArea() {
        return idArea;
    }

    public void setIdArea(String idArea) {
        this.idArea = idArea;
    }

    public String getIdServicio() {
        return idServicio;
    }

    public void setIdServicio(String idServicio) {
        this.idServicio = idServicio;
    }

    public String getNroAnexo() {
        return nroAnexo;
    }

    public void setNroAnexo(String nroAnexo) {
        this.nroAnexo = nroAnexo;
    }

    public List<SelectItem> getSelectItemsOneArea() {
        this.session = null;
        this.transaction = null;
        
        try {
            this.session = NewHibernateUtil.getSessionFactory().openSession();
            this.selectItemsOneArea = new ArrayList<>();
            IAreaServicioDao areaServicioDao = new AreaServicioDao();
            this.transaction = this.session.beginTransaction();
            List<Area> areas = areaServicioDao.findAllArea(this.session);
            selectItemsOneArea.clear();
            for (Area area : areas) {
                SelectItem selectItem = new SelectItem(area.getIdArea(), area.getDescripcion());
                this.selectItemsOneArea.add(selectItem);
            }
            this.transaction.commit();
            
            return selectItemsOneArea;
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

    public void setSelectItemsOneArea(List<SelectItem> selectItemsOneArea) {
        this.selectItemsOneArea = selectItemsOneArea;
    }

    public List<SelectItem> getSelectItemsOneServicio() {
        this.session = null;
        this.transaction = null;
        
        try {
            this.session = NewHibernateUtil.getSessionFactory().openSession();
            this.selectItemsOneServicio = new ArrayList<>();
            IAreaServicioDao areaServicioDao = new AreaServicioDao();
            this.transaction = this.session.beginTransaction();
            List<Servicio> servicios = areaServicioDao.getServiciosByArea(session, getIdArea());
            selectItemsOneServicio.clear();
            for (Servicio servicio : servicios) {
                SelectItem selectItem = new SelectItem(servicio.getIdServicio(), servicio.getDescripcion());
                this.selectItemsOneServicio.add(selectItem);
            }
            this.transaction.commit();
            
            return selectItemsOneServicio;
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

    public void setSelectItemsOneServicio(List<SelectItem> selectItemsOneServicio) {
        this.selectItemsOneServicio = selectItemsOneServicio;
    }
    
    public void registrarServicio() {
        this.session = null;
        this.transaction = null;
        
        try {
            this.session = NewHibernateUtil.getSessionFactory().openSession();
            ISolicitudDao solicitudDao = new SolicitudDao();
            SolicitudId solicitudId = new SolicitudId();
            solicitudId.setUsuarioIdUsuario(usuario.getIdUsuario());
            solicitudId.setEstadoIdEstado(3);
            solicitudId.setServicioIdServicio(Integer.parseInt(idServicio));
            solicitudId.setOperarioIdOperario(1);
            this.transaction = this.session.beginTransaction();
            solicitudDao.insertarSolicitud(this.session, new Solicitud(solicitudId, null, null, 
                    null, null, null, null, null, null, null, null, nroAnexo));
            this.transaction.commit();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(
                    FacesMessage.SEVERITY_INFO, "Correcto", "Servicio registrado correctamente"));
            RequestContext.getCurrentInstance().update("servicioForm:mensajeGeneral");
        }
        catch(Exception ex) {
            if(this.transaction != null) {
                transaction.rollback();
            }
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(
                    FacesMessage.SEVERITY_FATAL, "Error", ex.getMessage()));
        }
        finally {
            if(this.session != null) {
                this.session.close();
            }
        }
    }
}
