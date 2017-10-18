/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo.fachadas;

import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.NamedQuery;
import javax.persistence.Query;
import modelo.entidades.CfgMedicamento;

/**
 *
 * @author santos
 */
@Stateless
public class CfgMedicamentoFacade extends AbstractFacade<CfgMedicamento> {

    public CfgMedicamentoFacade() {
        super(CfgMedicamento.class);
    }

//    @NamedQuery(name = "CfgMedicamento.findByIdMedicamento", query = "SELECT c FROM CfgMedicamento c WHERE c.idMedicamento = :idMedicamento"),
            
    public List<CfgMedicamento> getMedicamentosById() {
        try {
            String hql = "SELECT c FROM CfgMedicamento c WHERE c.idMedicamento = :idMedicamento ";
            return getEntityManager().createQuery(hql).getResultList();
        } catch (Exception e) {
            return null;
        }
    }
    
    public List<CfgMedicamento> buscarOrdenado() {
        try {
            String hql = "SELECT m FROM CfgMedicamento m ORDER BY m.idMedicamento ASC";
            return getEntityManager().createQuery(hql).getResultList();
        } catch (Exception e) {
            return null;
        }
    }

    public List<CfgMedicamento> buscarNoEstanEnManual(Integer idManualTarifario) {//medicamento que no se encuentra en un amnual tarifario
        try {
            String sql = "SELECT * FROM cfg_medicamento WHERE id_medicamento NOT IN (SELECT id_medicamento FROM fac_manual_tarifario_medicamento WHERE id_manual_tarifario = " + idManualTarifario.toString() + ") ORDER BY id_medicamento";
            List<CfgMedicamento> listaMedicamentos = (List<CfgMedicamento>) getEntityManager().createNativeQuery(sql, CfgMedicamento.class).getResultList();
            return listaMedicamentos;
        } catch (Exception e) {
            return null;
        }
    }
    
    public List<CfgMedicamento> buscarNoEstanEnPaquete(Integer idPaquete) {//medicamentos que no se encuentran en un paquete
        try {
            String sql = "SELECT * FROM cfg_medicamento WHERE id_medicamento NOT IN (SELECT id_medicamento FROM fac_paquete_medicamento WHERE id_paquete = " + idPaquete.toString() + ") ORDER BY id_medicamento";
            List<CfgMedicamento> listaMedicamentos = (List<CfgMedicamento>) getEntityManager().createNativeQuery(sql, CfgMedicamento.class).getResultList();
            return listaMedicamentos;
        } catch (Exception e) {
            return null;
        }
    }
    
    public CfgMedicamento medicamentoXIdProducto(int idProducto){
        try {
            String hql  ="SELECT c FROM CfgMedicamento c WHERE c.idProducto.idProducto=:idProducto";
            Query query = getEntityManager().createQuery(hql).setParameter("idProducto", idProducto);
            return (CfgMedicamento)query.getSingleResult();
        } catch (Exception e) {
        }
        return null;
    }
    
     public CfgMedicamento medicamentoXCodigo(String codigoMedicamento){
        try {
            String hql  ="SELECT c FROM CfgMedicamento c WHERE c.codigoMedicamento=:codigoMedicamento";
            Query query = getEntityManager().createQuery(hql).setParameter("codigoMedicamento", codigoMedicamento);
            List<CfgMedicamento> lista = query.getResultList();
            return lista.size() > 0 ? lista.get(0) : null;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
