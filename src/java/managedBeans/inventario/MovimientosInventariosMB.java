/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managedBeans.inventario;

import beans.enumeradores.TipoInventarioEnum;
import beans.utilidades.MetodosGenerales;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import javax.mail.Authenticator;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.servlet.ServletContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import managedBeans.seguridad.LoginMB;
import modelo.entidades.CfgCorreo;
import modelo.entidades.InvBodegaProductos;
import modelo.entidades.InvBodegas;
import modelo.entidades.InvConsecutivos;
import modelo.entidades.InvLotes;
import modelo.entidades.InvMovimientoProductos;
import modelo.entidades.InvMovimientos;
import modelo.entidades.InvOrdenCompra;
import modelo.fachadas.CfgCorreoFacade;
import modelo.fachadas.DBConnector;
import modelo.fachadas.InvBodegaProductosFacade;
import modelo.fachadas.InvBodegasFacade;
import modelo.fachadas.InvConsecutivosFacade;
import modelo.fachadas.InvLotesFacade;
import modelo.fachadas.InvMovimientoProductosFacade;
import modelo.fachadas.InvMovimientosFacade;
import modelo.fachadas.InvOrdenCompraFacade;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRLoader;
import org.primefaces.context.RequestContext;
import org.primefaces.event.CellEditEvent;

/**
 *
 * @author miguel
 */
@Named(value = "movimientosInventariosMB")
@ViewScoped
public class MovimientosInventariosMB extends MetodosGenerales implements java.io.Serializable{

    @EJB
    private InvBodegasFacade bodegaFachada;
    @EJB
    private InvConsecutivosFacade consecutivoFacade;
    @EJB
    private InvBodegaProductosFacade bodegaProductosFacade;
    @EJB
    private InvMovimientosFacade movimientoFacade;
    @EJB
    private InvMovimientoProductosFacade movimientoProductoFacade;
    @EJB
    private CfgCorreoFacade correoFacade;
    @EJB
    private InvOrdenCompraFacade ordenCompraFacade;
    @EJB
    private InvLotesFacade loteFachada;
    
    private InvBodegas bodegaOrigen;
    private InvBodegas bodegaDestino;
    private InvMovimientos movimiento;
    private InvMovimientos movimientoSeleccionado;
    private InvMovimientos movimientoEntrada;
    private InvConsecutivos consecutivo;
    private InvBodegaProductos bodegaProducto;
    private InvOrdenCompra ordenCompra;
    private CfgCorreo correo;
    
    private List<InvBodegas> listaBodegas;
    private List<InvBodegas> listaBodegasDestinos;
    private List<InvBodegaProductos> listaProductosBodegas;
    private List<InvMovimientoProductos> listaMovimientosProductos;
    private List<InvMovimientos> listaMovimientos;
    private List<InvLotes> listaLote;
    private List<SelectItem> listaProcesos;
    
    
    private boolean renderBotones;
    private boolean renderBotonGuardar;
    private boolean renderBotonReporte;
    private boolean renderForm;
    private boolean renderTitulo;
    //Entrada
    private boolean renderEntradaTraslado;
    private boolean renderEntradaTrasladoB;
    private boolean renderEntradaSaldoInicial;
    private boolean renderEntradaAjustePositivo;
    private boolean renderEntradaDevolucionCompra;
    private boolean renderEntradaDevolucionCompraB;
    //Salida
    private boolean renderSalidaTraslados;
    private boolean renderSalidaTrasladosAjusteNegativo;
    private boolean renderSalidaTrasladosDevolucionCompra;
    private boolean renderSalidaTrasladosDevolucionCompraB;
    private boolean renderProcesos;
    private String tipoMovimiento;
    private String observaciones;
    private LoginMB loginMB;
    private String asunto;
    private String mensaje;
    private String email;
    private String nroSalida;
    private String nroOrden;
    private String numeroDocumento;
    private Date fechaMovimiento;
    private int tipoProceso;
    private int idLote;
    
    public MovimientosInventariosMB() {
        loginMB = FacesContext.getCurrentInstance().getApplication().evaluateExpressionGet(FacesContext.getCurrentInstance(), "#{loginMB}", LoginMB.class);
        
    }
    
    @PostConstruct
    public void init(){
        tipoMovimiento = "0";
        movimiento = new InvMovimientos();
        movimientoEntrada = new InvMovimientos();
        bodegaOrigen = new InvBodegas();
        bodegaDestino = new InvBodegas();
        listaBodegas = new ArrayList<>();
        listaBodegasDestinos = new ArrayList<>();
        listaProductosBodegas = new ArrayList<>();
        listaMovimientosProductos = new ArrayList<>();
        listaMovimientos = new ArrayList<>();
        listaProcesos = new ArrayList<>();
        //cargamos listas de bodegas activas por empresa
        listaBodegas = bodegaFachada.bodegaActivasEmpresas(loginMB.getEmpresaActual().getCodEmpresa());
        correo = correoFacade.find(1);
        fechaMovimiento = new Date();
        //Cargamos la bodega asignada al usuario logueado
        bodegaOrigen = bodegaFachada.bodegaUsuarioReponsable(loginMB.getUsuarioActual().getIdUsuario());
        if(bodegaOrigen==null){
            imprimirMensaje("Iniciando", "Usuario Actual no este responsable de bodega", FacesMessage.SEVERITY_WARN);
        }
        //cargamos los lotes creados por la empresa
        listaLote = loteFachada.getLotesSinVencer(loginMB.getEmpresaActual().getCodEmpresa());
        initRender();
        idLote= 0;
    }
    
