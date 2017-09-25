/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beans.enumeradores;

/**
 *
 * @author Enderson
 */
public enum EstadoConsumo {

    EJECUTADO("Ejecutado"),
    POR_EJECUTAR("Por ejecutar"),
    CANCELADO("Cancelado");
   
    private String description;

    private EstadoConsumo(String s) {
        description = s;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
