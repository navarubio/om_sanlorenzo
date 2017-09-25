/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo.fachadas;

import javax.ejb.Stateless;
import modelo.entidades.UrgControlPrescripcionMedicamento;

/**
 *
 * @author Enderson
 */
@Stateless
public class UrgControlySuministroMedicamentosFacade extends AbstractFacade<UrgControlPrescripcionMedicamento> {

    public UrgControlySuministroMedicamentosFacade() {
        super(UrgControlPrescripcionMedicamento.class);
    }

   
}
