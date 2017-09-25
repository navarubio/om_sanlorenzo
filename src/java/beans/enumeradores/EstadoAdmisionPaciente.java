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
public enum EstadoAdmisionPaciente {

    ADMITIDO("Paciente admitido"),
    ADMISION_PACIENTE_TRIAGE("Paciente en proceso Triage"),
    ADMISION_PACIENTE_ENVIADO_TRIAGE("Paciente enviado a triage"),
    ADMISION_PACIENTE_EN_CONSULTA_DOCTOR("Paciente en consulta con el doctor"),
    ADMISION_PACIENTE_ENVIADO_OBSERVACION("Paciente en observación en habitación urgencias"),
    ADMISION_PACIENTE_ENVIADO_DADO_ALTA("Paciente dado de alta");

    private String description;

    private EstadoAdmisionPaciente(String s) {
        description = s;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
