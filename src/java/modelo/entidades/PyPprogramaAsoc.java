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
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author cores
 */
@Entity
@Table(name = "pyp_programa_asoc", schema = "public")
@XmlRootElement
public class PyPprogramaAsoc implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_programa_asoc", nullable = false)
    private Integer idProgramaAsoc;
    @Column(name = "id_program")
    private Integer idPrograma;
    @Column(name = "id_administradora")
    private Integer idAdministradora;
    @Column(name = "id_contrato")
    private Integer idContrato;
    @Column(name = "meta")
    private double meta;

    public PyPprogramaAsoc() {
    }

    public Integer getIdPrograma() {
        return idPrograma;
    }

    public void setIdPrograma(Integer idPrograma) {
        this.idPrograma = idPrograma;
    }

    public Integer getIdProgramaAsoc() {
        return idProgramaAsoc;
    }

    public void setIdProgramaAsoc(Integer idProgramaAsoc) {
        this.idProgramaAsoc = idProgramaAsoc;
    }

    public Integer getIdAdministradora() {
        return idAdministradora;
    }

    public void setIdAdministradora(Integer idAdministradora) {
        this.idAdministradora = idAdministradora;
    }

    public Integer getIdContrato() {
        return idContrato;
    }

    public void setIdContrato(Integer idContrato) {
        this.idContrato = idContrato;
    }

    public double getMeta() {
        return meta;
    }

    public void setMeta(double meta) {
        this.meta = meta;
    }
   
    
    
}
