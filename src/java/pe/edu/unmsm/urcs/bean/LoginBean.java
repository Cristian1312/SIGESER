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
    private FacesMessage facesMessage;

    public LoginBean() {
        this.usuario = new Usuario();
    }

    public void login() {
        this.session = null;
        this.transaction = null;
        RequestContext context = RequestContext.getCurrentInstance();
        String view = "";
        int perfil;

        try {
            this.session = NewHibernateUtil.getSessionFactory().openSession();
            UsuarioDao usuarioDao = new UsuarioDao();
            this.transaction = this.session.beginTransaction();
            this.usuario = usuarioDao.verificarUsuario(this.session, this.usuario);
            if (this.usuario != null) {
                FacesContext.getCurrentInstance().getExternalContext()
                        .getSessionMap().put("usuario", this.usuario);
                facesMessage = new FacesMessage(FacesMessage.SEVERITY_INFO, "Bienvenido",
                        this.usuario.getNombre() + " " + this.usuario.getApellido());
                perfil = this.usuario.getPerfil().getIdPerfil();
                switch (perfil) {
                    case 1: view = "tareasTrabajador.xhtml";break;
                    case 2: view = "tareasJefaUrcs.xhtml";break;
                }
            } else {
                facesMessage = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Login Error",
                        "Usuario y/o contraseña incorrecto");
                view = "index.xhtml";
            }
            this.transaction.commit();
            FacesContext.getCurrentInstance().addMessage(null, facesMessage);
            context.addCallbackParam("view", view);
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

    public void cerrarSesion() {
        RequestContext context = RequestContext.getCurrentInstance();
        FacesContext.getCurrentInstance().getExternalContext()
                .invalidateSession();
        context.addCallbackParam("view", "index.xhtml");
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }
}
