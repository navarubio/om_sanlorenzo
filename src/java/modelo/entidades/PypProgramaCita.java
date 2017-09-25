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
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author cores
 */
@Entity
@Table(name = "pyp_programa_cita")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "PypProgramaCita.findAll", query = "SELECT p FROM PypProgramaCita p"),
    @NamedQuery(name = "PypProgramaCita.findByIdPypProgramaCita", query = "SELECT p FROM PypProgramaCita p WHERE p.idPypProgramaCita = :idPypProgramaCita")})
public class PypProgramaCita implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_pyp_programa_cita")
    private Integer idPypProgramaCita;
    
    @Column(name = "id_cita")
    private int idCita;

    /**
     * Get the value of idCita
     *
     * @return the value of idCita
     */
    public int getIdCita() {
        return idCita;
    }

    /**
     * Set the value of idCita
     *
     * @param idCita new value of idCita
     */
    public void setIdCita(int idCita) {
        this.idCita = idCita;
    }
    
    @Column(name = "id_programa_items")
    private int idProgramaItem;
    
    /**
     * Get the value of idProgramaItem
     *
     * @return the value of idProgramaItem
     */
    public int getIdProgramaItem() {
        return idProgramaItem;
    }

    /**
     * Set the value of idProgramaItem
     *
     * @param idProgramaItem new value of idProgramaItem
     */
    public void setIdProgramaItem(int idProgramaItem) {
        this.idProgramaItem = idProgramaItem;
    }

    public PypProgramaCita() {
    }

    public PypProgramaCita(Integer idPypProgramaCita) {
        this.idPypProgramaCita = idPypProgramaCita;
    }

    public Integer getIdPypProgramaCita() {
        return idPypProgramaCita;
    }

    public void setIdPypProgramaCita(Integer idPypProgramaCita) {
        this.idPypProgramaCita = idPypProgramaCita;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idPypProgramaCita != null ? idPypProgramaCita.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof PypProgramaCita)) {
            return false;
        }
        PypProgramaCita other = (PypProgramaCita) object;
        if ((this.idPypProgramaCita == null && other.idPypProgramaCita != null) || (this.idPypProgramaCita != null && !this.idPypProgramaCita.equals(other.idPypProgramaCita))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "modelo.entidades.PypProgramaCita[ idPypProgramaCita=" + idPypProgramaCita + " ]";
    }
    
}
