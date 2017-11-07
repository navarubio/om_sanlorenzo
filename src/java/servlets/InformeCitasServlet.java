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
import javax.faces.application.FacesMessage;
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
public class InformeCitasServlet extends HttpServlet {

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        Connection con = null;
         PreparedStatement ps = null;
         ResultSet rs = null;
         String url = getServletContext().getRealPath("/");
         String medico =  request.getParameter("medico");
         String fechaDesde =  request.getParameter("fechaDesde");
         String fechaHasta =  request.getParameter("fechaHasta");
         String administradora =  request.getParameter("administradora");
         String motivo =  request.getParameter("motivo");
         String oportunidad =  request.getParameter("oportunidad");
         String estado =  request.getParameter("estado");
         String estadoPaciente =  request.getParameter("estadoPaciente");
         String nombreArchivo = "CONSULTA_DE_CITAS.xls";
         try {
             con = DBConnector.getInstance().getConnection();

             String query = "select cita.fecha_Registro,turnos.fecha_creacion,case when cita.cancelada=true then '' else concat(turnos.fecha,' ',hora_ini) end fecha_atencion,case when cita.cancelada=true then '' else concat(turnos.fecha,' ',hora_fin) end fecha_finalizacion, "
                     + "concat(medico.primer_nombre,' ',medico.segundo_nombre,' ',medico.primer_apellido,' ',medico.segundo_apellido) as medico, "
                     + "especialidad.descripcion as especialidad,sede.nombre_sede,consultorio.nom_consultorio,administradora.razon_social, "
                     + "(select descripcion from fac_contrato c where c.id_Contrato = max(contrato.id_contrato)) as contrato, "
                     + "concat(paciente.primer_nombre,' ',paciente.segundo_nombre,' ',paciente.primer_apellido,' ',paciente.segundo_apellido) as paciente, "
                     + "factura.valor_total, "
                     + "case when (cita.atendida=false and cita.cancelada=false and cita.facturada=false) then 'ASIGNADA' "
                     + "when (cita.facturada=true) then 'FACTURADA' "
                     + "when cita.atendida=true and cita.facturada=false  then 'ATENDIDA' "
                     + "when cita.cancelada=true then 'CANCELADA' else '' end estado,cancelacion.descripcion observacion "
                     + "from cit_citas cita "
                     + "inner join cit_turnos turnos on cita.id_turno = turnos.id_turno "
                     + "inner join fac_administradora administradora on  administradora.id_administradora = cita.id_Administradora "
                     + "inner join cfg_usuarios medico on medico.id_usuario = cita.id_prestador "
                     + "inner join cfg_clasificaciones especialidad on especialidad.id = medico.especialidad and especialidad.maestro='Especialidad' "
                     + "inner join cfg_consultorios consultorio on consultorio.id_consultorio = turnos.id_consultorio "
                     + "inner join cfg_sede sede on sede.id_sede = consultorio.id_Sede "
                     + "inner join fac_contrato contrato on contrato.id_administradora = administradora.id_administradora "
                     + "inner join cfg_pacientes paciente on paciente.id_paciente = cita.id_paciente "
                     + "left join fac_factura_paciente factura on factura.id_cita = cita.id_cita "
                     + "inner join cfg_clasificaciones consulta on consulta.id =cita.tipo_cita "
                     + "left join cfg_clasificaciones cancelacion on cancelacion.id = cita.motivo_cancelacion "
                     + "where cita.id_Cita>0 ";
             //validamos filtro
            String filtro="";
            if (medico != null) {
                if (!medico.equals("null")) {
                    filtro += " AND medico.id_Usuario =" + medico;
                }
            }
            if (fechaDesde != null && fechaHasta != null) {
                if(!fechaDesde.equals("") && !fechaHasta.equals("")){
                    filtro +=" and  to_char(cita.fecha_registro, 'yyyy-MM-dd')  between '"+fechaDesde+"' and "+"'"+fechaHasta+"' ";
                }
            }
            if(administradora!=null){
                if(!administradora.equals("null")){
                    filtro += " AND administradora.id_Administradora = "+administradora;
                }
            }
            if(motivo!=null){
                if(!motivo.equals("null")){
                    filtro += " AND consulta.id = "+motivo;
                }
            }
            if(oportunidad!=null){
                if(!oportunidad.equals("null")){
                    filtro +=" AND turnos.estado='"+oportunidad+"'";
                }
            }
            if(estado!=null){
                if(!estado.equals("")){
                    switch(estado){
                        case "1"://Asignada
                            filtro  += " AND cita.atendida=false and cita.cancelada=false and cita.facturada=false";
                            break;
                        case "2"://Atendida
                            filtro  += " AND cita.atendida=true and cita.facturada=false";
                            break;        
                        case "3"://Cancelada
                            filtro  += " AND cita.cancelada=true";
                            break;    
                        case "4"://Facturada
                            filtro  += " AND cita.facturada=true";
                            break;    
                    }
                }
            }
            
            /*if(estadoPaciente!=null){
                if(!estadoPaciente.equals("")){
                    if(estadoPaciente.equals("1")){
                        filtro +=" AND paciente.activo=true";
                    }else{
                        filtro +=" AND paciente.activo=false";
                    }
                }
            }*/
            
            query +=filtro+" "
                    + " group by cita.id_cita, cita.fecha_Registro,turnos.fecha_creacion,turnos.fecha,hora_ini,turnos.fecha,hora_fin, medico.primer_nombre,medico.segundo_nombre,medico.primer_apellido,medico.segundo_apellido,\n" +       "especialidad.descripcion ,sede.nombre_sede,consultorio.nom_consultorio,administradora.razon_social, " +
                      " paciente.primer_nombre,paciente.segundo_nombre,paciente.primer_apellido,paciente.segundo_apellido, " +
                        "factura.valor_total,cancelacion.descripcion  ";
            ps  =con.prepareCall(query);
            rs = ps.executeQuery();
            
            
            String rutaArchivo = url + "/citas/reportes/";
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
            
            hoja = libro.createSheet("CONSULTA DE CITAS");
            fila = hoja.createRow(i);
            
            celda = fila.createCell(0);
            celda.setCellValue("FECHA SOLICITUD");
            celda.setCellStyle(cellStyle);
            
            celda = fila.createCell(1);
            celda.setCellValue("FECHA ATENCION");
            celda.setCellStyle(cellStyle);
            
            celda = fila.createCell(2);
            celda.setCellValue("FECHA REAL ATENCION    ");
            celda.setCellStyle(cellStyle);
            
            celda = fila.createCell(3);
            celda.setCellValue("FECHA_FINALIZACION     ");
            celda.setCellStyle(cellStyle);

            celda = fila.createCell(4);
            celda.setCellValue("MEDICO                                           ");
            celda.setCellStyle(cellStyle);
            
            celda = fila.createCell(5);
            celda.setCellValue("ESPECIALIDAD                                        ");
            celda.setCellStyle(cellStyle);
            
            celda = fila.createCell(6);
            celda.setCellValue("SEDE                                  ");
            celda.setCellStyle(cellStyle);
            
            
            celda = fila.createCell(7);
            celda.setCellValue("CONSULTORIO                       ");
            celda.setCellStyle(cellStyle);
            
            celda = fila.createCell(8);
            celda.setCellValue("ADMINISTRADORA                                                         ");
            celda.setCellStyle(cellStyle);
            
            celda = fila.createCell(9);
            celda.setCellValue("CONTRATO                                                                  ");
            celda.setCellStyle(cellStyle);
            
            celda = fila.createCell(10);
            celda.setCellValue("PACIENTE                                                                 ");
            celda.setCellStyle(cellStyle);
            
            celda = fila.createCell(11);
            celda.setCellValue("VALOR SERVICIO                       ");
            celda.setCellStyle(cellStyle);
            
            celda = fila.createCell(12);
            celda.setCellValue("USUARIO SISTEMA                       ");
            celda.setCellStyle(cellStyle);
            
            celda = fila.createCell(13);
            celda.setCellValue("ESTADO CITA                       ");
            celda.setCellStyle(cellStyle);
            
            celda = fila.createCell(14);
            celda.setCellValue("OBSERVACIONES                       ");
            celda.setCellStyle(cellStyle);
            				
            i ++;
             for (int j = 0; j <= 14; j++) {
                 hoja.autoSizeColumn(j);
             }
            
            while(rs.next()){
                fila = hoja.createRow(i);
                celda = fila.createCell(0);
                celda.setCellValue(rs.getString(1));
                
                celda = fila.createCell(1);
                 celda.setCellValue(rs.getString(2));
                 
                 celda = fila.createCell(2);
                 celda.setCellValue(rs.getString(3));
                 
                 celda = fila.createCell(3);
                 celda.setCellValue(rs.getString(4));
                 
                 celda = fila.createCell(4);
                 celda.setCellValue(rs.getString(5));
                 
                 celda = fila.createCell(5);
                 celda.setCellValue(rs.getString(6));
                 
                 celda = fila.createCell(6);
                 celda.setCellValue(rs.getString(7));
                 
                 celda = fila.createCell(7);
                 celda.setCellValue(rs.getString(8));
                 
                 celda = fila.createCell(8);
                 celda.setCellValue(rs.getString(9));
                 
                 celda = fila.createCell(9);
                 celda.setCellValue(rs.getString(10));
                 
                 celda = fila.createCell(10);
                 celda.setCellValue(rs.getString(11));
                 
                 celda = fila.createCell(11);
                 celda.setCellValue(rs.getString(12));
                 
                 celda = fila.createCell(12);
                 celda.setCellValue("");
                 
                 celda = fila.createCell(13);
                 celda.setCellValue(rs.getString(13));
                 
                 celda = fila.createCell(14);
                 celda.setCellValue(rs.getString(14));
                 
                 
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
         }catch(Exception ex){
             
         }
        }
    }
    
    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        this.doGet(request, response);
    }
}
