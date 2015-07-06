package pe.edu.unmsm.urcs.modelo;
// Generated 05/07/2015 12:56:44 AM by Hibernate Tools 4.3.1



/**
 * OficinaId generated by hbm2java
 */
public class OficinaId  implements java.io.Serializable {


     private int idOficina;
     private int facultadDependenciaIdFacultadDependencia;

    public OficinaId() {
    }

    public OficinaId(int idOficina, int facultadDependenciaIdFacultadDependencia) {
       this.idOficina = idOficina;
       this.facultadDependenciaIdFacultadDependencia = facultadDependenciaIdFacultadDependencia;
    }
   
    public int getIdOficina() {
        return this.idOficina;
    }
    
    public void setIdOficina(int idOficina) {
        this.idOficina = idOficina;
    }
    public int getFacultadDependenciaIdFacultadDependencia() {
        return this.facultadDependenciaIdFacultadDependencia;
    }
    
    public void setFacultadDependenciaIdFacultadDependencia(int facultadDependenciaIdFacultadDependencia) {
        this.facultadDependenciaIdFacultadDependencia = facultadDependenciaIdFacultadDependencia;
    }


   public boolean equals(Object other) {
         if ( (this == other ) ) return true;
		 if ( (other == null ) ) return false;
		 if ( !(other instanceof OficinaId) ) return false;
		 OficinaId castOther = ( OficinaId ) other; 
         
		 return (this.getIdOficina()==castOther.getIdOficina())
 && (this.getFacultadDependenciaIdFacultadDependencia()==castOther.getFacultadDependenciaIdFacultadDependencia());
   }
   
   public int hashCode() {
         int result = 17;
         
         result = 37 * result + this.getIdOficina();
         result = 37 * result + this.getFacultadDependenciaIdFacultadDependencia();
         return result;
   }   


}


