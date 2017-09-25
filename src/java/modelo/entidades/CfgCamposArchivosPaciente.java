/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo.entidades;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 *
 * @author Familia Ibañez Duran
 */
@Entity
@Table(name = "cfg_campos_archivos_paciente", schema = "public")
public class CfgCamposArchivosPaciente implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_campoarchivo", nullable = false)
    private Integer idCampoarchivo;
    @Column(name = "nombre_campo", length = 45)
    private String nombreCampo;
    @Column(name = "orden")
    private Integer orden;
    @JoinColumn(name = "id_administradora", referencedColumnName = "id_administradora")
    @ManyToOne
    private FacAdministradora idAdministradora;

    public Integer getIdCampoarchivo() {
        return idCampoarchivo;
    }

    public void setIdCampoarchivo(Integer idCampoarchivo) {
        this.idCampoarchivo = idCampoarchivo;
    }

    public String getNombreCampo() {
        return nombreCampo;
    }

    public void setNombreCampo(String nombreCampo) {
        this.nombreCampo = nombreCampo;
    }

    public Integer getOrden() {
        return orden;
    }

    public void setOrden(Integer orden) {
        this.orden = orden;
    }

    public FacAdministradora getIdAdministradora() {
        return idAdministradora;
    }

    public void setIdAdministradora(FacAdministradora idAdministradora) {
        this.idAdministradora = idAdministradora;
    }
    
    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idCampoarchivo != null ? idCampoarchivo.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof CfgCamposArchivosPaciente)) {
            return false;
        }
        CfgCamposArchivosPaciente other = (CfgCamposArchivosPaciente) object;
        if ((this.idCampoarchivo == null && other.idCampoarchivo != null) || (this.idCampoarchivo!= null && !this.idCampoarchivo.equals(other.idCampoarchivo))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "modelo.entidades.CfgCamposArchivosPaciente[ id=" + idCampoarchivo + " ]";
    }
    
}