    public void nuevo(){
        tipoProceso=0;
        renderTitulo=false;
        tipoMovimiento = "0";
        movimiento = new InvMovimientos();
        bodegaDestino = new InvBodegas();
        bodegaOrigen = new InvBodegas();
        listaBodegas = new ArrayList<>();
        listaBodegasDestinos = new ArrayList<>();
        listaProductosBodegas = new ArrayList<>();
        listaMovimientosProductos = new ArrayList<>();
        listaProcesos = new ArrayList<>();
        renderProcesos = false;
        observaciones = "";
        nroSalida="";
        listaBodegas = bodegaFachada.bodegaActivasEmpresas(loginMB.getEmpresaActual().getCodEmpresa());
        correo = correoFacade.find(1);
        initRender();
    }
    public void buscar(){
        if(movimiento!=null){
            movimientoSeleccionado = movimiento;
            renderBotonGuardar = false;
            renderBotonReporte = true;
            listaMovimientosProductos = movimiento.getInvMovimientoProductosList();
            observaciones = movimiento.getObservaciones();
            bodegaOrigen = movimiento.getIdBodegaOrigen();
            listaBodegasDestinos = bodegaFachada.bodegaActivasEmpresasDiferenteOrigen(loginMB.getEmpresaActual().getCodEmpresa(), bodegaOrigen.getIdBodega());
            bodegaDestino = movimiento.getIdBodegaDestino();
            if(movimiento.getTipoMovimiento().equals("S"))asunto = "Salida "+movimiento.getNumeroDocumento();
        }
    }
    public void guardar(){
        if(validarDatos()){
            if(tipoMovimiento.equals("S")){
                switch(tipoProceso){
                    case 1:
                        guardarSalida();
                    break;
                    case 6:
                        guardarSalidaAjusteNegativo();
                    break;
                    case 7:
                        guardarSalidaDevolucionCompra();
                    break;
                }
                
            }else if(tipoMovimiento.equals("E")){
                //1. Traslado
                switch(tipoProceso){
                    case 1:
                        guardarTrasladoEntrada();
                        break;
                    case 2:
                        guardarSaldoInicial();
                        break;
                    case 3:
                        guardarEntradaAjustePostivo();
                        break;
                }
            }
        }
    }
    public void eliminar(InvMovimientos mov){
        try {
            if(mov.getEstado().equals("P")){
                mov.setEstado(TipoInventarioEnum.N.toString());
                movimientoFacade.edit(mov);
            }
            imprimirMensaje("Guardado", "Anulado correctamente", FacesMessage.SEVERITY_INFO);
            nuevo();
        } catch (Exception e) {
        }
    }
    private void initRender(){
        this.renderBotonGuardar = false;
        this.renderBotonReporte = false;
        this.renderBotones = false;
        this.renderEntradaAjustePositivo = false;
        this.renderEntradaDevolucionCompra =false;
        this.renderEntradaDevolucionCompraB = false;
        this.renderEntradaSaldoInicial = false;
        this.renderEntradaTraslado  =false;
        this.renderEntradaTrasladoB = false;
        this.renderForm = false;
        this.renderSalidaTraslados = false;
        this.renderSalidaTrasladosAjusteNegativo = false;
        this.renderSalidaTrasladosDevolucionCompra = false;
        this.renderTitulo = false;
        this.renderSalidaTrasladosDevolucionCompra = false;
        this.renderSalidaTrasladosDevolucionCompraB = false;
        this.idLote = 0;
    }
    private void guardarEntradaAjustePostivo(){
        try {
            //actualizamos saldo inicial
            movimiento = new InvMovimientos();
            movimiento.setFechaMovimiento(new Date());
            movimiento.setObservaciones(observaciones);
            movimiento.setIdEmpresa(loginMB.getEmpresaActual());
            movimiento.setIdUsuarioCrea(loginMB.getUsuarioActual());
            movimiento.setTipoMovimiento(TipoInventarioEnum.E.toString());
            movimiento.setTipoProceso(3);
            movimiento.setEstado(TipoInventarioEnum.C.toString());
            movimiento.setUsuarioAprueba(loginMB.getUsuarioActual());
            movimiento.setFechaAprobacion(new Date());
            movimiento.setIdBodegaOrigen(bodegaOrigen);
            movimiento.setInvMovimientoProductosList(new ArrayList<InvMovimientoProductos>());
            for(InvMovimientoProductos pr : listaMovimientosProductos){
                InvMovimientoProductos mov= new InvMovimientoProductos();
                mov.setIdProducto(pr.getIdProducto());
                mov.setIdMovimiento(movimiento);
                mov.setExistencia(pr.getExistencia());
                mov.setCantidadSolicitada(pr.getCantidadSolicitada());
                mov.setCantidadRecibida(pr.getCantidadSolicitada());
                movimiento.getInvMovimientoProductosList().add(mov);
            }
            
            
            int cod = consecutivo.getConsecutivo()+1;
            movimiento.setNumeroDocumento(String.format("%04d",cod));
            movimientoFacade.edit(movimiento);
            consecutivo.setConsecutivo(consecutivo.getConsecutivo()+1);
            consecutivoFacade.edit(consecutivo);
            //aumentamos valores de traslados de la bodega principal
            for(InvMovimientoProductos pr:movimiento.getInvMovimientoProductosList()){
                bodegaProducto = bodegaProductosFacade.getBodegaProductoLote(movimiento.getIdBodegaOrigen().getIdBodega(), pr.getIdProducto().getIdProducto(),idLote);
                bodegaProducto.setExistencia(pr.getExistencia()+pr.getCantidadRecibida());
                bodegaProductosFacade.edit(bodegaProducto);
            }
            
            imprimirMensaje("Guardado", "Guardado Correctamente", FacesMessage.SEVERITY_INFO);
            RequestContext.getCurrentInstance().update("IdFormMovimientosInventario:opFormulario");
            RequestContext.getCurrentInstance().update("IdFormMovimientosInventario:opProductos");
            movimientoSeleccionado = movimiento;
            nuevo();
        } catch (Exception e) {
            imprimirMensaje("Error al guardar", e.getLocalizedMessage(), FacesMessage.SEVERITY_ERROR);
            Logger.getLogger(MovimientosInventariosMB.class.getName()).log(Level.SEVERE, null, e);
        }
    }
    private void guardarSaldoInicial(){
        try {
            //actualizamos saldo inicial
            movimiento.setFechaMovimiento(new Date());
            movimiento.setObservaciones(observaciones);
            movimiento.setIdEmpresa(loginMB.getEmpresaActual());
            movimiento.setIdUsuarioCrea(loginMB.getUsuarioActual());
            movimiento.setTipoMovimiento(TipoInventarioEnum.E.toString());
            movimiento.setTipoProceso(2);
            movimiento.setEstado(TipoInventarioEnum.C.toString());
            movimiento.setUsuarioAprueba(loginMB.getUsuarioActual());
            movimiento.setFechaAprobacion(new Date());
            movimiento.setIdBodegaOrigen(bodegaOrigen);
            movimiento.setInvMovimientoProductosList(new ArrayList<InvMovimientoProductos>());
            for(InvMovimientoProductos pr : listaMovimientosProductos){
                InvMovimientoProductos mov= new InvMovimientoProductos();
                mov.setIdProducto(pr.getIdProducto());
                mov.setIdMovimiento(movimiento);
                mov.setExistencia(pr.getExistencia());
                mov.setCantidadSolicitada(pr.getCantidadSolicitada());
                mov.setCantidadRecibida(pr.getCantidadSolicitada());
                movimiento.getInvMovimientoProductosList().add(mov);
            }
            
            
            int cod = consecutivo.getConsecutivo()+1;
            movimiento.setNumeroDocumento(String.format("%04d",cod));
            movimientoFacade.edit(movimiento);
            consecutivo.setConsecutivo(consecutivo.getConsecutivo()+1);
            consecutivoFacade.edit(consecutivo);
            //aumentamos valores de traslados de la bodega principal
            for(InvMovimientoProductos pr:movimiento.getInvMovimientoProductosList()){
                bodegaProducto = bodegaProductosFacade.getBodegaProductoLote(movimiento.getIdBodegaOrigen().getIdBodega(), pr.getIdProducto().getIdProducto(),idLote);
                bodegaProducto.setExistencia(pr.getCantidadSolicitada());
                bodegaProductosFacade.edit(bodegaProducto);
            }
            
            imprimirMensaje("Guardado", "Guardado Correctamente", FacesMessage.SEVERITY_INFO);
            movimientoSeleccionado = movimiento;
            nuevo();
        } catch (Exception e) {
            imprimirMensaje("Error al guardar", e.getLocalizedMessage(), FacesMessage.SEVERITY_ERROR);
            Logger.getLogger(MovimientosInventariosMB.class.getName()).log(Level.SEVERE, null, e);
        }
    }
    private void guardarTrasladoEntrada(){
        try {
            if(idLote!=0){
                //actualizamos la orden de salida
                movimiento.setTipoProceso(1);
                movimiento.setEstado(TipoInventarioEnum.C.toString());
                movimiento.setUsuarioAprueba(loginMB.getUsuarioActual());
                movimiento.setFechaAprobacion(new Date());
                movimiento.setIdMovimientoOrigen(movimientoSeleccionado!=null?movimientoSeleccionado.getIdMovimiento():0);
                movimientoFacade.edit(movimiento);
                //actualizamos y cargamos
                movimientoEntrada.setEstado(TipoInventarioEnum.C.toString());
                this.consecutivo = consecutivoFacade.getConsecutivoTipo(tipoMovimiento);
                int cod = consecutivo.getConsecutivo()+1;
                movimientoEntrada.setNumeroDocumento(String.format("%04d",cod));
                movimientoEntrada.setObservaciones(observaciones);
                consecutivo.setConsecutivo(consecutivo.getConsecutivo()+1);
                consecutivoFacade.edit(consecutivo);
                movimientoEntrada.setInvMovimientoProductosList(new ArrayList<InvMovimientoProductos>());
                for(InvMovimientoProductos pr : listaMovimientosProductos){
                    movimientoProductoFacade.edit(pr);
                    InvMovimientoProductos mov= new InvMovimientoProductos();
                    mov.setIdProducto(pr.getIdProducto());
                    mov.setIdMovimiento(movimientoEntrada);
                    mov.setCantidadSolicitada(pr.getCantidadSolicitada());
                    mov.setCantidadRecibida(pr.getCantidadRecibida());
                    movimientoEntrada.getInvMovimientoProductosList().add(mov);
                }
                movimientoEntrada.setIdBodegaOrigen(movimiento.getIdBodegaOrigen());
                movimientoEntrada.setIdBodegaDestino(movimiento.getIdBodegaDestino());
                movimientoEntrada.setUsuarioAprueba(loginMB.getUsuarioActual());
                movimientoEntrada.setFechaAprobacion(new Date());
                movimientoEntrada.setIdUsuarioCrea(loginMB.getUsuarioActual());
                movimientoEntrada.setIdEmpresa(loginMB.getEmpresaActual());
                movimientoFacade.create(movimientoEntrada);

                //aumentamos valores de traslados de la bodega principal
                for(InvMovimientoProductos pr:movimientoEntrada.getInvMovimientoProductosList()){
                    InvBodegaProductos bodegaProductos = bodegaProductosFacade.getBodegaProductoLote(movimiento.getIdBodegaDestino().getIdBodega(), pr.getIdProducto().getIdProducto(),idLote);
                    if(bodegaProductos==null){//Creamos el producto
                        bodegaProductos = new InvBodegaProductos();
                        bodegaProductos.setIdBodega(movimiento.getIdBodegaDestino());
                        bodegaProductos.setIdProducto(pr.getIdProducto());
                        bodegaProductos.setExistencia(pr.getCantidadRecibida());
                        bodegaProductos.setIdLote(new InvLotes(idLote));
                        bodegaProductosFacade.create(bodegaProductos);
                    }else{
                        bodegaProductos.setExistencia(bodegaProductos.getExistencia()+pr.getCantidadRecibida());
                        bodegaProductosFacade.edit(bodegaProductos);
                    }
                }
                imprimirMensaje("Guardado", "Guardado Correctamente", FacesMessage.SEVERITY_INFO);
                renderBotonGuardar=false;
                RequestContext.getCurrentInstance().update("IdFormMovimientosInventario:opFormulario");
                RequestContext.getCurrentInstance().update("IdFormMovimientosInventario:opProductos");
                movimientoSeleccionado = movimiento;
            }else{
                imprimirMensaje("Error al guardar", "Seleccione Lote", FacesMessage.SEVERITY_ERROR);
            }
        } catch (Exception e) {
        }
    }
    private void guardarSalida(){
        try {
            movimiento.setEstado(TipoInventarioEnum.P.toString());
            this.consecutivo = consecutivoFacade.getConsecutivoTipo(tipoMovimiento);
            int cod = consecutivo.getConsecutivo()+1;
            movimiento.setNumeroDocumento(String.format("%04d",cod));
            movimiento.setObservaciones(observaciones);
            consecutivo.setConsecutivo(consecutivo.getConsecutivo()+1);
            consecutivoFacade.edit(consecutivo);
            movimiento.setIdBodegaOrigen(bodegaOrigen);
            movimiento.setIdBodegaDestino(bodegaDestino);
            movimiento.setInvMovimientoProductosList(listaMovimientosProductos);
            movimiento.setIdUsuarioCrea(loginMB.getUsuarioActual());
            movimiento.setIdEmpresa(loginMB.getEmpresaActual());
            movimientoFacade.create(movimiento);
            renderBotonGuardar = false;
            renderBotonReporte = true;
            
            //disminuir valores de traslados de la bodega principal
            for(InvMovimientoProductos productos:listaMovimientosProductos){
                InvBodegaProductos productoBodega = bodegaProductosFacade.getBodegaProductoLote(bodegaOrigen.getIdBodega(), productos.getIdProducto().getIdProducto(),idLote);
                productoBodega.setExistencia(productoBodega.getExistencia()-productos.getCantidadSolicitada());
                bodegaProductosFacade.edit(productoBodega);
            }
            imprimirMensaje("Guardado", "Guardado Correctamente", FacesMessage.SEVERITY_INFO);
            asunto="Salida "+movimiento.getNumeroDocumento();
            movimientoSeleccionado = movimiento;
        } catch (Exception e) {
            imprimirMensaje("Error al guardar", e.getLocalizedMessage(), FacesMessage.SEVERITY_ERROR);
            Logger.getLogger(MovimientosInventariosMB.class.getName()).log(Level.SEVERE, null, e);
        }
    }
    private void guardarSalidaAjusteNegativo(){
        try {
            //actualizamos saldo inicial
            movimiento = new InvMovimientos();
            movimiento.setFechaMovimiento(new Date());
            movimiento.setObservaciones(observaciones);
            movimiento.setIdEmpresa(loginMB.getEmpresaActual());
            movimiento.setIdUsuarioCrea(loginMB.getUsuarioActual());
            movimiento.setTipoMovimiento(TipoInventarioEnum.S.toString());
            movimiento.setTipoProceso(6);
            movimiento.setEstado(TipoInventarioEnum.C.toString());
            movimiento.setUsuarioAprueba(loginMB.getUsuarioActual());
            movimiento.setFechaAprobacion(new Date());
            movimiento.setIdBodegaOrigen(bodegaOrigen);
            movimiento.setInvMovimientoProductosList(new ArrayList<InvMovimientoProductos>());
            for(InvMovimientoProductos pr : listaMovimientosProductos){
                InvMovimientoProductos mov= new InvMovimientoProductos();
                mov.setIdProducto(pr.getIdProducto());
                mov.setIdMovimiento(movimiento);
                mov.setExistencia(pr.getExistencia());
                mov.setCantidadSolicitada(pr.getCantidadSolicitada());
                mov.setCantidadRecibida(pr.getCantidadSolicitada());
                movimiento.getInvMovimientoProductosList().add(mov);
            }
            
            
            int cod = consecutivo.getConsecutivo()+1;
            movimiento.setNumeroDocumento(String.format("%04d",cod));
            movimientoFacade.edit(movimiento);
            consecutivo.setConsecutivo(consecutivo.getConsecutivo()+1);
            consecutivoFacade.edit(consecutivo);
            //aumentamos valores de traslados de la bodega principal
            for(InvMovimientoProductos pr:movimiento.getInvMovimientoProductosList()){
                bodegaProducto = bodegaProductosFacade.getBodegaProductoLote(movimiento.getIdBodegaOrigen().getIdBodega(), pr.getIdProducto().getIdProducto(),idLote);
                bodegaProducto.setExistencia(pr.getExistencia()-pr.getCantidadRecibida());
                bodegaProductosFacade.edit(bodegaProducto);
            }
            
            imprimirMensaje("Guardado", "Guardado Correctamente", FacesMessage.SEVERITY_INFO);
            RequestContext.getCurrentInstance().update("IdFormMovimientosInventario:opFormulario");
            RequestContext.getCurrentInstance().update("IdFormMovimientosInventario:opProductos");
            movimientoSeleccionado = movimiento;
            nuevo();
        } catch (Exception e) {
            imprimirMensaje("Error al guardar", e.getLocalizedMessage(), FacesMessage.SEVERITY_ERROR);
            Logger.getLogger(MovimientosInventariosMB.class.getName()).log(Level.SEVERE, null, e);
        }
    }
    public void guardarSalidaDevolucionCompra(){
        try {
            movimiento = new InvMovimientos();
            movimiento.setFechaMovimiento(new Date());
            movimiento.setObservaciones(observaciones);
            movimiento.setIdEmpresa(loginMB.getEmpresaActual());
            movimiento.setIdUsuarioCrea(loginMB.getUsuarioActual());
            movimiento.setTipoMovimiento(TipoInventarioEnum.S.toString());
            movimiento.setTipoProceso(7);
            movimiento.setEstado(TipoInventarioEnum.C.toString());
            movimiento.setUsuarioAprueba(loginMB.getUsuarioActual());
            movimiento.setFechaAprobacion(new Date());
            if (movimientoSeleccionado.getIdOrdenCompra() == null) {
                movimiento.setIdBodegaOrigen(movimientoSeleccionado.getIdBodegaDestino());
            }
            movimiento.setIdBodegaDestino(movimientoSeleccionado.getIdBodegaOrigen());
            movimiento.setIdMovimientoOrigen(movimientoSeleccionado!=null?movimientoSeleccionado.getIdMovimiento():null);
            movimiento.setInvMovimientoProductosList(new ArrayList<InvMovimientoProductos>());
            for(InvMovimientoProductos m:listaMovimientosProductos){
                if (movimientoSeleccionado.getIdOrdenCompra() == null) {
                    InvBodegaProductos pro = bodegaProductosFacade.getBodegaProductoLote(bodegaOrigen.getIdBodega(), m.getIdProducto().getIdProducto(),idLote);
                    if (pro != null) {

                        //sumamos la existencia a la bodega de donde provino la salida
                        InvMovimientoProductos movProducto = new InvMovimientoProductos();
                        movProducto.setIdMovimiento(movimiento);
                        movProducto.setIdProducto(m.getIdProducto());
                        movProducto.setCantidadSolicitada(m.getCantidadSolicitada());
                        movProducto.setCantidadRecibida(m.getCantidadDevolver());
                        movProducto.setExistencia(pro.getExistencia());
                        movimiento.getInvMovimientoProductosList().add(movProducto);
                        pro.setExistencia(pro.getExistencia() + m.getCantidadDevolver());
                        bodegaProductosFacade.edit(pro);
                    }

                    //resta la existencia a la bodega de donde provino la salida
                    InvBodegaProductos proSalida = bodegaProductosFacade.getBodegaProductoLote(bodegaDestino.getIdBodega(), m.getIdProducto().getIdProducto(),idLote);
                    if (proSalida != null) {
                        proSalida.setExistencia(proSalida.getExistencia() - m.getCantidadDevolver());
                        bodegaProductosFacade.edit(proSalida);
                    }
                }else{
                    //resta la existencia a la bodega de donde provino la salida
                    InvBodegaProductos proSalida = bodegaProductosFacade.getBodegaProductoLote(bodegaDestino.getIdBodega(), m.getIdProducto().getIdProducto(),idLote);
                    if (proSalida != null) {
                        proSalida.setExistencia(proSalida.getExistencia() - m.getCantidadDevolver());
                        bodegaProductosFacade.edit(proSalida);
                    }
                }
            }
            int cod = consecutivo.getConsecutivo()+1;
            movimiento.setNumeroDocumento(String.format("%04d",cod));
            consecutivo.setConsecutivo(consecutivo.getConsecutivo()+1);
            consecutivoFacade.edit(consecutivo);
            movimientoFacade.create(movimiento);
            imprimirMensaje("Guardado", "Guardado Correctamente", FacesMessage.SEVERITY_INFO);
            RequestContext.getCurrentInstance().update("IdFormMovimientosInventario:opFormulario");
            RequestContext.getCurrentInstance().update("IdFormMovimientosInventario:opProductos");
            nuevo();
        } catch (Exception e) {
        }
        
    }
    public void imprimir(){
        FacesContext facesContext = FacesContext.getCurrentInstance();
        HttpServletResponse httpServletResponse = (HttpServletResponse) facesContext.getExternalContext().getResponse();
        try (ServletOutputStream servletOutputStream = httpServletResponse.getOutputStream()) {
            httpServletResponse.setContentType("application/pdf");
            ServletContext servletContext = (ServletContext) facesContext.getExternalContext().getContext();
            String ruta ;
                ruta = servletContext.getRealPath("/inventario/reportes/r_salida.jasper");
            Map<String, Object> parametros = new HashMap<>();
            if(movimientoSeleccionado.getIdMovimiento()!=null)parametros.put("ID_MOVIMIENTO", movimientoSeleccionado.getIdMovimiento());
            try{
                
                try (Connection con = DBConnector.getInstance().getConnection()) {
                    JasperPrint jasperPrint = JasperFillManager.fillReport(ruta, parametros, con);
                    JasperExportManager.exportReportToPdfStream(jasperPrint, servletOutputStream);
                    FacesContext.getCurrentInstance().responseComplete();
                }
                DBConnector.getInstance().closeConnection();
            }catch(Exception e){
                Logger.getLogger(OrdenCompraMB.class.getName()).log(Level.SEVERE, null, e);
            }
        } catch (IOException ex) {
            Logger.getLogger(OrdenCompraMB.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void enviarEmail(){
        try {
            if(validarEmail()){
                JasperReport jasperReport;
                JasperPrint jasperPrint;
                Connection con = DBConnector.getInstance().getConnection();
                FacesContext facesContext = FacesContext.getCurrentInstance();
                ServletContext servletContext = (ServletContext) facesContext.getExternalContext().getContext();
                File in = new File(servletContext.getRealPath("/inventario/reportes/r_salida.jasper"));
                Map<String, Object> parametros = new HashMap<>();
                if (movimientoSeleccionado.getIdMovimiento() != null) {
                    parametros.put("ID_MOVIMIENTO", movimientoSeleccionado.getIdMovimiento());
                }
                jasperReport = (JasperReport) JRLoader.loadObject(in);
                jasperPrint = JasperFillManager.fillReport(jasperReport, parametros, con);
                //se crea el archivo PDF
                String url = servletContext.getRealPath("/inventario/reportes/");
                JasperExportManager.exportReportToPdfFile(jasperPrint, url+ "/reporte_" + movimientoSeleccionado.getNumeroDocumento() + ".pdf");
                Properties props = System.getProperties();
                MimeMultipart multipart = new MimeMultipart();
                props.put("mail.smtp.auth", "true");
                props.put("mail.smtp.starttls.enable", "true");
                props.put("mail.smtp.host", correo.getHost());
                props.put("mail.smtp.ssl.trust", correo.getHost());
                props.put("mail.smtp.port", correo.getPort());

                Authenticator auth = new MyAuthenticator();
                Session smtpSession = Session.getInstance(props, auth);
                smtpSession.setDebug(false);
                MimeMessage message = new MimeMessage(smtpSession);
                message.setFrom(new InternetAddress(correo.getEmail(), asunto));
                message.addRecipient(Message.RecipientType.TO, new InternetAddress(email));
                final String encoding = "UTF-8";
                BodyPart adjunto = new MimeBodyPart();
                adjunto.setDataHandler(new DataHandler(new FileDataSource(url+ "/reporte_" + movimientoSeleccionado.getNumeroDocumento() + ".pdf")));
                String nombreDocumento = (tipoMovimiento.equals("S")?"salida_":"Entrada_") + movimientoSeleccionado.getNumeroDocumento();
                adjunto.setFileName( nombreDocumento+ ".pdf");
                message.setSubject(asunto, encoding);
                BodyPart mbp = new MimeBodyPart();
                mbp.setContent(mensaje, "text/html");
                multipart.addBodyPart(mbp);
                multipart.addBodyPart(adjunto);
                message.setContent(multipart);
                Transport.send(message);
                File pdf = new File(url + "/reporte_" +movimientoSeleccionado.getNumeroDocumento() + ".pdf");
                pdf.delete();
                imprimirMensaje("Enviado", "Enviado Correctamente", FacesMessage.SEVERITY_INFO);
                
            }
        } catch (Exception e) {
            Logger.getLogger(OrdenCompraMB.class.getName()).log(Level.SEVERE, null, e);
        }
    }
    
        private boolean validarEmail(){
        if(email.equals("")){
            imprimirMensaje("Error al enviar", "Dígite email", FacesMessage.SEVERITY_ERROR);
            return false;
        }
        if(asunto.equals("")){
            imprimirMensaje("Error al enviar", "Dígite asunto", FacesMessage.SEVERITY_ERROR);
            return false;
        }
        return true;
    }
    
    private boolean validarDatos(){
        if(tipoMovimiento.equals("S")){
            if(numeroDocumento.equals("")){
                imprimirMensaje("Error al guardar", "Dígite número de documento", FacesMessage.SEVERITY_INFO);
                return false;
            }
            if(tipoProceso==1){
                if(bodegaOrigen.getIdBodega()==0){
                    imprimirMensaje("Error al guardar", "Seleccione Bodega Origen", FacesMessage.SEVERITY_INFO);
                    return false;
                }
                if(bodegaDestino.getIdBodega()==0){
                    imprimirMensaje("Error al guardar", "Seleccione Bodega Destino", FacesMessage.SEVERITY_INFO);
                    return false;
                }
                if(listaMovimientosProductos.isEmpty()){
                    imprimirMensaje("Error al guardar", "Agregue Productos a trasladar", FacesMessage.SEVERITY_INFO);
                    return false;
                }
            }
        }else if(tipoMovimiento.equals("E")){
            if(numeroDocumento.equals("")){
                imprimirMensaje("Error al guardar", "Dígite número de documento", FacesMessage.SEVERITY_INFO);
                return false;
            }
        }
        return true;
    }

    private void cargarProcesosTipoMovimiento(){
        if(tipoMovimiento.equals("E")){
            listaProcesos.clear();
            listaProcesos.add(new SelectItem(1,"Traslados Bodegas"));
            listaProcesos.add(new SelectItem(2,"Saldo Inicial"));
            listaProcesos.add(new SelectItem(3,"Ajuste Positivo"));
            //listaProcesos.add(new SelectItem(4,"Devolución por cliente"));
        }else{
            listaProcesos.clear();
            listaProcesos.add(new SelectItem(1,"Traslados Bodegas"));
            listaProcesos.add(new SelectItem(6,"Ajuste Negativo"));
            listaProcesos.add(new SelectItem(7,"Devoluciones por compra"));
        }
    }
    private void cargarInformacionPorEntrada(String mov){
        if(!mov.equals("0")){
            this.consecutivo = consecutivoFacade.getConsecutivoTipo(mov);
            int cod = consecutivo.getConsecutivo()+1;
            numeroDocumento = String.format("%04d",cod);
            if(mov.equals("S")){
                movimiento.setTipoMovimiento(tipoMovimiento);
                movimiento.setNumeroDocumento(String.format("%04d",cod));
                movimiento.setFechaMovimiento(new Date());
            }else if(mov.equals("E")){
                movimientoEntrada.setTipoMovimiento(mov);
                movimientoEntrada.setNumeroDocumento(String.format("%04d",cod));
                movimientoEntrada.setFechaMovimiento(new Date());
                tipoMovimiento=mov;
            }
            cargarProcesosTipoMovimiento();
            //listaMovimientos = movimientoFacade.getMovimientoTipoEmpresa(loginMB.getEmpresaActual().getCodEmpresa(), tipoMovimiento);
            //RequestContext.getCurrentInstance().update("IdFormMovimientosInventario:IdDialogoMovimientos");
        }
        
        
        
        
    }
    public void validarTipoMovimiento(){
        initRender();
        this.tipoProceso = 0;
        switch (tipoMovimiento) {
        //Salida
            case "S":
                renderProcesos = true;
                break;
        //Entrada
            case "E":
                renderProcesos = true;
                break;
            default:
                nuevo();
                break;
        }
        cargarInformacionPorEntrada(tipoMovimiento);
    }
    private void ajustePositivo(){
        try {
            renderEntradaAjustePositivo = true;
            this.idLote= 0;
            imprimirMensaje("Resultado", "Seleccionar Lote", FacesMessage.SEVERITY_INFO);
        } catch (Exception e) {
        }
    }
    
    public void validarAjustePositivo(){
        try {
            listaMovimientosProductos.clear();
            if(idLote!=0){

                listaProductosBodegas = bodegaProductosFacade.getProductosBodegasXLote(bodegaOrigen.getIdBodega(),idLote);
                if(!listaProductosBodegas.isEmpty()){
                    for(InvBodegaProductos bp:listaProductosBodegas){
                        InvMovimientoProductos mp = new InvMovimientoProductos();
                        mp.setIdMovimiento(movimiento);
                        mp.setExistencia(bp.getExistencia());
                        mp.setCantidadSolicitada(0d);
                        mp.setCantidadRecibida(0d);
                        mp.setTotal(bp.getExistencia());
                        mp.setIdProducto(bp.getIdProducto());
                        listaMovimientosProductos.add(mp);
                    }
                    renderTitulo = true;
                    renderForm = true;
                    renderBotones =true;
                    renderBotonGuardar = true;
                    renderEntradaAjustePositivo =true;
                }else{
                    imprimirMensaje("No hay registros", "No se encontrarón productos en este lote", FacesMessage.SEVERITY_INFO);
                    renderTitulo = false;
                    renderForm = false;
                    renderBotones =false;
                    renderBotonGuardar = false;
                    renderEntradaAjustePositivo =true;
                }
            }else{
                imprimirMensaje("No hay registros", "Seleccione lote", FacesMessage.SEVERITY_INFO);
                renderTitulo = false;
                renderForm = false;
                renderBotones =false;
                renderBotonGuardar = false;
                renderEntradaAjustePositivo =true;
            }
        } catch (Exception e) {
        }
    }
    private void saldoInicial(){
        renderEntradaSaldoInicial = true;
        this.idLote= 0;
        imprimirMensaje("Resultado", "Seleccionar Lote", FacesMessage.SEVERITY_INFO);
    }
    
    public void validarSaldoInicial(){
        try {
            listaMovimientosProductos.clear();
            if(idLote!=0){
                //Cargamos los productos de la bodega de usuario
                listaProductosBodegas = bodegaProductosFacade.getProductosBodegasXLote(bodegaOrigen.getIdBodega(),idLote);
                for(InvBodegaProductos bp:listaProductosBodegas){
                    InvMovimientoProductos mp = new InvMovimientoProductos();
                    mp.setIdMovimiento(movimiento);
                    mp.setExistencia(bp.getExistencia());
                    mp.setCantidadSolicitada(bp.getExistencia());
                    mp.setCantidadRecibida(bp.getExistencia());
                    mp.setIdProducto(bp.getIdProducto());
                    listaMovimientosProductos.add(mp);
                }

                if(!listaMovimientosProductos.isEmpty()){
                    renderEntradaSaldoInicial = true;
                    renderTitulo = true;
                    renderForm = true;
                    renderBotones =true;
                    renderBotonGuardar = true;
                }else{
                    imprimirMensaje("No hay registros", "No se encontrarón productos en este lote", FacesMessage.SEVERITY_INFO);
                    renderEntradaSaldoInicial = true;
                    renderTitulo = false;
                    renderForm = false;
                    renderBotones =false;
                    renderBotonGuardar = false;
                }
            }else{
                imprimirMensaje("No hay registros", "Seleccione Lote", FacesMessage.SEVERITY_INFO);
                renderEntradaSaldoInicial = true;
                renderTitulo = false;
                renderForm = false;
                renderBotones =false;
                renderBotonGuardar = false;
            }
            
        } catch (Exception e) {
        }
    }
    private void ajusteNegativo(){
        try {
            renderSalidaTrasladosAjusteNegativo = true;
            imprimirMensaje("Seleccione", "Seleccione un lote", FacesMessage.SEVERITY_INFO);
        } catch (Exception e) {
        }
    }
    
    public void validarLoteAjusteNegativo(){
        try {
            if(idLote!=0){
                listaProductosBodegas = bodegaProductosFacade.getProductosBodegasXLote(bodegaOrigen.getIdBodega(),idLote);
                if(!listaProductosBodegas.isEmpty()){
                    listaMovimientosProductos.clear();
                    for(InvBodegaProductos bp:listaProductosBodegas){
                        InvMovimientoProductos mp = new InvMovimientoProductos();
                        mp.setIdMovimiento(movimiento);
                        mp.setExistencia(bp.getExistencia());
                        mp.setCantidadSolicitada(0d);
                        mp.setCantidadRecibida(0d);
                        mp.setTotal(bp.getExistencia());
                        mp.setIdProducto(bp.getIdProducto());
                        listaMovimientosProductos.add(mp);
                    }
                    renderTitulo = true;
                    renderForm = true;
                    renderBotones =true;
                    renderBotonGuardar = true;
                    renderEntradaSaldoInicial = false;
                    renderEntradaTraslado = false;
                    renderEntradaAjustePositivo =false;
                    renderEntradaDevolucionCompra=false;
                }else{
                    imprimirMensaje("No hay resultados", "No hay productos en este Lote", FacesMessage.SEVERITY_INFO);
                }
            }else{
                imprimirMensaje("No hay resultados", "Seleccione Lote", FacesMessage.SEVERITY_INFO);
                renderTitulo = false;
                renderForm = false;
                renderBotones =false;
                renderBotonGuardar = false;
                renderEntradaSaldoInicial = false;
                renderEntradaTraslado = false;
                renderEntradaAjustePositivo =false;
                renderEntradaDevolucionCompra=false;
            }
        } catch (Exception e) {
        }
    }
    public void validarTipoProceso(){
        try {
            initRender();
            listaMovimientosProductos.clear();
            if(tipoMovimiento.equals(TipoInventarioEnum.E.toString())){
                //validamos el tipo de proceso
                switch(tipoProceso){
                    case 1://1. Traslados
                        listaMovimientosProductos.clear();
                        renderEntradaTraslado = true;
                    break;
                    case 2://2. Saldo inicial
                        saldoInicial();
                    break;
                    case 3://3. Ajuste Positivo
                        ajustePositivo();
                    break;
                    case 4://4. Devolución Cliente
                        listaMovimientosProductos.clear();
                        renderEntradaDevolucionCompra=true;
                    break;
                    default:
                        nuevo();
                    break;
                }
            }else if(tipoMovimiento.equals(TipoInventarioEnum.S.toString())){
                //validamos el tipo de proceso
                switch(tipoProceso){
                    case 1://1. Traslados
                        renderSalidaTraslados= true;
                        listaBodegasDestinos = bodegaFachada.bodegaActivasEmpresasDiferenteOrigen(loginMB.getEmpresaActual().getCodEmpresa(), bodegaOrigen.getIdBodega());
                        //Cargamos productos Bodegas con existencia mayor a 0
                        //listaProductosBodegas = bodegaProductosFacade.getProductosBodegasExistencia(bodegaOrigen.getIdBodega());
                        imprimirMensaje("Seleccione", "Seleccione lote", FacesMessage.SEVERITY_INFO);
                        RequestContext.getCurrentInstance().update("IdFormMovimientosInventario:listaProductos");
                    break;
                    case 6://6. Ajuste Negativo
                        ajusteNegativo();
                    break;
                    case 7://7. Devoluciones por compra
                        renderSalidaTrasladosDevolucionCompra = true;
                     break;
                }
                
            }else{
                renderSalidaTraslados = false;
                renderEntradaTraslado = false;
                renderProcesos = false;
            }
        } catch (Exception e) {
        }
    }
    
    public void validarProductosBodegaLoteEntrada(){
        if(idLote!=0){
            renderForm = true;
            renderTitulo = true;
            renderBotonGuardar = true;
            renderBotones = true;
        }else{
            renderForm = false;
                renderTitulo = false;
                renderBotonGuardar = false;
                renderBotones = false;
                imprimirMensaje("No hay Registros", "Seleccione lote", FacesMessage.SEVERITY_INFO);
        }
    }
    
    public void validarProductosBodegaLote(){
        
        if(idLote!=0){
            RequestContext.getCurrentInstance().update("IdFormMovimientosInventario:listaProductos");
                renderForm = true;
                renderTitulo = true;
                renderBotonGuardar = true;
                renderBotones = true;
            listaProductosBodegas = bodegaProductosFacade.getProductosBodegasExistenciaXlote(bodegaOrigen.getIdBodega(),idLote);
            if(!listaProductosBodegas.isEmpty()){
                renderForm = true;
                renderTitulo = true;
                renderBotonGuardar = true;
                renderBotones = true;
            }else{
                renderForm = false;
                renderTitulo = false;
                renderBotonGuardar = false;
                renderBotones = false;
                imprimirMensaje("No hay Registros", "No se encontrarón productos en este lote", FacesMessage.SEVERITY_INFO);
            }
            }else{
                renderForm = false;
                renderTitulo = false;
                renderBotonGuardar = false;
                renderBotones = false;
                imprimirMensaje("No hay Registros", "Seleccione lote", FacesMessage.SEVERITY_INFO);
        }
    }
    public void validarBodegaOrigen(){
        //cargamos bodega destinos
        listaBodegasDestinos = bodegaFachada.bodegaActivasEmpresasDiferenteOrigen(loginMB.getEmpresaActual().getCodEmpresa(), bodegaOrigen.getIdBodega());
       
    }
    public void onCellEdit(CellEditEvent event) {
        try{
            int index = event.getRowIndex();
            double dif = listaMovimientosProductos.get(index).getExistencia() - listaMovimientosProductos.get(index).getCantidadSolicitada();
            if(dif<0d){
                imprimirMensaje("Error", "La cantidad no puede ser mayor a la existencia", FacesMessage.SEVERITY_INFO);
                listaMovimientosProductos.get(index).setCantidadSolicitada(1d);
            }
        }catch(Exception e){
            
        }
    }
    public void onCellEditAjustePositivo(CellEditEvent event) {
        try{
            int index = event.getRowIndex();
            double dif = listaMovimientosProductos.get(index).getExistencia() + listaMovimientosProductos.get(index).getCantidadSolicitada();
            listaMovimientosProductos.get(index).setTotal(dif);
            if(dif<0d){
                listaMovimientosProductos.get(index).setCantidadSolicitada(listaMovimientosProductos.get(index).getExistencia());
                listaMovimientosProductos.get(index).setTotal(listaMovimientosProductos.get(index).getExistencia());
            }
            
            RequestContext.getCurrentInstance().update("IdFormMovimientosInventario:opProductos");
        }catch(Exception e){
            
        }
    }
    public void onCellEditAjusteNegativo(CellEditEvent event) {
        try{
            int index = event.getRowIndex();
            double dif = listaMovimientosProductos.get(index).getExistencia() - listaMovimientosProductos.get(index).getCantidadSolicitada();
            listaMovimientosProductos.get(index).setTotal(dif);
            if(dif<0d){
                listaMovimientosProductos.get(index).setCantidadSolicitada(listaMovimientosProductos.get(index).getExistencia());
                listaMovimientosProductos.get(index).setTotal(listaMovimientosProductos.get(index).getExistencia());
            }
            RequestContext.getCurrentInstance().update("IdFormMovimientosInventario:opProductos");
        }catch(Exception e){
            
        }
    }
    public void updateTabla(){
        RequestContext.getCurrentInstance().update("IdFormMovimientosInventario:opProductos");
    }
    public void agregarProducto(){
        if(bodegaProducto!=null){
            InvMovimientoProductos producto = new InvMovimientoProductos();
            producto.setIdMovimiento(movimiento);
            producto.setCantidadSolicitada(1d);
            producto.setIdProducto(bodegaProducto.getIdProducto());
            producto.setCantidadRecibida(0d);
            producto.setExistencia(bodegaProducto.getExistencia());
            listaMovimientosProductos.add(producto);
            RequestContext.getCurrentInstance().update("IdFormMovimientosInventario:opProductos");
        }
    }
    public void buscarCodigoDevolucion(){//entrada devolución por cliente
        initRender();
        if(!nroSalida.equals("")){
            InvMovimientos mov = movimientoFacade.getMovimientoNumero(nroSalida, "S",0);
            if(mov!=null){
                if(mov.getEstado().equals(TipoInventarioEnum.N.toString())){
                    imprimirMensaje("Salida", "El número de salida se encuentra anulada", FacesMessage.SEVERITY_INFO);
                    tipoProceso = 0;
                }else if(mov.getEstado().equals(TipoInventarioEnum.C.toString())){
                    imprimirMensaje("Salida", "El número de salida se encuentra cerrado", FacesMessage.SEVERITY_INFO);
                    tipoProceso = 0;
                }else{
                    renderTitulo=true;
                    movimiento = mov;
                    renderBotones = true;
                    renderBotonGuardar= true;
                    bodegaOrigen = movimiento.getIdBodegaOrigen();
                    renderEntradaTrasladoB = false;
                    renderEntradaDevolucionCompra = true;
                    renderEntradaDevolucionCompraB = true;
                    renderBotonGuardar = true;
                    listaMovimientosProductos = movimiento.getInvMovimientoProductosList();
                    for(int i=0;i<listaMovimientosProductos.size();i++){
                        listaMovimientosProductos.get(i).setCantidadRecibida(listaMovimientosProductos.get(i).getCantidadSolicitada());
                    }
                }
            }
        }
    }
    public void buscarCodigo(){//traslado bodega
        InvBodegas bodegaResponsable = bodegaFachada.bodegaUsuarioReponsable(loginMB.getUsuarioActual().getIdUsuario());
        if(bodegaResponsable==null){
            imprimirMensaje("Iniciando", "Usuario Actual no este responsable de bodega", FacesMessage.SEVERITY_WARN);
        }
        if(!nroSalida.equals("")){
            InvMovimientos mov = movimientoFacade.getMovimientoNumero(nroSalida, "S",bodegaResponsable.getIdBodega());
            if(mov!=null){
                if(mov.getEstado().equals(TipoInventarioEnum.N.toString())){
                    imprimirMensaje("Salida", "El número de salida se encuentra anulada", FacesMessage.SEVERITY_INFO);
                    renderTitulo=false;
                    renderBotones = false;
                    renderBotonGuardar= false;
                    renderEntradaTraslado=false;
                    renderEntradaTrasladoB=false;
                    tipoProceso = 0;
                }else if(mov.getEstado().equals(TipoInventarioEnum.C.toString())){
                    imprimirMensaje("Salida", "El número de salida se encuentra cerrado", FacesMessage.SEVERITY_INFO);
                    renderBotones = false;
                    renderBotonGuardar= false;
                    renderEntradaTraslado=false;
                    renderEntradaTrasladoB=false;
                    tipoProceso = 0;
                    renderTitulo=false;
                }else{
                    renderTitulo=true;
                    movimiento = mov;
                    renderBotones = true;
                    renderBotonGuardar= true;
                    bodegaOrigen = movimiento.getIdBodegaOrigen();
                    bodegaDestino = movimiento.getIdBodegaDestino();
                    renderEntradaTrasladoB = true;
                    renderBotonGuardar = true;
                    listaMovimientosProductos = movimiento.getInvMovimientoProductosList();
                    movimientoSeleccionado = mov;
                    for(int i=0;i<listaMovimientosProductos.size();i++){
                        listaMovimientosProductos.get(i).setCantidadRecibida(listaMovimientosProductos.get(i).getCantidadSolicitada());
                    }
                }
            }else{
                imprimirMensaje("No hay registros", "No se encontró orden de salida asociada a la bodega "+bodegaResponsable.getNombre(), FacesMessage.SEVERITY_INFO);
            }
        }
    }
    
    public void buscarCodigoSalidaDevolucionCompra(){
        try {
            InvBodegas bodegaResponsable = bodegaFachada.bodegaUsuarioReponsable(loginMB.getUsuarioActual().getIdUsuario());
            if(bodegaResponsable==null){
                imprimirMensaje("Iniciando", "Usuario Actual no este responsable de bodega", FacesMessage.SEVERITY_WARN);
            }
            if(!nroSalida.equals("")){
                InvMovimientos mov = movimientoFacade.getMovimientoNumero(nroSalida, "S",bodegaResponsable.getIdBodega());
                if(mov!=null){
                    if(mov.getEstado().equals(TipoInventarioEnum.N.toString())){
                        imprimirMensaje("Salida", "El número de salida se encuentra anulada", FacesMessage.SEVERITY_INFO);
                    }else if(mov.getEstado().equals(TipoInventarioEnum.P.toString())){
                        imprimirMensaje("Salida", "El número de salida se encuentra pendiente", FacesMessage.SEVERITY_INFO);
                    }else{
                        bodegaDestino = mov.getIdBodegaDestino();
                        movimientoSeleccionado = mov;
                        renderForm = true;
                        renderTitulo=true;
                        movimiento = mov;
                        renderBotones = true;
                        renderBotonGuardar= true;
                        bodegaOrigen = movimiento.getIdBodegaOrigen();
                        bodegaDestino = movimiento.getIdBodegaDestino();
                        renderSalidaTrasladosDevolucionCompraB=true;
                        renderBotonGuardar = true;
                        listaMovimientosProductos = movimiento.getInvMovimientoProductosList();
                    }
                    
                }else{
                    imprimirMensaje("No hay registros", "No se encontró documento asociado a la bodega "+bodegaResponsable.getNombre(), FacesMessage.SEVERITY_INFO);
                }
            }else if(!nroOrden.equals("")){
                //buscamos orde de compra
                if(bodegaResponsable.getPrincipal()){
                    this.ordenCompra = ordenCompraFacade.getCompraXDocumento(nroOrden);
                    if(ordenCompra!=null){
                        InvMovimientos mov = movimientoFacade.getMovimientoOrden(ordenCompra.getIdOrdenCompra());
                        if (mov != null) {
                            if (mov.getEstado().equals(TipoInventarioEnum.N.toString())) {
                                imprimirMensaje("Salida", "El número de salida se encuentra anulada", FacesMessage.SEVERITY_INFO);
                            } else if (mov.getEstado().equals(TipoInventarioEnum.P.toString())) {
                                imprimirMensaje("Salida", "El número de salida se encuentra pendiente", FacesMessage.SEVERITY_INFO);
                            } else {
                                //idLote = ordenCompra.getIdLote()!=null?ordenCompra.getIdLote().getIdLote():0;
                                bodegaDestino = mov.getIdBodegaDestino();
                                movimientoSeleccionado = mov;
                                renderForm = true;
                                renderTitulo=true;
                                movimiento = mov;
                                renderBotones = true;
                                renderBotonGuardar= true;
                                bodegaOrigen = movimiento.getIdBodegaOrigen();
                                bodegaDestino = movimiento.getIdBodegaDestino();
                                renderSalidaTrasladosDevolucionCompraB=true;
                                renderBotonGuardar = true;
                                listaMovimientosProductos = movimiento.getInvMovimientoProductosList();
                            }
                        }
                    }else{
                        imprimirMensaje("No hay registros", "No se encontró número de orden", FacesMessage.SEVERITY_INFO);
                    }
                }else{
                    imprimirMensaje("No hay registros", "No es una bodega prinicipal", FacesMessage.SEVERITY_INFO);
                }
            }else{
                imprimirMensaje("Digite Documento", "Digite un número de documento", FacesMessage.SEVERITY_INFO);
            }
                  
        } catch (Exception e) {
        }
    }
    public boolean isRenderBotones() {
        return renderBotones;
    }
    
    public void setRenderBotones(boolean renderBotones) {
        this.renderBotones = renderBotones;
    }

    public boolean isRenderForm() {
        return renderForm;
    }

    public void setRenderForm(boolean renderForm) {
        this.renderForm = renderForm;
    }

    public String getTipoMovimiento() {
        return tipoMovimiento;
    }

    public void setTipoMovimiento(String tipoMovimiento) {
        this.tipoMovimiento = tipoMovimiento;
    }

    public InvBodegas getBodegaOrigen() {
        return bodegaOrigen;
    }

    public void setBodegaOrigen(InvBodegas bodegaOrigen) {
        this.bodegaOrigen = bodegaOrigen;
    }

    public InvBodegas getBodegaDestino() {
        return bodegaDestino;
    }

    public void setBodegaDestino(InvBodegas bodegaDestino) {
        this.bodegaDestino = bodegaDestino;
    }
    
    public LoginMB getLoginMB() {
        return loginMB;
    }

    public void setLoginMB(LoginMB loginMB) {
        this.loginMB = loginMB;
    }

    public List<InvBodegas> getListaBodegas() {
        return listaBodegas;
    }

    public void setListaBodegas(List<InvBodegas> listaBodegas) {
        this.listaBodegas = listaBodegas;
    }

    public InvMovimientos getMovimiento() {
        return movimiento;
    }

    public void setMovimiento(InvMovimientos movimiento) {
        this.movimiento = movimiento;
    }

    public boolean isRenderEntradaTraslado() {
        return renderEntradaTraslado;
    }

    public void setRenderEntradaTraslado(boolean renderEntradaTraslado) {
        this.renderEntradaTraslado = renderEntradaTraslado;
    }

    public boolean isRenderEntradaTrasladoB() {
        return renderEntradaTrasladoB;
    }

    public void setRenderEntradaTrasladoB(boolean renderEntradaTrasladoB) {
        this.renderEntradaTrasladoB = renderEntradaTrasladoB;
    }

    public List<InvBodegas> getListaBodegasDestinos() {
        return listaBodegasDestinos;
    }

    public void setListaBodegasDestinos(List<InvBodegas> listaBodegasDestinos) {
        this.listaBodegasDestinos = listaBodegasDestinos;
    }

    public InvConsecutivos getConsecutivo() {
        return consecutivo;
    }

    public void setConsecutivo(InvConsecutivos consecutivo) {
        this.consecutivo = consecutivo;
    }

    public List<InvBodegaProductos> getListaProductosBodegas() {
        return listaProductosBodegas;
    }

    public void setListaProductosBodegas(List<InvBodegaProductos> listaProductosBodegas) {
        this.listaProductosBodegas = listaProductosBodegas;
    }

    public InvBodegaProductos getBodegaProducto() {
        return bodegaProducto;
    }

    public void setBodegaProducto(InvBodegaProductos bodegaProducto) {
        this.bodegaProducto = bodegaProducto;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public List<InvMovimientoProductos> getListaMovimientosProductos() {
        return listaMovimientosProductos;
    }

    public void setListaMovimientosProductos(List<InvMovimientoProductos> listaMovimientosProductos) {
        this.listaMovimientosProductos = listaMovimientosProductos;
    }

    public boolean isRenderBotonGuardar() {
        return renderBotonGuardar;
    }

    public void setRenderBotonGuardar(boolean renderBotonGuardar) {
        this.renderBotonGuardar = renderBotonGuardar;
    }

    public boolean isRenderBotonReporte() {
        return renderBotonReporte;
    }

    public void setRenderBotonReporte(boolean renderBotonReporte) {
        this.renderBotonReporte = renderBotonReporte;
    }

    public String getAsunto() {
        return asunto;
    }

    public void setAsunto(String asunto) {
        this.asunto = asunto;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
    private class MyAuthenticator extends Authenticator {  
        
        @Override
        public PasswordAuthentication getPasswordAuthentication() {  
            String username = "openmedicalinfo@gmail.com";  
            String password = "open2017";  
       
            return new PasswordAuthentication(username, password);  
        }  
    }

    public List<InvMovimientos> getListaMovimientos() {
        return listaMovimientos;
    }

    public void setListaMovimientos(List<InvMovimientos> listaMovimientos) {
        this.listaMovimientos = listaMovimientos;
    }

    public String getNroSalida() {
        return nroSalida;
    }

    public void setNroSalida(String nroSalida) {
        this.nroSalida = nroSalida;
    }

    public InvMovimientos getMovimientoEntrada() {
        return movimientoEntrada;
    }

    public void setMovimientoEntrada(InvMovimientos movimientoEntrada) {
        this.movimientoEntrada = movimientoEntrada;
    }

    public CfgCorreo getCorreo() {
        return correo;
    }

    public void setCorreo(CfgCorreo correo) {
        this.correo = correo;
    }

    public String getNumeroDocumento() {
        return numeroDocumento;
    }

    public void setNumeroDocumento(String numeroDocumento) {
        this.numeroDocumento = numeroDocumento;
    }

    public Date getFechaMovimiento() {
        return fechaMovimiento;
    }

    public void setFechaMovimiento(Date fechaMovimiento) {
        this.fechaMovimiento = fechaMovimiento;
    }

    public List<SelectItem> getListaProcesos() {
        return listaProcesos;
    }

    public void setListaProcesos(List<SelectItem> listaProcesos) {
        this.listaProcesos = listaProcesos;
    }

    public int getTipoProceso() {
        return tipoProceso;
    }

    public void setTipoProceso(int tipoProceso) {
        this.tipoProceso = tipoProceso;
    }

    public boolean isRenderProcesos() {
        return renderProcesos;
    }

    public void setRenderProcesos(boolean renderProcesos) {
        this.renderProcesos = renderProcesos;
    }

    public boolean isRenderEntradaSaldoInicial() {
        return renderEntradaSaldoInicial;
    }

    public void setRenderEntradaSaldoInicial(boolean renderEntradaSaldoInicial) {
        this.renderEntradaSaldoInicial = renderEntradaSaldoInicial;
    }

    public boolean isRenderEntradaAjustePositivo() {
        return renderEntradaAjustePositivo;
    }

    public void setRenderEntradaAjustePositivo(boolean renderEntradaAjustePositivo) {
        this.renderEntradaAjustePositivo = renderEntradaAjustePositivo;
    }

    public boolean isRenderSalidaTraslados() {
        return renderSalidaTraslados;
    }

    public void setRenderSalidaTraslados(boolean renderSalidaTraslados) {
        this.renderSalidaTraslados = renderSalidaTraslados;
    }

    
    public boolean isRenderSalidaTrasladosAjusteNegativo() {
        return renderSalidaTrasladosAjusteNegativo;
    }

    public void setRenderSalidaTrasladosAjusteNegativo(boolean renderSalidaTrasladosAjusteNegativo) {
        this.renderSalidaTrasladosAjusteNegativo = renderSalidaTrasladosAjusteNegativo;
    }

    public boolean isRenderSalidaTrasladosDevolucionCompra() {
        return renderSalidaTrasladosDevolucionCompra;
    }

    public void setRenderSalidaTrasladosDevolucionCompra(boolean renderSalidaTrasladosDevolucionCompra) {
        this.renderSalidaTrasladosDevolucionCompra = renderSalidaTrasladosDevolucionCompra;
    }

    public boolean isRenderTitulo() {
        return renderTitulo;
    }

    public void setRenderTitulo(boolean renderTitulo) {
        this.renderTitulo = renderTitulo;
    }

    public boolean isRenderEntradaDevolucionCompra() {
        return renderEntradaDevolucionCompra;
    }

    public void setRenderEntradaDevolucionCompra(boolean renderEntradaDevolucionCompra) {
        this.renderEntradaDevolucionCompra = renderEntradaDevolucionCompra;
    }

    public boolean isRenderEntradaDevolucionCompraB() {
        return renderEntradaDevolucionCompraB;
    }

    public void setRenderEntradaDevolucionCompraB(boolean renderEntradaDevolucionCompraB) {
        this.renderEntradaDevolucionCompraB = renderEntradaDevolucionCompraB;
    }

    public String getNroOrden() {
        return nroOrden;
    }

    public void setNroOrden(String nroOrden) {
        this.nroOrden = nroOrden;
    }

    public boolean isRenderSalidaTrasladosDevolucionCompraB() {
        return renderSalidaTrasladosDevolucionCompraB;
    }

    public void setRenderSalidaTrasladosDevolucionCompraB(boolean renderSalidaTrasladosDevolucionCompraB) {
        this.renderSalidaTrasladosDevolucionCompraB = renderSalidaTrasladosDevolucionCompraB;
    }

    public List<InvLotes> getListaLote() {
        return listaLote;
    }

    public void setListaLote(List<InvLotes> listaLote) {
        this.listaLote = listaLote;
    }

    public int getIdLote() {
        return idLote;
    }

    public void setIdLote(int idLote) {
        this.idLote = idLote;
    }
}
