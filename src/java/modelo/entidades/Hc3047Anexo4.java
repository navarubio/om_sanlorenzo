/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo.entidades;

import java.io.Serializable;
import java.util.Collection;
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
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author sofimar
 */
@Entity
@Table(name = "hc_3047_anexo4")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Hc3047Anexo4.findAll", query = "SELECT h FROM Hc3047Anexo4 h"),
    @NamedQuery(name = "Hc3047Anexo4.findById3047anexo4", query = "SELECT h FROM Hc3047Anexo4 h WHERE h.id3047anexo4 = :id3047anexo4"),
    @NamedQuery(name = "Hc3047Anexo4.findByNumeroautorizacion", query = "SELECT h FROM Hc3047Anexo4 h WHERE h.numeroautorizacion = :numeroautorizacion"),
    @NamedQuery(name = "Hc3047Anexo4.findByFechadocumento", query = "SELECT h FROM Hc3047Anexo4 h WHERE h.fechadocumento = :fechadocumento"),
    @NamedQuery(name = "Hc3047Anexo4.findByIdubicacionpaciente", query = "SELECT h FROM Hc3047Anexo4 h WHERE h.idubicacionpaciente = :idubicacionpaciente"),
    @NamedQuery(name = "Hc3047Anexo4.findByIdcama", query = "SELECT h FROM Hc3047Anexo4 h WHERE h.idcama = :idcama"),
    @NamedQuery(name = "Hc3047Anexo4.findByManejoguia", query = "SELECT h FROM Hc3047Anexo4 h WHERE h.manejoguia = :manejoguia"),
    @NamedQuery(name = "Hc3047Anexo4.findByNumerosolicitudorigen", query = "SELECT h FROM Hc3047Anexo4 h WHERE h.numerosolicitudorigen = :numerosolicitudorigen"),
    @NamedQuery(name = "Hc3047Anexo4.findByFechasolicitudorigen", query = "SELECT h FROM Hc3047Anexo4 h WHERE h.fechasolicitudorigen = :fechasolicitudorigen"),
    @NamedQuery(name = "Hc3047Anexo4.findByPorcentajegeneralasumido", query = "SELECT h FROM Hc3047Anexo4 h WHERE h.porcentajegeneralasumido = :porcentajegeneralasumido"),
    @NamedQuery(name = "Hc3047Anexo4.findByReclamotiquete", query = "SELECT h FROM Hc3047Anexo4 h WHERE h.reclamotiquete = :reclamotiquete"),
    @NamedQuery(name = "Hc3047Anexo4.findByValorcuotam", query = "SELECT h FROM Hc3047Anexo4 h WHERE h.valorcuotam = :valorcuotam"),
    @NamedQuery(name = "Hc3047Anexo4.findByPorcentajecuotam", query = "SELECT h FROM Hc3047Anexo4 h WHERE h.porcentajecuotam = :porcentajecuotam"),
    @NamedQuery(name = "Hc3047Anexo4.findByValormaxcuotam", query = "SELECT h FROM Hc3047Anexo4 h WHERE h.valormaxcuotam = :valormaxcuotam"),
    @NamedQuery(name = "Hc3047Anexo4.findByValorcopago", query = "SELECT h FROM Hc3047Anexo4 h WHERE h.valorcopago = :valorcopago"),
    @NamedQuery(name = "Hc3047Anexo4.findByPorcentajecopago", query = "SELECT h FROM Hc3047Anexo4 h WHERE h.porcentajecopago = :porcentajecopago"),
    @NamedQuery(name = "Hc3047Anexo4.findByValormaxcopago", query = "SELECT h FROM Hc3047Anexo4 h WHERE h.valormaxcopago = :valormaxcopago"),
    @NamedQuery(name = "Hc3047Anexo4.findByValorcuotar", query = "SELECT h FROM Hc3047Anexo4 h WHERE h.valorcuotar = :valorcuotar"),
    @NamedQuery(name = "Hc3047Anexo4.findByPorcentajecuotar", query = "SELECT h FROM Hc3047Anexo4 h WHERE h.porcentajecuotar = :porcentajecuotar"),
    @NamedQuery(name = "Hc3047Anexo4.findByValormaxcuotar", query = "SELECT h FROM Hc3047Anexo4 h WHERE h.valormaxcuotar = :valormaxcuotar"),
    @NamedQuery(name = "Hc3047Anexo4.findByValorotro", query = "SELECT h FROM Hc3047Anexo4 h WHERE h.valorotro = :valorotro"),
    @NamedQuery(name = "Hc3047Anexo4.findByPorcentajeotro", query = "SELECT h FROM Hc3047Anexo4 h WHERE h.porcentajeotro = :porcentajeotro"),
    @NamedQuery(name = "Hc3047Anexo4.findByValormaxotro", query = "SELECT h FROM Hc3047Anexo4 h WHERE h.valormaxotro = :valormaxotro"),
    @NamedQuery(name = "Hc3047Anexo4.findByNombreautoriza", query = "SELECT h FROM Hc3047Anexo4 h WHERE h.nombreautoriza = :nombreautoriza"),
    @NamedQuery(name = "Hc3047Anexo4.findByCargoautoriza", query = "SELECT h FROM Hc3047Anexo4 h WHERE h.cargoautoriza = :cargoautoriza"),
    @NamedQuery(name = "Hc3047Anexo4.findByIndicativotelefono", query = "SELECT h FROM Hc3047Anexo4 h WHERE h.indicativotelefono = :indicativotelefono"),
    @NamedQuery(name = "Hc3047Anexo4.findByNumerotelefono", query = "SELECT h FROM Hc3047Anexo4 h WHERE h.numerotelefono = :numerotelefono"),
    @NamedQuery(name = "Hc3047Anexo4.findByExtensiontelefono", query = "SELECT h FROM Hc3047Anexo4 h WHERE h.extensiontelefono = :extensiontelefono"),
    @NamedQuery(name = "Hc3047Anexo4.findByIndicativocelular", query = "SELECT h FROM Hc3047Anexo4 h WHERE h.indicativocelular = :indicativocelular"),
    @NamedQuery(name = "Hc3047Anexo4.findByNumerocelular", query = "SELECT h FROM Hc3047Anexo4 h WHERE h.numerocelular = :numerocelular"),
    @NamedQuery(name = "Hc3047Anexo4.findByExtensioncelular", query = "SELECT h FROM Hc3047Anexo4 h WHERE h.extensioncelular = :extensioncelular")})
