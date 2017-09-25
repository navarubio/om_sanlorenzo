/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managedBeans.historias;

/**
 *
 * @author jcores
 */
public class RepExamenes {
    private String n;
    private String tipo;
    private String fecha;
    private String resultado;

    public RepExamenes(String n, String tipo, String fecha, String resultado) {
        this.n = n;
        this.tipo = tipo;
        this.fecha = fecha;
        this.resultado = resultado;
    }

    public String getN() {
        return n;
    }

    public void setN(String n) {
        this.n = n;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getResultado() {
        return resultado;
    }

    public void setResultado(String resultado) {
        this.resultado = resultado;
    }
}
