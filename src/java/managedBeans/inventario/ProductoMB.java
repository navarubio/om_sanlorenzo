/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managedBeans.inventario;

import beans.enumeradores.ClasificacionesEnum;
import beans.utilidades.MetodosGenerales;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import modelo.entidades.CfgClasificaciones;
import modelo.entidades.CfgMedicamento;
import modelo.entidades.CfgUnidad;
import modelo.entidades.InvCategorias;
import modelo.entidades.InvProductos;
import modelo.fachadas.CfgClasificacionesFacade;
import modelo.fachadas.CfgMedicamentoFacade;
import modelo.fachadas.CfgUnidadFacade;
import modelo.fachadas.InvCategoriasFacade;
import modelo.fachadas.InvProductosFacade;
import org.primefaces.context.RequestContext;

/**
 *
 * @author miguel
 */
@Named(value = "productoMB")
@ViewScoped
public class ProductoMB extends MetodosGenerales implements java.io.Serializable{

    @EJB
    private CfgClasificacionesFacade clasificacionesFachada;
    @EJB
    private InvCategoriasFacade categoriaFachada;
    @EJB
    private CfgUnidadFacade unidadFacada;
    @EJB
    private InvProductosFacade productoFachada;
    @EJB
    private CfgMedicamentoFacade medicamentoFachada;
    
    private InvCategorias categoria;
    private InvProductos producto;
    private CfgUnidad unidaMedida;
    private CfgClasificaciones viaAdministracion;
    private CfgClasificaciones presentacion;
    private CfgMedicamento medicamento;
    
    private boolean renderFormProducto;
    private boolean renderFormTabla;
    private boolean renderFormBotones;
    private boolean renderCodigoCUMS;
    
    private List<InvCategorias> listaCategorias;
    private List<CfgClasificaciones> listaViaAdministracion;
    private List<CfgUnidad> listaUnidades;
    private List<InvProductos> listaProductos;
    private List<CfgClasificaciones> listaPresentacion;
    
    public ProductoMB() {
    }
    
    @PostConstruct()
    public void init(){
        if(listaCategorias==null)listaCategorias = categoriaFachada.getActivos();
        if(listaUnidades==null)listaUnidades = unidadFacada.findAll();
        if(listaViaAdministracion==null)listaViaAdministracion=clasificacionesFachada.buscarPorMaestro(ClasificacionesEnum.ViaAdministracion.toString());
        this.renderFormProducto=false;
        this.renderFormBotones = false;
        this.categoria = new InvCategorias();
        this.categoria.setIdCategoria(0);
        this.producto = new InvProductos();
        this.unidaMedida = new CfgUnidad();
        this.viaAdministracion = new CfgClasificaciones();
        this.listaProductos = new ArrayList<>();
        this.presentacion = new CfgClasificaciones();
        this.listaPresentacion = clasificacionesFachada.buscarPorMaestro(ClasificacionesEnum.Presentacion.toString());
        this.renderCodigoCUMS = false;
    }
    
