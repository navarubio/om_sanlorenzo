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
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author usuario
 */
@Entity
@Table(name = "hc_rep_examenes")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "HcRepExamenes.findAll", query = "SELECT h FROM HcRepExamenes h"),
    @NamedQuery(name = "HcRepExamenes.findByIdRepExamenes", query = "SELECT h FROM HcRepExamenes h WHERE h.idRepExamenes = :idRepExamenes"),
    @NamedQuery(name = "HcRepExamenes.findByPosicion", query = "SELECT h FROM HcRepExamenes h WHERE h.posicion = :posicion"),
    @NamedQuery(name = "HcRepExamenes.findByNombreParaclinico", query = "SELECT h FROM HcRepExamenes h WHERE h.nombreParaclinico = :nombreParaclinico"),
    @NamedQuery(name = "HcRepExamenes.findByFecha", query = "SELECT h FROM HcRepExamenes h WHERE h.fecha = :fecha"),
    @NamedQuery(name = "HcRepExamenes.findByResultado", query = "SELECT h FROM HcRepExamenes h WHERE h.resultado = :resultado")})
public class HcRepExamenes implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_rep_examenes")
    private Integer idRepExamenes;
    @JoinColumn(name = "id_registro", referencedColumnName = "id_registro")
    @ManyToOne
    private HcRegistro idRegistro;
    @Column(name = "posicion")
    private Integer posicion;
    @Column(name = "nombre_paraclinico")
    private Integer nombreParaclinico;
    @Column(name = "fecha")
    @Temporal(TemporalType.DATE)
    private Date fecha;
    @Size(max = 2147483647)
    @Column(name = "resultado")
    private String resultado;
    @Column(name="id_sede", columnDefinition = "1")
    private Integer idSede=1;
    public HcRepExamenes() {
    }

    public HcRepExamenes(Integer idRepExamenes) {
        this.idRepExamenes = idRepExamenes;
    }

    public Integer getIdRepExamenes() {
        return idRepExamenes;
    }

    public void setIdRepExamenes(Integer idRepExamenes) {
        this.idRepExamenes = idRepExamenes;
    }

    public Integer getPosicion() {
        return posicion;
    }

    public void setPosicion(Integer posicion) {
        this.posicion = posicion;
    }

    public Integer getNombreParaclinico() {
        return nombreParaclinico;
    }

    public void setNombreParaclinico(Integer nombreParaclinico) {
        this.nombreParaclinico = nombreParaclinico;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public String getResultado() {
        return resultado;
    }

    public void setResultado(String resultado) {
        this.resultado = resultado;
    }

    public HcRegistro getIdRegistro() {
        return idRegistro;
    }

    public void setIdRegistro(HcRegistro idRegistro) {
        this.idRegistro = idRegistro;
    }

    public Integer getIdSede() {
        return idSede;
    }

    public void setIdSede(Integer idSede) {
        this.idSede = idSede;
    }
    
    

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idRepExamenes != null ? idRepExamenes.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof HcRepExamenes)) {
            return false;
        }
        HcRepExamenes other = (HcRepExamenes) object;
        if ((this.idRepExamenes == null && other.idRepExamenes != null) || (this.idRepExamenes != null && !this.idRepExamenes.equals(other.idRepExamenes))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "modelo.entidades.HcRepExamenes[ idRepExamenes=" + idRepExamenes + " ]";
    }
    
}
