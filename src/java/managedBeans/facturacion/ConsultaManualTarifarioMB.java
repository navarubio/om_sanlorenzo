/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managedBeans.facturacion;

import beans.utilidades.ManualTarifarioSOATISS;
import beans.utilidades.MetodosGenerales;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Connection;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import javax.servlet.ServletContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import modelo.entidades.FacContrato;
import modelo.entidades.FacManualTarifario;
import modelo.entidades.FacServicio;
import modelo.fachadas.DBConnector;
import modelo.fachadas.FacAdministradoraFacade;
import modelo.fachadas.FacContratoFacade;
import modelo.fachadas.FacManualTarifarioFacade;
import modelo.fachadas.FacUnidadValorFacade;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;

/**
 *
 * @author miguel
 */
@Named(value = "consultaManualTarifarioMB")
@ViewScoped
public class ConsultaManualTarifarioMB extends MetodosGenerales implements java.io.Serializable{

    @EJB
    private FacContratoFacade contradoFacade;
    @EJB
    private FacUnidadValorFacade unidadvalorFacade;
    @EJB
    private FacManualTarifarioFacade manualTarifarioFacade;
    @EJB
    private FacAdministradoraFacade administradoraFacade;
    
    private List<FacContrato> listaContratoAdministradora;
    private List<ManualTarifarioSOATISS> listaResultado;
    
    private String administradoraId;
    private String administradora;
    private FacContrato contrato;
    private FacContrato contratoFilter;
    private FacManualTarifario manualTarifario;
    private String tipoTarifa;
    private String annio;
    private double SMLVD = 0.0;
    private boolean renderLista;
    private String column1;
    private String column2;
    private String column3;
    private String column4;
    private String column5;
    private String idContrato;
    private final DecimalFormat formateadorDecimal = new DecimalFormat("0.00");
    public ConsultaManualTarifarioMB() {
        listaContratoAdministradora = new ArrayList();
        listaResultado = new ArrayList();
        contrato = new FacContrato();
        contratoFilter = new FacContrato();
        contrato.setIdContrato(0);
        column1  ="CODIGO";
        column2  ="SERVICIO";
        column3  ="";
        column4  ="SMLDV";
        column5  ="VLR. SERVICIO";
        idContrato = "0";
    }
    
    @PostConstruct
    public void init(){
        
    }
    
    public String valor(FacServicio servicio){
        if (contrato.getTipoManual() == 3) {//SOAT
            double valor = servicio.getFactorSoat()*SMLVD;
            BigDecimal x = new BigDecimal(valor);
            valor = x.setScale(-2, RoundingMode.HALF_DOWN).intValue();
            return ""+valor;
        }else if (contrato.getTipoManual() == 2) {//ISS
            double valor = servicio.getFactorIss()*SMLVD;
            BigDecimal x = new BigDecimal(valor);
            valor = x.setScale(-2, RoundingMode.HALF_DOWN).intValue();
            return ""+valor;
        }
        return "0.0";
    }
    
    public double factor(FacServicio servicio){
        if (contrato.getTipoManual() == 3) {//SOAT
            return servicio.getFactorSoat();
        }else if (contrato.getTipoManual() == 2) {//ISS
            return servicio.getFactorIss();
        }
        return 0.0;
    }
    
    public String valor_porcentaje(FacServicio servicio){
        if(contrato.getPorcentaje()>0){
            if (contrato.getTipoManual() == 3) {//SOAT
                double valor = servicio.getFactorSoat()*SMLVD;
                BigDecimal x = new BigDecimal(valor);
                valor = x.setScale(-2, RoundingMode.HALF_DOWN).intValue();
                return ""+(contrato.getPorcentaje()*(valor)/100);
            }else if (contrato.getTipoManual() == 2) {//ISS
                return formateadorDecimal.format(contrato.getPorcentaje()*(servicio.getFactorIss()*SMLVD)/100);
            }
        }
        return "0.0";
    }
    
