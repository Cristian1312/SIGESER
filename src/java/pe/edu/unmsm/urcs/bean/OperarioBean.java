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
import pe.edu.unmsm.urcs.dao.UsuarioDao;
import pe.edu.unmsm.urcs.interfaces.IAreaServicioDao;
import pe.edu.unmsm.urcs.interfaces.IOperarioDao;
import pe.edu.unmsm.urcs.modelo.Area;
import pe.edu.unmsm.urcs.modelo.Operario;
import pe.edu.unmsm.urcs.modelo.Perfil;
import pe.edu.unmsm.urcs.modelo.Usuario;
import pe.edu.unmsm.urcs.persistencia.NewHibernateUtil;

/**
 *
 * @author SILVIA
 */
@ManagedBean
@ViewScoped
public class OperarioBean implements Serializable {

    Session session;
    Transaction transaction;

    private Usuario usuario;
    private Operario operario;
    private Area area;
    private String idArea;
    private List<SelectItem> selectItemsOneArea;
    private List<Operario> operarios;
    private final HttpServletRequest httpServletRequest;
    private final FacesContext facesContext;
    private Perfil perfil;
    private FacesMessage facesMessage;

    public OperarioBean() {
        this.operario = new Operario();
        this.area = new Area();
        facesContext = FacesContext.getCurrentInstance();
        httpServletRequest = (HttpServletRequest) facesContext.getExternalContext().getRequest();
        this.perfil = new Perfil();
        this.usuario = new Usuario();
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

    public void setSelectItemsOneArea(List<SelectItem> selectItemsOneArea) {
        this.selectItemsOneArea = selectItemsOneArea;
    }

    public void setOperarios(List<Operario> operarios) {
        this.operarios = operarios;
    }

    public Perfil getPerfil() {
        return perfil;
    }

    public void setPerfil(Perfil perfil) {
        this.perfil = perfil;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public void registrarOperario() {
        this.session = null;
        this.transaction = null;
        try {
            this.session = NewHibernateUtil.getSessionFactory().openSession();
            UsuarioDao usuarioDao = new UsuarioDao();
            IOperarioDao operarioDao = new OperarioDao();
            this.transaction = this.session.beginTransaction();
            this.usuario = usuarioDao.verificarCorreo(this.session, this.usuario);
            if (this.usuario != null) {
                if (correoexiste(this.usuario.getEmail()) == false) {
                    this.area.setIdArea(Integer.parseInt(idArea));
                    this.perfil.setIdPerfil(3);
                    this.usuario.setPerfil(this.perfil);
                    this.operario.setArea(this.area);
                    this.operario.setCorreo(this.usuario.getEmail());
                    operarioDao.insertarOperario(this.session, this.operario);
                    this.transaction.commit();
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(
                            FacesMessage.SEVERITY_INFO, "Correcto", "Operario registrado correctamente"));
                    RequestContext.getCurrentInstance().update("operarioForm:mensajeGeneral");
                    this.operario = new Operario();
                    this.usuario = new Usuario();
                } else {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error en el Registro", "Correo ya existe"));
                    this.operario = new Operario();
                    this.usuario = new Usuario();
                }
            } else {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error en el Registro", "Correo Inválido"));
                this.operario = new Operario();
                this.usuario = new Usuario();
            }
        } catch (Exception ex) {
            if (this.transaction != null) {
                transaction.rollback();
            }
            ex.printStackTrace();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(
                    FacesMessage.SEVERITY_FATAL, "Error", ex.getMessage()));
        } finally {
            if (this.session != null) {
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

    public void modificarOperario() {
        this.session = null;
        this.transaction = null;

        try {
            this.session = NewHibernateUtil.getSessionFactory().openSession();
            IOperarioDao operarioDao = new OperarioDao();
            this.transaction = this.session.beginTransaction();
            this.area.setIdArea(Integer.parseInt(idArea));
            this.operario.setArea(this.area);
            operarioDao.modificarOperario(this.session, this.operario);
            this.transaction.commit();
            this.operario = new Operario();
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

    public void eliminarOperario() {
        this.session = null;
        this.transaction = null;

        try {
            this.session = NewHibernateUtil.getSessionFactory().openSession();
            IOperarioDao operarioDao = new OperarioDao();
            UsuarioDao usuarioDao = new UsuarioDao();
            this.transaction = this.session.beginTransaction();

            this.usuario.setEmail(this.operario.getCorreo());
            if (this.usuario.getEmail().equals(this.operario.getCorreo())) {
                this.usuario = usuarioDao.verificarCorreo(this.session, this.usuario);
                this.perfil.setIdPerfil(1);
                this.usuario.setPerfil(this.perfil);
                usuarioDao.modificarUsuario(this.session, this.usuario);
                operarioDao.eliminarOperario(this.session, this.operario);
                this.operario = new Operario();
                this.usuario = new Usuario();
            }
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

    public boolean correoexiste(String correo) {
        for (Operario ope : this.operarios) {
            if (correo.equals(ope.getCorreo())) {
                return true;
            }
        }
        return false;
    }

}
