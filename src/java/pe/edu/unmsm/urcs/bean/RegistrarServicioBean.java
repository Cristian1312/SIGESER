/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.edu.unmsm.urcs.bean;

import java.util.ArrayList;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.primefaces.context.RequestContext;
import pe.edu.unmsm.urcs.dao.AreaServicioDao;
import pe.edu.unmsm.urcs.dao.EstadoDao;
import pe.edu.unmsm.urcs.dao.SolicitudDao;
import pe.edu.unmsm.urcs.interfaces.IAreaServicioDao;
import pe.edu.unmsm.urcs.interfaces.IEstadoDao;
import pe.edu.unmsm.urcs.interfaces.ISolicitudDao;
import pe.edu.unmsm.urcs.modelo.Area;
import pe.edu.unmsm.urcs.modelo.Estado;
import pe.edu.unmsm.urcs.modelo.Operario;
import pe.edu.unmsm.urcs.modelo.Servicio;
import pe.edu.unmsm.urcs.modelo.Solicitud;
import pe.edu.unmsm.urcs.modelo.Usuario;
import pe.edu.unmsm.urcs.persistencia.NewHibernateUtil;

/**
 *
 * @author Cristian1312
 */
@ManagedBean
@SessionScoped
public class RegistrarServicioBean {

    Session session;
    Transaction transaction;
    Usuario usuario;
    
    private String idArea;
    private String idServicio;
    private String idEstado;
    private String idSolicitud;
    private List<SelectItem> selectItemsOneArea;
    private List<SelectItem> selectItemsOneServicio;
    private List<String> estados;
    private List<String> idsSolicitud;
    private List<Solicitud> solicitudes;
    private Solicitud solicitud;
    
    public RegistrarServicioBean() {
        this.usuario = (Usuario) FacesContext.getCurrentInstance().getExternalContext()
                .getSessionMap().get("usuario");
        this.solicitud = new Solicitud();
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

    public String getIdEstado() {
        return idEstado;
    }

    public void setIdEstado(String idEstado) {
        this.idEstado = idEstado;
    }

    public String getIdSolicitud() {
        return idSolicitud;
    }

    public void setIdSolicitud(String idSolicitud) {
        this.idSolicitud = idSolicitud;
    }

    public Solicitud getSolicitud() {
        return solicitud;
    }

    public void setSolicitud(Solicitud solicitud) {
        this.solicitud = solicitud;
    }
    
    public List<Solicitud> getSolicitudes() {
        this.session = null;
        this.transaction = null;
        
        try {
            this.session = NewHibernateUtil.getSessionFactory().openSession();
            ISolicitudDao solicitudDao = new SolicitudDao();
            this.transaction = this.session.beginTransaction();
            this.solicitudes = solicitudDao.getAll(this.session);
            this.transaction.commit();
            return this.solicitudes;
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

    public void setSolicitudes(List<Solicitud> solicitudes) {
        this.solicitudes = solicitudes;
    }
    
    public List<String> getEstados() {
        this.session = null;
        this.transaction = null;
        
        try {
            this.session = NewHibernateUtil.getSessionFactory().openSession();
            this.estados = new ArrayList<>();
            IEstadoDao estadoDao = new EstadoDao();
            this.transaction = this.session.beginTransaction();
            List<Estado> estadosTemp = estadoDao.getAll(this.session);
            for (Estado estado : estadosTemp) {
                this.estados.add(estado.getDescripcion());
            }
            this.transaction.commit();
            
            return this.estados;
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

    public void setEstados(List<String> estados) {
        this.estados = estados;
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
            return this.idsSolicitud;
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

    public void setIdsSolicitud(List<String> idsSolicitud) {
        this.idsSolicitud = idsSolicitud;
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
            List<Servicio> servicios = areaServicioDao.getServiciosByArea(this.session, getIdArea());
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
            this.solicitud.setUsuario(usuario);
            Servicio servicio = new Servicio();
            servicio.setIdServicio(Integer.parseInt(this.idServicio));
            this.solicitud.setServicio(servicio);
            Estado estado = new Estado();
            estado.setIdEstado(1);
            this.solicitud.setEstado(estado);
            Operario operario = new Operario();
            operario.setIdOperario(1);
            this.solicitud.setOperario(operario);
            this.transaction = this.session.beginTransaction();
            solicitudDao.insertarSolicitud(this.session, this.solicitud);
            this.transaction.commit();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(
                    FacesMessage.SEVERITY_INFO, "Correcto", "Servicio registrado correctamente"));
            RequestContext.getCurrentInstance().update("servicioForm:mensajeGeneral");
            this.solicitud = new Solicitud();
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
    
    public void eliminarServicio() {
        this.session = null;
        this.transaction = null;
        
        try {
            this.session = NewHibernateUtil.getSessionFactory().openSession();
            ISolicitudDao solicitudDao = new SolicitudDao();
            this.transaction = this.session.beginTransaction();
            solicitudDao.eliminarSolicitud(this.session, this.solicitud);
            this.transaction.commit();
            this.solicitud = new Solicitud();
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
