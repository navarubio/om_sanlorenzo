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
@Table(name = "hc_3047_anexo5")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Hc3047Anexo5.findAll", query = "SELECT h FROM Hc3047Anexo5 h"),
    @NamedQuery(name = "Hc3047Anexo5.findById3047anexo5", query = "SELECT h FROM Hc3047Anexo5 h WHERE h.id3047anexo5 = :id3047anexo5"),
    @NamedQuery(name = "Hc3047Anexo5.findByNumeroremision", query = "SELECT h FROM Hc3047Anexo5 h WHERE h.numeroremision = :numeroremision"),
    @NamedQuery(name = "Hc3047Anexo5.findByFechadocumento", query = "SELECT h FROM Hc3047Anexo5 h WHERE h.fechadocumento = :fechadocumento"),
    @NamedQuery(name = "Hc3047Anexo5.findByPrimerapellido", query = "SELECT h FROM Hc3047Anexo5 h WHERE h.primerapellido = :primerapellido"),
    @NamedQuery(name = "Hc3047Anexo5.findBySegundoapellido", query = "SELECT h FROM Hc3047Anexo5 h WHERE h.segundoapellido = :segundoapellido"),
    @NamedQuery(name = "Hc3047Anexo5.findByPrimernombre", query = "SELECT h FROM Hc3047Anexo5 h WHERE h.primernombre = :primernombre"),
    @NamedQuery(name = "Hc3047Anexo5.findBySegundonombre", query = "SELECT h FROM Hc3047Anexo5 h WHERE h.segundonombre = :segundonombre"),
    @NamedQuery(name = "Hc3047Anexo5.findByIdentificiacion", query = "SELECT h FROM Hc3047Anexo5 h WHERE h.identificiacion = :identificiacion"),
    @NamedQuery(name = "Hc3047Anexo5.findByDireccion", query = "SELECT h FROM Hc3047Anexo5 h WHERE h.direccion = :direccion"),
    @NamedQuery(name = "Hc3047Anexo5.findByTelefono", query = "SELECT h FROM Hc3047Anexo5 h WHERE h.telefono = :telefono"),
    @NamedQuery(name = "Hc3047Anexo5.findByServicioreferenciasalida", query = "SELECT h FROM Hc3047Anexo5 h WHERE h.servicioreferenciasalida = :servicioreferenciasalida"),
    @NamedQuery(name = "Hc3047Anexo5.findByServicioreferenciallegada", query = "SELECT h FROM Hc3047Anexo5 h WHERE h.servicioreferenciallegada = :servicioreferenciallegada"),
    @NamedQuery(name = "Hc3047Anexo5.findByInformacionrelevante", query = "SELECT h FROM Hc3047Anexo5 h WHERE h.informacionrelevante = :informacionrelevante")})
