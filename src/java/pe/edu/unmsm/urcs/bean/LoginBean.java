/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.edu.unmsm.urcs.bean;

import java.io.Serializable;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.primefaces.context.RequestContext;
import pe.edu.unmsm.urcs.dao.UsuarioDao;
import pe.edu.unmsm.urcs.modelo.Usuario;
import pe.edu.unmsm.urcs.persistencia.NewHibernateUtil;

/**
 *
 * @author Cristian1312
 */
@ManagedBean
@SessionScoped
public class LoginBean implements Serializable {

    Session session;
    Transaction transaction;

    private Usuario usuario;
    private final HttpServletRequest httpServletRequest;
    private final FacesContext facesContext;
    private FacesMessage facesMessage;

    public LoginBean() {
        facesContext = FacesContext.getCurrentInstance();
        httpServletRequest = (HttpServletRequest) facesContext.getExternalContext().getRequest();
        if (this.usuario == null) {
            this.usuario = new Usuario();
        }
    }

    public void logIn() {
        this.session = null;
        this.transaction = null;
        RequestContext context = RequestContext.getCurrentInstance();
        String view;

        try {
            this.session = NewHibernateUtil.getSessionFactory().openSession();
            UsuarioDao usuarioDao = new UsuarioDao();
            this.transaction = this.session.beginTransaction();
            this.usuario = usuarioDao.verificarUsuario(this.session, this.usuario);
            if (this.usuario != null) {
                httpServletRequest.getSession().setAttribute("usuario", usuario);
                facesMessage = new FacesMessage(FacesMessage.SEVERITY_INFO, "Bienvenido",
                        this.usuario.getNombre() + " " + this.usuario.getApellido());
                view = "tareasTrabajador.xhtml";
            } else {
                facesMessage = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Login Error",
                        "Usuario y/o contraseña incorrecto");
                view = "index.xhtml";
            }
            this.transaction.commit();
            FacesContext.getCurrentInstance().addMessage(null, facesMessage);
            context.addCallbackParam("view", view);
        } catch (Exception ex) {
            if(this.transaction!=null) {
                transaction.rollback();
            }
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(
                    FacesMessage.SEVERITY_FATAL, "Error", ex.getMessage()));
        } finally {
            if(this.session!=null) {
                this.session.close();
            }
        }
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }
}
