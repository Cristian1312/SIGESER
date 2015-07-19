/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.edu.unmsm.urcs.bean;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javafx.event.ActionEvent;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.hibernate.Session;
import org.hibernate.Transaction;
import pe.edu.unmsm.urcs.dao.SolicitudDao;
import pe.edu.unmsm.urcs.interfaces.ISolicitudDao;
import pe.edu.unmsm.urcs.modelo.Solicitud;
import pe.edu.unmsm.urcs.modelo.Usuario;
import pe.edu.unmsm.urcs.persistencia.NewHibernateUtil;

/**
 *
 * @author Cristian1312
 */
@ManagedBean
@SessionScoped
public class SolicitudesFinalizadasBean implements Serializable {

    Session session;
    Transaction transaction;
    Usuario usuario;
    private List<String> idsSolicitud;
    private List<String> servicios;
    private List<Solicitud> solicitudesFinalizadas;
    
    public SolicitudesFinalizadasBean() {
        this.usuario = (Usuario) FacesContext.getCurrentInstance().getExternalContext()
                .getSessionMap().get("usuario");
    }

    public List<String> getServicios() {
        this.session = null;
        this.transaction = null;
        
        try {
            this.session = NewHibernateUtil.getSessionFactory().openSession();
            this.servicios = new ArrayList<>();
            ISolicitudDao solicitudDao = new SolicitudDao();
            this.transaction = this.session.beginTransaction();
            List<Solicitud> solicitudesTemp = solicitudDao.getSolicitudesFinalizadas(this.session);
            for (Solicitud sol : solicitudesTemp) {
                this.servicios.add(sol.getServicio().getDescripcion());
            }
            this.transaction.commit();
            
            return this.servicios;
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

    public void setServicios(List<String> servicios) {
        this.servicios = servicios;
    }

    public List<Solicitud> getSolicitudesFinalizadas() {
        this.session = null;
        this.transaction = null;
        try {
            this.session = NewHibernateUtil.getSessionFactory().openSession();
            ISolicitudDao solicitudDao = new SolicitudDao();
            this.transaction = this.session.beginTransaction();
            this.solicitudesFinalizadas = solicitudDao.getSolicitudesFinalizadas(this.session);
            this.transaction.commit();
            return this.solicitudesFinalizadas;
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
    
    public List<Solicitud> getSolicitudFinalizadaParaInforme(int idSolicitud) {
        this.session = null;
        this.transaction = null;
        try {
            this.session = NewHibernateUtil.getSessionFactory().openSession();
            ISolicitudDao solicitudDao = new SolicitudDao();
            this.transaction = this.session.beginTransaction();
            List<Solicitud> solicitudFinalizada= solicitudDao.getSolicitudFinalizadaById(this.session,
                    idSolicitud);
            this.transaction.commit();
            return solicitudFinalizada;
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

    public void setSolicitudesFinalizadas(List<Solicitud> solicitudesFinalizadas) {
        this.solicitudesFinalizadas = solicitudesFinalizadas;
    }
    
    public List<String> getIdsSolicitud() {
        this.session = null;
        this.transaction = null;
        
        try {
            this.session = NewHibernateUtil.getSessionFactory().openSession();
            this.idsSolicitud = new ArrayList<>();
            ISolicitudDao solicitudDao = new SolicitudDao();
            this.transaction = this.session.beginTransaction();
            List<Solicitud> solicitudesTemp = solicitudDao.getSolicitudesFinalizadas(this.session);
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

    public void exportarInformePDF(ActionEvent actionEvent, int idSolicitud) throws JRException, IOException {
        Map<String, Object> parametros = new HashMap<String, Object>();
        parametros.put("txtJefa", "Ing. Rosinna Gonzales Calienes");

        File jasper = new File(FacesContext.getCurrentInstance().getExternalContext().getRealPath("/reportes/informeSolicitudesAtendidas.jasper"));
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasper.getPath(), parametros, new JRBeanCollectionDataSource(this.getSolicitudFinalizadaParaInforme(idSolicitud)));

        HttpServletResponse response = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
        response.addHeader("Content-disposition", "attachment; filename=informe.pdf");
        ServletOutputStream stream = response.getOutputStream();

        JasperExportManager.exportReportToPdfStream(jasperPrint, stream);

        stream.flush();
        stream.close();
        FacesContext.getCurrentInstance().responseComplete();
    }
}
