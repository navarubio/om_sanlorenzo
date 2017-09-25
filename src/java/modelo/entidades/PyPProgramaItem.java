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
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author cores
 */
@Entity
@Table(name = "pyp_programa_item", schema = "public")
@XmlRootElement
public class PyPProgramaItem implements Serializable {
    
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_programa_items", nullable = false)
    private Integer idProgramaItems;
//     
//    @MapsId("idPrograma")
//    @JoinColumn(name = "idPrograma", referencedColumnName = "idPrograma")
//    @ManyToOne
//    private Integer idProgramaFK;
    
    @Column(name = "id_programa")
    private Integer idPrograma;
    @Column(name = "id_servicio")
    private Integer idServicio;
    @Column(name = "id_medicamento")
    private Integer idMedicamento;
    @Column(name = "id_insumo")
    private Integer idInsumo;
    @Column(name = "nom_actividad")
    private String nombreActividad;
    @Column(name = "finalidad")
    private String finalidad;
    @Column(name = "genero")
    private Integer genero;
    @Column(name = "edad_inicial")
    private Integer edad_ini;
    @Column(name = "edad_inicial_list")
    private Integer edad_ini_list;
    @Column(name = "edad_final")
    private Integer edad_fin;
    @Column(name = "edad_final_list")
    private Integer edad_fin_list;
    @Column(name = "dosis")
    private String dosis;
    @Column(name = "frecuencia")
    private String frecuencia;
    @Column(name = "meta")
    private double meta;
    
    @JoinColumn(name = "id_servicio", referencedColumnName = "id_servicio", nullable = false, insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private FacServicio facServicio;
    
    @ManyToMany
    @JoinTable(name = "pyp_programa_cita", joinColumns = @JoinColumn(name = "id_programa_items"),   inverseJoinColumns = @JoinColumn(name = "id_programa_items"))
    private Set<PyPProgramaItem> itemLiked;
    
    @ManyToMany
    @JoinTable(name = "pyp_programa_asoc", joinColumns = @JoinColumn(name = "id_program"),   inverseJoinColumns = @JoinColumn(name = "id_programa"))
    private Set<PyPProgramaItem> item;

    public PyPProgramaItem() {
    } 
    
    public Integer getIdProgramaItems() {
        return idProgramaItems;
    }

    public void setIdProgramaItems(Integer idProgramaItems) {
        this.idProgramaItems = idProgramaItems;
    } 

    public Integer getIdPrograma() {
        return idPrograma;
    }

    public void setIdPrograma(Integer idPrograma) {
        this.idPrograma = idPrograma;
    }
    
    public Integer getIdServicio() {
        return idServicio;
    }

    public void setIdServicio(Integer idServicio) {
        this.idServicio = idServicio;
    }

    public Integer getIdMedicamento() {
        return idMedicamento;
    }

    public void setIdMedicamento(Integer idMedicamento) {
        this.idMedicamento = idMedicamento;
    }

    public Integer getIdInsumo() {
        return idInsumo;
    }

    public void setIdInsumo(Integer idInsumo) {
        this.idInsumo = idInsumo;
    }

    public String getNombreActividad() {
        return nombreActividad;
    }

    public void setNombreActividad(String nombreActividad) {
        this.nombreActividad = nombreActividad;
    }

    public String getFinalidad() {
        return finalidad;
    }

    public void setFinalidad(String finalidad) {
        this.finalidad = finalidad;
    }

    public Integer getGenero() {
        return genero;
    }

    public void setGenero(Integer genero) {
        this.genero = genero;
    }

    public Integer getEdad_ini() {
        return edad_ini;
    }

    public void setEdad_ini(Integer edad_ini) {
        this.edad_ini = edad_ini;
    }

    public Integer getEdad_fin() {
        return edad_fin;
    }

    public void setEdad_fin(Integer edad_fin) {
        this.edad_fin = edad_fin;
    }

    public String getDosis() {
        return dosis;
    }

    public void setDosis(String dosis) {
        this.dosis = dosis;
    }

    public String getFrecuencia() {
        return frecuencia;
    }

    public void setFrecuencia(String frecuencia) {
        this.frecuencia = frecuencia;
    }

    public FacServicio getFacServicio() {
        return facServicio;
    }

    public void setFacServicio(FacServicio facServicio) {
        this.facServicio = facServicio;
    }

    public double getMeta() {
        return meta;
    }

    public void setMeta(double meta) {
        this.meta = meta;
    }

    public Integer getEdad_ini_list() {
        return edad_ini_list;
    }

    public void setEdad_ini_list(Integer edad_ini_list) {
        this.edad_ini_list = edad_ini_list;
    }

    public Integer getEdad_fin_list() {
        return edad_fin_list;
    }

    public void setEdad_fin_list(Integer edad_fin_list) {
        this.edad_fin_list = edad_fin_list;
    }

    public Set<PyPProgramaItem> getItemLiked() {
        return itemLiked;
    }

    public void setItemLiked(Set<PyPProgramaItem> itemLiked) {
        this.itemLiked = itemLiked;
    }

    public Set<PyPProgramaItem> getItem() {
        return item;
    }

    public void setItem(Set<PyPProgramaItem> item) {
        this.item = item;
    }
    
    
    
}
