/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo.entidades;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Enderson
 */
@Entity
@XmlRootElement
@Table(name = "urg_orden_cobro", schema = "public")

public class UrgOrdenCobro implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_orden_cobro", nullable = false)
    private Integer idCobro;
    @Column(name = "hora")
    @Temporal(TemporalType.TIME)
    private Date hora;
    @Column(name = "fecha")
    @Temporal(TemporalType.DATE)
    private Date fecha;
    @JoinColumn(name = "id_consulta")
    @OneToOne
    private UrgConsultaPacienteUrgencia idUrgConsultaPacienteUrgencia;
    @OneToMany(mappedBy = "urgOrdenCobro", cascade = CascadeType.ALL)
    private List<FacConsumoMedicamento> facConsumoMedicamentolist;
    @OneToMany(mappedBy = "urgOrdenCobro", cascade = CascadeType.ALL)
    private List<FacConsumoInsumo> facConsumoInsumolist;
    @OneToMany(mappedBy = "urgOrdenCobro", cascade = CascadeType.ALL)
    private List<FacConsumoServicio> facConsumoServiciolist;
    @OneToMany(mappedBy = "urgOrdenCobro", cascade = CascadeType.ALL)
    private List<FacConsumoPaquete> facConsumoPaquetelist;

    public UrgOrdenCobro() {
    }

    public UrgOrdenCobro(Integer idCobro) {
        this.idCobro = idCobro;
    }

    public Integer getIdCobro() {
        return idCobro;
    }

    public void setIdCobro(Integer idCobro) {
        this.idCobro = idCobro;
    }

    public Date getHora() {
        return hora;
    }

    public void setHora(Date hora) {
        this.hora = hora;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public UrgConsultaPacienteUrgencia getIdUrgConsultaPacienteUrgencia() {
        return idUrgConsultaPacienteUrgencia;
    }

    public void setIdUrgConsultaPacienteUrgencia(UrgConsultaPacienteUrgencia idUrgConsultaPacienteUrgencia) {
        this.idUrgConsultaPacienteUrgencia = idUrgConsultaPacienteUrgencia;
    }

    @XmlTransient
    public List<FacConsumoMedicamento> getFacConsumoMedicamentolist() {
        return facConsumoMedicamentolist;
    }

    public void setFacConsumoMedicamentolist(List<FacConsumoMedicamento> facConsumoMedicamentolist) {
        this.facConsumoMedicamentolist = facConsumoMedicamentolist;
    }

    @XmlTransient
    public List<FacConsumoInsumo> getFacConsumoInsumolist() {
        return facConsumoInsumolist;
    }

    public void setFacConsumoInsumolist(List<FacConsumoInsumo> facConsumoInsumolist) {
        this.facConsumoInsumolist = facConsumoInsumolist;
    }

    @XmlTransient
    public List<FacConsumoServicio> getFacConsumoServiciolist() {
        return facConsumoServiciolist;
    }

    public void setFacConsumoServiciolist(List<FacConsumoServicio> facConsumoServiciolist) {
        this.facConsumoServiciolist = facConsumoServiciolist;
    }

    @XmlTransient
    public List<FacConsumoPaquete> getFacConsumoPaquetelist() {
        return facConsumoPaquetelist;
    }

    public void setFacConsumoPaquetelist(List<FacConsumoPaquete> facConsumoPaquetelist) {
        this.facConsumoPaquetelist = facConsumoPaquetelist;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 67 * hash + Objects.hashCode(this.idCobro);
        hash = 67 * hash + Objects.hashCode(this.idUrgConsultaPacienteUrgencia);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final UrgOrdenCobro other = (UrgOrdenCobro) obj;
        if (!Objects.equals(this.idCobro, other.idCobro)) {
            return false;
        }
        if (!Objects.equals(this.idUrgConsultaPacienteUrgencia, other.idUrgConsultaPacienteUrgencia)) {
            return false;
        }
        return true;
    }

}
