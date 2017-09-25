/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managedBeans.pyp_programas;
import java.io.Serializable;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
/**
 *
 * @author PC
 */
@ManagedBean
@ViewScoped
public class programas implements Serializable  {
    
    private String tipo;
    public int id;
    private String cups;
    private String rips;
    private String actividad;
    private String finalidad;
    private int genero;
    private int edad_ini;
    private int edad_fin;
    private String dosis;
    private String frecuencia;

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCups() {
        return cups;
    }

    public void setCups(String cups) {
        this.cups = cups;
    }

    public String getRips() {
        return rips;
    }

    public void setRips(String rips) {
        this.rips = rips;
    }

    public String getActividad() {
        return actividad;
    }

    public void setActividad(String actividad) {
        this.actividad = actividad;
    }

    public String getFinalidad() {
        return finalidad;
    }

    public void setFinalidad(String finalidad) {
        this.finalidad = finalidad;
    }

    public int getGenero() {
        return genero;
    }

    public void setGenero(int genero) {
        this.genero = genero;
    }

    public int getEdad_ini() {
        return edad_ini;
    }

    public void setEdad_ini(int edad_ini) {
        this.edad_ini = edad_ini;
    }

    public int getEdad_fin() {
        return edad_fin;
    }

    public void setEdad_fin(int edad_fin) {
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
    
}
