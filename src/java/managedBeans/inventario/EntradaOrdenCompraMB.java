/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managedBeans.inventario;

import beans.enumeradores.TipoInventarioEnum;
import beans.utilidades.MetodosGenerales;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import managedBeans.seguridad.LoginMB;
import modelo.entidades.InvBodegaProductos;
import modelo.entidades.InvBodegas;
import modelo.entidades.InvConsecutivos;
import modelo.entidades.InvMovimientoProductos;
import modelo.entidades.InvMovimientos;
import modelo.entidades.InvOrdenCompra;
import modelo.entidades.InvOrdenCompraProductos;
import modelo.fachadas.InvBodegaProductosFacade;
import modelo.fachadas.InvBodegasFacade;
import modelo.fachadas.InvConsecutivosFacade;
import modelo.fachadas.InvMovimientosFacade;
import modelo.fachadas.InvOrdenCompraFacade;
import modelo.fachadas.InvOrdenCompraProductosFacade;

/**
 *
 * @author miguel
 */
@Named(value = "entradaOrdenCompraMB")
@ViewScoped
public class EntradaOrdenCompraMB extends MetodosGenerales implements java.io.Serializable{
    @EJB
    private InvOrdenCompraFacade ordenCompraFacade;
    @EJB
    private InvConsecutivosFacade consecutivoFacade;
    @EJB
    private InvBodegasFacade bodegaFacade;
    @EJB
    private InvBodegaProductosFacade bodegaProductoFacade;
    @EJB
    private InvMovimientosFacade movimientoFacade;
    @EJB
    private InvOrdenCompraProductosFacade ordenCompraProductoFacade;
    
