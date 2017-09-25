/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo.entidades;

import java.io.Serializable;
import java.util.Set;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author cores
 */
@Entity
@Table(name = "pyp_programa", schema = "public")
@XmlRootElement
public class PyPprograma implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_programa", nullable = false)      
    private Integer idPrograma;
    @Column(name = "cod_programa")
    private String codigoPrograma;
    @Column(name = "nom_programa")
    private String nombrePrograma;
    @Column(name = "estado")
    private boolean estado;
    
    @ManyToMany
    @JoinTable(name = "pyp_programa_asoc", joinColumns = @JoinColumn(name = "id_program"),   inverseJoinColumns = @JoinColumn(name = "id_programa"))
    private Set<PyPprogramaAsoc> asocLiked;
    
    @ManyToMany
    @JoinTable(name = "pyp_programa_item", joinColumns = @JoinColumn(name = "id_programa"),   inverseJoinColumns = @JoinColumn(name = "id_programa"))
    private Set<PyPProgramaItem> itemLiked;
    
    public PyPprograma() {
    }

    public Integer getIdPrograma() {
        return idPrograma;
    }

    public void setIdPrograma(Integer idPrograma) {
        this.idPrograma = idPrograma;
    }

    public String getCodigoPrograma() {
        return codigoPrograma;
    }

    public void setCodigoPrograma(String codigoPrograma) {
        this.codigoPrograma = codigoPrograma;
    }

    public String getNombrePrograma() {
        return nombrePrograma;
    }

    public void setNombrePrograma(String nombrePrograma) {
        this.nombrePrograma = nombrePrograma;
    }

    public boolean isEstado() {
        return estado;
    }

    public void setEstado(boolean estado) {
        this.estado = estado;
    }
    
    
    
}