    public String valor_final(FacServicio servicio){
        if(contrato.getPorcentaje()>0){
            double valor = 0.0;
            if (contrato.getTipoManual() == 3) {//SOAT 
                valor = servicio.getFactorSoat()*SMLVD;
                BigDecimal x = new BigDecimal(valor);
                valor = x.setScale(-2, RoundingMode.HALF_DOWN).intValue();
            }else if (contrato.getTipoManual() == 2) {//ISS
                valor = servicio.getFactorIss()*SMLVD;
                BigDecimal x = new BigDecimal(valor);
                valor = x.setScale(-2, RoundingMode.HALF_DOWN).intValue();
            }
            double porcentaje = 0.0;
            if(contrato.getSignoPorcentaje().equals("-")){
                if(contrato.getPorcentaje().intValue() < 99){
                    System.out.println("0."+contrato.getPorcentaje().intValue());
                    porcentaje = Double.parseDouble("0."+contrato.getPorcentaje().intValue());
                }else{
                    porcentaje = Double.parseDouble(contrato.getPorcentaje().toString().substring(0,1)+"."+(contrato.getPorcentaje().intValue()+"").substring(1));
                }
                valor = valor-valor*porcentaje; 
            }
            if(contrato.getSignoPorcentaje().equals("+")){
                if(contrato.getPorcentaje().intValue() < 99){
                    System.out.println("0."+contrato.getPorcentaje().intValue());
                    porcentaje = Double.parseDouble("0."+contrato.getPorcentaje().intValue());
                }else{
                    porcentaje = Double.parseDouble(contrato.getPorcentaje().toString().substring(0,1)+"."+(contrato.getPorcentaje().intValue()+"").substring(1));
                }
                valor = valor+valor*porcentaje; 
            }
            return valor+"";
        }
        return "0.0";
    }

    public void validarAdministradora(ValueChangeEvent evt){
    }
    public void validarAdministrador(){
        if(!administradoraId.equals("")){
            administradora = administradoraFacade.find(Integer.parseInt(administradoraId)).getRazonSocial();
            listaContratoAdministradora = contradoFacade.buscarPorAdministrador(Integer.parseInt(administradoraId));
        }else
            listaContratoAdministradora = new ArrayList();
    }
    public void buscar(){
        
        if(!idContrato.equals("0")){
            contrato = contradoFacade.find(Integer.parseInt(idContrato));
            contratoFilter = contradoFacade.find(Integer.parseInt(idContrato));
            if(contrato!=null){
                if (contrato.getIdManualTarifario() != null) {
                    manualTarifario = manualTarifarioFacade.find(contrato.getIdManualTarifario().getIdManualTarifario());
                    if (contrato.getTipoManual() != null) {
                        if (contrato.getTipoManual() == 2) {//ISS
                            tipoTarifa = "ISS";
                            renderLista = true;
                            annio = String.valueOf(contrato.getAnnioManual());
                            SMLVD = unidadvalorFacade.find(contrato.getAnnioManual()).getSmlvd();
                        } else if (contrato.getTipoManual() == 3) {//SOAT
                            tipoTarifa = "SOAT";
                            renderLista = true;
                            annio = String.valueOf(contrato.getAnnioManual());
                            SMLVD = unidadvalorFacade.find(contrato.getAnnioManual()).getSmlvd();
                        }else if(contrato.getTipoManual()==1){//Especifico
                            tipoTarifa = "ESPECIFICA";
                            renderLista = true;
                            SMLVD = 0.0;
                        }
                    }else{
                        imprimirMensaje("No hay registros", "El contrato no tiene una tarifa configurada", FacesMessage.SEVERITY_INFO);
                        listaResultado.clear();
                        renderLista = false;
                    }
                }else{
                    imprimirMensaje("No hay registros", "No posee manual tarifario configurado", FacesMessage.SEVERITY_INFO);
                    listaResultado.clear();
                    renderLista = false;
                }
            }else{
                imprimirMensaje("No hay registros", "Seleccione un contrato", FacesMessage.SEVERITY_INFO);
                listaResultado.clear();
                renderLista = false;
            }
                    
        }else{
                imprimirMensaje("No hay registros", "Seleccione un contrato", FacesMessage.SEVERITY_INFO);
            }
    }
    
