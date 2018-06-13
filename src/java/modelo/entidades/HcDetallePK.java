/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo.entidades;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 *
 * @author santos
 */
@Embeddable
public class HcDetallePK implements Serializable {
    @Basic(optional = false)
    @Column(name = "id_registro", nullable = false)
    private int idRegistro;
    @Basic(optional = false)
    @Column(name = "id_campo", nullable = false)
    private int idCampo;
    @Basic(optional = false)
    @Column(name = "id_sede", nullable = false, columnDefinition = "1")
    private int idSede=1;
    public HcDetallePK() {
    }

    public HcDetallePK(int idRegistro, int idCampo, int idSede) {
        this.idRegistro = idRegistro;
        this.idCampo = idCampo;
        this.idSede = idSede;
    }

    public int getIdRegistro() {
        return idRegistro;
    }

    public void setIdRegistro(int idRegistro) {
        this.idRegistro = idRegistro;
    }

    public int getIdCampo() {
        return idCampo;
    }

    public void setIdCampo(int idCampo) {
        this.idCampo = idCampo;
    }

    public int getIdSede() {
        return idSede;
    }

    public void setIdSede(int idSede) {
        this.idSede = idSede;
    }

     
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) idRegistro;
        hash += (int) idCampo;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof HcDetallePK)) {
            return false;
        }
        HcDetallePK other = (HcDetallePK) object;
        if (this.idRegistro != other.idRegistro) {
            return false;
        }
        if (this.idCampo != other.idCampo) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entidades2.HcDetallePK[ idRegistro=" + idRegistro + ", idCampo=" + idCampo + " ]";
    }
    
}
