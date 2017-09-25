/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo.fachadas;

import java.util.ArrayList;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import modelo.entidades.CfgUnidad;

/**
 *
 * @author miguel
 */
@Stateless
public class CfgUnidadFacade extends AbstractFacade<CfgUnidad> {

    public CfgUnidadFacade() {
        super(CfgUnidad.class);
    }

}

