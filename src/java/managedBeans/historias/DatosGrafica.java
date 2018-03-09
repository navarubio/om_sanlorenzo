/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managedBeans.historias;

/**
 *
 * @author miguel
 */
public class DatosGrafica {
    public Double valor;
    public Double valorX;

    public DatosGrafica(){
        
    }
    public DatosGrafica(Double valor, Double valorX) {
        this.valor = valor;
        this.valorX = valorX;
    }

    public Double getValor() {
        return valor;
    }

    public void setValor(Double valor) {
        this.valor = valor;
    }

    public Double getValorX() {
        return valorX;
    }

    public void setValorX(Double valorX) {
        this.valorX = valorX;
    }
    
    
    
}
