/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servlets;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import modelo.fachadas.DBConnector;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

/**
 *
 * @author miguel
 */
public class EstadisticasGeneralesServlet extends HttpServlet{
    
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Connection con = null;
         PreparedStatement ps = null;
         ResultSet rs = null;
         String url = getServletContext().getRealPath("/");
         String tipoFiltro= request.getParameter("filtro");
         String filtro1 =  request.getParameter("param1");
         String filtro2 =  request.getParameter("param2");
         String filtro="";
         String nombreArchivo = "ESTADISTICA_GENERALES.xls";
         try {
             //configuramos filtro
           switch(tipoFiltro){
            case "1":
                if(!filtro1.equals("0")){
                    filtro= " and fp.id_administradora ="+filtro1;
                }
                break;
            case "2":
                filtro= " and fp.id_paciente ="+filtro1;
                break;
            case "3":
                SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd");
                filtro= " and  to_char(fp.fecha_sistema, 'yyyy-MM-dd') between '"+filtro1+"' and '"+filtro2+"' ";
                break;
            case "4":
        switch (filtro1) {
            case "1":
                filtro =" and fp.facturada_en_admi =TRUE ";
                break;
            case "2":
                filtro =" and fp.facturada_en_admi =FALSE ";
                break;
            case "3":
                filtro =" and fp.anulada = TRUE ";
                break;
            default:
                break;
        }
                break;
            case "6":
                filtro = "and ff.id_medico ="+filtro1;
                break;
                
        }
             
             con = DBConnector.getInstance().getConnection();
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
                            "	to_char(paciente.fecha_nacimiento, 'dd/MM/yyyy') fecha_nacimiento,  " +
                            "	date_part('year',age(paciente.fecha_nacimiento)) edad,  " +
                            "	mun.descripcion municipio,  " +
                            "	case when length(cast(min(registro.fecha_reg) as character)) =0 then min(registro.fecha_reg) else fp.fecha_sistema end fecha_Registro,  " +
                            "	case when length(cast(min(registro.fecha_reg) as character)) =0 then min(registro.fecha_reg) else fp.fecha_sistema end fecha_salida,  " +
                            "	tipoIngreso.descripcion tipo_ingreso,  " +
                            "	case when fp.anulada  then'ANULADA'  else   " +
                            "	case when fp.facturada_en_admi then'FACTURADA' else 'PENDIENTE FACTURACION' end end estado,  " +
                            "	to_char(fp.fecha_sistema, 'dd/MM/yyyy') as fecha_facturacion,  " +
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
                            "	escolaridad.descripcion as educacion,  " +
                            "	religion.descripcion as religion,  " +
                            "	ocupacion.descripcion as ocupacion,  " +
                            "	periodo.mes, periodo.anio,administradora.razon_social,contrato.descripcion as contrato,  " +
                            " fs.codigo_servicio,codigo_cup,fs.nombre_servicio,ff.cantidad_servicio,ff.valor_parcial,ff.valor_empresa, "+
                            "	fp.copago,fp.cuota_moderadora,ff.valor_parcial+ff.valor_iva valor_total,ff.fecha_servicio,ff.diagnostico_principal,ff.diagnostico_relacionado,regimen.descripcion as regimen,costo.nom_centro_costo as centro_costo, "+
                            " finalidad.descripcion as finalidad,estrato.descripcion as estrato,programa.nom_programa,programa_item.frecuencia,programa_item.nom_actividad,paciente.carnet, "+
                            " (select valor from hc_detalle h where h.id_Campo=3 and h.id_Registro=registro.id_registro limit 1) as motivo_consulta, "+
                            " (select valor from hc_detalle h where h.id_Campo=1 and h.id_Registro=registro.id_registro limit 1) as dx1, "+
                            " (select valor from hc_detalle h where h.id_Campo=2 and h.id_Registro=registro.id_registro limit 1) as dx2, "+
                            " (select valor from hc_detalle h where h.id_Campo=72 and h.id_Registro=registro.id_registro limit 1) as tipo_diagnostico "+
                            "from fac_factura_paciente fp  " +
                            "inner join fac_factura_servicio ff on ff.id_factura = id_factura_paciente " +
                            "inner join fac_servicio fs on fs.id_servicio = ff.id_servicio "+
                            "left join cfg_clasificaciones finalidad on finalidad.id = fs.finalidad and finalidad.maestro='FinalidadProcedimiento'  " +
                            "left join cfg_centro_costo costo on costo.id_centro_costo = fs.centro_costo "+
                            "left join cit_citas cita on cita.id_cita =fp.id_cita  " +
                            "left join hc_registro registro on registro.id_cita =cita.id_cita  " +
                            "inner join cfg_pacientes paciente on paciente.id_paciente= fp.id_paciente  " +
                            "left join cfg_clasificaciones estrato on estrato.id = paciente.nivel and estrato.maestro='Estrato'  " +
                            "left join fac_administradora administradora on administradora.id_administradora = fp.id_administradora "+
                            "left join fac_contrato contrato on contrato.id_contrato = fp.id_contrato "+
                            "left join pyp_programa_asoc pyp on pyp.id_contrato = contrato.id_contrato "+
                            "left join pyp_programa programa on programa.id_programa = pyp.id_program "+
                            "left join pyp_programa_item programa_item on programa_item.id_programa = programa.id_programa and programa_item.id_servicio=ff.id_servicio "+
                            "left join cfg_clasificaciones regimen on regimen.id = paciente.regimen and regimen.maestro='Regimen'  " +
                            "inner join cfg_clasificaciones tipoI on tipoI.id = paciente.tipo_identificacion and tipoI.maestro='TipoIdentificacion'  " +
                            "left join cfg_clasificaciones genero on genero.id = paciente.genero and genero.maestro='Genero'  " +
                            "left join cfg_clasificaciones mun on mun.id = paciente.municipio and mun.maestro='Municipios'  " +
                            "left join cfg_clasificaciones tipoIngreso on tipoIngreso.id = fp.tipo_ingreso and tipoIngreso.maestro='TipoIngreso'  " +
                            "left join cfg_usuarios medico on medico.id_usuario = registro.id_medico  " +
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
                            "	paciente.identificacion,paciente.carnet,  " +
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
                            "	periodo.mes, periodo.anio,administradora.razon_social,contrato.descripcion , "+
                            "   fs.codigo_servicio,codigo_cup,fs.nombre_servicio," +
                            "    ff.cantidad_servicio,ff.valor_parcial,ff.valor_empresa, " +
                            "    ff.valor_parcial,ff.valor_iva,ff.fecha_servicio,ff.diagnostico_principal,ff.diagnostico_relacionado,regimen.descripcion, " +
                            "  costo.nom_centro_costo ,finalidad.descripcion,programa.nom_programa,programa_item.frecuencia,programa_item.nom_actividad, "+
                            "  estrato.descripcion,registro.id_registro "+
                            " order by fp.fecha_elaboracion  " ;
             
             ps = con.prepareCall(sql);
             ps.clearParameters();
             rs = ps.executeQuery();
             
             String rutaArchivo = url + "/facturacion/reportes/";
            rutaArchivo = rutaArchivo + nombreArchivo;
             File archivo1 = new File(rutaArchivo);
            if (archivo1.exists()) {
                archivo1.delete();
            }
            archivo1.createNewFile();
            HSSFWorkbook libro = new HSSFWorkbook();
             
            HSSFCellStyle cellStyle = libro.createCellStyle();
            HSSFFont font = libro.createFont();
            font.setFontName(HSSFFont.FONT_ARIAL);
            font.setFontHeightInPoints((short) 10);
            font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
            font.setColor(HSSFColor.BLACK.index);
            font.setCharSet(HSSFFont.ANSI_CHARSET);
            cellStyle.setFont(font);
            /*Se inicializa el flujo de datos con el archivo xls*/
             FileOutputStream archi = new FileOutputStream(rutaArchivo);
             Sheet hoja;
             Cell celda;
             Row fila ;
            int i = 0;
            
            hoja = libro.createSheet("ESTADISTICAS GENERALES");
            fila = hoja.createRow(i);
            celda = fila.createCell(0);
            celda.setCellValue("FACTURA");
            celda.setCellStyle(cellStyle);
            
            celda = fila.createCell(1);
            celda.setCellValue("HISTORIA");
            celda.setCellStyle(cellStyle);
            
            celda = fila.createCell(2);
            celda.setCellValue("PRIMER APELLIDO");
            celda.setCellStyle(cellStyle);
            
            celda = fila.createCell(3);
            celda.setCellValue("SEGUNDO APELLIDO");
            celda.setCellStyle(cellStyle);
            
            celda = fila.createCell(4);
            celda.setCellValue("PRIMER NOMBRE");
            celda.setCellStyle(cellStyle);
            
            celda = fila.createCell(5);
            celda.setCellValue("SEGUNDO NOMBRE");
            celda.setCellStyle(cellStyle);
            
            celda = fila.createCell(6);
            celda.setCellValue("IDENTIFICACION");
            celda.setCellStyle(cellStyle);
            
            celda = fila.createCell(7);
            celda.setCellValue("TIPO IDENTIFICACION");
            celda.setCellStyle(cellStyle);
            
            celda = fila.createCell(8);
            celda.setCellValue("GENERO        ");
            celda.setCellStyle(cellStyle);
            
            celda = fila.createCell(9);
            celda.setCellValue("FECHA NACIMIENTO");
            celda.setCellStyle(cellStyle);
            
            celda = fila.createCell(10);
            celda.setCellValue("EDAD");
            celda.setCellStyle(cellStyle);
            
            celda = fila.createCell(11);
            celda.setCellValue("MUNICIPIO");
            celda.setCellStyle(cellStyle);
            
            celda = fila.createCell(12);
            celda.setCellValue("FECHA INGRESO               ");
            celda.setCellStyle(cellStyle);
            
            celda = fila.createCell(13);
            celda.setCellValue("FECHA EGRESO                ");
            celda.setCellStyle(cellStyle);
            
            celda = fila.createCell(14);
            celda.setCellValue("DIAGNOSTICO");
            celda.setCellStyle(cellStyle);
            
            celda = fila.createCell(15);
            celda.setCellValue("NOMBRE DEL DIAGNOSTICO DE EGRESO");
            celda.setCellStyle(cellStyle);
            
            celda = fila.createCell(16);
            celda.setCellValue("TIPO ATENCION");
            celda.setCellStyle(cellStyle);
            
            celda = fila.createCell(17);
            celda.setCellValue("REGIMEN");
            celda.setCellStyle(cellStyle);
            
            celda = fila.createCell(18);
            celda.setCellValue("TIPO DIG");
            celda.setCellStyle(cellStyle);

            celda = fila.createCell(19);
            celda.setCellValue("CAUSA MUERTE");
            celda.setCellStyle(cellStyle);
            
            celda = fila.createCell(20);
            celda.setCellValue("ESTADO                   ");
            celda.setCellStyle(cellStyle);
            
            celda = fila.createCell(21);
            celda.setCellValue("DX ADICIONAL1");
            celda.setCellStyle(cellStyle);
            
            celda = fila.createCell(22);
            celda.setCellValue("DX ADICIONAL2");
            celda.setCellStyle(cellStyle);
            
            celda = fila.createCell(23);
            celda.setCellValue("DX ADICIONAL3");
            celda.setCellStyle(cellStyle);
            
            celda = fila.createCell(24);
            celda.setCellValue("EMPRESA                      ");
            celda.setCellStyle(cellStyle);
            
            celda = fila.createCell(25);
            celda.setCellValue("CONTRATO EMPRESA              ");
            celda.setCellStyle(cellStyle);
            
            celda = fila.createCell(26);
            celda.setCellValue("CONTRATO SISB");
            celda.setCellStyle(cellStyle);
            
            celda = fila.createCell(27);
            celda.setCellValue("ESTRATO");
            celda.setCellStyle(cellStyle);
            
            celda = fila.createCell(28);
            celda.setCellValue("MARCA SIS");
            celda.setCellStyle(cellStyle);
            
            celda = fila.createCell(29);
            celda.setCellValue("FECHA FACTURA");
            celda.setCellStyle(cellStyle);
            
            celda = fila.createCell(30);
            celda.setCellValue("ES PYP");
            celda.setCellStyle(cellStyle);
            
            celda = fila.createCell(31);
            celda.setCellValue("CODIGO");
            celda.setCellStyle(cellStyle);
            
            celda = fila.createCell(32);
            celda.setCellValue("CUPS");
            celda.setCellStyle(cellStyle);
            
            celda = fila.createCell(33);
            celda.setCellValue("NOMBRE DEL PROCEDIMIENTO");
            celda.setCellStyle(cellStyle);
            
            celda = fila.createCell(34);
            celda.setCellValue("CENTRO DE COSTO");
            celda.setCellStyle(cellStyle);
            
            celda = fila.createCell(35);
            celda.setCellValue("CANTIDAD");
            celda.setCellStyle(cellStyle);
            
            celda = fila.createCell(36);
            celda.setCellValue("DESCUENTO");
            celda.setCellStyle(cellStyle);
            
            celda = fila.createCell(37);
            celda.setCellValue("VALOR UNITARIO");
            celda.setCellStyle(cellStyle);
            
            celda = fila.createCell(38);
            celda.setCellValue("VALOR EMPRESA");
            celda.setCellStyle(cellStyle);
            
            celda = fila.createCell(39);
            celda.setCellValue("VALOR TOTAL");
            celda.setCellStyle(cellStyle);
            
            celda = fila.createCell(40);
            celda.setCellValue("COPAGO");
            celda.setCellStyle(cellStyle);
            
            celda = fila.createCell(41);
            celda.setCellValue("CUOTA MODERADORA");
            celda.setCellStyle(cellStyle);
            
            celda = fila.createCell(42);
            celda.setCellValue("NOMBRE ACTIVIDAD PYP");
            celda.setCellStyle(cellStyle);
            
            celda = fila.createCell(43);
            celda.setCellValue("FRECUENCIA");
            celda.setCellStyle(cellStyle);
            
            celda = fila.createCell(44);
            celda.setCellValue("CAUSA EXT");
            celda.setCellStyle(cellStyle);
            
            celda = fila.createCell(45);
            celda.setCellValue("FIN PRO");
            celda.setCellStyle(cellStyle);
            
            celda = fila.createCell(46);
            celda.setCellValue("FIN CONSU");
            celda.setCellStyle(cellStyle);
            
            celda = fila.createCell(47);
            celda.setCellValue("DIRECCION                   ");
            celda.setCellStyle(cellStyle);
            
            celda = fila.createCell(48);
            celda.setCellValue("BARRIO                       ");
            celda.setCellStyle(cellStyle);
            
            celda = fila.createCell(49);
            celda.setCellValue("COMUNA");
            celda.setCellStyle(cellStyle);
            
            celda = fila.createCell(50);
            celda.setCellValue("MEDICO");
            celda.setCellStyle(cellStyle);
            
            celda = fila.createCell(51);
            celda.setCellValue("USUARIO");
            celda.setCellStyle(cellStyle);
            
            celda = fila.createCell(52);
            celda.setCellValue("PUESTO DE SALUD (SEDE)");
            celda.setCellStyle(cellStyle);
            
            celda = fila.createCell(53);
            celda.setCellValue("FECHA PROCEDIMIENTO");
            celda.setCellStyle(cellStyle);
            
            celda = fila.createCell(54);
            celda.setCellValue("FECHA SOLICITUD");
            celda.setCellStyle(cellStyle);
            
            celda = fila.createCell(55);
            celda.setCellValue("VICTIMA");
            celda.setCellStyle(cellStyle);
            
            celda = fila.createCell(56);
            celda.setCellValue("LG");
            celda.setCellStyle(cellStyle);
            
            celda = fila.createCell(57);
            celda.setCellValue("DESPLAZADO");
            celda.setCellStyle(cellStyle);
            
            celda = fila.createCell(58);
            celda.setCellValue("MALTRATO");
            celda.setCellStyle(cellStyle);
            
            celda = fila.createCell(59);
            celda.setCellValue("GESTACION");
            celda.setCellStyle(cellStyle);
            
            celda = fila.createCell(60);
            celda.setCellValue("ETNIA");
            celda.setCellStyle(cellStyle);
            
            celda = fila.createCell(61);
            celda.setCellValue("DISCAPACIDAD");
            celda.setCellStyle(cellStyle);
            
            celda = fila.createCell(62);
            celda.setCellValue("EDUCACION                   ");
            celda.setCellStyle(cellStyle);
            
            celda = fila.createCell(63);
            celda.setCellValue("RELIGION                     ");
            celda.setCellStyle(cellStyle);
            
            celda = fila.createCell(64);
            celda.setCellValue("OCUPACION                      ");
            celda.setCellStyle(cellStyle);
            
            celda = fila.createCell(65);
            celda.setCellValue("TIPO");
            celda.setCellStyle(cellStyle);
            
            celda = fila.createCell(66);
            celda.setCellValue("TIPO COPAGO");
            celda.setCellStyle(cellStyle);
            
            celda = fila.createCell(67);
            celda.setCellValue("MES");
            celda.setCellStyle(cellStyle);
            
            celda = fila.createCell(68);
            celda.setCellValue("AÑO");
            celda.setCellStyle(cellStyle);
            
            i ++;
             for (int j = 0; j <= 68; j++) {
                 hoja.autoSizeColumn(j);
             }
             
             while(rs.next()){
                 fila = hoja.createRow(i);
                 celda = fila.createCell(0);
                 celda.setCellValue(rs.getString(2));
                 
                 celda = fila.createCell(1);
                 celda.setCellValue(rs.getString(3));
                 
                 celda = fila.createCell(2);
                 celda.setCellValue(rs.getString(4));
                 
                 celda = fila.createCell(3);
                 celda.setCellValue(rs.getString(5));
                 
                 celda = fila.createCell(4);
                 celda.setCellValue(rs.getString(6));
                 
                 celda = fila.createCell(5);
                 celda.setCellValue(rs.getString(7));
                 
                 celda = fila.createCell(6);
                 celda.setCellValue(rs.getString(8));
                 
                 celda = fila.createCell(7);
                 celda.setCellValue(rs.getString(9));
                 
                 celda = fila.createCell(8);
                 celda.setCellValue(rs.getString(10));
                 
                 celda = fila.createCell(9);
                 celda.setCellValue(rs.getString("FECHA_NACIMIENTO"));
                 
                 celda = fila.createCell(10);
                 celda.setCellValue(rs.getString("EDAD"));
                 
                 celda = fila.createCell(11);
                 celda.setCellValue(rs.getString("MUNICIPIO"));
                 
                 celda = fila.createCell(12);
                 celda.setCellValue(rs.getString("FECHA_REGISTRO"));
                 
                 celda = fila.createCell(13);
                 celda.setCellValue(rs.getString("FECHA_SALIDA"));
                 
                 celda = fila.createCell(14);//Diagnostico
                 celda.setCellValue(rs.getString("diagnostico_principal"));
                 
                 celda = fila.createCell(15);//Diagnostico Egreso
                 celda.setCellValue(rs.getString("diagnostico_relacionado"));
                 
                 celda = fila.createCell(16);//Tipo Atención
                 celda.setCellValue("");
                 
                 celda = fila.createCell(17);//Regimen
                 celda.setCellValue(rs.getString("REGIMEN"));
                 
                 celda = fila.createCell(18);//Tipo Dig
                 celda.setCellValue(rs.getString("tipo_diagnostico"));
                 
                 celda = fila.createCell(19);//Causa Muerte
                 celda.setCellValue("");
                 
                 celda = fila.createCell(20);
                 celda.setCellValue(rs.getString("ESTADO"));
                 
                 celda = fila.createCell(21);//DX 1
                 celda.setCellValue(rs.getString("dx1"));
                 
                 celda = fila.createCell(22);//DX 2
                 celda.setCellValue(rs.getString("dx2"));
                 
                 celda = fila.createCell(23);//DX 3
                 celda.setCellValue("");
                 
                 celda = fila.createCell(24);
                 celda.setCellValue(rs.getString("RAZON_SOCIAL"));
                 
                 celda = fila.createCell(25);
                 celda.setCellValue(rs.getString("CONTRATO"));
                 
                 celda = fila.createCell(26);//Contrato SISB
                 celda.setCellValue(rs.getString("CARNET"));
                 
                 celda = fila.createCell(27);//Estrato
                 celda.setCellValue(rs.getString("ESTRATO"));
                 
                 celda = fila.createCell(28);//Marca SIS
                 celda.setCellValue("");
                 
                 celda = fila.createCell(29);
                 celda.setCellValue(rs.getString("fecha_facturacion"));
                 
                 celda = fila.createCell(30);//ES PYP
                 celda.setCellValue(rs.getString("nom_programa"));
                 
                 celda = fila.createCell(31);//CODIGO
                 celda.setCellValue(rs.getString("CODIGO_SERVICIO"));
                 
                 celda = fila.createCell(32);//CUPS
                 celda.setCellValue(rs.getString("CODIGO_CUP"));
                 
                 celda = fila.createCell(33);//NOMBRE PROCEDIMIENTO
                 celda.setCellValue(rs.getString("NOMBRE_SERVICIO"));
                 
                 celda = fila.createCell(34);//CENTRO DE COSTO
                 celda.setCellValue(rs.getString("CENTRO_COSTO"));
                 
                 celda = fila.createCell(35);//CANTIDAD
                 celda.setCellValue(rs.getDouble("CANTIDAD_SERVICIO"));
                 
                 celda = fila.createCell(36);//DESCUENTO
                 celda.setCellValue(0d);
                 
                 celda = fila.createCell(37);//VALOR UNITARIO
                 celda.setCellValue(rs.getDouble("VALOR_PARCIAL"));
                 
                 celda = fila.createCell(38);//VALOR EMPRESA
                 celda.setCellValue(rs.getDouble("VALOR_EMPRESA"));
                 
                 celda = fila.createCell(39);//VALOR TOTAL
                 celda.setCellValue(rs.getDouble("VALOR_TOTAL"));
                 
                 celda = fila.createCell(40);//COPAGO
                 celda.setCellValue(rs.getDouble("COPAGO"));
                 
                 celda = fila.createCell(41);//CUOTA MODERADORA
                 celda.setCellValue(rs.getDouble("CUOTA_MODERADORA"));
                 
                 celda = fila.createCell(42);//NOMBRE ACTIVIDAD PYP
                 celda.setCellValue(rs.getString("nom_actividad"));
                 
                 celda = fila.createCell(43);//FRECUENCIA
                 celda.setCellValue(rs.getString("FRECUENCIA"));
                 
                 celda = fila.createCell(44);//CAUSA EXT
                 celda.setCellValue("");
                 
                 celda = fila.createCell(45);//FIN PRO
                 celda.setCellValue(rs.getString("motivo_consulta"));
                 
                 celda = fila.createCell(46);//FIN CONSU
                 celda.setCellValue(rs.getString("motivo_consulta"));
                 
                 celda = fila.createCell(47);
                 celda.setCellValue(rs.getString("DIRECCION"));
                 
                 celda = fila.createCell(48);
                 celda.setCellValue(rs.getString("BARRIO"));
                 
                 celda = fila.createCell(49);//COMUNA
                 celda.setCellValue("");
                 
                 celda = fila.createCell(50);
                 celda.setCellValue(rs.getString("MEDICO"));
                 
                 celda = fila.createCell(51);
                 celda.setCellValue(rs.getString("USUARIO"));
                 
                 celda = fila.createCell(52);
                 celda.setCellValue(rs.getString("NOMBRE_SEDE"));
                 
                 celda = fila.createCell(53); //Fecha procedimiento
                 celda.setCellValue(rs.getString("FECHA_SERVICIO"));
                 
                 celda = fila.createCell(54); //Fecha solicitud
                 celda.setCellValue(rs.getString("FECHA_SERVICIO"));
                 
                 celda = fila.createCell(55); 
                 celda.setCellValue(rs.getString("VICTIMA"));
                 
                 celda = fila.createCell(56); 
                 celda.setCellValue(rs.getString("LG"));
                 
                 celda = fila.createCell(57); 
                 celda.setCellValue(rs.getString("DESPLAZADO"));
                 
                 celda = fila.createCell(58); 
                 celda.setCellValue(rs.getString("AMALTRATO"));
                 
                 celda = fila.createCell(59); 
                 celda.setCellValue(rs.getString("GESTACION"));
                 
                 celda = fila.createCell(60); 
                 celda.setCellValue(rs.getString("ETNIA"));
                 
                 celda = fila.createCell(61); 
                 celda.setCellValue(rs.getString("DISCAPACIDAD"));
                 
                 celda = fila.createCell(62); 
                 celda.setCellValue(rs.getString("EDUCACION"));
                 
                 celda = fila.createCell(63); 
                 celda.setCellValue(rs.getString("RELIGION"));
                 
                 celda = fila.createCell(64); 
                 celda.setCellValue(rs.getString("OCUPACION"));
                 
                 celda = fila.createCell(65); //Tipo
                 celda.setCellValue("");
                 
                 celda = fila.createCell(66); //Tipo copago
                 celda.setCellValue("");
                 
                 celda = fila.createCell(67);
                 celda.setCellValue(rs.getString("MES"));
                 
                 celda = fila.createCell(68);
                 celda.setCellValue(rs.getString("ANIO"));
                 i++;
             }
             /*Escribimos en el libro*/
            libro.write(archi);
            /*Cerramos el flujo de datos*/
            archi.close();
            /*DESCARGAMOS EL ARCHIVO */
            File f;
            f = new File(rutaArchivo);
            int bit;
             InputStream in;
             ServletOutputStream out;
            response.setContentType("application/vnd.ms-excel"); //Tipo de fichero.
            response.setHeader("Content-Disposition", "attachment;filename=\"" + nombreArchivo + "\""); //Configurar cabecera http

            in = new FileInputStream(f);
            out = response.getOutputStream();

            bit = 256;
            while ((bit) >= 0) {
                bit = in.read();
                out.write(bit);
            }

            out.flush();
            out.close();
            in.close();
         } catch (Exception e) {
             e.printStackTrace();
         }finally{
             try {
                if (rs != null) {
                    rs.close();
                }
                if (ps != null) {
                    ps.close();
                }
                if (con != null) {
                    con.close();
                }
                DBConnector.getInstance().closeConnection();
            } catch (Exception e) {
            }
         }
    }
    
    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        this.doGet(request, response);
    }
}
