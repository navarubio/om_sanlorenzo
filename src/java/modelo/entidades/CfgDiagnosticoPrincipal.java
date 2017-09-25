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
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author jcores
 */
@Entity
@Table(name = "cfg_diagnostico_principal")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "CfgDiagnosticoPrincipal.findAll", query = "SELECT c FROM CfgDiagnosticoPrincipal c")
    , @NamedQuery(name = "CfgDiagnosticoPrincipal.findByCodigoDiagnostico", query = "SELECT c FROM CfgDiagnosticoPrincipal c WHERE c.codigoDiagnostico = :codigoDiagnostico")
    , @NamedQuery(name = "CfgDiagnosticoPrincipal.findByNombreDiagnostico", query = "SELECT c FROM CfgDiagnosticoPrincipal c WHERE c.nombreDiagnostico = :nombreDiagnostico")
    , @NamedQuery(name = "CfgDiagnosticoPrincipal.findByCodigoEspecialidad", query = "SELECT c FROM CfgDiagnosticoPrincipal c WHERE c.codigoEspecialidad = :codigoEspecialidad")
    , @NamedQuery(name = "CfgDiagnosticoPrincipal.findByCodigoEvento", query = "SELECT c FROM CfgDiagnosticoPrincipal c WHERE c.codigoEvento = :codigoEvento")
    , @NamedQuery(name = "CfgDiagnosticoPrincipal.findBySimbolo", query = "SELECT c FROM CfgDiagnosticoPrincipal c WHERE c.simbolo = :simbolo")
    , @NamedQuery(name = "CfgDiagnosticoPrincipal.findBySexo", query = "SELECT c FROM CfgDiagnosticoPrincipal c WHERE c.sexo = :sexo")
    , @NamedQuery(name = "CfgDiagnosticoPrincipal.findByEdad", query = "SELECT c FROM CfgDiagnosticoPrincipal c WHERE c.edad = :edad")
    , @NamedQuery(name = "CfgDiagnosticoPrincipal.findByUnidadEdad", query = "SELECT c FROM CfgDiagnosticoPrincipal c WHERE c.unidadEdad = :unidadEdad")
    , @NamedQuery(name = "CfgDiagnosticoPrincipal.findByEdadFinal", query = "SELECT c FROM CfgDiagnosticoPrincipal c WHERE c.edadFinal = :edadFinal")
    , @NamedQuery(name = "CfgDiagnosticoPrincipal.findByUnidadEdadFinal", query = "SELECT c FROM CfgDiagnosticoPrincipal c WHERE c.unidadEdadFinal = :unidadEdadFinal")
    , @NamedQuery(name = "CfgDiagnosticoPrincipal.findByAtencionObligatoria", query = "SELECT c FROM CfgDiagnosticoPrincipal c WHERE c.atencionObligatoria = :atencionObligatoria")
    , @NamedQuery(name = "CfgDiagnosticoPrincipal.findByVigilanciaEpidemiologica", query = "SELECT c FROM CfgDiagnosticoPrincipal c WHERE c.vigilanciaEpidemiologica = :vigilanciaEpidemiologica")
    , @NamedQuery(name = "CfgDiagnosticoPrincipal.findByCent", query = "SELECT c FROM CfgDiagnosticoPrincipal c WHERE c.cent = :cent")
    , @NamedQuery(name = "CfgDiagnosticoPrincipal.findByPatCron", query = "SELECT c FROM CfgDiagnosticoPrincipal c WHERE c.patCron = :patCron")
    , @NamedQuery(name = "CfgDiagnosticoPrincipal.findByDisMental", query = "SELECT c FROM CfgDiagnosticoPrincipal c WHERE c.disMental = :disMental")
    , @NamedQuery(name = "CfgDiagnosticoPrincipal.findByDisSensorial", query = "SELECT c FROM CfgDiagnosticoPrincipal c WHERE c.disSensorial = :disSensorial")
    , @NamedQuery(name = "CfgDiagnosticoPrincipal.findByDisMotriz", query = "SELECT c FROM CfgDiagnosticoPrincipal c WHERE c.disMotriz = :disMotriz")})
