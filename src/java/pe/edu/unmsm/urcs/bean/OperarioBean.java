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
import pe.edu.unmsm.urcs.dao.OperarioDao;
import pe.edu.unmsm.urcs.interfaces.IAreaServicioDao;
import pe.edu.unmsm.urcs.interfaces.IOperarioDao;
import pe.edu.unmsm.urcs.modelo.Area;
import pe.edu.unmsm.urcs.modelo.Operario;
import pe.edu.unmsm.urcs.persistencia.NewHibernateUtil;

/**
 *
 * @author SILVIA
 */
@ManagedBean
@ViewScoped
public class OperarioBean implements Serializable{

    Session session;
    Transaction transaction;
    private Operario operario;
    private Area area;
    private String idArea;
    private List<SelectItem> selectItemsOneArea;
    private List<Operario> operarios;
    private final HttpServletRequest httpServletRequest;
    private final FacesContext facesContext;
    
    public OperarioBean() {
        this.operario = new Operario();
        this.area = new Area();
        facesContext = FacesContext.getCurrentInstance();
        httpServletRequest = (HttpServletRequest) facesContext.getExternalContext().getRequest();
    }

    public Operario getOperario() {
        return operario;
    }

    public void setOperario(Operario operario) {
        this.operario = operario;
    }

    public Area getArea() {
        return area;
    }

    public void setArea(Area area) {
        this.area = area;
    }

    public String getIdArea() {
        return idArea;
    }

    public void setIdArea(String idArea) {
        this.idArea = idArea;
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
    
    public void registrarOperario() {
        this.session = null;
        this.transaction = null;
        try {
            this.session = NewHibernateUtil.getSessionFactory().openSession();
            IOperarioDao operarioDao = new OperarioDao();
            this.area.setIdArea(Integer.parseInt(idArea));
            this.operario.setArea(this.area);
            this.transaction = this.session.beginTransaction();
            operarioDao.insertarOperario(this.session, operario);
            this.transaction.commit();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(
                    FacesMessage.SEVERITY_INFO, "Correcto", "Operario registrado correctamente"));
            RequestContext.getCurrentInstance().update("operarioForm:mensajeGeneral");
        } catch (Exception ex) {
            if (this.transaction != null) {
                transaction.rollback();
            }
            ex.printStackTrace();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(
                    FacesMessage.SEVERITY_FATAL, "Error", ex.getMessage()));
        }
        finally {
            if(this.session != null) {
                this.session.close();
            }
        }
    }

    /**
     * @return the operarios
     */
    public List<Operario> getOperarios() {
        this.session = null;
        this.transaction = null;
        
        try {
            this.session = NewHibernateUtil.getSessionFactory().openSession();
            IOperarioDao operarioDao = new OperarioDao();
            this.transaction = this.session.beginTransaction();
            this.operarios = operarioDao.getAll(this.session);
            this.transaction.commit();
            return this.operarios;
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

    /**
     * @param operarios the operarios to set
     */
    public void setOperarios(List<Operario> operarios) {
        this.operarios = operarios;
    }
}
