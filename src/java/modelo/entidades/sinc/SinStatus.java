/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo.entidades.sinc;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author casc
 */
@Entity
@Table(name = "sin_status")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "SinStatus.findAll", query = "SELECT s FROM SinStatus s")})
public class SinStatus implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected SinStatusPK sinStatusPK;
    @Column(name = "id_remoto")
    private Integer idRemoto;
    @Column(name = "status")
    private Boolean status;
    @JoinColumn(name = "id_nodo", referencedColumnName = "id_nodo", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private SinNodos sinNodos;
    @JoinColumn(name = "id_tabla", referencedColumnName = "id_tabla", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private SinTablas sinTablas;

    public SinStatus() {
    }

    public SinStatus(SinStatusPK sinStatusPK) {
        this.sinStatusPK = sinStatusPK;
    }

    public SinStatus(int idTabla, int idLocal, int idNodo) {
        this.sinStatusPK = new SinStatusPK(idTabla, idLocal, idNodo);
    }

    public SinStatusPK getSinStatusPK() {
        return sinStatusPK;
    }

    public void setSinStatusPK(SinStatusPK sinStatusPK) {
        this.sinStatusPK = sinStatusPK;
    }

    public Integer getIdRemoto() {
        return idRemoto;
    }

    public void setIdRemoto(Integer idRemoto) {
        this.idRemoto = idRemoto;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public SinNodos getSinNodos() {
        return sinNodos;
    }

    public void setSinNodos(SinNodos sinNodos) {
        this.sinNodos = sinNodos;
    }

    public SinTablas getSinTablas() {
        return sinTablas;
    }

    public void setSinTablas(SinTablas sinTablas) {
        this.sinTablas = sinTablas;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (sinStatusPK != null ? sinStatusPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SinStatus)) {
            return false;
        }
        SinStatus other = (SinStatus) object;
        if ((this.sinStatusPK == null && other.sinStatusPK != null) || (this.sinStatusPK != null && !this.sinStatusPK.equals(other.sinStatusPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "modelo.entidades.sinc.SinStatus[ sinStatusPK=" + sinStatusPK + " ]";
    }
    
}