public class Hc3047Anexo5 implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_3047anexo5")
    private Integer id3047anexo5;
    @Size(max = 10)
    @Column(name = "numeroremision")
    private String numeroremision;
    @Column(name = "fechadocumento")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechadocumento;
    @Size(max = 30)
    @Column(name = "primerapellido")
    private String primerapellido;
    @Size(max = 30)
    @Column(name = "segundoapellido")
    private String segundoapellido;
    @Size(max = 30)
    @Column(name = "primernombre")
    private String primernombre;
    @Size(max = 30)
    @Column(name = "segundonombre")
    private String segundonombre;
    @Size(max = 50)
    @Column(name = "identificiacion")
    private String identificiacion;
    @Size(max = 150)
    @Column(name = "direccion")
    private String direccion;
    @Size(max = 50)
    @Column(name = "telefono")
    private String telefono;
    @Size(max = 150)
    @Column(name = "servicioreferenciasalida")
    private String servicioreferenciasalida;
    @Size(max = 150)
    @Column(name = "servicioreferenciallegada")
    private String servicioreferenciallegada;
    @Size(max = 2147483647)
    @Column(name = "informacionrelevante")
    private String informacionrelevante;
    @JoinColumn(name = "tipo_identificacion", referencedColumnName = "id")
    @ManyToOne
    private CfgClasificaciones tipoIdentificacion;
    @JoinColumn(name = "id_departamento", referencedColumnName = "id")
    @ManyToOne
    private CfgClasificaciones idDepartamento;
    @JoinColumn(name = "id_municipio", referencedColumnName = "id")
    @ManyToOne
    private CfgClasificaciones idMunicipio;
    @JoinColumn(name = "id_paciente", referencedColumnName = "id_paciente")
    @ManyToOne
    private CfgPacientes idPaciente;
    @JoinColumn(name = "idusuario", referencedColumnName = "id_usuario")
    @ManyToOne
    private CfgUsuarios idusuario;

    public Hc3047Anexo5() {
    }

    public Hc3047Anexo5(Integer id3047anexo5) {
        this.id3047anexo5 = id3047anexo5;
    }

    public Integer getId3047anexo5() {
        return id3047anexo5;
    }

    public void setId3047anexo5(Integer id3047anexo5) {
        this.id3047anexo5 = id3047anexo5;
    }

    public String getNumeroremision() {
        return numeroremision;
    }

    public void setNumeroremision(String numeroremision) {
        this.numeroremision = numeroremision;
    }

    public Date getFechadocumento() {
        return fechadocumento;
    }

    public void setFechadocumento(Date fechadocumento) {
        this.fechadocumento = fechadocumento;
    }

    public String getPrimerapellido() {
        return primerapellido;
    }

    public void setPrimerapellido(String primerapellido) {
        this.primerapellido = primerapellido;
    }

    public String getSegundoapellido() {
        return segundoapellido;
    }

    public void setSegundoapellido(String segundoapellido) {
        this.segundoapellido = segundoapellido;
    }

    public String getPrimernombre() {
        return primernombre;
    }

    public void setPrimernombre(String primernombre) {
        this.primernombre = primernombre;
    }

    public String getSegundonombre() {
        return segundonombre;
    }

    public void setSegundonombre(String segundonombre) {
        this.segundonombre = segundonombre;
    }

    public String getIdentificiacion() {
        return identificiacion;
    }

    public void setIdentificiacion(String identificiacion) {
        this.identificiacion = identificiacion;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getServicioreferenciasalida() {
        return servicioreferenciasalida;
    }

    public void setServicioreferenciasalida(String servicioreferenciasalida) {
        this.servicioreferenciasalida = servicioreferenciasalida;
    }

    public String getServicioreferenciallegada() {
        return servicioreferenciallegada;
    }

    public void setServicioreferenciallegada(String servicioreferenciallegada) {
        this.servicioreferenciallegada = servicioreferenciallegada;
    }

    public String getInformacionrelevante() {
        return informacionrelevante;
    }

    public void setInformacionrelevante(String informacionrelevante) {
        this.informacionrelevante = informacionrelevante;
    }

    public CfgClasificaciones getTipoIdentificacion() {
        return tipoIdentificacion;
    }

    public void setTipoIdentificacion(CfgClasificaciones tipoIdentificacion) {
        this.tipoIdentificacion = tipoIdentificacion;
    }

    public CfgClasificaciones getIdDepartamento() {
        return idDepartamento;
    }

    public void setIdDepartamento(CfgClasificaciones idDepartamento) {
        this.idDepartamento = idDepartamento;
    }

    public CfgClasificaciones getIdMunicipio() {
        return idMunicipio;
    }

    public void setIdMunicipio(CfgClasificaciones idMunicipio) {
        this.idMunicipio = idMunicipio;
    }

    public CfgPacientes getIdPaciente() {
        return idPaciente;
    }

    public void setIdPaciente(CfgPacientes idPaciente) {
        this.idPaciente = idPaciente;
    }

    public CfgUsuarios getIdusuario() {
        return idusuario;
    }

    public void setIdusuario(CfgUsuarios idusuario) {
        this.idusuario = idusuario;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id3047anexo5 != null ? id3047anexo5.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Hc3047Anexo5)) {
            return false;
        }
        Hc3047Anexo5 other = (Hc3047Anexo5) object;
        if ((this.id3047anexo5 == null && other.id3047anexo5 != null) || (this.id3047anexo5 != null && !this.id3047anexo5.equals(other.id3047anexo5))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "modelo.entidades.Hc3047Anexo5[ id3047anexo5=" + id3047anexo5 + " ]";
    }
    
}
