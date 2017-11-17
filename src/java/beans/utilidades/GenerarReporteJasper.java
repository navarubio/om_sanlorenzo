/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beans.utilidades;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Connection;
import java.util.HashMap;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.export.JExcelApiExporterParameter;
import net.sf.jasperreports.engine.export.JRXlsExporter;

/**
 *
 * @author casc
 */
public class GenerarReporteJasper {

    public static final int REPORT_TYPE_PDF = 0;
    public static final int REPORT_TYPE_XLS = 1;

    public void generar(String destFile, String _rutaJasper, String pathf,
            int exportType, Connection conn, HashMap _params)
            throws Exception {

        JasperPrint jasperPrint;
        try {
            jasperPrint = JasperFillManager.fillReport(_rutaJasper, _params, conn);

            if (exportType == REPORT_TYPE_PDF) {
                JasperExportManager.exportReportToPdfFile(jasperPrint, pathf
                        + destFile + ".pdf");
            } else if (exportType == REPORT_TYPE_XLS) {
                OutputStream outputStream = new FileOutputStream(new File(pathf
                        + destFile + ".xls"));
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

                JRXlsExporter exporter = new JRXlsExporter();

                exporter.setParameter(JExcelApiExporterParameter.IS_DETECT_CELL_TYPE, Boolean.TRUE);

                exporter.setParameter(JRExporterParameter.JASPER_PRINT,
                        jasperPrint);
                exporter.setParameter(JRExporterParameter.OUTPUT_STREAM,
                        byteArrayOutputStream);

                exporter.exportReport();
                outputStream.write(byteArrayOutputStream.toByteArray());
                outputStream.flush();
                outputStream.close();

            }
        } catch (IOException | JRException ex) {
            System.out.println("Error generando reporte " + ex.getMessage());
            throw new Exception(ex.toString());

        }
    }
}
