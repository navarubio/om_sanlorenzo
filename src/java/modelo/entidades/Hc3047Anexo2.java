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
 * @author sofimar
 */
@Entity
@Table(name = "hc_3047_anexo2")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Hc3047Anexo2.findAll", query = "SELECT h FROM Hc3047Anexo2 h"),
    @NamedQuery(name = "Hc3047Anexo2.findById3047anexo2", query = "SELECT h FROM Hc3047Anexo2 h WHERE h.id3047anexo2 = :id3047anexo2"),
    @NamedQuery(name = "Hc3047Anexo2.findByNumeroatencion", query = "SELECT h FROM Hc3047Anexo2 h WHERE h.numeroatencion = :numeroatencion"),
    @NamedQuery(name = "Hc3047Anexo2.findByFechadocumento", query = "SELECT h FROM Hc3047Anexo2 h WHERE h.fechadocumento = :fechadocumento"),
    @NamedQuery(name = "Hc3047Anexo2.findByAccidentetransito", query = "SELECT h FROM Hc3047Anexo2 h WHERE h.accidentetransito = :accidentetransito"),
    @NamedQuery(name = "Hc3047Anexo2.findByIdclasificaciontriage", query = "SELECT h FROM Hc3047Anexo2 h WHERE h.idclasificaciontriage = :idclasificaciontriage"),
    @NamedQuery(name = "Hc3047Anexo2.findByFechaingreso", query = "SELECT h FROM Hc3047Anexo2 h WHERE h.fechaingreso = :fechaingreso"),
    @NamedQuery(name = "Hc3047Anexo2.findByReferido", query = "SELECT h FROM Hc3047Anexo2 h WHERE h.referido = :referido"),
    @NamedQuery(name = "Hc3047Anexo2.findByNombreprestador", query = "SELECT h FROM Hc3047Anexo2 h WHERE h.nombreprestador = :nombreprestador"),
    @NamedQuery(name = "Hc3047Anexo2.findByCodigoremitente", query = "SELECT h FROM Hc3047Anexo2 h WHERE h.codigoremitente = :codigoremitente"),
    @NamedQuery(name = "Hc3047Anexo2.findByMotivoconsulta", query = "SELECT h FROM Hc3047Anexo2 h WHERE h.motivoconsulta = :motivoconsulta")})
