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
import java.util.Date;
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
public class ConsultaContratos extends HttpServlet {

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Connection con = null;
         PreparedStatement ps = null;
         ResultSet rs = null;
         String url = getServletContext().getRealPath("/");
         String tipoFiltro= request.getParameter("filtro");
         String filtro1 =  request.getParameter("param1");
         String filtro="";
         String nombreArchivo = "CONTRATOS.xls";
         try {
            //configuramos filtro
           switch(tipoFiltro){
            case "1":
                if(!filtro1.equals("0")){
                    filtro= " WHERE c.id_administradora ="+filtro1;
                }
                break;
            case "2":
                SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd");
                if(filtro1.equals("1"))
                  filtro= " WHERE to_char(c.fecha_final, 'yyyy-MM-dd') >='"+formato.format(new Date())+"'";
                else 
                    filtro= " WHERE to_char(c.fecha_final, 'yyyy-MM-dd') <='"+formato.format(new Date())+"'";
                break;
            case "3":
                filtro= " WHERE  c.tipo_pago="+filtro1;
                break;
           }
           
           con = DBConnector.getInstance().getConnection();
           String query  =  "select c.codigo_contrato,c.descripcion,a.razon_social, "+
                            "case when tipo_manual = 1 then 'ESPECIFICO' else "+
                            "case when tipo_manual = 2 then concat('ISS ',signo_porcentaje,porcentaje,'%') else "+
                            "case when tipo_manual = 3 then concat('SOAT ',signo_porcentaje,porcentaje,'%') else '' end end end tipo_manual, "+
                            "p.descripcion as modalidad "+
                            "from fac_contrato c "+
                            "inner join fac_administradora a on a.id_administradora = c.id_administradora "+
                            "inner join cfg_clasificaciones p on p.id = c.tipo_pago "+filtro;
           ps = con.prepareCall(query);
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
            
            hoja = libro.createSheet("CONTRATOS");
            fila = hoja.createRow(i);
            celda = fila.createCell(0);
            celda.setCellValue("CODIGO      ");
            celda.setCellStyle(cellStyle);
            
            celda = fila.createCell(1);
            celda.setCellValue("NOMBRE CONTRATO                             ");
            celda.setCellStyle(cellStyle);
            
            celda = fila.createCell(2);
            celda.setCellValue("EMPRESA                                       ");
            celda.setCellStyle(cellStyle);
            
            celda = fila.createCell(3);
            celda.setCellValue("TARIFA                                        ");
            celda.setCellStyle(cellStyle);
            
            celda = fila.createCell(4);
            celda.setCellValue("MODALIDAD                       ");
            celda.setCellStyle(cellStyle);
            
            i ++;
             for (int j = 0; j <= 4; j++) {
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
         }catch (Exception e) {
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
