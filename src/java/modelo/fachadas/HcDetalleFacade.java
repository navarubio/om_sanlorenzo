/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo.fachadas;

import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.Query;
import modelo.entidades.HcDetalle;

/**
 *
 * @author santos
 */
@Stateless
public class HcDetalleFacade extends AbstractFacade<HcDetalle> {

    public HcDetalleFacade() {
        super(HcDetalle.class);
    }

    public String diagnosticoPrincipal(int idPaciente){
        try {
            String sql = "select hd.valor "
                    + "from hc_registro h "
                    + "inner join hc_detalle hd on hd.id_registro = h.id_registro "
                    + "inner join hc_campos_reg hc on hc.id_campo=hd.id_campo "
                    + "where hc.id_Campo in"
                    //Diagnosticos y diagnosticos principales 
                    //+ "(35,108494,72,1474,1229,107641,107400) "
                    //Se colocaron todos los que dicen principal o solo diagnostico en hc_campos_reg
                    + " (262, 1229,72,35,1347,1357,1474,1062,1113,1564,1657,1661,1864, 107427, 107621, 107621, 108009, 108960, 109448)"
                    + "and  h.id_paciente= ? "
                    + " and char_length(hd.valor)>0 order by hd.id_registro desc limit 1 ";
            Query query  =getEntityManager().createNativeQuery(sql).setParameter(1, idPaciente);
            List<String> lista  =query.getResultList();
            return lista.size() > 0 ?  lista.get(0) : "";
        } catch (Exception e) {
            return "";
        }
        
    }
    
    public String diagnosticoRel1(int idPaciente){
        try {
            String sql = "select hd.valor "
                    + "from hc_registro h "
                    + "inner join hc_detalle hd on hd.id_registro = h.id_registro "
                    + "inner join hc_campos_reg hc on hc.id_campo=hd.id_campo "
                    + "where hc.id_Campo in "
                    //+ "(1,108495,57,1230,107642,107401)"
                    //Se consulto en  hc_campos_reg todos los que tuvieran diagostico relacionado 1
                    +"(1, 57, 1230, 1348,1358, 1658, 1662)"
                    + " and  h.id_paciente= ? "
                    + " and char_length(hd.valor)>0 order by hd.id_registro desc limit 1 ";
            Query query  =getEntityManager().createNativeQuery(sql).setParameter(1, idPaciente);
            List<String> lista  =query.getResultList();
            return lista.size() > 0 ?  lista.get(0) : "";
        } catch (Exception e) {
            return "";
        }
        
    }
    
    public String diagnosticoRel2(int idPaciente){
        try {
            String sql = "select hd.valor "
                    + "from hc_registro h "
                    + "inner join hc_detalle hd on hd.id_registro = h.id_registro "
                    + "inner join hc_campos_reg hc on hc.id_campo=hd.id_campo "
                    + "where hc.id_Campo in"
                    //+ "(2,108496,61,1231,107643,107402)"
                    //Se consulto en  hc_campos_reg todos los que tuvieran diagostico relacionado 2 y 3
                    + "("
                    //Diagnosticos 2
                    + "2, 61, 1231,1349,1659,1663,"
                    //Diagnosticos 3
                    + "37,70,1232,1350"
                    + ")"
                    + " and  h.id_paciente= ? "
                    + " and char_length(hd.valor)>0 order by hd.id_registro desc limit 1 ";
            Query query  =getEntityManager().createNativeQuery(sql).setParameter(1, idPaciente);
            List<String> lista  =query.getResultList();
            return lista.size() > 0 ?  lista.get(0) : "";
        } catch (Exception e) {
            return "";
        }
        
    }

   public List<HcDetalle> buscarDetallesPorRegistro(int idRegistro) {
        try {
            return getEntityManager().createNativeQuery(
                    " select hc_detalle.id_registro, hc_detalle.id_campo, hc_detalle.valor from hc_detalle\n"
                    + " inner join hc_campos_reg\n"
                    + " on hc_detalle.id_campo = hc_campos_reg.id_campo\n"
                    + " where hc_detalle.id_registro = " + idRegistro + "\n"
                    + " order by hc_campos_reg.posicion", HcDetalle.class).getResultList();
        } catch (Exception e) {
            return null;
        }
    }    
}
