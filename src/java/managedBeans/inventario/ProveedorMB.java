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
import java.util.Objects;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import modelo.entidades.CfgClasificaciones;
import modelo.entidades.InvProductos;
import modelo.entidades.InvProveedorProductos;
import modelo.entidades.InvProveedores;
import modelo.fachadas.CfgClasificacionesFacade;
import modelo.fachadas.InvProductosFacade;
import modelo.fachadas.InvProveedorProductosFacade;
import modelo.fachadas.InvProveedoresFacade;
import org.primefaces.context.RequestContext;

/**
 *
 * @author miguel
 */
@Named(value = "proveedorMB")
@ViewScoped
public class ProveedorMB extends MetodosGenerales implements java.io.Serializable{

    @EJB
    private CfgClasificacionesFacade clasificacionFachada;
    @EJB
    private InvProveedoresFacade proveedorFachada;
    @EJB
    private InvProductosFacade productoFachada;
    @EJB
    private InvProveedorProductosFacade proveedorProductoFachada;
    
    private InvProveedores proveedor;
    private CfgClasificaciones departamento;
    private CfgClasificaciones municipio;
    private CfgClasificaciones tipoDocumento;
    private InvProveedores proveedorSeleccionada;
    private InvProductos producto;
    
    private List<InvProveedores>        listaProveedores;
    private List<CfgClasificaciones>    listaDepartamentos;
    private List<CfgClasificaciones>    listaMunicipios;
    private List<CfgClasificaciones>    listaTipoDocumentos;
    private List<InvProductos> listaProductos;
    private List<InvProveedorProductos> listaProveedorProductos;
    private List<InvProveedorProductos> listaProveedorProductosEliminar;
    public ProveedorMB() {
    }
    
    @PostConstruct
    public void init(){
        proveedor = new InvProveedores();
        proveedor.setActivo(true);
        departamento = new CfgClasificaciones();
        municipio = new CfgClasificaciones();
        tipoDocumento  = new CfgClasificaciones();
        listaProveedores = proveedorFachada.findAll();
        listaDepartamentos = clasificacionFachada.buscarPorMaestro(ClasificacionesEnum.DPTO.toString());
        listaMunicipios = new ArrayList<>();
        listaTipoDocumentos = clasificacionFachada.buscarPorMaestro(ClasificacionesEnum.TipoIdentificacion.toString());
        listaProductos = productoFachada.getProductosActivos();
        listaProveedorProductos = new ArrayList();
        listaProveedorProductosEliminar = new ArrayList<>();
    }
    
