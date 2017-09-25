/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managedBeans.facturacion;

import beans.enumeradores.ClasificacionesEnum;
import beans.utilidades.MetodosGenerales;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import modelo.entidades.CfgClasificaciones;
import modelo.entidades.FacAdministradora;
import modelo.entidades.FacContrato;
import modelo.fachadas.CfgClasificacionesFacade;
import modelo.fachadas.FacAdministradoraFacade;
import modelo.fachadas.FacContratoFacade;

/**
 *
 * @author miguel
 */
@Named(value = "informeContratosMB")
@ViewScoped
public class InformeContratosMB extends MetodosGenerales implements java.io.Serializable{

    @EJB
    private FacAdministradoraFacade administradoraFacade;
    @EJB
    private CfgClasificacionesFacade clasificacionFacade;
    @EJB
    private FacContratoFacade contratoFacade;
    
    private FacAdministradora administradora;
    
    private List<SelectItem> listaFiltro;
    private List<FacAdministradora> listaAdministradora;
    private List<CfgClasificaciones> listaTipoPago;
    private List<FacContrato> listaConsulta;
    
    private String filtroReporte;
    private String estadoContrato;
    private String modalidad;
    private boolean renderFiltro1;
    private boolean renderFiltro2;
    private boolean renderFiltro3;
    private boolean renderFiltro4;
    private boolean renderFiltro5;
    private boolean renderFiltro6;
    private boolean renderBuscar;
    private boolean renderResultado;
    
    public InformeContratosMB() {
        listaAdministradora = new ArrayList();
        listaConsulta = new ArrayList();
        listaFiltro = new ArrayList<>();
        listaFiltro.add(new SelectItem("1", "Administradora"));
        listaFiltro.add(new SelectItem("2", "Vigencia"));
        listaFiltro.add(new SelectItem("3", "Modalidad"));
        administradora = new FacAdministradora();
        administradora.setIdAdministradora(0);
        estadoContrato = "1";
        //listaFiltro.add(new SelectItem("4", "Tarifa"));
    }
    @PostConstruct
    public void init(){
        listaAdministradora = administradoraFacade.findAll();
        listaTipoPago = clasificacionFacade.buscarPorMaestro(ClasificacionesEnum.TipoPago.toString());
    }
    private void limpiar(){
        renderFiltro1 = false;
        renderFiltro2 = false;
        renderFiltro3 = false;
        renderBuscar = false;
        renderResultado = false;
        administradora = new FacAdministradora();
        administradora.setIdAdministradora(0);
    }
    
    public void consultar(){
        String filtro="";
        Date date = null;
        switch(filtroReporte){
            case "1":
                if(!administradora.getIdAdministradora().equals(0))filtro="WHERE f.idAdministradora.idAdministradora="+administradora.getIdAdministradora();
                break;
            case "2":
                date = Calendar.getInstance().getTime();
                
                if(estadoContrato.equals("1"))
                    filtro = "WHERE f.fechaFinal>= :param1";
                else
                    filtro = "WHERE f.fechaFinal<= :param1";
                break;
            case "3":
                if(!modalidad.equals("0"))filtro = "WHERE f.tipoPago.id="+modalidad;
                break;
        }
        listaConsulta = contratoFacade.contratoFiltros(filtro,date);
        if(listaConsulta.size()>0){
            renderResultado = true;
        }else{
            imprimirMensaje("No hay registros", "No se encontró registros", FacesMessage.SEVERITY_INFO);
            renderResultado = false;
        }
    }
    public void exportarCSV(){
        FacesContext context = FacesContext.getCurrentInstance();
        String parametro1="";
        try{
            switch(filtroReporte){
                case "1":
                parametro1 = administradora.getIdAdministradora().toString();
                break;
            case "2":
                parametro1 = estadoContrato;
                break;
            case "3":
                parametro1 = modalidad;
                break;
            }
            FacesContext.getCurrentInstance().responseComplete();
            String baseURL = context.getExternalContext().getRequestContextPath();
            String url =  baseURL +"/ConsultaContratos?filtro="+filtroReporte+"&param1="+parametro1;
            String encodeURL = context.getExternalContext().encodeResourceURL(url);
        context.getExternalContext().redirect(encodeURL);
        }  catch(Exception e)    {
            e.printStackTrace();
        }
    }
    public void validarFiltro(){
        limpiar();
        switch(filtroReporte){
            case "1":
                renderFiltro1 = true;
                renderBuscar = true;
                break;
            case "2":
                renderFiltro2 = true;
                renderBuscar = true;
                break;
            case "3":
                renderFiltro3 = true;
                renderBuscar = true;
                break;
            case "4":
                renderFiltro4 = true;
                renderBuscar = true;
                break;
            case "5":
                renderFiltro5 = true;
                renderBuscar = true;
                break;
                
        }
    }

