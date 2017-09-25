/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managedBeans.inventario;

import beans.enumeradores.TipoInventarioEnum;
import beans.utilidades.MetodosGenerales;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import modelo.entidades.CfgEmpresa;
import modelo.entidades.CfgSede;
import modelo.entidades.CfgUsuarios;
import modelo.entidades.InvBodegas;
import modelo.entidades.InvConsecutivos;
import modelo.fachadas.CfgEmpresaFacade;
import modelo.fachadas.CfgSedeFacade;
import modelo.fachadas.CfgUsuariosFacade;
import modelo.fachadas.InvBodegasFacade;
import modelo.fachadas.InvConsecutivosFacade;
import org.primefaces.context.RequestContext;

/**
 *
 * @author miguel
 */
@Named(value = "bodegaMB")
@ViewScoped
public class BodegaMB extends MetodosGenerales implements java.io.Serializable{

    @EJB
    private InvBodegasFacade bodegaFachada;
    @EJB
    private CfgSedeFacade sedeFachada;
    @EJB
    private CfgUsuariosFacade usuarioFachada;
    @EJB
    private CfgEmpresaFacade empresaFacade;
    @EJB
    private InvConsecutivosFacade consecutivoFacade;
    
    private InvBodegas  bodega;
    private InvBodegas  bodegaSeleccionada;
    private CfgSede     sede;
    private CfgUsuarios usuario;        
    private CfgEmpresa empresaActual;
    private InvBodegas bodegaPrincipal;
    private InvConsecutivos consecutivo;
    
    private List<CfgSede>       listaSedes;
    private List<CfgUsuarios>   listaUsuarios;
    private List<InvBodegas>    listaBodegas;
    
    private boolean renderPrincipal;
    public BodegaMB() {
        this.bodega = new InvBodegas();
        this.renderPrincipal = true;
        this.sede = new CfgSede();
        this.usuario = new CfgUsuarios();
    }
    
    @PostConstruct
    public void init(){
        this.usuario = new CfgUsuarios();
        this.bodega = new InvBodegas();
        this.bodega.setActivo(true);
        this.bodega.setTipo(1);
        this.empresaActual =  empresaFacade.find(1);
        this.bodega.setIdEmpresa(empresaActual);
        this.sede = new CfgSede();
        this.renderPrincipal = true;
        this.listaSedes = sedeFachada.buscarOrdenado();
        this.listaUsuarios = usuarioFachada.buscarOrdenado();
        bodegaPrincipal = bodegaFachada.bodegaPrincipal(empresaActual.getCodEmpresa(),bodega.getTipo());
        this.renderPrincipal = bodegaPrincipal == null;
        listaBodegas = bodegaFachada.bodegaEmpresas(empresaActual.getCodEmpresa());
        this.consecutivo = consecutivoFacade.getConsecutivoTipo(TipoInventarioEnum.B.toString());
        int cod = consecutivo.getConsecutivo()+1;
        bodega.setCodigoBodega(String.format("%04d",cod));
    }
    
    public void nuevo(){
        this.usuario = new CfgUsuarios();
        this.bodega = new InvBodegas();
        this.bodega.setActivo(true);
        this.bodega.setTipo(1);
        this.empresaActual =  empresaFacade.find(1);
        this.bodega.setIdEmpresa(empresaActual);
        this.sede = new CfgSede();
        this.renderPrincipal = true;
        this.listaSedes = sedeFachada.buscarOrdenado();
        this.listaUsuarios = usuarioFachada.buscarOrdenado();
        bodegaPrincipal = bodegaFachada.bodegaPrincipal(empresaActual.getCodEmpresa(),bodega.getTipo());
        this.renderPrincipal = bodegaPrincipal == null;
        listaBodegas = bodegaFachada.bodegaEmpresas(empresaActual.getCodEmpresa());
        this.consecutivo = consecutivoFacade.getConsecutivoTipo(TipoInventarioEnum.B.toString());
        int cod = consecutivo.getConsecutivo()+1;
        bodega.setCodigoBodega(String.format("%04d",cod));
    }
    public void buscar(){
        bodega = bodegaSeleccionada;
        if(bodega!=null){
            sede = bodega.getIdSede();
            usuario = bodega.getResponsable();
        }
        RequestContext.getCurrentInstance().update("IdFormBodegas:opFormulario");
        RequestContext.getCurrentInstance().update("IdFormBodegas:opBotones");
    }
    public void guardar(){
        if(validar()){
            try {
                if(bodegaPrincipal!=null)bodega.setIdBodegaPadre(bodegaPrincipal.getIdBodega());
                bodega.setIdSede(sede);
                bodega.setResponsable(usuario);
                bodega.setActivo(true);
                if(bodega.getIdBodega()==null){
                    bodegaFachada.create(bodega);
                }else{
                    bodega.setResponsable(usuarioFachada.find(bodega.getResponsable().getIdUsuario()));
                    bodegaFachada.edit(bodega);
                }
                consecutivo.setConsecutivo(consecutivo.getConsecutivo()+1);
                consecutivoFacade.edit(consecutivo);
                nuevo();
                imprimirMensaje("Guardado", "Guardado Correctamente", FacesMessage.SEVERITY_INFO);
                RequestContext.getCurrentInstance().update("IdTablaBodegas");
                //Guardamos consecutivo
                
            } catch (Exception e) {
                imprimirMensaje("Error al guardar", e.getMessage(), FacesMessage.SEVERITY_ERROR);
            }
        }
    }
    public void eliminar(){
        if(bodega!=null){
            try {
                bodega.setActivo(false);
                bodegaFachada.edit(bodega);
                imprimirMensaje("Guardado", "Guardado Correctamente", FacesMessage.SEVERITY_INFO);
                nuevo();
            } catch (Exception e) {
            }
        }
    }