    public void nuevo(){
        proveedor = new InvProveedores();
        proveedor.setActivo(true);
        departamento = new CfgClasificaciones();
        municipio = new CfgClasificaciones();
        tipoDocumento  = new CfgClasificaciones();
        listaProveedores = proveedorFachada.findAll();
        listaDepartamentos = clasificacionFachada.buscarPorMaestro(ClasificacionesEnum.DPTO.toString());
        listaMunicipios = new ArrayList<>();
        listaTipoDocumentos = clasificacionFachada.buscarPorMaestro(ClasificacionesEnum.TipoIdentificacion.toString());
        listaProveedorProductos = new ArrayList();
        listaProveedorProductosEliminar = new ArrayList<>();
    }
    public void buscar(){
         proveedor=proveedorSeleccionada;
        try {
            departamento = proveedor.getIdDepartamento();
            cargarMunicipios();
            municipio = proveedor.getIdMunicipio();
            tipoDocumento = proveedor.getTipoDocumento();
            listaProveedorProductos = proveedor.getInvProveedorProductosList();
        } catch (Exception e) {
        }
    }
    public void guardar(){
        if(validarDatos()){
            try {
                InvProveedores proveedorConsulta = proveedorFachada.getProveedorXCodigo(proveedor.getCodigoProveedor());
                if(proveedorConsulta!=null && proveedor.getIdProveedor()==null)proveedor.setIdProveedor(proveedorConsulta.getIdProveedor());
                proveedor.setActivo(true);
                proveedor.setIdDepartamento(departamento);
                proveedor.setIdMunicipio(municipio);
                proveedor.setTipoDocumento(tipoDocumento);
                if(proveedor.getIdProveedor()==null){
                    proveedorFachada.create(proveedor);
                    //guardamos el detalle
                    for(InvProveedorProductos il:listaProveedorProductos){
                        il.setIdProveedor(proveedor);
                        proveedorProductoFachada.create(il);
                    }
                }else{
                    proveedorFachada.edit(proveedor);
                    //guardamos el detalle
                    for(InvProveedorProductos il:listaProveedorProductos){
                        if(il.getIdProveedor()==null){
                            il.setIdProveedor(proveedor);
                            proveedorProductoFachada.create(il);
                        }
                    }
                    //eliminamos los registros
                    for(InvProveedorProductos il:listaProveedorProductosEliminar){
                        proveedorProductoFachada.remove(il);
                    }
                }
                imprimirMensaje("Guardado", "Guardado Correctamente", FacesMessage.SEVERITY_INFO);
                nuevo();
                RequestContext.getCurrentInstance().update("IdTablaProveedor");
            } catch (Exception e) {
                imprimirMensaje("Error al guardar", e.getLocalizedMessage(), FacesMessage.SEVERITY_ERROR);
            }
        }
    }
    public void eliminar(){
        if(proveedor!=null){
            try {
                proveedor.setActivo(false);
                proveedorFachada.edit(proveedor);
                imprimirMensaje("Guardado", "Actualizado Correctamente", FacesMessage.SEVERITY_INFO);
                nuevo();
            } catch (Exception e) {
            }
        }
    }
    public void eliminarProducto(InvProductos producto){
        for(InvProveedorProductos il:listaProveedorProductos){
            if(Objects.equals(il.getIdProducto().getIdProducto(), producto.getIdProducto())){
                listaProveedorProductosEliminar.add(il);
                listaProveedorProductos.remove(il);
                break;
            }
        }
        RequestContext.getCurrentInstance().update("IdFormProveedores:idDTProductos");
    }
    public void buscarCodigo(){
        try {
            proveedor = proveedorFachada.getProveedorXCodigo(proveedor.getCodigoProveedor());
            if(proveedor==null){
                proveedor = new InvProveedores();
                proveedor.setActivo(true);
                imprimirMensaje("No existe", "Proveedor no existe", FacesMessage.SEVERITY_INFO);
            }else{
                try {
                    departamento = proveedor.getIdDepartamento();
                    cargarMunicipios();
                    municipio = proveedor.getIdMunicipio();
                    tipoDocumento = proveedor.getTipoDocumento();
                } catch (Exception e) {
                }
            }
        } catch (Exception e) {
        }
    }
    public void seleccionarProducto(){
        if(producto!=null){
            InvProveedorProductos proveedorProducto = new InvProveedorProductos();
            proveedorProducto.setIdProducto(producto);
            proveedorProducto.setIdProveedor(proveedor);
            listaProveedorProductos.add(proveedorProducto);
        }
    }
    public void cargarMunicipios() {
        listaMunicipios = new ArrayList<>();
        if (departamento.getId()!=0) {
            listaMunicipios = clasificacionFachada.buscarMunicipioPorDepartamento(clasificacionFachada.find(departamento.getId()).getCodigo());
        }
    }

