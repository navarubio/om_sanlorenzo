/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managedBeans.informe4505;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletResponse;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporter;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.util.JRLoader;

/**
 *
 * @author sismacontab
 */
public class ReporteAnexos {
        public void getReporte(String ruta) throws ClassNotFoundException, InstantiationException, IllegalAccessException, SQLException {
        if (ruta != null) {

            Connection conexion;
            Class.forName("com.postgresql.jdbc.Driver").newInstance();
            conexion = DriverManager.getConnection("jdbc:postgresql://localhost:/Produccion", "postgres", "091095");

            //Se definen los parametros si es que el reporte necesita
            Map parameter = new HashMap();

            try {
                File file = new File(ruta);

                HttpServletResponse httpServletResponse = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();

                httpServletResponse.setContentType("application/pdf");
                httpServletResponse.addHeader("Content-Type", "application/pdf");

                JasperReport jasperReport = (JasperReport) JRLoader.loadObjectFromFile(file.getPath());

                JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameter, conexion);

                JRExporter jrExporter = null;
                jrExporter = new JRPdfExporter();
                jrExporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
                jrExporter.setParameter(JRExporterParameter.OUTPUT_STREAM, httpServletResponse.getOutputStream());

                if (jrExporter != null) {
                    try {
                        jrExporter.exportReport();
                    } catch (JRException e) {
                        e.printStackTrace();
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (conexion != null) {
                    try {
                        conexion.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
    public void getAnexo1(String admin, String codadmin, String mcpiopaciente, String dptopaciente, String dptoempresa, String mcpioempresa, String numinform,String ruta ) throws ClassNotFoundException, InstantiationException, IllegalAccessException, SQLException {
        if (ruta != null) {

            Connection conexion;
            Class.forName("com.postgresql.jdbc.Driver").newInstance();
            conexion = DriverManager.getConnection("jdbc:postgresql://localhost:5433/Produccion", "postgres", "091095");

            //Se definen los parametros si es que el reporte necesita
            Map parameter = new HashMap();
            parameter.put("admin", admin);
            parameter.put("codadmin", codadmin);
            parameter.put("mcpiopaciente", mcpiopaciente);
            parameter.put("dptopaciente", dptopaciente);
            parameter.put("dptoempresa", dptoempresa);
            parameter.put("mcpioempresa", mcpioempresa);
            parameter.put("numinform", numinform);
            
            try {
                File file = new File(ruta);

                HttpServletResponse httpServletResponse = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();

                httpServletResponse.setContentType("application/pdf");
                httpServletResponse.addHeader("Content-Type", "application/pdf");

                JasperReport jasperReport = (JasperReport) JRLoader.loadObjectFromFile(file.getPath());

                JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameter, conexion);

                JRExporter jrExporter = null;
                jrExporter = new JRPdfExporter();
                jrExporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
                jrExporter.setParameter(JRExporterParameter.OUTPUT_STREAM, httpServletResponse.getOutputStream());

                if (jrExporter != null) {
                    try {
                        jrExporter.exportReport();
                    } catch (JRException e) {
                        e.printStackTrace();
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (conexion != null) {
                    try {
                        conexion.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
    
    
}
