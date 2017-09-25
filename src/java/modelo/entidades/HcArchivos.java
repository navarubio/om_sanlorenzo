/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo.entidades;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Cristhian
 */
@Entity
@Table(name = "hc_archivos", schema = "public")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "HcArchivos.findAll", query = "SELECT h FROM HcArchivos h"),
    @NamedQuery(name = "HcArchivos.findByUrlFile", query = "SELECT h FROM HcArchivos h WHERE h.urlFile = :urlFile"),
    @NamedQuery(name = "HcArchivos.findByFechaUpload", query = "SELECT h FROM HcArchivos h WHERE h.fechaUpload = :fechaUpload"),
    @NamedQuery(name = "HcArchivos.findByDescripcion", query = "SELECT h FROM HcArchivos h WHERE h.descripcion = :descripcion"),
    @NamedQuery(name = "HcArchivos.findByIdArchivo", query = "SELECT h FROM HcArchivos h WHERE h.idArchivo = :idArchivo")})
public class HcArchivos implements Serializable {

    private static final long serialVersionUID = 1L;
    @Column(name = "url_file", length = 2147483647)
    private String urlFile;
    @Column(name = "fecha_upload")
    @Temporal(TemporalType.DATE)
    private Date fechaUpload;
    @Column(length = 2147483647)
    private String descripcion;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_archivo", nullable = false)
    private Long idArchivo;
    @JoinColumn(name = "id_paciente", referencedColumnName = "id_paciente")
    @ManyToOne(fetch = FetchType.LAZY)
    private CfgPacientes idPaciente;

    public HcArchivos() {
    }

    public HcArchivos(Long idArchivo) {
        this.idArchivo = idArchivo;
    }

    public String getUrlFile() {
        return urlFile;
    }

    public void setUrlFile(String urlFile) {
        this.urlFile = urlFile;
    }

    public Date getFechaUpload() {
        return fechaUpload;
    }

    public void setFechaUpload(Date fechaUpload) {
        this.fechaUpload = fechaUpload;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Long getIdArchivo() {
        return idArchivo;
    }

    public void setIdArchivo(Long idArchivo) {
        this.idArchivo = idArchivo;
    }

    public CfgPacientes getIdPaciente() {
        return idPaciente;
    }

    public void setIdPaciente(CfgPacientes idPaciente) {
        this.idPaciente = idPaciente;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idArchivo != null ? idArchivo.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof HcArchivos)) {
            return false;
        }
        HcArchivos other = (HcArchivos) object;
        if ((this.idArchivo == null && other.idArchivo != null) || (this.idArchivo != null && !this.idArchivo.equals(other.idArchivo))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "modelo.entidades.HcArchivos[ idArchivo=" + idArchivo + " ]";
    }
    
}