    private boolean validarDatos(){
        if(proveedor.getCodigoProveedor().equals("")){
            imprimirMensaje("Error al guardar", "Dígite      código", FacesMessage.SEVERITY_ERROR);
            return false;
        }
        if(departamento.getId()==0){
            imprimirMensaje("Error al guardar", "Seleccione Departamento", FacesMessage.SEVERITY_ERROR);
            return false;
        }
        if(municipio.getId()==0){
            imprimirMensaje("Error al guardar", "Seleccione Municipio", FacesMessage.SEVERITY_ERROR);
            return false;
        }
        if(tipoDocumento.getId()==0){
            imprimirMensaje("Error al guardar", "Seleccione Tipo Documento", FacesMessage.SEVERITY_ERROR);
            return false;
        }
        if(proveedor.getNumeroDocumento().equals("")){
            imprimirMensaje("Error al guardar", "Dígite Número de Identificación", FacesMessage.SEVERITY_ERROR);
            return false;
        }
        if(proveedor.getRazonSocial().equals("")){
            imprimirMensaje("Error al guardar", "Dígite Razón Social", FacesMessage.SEVERITY_ERROR);
            return false;
        }
        if(proveedor.getContacto().equals("")){
            imprimirMensaje("Error al guardar", "Dígite Contacto", FacesMessage.SEVERITY_ERROR);
            return false;
        }
        if(proveedor.getDireccion().equals("")){
            imprimirMensaje("Error al guardar", "Dígite Dirección", FacesMessage.SEVERITY_ERROR);
            return false;
        }
        if(proveedor.getTelefono().equals("")){
            imprimirMensaje("Error al guardar", "Dígite Teléfono", FacesMessage.SEVERITY_ERROR);
            return false;
        }
        
        return true;
    }
    
    public InvProveedores getProveedor() {
        return proveedor;
    }

    public void setProveedor(InvProveedores proveedor) {
        this.proveedor = proveedor;
    }

    public List<InvProveedores> getListaProveedores() {
        return listaProveedores;
    }

    public void setListaProveedores(List<InvProveedores> listaProveedores) {
        this.listaProveedores = listaProveedores;
    }

    public InvProveedores getProveedorSeleccionada() {
        return proveedorSeleccionada;
    }

    public void setProveedorSeleccionada(InvProveedores proveedorSeleccionada) {
        this.proveedorSeleccionada = proveedorSeleccionada;
    }

    public CfgClasificaciones getDepartamento() {
        return departamento;
    }

    public void setDepartamento(CfgClasificaciones departamento) {
        this.departamento = departamento;
    }

    public CfgClasificaciones getMunicipio() {
        return municipio;
    }

    public void setMunicipio(CfgClasificaciones municipio) {
        this.municipio = municipio;
    }

    public CfgClasificaciones getTipoDocumento() {
        return tipoDocumento;
    }

    public void setTipoDocumento(CfgClasificaciones tipoDocumento) {
        this.tipoDocumento = tipoDocumento;
    }

    public List<CfgClasificaciones> getListaDepartamentos() {
        return listaDepartamentos;
    }

    public void setListaDepartamentos(List<CfgClasificaciones> listaDepartamentos) {
        this.listaDepartamentos = listaDepartamentos;
    }

    public List<CfgClasificaciones> getListaMunicipios() {
        return listaMunicipios;
    }

    public void setListaMunicipios(List<CfgClasificaciones> listaMunicipios) {
        this.listaMunicipios = listaMunicipios;
    }

    public List<CfgClasificaciones> getListaTipoDocumentos() {
        return listaTipoDocumentos;
    }

    public void setListaTipoDocumentos(List<CfgClasificaciones> listaTipoDocumentos) {
        this.listaTipoDocumentos = listaTipoDocumentos;
    }

    public List<InvProductos> getListaProductos() {
        return listaProductos;
    }

    public void setListaProductos(List<InvProductos> listaProductos) {
        this.listaProductos = listaProductos;
    }

    public List<InvProveedorProductos> getListaProveedorProductos() {
        return listaProveedorProductos;
    }

    public void setListaProveedorProductos(List<InvProveedorProductos> listaProveedorProductos) {
        this.listaProveedorProductos = listaProveedorProductos;
    }

    public List<InvProveedorProductos> getListaProveedorProductosEliminar() {
        return listaProveedorProductosEliminar;
    }

    public void setListaProveedorProductosEliminar(List<InvProveedorProductos> listaProveedorProductosEliminar) {
        this.listaProveedorProductosEliminar = listaProveedorProductosEliminar;
    }

    public InvProductos getProducto() {
        return producto;
    }

    public void setProducto(InvProductos producto) {
        this.producto = producto;
    }
}