    public List<SelectItem> getListaFiltro() {
        return listaFiltro;
    }

    public void setListaFiltro(List<SelectItem> listaFiltro) {
        this.listaFiltro = listaFiltro;
    }

    public List<FacAdministradora> getListaAdministradora() {
        return listaAdministradora;
    }

    public void setListaAdministradora(List<FacAdministradora> listaAdministradora) {
        this.listaAdministradora = listaAdministradora;
    }

    public String getFiltroReporte() {
        return filtroReporte;
    }

    public void setFiltroReporte(String filtroReporte) {
        this.filtroReporte = filtroReporte;
    }

    public String getEstadoContrato() {
        return estadoContrato;
    }

    public void setEstadoContrato(String estadoContrato) {
        this.estadoContrato = estadoContrato;
    }

    public boolean isRenderFiltro1() {
        return renderFiltro1;
    }

    public void setRenderFiltro1(boolean renderFiltro1) {
        this.renderFiltro1 = renderFiltro1;
    }

    public boolean isRenderFiltro2() {
        return renderFiltro2;
    }

    public void setRenderFiltro2(boolean renderFiltro2) {
        this.renderFiltro2 = renderFiltro2;
    }

    public boolean isRenderFiltro3() {
        return renderFiltro3;
    }

    public void setRenderFiltro3(boolean renderFiltro3) {
        this.renderFiltro3 = renderFiltro3;
    }

    public boolean isRenderFiltro4() {
        return renderFiltro4;
    }

    public void setRenderFiltro4(boolean renderFiltro4) {
        this.renderFiltro4 = renderFiltro4;
    }

    public boolean isRenderFiltro5() {
        return renderFiltro5;
    }

    public void setRenderFiltro5(boolean renderFiltro5) {
        this.renderFiltro5 = renderFiltro5;
    }

    public boolean isRenderFiltro6() {
        return renderFiltro6;
    }

    public void setRenderFiltro6(boolean renderFiltro6) {
        this.renderFiltro6 = renderFiltro6;
    }

    public boolean isRenderBuscar() {
        return renderBuscar;
    }

    public void setRenderBuscar(boolean renderBuscar) {
        this.renderBuscar = renderBuscar;
    }

    public boolean isRenderResultado() {
        return renderResultado;
    }

    public void setRenderResultado(boolean renderResultado) {
        this.renderResultado = renderResultado;
    }

    public FacAdministradora getAdministradora() {
        return administradora;
    }

    public void setAdministradora(FacAdministradora administradora) {
        this.administradora = administradora;
    }

    public List<CfgClasificaciones> getListaTipoPago() {
        return listaTipoPago;
    }

    public void setListaTipoPago(List<CfgClasificaciones> listaTipoPago) {
        this.listaTipoPago = listaTipoPago;
    }

    public String getModalidad() {
        return modalidad;
    }

    public void setModalidad(String modalidad) {
        this.modalidad = modalidad;
    }

    public List<FacContrato> getListaConsulta() {
        return listaConsulta;
    }

    public void setListaConsulta(List<FacContrato> listaConsulta) {
        this.listaConsulta = listaConsulta;
    }
    
}