public class Hc3047Anexo4 implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_3047anexo4")
    private Integer id3047anexo4;
    @Size(max = 10)
    @Column(name = "numeroautorizacion")
    private String numeroautorizacion;
    @Column(name = "fechadocumento")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechadocumento;
    @Column(name = "idubicacionpaciente")
    private Integer idubicacionpaciente;
    @Size(max = 10)
    @Column(name = "idcama")
    private String idcama;
    @Size(max = 150)
    @Column(name = "manejoguia")
    private String manejoguia;
    @Size(max = 10)
    @Column(name = "numerosolicitudorigen")
    private String numerosolicitudorigen;
    @Column(name = "fechasolicitudorigen")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechasolicitudorigen;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "porcentajegeneralasumido")
    private Double porcentajegeneralasumido;
    @Column(name = "reclamotiquete")
    private Boolean reclamotiquete;
    @Column(name = "valorcuotam")
    private Double valorcuotam;
    @Column(name = "porcentajecuotam")
    private Double porcentajecuotam;
    @Column(name = "valormaxcuotam")
    private Double valormaxcuotam;
    @Column(name = "valorcopago")
    private Double valorcopago;
    @Column(name = "porcentajecopago")
    private Double porcentajecopago;
    @Column(name = "valormaxcopago")
    private Double valormaxcopago;
    @Column(name = "valorcuotar")
    private Double valorcuotar;
    @Column(name = "porcentajecuotar")
    private Double porcentajecuotar;
    @Column(name = "valormaxcuotar")
    private Double valormaxcuotar;
    @Column(name = "valorotro")
    private Double valorotro;
    @Column(name = "porcentajeotro")
    private Double porcentajeotro;
    @Column(name = "valormaxotro")
    private Double valormaxotro;
    @Size(max = 150)
    @Column(name = "nombreautoriza")
    private String nombreautoriza;
    @Size(max = 150)
    @Column(name = "cargoautoriza")
    private String cargoautoriza;
    @Size(max = 5)
    @Column(name = "indicativotelefono")
    private String indicativotelefono;
    @Size(max = 10)
    @Column(name = "numerotelefono")
    private String numerotelefono;
    @Size(max = 5)
    @Column(name = "extensiontelefono")
    private String extensiontelefono;
    @Size(max = 5)
    @Column(name = "indicativocelular")
    private String indicativocelular;
    @Size(max = 10)
    @Column(name = "numerocelular")
    private String numerocelular;
    @Size(max = 5)
    @Column(name = "extensioncelular")
    private String extensioncelular;
    @JoinColumn(name = "idservicio", referencedColumnName = "id")
    @ManyToOne
    private CfgClasificaciones idservicio;
    @JoinColumn(name = "id_paciente", referencedColumnName = "id_paciente")
    @ManyToOne
    private CfgPacientes idPaciente;
    @JoinColumn(name = "id_usuario", referencedColumnName = "id_usuario")
    @ManyToOne
    private CfgUsuarios idUsuario;
    @OneToMany(mappedBy = "id3047anexo4")
    private Collection<Hc3047Anexo41> hc3047Anexo41Collection;

    public Hc3047Anexo4() {
    }

    public Hc3047Anexo4(Integer id3047anexo4) {
        this.id3047anexo4 = id3047anexo4;
    }

    public Integer getId3047anexo4() {
        return id3047anexo4;
    }

    public void setId3047anexo4(Integer id3047anexo4) {
        this.id3047anexo4 = id3047anexo4;
    }

    public String getNumeroautorizacion() {
        return numeroautorizacion;
    }

    public void setNumeroautorizacion(String numeroautorizacion) {
        this.numeroautorizacion = numeroautorizacion;
    }

    public Date getFechadocumento() {
        return fechadocumento;
    }

    public void setFechadocumento(Date fechadocumento) {
        this.fechadocumento = fechadocumento;
    }

    public Integer getIdubicacionpaciente() {
        return idubicacionpaciente;
    }

    public void setIdubicacionpaciente(Integer idubicacionpaciente) {
        this.idubicacionpaciente = idubicacionpaciente;
    }

    public String getIdcama() {
        return idcama;
    }

    public void setIdcama(String idcama) {
        this.idcama = idcama;
    }

    public String getManejoguia() {
        return manejoguia;
    }

    public void setManejoguia(String manejoguia) {
        this.manejoguia = manejoguia;
    }

    public String getNumerosolicitudorigen() {
        return numerosolicitudorigen;
    }

    public void setNumerosolicitudorigen(String numerosolicitudorigen) {
        this.numerosolicitudorigen = numerosolicitudorigen;
    }

    public Date getFechasolicitudorigen() {
        return fechasolicitudorigen;
    }

    public void setFechasolicitudorigen(Date fechasolicitudorigen) {
        this.fechasolicitudorigen = fechasolicitudorigen;
    }

    public Double getPorcentajegeneralasumido() {
        return porcentajegeneralasumido;
    }

    public void setPorcentajegeneralasumido(Double porcentajegeneralasumido) {
        this.porcentajegeneralasumido = porcentajegeneralasumido;
    }

    public Boolean getReclamotiquete() {
        return reclamotiquete;
    }

    public void setReclamotiquete(Boolean reclamotiquete) {
        this.reclamotiquete = reclamotiquete;
    }

    public Double getValorcuotam() {
        return valorcuotam;
    }

    public void setValorcuotam(Double valorcuotam) {
        this.valorcuotam = valorcuotam;
    }

    public Double getPorcentajecuotam() {
        return porcentajecuotam;
    }

    public void setPorcentajecuotam(Double porcentajecuotam) {
        this.porcentajecuotam = porcentajecuotam;
    }

    public Double getValormaxcuotam() {
        return valormaxcuotam;
    }

    public void setValormaxcuotam(Double valormaxcuotam) {
        this.valormaxcuotam = valormaxcuotam;
    }

    public Double getValorcopago() {
        return valorcopago;
    }

    public void setValorcopago(Double valorcopago) {
        this.valorcopago = valorcopago;
    }

    public Double getPorcentajecopago() {
        return porcentajecopago;
    }

    public void setPorcentajecopago(Double porcentajecopago) {
        this.porcentajecopago = porcentajecopago;
    }

    public Double getValormaxcopago() {
        return valormaxcopago;
    }

    public void setValormaxcopago(Double valormaxcopago) {
        this.valormaxcopago = valormaxcopago;
    }

    public Double getValorcuotar() {
        return valorcuotar;
    }

    public void setValorcuotar(Double valorcuotar) {
        this.valorcuotar = valorcuotar;
    }

    public Double getPorcentajecuotar() {
        return porcentajecuotar;
    }

    public void setPorcentajecuotar(Double porcentajecuotar) {
        this.porcentajecuotar = porcentajecuotar;
    }

    public Double getValormaxcuotar() {
        return valormaxcuotar;
    }

    public void setValormaxcuotar(Double valormaxcuotar) {
        this.valormaxcuotar = valormaxcuotar;
    }

    public Double getValorotro() {
        return valorotro;
    }

    public void setValorotro(Double valorotro) {
        this.valorotro = valorotro;
    }

    public Double getPorcentajeotro() {
        return porcentajeotro;
    }

    public void setPorcentajeotro(Double porcentajeotro) {
        this.porcentajeotro = porcentajeotro;
    }

    public Double getValormaxotro() {
        return valormaxotro;
    }

    public void setValormaxotro(Double valormaxotro) {
        this.valormaxotro = valormaxotro;
    }

    public String getNombreautoriza() {
        return nombreautoriza;
    }

    public void setNombreautoriza(String nombreautoriza) {
        this.nombreautoriza = nombreautoriza;
    }

    public String getCargoautoriza() {
        return cargoautoriza;
    }

    public void setCargoautoriza(String cargoautoriza) {
        this.cargoautoriza = cargoautoriza;
    }

    public String getIndicativotelefono() {
        return indicativotelefono;
    }

    public void setIndicativotelefono(String indicativotelefono) {
        this.indicativotelefono = indicativotelefono;
    }

    public String getNumerotelefono() {
        return numerotelefono;
    }

    public void setNumerotelefono(String numerotelefono) {
        this.numerotelefono = numerotelefono;
    }

    public String getExtensiontelefono() {
        return extensiontelefono;
    }

    public void setExtensiontelefono(String extensiontelefono) {
        this.extensiontelefono = extensiontelefono;
    }

    public String getIndicativocelular() {
        return indicativocelular;
    }

    public void setIndicativocelular(String indicativocelular) {
        this.indicativocelular = indicativocelular;
    }

    public String getNumerocelular() {
        return numerocelular;
    }

    public void setNumerocelular(String numerocelular) {
        this.numerocelular = numerocelular;
    }

    public String getExtensioncelular() {
        return extensioncelular;
    }

    public void setExtensioncelular(String extensioncelular) {
        this.extensioncelular = extensioncelular;
    }

    public CfgClasificaciones getIdservicio() {
        return idservicio;
    }

    public void setIdservicio(CfgClasificaciones idservicio) {
        this.idservicio = idservicio;
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

    @XmlTransient
    public Collection<Hc3047Anexo41> getHc3047Anexo41Collection() {
        return hc3047Anexo41Collection;
    }

    public void setHc3047Anexo41Collection(Collection<Hc3047Anexo41> hc3047Anexo41Collection) {
        this.hc3047Anexo41Collection = hc3047Anexo41Collection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id3047anexo4 != null ? id3047anexo4.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Hc3047Anexo4)) {
            return false;
        }
        Hc3047Anexo4 other = (Hc3047Anexo4) object;
        if ((this.id3047anexo4 == null && other.id3047anexo4 != null) || (this.id3047anexo4 != null && !this.id3047anexo4.equals(other.id3047anexo4))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "modelo.entidades.Hc3047Anexo4[ id3047anexo4=" + id3047anexo4 + " ]";
    }
    
}
