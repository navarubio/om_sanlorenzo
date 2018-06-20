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
import javax.persistence.Transient;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author miguel
 */
@Entity
@Table(name = "inv_entrega_medicamentos_detalle")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "InvEntregaMedicamentosDetalle.findAll", query = "SELECT i FROM InvEntregaMedicamentosDetalle i")
    , @NamedQuery(name = "InvEntregaMedicamentosDetalle.findByIdEntregaDetalle", query = "SELECT i FROM InvEntregaMedicamentosDetalle i WHERE i.idEntregaDetalle = :idEntregaDetalle")
    , @NamedQuery(name = "InvEntregaMedicamentosDetalle.findByCantidadRecetada", query = "SELECT i FROM InvEntregaMedicamentosDetalle i WHERE i.cantidadRecetada = :cantidadRecetada")
    , @NamedQuery(name = "InvEntregaMedicamentosDetalle.findByCantidadRecibida", query = "SELECT i FROM InvEntregaMedicamentosDetalle i WHERE i.cantidadRecibida = :cantidadRecibida")
    , @NamedQuery(name = "InvEntregaMedicamentosDetalle.findByObservaciones", query = "SELECT i FROM InvEntregaMedicamentosDetalle i WHERE i.observaciones = :observaciones")})
public class InvEntregaMedicamentosDetalle implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_entrega_detalle")
    private Integer idEntregaDetalle;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "cantidad_recetada")
    private Double cantidadRecetada;
    @Column(name = "cantidad_recibida")
    private Double cantidadRecibida;
    @Size(max = 1000)
    @Column(name = "observaciones")
    private String observaciones;
    @JoinColumn(name = "id_entrega", referencedColumnName = "id_entrega")
    @ManyToOne
    private InvEntregaMedicamentos idEntrega;
    @JoinColumn(name = "id_producto", referencedColumnName = "id_producto")
    @ManyToOne
    private InvProductos idProducto;
    @JoinColumn(name = "id_lote", referencedColumnName = "id_lote")
    @ManyToOne
    private InvLotes idLote;

    @Transient
    private Double existencia;
    public InvEntregaMedicamentosDetalle() {
    }

    public InvEntregaMedicamentosDetalle(Integer idEntregaDetalle) {
        this.idEntregaDetalle = idEntregaDetalle;
    }

    public Integer getIdEntregaDetalle() {
        return idEntregaDetalle;
    }

    public void setIdEntregaDetalle(Integer idEntregaDetalle) {
        this.idEntregaDetalle = idEntregaDetalle;
    }

    public Double getCantidadRecetada() {
        return cantidadRecetada;
    }

    public void setCantidadRecetada(Double cantidadRecetada) {
        this.cantidadRecetada = cantidadRecetada;
    }

    public Double getCantidadRecibida() {
        return cantidadRecibida;
    }

    public void setCantidadRecibida(Double cantidadRecibida) {
        this.cantidadRecibida = cantidadRecibida;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public InvEntregaMedicamentos getIdEntrega() {
        return idEntrega;
    }

    public void setIdEntrega(InvEntregaMedicamentos idEntrega) {
        this.idEntrega = idEntrega;
    }

    public InvProductos getIdProducto() {
        return idProducto;
    }

    public void setIdProducto(InvProductos idProducto) {
        this.idProducto = idProducto;
    }
     public InvLotes getIdLote() {
        return idLote;
    }

    public void setIdLote(InvLotes idLote) {
        this.idLote = idLote;
    }

    public Double getExistencia() {
        return existencia;
    }

    public void setExistencia(Double existencia) {
        this.existencia = existencia;
    }
    

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idEntregaDetalle != null ? idEntregaDetalle.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof InvEntregaMedicamentosDetalle)) {
            return false;
        }
        InvEntregaMedicamentosDetalle other = (InvEntregaMedicamentosDetalle) object;
        if ((this.idEntregaDetalle == null && other.idEntregaDetalle != null) || (this.idEntregaDetalle != null && !this.idEntregaDetalle.equals(other.idEntregaDetalle))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "modelo.entidades.InvEntregaMedicamentosDetalle[ idEntregaDetalle=" + idEntregaDetalle + " ]";
    }
    
}
