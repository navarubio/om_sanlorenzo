/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo.fachadas;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TemporalType;
import modelo.entidades.FacFacturaPaciente;

/**
 *
 * @author santos
 */
@Stateless
public class FacFacturaPacienteFacade extends AbstractFacade<FacFacturaPaciente> {

    public FacFacturaPacienteFacade() {
        super(FacFacturaPaciente.class);
    }

    public List<FacFacturaPaciente> buscarOrdenado() {
        try {
            String hql = "SELECT i FROM FacFactura i ORDER BY i.idFactura ASC";
            return getEntityManager().createQuery(hql).getResultList();
        } catch (Exception e) {
            return null;
        }
    }

    public List<FacFacturaPaciente> buscarPorAministradora(String administradora) {
        try {
            String hql = "SELECT f FROM FacFactura f WHERE f.idAdministradora.idAdministradora = " + administradora + " ORDER BY f.fecha DESC";
            return getEntityManager().createQuery(hql).getResultList();
        } catch (Exception e) {
            return null;
        }
    }

    public List<FacFacturaPaciente> buscarPorAutorizacion(String idAutorizacion) {
        try {
            String sql = ""
                    + " select * from fac_factura_paciente where id_cita in "
                    + " (select id_cita from cit_citas where id_autorizacion="+idAutorizacion+")";
            return (List<FacFacturaPaciente>) getEntityManager().createNativeQuery(sql, FacFacturaPaciente.class).getResultList();
            
        } catch (Exception e) {
            return null;
        }
    }

    public int numeroTotalRegistros() {
        try {
            return Integer.parseInt(getEntityManager().createNativeQuery("SELECT COUNT(*) FROM fac_factura_paciente").getSingleResult().toString());
        } catch (Exception e) {
            return 0;
        }
    }

    public List<FacFacturaPaciente> consultaNativaFacturas(String sql) {
        List<FacFacturaPaciente> listaFacturas = (List<FacFacturaPaciente>) getEntityManager().createNativeQuery(sql, FacFacturaPaciente.class).getResultList();
        return listaFacturas;
    }
    
    public List<FacFacturaPaciente> buscarFaltanFacturar() {
        try {
            
            String hql = "SELECT a FROM FacFacturaPaciente a WHERE a.anulada = false and a.facturadaEnAdmi = false AND a.idAdministradora.idAdministradora != 1";
            return getEntityManager().createQuery(hql).getResultList();
        } catch (Exception e) {
            return null;
        }
    }
    
