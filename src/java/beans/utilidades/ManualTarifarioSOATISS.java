/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beans.utilidades;

/**
 *
 * @author miguel
 */
public class ManualTarifarioSOATISS {
    private String codigo;
    private String servicio;
    private String uvr;
    private double smldv;
    private double soat;
    private double porcentaje;
    private double valor_soat;
    private double valorServicio;
    private double valor_porcentaje;
    
    public ManualTarifarioSOATISS(){
        
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getServicio() {
        return servicio;
    }

    public void setServicio(String servicio) {
        this.servicio = servicio;
    }

    public String getUvr() {
        return uvr;
    }

    public void setUvr(String uvr) {
        this.uvr = uvr;
    }

    public double getSmldv() {
        return smldv;
    }

    public void setSmldv(double smldv) {
        this.smldv = smldv;
    }

    public double getSoat() {
        return soat;
    }

    public void setSoat(double soat) {
        this.soat = soat;
    }

    public double getPorcentaje() {
        return porcentaje;
    }

    public void setPorcentaje(double porcentaje) {
        this.porcentaje = porcentaje;
    }

    public double getValor_soat() {
        return valor_soat;
    }

    public void setValor_soat(double valor_soat) {
        this.valor_soat = valor_soat;
    }

    public double getValorServicio() {
        return valorServicio;
    }

    public void setValorServicio(double valorServicio) {
        this.valorServicio = valorServicio;
    }

    public double getValor_porcentaje() {
        return valor_porcentaje;
    }

    public void setValor_porcentaje(double valor_porcentaje) {
        this.valor_porcentaje = valor_porcentaje;
    }
    
    
    
}
