/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo.entidades.sinc;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;

/**
 *
 * @author casc
 */
@Embeddable
public class SinStatusPK implements Serializable {

    @Basic(optional = false)
    @NotNull
    @Column(name = "id_tabla")
    private int idTabla;
    @Basic(optional = false)
    @NotNull
    @Column(name = "id_local")
    private int idLocal;
    @Basic(optional = false)
    @NotNull
    @Column(name = "id_nodo")
    private int idNodo;

    public SinStatusPK() {
    }

    public SinStatusPK(int idTabla, int idLocal, int idNodo) {
        this.idTabla = idTabla;
        this.idLocal = idLocal;
        this.idNodo = idNodo;
    }

    public int getIdTabla() {
        return idTabla;
    }

    public void setIdTabla(int idTabla) {
        this.idTabla = idTabla;
    }

    public int getIdLocal() {
        return idLocal;
    }

    public void setIdLocal(int idLocal) {
        this.idLocal = idLocal;
    }

    public int getIdNodo() {
        return idNodo;
    }

    public void setIdNodo(int idNodo) {
        this.idNodo = idNodo;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) idTabla;
        hash += (int) idLocal;
        hash += (int) idNodo;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SinStatusPK)) {
            return false;
        }
        SinStatusPK other = (SinStatusPK) object;
        if (this.idTabla != other.idTabla) {
            return false;
        }
        if (this.idLocal != other.idLocal) {
            return false;
        }
        if (this.idNodo != other.idNodo) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "modelo.entidades.sinc.SinStatusPK[ idTabla=" + idTabla + ", idLocal=" + idLocal + ", idNodo=" + idNodo + " ]";
    }
    
}
