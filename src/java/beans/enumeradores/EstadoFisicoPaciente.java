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
public enum EstadoFisicoPaciente {

    NORMAL("Normal"),
    ANORMAL("Anormal"),
    NO_SE_EXPLORA("No se explora");

    private String description;

    private EstadoFisicoPaciente(String s) {
        description = s;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
