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
public enum NivelTriage {

    NIVELTRIAGE_I("Triage I"),
    NIVELTRIAGE_II("Triage II"),
    NIVELTRIAGE_III("Triage III"),
    NIVELTRIAGE_IV("Triage IV"),
    NIVELTRIAGE_V("Triage V");

    private String description;

    private NivelTriage(String s) {
        description = s;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