    public void validarTipo(){
        bodegaPrincipal = bodegaFachada.bodegaPrincipal(empresaActual.getCodEmpresa(),bodega.getTipo());
        this.renderPrincipal = bodegaPrincipal == null;
        listaBodegas = bodegaFachada.bodegaEmpresas(empresaActual.getCodEmpresa());
        this.consecutivo = consecutivoFacade.getConsecutivoTipo(bodega.getTipo()==1?TipoInventarioEnum.B.toString():TipoInventarioEnum.F.toString());
        int cod = consecutivo.getConsecutivo()+1;
        bodega.setCodigoBodega(String.format("%04d",cod));
    }
    private boolean validar(){
        if(sede.getIdSede()==0){
            imprimirMensaje("Error al Guardar", "Seleccione Sede", FacesMessage.SEVERITY_ERROR);
            return false;
        }
        if(bodega.getCodigoBodega().equals("")){
            imprimirMensaje("Error al Guardar", "Digite Código Bodega", FacesMessage.SEVERITY_ERROR);
            return false;
        }
        if(bodega.getNombre().equals("")){
            imprimirMensaje("Error al Guardar", "Digite Nombre Bodega", FacesMessage.SEVERITY_ERROR);
            return false;
        }
        if(usuario.getIdUsuario()==0){
            imprimirMensaje("Error al Guardar", "Seleccione Responsable", FacesMessage.SEVERITY_ERROR);
            return false;
        }
        return true;
    }
    public InvBodegas getBodega() {
        return bodega;
    }

    public void setBodega(InvBodegas bodega) {
        this.bodega = bodega;
    }

    public boolean isRenderPrincipal() {
        return renderPrincipal;
    }

    public void setRenderPrincipal(boolean renderPrincipal) {
        this.renderPrincipal = renderPrincipal;
    }

    public CfgSede getSede() {
        return sede;
    }

    public void setSede(CfgSede sede) {
        this.sede = sede;
    }

    public CfgUsuarios getUsuario() {
        return usuario;
    }

    public void setUsuario(CfgUsuarios usuario) {
        this.usuario = usuario;
    }

    public List<CfgSede> getListaSedes() {
        return listaSedes;
    }

    public void setListaSedes(List<CfgSede> listaSedes) {
        this.listaSedes = listaSedes;
    }

    public List<CfgUsuarios> getListaUsuarios() {
        return listaUsuarios;
    }

    public void setListaUsuarios(List<CfgUsuarios> listaUsuarios) {
        this.listaUsuarios = listaUsuarios;
    }

    public CfgEmpresa getEmpresaActual() {
        return empresaActual;
    }

    public void setEmpresaActual(CfgEmpresa empresaActual) {
        this.empresaActual = empresaActual;
    }

    public List<InvBodegas> getListaBodegas() {
        return listaBodegas;
    }

    public void setListaBodegas(List<InvBodegas> listaBodegas) {
        this.listaBodegas = listaBodegas;
    }

    public InvBodegas getBodegaSeleccionada() {
        return bodegaSeleccionada;
    }

    public void setBodegaSeleccionada(InvBodegas bodegaSeleccionada) {
        this.bodegaSeleccionada = bodegaSeleccionada;
    }
    
}
