/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managedBeans.configuraciones;

import beans.utilidades.MetodosGenerales;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import modelo.entidades.CfgHistoriaCamposPredefinidos;
import modelo.entidades.HcCamposReg;
import modelo.entidades.HcTipoReg;
import modelo.fachadas.CfgHistoriaCamposPredefinidosFacade;
import modelo.fachadas.HcCamposRegFacade;
import modelo.fachadas.HcTipoRegFacade;
import org.primefaces.context.RequestContext;

/**
 *
 * @author Arcosoft-PC2
 */
@Named(value = "historiaCamposDefinidosMB")
@ViewScoped
public class HistoriaCamposDefinidosMB extends MetodosGenerales implements java.io.Serializable{

    
    @EJB
    private CfgHistoriaCamposPredefinidosFacade cfgHistoriaCamposPredefinidosFacade;
    @EJB
    private HcTipoRegFacade hcTipoRegFacade;
    @EJB
    private HcCamposRegFacade hcCamposRegFacade;
    
    private List<HcTipoReg> listaHistoriasClinicas;
    private List<HcCamposReg> listaCamposHistorias;
    private List<CfgHistoriaCamposPredefinidos> listaCampos;
    private List<CfgHistoriaCamposPredefinidos> listaValores;
    private CfgHistoriaCamposPredefinidos cfgHistoriaCamposPredefinidos;
    
    private int id;
    private int idHcTipoReg;
    private int idCampo;
    private String valor;
    private boolean renderAgregar;
    private CfgHistoriaCamposPredefinidos campoSeleccionado;
    public HistoriaCamposDefinidosMB() {
    }
    
    @PostConstruct
    public void init(){
       listaHistoriasClinicas = hcTipoRegFacade.buscarTiposRegstroActivos();
       cfgHistoriaCamposPredefinidos = new CfgHistoriaCamposPredefinidos();
       listaCamposHistorias= new ArrayList<>();
       listaCampos= cfgHistoriaCamposPredefinidosFacade.findAll();
       idHcTipoReg = 0;
       idCampo=0;
       valor="";
       id=0;
       listaValores = new ArrayList<>();
       renderAgregar = false;
    }
    
    public void cargarCamposHistorias(){
        if(idCampo!=0){
            listaValores = cfgHistoriaCamposPredefinidosFacade.getCamposDefinidosXHistoriaClinicaXCampo(idHcTipoReg, idCampo);
            renderAgregar = true;
        }else{
            renderAgregar = false;
        }
        
    }

