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
@Table(name = "hc_3047_anexo4_1")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Hc3047Anexo41.findAll", query = "SELECT h FROM Hc3047Anexo41 h"),
    @NamedQuery(name = "Hc3047Anexo41.findById3047anexo41", query = "SELECT h FROM Hc3047Anexo41 h WHERE h.id3047anexo41 = :id3047anexo41"),
    @NamedQuery(name = "Hc3047Anexo41.findByCantidad", query = "SELECT h FROM Hc3047Anexo41 h WHERE h.cantidad = :cantidad")})
public class Hc3047Anexo41 implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_3047anexo4_1")
    private Integer id3047anexo41;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "cantidad")
    private Double cantidad;
    @JoinColumn(name = "id_servicio", referencedColumnName = "id_servicio")
    @ManyToOne
    private FacServicio idServicio;
    @JoinColumn(name = "id_3047anexo4", referencedColumnName = "id_3047anexo4")
    @ManyToOne
    private Hc3047Anexo4 id3047anexo4;

    public Hc3047Anexo41() {
    }

    public Hc3047Anexo41(Integer id3047anexo41) {
        this.id3047anexo41 = id3047anexo41;
    }

    public Integer getId3047anexo41() {
        return id3047anexo41;
    }

    public void setId3047anexo41(Integer id3047anexo41) {
        this.id3047anexo41 = id3047anexo41;
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

    public Hc3047Anexo4 getId3047anexo4() {
        return id3047anexo4;
    }

    public void setId3047anexo4(Hc3047Anexo4 id3047anexo4) {
        this.id3047anexo4 = id3047anexo4;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id3047anexo41 != null ? id3047anexo41.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Hc3047Anexo41)) {
            return false;
        }
        Hc3047Anexo41 other = (Hc3047Anexo41) object;
        if ((this.id3047anexo41 == null && other.id3047anexo41 != null) || (this.id3047anexo41 != null && !this.id3047anexo41.equals(other.id3047anexo41))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "modelo.entidades.Hc3047Anexo41[ id3047anexo41=" + id3047anexo41 + " ]";
    }
    
}
