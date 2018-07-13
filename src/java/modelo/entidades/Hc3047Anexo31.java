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
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author sofimar
 */
@Entity
@Table(name = "hc_3047_anexo3_1")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Hc3047Anexo31.findAll", query = "SELECT h FROM Hc3047Anexo31 h"),
    @NamedQuery(name = "Hc3047Anexo31.findById3047anexo31", query = "SELECT h FROM Hc3047Anexo31 h WHERE h.id3047anexo31 = :id3047anexo31"),
    @NamedQuery(name = "Hc3047Anexo31.findByCantidad", query = "SELECT h FROM Hc3047Anexo31 h WHERE h.cantidad = :cantidad")})
public class Hc3047Anexo31 implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_3047anexo3_1")
    private Integer id3047anexo31;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "cantidad")
    private Double cantidad;
    @JoinColumn(name = "id_servicio", referencedColumnName = "id_servicio")
    @ManyToOne
    private FacServicio idServicio;
    @JoinColumn(name = "id_3047anexo3", referencedColumnName = "id_3047anexo3")
    @ManyToOne
    private Hc3047Anexo3 id3047anexo3;

    public Hc3047Anexo31() {
    }

    public Hc3047Anexo31(Integer id3047anexo31) {
        this.id3047anexo31 = id3047anexo31;
    }

    public Integer getId3047anexo31() {
        return id3047anexo31;
    }

    public void setId3047anexo31(Integer id3047anexo31) {
        this.id3047anexo31 = id3047anexo31;
    }

    public Double getCantidad() {
        return cantidad;
    }

    public void setCantidad(Double cantidad) {
        this.cantidad = cantidad;
    }

    public FacServicio getIdServicio() {
        return idServicio;
    }

    public void setIdServicio(FacServicio idServicio) {
        this.idServicio = idServicio;
    }

    public Hc3047Anexo3 getId3047anexo3() {
        return id3047anexo3;
    }

    public void setId3047anexo3(Hc3047Anexo3 id3047anexo3) {
        this.id3047anexo3 = id3047anexo3;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id3047anexo31 != null ? id3047anexo31.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Hc3047Anexo31)) {
            return false;
        }
        Hc3047Anexo31 other = (Hc3047Anexo31) object;
        if ((this.id3047anexo31 == null && other.id3047anexo31 != null) || (this.id3047anexo31 != null && !this.id3047anexo31.equals(other.id3047anexo31))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "modelo.entidades.Hc3047Anexo31[ id3047anexo31=" + id3047anexo31 + " ]";
    }
    
}
