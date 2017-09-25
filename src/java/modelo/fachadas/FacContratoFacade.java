/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo.fachadas;

import beans.utilidades.EstadisticaGeneral;
import beans.utilidades.ManualTarifarioSOATISS;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.Query;
import javax.persistence.TemporalType;
import modelo.entidades.FacContrato;

/**
 *
 * @author santos
 */
@Stateless
public class FacContratoFacade extends AbstractFacade<FacContrato> {

    public FacContratoFacade() {
        super(FacContrato.class);
    }

    public List<FacContrato> buscarOrdenado() {
        try {
            String hql = "SELECT c FROM FacContrato c ORDER By c.codigoContrato";
            return getEntityManager().createQuery(hql).getResultList();
        } catch (Exception e) {
            return null;
        }
    }

    public List<FacContrato> buscarPorCodigo(String codigoContrato) {
        try {
            String hql = "SELECT c FROM FacContrato c WHERE c.codigoContrato LIKE '" + codigoContrato + "'";
            return getEntityManager().createQuery(hql).getResultList();
        } catch (Exception e) {
            return null;
        }
    }

    public FacContrato buscarPorNombre(String nombreContrato) {
        try {
            String hql = "SELECT c FROM FacContrato c WHERE c.descripcion LIKE '" + nombreContrato + "'";
            return (FacContrato) getEntityManager().createQuery(hql).getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }
    
    public List<FacContrato> buscarPorAdministrador(int idAdminitradora) {
        try {
            String hql = "SELECT c FROM FacContrato c WHERE c.idAdministradora.idAdministradora =:idAdministradora";
            return getEntityManager().createQuery(hql).setParameter("idAdministradora", idAdminitradora).getResultList();
        } catch (Exception e) {
            return null;
        }
    }
    
    public List<ManualTarifarioSOATISS> getConsultaManual(int idManualTarifario){
        List<ManualTarifarioSOATISS> lista = new ArrayList<>();
        try {
            String sql  =   "select fs.codigo_cup as codigo,fs.nombre_servicio as servicio,fs.factor_iss as uvr,fu.smlvd, " + 
                            "	case when fc.tipo_manual=1 then ft.valor_final else round((fu.smlvd*(case when fc.tipo_manual=2 then fs.factor_iss else fs.factor_soat end ))+(case when fc.signo_porcentaje ='+' then  1  else -1 end * fc.porcentaje))  end as valorServicio,grupo.descripcion as grupo,fc.tipo_manual " +
                            "from  fac_contrato fc " +
                            "inner join fac_manual_tarifario fm on fc.id_manual_tarifario = fm.id_manual_tarifario " +
                            "inner join fac_manual_tarifario_servicio ft on ft.id_manual_tarifario = fm.id_manual_tarifario " +
                            "inner join fac_servicio fs on fs.id_servicio = ft.id_Servicio " +
                            "left join fac_unidad_valor fu on fu.anio = fc.annio_manual "+
                            "left join cfg_clasificaciones grupo on grupo.maestro='GrupoServicio' and grupo.id = fs.grupo_servicio " +
                            "where fc.id_manual_tarifario=?1 " ;
             
            Query query = getEntityManager().createNativeQuery(sql).setParameter(1, idManualTarifario);
            List<Object[]> results=query.getResultList();
            Object[] resultData;
            for(int i=0;i<results.size();i++){
                ManualTarifarioSOATISS m = new ManualTarifarioSOATISS();
                resultData = results.get(i);
                m.setCodigo(resultData[0].toString());
                m.setServicio(resultData[1].toString());
                m.setUvr(resultData[6].toString().equals("2")?resultData[2].toString():(resultData[5]!=null?resultData[5].toString():""));
                m.setSmldv(resultData[3]!=null?Double.parseDouble(resultData[3].toString()):0d);
                m.setValorServicio(Double.parseDouble(resultData[4].toString()));
                lista.add(m);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return lista;
    }
    
    public List<EstadisticaGeneral> getEstadisticaGeneral(String filtro){
        List<EstadisticaGeneral> lista = new ArrayList<>();
        try {
            String sql  ="select 	paciente.id_paciente,  " +
                            "	fp.codigo_documento,   " +
                            "	min(registro.folio) as registro,  " +
                            "	paciente.primer_apellido,  " +
                            "	paciente.segundo_apellido,  " +
                            "	paciente.primer_nombre,  " +
                            "	paciente.segundo_nombre,  " +
                            "	paciente.identificacion,  " +
                            "	tipoI.descripcion tipo_identificacion,  " +
                            "	genero.descripcion genero,  " +
                            "	paciente.fecha_nacimiento,  " +
                            "	date_part('year',age(paciente.fecha_nacimiento)) edad,  " +
                            "	mun.descripcion municipio,  " +
                            "	case when length(cast(min(registro.fecha_reg) as character)) =0 then min(registro.fecha_reg) else fp.fecha_sistema end fecha_Registro,  " +
                            "	case when length(cast(min(registro.fecha_reg) as character)) =0 then min(registro.fecha_reg) else fp.fecha_sistema end fecha_salida,  " +
                            "	tipoIngreso.descripcion tipo_ingreso,  " +
                            "	case when fp.anulada  then'ANULADA'  else   " +
                            "	case when fp.facturada_en_admi then'FACTURADA' else 'PENDIENTE FACTURACION' end end estado,  " +
                            "	fp.fecha_sistema as fecha_facturacion,  " +
                            "	paciente.direccion,  " +
                            "	paciente.barrio,  " +
                            "	'' comuna,  " +
                            "	concat(medico.primer_nombre,' ',medico.segundo_nombre,' ',medico.primer_apellido,' ',medico.segundo_apellido) medico,  " +
                            "	usuario.login_usuario usuario,  " +
                            "	sede.nombre_sede,  " +
                            "	case when (victima_conflicto_armado) then 'SI' else 'NO' end victima,  " +
                            "	case when paciente.poblacion_lbgt then 'SI' else 'NO' end lg,  " +
                            "	case when paciente.desplazado then 'SI' else 'NO' end desplazado,  " +
                            "	case when (paciente.victima_maltrato) then 'SI' else 'NO' end amaltrato,  " +
                            "	gestacion.descripcion as gestacion,  " +
                            "	etnia.descripcion as etnia,  " +
                            "	discapacidad.descripcion as discapacidad,  " +
                            "	escolaridad.descripcion as escolaridad,  " +
                            "	religion.descripcion as religion,  " +
                            "	ocupacion.descripcion as ocupacion,  " +
                            "	periodo.mes, periodo.anio, "
                            + " adm.razon_social as administradora, "
                            + " contrato.descripcion as contrato " +
                            "from fac_factura_paciente fp  " +
                            "inner join fac_factura_servicio ff on ff.id_factura = id_factura_paciente "+
                            "left join cit_citas cita on cita.id_cita =fp.id_cita  " +
                            "left join hc_registro registro on registro.id_cita =cita.id_cita  " +
                            "inner join cfg_pacientes paciente on paciente.id_paciente= fp.id_paciente  " +
                            "inner join cfg_clasificaciones tipoI on tipoI.id = paciente.tipo_identificacion and tipoI.maestro='TipoIdentificacion'  " +
                            "left join cfg_clasificaciones genero on genero.id = paciente.genero and genero.maestro='Genero'  " +
                            "left join cfg_clasificaciones mun on mun.id = paciente.municipio and mun.maestro='Municipios'  " +
                            "left join cfg_clasificaciones tipoIngreso on tipoIngreso.id = fp.tipo_ingreso and tipoIngreso.maestro='TipoIngreso'  " +
                            "left join cfg_usuarios medico on medico.id_usuario = ff.id_medico  " +
                            "left join fac_administradora adm on adm.id_administradora = fp.id_administradora  " +
                            "left join fac_contrato contrato on contrato.id_contrato = fp.id_contrato  " +
                            "inner join fac_caja caja on caja.id_caja = fp.id_caja  " +
                            "inner join fac_periodo periodo on periodo.id_periodo = fp.id_periodo  " +
                            "left join cfg_usuarios usuario on usuario.id_usuario = caja.id_usuario  " +
                            "left join cfg_sede sede on sede.id_sede = caja.id_sede  " +
                            "left join cfg_clasificaciones gestacion on gestacion.id = paciente.id_gestacion and gestacion.maestro='Gestacion'  " +
                            "left join cfg_clasificaciones etnia on etnia.id = paciente.etnia and etnia.maestro='Etnia'  " +
                            "left join cfg_clasificaciones discapacidad on discapacidad.id = paciente.id_discapacidad and discapacidad.maestro='Discapacidad'  " +
                            "left join cfg_clasificaciones escolaridad on escolaridad.id = paciente.escolaridad and escolaridad.maestro='Escolaridad'  " +
                            "left join cfg_clasificaciones religion on religion.id = paciente.id_religion and religion.maestro='Religion'  " +
                            "left join cfg_clasificaciones ocupacion on ocupacion.id = paciente.ocupacion and ocupacion.maestro='Ocupacion'  " +
                            "WHERE fp.id_factura_paciente>0  "+filtro+
                            "  group by fp.id_factura_paciente,  " +
                            "	paciente.id_paciente,  " +
                            "	fp.codigo_documento,   " +
                            "	paciente.primer_apellido,  " +
                            "	paciente.segundo_apellido,  " +
                            "	paciente.primer_nombre,  " +
                            "	paciente.segundo_nombre,  " +
                            "	paciente.identificacion,  " +
                            "	tipoI.descripcion,  " +
                            "	genero.descripcion,  " +
                            "	paciente.fecha_nacimiento,  " +
                            "	mun.descripcion,  " +
                            "	fp.fecha_sistema,  " +
                            "	tipoIngreso.descripcion,  " +
                            "	medico.primer_nombre  " +
                            "	,medico.segundo_nombre,  " +
                            "	medico.primer_apellido,  " +
                            "	medico.segundo_apellido,  " +
                            "	usuario.login_usuario,  " +
                            "	sede.nombre_sede,  " +
                            "	gestacion.descripcion,  " +
                            "	etnia.descripcion,  " +
                            "	discapacidad.descripcion,  " +
                            "	escolaridad.descripcion,  " +
                            "	religion.descripcion,  " +
                            "	ocupacion.descripcion,  " +
                            "	periodo.mes, periodo.anio,"
                    + "adm.razon_social,contrato.descripcion  " +
                            " order by fp.fecha_elaboracion  " ;
            Query query = getEntityManager().createNativeQuery(sql);
            List<Object[]> results=query.getResultList();
            Object[] resultData;
            for(int i=0;i<results.size();i++){
                EstadisticaGeneral estadistica = new EstadisticaGeneral();
                resultData = results.get(i);
                estadistica.setFactura(resultData[1].toString());
                estadistica.setHistoria(resultData[2]!=null?resultData[2].toString():"");
                estadistica.setNombre1(resultData[3]!=null?resultData[3].toString():"");
                estadistica.setNombre2(resultData[4]!=null?resultData[4].toString():"");
                estadistica.setApellido1(resultData[5]!=null?resultData[5].toString():"");
                estadistica.setApellido2(resultData[6]!=null?resultData[6].toString():"");
                estadistica.setTipoIdentificacion(resultData[8]!=null?resultData[8].toString():"");
                estadistica.setIdentificacion(resultData[7]!=null?resultData[7].toString():"");
                estadistica.setEmpresa(resultData[36]!=null?resultData[36].toString():"");
                estadistica.setContrato(resultData[37]!=null?resultData[37].toString():"");
                estadistica.setSede(resultData[23]!=null?resultData[23].toString():"");
                lista.add(estadistica);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return lista;
    }
    
    public List<EstadisticaGeneral> getEstadisticaGeneralConsolidada(String filtro){
        List<EstadisticaGeneral> lista = new ArrayList<>();
        try {
            String sql  ="select 	paciente.id_paciente,  " +
                            "	fp.codigo_documento,   " +
                            "	min(registro.folio) as registro,  " +
                            "	paciente.primer_nombre,  " +
                            "	paciente.segundo_nombre,  " +
                            "	paciente.primer_apellido,  " +
                            "	paciente.segundo_apellido,  " +
                            "	paciente.identificacion,  " +
                            "	tipoI.descripcion tipo_identificacion,  " +
                            "	genero.descripcion genero,  " +
                            "	paciente.fecha_nacimiento,  " +
                            "	date_part('year',age(paciente.fecha_nacimiento)) edad,  " +
                            "	mun.descripcion municipio,  " +
                            "	case when length(cast(min(registro.fecha_reg) as character)) =0 then min(registro.fecha_reg) else fp.fecha_sistema end fecha_Registro,  " +
                            "	case when length(cast(min(registro.fecha_reg) as character)) =0 then min(registro.fecha_reg) else fp.fecha_sistema end fecha_salida,  " +
                            "	tipoIngreso.descripcion tipo_ingreso,  " +
                            "	case when fp.anulada  then'ANULADA'  else   " +
                            "	case when fp.facturada_en_admi then'FACTURADA' else 'PENDIENTE FACTURACION' end end estado,  " +
                            "	fp.fecha_sistema as fecha_facturacion,  " +
                            "	paciente.direccion,  " +
                            "	paciente.barrio,  " +
                            "	'' comuna,  " +
                            "	concat(medico.primer_nombre,' ',medico.segundo_nombre,' ',medico.primer_apellido,' ',medico.segundo_apellido) medico,  " +
                            "	usuario.login_usuario usuario,  " +
                            "	sede.nombre_sede,  " +
                            "	case when (victima_conflicto_armado) then 'SI' else 'NO' end victima,  " +
                            "	case when paciente.poblacion_lbgt then 'SI' else 'NO' end lg,  " +
                            "	case when paciente.desplazado then 'SI' else 'NO' end desplazado,  " +
                            "	case when (paciente.victima_maltrato) then 'SI' else 'NO' end amaltrato,  " +
                            "	gestacion.descripcion as gestacion,  " +
                            "	etnia.descripcion as etnia,  " +
                            "	discapacidad.descripcion as discapacidad,  " +
                            "	escolaridad.descripcion as escolaridad,  " +
                            "	religion.descripcion as religion,  " +
                            "	ocupacion.descripcion as ocupacion,  " +
                            "	periodo.mes, periodo.anio, "
                            + " adm.razon_social as administradora, "
                            + " contrato.descripcion as contrato,"
                            + " fp.valor_total as valorTotal " +
                            "from fac_factura_paciente fp  " +
                            "inner join fac_factura_servicio ff on ff.id_factura = id_factura_paciente "+
                            "left join cit_citas cita on cita.id_cita =fp.id_cita  " +
                            "left join hc_registro registro on registro.id_cita =cita.id_cita  " +
                            "inner join cfg_pacientes paciente on paciente.id_paciente= fp.id_paciente  " +
                            "inner join cfg_clasificaciones tipoI on tipoI.id = paciente.tipo_identificacion and tipoI.maestro='TipoIdentificacion'  " +
                            "left join cfg_clasificaciones genero on genero.id = paciente.genero and genero.maestro='Genero'  " +
                            "left join cfg_clasificaciones mun on mun.id = paciente.municipio and mun.maestro='Municipios'  " +
                            "left join cfg_clasificaciones tipoIngreso on tipoIngreso.id = fp.tipo_ingreso and tipoIngreso.maestro='TipoIngreso'  " +
                            "left join cfg_usuarios medico on medico.id_usuario = ff.id_medico  " +
                            "left join fac_administradora adm on adm.id_administradora = fp.id_administradora  " +
                            "left join fac_contrato contrato on contrato.id_contrato = fp.id_contrato  " +
                            "inner join fac_caja caja on caja.id_caja = fp.id_caja  " +
                            "inner join fac_periodo periodo on periodo.id_periodo = fp.id_periodo  " +
                            "left join cfg_usuarios usuario on usuario.id_usuario = caja.id_usuario  " +
                            "left join cfg_sede sede on sede.id_sede = caja.id_sede  " +
                            "left join cfg_clasificaciones gestacion on gestacion.id = paciente.id_gestacion and gestacion.maestro='Gestacion'  " +
                            "left join cfg_clasificaciones etnia on etnia.id = paciente.etnia and etnia.maestro='Etnia'  " +
                            "left join cfg_clasificaciones discapacidad on discapacidad.id = paciente.id_discapacidad and discapacidad.maestro='Discapacidad'  " +
                            "left join cfg_clasificaciones escolaridad on escolaridad.id = paciente.escolaridad and escolaridad.maestro='Escolaridad'  " +
                            "left join cfg_clasificaciones religion on religion.id = paciente.id_religion and religion.maestro='Religion'  " +
                            "left join cfg_clasificaciones ocupacion on ocupacion.id = paciente.ocupacion and ocupacion.maestro='Ocupacion'  " +
                            "WHERE fp.id_factura_paciente>0  "+filtro+
                            "  group by fp.id_factura_paciente,  " +
                            "	paciente.id_paciente,  " +
                            "	fp.codigo_documento,   " +
                            "	paciente.primer_apellido,  " +
                            "	paciente.segundo_apellido,  " +
                            "	paciente.primer_nombre,  " +
                            "	paciente.segundo_nombre,  " +
                            "	paciente.identificacion,  " +
                            "	tipoI.descripcion,  " +
                            "	genero.descripcion,  " +
                            "	paciente.fecha_nacimiento,  " +
                            "	mun.descripcion,  " +
                            "	fp.fecha_sistema,  " +
                            "	tipoIngreso.descripcion,  " +
                            "	medico.primer_nombre  " +
                            "	,medico.segundo_nombre,  " +
                            "	medico.primer_apellido,  " +
                            "	medico.segundo_apellido,  " +
                            "	usuario.login_usuario,  " +
                            "	sede.nombre_sede,  " +
                            "	gestacion.descripcion,  " +
                            "	etnia.descripcion,  " +
                            "	discapacidad.descripcion,  " +
                            "	escolaridad.descripcion,  " +
                            "	religion.descripcion,  " +
                            "	ocupacion.descripcion,  " +
                            "	periodo.mes, periodo.anio,"
                            + "adm.razon_social,contrato.descripcion,valorTotal  " +
                            " order by fp.id_administradora,fp.id_contrato,fp.fecha_elaboracion  " ;
            Query query = getEntityManager().createNativeQuery(sql);
            List<Object[]> results=query.getResultList();
            Object[] resultData;
            for(int i=0;i<results.size();i++){
                EstadisticaGeneral estadistica = new EstadisticaGeneral();
                resultData = results.get(i);
                estadistica.setFactura(resultData[1].toString());
                estadistica.setHistoria(resultData[2]!=null?resultData[2].toString():"");
                estadistica.setNombre1(resultData[3]!=null?resultData[3].toString():"");
                estadistica.setNombre2(resultData[4]!=null?resultData[4].toString():"");
                estadistica.setApellido1(resultData[5]!=null?resultData[5].toString():"");
                estadistica.setApellido2(resultData[6]!=null?resultData[6].toString():"");
                estadistica.setTipoIdentificacion(resultData[8]!=null?resultData[8].toString():"");
                estadistica.setIdentificacion(resultData[7]!=null?resultData[7].toString():"");
                estadistica.setEmpresa(resultData[36]!=null?resultData[36].toString():"");
                estadistica.setContrato(resultData[37]!=null?resultData[37].toString():"");
                estadistica.setSede(resultData[23]!=null?resultData[23].toString():"");
                estadistica.setFechaFactura(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(resultData[17].toString()));
                estadistica.setValorTotal(Double.parseDouble(resultData[38]!=null?resultData[38].toString():"0.0"));
                lista.add(estadistica);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return lista;
    }
    
    public List<FacContrato> contratoFiltros(String filtro,Date fecha){
        try {
            String hql = "SELECT f FROM FacContrato f "+filtro;
            Query q ;
            if(fecha!=null){
                q = getEntityManager().createQuery(hql).setParameter("param1", fecha, TemporalType.TIMESTAMP);
            }else{
                q = getEntityManager().createQuery(hql);
            }
            return q.getResultList();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ArrayList();
    }
}