    private InvOrdenCompra ordenCompra;
    private InvMovimientos movimiento;
    private InvConsecutivos consecutivo;
    private InvBodegas bodega;
    private InvBodegaProductos bodegaProductos;
    private String observaciones;
    private LoginMB loginMB;
    private String nroDocumento;
    private List<InvOrdenCompra> listaOrdenCompra;
    private List<InvOrdenCompraProductos> listaOrdenCompraProductos;
    public EntradaOrdenCompraMB() {
        loginMB = FacesContext.getCurrentInstance().getApplication().evaluateExpressionGet(FacesContext.getCurrentInstance(), "#{loginMB}", LoginMB.class);
    }
        
    
    @PostConstruct
    public void init(){
        ordenCompra = new InvOrdenCompra();
        movimiento = new InvMovimientos();
        movimiento.setFechaMovimiento(new Date());
        movimiento.setTipoMovimiento(TipoInventarioEnum.E.toString());
        this.consecutivo = consecutivoFacade.getConsecutivoTipo(TipoInventarioEnum.E.toString());
        int cod = consecutivo.getConsecutivo()+1;
        movimiento.setNumeroDocumento(String.format("%04d",cod));
        listaOrdenCompra = ordenCompraFacade.getComprasXEstado(TipoInventarioEnum.P.toString());
        bodega = bodegaFacade.bodegaPrincipal(loginMB.getEmpresaActual().getCodEmpresa(), 1);
    }
    public void buscar(){
        try {
            if(ordenCompra==null){
                imprimirMensaje("Resultado", "No se encontró resultado", FacesMessage.SEVERITY_INFO);
            }else{
                if(ordenCompra.getEstado().equals(TipoInventarioEnum.N.toString())){
                    imprimirMensaje("Resultado", "La orden se encuentra en estado anulado", FacesMessage.SEVERITY_INFO);
                }else if(ordenCompra.getEstado().equals(TipoInventarioEnum.C.toString())){
                    imprimirMensaje("Resultado", "La orden se encuentra en estado cerrada", FacesMessage.SEVERITY_INFO);
                }else{
                    listaOrdenCompraProductos = ordenCompra.getInvOrdenCompraProductosList();
                    movimiento.setNumeroDocumentoProveedor(ordenCompra.getNroDocumento());
                    for(int i=0;i<listaOrdenCompraProductos.size();i++){
                        listaOrdenCompraProductos.get(i).setCantidadEntregada(listaOrdenCompraProductos.get(i).getCantidad());
                    }
                }
            }
        } catch (Exception e) {
        }
    }
    public void guardar(){
        //guardamos movimiento y guardamos en la bodega
        try {
            this.consecutivo = consecutivoFacade.getConsecutivoTipo(TipoInventarioEnum.E.toString());
            int cod = consecutivo.getConsecutivo() + 1;
            movimiento.setNumeroDocumento(String.format("%04d",cod));
            movimiento.setObservaciones(observaciones);
            movimiento.setIdOrdenCompra(ordenCompra);
            movimiento.setUsuarioAprueba(loginMB.getUsuarioActual());
            movimiento.setFechaAprobacion(new Date());
            movimiento.setIdBodegaDestino(bodega);
            movimiento.setEstado(TipoInventarioEnum.C.toString());
            movimiento.setUsuarioAprueba(loginMB.getUsuarioActual());
            movimiento.setFechaAprobacion(new Date());
            movimiento.setTipoProceso(5);
            consecutivo.setConsecutivo(consecutivo.getConsecutivo()+1);
            consecutivoFacade.edit(consecutivo);
            
            movimiento.setInvMovimientoProductosList(new ArrayList<InvMovimientoProductos>());
            
            //recorremos los productos a actualizar en la bodega
            for(InvOrdenCompraProductos producto:listaOrdenCompraProductos){
                bodegaProductos = bodegaProductoFacade.getBodegaProducto(bodega.getIdBodega(), producto.getIdProducto().getIdProducto());
                if(bodegaProductos==null){//Creamos el producto
                    bodegaProductos = new InvBodegaProductos();
                    bodegaProductos.setIdBodega(bodega);
                    bodegaProductos.setIdProducto(producto.getIdProducto());
                    bodegaProductos.setExistencia(producto.getCantidadEntregada());
                    bodegaProductoFacade.create(bodegaProductos);
                }else{
                    bodegaProductos.setExistencia(bodegaProductos.getExistencia()+producto.getCantidadEntregada());
                    bodegaProductoFacade.edit(bodegaProductos);
                }
                
                //asociamos los productos al movimiento
                InvMovimientoProductos movimientoProducto = new InvMovimientoProductos();
                movimientoProducto.setIdProducto(producto.getIdProducto());
                movimientoProducto.setCantidadSolicitada(producto.getCantidad());
                movimientoProducto.setCantidadRecibida(producto.getCantidadEntregada());
                movimientoProducto.setIdMovimiento(movimiento);
                movimiento.getInvMovimientoProductosList().add(movimientoProducto);
                
                //actualizamos las entregas recibidas
                ordenCompraProductoFacade.edit(producto);
            }
            //guardamos movimiento
            movimientoFacade.create(movimiento);
            
            //actualizamos orden
            ordenCompra.setEstado(TipoInventarioEnum.C.toString());
            ordenCompra.setFechaActualizacion(new Date());
            ordenCompra.setUsuarioActualiza(loginMB.getUsuarioActual());
            ordenCompra.setInvOrdenCompraProductosList(listaOrdenCompraProductos);
            ordenCompraFacade.edit(ordenCompra);
            imprimirMensaje("Guardado", "Guardado Correctamente", FacesMessage.SEVERITY_INFO);
            nuevo();
        } catch (Exception e) {
        }
    }
    public void nuevo(){
        ordenCompra = new InvOrdenCompra();
        movimiento = new InvMovimientos();
        movimiento.setFechaMovimiento(new Date());
        movimiento.setTipoMovimiento(TipoInventarioEnum.E.toString());
        this.consecutivo = consecutivoFacade.getConsecutivoTipo(TipoInventarioEnum.E.toString());
        int cod = consecutivo.getConsecutivo()+1;
        movimiento.setNumeroDocumento(String.format("%04d",cod));
        listaOrdenCompra = ordenCompraFacade.getComprasXEstado(TipoInventarioEnum.P.toString());
        listaOrdenCompraProductos = new ArrayList<>();
        nroDocumento = "";
    }
    public void buscarCodigo(){
        try {
            ordenCompra = new InvOrdenCompra();
            ordenCompra.setNroDocumento(nroDocumento);
            ordenCompra = ordenCompraFacade.getCompraXDocumento(ordenCompra.getNroDocumento());
            if(ordenCompra==null){
                imprimirMensaje("Resultado", "No se encontró resultado", FacesMessage.SEVERITY_INFO);
            }else{
                if(ordenCompra.getEstado().equals(TipoInventarioEnum.N.toString())){
                    imprimirMensaje("Resultado", "La orden se encuentra en estado anulado", FacesMessage.SEVERITY_INFO);
                }else if(ordenCompra.getEstado().equals(TipoInventarioEnum.C.toString())){
                    imprimirMensaje("Resultado", "La orden se encuentra en estado cerrada", FacesMessage.SEVERITY_INFO);
                }else{
                    listaOrdenCompraProductos = ordenCompra.getInvOrdenCompraProductosList();
                    movimiento.setNumeroDocumentoProveedor(ordenCompra.getNroDocumento());
                    for(int i=0;i<listaOrdenCompraProductos.size();i++){
                        listaOrdenCompraProductos.get(i).setCantidadEntregada(listaOrdenCompraProductos.get(i).getCantidad());
                    }
                }
            }
        } catch (Exception e) {
        }
    }
    public InvOrdenCompra getOrdenCompra() {
        return ordenCompra;
    }

    public void setOrdenCompra(InvOrdenCompra ordenCompra) {
        this.ordenCompra = ordenCompra;
    }

    public List<InvOrdenCompra> getListaOrdenCompra() {
        return listaOrdenCompra;
    }

    public void setListaOrdenCompra(List<InvOrdenCompra> listaOrdenCompra) {
        this.listaOrdenCompra = listaOrdenCompra;
    }

    public InvMovimientos getMovimiento() {
        return movimiento;
    }

    public void setMovimiento(InvMovimientos movimiento) {
        this.movimiento = movimiento;
    }

    public InvConsecutivos getConsecutivo() {
        return consecutivo;
    }

    public void setConsecutivo(InvConsecutivos consecutivo) {
        this.consecutivo = consecutivo;
    }

    public List<InvOrdenCompraProductos> getListaOrdenCompraProductos() {
        return listaOrdenCompraProductos;
    }

    public void setListaOrdenCompraProductos(List<InvOrdenCompraProductos> listaOrdenCompraProductos) {
        this.listaOrdenCompraProductos = listaOrdenCompraProductos;
    }

    public InvBodegas getBodega() {
        return bodega;
    }

    public void setBodega(InvBodegas bodega) {
        this.bodega = bodega;
    }

    

    public LoginMB getLoginMB() {
        return loginMB;
    }

    public void setLoginMB(LoginMB loginMB) {
        this.loginMB = loginMB;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public String getNroDocumento() {
        return nroDocumento;
    }

    public void setNroDocumento(String nroDocumento) {
        this.nroDocumento = nroDocumento;
    }
}