    public List<FacFacturaPaciente> buscarFaltanFacturarInforme(Date fechaDesde, Date fechaHasta) {
        try {
            String hql = "SELECT a FROM FacFacturaPaciente a WHERE a.anulada = false and a.facturadaEnAdmi = false AND a.idAdministradora.idAdministradora != 1 and a.fechaElaboracion BETWEEN :param1 and :param2 ";
            return getEntityManager().createQuery(hql).setParameter("param1", fechaDesde, TemporalType.TIMESTAMP).setParameter("param2", fechaHasta, TemporalType.TIMESTAMP).getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
     public List<Object[]> getInformeMasivo(int idAdministradora){
        List<Object[]> lista = new ArrayList<>();
        try {
            Query query;
            String sql = "SELECT   "
                    + "(select 98 ) as primer_nombre, "
                    + "   cp.primer_nombre as n,  "
                    + "   cp.segundo_nombre,  "
                    + "   cp.primer_apellido,  "
                    + "   cp.segundo_apellido,  "
                    + "   ti.observacion as tipo_identificacion, "
                    + "   cp.identificacion , "
                    + "   cp.fecha_nacimiento,  "
                    + "   date_part('year',age(cp.fecha_nacimiento)) as edad, "
                    + "   ge.observacion as genero,  "
                    + "   etnia.descripcion as etnia,   "
                    + "(select 98 ) as grupoplacional, "
                    + "   cp.telefono_residencia,  "
                    + "   zo.observacion as zona,  "
                    + "   cp.direccion, "
                    + "(select 190010892301) as codigoips, "
                    + "(select  '1845-01-01') as FIPN, " //tener en cuenta
                    + "  (SELECT  hc_detalle.valor FROM public.hc_registro, public.hc_detalle, public.hc_tipo_reg, public.hc_campos_reg WHERE hc_registro.id_tipo_reg = hc_tipo_reg.id_tipo_reg AND hc_detalle.id_registro = hc_registro.id_registro AND hc_detalle.id_campo = hc_campos_reg.id_campo AND  "
                    + "  hc_detalle.id_campo = 112059 and hc_registro.id_registro = hr.id_registro) as tabaquismo, "
                    + "(SELECT  hc_detalle.valor FROM public.hc_registro, public.hc_detalle, public.hc_tipo_reg, public.hc_campos_reg WHERE hc_registro.id_tipo_reg = hc_tipo_reg.id_tipo_reg AND hc_detalle.id_registro = hc_registro.id_registro AND hc_detalle.id_campo = hc_campos_reg.id_campo AND  "
                    + "  hc_detalle.id_campo = 108549 and hc_registro.id_registro = hr.id_registro)  as alcohol, "
                    + "(SELECT  hc_detalle.valor FROM public.hc_registro, public.hc_detalle, public.hc_tipo_reg, public.hc_campos_reg WHERE hc_registro.id_tipo_reg = hc_tipo_reg.id_tipo_reg AND hc_detalle.id_registro = hc_registro.id_registro AND hc_detalle.id_campo = hc_campos_reg.id_campo AND  "
                    + "  hc_detalle.id_campo = 108551 and hc_registro.id_registro = hr.id_registro)  as sedentarismo, "
                    + "(SELECT  hc_detalle.valor FROM public.hc_registro, public.hc_detalle, public.hc_tipo_reg, public.hc_campos_reg WHERE hc_registro.id_tipo_reg = hc_tipo_reg.id_tipo_reg AND hc_detalle.id_registro = hc_registro.id_registro AND hc_detalle.id_campo = hc_campos_reg.id_campo AND  "
                    + "  hc_detalle.id_campo = 108547 and hc_registro.id_registro = hr.id_registro)  as frutasyverduras, "
                    + "(SELECT  hc_detalle.valor FROM public.hc_registro, public.hc_detalle, public.hc_tipo_reg, public.hc_campos_reg WHERE hc_registro.id_tipo_reg = hc_tipo_reg.id_tipo_reg AND hc_detalle.id_registro = hc_registro.id_registro AND hc_detalle.id_campo = hc_campos_reg.id_campo AND  "
                    + "  hc_detalle.id_campo = 108550 and hc_registro.id_registro = hr.id_registro)  as dietabgrasas, "
                    + "(SELECT  hc_detalle.valor FROM public.hc_registro, public.hc_detalle, public.hc_tipo_reg, public.hc_campos_reg WHERE hc_registro.id_tipo_reg = hc_tipo_reg.id_tipo_reg AND hc_detalle.id_registro = hc_registro.id_registro AND hc_detalle.id_campo = hc_campos_reg.id_campo AND  "
                    + "  hc_detalle.id_campo = 108554 and hc_registro.id_registro = hr.id_registro)  as sal, "
                    + "(select hd.valor "
                    + "from hc_registro h "
                    + "inner join hc_detalle hd on hd.id_registro = h.id_registro "
                    + "inner join hc_campos_reg hc on hc.id_campo=hd.id_campo "
                    + "where hc.id_Campo in(1657,108494) "
                    + "and h.id_paciente=hr.id_paciente "
                    + "and char_length(hd.valor)>0 order by hd.id_registro desc limit 1)  as diagppal, " //diagnóstico principal
                    + "(select h.fecha_reg " +
                      " from hc_registro h "
                    + "inner join hc_detalle hd on hd.id_registro = h.id_registro "
                    + "inner join hc_campos_reg hc on hc.id_campo=hd.id_campo "
                    + "where hc.id_Campo in(1657,108494) "
                    + "and h.id_paciente=hr.id_paciente "
                    + "and char_length(hd.valor)>0 order by hd.id_registro desc limit 1) as fdiagnostico, " //fecha diagnóstico
                    + "(select hd.valor "
                    + "from hc_registro h "
                    + "inner join hc_detalle hd on hd.id_registro = h.id_registro "
                    + "inner join hc_campos_reg hc on hc.id_campo=hd.id_campo "
                    + "where hc.id_Campo in(1658,108495) "
                    + "and h.id_paciente=hr.id_paciente "
                    + "and char_length(hd.valor)>0 order by hd.id_registro desc limit 1)  as diagrelacionado2, " 
                    + "(select h.fecha_reg "
                    + "from hc_registro h "
                    + "inner join hc_detalle hd on hd.id_registro = h.id_registro "
                    + "inner join hc_campos_reg hc on hc.id_campo=hd.id_campo "
                    + "where hc.id_Campo in(1658,108495) "
                    + "and h.id_paciente=hr.id_paciente " +
            "and char_length(hd.valor)>0 order by hd.id_registro desc limit 1) as fdiagnosticoR2, "
                    + "(select 98) as tipoDM, "
                    + "(select hd.valor "
                    + "from hc_registro h "
                    + "inner join hc_detalle hd on hd.id_registro = h.id_registro "
                    + "inner join hc_campos_reg hc on hc.id_campo=hd.id_campo "
                    + "where hc.id_Campo in(1659,108496) "
                    + "and h.id_paciente=hr.id_paciente "
                    + "and char_length(hd.valor)>0 order by hd.id_registro desc limit 1)  as diagrelacionado3, "
                    + " "
                    + "(select hd.valor "
                    + "from hc_registro h "
                    + "inner join hc_detalle hd on hd.id_registro = h.id_registro "
                    + "inner join hc_campos_reg hc on hc.id_campo=hd.id_campo "
                    + "where hc.id_Campo in(108497) "
                    + "and h.id_paciente=hr.id_paciente "
                    + "and char_length(hd.valor)>0 order by hd.id_registro desc limit 1)  as diagrelacionado4, "
                    + " "
                    + "(SELECT  hc_detalle.valor FROM public.hc_registro, public.hc_detalle, public.hc_tipo_reg, public.hc_campos_reg WHERE hc_registro.id_tipo_reg = hc_tipo_reg.id_tipo_reg AND hc_detalle.id_registro = hc_registro.id_registro AND hc_detalle.id_campo = hc_campos_reg.id_campo AND  "
                    + "  hc_detalle.id_campo = 111720 and hc_registro.id_registro = hr.id_registro)  as diagrelacionado5, "
                    + " "
                    + "(SELECT  hc_detalle.valor FROM public.hc_registro, public.hc_detalle, public.hc_tipo_reg, public.hc_campos_reg WHERE hc_registro.id_tipo_reg = hc_tipo_reg.id_tipo_reg AND hc_detalle.id_registro = hc_registro.id_registro AND hc_detalle.id_campo = hc_campos_reg.id_campo AND  "
                    + "  hc_detalle.id_campo = 111721 and hc_registro.id_registro = hr.id_registro)  as observaciones, "
                    + "(SELECT '1800-01-01') as dxERC, "
                    + "(SELECT 98) as etiologiaERC, "
                    + "(SELECT  hc_detalle.valor FROM public.hc_registro, public.hc_detalle, public.hc_tipo_reg, public.hc_campos_reg WHERE hc_registro.id_tipo_reg = hc_tipo_reg.id_tipo_reg AND hc_detalle.id_registro = hc_registro.id_registro AND hc_detalle.id_campo = hc_campos_reg.id_campo AND  "
                    + "  hc_detalle.id_campo = 108456 and hc_registro.id_registro = (select min(id_registro) from hc_registro where id_paciente=cp.id_paciente and id_tipo_reg=54 and hr.folio=1))  as sentadobrazoderechopas, "
                    + " "
                    + "(SELECT  hc_detalle.valor FROM public.hc_registro, public.hc_detalle, public.hc_tipo_reg, public.hc_campos_reg WHERE hc_registro.id_tipo_reg = hc_tipo_reg.id_tipo_reg AND hc_detalle.id_registro = hc_registro.id_registro AND hc_detalle.id_campo = hc_campos_reg.id_campo AND  "
                    + "  hc_detalle.id_campo = 108457 and hc_registro.id_registro = (select min(id_registro) from hc_registro where id_paciente=cp.id_paciente and id_tipo_reg=54 and hr.folio=1))  as sentadobrazoderechopad, "
                    + " "
                    + "(SELECT  hc_detalle.valor FROM public.hc_registro, public.hc_detalle, public.hc_tipo_reg, public.hc_campos_reg WHERE hc_registro.id_tipo_reg = hc_tipo_reg.id_tipo_reg AND hc_detalle.id_registro = hc_registro.id_registro AND hc_detalle.id_campo = hc_campos_reg.id_campo AND  "
                    + "  hc_detalle.id_campo = 108458 and hc_registro.id_registro = (select min(id_registro) from hc_registro where id_paciente=cp.id_paciente and id_tipo_reg=54 and hr.folio=1))  as observacionessentadobd, "
                    + " "
                    + "(SELECT  hc_detalle.valor FROM public.hc_registro, public.hc_detalle, public.hc_tipo_reg, public.hc_campos_reg WHERE hc_registro.id_tipo_reg = hc_tipo_reg.id_tipo_reg AND hc_detalle.id_registro = hc_registro.id_registro AND hc_detalle.id_campo = hc_campos_reg.id_campo AND  "
                    + "  hc_detalle.id_campo = 111700 and hc_registro.id_registro = (select min(id_registro) from hc_registro where id_paciente=cp.id_paciente and id_tipo_reg=54 and hr.folio=1))  as sentadobrazoizquierdopas, "
                    + " "
                    + "(SELECT  hc_detalle.valor FROM public.hc_registro, public.hc_detalle, public.hc_tipo_reg, public.hc_campos_reg WHERE hc_registro.id_tipo_reg = hc_tipo_reg.id_tipo_reg AND hc_detalle.id_registro = hc_registro.id_registro AND hc_detalle.id_campo = hc_campos_reg.id_campo AND  "
                    + "  hc_detalle.id_campo = 111701 and hc_registro.id_registro = (select min(id_registro) from hc_registro where id_paciente=cp.id_paciente and id_tipo_reg=54 and hr.folio=1))  as sentadobrazoizquierdopad, "
                    + " "
                    + "(SELECT  hc_detalle.valor FROM public.hc_registro, public.hc_detalle, public.hc_tipo_reg, public.hc_campos_reg WHERE hc_registro.id_tipo_reg = hc_tipo_reg.id_tipo_reg AND hc_detalle.id_registro = hc_registro.id_registro AND hc_detalle.id_campo = hc_campos_reg.id_campo AND  "
                    + "  hc_detalle.id_campo = 111702 and hc_registro.id_registro = (select min(id_registro) from hc_registro where id_paciente=cp.id_paciente and id_tipo_reg=54 and hr.folio=1))  as observacionessentadobrazoizquierdo, "
                    + " "
                    + "(SELECT  hc_detalle.valor FROM public.hc_registro, public.hc_detalle, public.hc_tipo_reg, public.hc_campos_reg WHERE hc_registro.id_tipo_reg = hc_tipo_reg.id_tipo_reg AND hc_detalle.id_registro = hc_registro.id_registro AND hc_detalle.id_campo = hc_campos_reg.id_campo AND  "
                    + "  hc_detalle.id_campo = 111703 and hc_registro.id_registro = (select min(id_registro) from hc_registro where id_paciente=cp.id_paciente and id_tipo_reg=54 and hr.folio=1))  as decubitopas, "
                    + " "
                    + "(SELECT  hc_detalle.valor FROM public.hc_registro, public.hc_detalle, public.hc_tipo_reg, public.hc_campos_reg WHERE hc_registro.id_tipo_reg = hc_tipo_reg.id_tipo_reg AND hc_detalle.id_registro = hc_registro.id_registro AND hc_detalle.id_campo = hc_campos_reg.id_campo AND  "
                    + "  hc_detalle.id_campo = 111704 and hc_registro.id_registro = (select min(id_registro) from hc_registro where id_paciente=cp.id_paciente and id_tipo_reg=54 and hr.folio=1))  as decubitopad, "
                    + " "
                    + "(SELECT  hc_detalle.valor FROM public.hc_registro, public.hc_detalle, public.hc_tipo_reg, public.hc_campos_reg WHERE hc_registro.id_tipo_reg = hc_tipo_reg.id_tipo_reg AND hc_detalle.id_registro = hc_registro.id_registro AND hc_detalle.id_campo = hc_campos_reg.id_campo AND  "
                    + "  hc_detalle.id_campo = 111705 and hc_registro.id_registro = (select min(id_registro) from hc_registro where id_paciente=cp.id_paciente and id_tipo_reg=54 and hr.folio=1))  as observacionesdecubito, "
                    + " "
                    + "(SELECT  hc_detalle.valor FROM public.hc_registro, public.hc_detalle, public.hc_tipo_reg, public.hc_campos_reg WHERE hc_registro.id_tipo_reg = hc_tipo_reg.id_tipo_reg AND hc_detalle.id_registro = hc_registro.id_registro AND hc_detalle.id_campo = hc_campos_reg.id_campo AND  "
                    + "  hc_detalle.id_campo = 111706 and hc_registro.id_registro = (select min(id_registro) from hc_registro where id_paciente=cp.id_paciente and id_tipo_reg=54 and hr.folio=1))  as condicionespecialpas, "
                    + " "
                    + "(SELECT  hc_detalle.valor FROM public.hc_registro, public.hc_detalle, public.hc_tipo_reg, public.hc_campos_reg WHERE hc_registro.id_tipo_reg = hc_tipo_reg.id_tipo_reg AND hc_detalle.id_registro = hc_registro.id_registro AND hc_detalle.id_campo = hc_campos_reg.id_campo AND  "
                    + "  hc_detalle.id_campo = 111707 and hc_registro.id_registro = (select min(id_registro) from hc_registro where id_paciente=cp.id_paciente and id_tipo_reg=54 and hr.folio=1))  as condicionespecialpad, "
                    + " "
                    + "(SELECT  hc_detalle.valor FROM public.hc_registro, public.hc_detalle, public.hc_tipo_reg, public.hc_campos_reg WHERE hc_registro.id_tipo_reg = hc_tipo_reg.id_tipo_reg AND hc_detalle.id_registro = hc_registro.id_registro AND hc_detalle.id_campo = hc_campos_reg.id_campo AND  "
                    + "  hc_detalle.id_campo = 111709 and hc_registro.id_registro = (select min(id_registro) from hc_registro where id_paciente=cp.id_paciente and id_tipo_reg=54 and hr.folio=1))  as observacioncondicionespecial, "
                    + "(SELECT  hc_detalle.valor FROM public.hc_registro, public.hc_detalle, public.hc_tipo_reg, public.hc_campos_reg WHERE hc_registro.id_tipo_reg = hc_tipo_reg.id_tipo_reg AND hc_detalle.id_registro = hc_registro.id_registro AND hc_detalle.id_campo = hc_campos_reg.id_campo AND  "
                    + "  hc_detalle.id_campo = 111703 and hc_registro.id_registro = (select min(id_registro) from hc_registro where id_paciente=cp.id_paciente and id_tipo_reg=54 and hr.folio=1) ) as tensionarterialsistolicaibase, "
                    + "(SELECT  hc_detalle.valor FROM public.hc_registro, public.hc_detalle, public.hc_tipo_reg, public.hc_campos_reg WHERE hc_registro.id_tipo_reg = hc_tipo_reg.id_tipo_reg AND hc_detalle.id_registro = hc_registro.id_registro AND hc_detalle.id_campo = hc_campos_reg.id_campo AND  "
                    + "  hc_detalle.id_campo = 111704 and hc_registro.id_registro = (select min(id_registro) from hc_registro where id_paciente=cp.id_paciente and id_tipo_reg=54 and hr.folio=1)) as tensionarterialdiastolicaibase, "
                    + "(select min(fecha_reg) from hc_registro where id_paciente=cp.id_paciente and id_tipo_reg=54 and hr.folio=1) as fechatomatensionarterialibase, "
                    + "(SELECT 'Sin Dato..') as clasificacionatensionarterialibase, "
                    + "(SELECT  hc_detalle.valor FROM public.hc_registro, public.hc_detalle, public.hc_tipo_reg, public.hc_campos_reg WHERE hc_registro.id_tipo_reg = hc_tipo_reg.id_tipo_reg AND hc_detalle.id_registro = hc_registro.id_registro AND hc_detalle.id_campo = hc_campos_reg.id_campo AND  "
                    + "  hc_detalle.id_campo = 111703 and hc_registro.id_registro = (select max(id_registro) from hc_registro where id_paciente=cp.id_paciente and id_tipo_reg=54 and hr.folio=1) ) as ultimatensionasistolica, "
                    + "(SELECT  hc_detalle.valor FROM public.hc_registro, public.hc_detalle, public.hc_tipo_reg, public.hc_campos_reg WHERE hc_registro.id_tipo_reg = hc_tipo_reg.id_tipo_reg AND hc_detalle.id_registro = hc_registro.id_registro AND hc_detalle.id_campo = hc_campos_reg.id_campo AND  "
                    + "  hc_detalle.id_campo = 111704 and hc_registro.id_registro = (select max(id_registro) from hc_registro where id_paciente=cp.id_paciente and id_tipo_reg=54 and hr.folio=1)) as ultimatensionadiastolica, "
                    + "(SELECT 98) as clasificacionHTA, "
                    + "(select hr.fecha_reg) as fultimatomahta, "
                    + " "
                    + "(SELECT  hc_detalle.valor FROM public.hc_registro, public.hc_detalle, public.hc_tipo_reg, public.hc_campos_reg WHERE hc_registro.id_tipo_reg = hc_tipo_reg.id_tipo_reg AND hc_detalle.id_registro = hc_registro.id_registro AND hc_detalle.id_campo = hc_campos_reg.id_campo AND  "
                    + "  hc_detalle.id_campo = 108459 and hc_registro.id_registro = hr.id_registro)  as peso, "
                    + "(SELECT hr.fecha_reg) as fechaultimopeso, "
                    + "(SELECT  hc_detalle.valor FROM public.hc_registro, public.hc_detalle, public.hc_tipo_reg, public.hc_campos_reg WHERE hc_registro.id_tipo_reg = hc_tipo_reg.id_tipo_reg AND hc_detalle.id_registro = hc_registro.id_registro AND hc_detalle.id_campo = hc_campos_reg.id_campo AND  "
                    + "  hc_detalle.id_campo = 108460 and hc_registro.id_registro = hr.id_registro)  as talla , "
                    + "(select 'CALCULAR CON PESO Y TALLA') as IMC, "
                    + "(select 'VALIDAR SEGUN TABLA ') as CLASIFICACIONIMC, "
                    + "(SELECT  hc_detalle.valor FROM public.hc_registro, public.hc_detalle, public.hc_tipo_reg, public.hc_campos_reg WHERE hc_registro.id_tipo_reg = hc_tipo_reg.id_tipo_reg AND hc_detalle.id_registro = hc_registro.id_registro AND hc_detalle.id_campo = hc_campos_reg.id_campo AND  "
                    + "  hc_detalle.id_campo = 108465 and hc_registro.id_registro = hr.id_registro)  as perimetro_abdominal, "
                    + "(select hr.fecha_reg) as fultimperimetroabdominal, "
                    + "(select 98) as clasificacionrcvactualib, "
                    + "(select 98) as fechaclasificacionrcvactualib, "
                    + "(select 98) as clasificacionrcvactual, "
                    + "(select 98) as fechaclasificacionrcvactual, "
                    + "(select 'N') as glicemiabasal, "
                    + "(select 'N') as fechaglicemiabasal, "
                    + "(select 'N') as parcialdeorina, "
                    + "(select 'N') as fechaparcialdeorina, "
                    + "(select 'N') as creatininasangre, "
                    + "(select 'N') as fechacreatininasangre, "
                    + "(select 'N') as colesteroltotal, "
                    + "(select 'N') as fechacolesteroltotal, "
                    + "(select 'N') as hdl, "
                    + "(select 'N') as fechahdl, "
                    + "(select 'N') as ldl, "
                    + "(select 'N') as fechaldl, "
                    + "(select 'N') as colesterol, "
                    + "(select 'N') as fechacolesterol, "
                    + "(select 'N') as hemoglobinaglicolisada, "
                    + "(select 'N') as fechahemoglobinaglicolisada, "
                    + "(select 98) as relalbuminuriacreatinuria, "
                    + "(select 98) as fechaalbuminuriacreatinuria, "
                    + "(select 'N') as microalbuminuria, "
                    + "(select 'N') as fechamicroalbuminuria, "
                    + "(select 'N') as reportedeekg, "
                    + "(select 'N') as fechaekg, "
                    + "(select 98) as tfg, "
                    + "(select 98) as estadioseguntfg, "
                    + "(select 'REPORTE') as MEDICAMENTO1, "
                    + "(select 'REPORTE') as MEDICAMENTO2, "
                    + "(select 'REPORTE') as MEDICAMENTO3, "
                    + "(select 'REPORTE') as MEDICAMENTO4, "
                    + "(select 'REPORTE') as MEDICAMENTO5, "
                    + "(select 'REPORTE') as MEDICAMENTO6, "
                    + "(select 'REPORTE') as MEDICAMENTO7, "
                    + "(select 'REPORTE') as MEDICAMENTO8, "
                    + "(select 'REPORTE') as MEDICAMENTO9, "
                    + "(select 'REPORTE') as MEDICAMENTO10, "
                    + "(select 98) as recibeinhibidorieca, "
                    + "(select 98) as recibeantagonistaaraii, "
                    + "(select 98) as adherenciattomorisky, "
                    + "(select 98) as tipolesionorganoblanco, "
                    + "(select 98) as novedades, "
                    + "(select 98) as fechamuerte, "
                    + "(select 98) as causamuerte, "
                    + "(select 98) as observaciones, "
                    + "(select 98) as nombreinsulina, "
                    + "(select 98) as dosisinsulina, "
                    + "(select 98) as aplicapoblacioncohorte, "
                    + "(select 98) as poblacionnuevaparabase, "
                    + "(select cs.nombre_sede from cfg_sede cs where  "
                    + " cs.id_sede = (select fc.id_sede from fac_caja fc where fc.id_caja = (select ffp.id_caja from fac_factura_paciente ffp where ffp.id_cita = hr.id_cita))), "
                    + " (select ffp.id_caja from fac_factura_paciente ffp where ffp.id_cita = hr.id_cita),hr.id_cita "
                    + " "
                    + "FROM  "
                    + "  public.cfg_pacientes cp "
                    + "  inner join  public.hc_registro hr on cp.id_paciente=hr.id_paciente and hr.id_registro =(select max(id_registro) from hc_registro where id_paciente=cp.id_paciente and id_tipo_reg=54 and folio=1) "
                    + "   left join cfg_clasificaciones ti on ti.id = cp.tipo_identificacion "
                    + "left join cfg_clasificaciones ge on ge.id = cp.genero "
                    + "left join cfg_clasificaciones zo on zo.id = cp.zona "
                    + "left join cfg_clasificaciones etnia on etnia.id = cp.etnia "
                    + "WHERE   "
                    + "   hr.id_tipo_reg = 54 and hr.folio=1 ";
                    if(idAdministradora!=0) sql+=" and cp.id_administradora=? ";
                    sql+="  order by cp.identificacion";
                    query = getEntityManager().createNativeQuery(sql);
                    if(idAdministradora!=0)query.setParameter(1,idAdministradora);
                    lista = query.getResultList();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return lista;
    }

}