public class CfgDiagnosticoPrincipal implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 10)
    @Column(name = "codigo_diagnostico")
    private String codigoDiagnostico;
    @Size(max = 2147483647)
    @Column(name = "nombre_diagnostico")
    private String nombreDiagnostico;
    @Size(max = 5)
    @Column(name = "codigo_especialidad")
    private String codigoEspecialidad;
    @Size(max = 5)
    @Column(name = "codigo_evento")
    private String codigoEvento;
    @Size(max = 1)
    @Column(name = "simbolo")
    private String simbolo;
    @Size(max = 1)
    @Column(name = "sexo")
    private String sexo;
    @Column(name = "edad")
    private Short edad;
    @Size(max = 1)
    @Column(name = "unidad_edad")
    private String unidadEdad;
    @Column(name = "edad_final")
    private Short edadFinal;
    @Size(max = 1)
    @Column(name = "unidad_edad_final")
    private String unidadEdadFinal;
    @Column(name = "atencion_obligatoria")
    private Boolean atencionObligatoria;
    @Column(name = "vigilancia_epidemiologica")
    private Boolean vigilanciaEpidemiologica;
    @Column(name = "cent")
    private Boolean cent;
    @Column(name = "pat_cron")
    private Boolean patCron;
    @Column(name = "dis_mental")
    private Boolean disMental;
    @Column(name = "dis_sensorial")
    private Boolean disSensorial;
    @Column(name = "dis_motriz")
    private Boolean disMotriz;

    public CfgDiagnosticoPrincipal() {
    }

    public CfgDiagnosticoPrincipal(String codigoDiagnostico) {
        this.codigoDiagnostico = codigoDiagnostico;
    }

    public String getCodigoDiagnostico() {
        return codigoDiagnostico;
    }

    public void setCodigoDiagnostico(String codigoDiagnostico) {
        this.codigoDiagnostico = codigoDiagnostico;
    }

    public String getNombreDiagnostico() {
        return nombreDiagnostico;
    }

    public void setNombreDiagnostico(String nombreDiagnostico) {
        this.nombreDiagnostico = nombreDiagnostico;
    }

    public String getCodigoEspecialidad() {
        return codigoEspecialidad;
    }

    public void setCodigoEspecialidad(String codigoEspecialidad) {
        this.codigoEspecialidad = codigoEspecialidad;
    }

    public String getCodigoEvento() {
        return codigoEvento;
    }

    public void setCodigoEvento(String codigoEvento) {
        this.codigoEvento = codigoEvento;
    }

    public String getSimbolo() {
        return simbolo;
    }

    public void setSimbolo(String simbolo) {
        this.simbolo = simbolo;
    }

    public String getSexo() {
        return sexo;
    }

    public void setSexo(String sexo) {
        this.sexo = sexo;
    }

    public Short getEdad() {
        return edad;
    }

    public void setEdad(Short edad) {
        this.edad = edad;
    }

    public String getUnidadEdad() {
        return unidadEdad;
    }

    public void setUnidadEdad(String unidadEdad) {
        this.unidadEdad = unidadEdad;
    }

    public Short getEdadFinal() {
        return edadFinal;
    }

    public void setEdadFinal(Short edadFinal) {
        this.edadFinal = edadFinal;
    }

    public String getUnidadEdadFinal() {
        return unidadEdadFinal;
    }

    public void setUnidadEdadFinal(String unidadEdadFinal) {
        this.unidadEdadFinal = unidadEdadFinal;
    }

    public Boolean getAtencionObligatoria() {
        return atencionObligatoria;
    }

    public void setAtencionObligatoria(Boolean atencionObligatoria) {
        this.atencionObligatoria = atencionObligatoria;
    }

    public Boolean getVigilanciaEpidemiologica() {
        return vigilanciaEpidemiologica;
    }

    public void setVigilanciaEpidemiologica(Boolean vigilanciaEpidemiologica) {
        this.vigilanciaEpidemiologica = vigilanciaEpidemiologica;
    }

    public Boolean getCent() {
        return cent;
    }

    public void setCent(Boolean cent) {
        this.cent = cent;
    }

    public Boolean getPatCron() {
        return patCron;
    }

    public void setPatCron(Boolean patCron) {
        this.patCron = patCron;
    }

    public Boolean getDisMental() {
        return disMental;
    }

    public void setDisMental(Boolean disMental) {
        this.disMental = disMental;
    }

    public Boolean getDisSensorial() {
        return disSensorial;
    }

    public void setDisSensorial(Boolean disSensorial) {
        this.disSensorial = disSensorial;
    }

    public Boolean getDisMotriz() {
        return disMotriz;
    }

    public void setDisMotriz(Boolean disMotriz) {
        this.disMotriz = disMotriz;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (codigoDiagnostico != null ? codigoDiagnostico.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof CfgDiagnosticoPrincipal)) {
            return false;
        }
        CfgDiagnosticoPrincipal other = (CfgDiagnosticoPrincipal) object;
        if ((this.codigoDiagnostico == null && other.codigoDiagnostico != null) || (this.codigoDiagnostico != null && !this.codigoDiagnostico.equals(other.codigoDiagnostico))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "modelo.entidades.CfgDiagnosticoPrincipal[ codigoDiagnostico=" + codigoDiagnostico + " ]";
    }
    
}
