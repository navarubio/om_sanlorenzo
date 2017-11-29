/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo.entidades.sinc;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author casc
 */
@Entity
@Table(name = "sin_nodos")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "SinNodos.findAll", query = "SELECT s FROM SinNodos s")})
public class SinNodos implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_nodo")
    private Integer idNodo;
    @Size(max = 40)
    @Column(name = "nombre")
    private String nombre;
    @Size(max = 2147483647)
    @Column(name = "descripcion")
    private String descripcion;
    @Column(name = "tipo")
    private Character tipo;
    @Size(max = 40)
    @Column(name = "usuario_db")
    private String usuarioDb;
    @Column(name = "status")
    private Boolean status;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "sinNodos")
    private List<SinStatus> sinStatusList;

    public SinNodos() {
    }

    public SinNodos(Integer idNodo) {
        this.idNodo = idNodo;
    }

    public Integer getIdNodo() {
        return idNodo;
    }

    public void setIdNodo(Integer idNodo) {
        this.idNodo = idNodo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Character getTipo() {
        return tipo;
    }

    public void setTipo(Character tipo) {
        this.tipo = tipo;
    }

    public String getUsuarioDb() {
        return usuarioDb;
    }

    public void setUsuarioDb(String usuarioDb) {
        this.usuarioDb = usuarioDb;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    @XmlTransient
    public List<SinStatus> getSinStatusList() {
        return sinStatusList;
    }

    public void setSinStatusList(List<SinStatus> sinStatusList) {
        this.sinStatusList = sinStatusList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idNodo != null ? idNodo.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SinNodos)) {
            return false;
        }
        SinNodos other = (SinNodos) object;
        if ((this.idNodo == null && other.idNodo != null) || (this.idNodo != null && !this.idNodo.equals(other.idNodo))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "modelo.entidades.sinc.SinNodos[ idNodo=" + idNodo + " ]";
    }
    
}
