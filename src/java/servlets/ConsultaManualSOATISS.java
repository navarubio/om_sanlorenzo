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
public class ConsultaManualSOATISS extends HttpServlet {

     @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
         Connection con = null;
         PreparedStatement ps = null;
         ResultSet rs = null;
         String url = getServletContext().getRealPath("/");
         int idManualTarifario = Integer.parseInt(request.getParameter("manual"));

         String tipo =request.getParameter("tipo");
         String administradora=request.getParameter("administradora");
         String tipoContrato=request.getParameter("contrato");
         String annio=request.getParameter("annio");
         String nombreArchivo = administradora+" MANUAL TARIFARIO "+tipo+" "+annio+ " "+tipoContrato+".xls";
         try {
             con = DBConnector.getInstance().getConnection();
             String query  ="select fs.codigo_cup,fs.nombre_servicio,fs.factor_iss,fu.smlvd,fu.uvr, " +
                            "	case when fc.tipo_manual=1 then ft.valor_final else round((fu.smlvd*(case when fc.tipo_manual=2 then fs.factor_iss else fs.factor_soat end ))+(case when fc.signo_porcentaje ='+' then  1  else -1 end * fc.porcentaje))  end  " +
                            " as servicio, fa.razon_social,fc.tipo_manual,fc.annio_manual,fc.descripcion as nombre_contrato,grupo.descripcion as grupo " +
                            "from  fac_contrato fc " +
                            "inner join fac_manual_tarifario fm on fc.id_manual_tarifario = fm.id_manual_tarifario " +
                            "inner join fac_manual_tarifario_servicio ft on ft.id_manual_tarifario = fm.id_manual_tarifario " +
                            "inner join fac_servicio fs on fs.id_servicio = ft.id_Servicio " +
                            "left join fac_unidad_valor fu on fu.anio = fc.annio_manual " +
                            "inner join fac_administradora fa on fa.id_administradora = fc.id_administradora "+
                            "left join cfg_clasificaciones grupo on grupo.maestro='GrupoServicio' and grupo.id = fs.grupo_servicio " +
                            "where fc.id_manual_tarifario=? ";
             ps = con.prepareCall(query);
             ps.clearParameters();
             ps.setInt(1, idManualTarifario);
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
            hoja = libro.createSheet(administradora+" MANUAL TARIFARIO "+tipo+" "+annio+ " "+tipoContrato);
            fila = hoja.createRow(i);
            celda = fila.createCell(0);
            celda.setCellValue("CODIGO");
            celda.setCellStyle(cellStyle);
            
            celda = fila.createCell(1);
            celda.setCellValue("SERVICIO                                                   ");
            celda.setCellStyle(cellStyle);
            
            celda = fila.createCell(2);
            celda.setCellValue(tipo.equals("ISS")?"UVR":"GRUPO");
            celda.setCellStyle(cellStyle);
            
            celda = fila.createCell(3);
            celda.setCellValue("SMLDV");
            celda.setCellStyle(cellStyle);
            
            celda = fila.createCell(4);
            celda.setCellValue("VLR. SERVICIO");
            celda.setCellStyle(cellStyle);

             i ++;
             for (int j = 0; j <= 5; j++) {
                 hoja.autoSizeColumn(j);
             }

             while(rs.next()){
                 fila = hoja.createRow(i);
                 celda = fila.createCell(0);
                 celda.setCellValue(rs.getString(1));
                 
                 celda = fila.createCell(1);
                 celda.setCellValue(rs.getString(2));
                 
                 celda = fila.createCell(2);
                 String value = rs.getString(11);
                 double valueiss= rs.getDouble(3);
                 if(tipo.equals("ISS")){
                    celda.setCellValue(valueiss);
                 }else{
                    celda.setCellValue(value);
                 }
                 
                 
                 celda = fila.createCell(3);
                 celda.setCellValue(rs.getDouble(4));
                 
                 celda = fila.createCell(4);
                 celda.setCellValue(rs.getDouble(6));
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
