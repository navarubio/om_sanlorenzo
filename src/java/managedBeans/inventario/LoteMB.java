/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managedBeans.inventario;

import beans.utilidades.MetodosGenerales;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import managedBeans.seguridad.AplicacionGeneralMB;
import managedBeans.seguridad.LoginMB;
import modelo.entidades.CfgEmpresa;
import modelo.entidades.InvLoteProductos;
import modelo.entidades.InvLotes;
import modelo.entidades.InvProductos;
import modelo.fachadas.CfgEmpresaFacade;
import modelo.fachadas.InvLoteProductosFacade;
import modelo.fachadas.InvLotesFacade;
import modelo.fachadas.InvProductosFacade;
import org.primefaces.context.RequestContext;

/**
 *
 * @author miguel
 */
@Named(value = "loteMB")
@ViewScoped
public class LoteMB extends MetodosGenerales implements java.io.Serializable{

    @EJB
    private InvLotesFacade loteFachada;
    @EJB
    private InvLoteProductosFacade loteProductoFachada;
    @EJB 
    private InvProductosFacade productoFachada;
    @EJB
    private CfgEmpresaFacade empresaFacade;
    
    
    private InvLotes lote;
    private InvProductos producto;
    
    private LoginMB loginMB;
    private CfgEmpresa empresaActual;
    
    private List<InvLotes> listaLotes;
    private List<InvProductos> listaProductos;
    private List<InvLoteProductos> listaLoteProductos;
    private List<InvLoteProductos> listaLoteProductosEliminar;
    public LoteMB() {
        loginMB = FacesContext.getCurrentInstance().getApplication().evaluateExpressionGet(FacesContext.getCurrentInstance(), "#{loginMB}", LoginMB.class);
    }
   
    @PostConstruct
    public void init(){
        lote = new InvLotes();
        lote.setFechaVencimiento(new Date());
        listaProductos = productoFachada.getProductosLotesActivos();
        listaLoteProductos = new ArrayList<>();
        listaLoteProductosEliminar = new ArrayList<>();
        listaLotes = loteFachada.findAll();
        this.empresaActual =  empresaFacade.find(1);
    }
    public void buscar(){
        if(lote!=null){
            listaLoteProductos =lote.getInvLoteProductosList();
        }
    }
    public void nuevo(){
        lote = new InvLotes();
        lote.setFechaVencimiento(new Date());
        lote.setIdEmpresa(empresaActual);
        listaLoteProductos = new ArrayList<>();
        listaLoteProductosEliminar = new ArrayList<>();
    }
    public void guardar(){
        try {
            if(validarDatos()){
                if(lote.getIdLote()==null){
                    lote.setFechaCreacion(new Date());
                    lote.setUsuarioCrea(loginMB.getUsuarioActual());
                    lote.setIdEmpresa(empresaActual);
                    loteFachada.create(lote);
                    //guardamos el detalle
                    for(InvLoteProductos il:listaLoteProductos){
                        il.setIdLote(lote);
                        loteProductoFachada.create(il);
                    }
                }else{
                    loteFachada.edit(lote);
                    //guardamos el detalle
                    for(InvLoteProductos il:listaLoteProductos){
                        if(il.getIdLote()==null){
                            il.setIdLote(lote);
                            loteProductoFachada.create(il);
                        }
                    }
                    //eliminamos los registros
                    for(InvLoteProductos il:listaLoteProductosEliminar){
                        loteProductoFachada.remove(il);
                    }
                }
                imprimirMensaje("Guardado", "Guardado Correctamente", FacesMessage.SEVERITY_INFO);
                nuevo();
            }//fin validar datos
        } catch (Exception e) {
            imprimirMensaje("Error al guardar", e.getLocalizedMessage(), FacesMessage.SEVERITY_ERROR);
        }
    }
    public void eliminar(InvProductos producto){
        for(InvLoteProductos il:listaLoteProductos){
            if(Objects.equals(il.getIdProducto().getIdProducto(), producto.getIdProducto())){
                listaLoteProductosEliminar.add(il);
                listaLoteProductos.remove(il);
                break;
            }
        }
        RequestContext.getCurrentInstance().update("IdFormLotes:idDTProductos");
        
    }

    public void seleccionarProducto(){
        if(producto!=null){
            InvLoteProductos productoLote = new InvLoteProductos();
            productoLote.setIdProducto(producto);
            productoLote.setIdLote(lote);
            listaLoteProductos.add(productoLote);
            
        }
    }
    private boolean validarDatos(){
        if(lote.getCodigo().equals("")){
            imprimirMensaje("Error al guardr", "Dígite código del lote", FacesMessage.SEVERITY_ERROR);
            return false;
        }
        return true;
    }
    public InvLotesFacade getLoteFachada() {
        return loteFachada;
    }

    public void setLoteFachada(InvLotesFacade loteFachada) {
        this.loteFachada = loteFachada;
    }

    public InvLotes getLote() {
        return lote;
    }

    public void setLote(InvLotes lote) {
        this.lote = lote;
    }

    public List<InvLotes> getListaLotes() {
        return listaLotes;
    }

    public void setListaLotes(List<InvLotes> listaLotes) {
        this.listaLotes = listaLotes;
    }

    public List<InvProductos> getListaProductos() {
        return listaProductos;
    }

    public void setListaProductos(List<InvProductos> listaProductos) {
        this.listaProductos = listaProductos;
    }

    public List<InvLoteProductos> getListaLoteProductos() {
        return listaLoteProductos;
    }

    public void setListaLoteProductos(List<InvLoteProductos> listaLoteProductos) {
        this.listaLoteProductos = listaLoteProductos;
    }

    public InvProductos getProducto() {
        return producto;
    }

    public void setProducto(InvProductos producto) {
        this.producto = producto;
    }

    public LoginMB getLoginMB() {
        return loginMB;
    }

    public void setLoginMB(LoginMB loginMB) {
        this.loginMB = loginMB;
    }
}
