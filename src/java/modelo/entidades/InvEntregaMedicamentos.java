/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo.entidades;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author miguel
 */
@Entity
@Table(name = "inv_entrega_medicamentos")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "InvEntregaMedicamentos.findAll", query = "SELECT i FROM InvEntregaMedicamentos i")
    , @NamedQuery(name = "InvEntregaMedicamentos.findByIdEntrega", query = "SELECT i FROM InvEntregaMedicamentos i WHERE i.idEntrega = :idEntrega")
    , @NamedQuery(name = "InvEntregaMedicamentos.findByNumeroEntrega", query = "SELECT i FROM InvEntregaMedicamentos i WHERE i.numeroEntrega = :numeroEntrega")
    , @NamedQuery(name = "InvEntregaMedicamentos.findByFechaEntrega", query = "SELECT i FROM InvEntregaMedicamentos i WHERE i.fechaEntrega = :fechaEntrega")
    , @NamedQuery(name = "InvEntregaMedicamentos.findByEstado", query = "SELECT i FROM InvEntregaMedicamentos i WHERE i.estado = :estado")
    , @NamedQuery(name = "InvEntregaMedicamentos.findByFechaActualizacion", query = "SELECT i FROM InvEntregaMedicamentos i WHERE i.fechaActualizacion = :fechaActualizacion")})
public class InvEntregaMedicamentos implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_entrega")
    private Integer idEntrega;
    @Size(max = 10)
    @Column(name = "numero_entrega")
    private String numeroEntrega;
    @Column(name = "fecha_entrega")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaEntrega;
    @Column(name = "estado")
    private String estado;
    @Column(name = "fecha_actualizacion")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaActualizacion;
    @Size(max = 1000)
    @Column(name = "observaciones")
    private String observaciones;
    @JoinColumn(name = "id_empresa", referencedColumnName = "cod_empresa")
    @ManyToOne
    private CfgEmpresa idEmpresa;
    @JoinColumn(name = "id_paciente", referencedColumnName = "id_paciente")
    @ManyToOne
    private CfgPacientes idPaciente;
    @JoinColumn(name = "id_sede", referencedColumnName = "id_sede")
    @ManyToOne
    private CfgSede idSede;
    @JoinColumn(name = "usuario_actualiza", referencedColumnName = "id_usuario")
    @ManyToOne
    private CfgUsuarios usuarioActualiza;
    @JoinColumn(name = "usuario_elabora", referencedColumnName = "id_usuario")
    @ManyToOne
    private CfgUsuarios usuarioElabora;
    @JoinColumn(name = "id_historia_clinica", referencedColumnName = "id_registro")
    @ManyToOne
    private HcRegistro idHistoriaClinica;
    @JoinColumn(name = "id_bodega", referencedColumnName = "id_bodega")
    @ManyToOne
    private InvBodegas idBodega;
    @OneToMany(mappedBy = "idEntrega",cascade = CascadeType.PERSIST)
    private List<InvEntregaMedicamentosDetalle> invEntregaMedicamentosDetalleList;
    @OneToMany(mappedBy = "idEntrega")
    private List<InvMovimientos> invMovimientosList;
    public InvEntregaMedicamentos() {
    }

    public InvEntregaMedicamentos(Integer idEntrega) {
        this.idEntrega = idEntrega;
    }

    public Integer getIdEntrega() {
        return idEntrega;
    }

    public void setIdEntrega(Integer idEntrega) {
        this.idEntrega = idEntrega;
    }

    public String getNumeroEntrega() {
        return numeroEntrega;
    }

    public void setNumeroEntrega(String numeroEntrega) {
        this.numeroEntrega = numeroEntrega;
    }

    public Date getFechaEntrega() {
        return fechaEntrega;
    }

    public void setFechaEntrega(Date fechaEntrega) {
        this.fechaEntrega = fechaEntrega;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public Date getFechaActualizacion() {
        return fechaActualizacion;
    }

    public void setFechaActualizacion(Date fechaActualizacion) {
        this.fechaActualizacion = fechaActualizacion;
    }

    public CfgEmpresa getIdEmpresa() {
        return idEmpresa;
    }

    public void setIdEmpresa(CfgEmpresa idEmpresa) {
        this.idEmpresa = idEmpresa;
    }

    public CfgPacientes getIdPaciente() {
        return idPaciente;
    }

    public void setIdPaciente(CfgPacientes idPaciente) {
        this.idPaciente = idPaciente;
    }

    public CfgSede getIdSede() {
        return idSede;
    }

    public void setIdSede(CfgSede idSede) {
        this.idSede = idSede;
    }

    public CfgUsuarios getUsuarioActualiza() {
        return usuarioActualiza;
    }

    public void setUsuarioActualiza(CfgUsuarios usuarioActualiza) {
        this.usuarioActualiza = usuarioActualiza;
    }

    public CfgUsuarios getUsuarioElabora() {
        return usuarioElabora;
    }

    public void setUsuarioElabora(CfgUsuarios usuarioElabora) {
        this.usuarioElabora = usuarioElabora;
    }

    public HcRegistro getIdHistoriaClinica() {
        return idHistoriaClinica;
    }

    public void setIdHistoriaClinica(HcRegistro idHistoriaClinica) {
        this.idHistoriaClinica = idHistoriaClinica;
    }

    public InvBodegas getIdBodega() {
        return idBodega;
    }

    public void setIdBodega(InvBodegas idBodega) {
        this.idBodega = idBodega;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    @XmlTransient
    public List<InvEntregaMedicamentosDetalle> getInvEntregaMedicamentosDetalleList() {
        return invEntregaMedicamentosDetalleList;
    }

    public void setInvEntregaMedicamentosDetalleList(List<InvEntregaMedicamentosDetalle> invEntregaMedicamentosDetalleList) {
        this.invEntregaMedicamentosDetalleList = invEntregaMedicamentosDetalleList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idEntrega != null ? idEntrega.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof InvEntregaMedicamentos)) {
            return false;
        }
        InvEntregaMedicamentos other = (InvEntregaMedicamentos) object;
        if ((this.idEntrega == null && other.idEntrega != null) || (this.idEntrega != null && !this.idEntrega.equals(other.idEntrega))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "modelo.entidades.InvEntregaMedicamentos[ idEntrega=" + idEntrega + " ]";
    }
    @XmlTransient
    public List<InvMovimientos> getInvMovimientosList() {
        return invMovimientosList;
    }

    public void setInvMovimientosList(List<InvMovimientos> invMovimientosList) {
        this.invMovimientosList = invMovimientosList;
    }
    
}