public class Hc3047Anexo2 implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_3047anexo2")
    private Integer id3047anexo2;
    @Size(max = 10)
    @Column(name = "numeroatencion")
    private String numeroatencion;
    @Column(name = "fechadocumento")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechadocumento;
    @Column(name = "accidentetransito")
    private Boolean accidentetransito;
    @Size(max = 12)
    @Column(name = "idclasificaciontriage")
    private String idclasificaciontriage;
    @Column(name = "fechaingreso")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaingreso;
    @Column(name = "referido")
    private Boolean referido;
    @Size(max = 100)
    @Column(name = "nombreprestador")
    private String nombreprestador;
    @Size(max = 50)
    @Column(name = "codigoremitente")
    private String codigoremitente;
    @Size(max = 255)
    @Column(name = "motivoconsulta")
    private String motivoconsulta;
    @JoinColumn(name = "iddestinopaciente", referencedColumnName = "id")
    @ManyToOne
    private CfgClasificaciones iddestinopaciente;
    @JoinColumn(name = "idorigenatencion", referencedColumnName = "id")
    @ManyToOne
    private CfgClasificaciones idorigenatencion;
    @JoinColumn(name = "iddepartamento", referencedColumnName = "id")
    @ManyToOne
    private CfgClasificaciones iddepartamento;
    @JoinColumn(name = "idmunicipio", referencedColumnName = "id")
    @ManyToOne
    private CfgClasificaciones idmunicipio;
    @JoinColumn(name = "cei10_0", referencedColumnName = "codigo_diagnostico")
    @ManyToOne
    private CfgDiagnostico cei100;
    @JoinColumn(name = "cei10_1", referencedColumnName = "codigo_diagnostico")
    @ManyToOne
    private CfgDiagnostico cei101;
    @JoinColumn(name = "cei10_2", referencedColumnName = "codigo_diagnostico")
    @ManyToOne
    private CfgDiagnostico cei102;
    @JoinColumn(name = "cei10_3", referencedColumnName = "codigo_diagnostico")
    @ManyToOne
    private CfgDiagnostico cei103;
    @JoinColumn(name = "id_paciente", referencedColumnName = "id_paciente")
    @ManyToOne
    private CfgPacientes idPaciente;
    @JoinColumn(name = "id_usuario", referencedColumnName = "id_usuario")
    @ManyToOne
    private CfgUsuarios idUsuario;

    public Hc3047Anexo2() {
    }

    public Hc3047Anexo2(Integer id3047anexo2) {
        this.id3047anexo2 = id3047anexo2;
    }

    public Integer getId3047anexo2() {
        return id3047anexo2;
    }

    public void setId3047anexo2(Integer id3047anexo2) {
        this.id3047anexo2 = id3047anexo2;
    }

    public String getNumeroatencion() {
        return numeroatencion;
    }

    public void setNumeroatencion(String numeroatencion) {
        this.numeroatencion = numeroatencion;
    }

    public Date getFechadocumento() {
        return fechadocumento;
    }

    public void setFechadocumento(Date fechadocumento) {
        this.fechadocumento = fechadocumento;
    }

    public Boolean getAccidentetransito() {
        return accidentetransito;
    }

    public void setAccidentetransito(Boolean accidentetransito) {
        this.accidentetransito = accidentetransito;
    }

    public String getIdclasificaciontriage() {
        return idclasificaciontriage;
    }

    public void setIdclasificaciontriage(String idclasificaciontriage) {
        this.idclasificaciontriage = idclasificaciontriage;
    }

    public Date getFechaingreso() {
        return fechaingreso;
    }

    public void setFechaingreso(Date fechaingreso) {
        this.fechaingreso = fechaingreso;
    }

    public Boolean getReferido() {
        return referido;
    }

    public void setReferido(Boolean referido) {
        this.referido = referido;
    }

    public String getNombreprestador() {
        return nombreprestador;
    }

    public void setNombreprestador(String nombreprestador) {
        this.nombreprestador = nombreprestador;
    }

    public String getCodigoremitente() {
        return codigoremitente;
    }

    public void setCodigoremitente(String codigoremitente) {
        this.codigoremitente = codigoremitente;
    }

    public String getMotivoconsulta() {
        return motivoconsulta;
    }

    public void setMotivoconsulta(String motivoconsulta) {
        this.motivoconsulta = motivoconsulta;
    }

    public CfgClasificaciones getIddestinopaciente() {
        return iddestinopaciente;
    }

    public void setIddestinopaciente(CfgClasificaciones iddestinopaciente) {
        this.iddestinopaciente = iddestinopaciente;
    }

    public CfgClasificaciones getIdorigenatencion() {
        return idorigenatencion;
    }

    public void setIdorigenatencion(CfgClasificaciones idorigenatencion) {
        this.idorigenatencion = idorigenatencion;
    }

    public CfgClasificaciones getIddepartamento() {
        return iddepartamento;
    }

    public void setIddepartamento(CfgClasificaciones iddepartamento) {
        this.iddepartamento = iddepartamento;
    }

    public CfgClasificaciones getIdmunicipio() {
        return idmunicipio;
    }

    public void setIdmunicipio(CfgClasificaciones idmunicipio) {
        this.idmunicipio = idmunicipio;
    }

    public CfgDiagnostico getCei100() {
        return cei100;
    }

    public void setCei100(CfgDiagnostico cei100) {
        this.cei100 = cei100;
    }

    public CfgDiagnostico getCei101() {
        return cei101;
    }

    public void setCei101(CfgDiagnostico cei101) {
        this.cei101 = cei101;
    }

    public CfgDiagnostico getCei102() {
        return cei102;
    }

    public void setCei102(CfgDiagnostico cei102) {
        this.cei102 = cei102;
    }

    public CfgDiagnostico getCei103() {
        return cei103;
    }

    public void setCei103(CfgDiagnostico cei103) {
        this.cei103 = cei103;
    }

    public CfgPacientes getIdPaciente() {
        return idPaciente;
    }

    public void setIdPaciente(CfgPacientes idPaciente) {
        this.idPaciente = idPaciente;
    }

    public CfgUsuarios getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(CfgUsuarios idUsuario) {
        this.idUsuario = idUsuario;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id3047anexo2 != null ? id3047anexo2.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Hc3047Anexo2)) {
            return false;
        }
        Hc3047Anexo2 other = (Hc3047Anexo2) object;
        if ((this.id3047anexo2 == null && other.id3047anexo2 != null) || (this.id3047anexo2 != null && !this.id3047anexo2.equals(other.id3047anexo2))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "modelo.entidades.Hc3047Anexo2[ id3047anexo2=" + id3047anexo2 + " ]";
    }
    
}
