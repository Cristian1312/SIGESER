package pe.edu.unmsm.urcs.modelo;
// Generated 05/07/2015 12:56:44 AM by Hibernate Tools 4.3.1



/**
 * Informe generated by hbm2java
 */
public class Informe  implements java.io.Serializable {


     private Integer idInforme;
     private String descripcion;

    public Informe() {
    }

    public Informe(String descripcion) {
       this.descripcion = descripcion;
    }
   
    public Integer getIdInforme() {
        return this.idInforme;
    }
    
    public void setIdInforme(Integer idInforme) {
        this.idInforme = idInforme;
    }
    public String getDescripcion() {
        return this.descripcion;
    }
    
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }




}