    public void setDefault(CfgHistoriaCamposPredefinidos predefinido){
        try {
            //cfgHistoriaCamposPredefinidosFacade.setNoneDefault(predefinido.getIdCampo().getIdCampo());
            for(CfgHistoriaCamposPredefinidos campo : listaValores){
                campo.setDefaultValor(false);
                cfgHistoriaCamposPredefinidosFacade.edit(campo);
            }
            predefinido.setDefaultValor(true);
            cfgHistoriaCamposPredefinidosFacade.edit(predefinido);
            RequestContext.getCurrentInstance().update("IdFormPrincipal");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void editar(CfgHistoriaCamposPredefinidos campo){
        id=  campo.getId();
        campoSeleccionado = campo;
        idCampo = campo.getIdCampo().getIdCampo();
        valor = campo.getValor();
        RequestContext.getCurrentInstance().update("frmNuevoValor");
        RequestContext.getCurrentInstance().update("IdValorNuevo");
        RequestContext.getCurrentInstance().execute("PF('dialogoNuevoValor').show()");
        RequestContext.getCurrentInstance().update("frmNuevoValor");
    }
    public void eliminar(CfgHistoriaCamposPredefinidos campo){
        try {
            cfgHistoriaCamposPredefinidosFacade.remove(campo);
            listaValores = cfgHistoriaCamposPredefinidosFacade.getCamposDefinidosXHistoriaClinicaXCampo(idHcTipoReg, idCampo);
            RequestContext.getCurrentInstance().update("IdFormPrincipal");
        } catch (Exception e) {
        }
    }
    public void nuevo(){
        idHcTipoReg = 0;
        listaCamposHistorias.clear();
        cfgHistoriaCamposPredefinidos = new CfgHistoriaCamposPredefinidos();
        idHcTipoReg = 0;
        idCampo=0;
        valor="";
        id=0;
        listaCampos= cfgHistoriaCamposPredefinidosFacade.findAll();
        renderAgregar =false;
        listaValores= new ArrayList<>();
        RequestContext.getCurrentInstance().update("IdFormPrincipal");
    }
    
    
    public void cargar(){
        id= cfgHistoriaCamposPredefinidos.getId();
        idCampo = cfgHistoriaCamposPredefinidos.getIdCampo().getIdCampo();
        HcCamposReg camposReg = hcCamposRegFacade.find(idCampo);
        idHcTipoReg = camposReg.getIdTipoReg().getIdTipoReg();
        listaCamposHistorias = hcCamposRegFacade.buscarPorTipoRegistro(idHcTipoReg);
        valor = cfgHistoriaCamposPredefinidos.getValor();
        RequestContext.getCurrentInstance().execute("PF('dialogoBuscarCampos').hide(); PF('wvTablaCampos').clearFilters(); PF('wvTablaCampos').getPaginator().setPage(0);");
        RequestContext.getCurrentInstance().update("IdFormPrincipal");
    }
    public void guardar(){
        
        if(validar()){
            try {
            if(id==0){
                cfgHistoriaCamposPredefinidos = new CfgHistoriaCamposPredefinidos();
                cfgHistoriaCamposPredefinidos.setDefaultValor(false);
                cfgHistoriaCamposPredefinidos.setIdCampo(new HcCamposReg(idCampo));
                cfgHistoriaCamposPredefinidos.setValor(valor);
                cfgHistoriaCamposPredefinidosFacade.create(cfgHistoriaCamposPredefinidos);
            }else{
                campoSeleccionado.setValor(valor);
                //cfgHistoriaCamposPredefinidos.setIdCampo(new HcCamposReg(idCampo));
                //cfgHistoriaCamposPredefinidos.setValor(valor);
                cfgHistoriaCamposPredefinidosFacade.edit(campoSeleccionado);
            }
                imprimirMensaje("Guardado", "Guardado Correctamente", FacesMessage.SEVERITY_INFO);
                //nuevo();
                
            } catch (Exception e) {
                e.printStackTrace();
                imprimirMensaje("Error al guardar", e.getLocalizedMessage(), FacesMessage.SEVERITY_FATAL);
            }
            
        }
    }
    private boolean validar(){
        if(this.idCampo==0){
            imprimirMensaje("Seleccione campo", "Campo Vacío", FacesMessage.SEVERITY_WARN);
            return false;
        }else if(this.valor.equals("")){
            imprimirMensaje("Digite Valor", "Valor Predefinido Vacío", FacesMessage.SEVERITY_WARN);
            return false;
        }
        return true;
    }
    public void eliminar(){
        if(cfgHistoriaCamposPredefinidos!=null)cfgHistoriaCamposPredefinidosFacade.remove(cfgHistoriaCamposPredefinidos);
        else imprimirMensaje("Seleccione un campo", "Consulte un campo", FacesMessage.SEVERITY_FATAL);
        RequestContext.getCurrentInstance().update("IdFormPrincipal");
        nuevo();
    }
    public void cargarCampos(){
        if(idHcTipoReg!=0){
            listaCamposHistorias = hcCamposRegFacade.buscarPorTipoRegistro(idHcTipoReg);
        }else {
            renderAgregar = false;
            listaCamposHistorias.clear();
        }
    }
    
    public void cancelarValor(){
        valor="";
        RequestContext.getCurrentInstance().update("frmNuevoValor");
    }
    
    public void guardarValor(){
        
        guardar();
        valor="";
        listaValores = cfgHistoriaCamposPredefinidosFacade.getCamposDefinidosXHistoriaClinicaXCampo(idHcTipoReg, idCampo);
        RequestContext.getCurrentInstance().update("IdFormPrincipal");
        RequestContext.getCurrentInstance().update("frmNuevoValor");
        RequestContext.getCurrentInstance().execute("PF('dialogoNuevoValor').hide()");
    }
    public List<HcTipoReg> getListaHistoriasClinicas() {
        return listaHistoriasClinicas;
    }

    public void setListaHistoriasClinicas(List<HcTipoReg> listaHistoriasClinicas) {
        this.listaHistoriasClinicas = listaHistoriasClinicas;
    }

    public HcTipoRegFacade getHcTipoRegFacade() {
        return hcTipoRegFacade;
    }

    public void setHcTipoRegFacade(HcTipoRegFacade hcTipoRegFacade) {
        this.hcTipoRegFacade = hcTipoRegFacade;
    }

    public List<HcCamposReg> getListaCamposHistorias() {
        return listaCamposHistorias;
    }

    public void setListaCamposHistorias(List<HcCamposReg> listaCamposHistorias) {
        this.listaCamposHistorias = listaCamposHistorias;
    }

    public int getIdHcTipoReg() {
        return idHcTipoReg;
    }

    public void setIdHcTipoReg(int idHcTipoReg) {
        this.idHcTipoReg = idHcTipoReg;
    }

    public List<CfgHistoriaCamposPredefinidos> getListaCampos() {
        return listaCampos;
    }

    public void setListaCampos(List<CfgHistoriaCamposPredefinidos> listaCampos) {
        this.listaCampos = listaCampos;
    }

    public CfgHistoriaCamposPredefinidos getCfgHistoriaCamposPredefinidos() {
        return cfgHistoriaCamposPredefinidos;
    }

    public void setCfgHistoriaCamposPredefinidos(CfgHistoriaCamposPredefinidos cfgHistoriaCamposPredefinidos) {
        this.cfgHistoriaCamposPredefinidos = cfgHistoriaCamposPredefinidos;
    }

    public int getIdCampo() {
        return idCampo;
    }

    public void setIdCampo(int idCampo) {
        this.idCampo = idCampo;
    }

    public String getValor() {
        return valor;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<CfgHistoriaCamposPredefinidos> getListaValores() {
        return listaValores;
    }

    public void setListaValores(List<CfgHistoriaCamposPredefinidos> listaValores) {
        this.listaValores = listaValores;
    }

    public boolean isRenderAgregar() {
        return renderAgregar;
    }

    public void setRenderAgregar(boolean renderAgregar) {
        this.renderAgregar = renderAgregar;
    }
    
    
    
}