    public void imprimir() {
        if (manualTarifario != null) {

            FacesContext facesContext = FacesContext.getCurrentInstance();
            HttpServletResponse httpServletResponse = (HttpServletResponse) facesContext.getExternalContext().getResponse();
            try (ServletOutputStream servletOutputStream = httpServletResponse.getOutputStream()) {
                httpServletResponse.setContentType("application/pdf");
                ServletContext servletContext = (ServletContext) facesContext.getExternalContext().getContext();
                String ruta = servletContext.getRealPath("/facturacion/reportes/r_consulta_manual_SOAT_ISS.jasper");
                Map<String, Object> parametros = new HashMap<>();
                parametros.put("P_ID_MANUAL", manualTarifario.getIdManualTarifario());
                
                try {
                    try (Connection con = DBConnector.getInstance().getConnection()) {
                        JasperPrint jasperPrint = JasperFillManager.fillReport(ruta, parametros, con);
                        JasperExportManager.exportReportToPdfStream(jasperPrint, servletOutputStream);
                        FacesContext.getCurrentInstance().responseComplete();
                    }
                    DBConnector.getInstance().closeConnection();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }

        }
    }
    
    public void imprimirCSV(){
        FacesContext context = FacesContext.getCurrentInstance();
        try{
            FacesContext.getCurrentInstance().responseComplete();
            String baseURL = context.getExternalContext().getRequestContextPath();
            String url =  baseURL +"/ConsultaManualSOATISS?manual="+manualTarifario.getIdManualTarifario()+"&url="+baseURL+"&tipo="+tipoTarifa+"&administradora="+administradora+"&contrato="+contrato.getDescripcion()+"&annio="+annio;
            String encodeURL = context.getExternalContext().encodeResourceURL(url);
        
            context.getExternalContext().redirect(encodeURL);
        }  catch(Exception e)    {
            e.printStackTrace();
        }
    }
    public List<FacContrato> getListaContratoAdministradora() {
        return listaContratoAdministradora;
    }

    public void setListaContratoAdministradora(List<FacContrato> listaContratoAdministradora) {
        this.listaContratoAdministradora = listaContratoAdministradora;
    }

    public String getAdministradoraId() {
        return administradoraId;
    }

    public void setAdministradoraId(String administradoraId) {
        this.administradoraId = administradoraId;
    }

    public FacContrato getContrato() {
        return contrato;
    }

    public void setContrato(FacContrato contrato) {
        this.contrato = contrato;
    }

    public String getTipoTarifa() {
        return tipoTarifa;
    }

    public void setTipoTarifa(String tipoTarifa) {
        this.tipoTarifa = tipoTarifa;
    }

    public FacManualTarifario getManualTarifario() {
        return manualTarifario;
    }

    public void setManualTarifario(FacManualTarifario manualTarifario) {
        this.manualTarifario = manualTarifario;
    }

    public boolean isRenderLista() {
        return renderLista;
    }

    public void setRenderLista(boolean renderLista) {
        this.renderLista = renderLista;
    }

    public List<ManualTarifarioSOATISS> getListaResultado() {
        return listaResultado;
    }

    public void setListaResultado(List<ManualTarifarioSOATISS> listaResultado) {
        this.listaResultado = listaResultado;
    }

    public String getColumn1() {
        return column1;
    }

    public void setColumn1(String column1) {
        this.column1 = column1;
    }

    public String getColumn2() {
        return column2;
    }

    public void setColumn2(String column2) {
        this.column2 = column2;
    }

    public String getColumn3() {
        return column3;
    }

    public void setColumn3(String column3) {
        this.column3 = column3;
    }

    public String getColumn4() {
        return column4;
    }

    public void setColumn4(String column4) {
        this.column4 = column4;
    }

    public String getColumn5() {
        return column5;
    }

    public void setColumn5(String column5) {
        this.column5 = column5;
    }

    public String getIdContrato() {
        return idContrato;
    }

    public void setIdContrato(String idContrato) {
        this.idContrato = idContrato;
    }

    public String getAnnio() {
        return annio;
    }

    public void setAnnio(String annio) {
        this.annio = annio;
    }

    public double getSMLVD() {
        return SMLVD;
    }

    public void setSMLVD(double SMLVD) {
        this.SMLVD = SMLVD;
    }

    public FacContrato getContratoFilter() {
        return contratoFilter;
    }

    public void setContratoFilter(FacContrato contratoFilter) {
        this.contratoFilter = contratoFilter;
    }

    
    
}