    public void cancelar(){
        this.renderFormProducto=false;
        this.renderFormBotones = false;
        this.renderFormTabla = false;
        this.categoria = new InvCategorias();
        this.categoria.setIdCategoria(0);
        this.producto = new InvProductos();
        this.unidaMedida = new CfgUnidad();
        this.viaAdministracion = new CfgClasificaciones();
        this.listaProductos = new ArrayList<>();
        this.renderCodigoCUMS = false;
    }
    public void buscar(){
        if(producto.getIdPresentacion()==null){
            presentacion = new CfgClasificaciones();
            presentacion.setId(0);
        }else{
            presentacion = producto.getIdPresentacion();
        }
        this.renderCodigoCUMS = categoria.getCodigo().equals("001");
        
    }
    public void nuevo(){
        this.renderFormTabla = false;
        this.renderFormBotones = true;
        this.renderFormProducto = true;
        this.producto = new InvProductos();
        this.producto.setIdCategoria(categoria);
        this.producto.setActivo(true);
        this.unidaMedida = new CfgUnidad();
        this.viaAdministracion = new CfgClasificaciones();
        this.listaProductos = new ArrayList<>();
        this.presentacion = new CfgClasificaciones();
        this.listaPresentacion = clasificacionesFachada.buscarPorMaestro(ClasificacionesEnum.Presentacion.toString());
    }
    public void eliminar(InvProductos producto){
        this.producto = producto;
        this.producto.setActivo(false);
        productoFachada.edit(producto);
        imprimirMensaje("Guardado", "Actualizado Correctamente", FacesMessage.SEVERITY_INFO);
        cancelar();        
        RequestContext.getCurrentInstance().update("idFormProducto");
    }
    public void guardar(){
        if(validarDatos()){
            try {
                if(unidaMedida.getId()!=0)producto.setIdUnidadMedida(unidaMedida);
                if(viaAdministracion.getId()!=0)producto.setIdViaAdministracion(viaAdministracion);
                if(presentacion.getId()!=0)producto.setIdPresentacion(presentacion);
                producto.setIdCategoria(categoria);
                producto.setMedicamento(renderCodigoCUMS);
                if(producto.getIdProducto()==null){
                    productoFachada.create(producto);
                }else{
                    productoFachada.edit(producto);
                }
                //Validamos si el producto esta asociado a un medicamento
                medicamento = medicamentoFachada.medicamentoXIdProducto(producto.getIdProducto());
                if(medicamento==null){
                    medicamento = new CfgMedicamento();
                    medicamento.setCodigoMedicamento(producto.getCodigo());
                    medicamento.setCodigoCums(producto.getCodigoCums());
                    medicamento.setCodigoCups("");
                    medicamento.setNombreMedicamento(producto.getNombre());
                    medicamento.setNombreGenerico(producto.getNombreGenerico());
                    medicamento.setNombreComercial(producto.getNombreGenerico());
                    medicamento.setFormaMedicamento(producto.getIdPresentacion().getDescripcion());
                    medicamento.setPos(true);
                    medicamento.setConcentracion("");
                    medicamento.setUnidadMedida(producto.getIdUnidadMedida().getCodigo());
                    medicamento.setControlMedico(false);
                    medicamento.setRegistroSanitario(producto.getRegistroSanitario());
                    medicamento.setModAdmin(producto.getIdViaAdministracion().getDescripcion());
                    medicamento.setValor(producto.getCosto());
                    medicamento.setIdProducto(producto);
                    medicamentoFachada.create(medicamento);
                }else{
                    medicamento.setCodigoMedicamento(producto.getCodigo());
                    medicamento.setCodigoCums(producto.getCodigoCums());
                    medicamento.setCodigoCups("");
                    medicamento.setNombreMedicamento(producto.getNombre());
                    medicamento.setNombreGenerico(producto.getNombreGenerico());
                    medicamento.setNombreComercial(producto.getNombreGenerico());
                    medicamento.setFormaMedicamento(producto.getIdPresentacion().getDescripcion());
                    medicamento.setPos(true);
                    medicamento.setConcentracion("");
                    medicamento.setUnidadMedida(producto.getIdUnidadMedida().getCodigo());
                    medicamento.setControlMedico(false);
                    medicamento.setRegistroSanitario(producto.getRegistroSanitario());
                    medicamento.setModAdmin(producto.getIdViaAdministracion().getDescripcion());
                    medicamento.setValor(producto.getCosto());
                    medicamentoFachada.edit(medicamento);
                }
                imprimirMensaje("Guardado", "Guardado Correctamente", FacesMessage.SEVERITY_INFO);
                cancelar();        
                RequestContext.getCurrentInstance().update("idFormProducto");
            } catch (Exception e) {
                imprimirMensaje("Error al guardar", e.getLocalizedMessage(), FacesMessage.SEVERITY_ERROR);
            }
        }
    }

    public void editar(InvProductos producto){
        this.producto = producto;
        this.presentacion = this.producto.getIdPresentacion();
        this.renderFormTabla = false;
        this.renderFormBotones = true;
        this.renderFormProducto=true;
        if(this.producto.getIdUnidadMedida()!=null)unidaMedida=this.producto.getIdUnidadMedida();
        if(this.producto.getIdViaAdministracion()!=null)viaAdministracion=this.producto.getIdViaAdministracion();
        if(this.producto.getIdPresentacion()==null){
            presentacion = new CfgClasificaciones();
            presentacion.setId(0);
        }else{
            presentacion = this.producto.getIdPresentacion();
        }
        RequestContext.getCurrentInstance().update("idFormProducto:opFormulario");
        RequestContext.getCurrentInstance().update("idFormProducto:opProductos");
        
    }
    public void validarCategoria(){
        this.renderFormTabla=false;
        this.renderFormProducto = false;
        this.renderFormBotones = false;
        if(categoria.getIdCategoria()!=0){
            categoria = categoriaFachada.find(categoria.getIdCategoria());
            this.renderCodigoCUMS = categoria.getCodigo().equals("001");
            this.renderFormTabla = true;
            this.renderFormBotones = true;
            //Cargamos los productos asociados a la categoria
            listaProductos = productoFachada.getProductoCategorias(categoria.getIdCategoria());
        }else{
            this.renderFormTabla=false;
            this.renderFormProducto = false;
            this.renderFormBotones = false;
        }
    }
    private boolean validarDatos(){
        if(producto.getCodigo()==null){
            imprimirMensaje("Error al guardar", "Digite código del producto", FacesMessage.SEVERITY_ERROR);
            return false;
        }else if(producto.getNombre().equals("")){
            imprimirMensaje("Error al guardar", "Digite nombre del producto", FacesMessage.SEVERITY_ERROR);
            return false;
        }
        return true;
    }
    public List<InvCategorias> getListaCategorias() {
        return listaCategorias;
    }

    public void setListaCategorias(List<InvCategorias> listaCategorias) {
        this.listaCategorias = listaCategorias;
    }

    public List<CfgUnidad> getListaUnidades() {
        return listaUnidades;
    }

    public void setListaUnidades(List<CfgUnidad> listaUnidades) {
        this.listaUnidades = listaUnidades;
    }

    public List<CfgClasificaciones> getListaViaAdministracion() {
        return listaViaAdministracion;
    }

    public void setListaViaAdministracion(List<CfgClasificaciones> listaViaAdministracion) {
        this.listaViaAdministracion = listaViaAdministracion;
    }

    public boolean isRenderFormProducto() {
        return renderFormProducto;
    }

    public void setRenderFormProducto(boolean renderFormProducto) {
        this.renderFormProducto = renderFormProducto;
    }

    public InvCategorias getCategoria() {
        return categoria;
    }

    public void setCategoria(InvCategorias categoria) {
        this.categoria = categoria;
    }

    public boolean isRenderFormTabla() {
        return renderFormTabla;
    }

    public void setRenderFormTabla(boolean renderFormTabla) {
        this.renderFormTabla = renderFormTabla;
    }

    public boolean isRenderFormBotones() {
        return renderFormBotones;
    }

    public void setRenderFormBotones(boolean renderFormBotones) {
        this.renderFormBotones = renderFormBotones;
    }

    public CfgUnidadFacade getUnidadFacada() {
        return unidadFacada;
    }

    public void setUnidadFacada(CfgUnidadFacade unidadFacada) {
        this.unidadFacada = unidadFacada;
    }

    public InvProductos getProducto() {
        return producto;
    }

    public void setProducto(InvProductos producto) {
        this.producto = producto;
    }

    public CfgUnidad getUnidaMedida() {
        return unidaMedida;
    }

    public void setUnidaMedida(CfgUnidad unidaMedida) {
        this.unidaMedida = unidaMedida;
    }

    public CfgClasificaciones getViaAdministracion() {
        return viaAdministracion;
    }

    public void setViaAdministracion(CfgClasificaciones viaAdministracion) {
        this.viaAdministracion = viaAdministracion;
    }

    public List<InvProductos> getListaProductos() {
        return listaProductos;
    }

    public void setListaProductos(List<InvProductos> listaProductos) {
        this.listaProductos = listaProductos;
    }

    public List<CfgClasificaciones> getListaPresentacion() {
        return listaPresentacion;
    }

    public void setListaPresentacion(List<CfgClasificaciones> listaPresentacion) {
        this.listaPresentacion = listaPresentacion;
    }

    public CfgClasificaciones getPresentacion() {
        return presentacion;
    }

    public void setPresentacion(CfgClasificaciones presentacion) {
        this.presentacion = presentacion;
    }

    public boolean isRenderCodigoCUMS() {
        return renderCodigoCUMS;
    }

    public void setRenderCodigoCUMS(boolean renderCodigoCUMS) {
        this.renderCodigoCUMS = renderCodigoCUMS;
    }
    
            
}
