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
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
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
import modelo.entidades.FacImpuestos;
import modelo.entidades.InvConsecutivos;
import modelo.entidades.InvOrdenCompra;
import modelo.entidades.InvOrdenCompraProductos;
import modelo.entidades.InvProductos;
import modelo.entidades.InvProveedorProductos;
import modelo.entidades.InvProveedores;
import modelo.fachadas.CfgCorreoFacade;
import modelo.fachadas.DBConnector;
import modelo.fachadas.FacImpuestosFacade;
import modelo.fachadas.InvConsecutivosFacade;
import modelo.fachadas.InvOrdenCompraFacade;
import modelo.fachadas.InvOrdenCompraProductosFacade;
import modelo.fachadas.InvProductosFacade;
import modelo.fachadas.InvProveedoresFacade;
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
@Named(value = "ordenCompraMB")
@ViewScoped
public class OrdenCompraMB extends MetodosGenerales implements java.io.Serializable{

    @EJB
    private InvConsecutivosFacade consecutivoFacade;
    @EJB
    private InvProductosFacade productoFachada;
    @EJB
    private InvProveedoresFacade proveedorFachada;
    @EJB
    private InvOrdenCompraFacade ordenCompraFacade;
    @EJB
    private FacImpuestosFacade impuestosFacade;
    @EJB
    private CfgCorreoFacade correoFacade;
    
    private InvOrdenCompra ordenCompra;
    private InvOrdenCompra ordenCompraSeleccionado;
    private InvConsecutivos consecutivo;
    private InvProveedores proveedor;
    private InvProveedores proveedorSeleccionada;
    private InvProductos    producto;
    private CfgCorreo correo;
    
    private Double subTotal;
    private Double descuento;
    private Double iva;
    private Double total;
    private boolean renderBotonGuardar;
    private Double valorIva;
    private List<InvProveedores>        listaProveedores;
    private List<InvOrdenCompraProductos> listaOrdenCompraProductos;
    private List<FacImpuestos> listaImpuestos ;
    private List<InvProductos> listaProductos;
    private List<InvOrdenCompra> listaOrdenes;
    
    private LoginMB loginMB;
    private String numeroDocumento;
    private String observaciones;
    private Date fecha;
    private String asunto;
    private String mensaje;
    private String email;
    
    public OrdenCompraMB() {
        loginMB = FacesContext.getCurrentInstance().getApplication().evaluateExpressionGet(FacesContext.getCurrentInstance(), "#{loginMB}", LoginMB.class);
        listaOrdenes = new ArrayList<>();
        
    }
   
    @PostConstruct
    public void init(){
        ordenCompra = new InvOrdenCompra();
        ordenCompra.setFecha(new Date());
        fecha = new Date();
        proveedor = new InvProveedores();
        this.consecutivo = consecutivoFacade.getConsecutivoTipo(TipoInventarioEnum.O.toString());
        int cod = consecutivo.getConsecutivo()+1;
        ordenCompra.setNroDocumento(String.format("%04d",cod));
        numeroDocumento=ordenCompra.getNroDocumento();
        listaProveedores = proveedorFachada.getActivos();
        subTotal=0d;
        iva=0d;
        descuento=0d;
        total=0d;
        renderBotonGuardar =true;
        listaProductos = productoFachada.getProductosActivos();
        listaOrdenes = ordenCompraFacade.findAll();
        listaOrdenCompraProductos = new ArrayList<>();
        ordenCompra.setInvOrdenCompraProductosList(listaOrdenCompraProductos);
        listaImpuestos = impuestosFacade.buscarPorNombre("IVA");
        correo = correoFacade.find(1);
        //Double valor iva
        for (FacImpuestos impuesto : listaImpuestos) {
            valorIva = impuesto.getValor();
            break;
        }
    }
    
    public void nuevo(){    
        proveedor = new InvProveedores();
        ordenCompra = new InvOrdenCompra();
        ordenCompra.setFecha(new Date());
        fecha = new Date();
        this.consecutivo = consecutivoFacade.getConsecutivoTipo(TipoInventarioEnum.O.toString());
        int cod = consecutivo.getConsecutivo()+1;
        ordenCompra.setNroDocumento(String.format("%04d",cod));
        numeroDocumento=ordenCompra.getNroDocumento();
        subTotal=0d;
        iva=0d;
        descuento=0d;
        total=0d;
        listaOrdenes = ordenCompraFacade.findAll();
        listaOrdenCompraProductos = new ArrayList<>();
        ordenCompra.setInvOrdenCompraProductosList(listaOrdenCompraProductos);
        observaciones = "";
    }
    public void buscar(){
        ordenCompra = ordenCompraSeleccionado;
        if(ordenCompra!=null){
            listaOrdenCompraProductos=ordenCompra.getInvOrdenCompraProductosList();
            numeroDocumento = ordenCompra.getNroDocumento();
            fecha = ordenCompra.getFecha();
            observaciones = ordenCompra.getObservaciones();
            renderBotonGuardar = false;
            proveedor = ordenCompra.getIdProveedor();
            total = ordenCompra.getTotal();
            subTotal = ordenCompra.getSubtotal();
            iva = ordenCompra.getIva();
            descuento = ordenCompra.getDescuento();
            asunto = "Orden de compra "+ordenCompra.getNroDocumento();
        }
    }
    public void eliminar(InvOrdenCompra ordenCompra){
        try {
            this.ordenCompra = ordenCompra;
            this.ordenCompra.setEstado(TipoInventarioEnum.N.toString());
            this.ordenCompra.setFechaActualizacion(new Date());
            this.ordenCompra.setUsuarioActualiza(loginMB.getUsuarioActual());
            ordenCompraFacade.edit(this.ordenCompra);
            imprimirMensaje("Guardado", "Anulado Correctamente", FacesMessage.SEVERITY_INFO);
            nuevo();
            RequestContext.getCurrentInstance().update("IdFormOrdenCompra");
        } catch (Exception e) {
            imprimirMensaje("Error al guardar", e.getLocalizedMessage(), FacesMessage.SEVERITY_INFO);
            Logger.getLogger(OrdenCompraMB.class.getName()).log(Level.SEVERE, null, e);
        }
    }
    
    public void eliminarProducto(InvOrdenCompraProductos producto){
        int index=0;
        for(InvOrdenCompraProductos il:listaOrdenCompraProductos){
            if(Objects.equals(il.getIdProducto().getIdProducto(), producto.getIdProducto().getIdProducto())){
                listaOrdenCompraProductos.remove(index);
                break;
            }
            index++;
        }
        RequestContext.getCurrentInstance().update("IdFormOrdenCompra:opProductos");
    }
    public void imprimir(InvOrdenCompra orden){
        FacesContext facesContext = FacesContext.getCurrentInstance();
        HttpServletResponse httpServletResponse = (HttpServletResponse) facesContext.getExternalContext().getResponse();
        try (ServletOutputStream servletOutputStream = httpServletResponse.getOutputStream()) {
            httpServletResponse.setContentType("application/pdf");
            ServletContext servletContext = (ServletContext) facesContext.getExternalContext().getContext();
            String ruta ;
                ruta = servletContext.getRealPath("/inventario/reportes/r_orden_compra.jasper");
            Map<String, Object> parametros = new HashMap<>();
            if(ordenCompra.getIdOrdenCompra()!=null)parametros.put("ID_ORDEN_COMPRA", ordenCompra.getIdOrdenCompra());
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
    private boolean validarDatos(){
        if(proveedor==null){
            imprimirMensaje("Error al guardar", "Seleccione proveedor", FacesMessage.SEVERITY_ERROR);
            return false;
        }
        if(numeroDocumento.equals("")){
            imprimirMensaje("Error al guardar", "Dígite Número Documento", FacesMessage.SEVERITY_ERROR);
            return false;
        }
        if(listaOrdenCompraProductos.isEmpty()){
            imprimirMensaje("Error al guardar", "Agregue Productos a la orden", FacesMessage.SEVERITY_ERROR);
            return false;
        }
        return true;
    }
    public void guardar(){
        try {
            proveedor=proveedorSeleccionada;
            if (validarDatos()) {
                //Guardamos encabezado
                //Obtenemos consecutivo y actualizamos
                ordenCompra = new InvOrdenCompra();
                this.consecutivo = consecutivoFacade.getConsecutivoTipo(TipoInventarioEnum.O.toString());
                int cod = consecutivo.getConsecutivo() + 1;
                ordenCompra.setIdProveedor(proveedor);
                ordenCompra.setFecha(new Date());
                ordenCompra.setSubtotal(subTotal);
                ordenCompra.setIva(iva);
                ordenCompra.setTotal(total);
                ordenCompra.setDescuento(descuento);
                ordenCompra.setEstado(TipoInventarioEnum.P.toString());
                ordenCompra.setFechaCreacion(new Date());
                ordenCompra.setFechaActualizacion(new Date());
                ordenCompra.setUsuarioCrea(loginMB.getUsuarioActual());
                ordenCompra.setUsuarioActualiza(loginMB.getUsuarioActual());
                ordenCompra.setNroDocumento(String.format("%04d", cod));
                ordenCompra.setIdOrdenCompra(0);
                ordenCompra.setInvOrdenCompraProductosList(new ArrayList());
                ordenCompra.setObservaciones(observaciones);
                ordenCompra.setIdEmpresa(loginMB.getEmpresaActual());
                for (InvOrdenCompraProductos productoCompra : listaOrdenCompraProductos) {
                    productoCompra.setIdOrdenCompra(ordenCompra);
                    ordenCompra.getInvOrdenCompraProductosList().add(productoCompra);
                }
                ordenCompraFacade.create(ordenCompra);
                
                consecutivo.setConsecutivo(consecutivo.getConsecutivo() + 1);
                consecutivoFacade.edit(consecutivo);
                

                imprimirMensaje("Guardado", "Guardado Correctamente Orden de compra " + ordenCompra.getNroDocumento(), FacesMessage.SEVERITY_INFO);
                //nuevo();
                renderBotonGuardar = false;
                ordenCompraSeleccionado= ordenCompra;
                asunto = "Orden de Compra "+ordenCompra.getNroDocumento();
            }
        } catch (Exception e) {
            Logger.getLogger(OrdenCompraMB.class.getName()).log(Level.SEVERE, null, e);
            imprimirMensaje("Error al guardar", e.getLocalizedMessage(), FacesMessage.SEVERITY_ERROR);
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
                File in = new File(servletContext.getRealPath("/inventario/reportes/r_orden_compra.jasper"));
                Map<String, Object> parametros = new HashMap<>();
                if (ordenCompra.getIdOrdenCompra() != null) {
                    parametros.put("ID_ORDEN_COMPRA", ordenCompra.getIdOrdenCompra());
                }
                jasperReport = (JasperReport) JRLoader.loadObject(in);
                jasperPrint = JasperFillManager.fillReport(jasperReport, parametros, con);
                //se crea el archivo PDF
                String url = servletContext.getRealPath("/inventario/reportes/");
                JasperExportManager.exportReportToPdfFile(jasperPrint, url+ "/reporte_" + ordenCompra.getNroDocumento() + ".pdf");
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
                adjunto.setDataHandler(new DataHandler(new FileDataSource(url+ "/reporte_" + ordenCompra.getNroDocumento() + ".pdf")));
                adjunto.setFileName("orden_" + ordenCompra.getNroDocumento() + ".pdf");
                message.setSubject(asunto, encoding);
                BodyPart mbp = new MimeBodyPart();
                mbp.setContent(mensaje, "text/html");
                multipart.addBodyPart(mbp);
                multipart.addBodyPart(adjunto);
                message.setContent(multipart);
                Transport.send(message);
                File pdf = new File(url + "/reporte_" +ordenCompra.getNroDocumento() + ".pdf");
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
    public void buscarProveedor(){
         proveedor=proveedorSeleccionada;
        try {
            subTotal = subTotal+0d;
            for(InvProveedorProductos productoProveedor:proveedor.getInvProveedorProductosList()){
                InvProductos p = productoProveedor.getIdProducto();
                InvOrdenCompraProductos ordenCompraProductos = new InvOrdenCompraProductos();
                ordenCompraProductos.setIdOrdenCompra(ordenCompra);
                ordenCompraProductos.setIdProducto(p);
                ordenCompraProductos.setCantidad(1d);
                ordenCompraProductos.setDescuento(0d);
                ordenCompraProductos.setPrecioUnidad(p.getCosto());
                ordenCompraProductos.setSubTotal(ordenCompraProductos.getCantidad()*ordenCompraProductos.getPrecioUnidad());
                subTotal = subTotal+ordenCompraProductos.getSubTotal();
                descuento = 0d;
                
                for(FacImpuestos impuesto:listaImpuestos){
                    iva =impuesto.getValor()*subTotal;    
                    break;
                }
                total=subTotal-descuento+iva;
                listaOrdenCompraProductos.add(ordenCompraProductos);
            }
            RequestContext.getCurrentInstance().update("IdFormOrdenCompra:opFormulario");
            RequestContext.getCurrentInstance().update("IdFormOrdenCompra:opProductos");
            RequestContext.getCurrentInstance().update("IdFormOrdenCompra:opTotales");
        } catch (Exception e) {
            Logger.getLogger(OrdenCompraMB.class.getName()).log(Level.SEVERE, null, e);
        }
    }
    
    private void calcularValoresTotales(){
        subTotal = 0d;
        descuento = 0d;
        iva = 0d;
        total = 0d;
        for(int i=0;i<listaOrdenCompraProductos.size();i++){
            Double dscto = listaOrdenCompraProductos.get(i).getDescuento()/100d;
            Double subTotalCalculo=listaOrdenCompraProductos.get(i).getPrecioUnidad()*listaOrdenCompraProductos.get(i).getCantidad();
            listaOrdenCompraProductos.get(i).setSubTotal(subTotalCalculo-(subTotalCalculo*dscto));
            subTotal = subTotal+listaOrdenCompraProductos.get(i).getSubTotal();
            descuento = descuento+(subTotalCalculo*dscto);
            }
        iva = subTotal*valorIva;
        total=subTotal+iva;
    }
    public void onCellEdit(CellEditEvent event) {
        try{
            int index = event.getRowIndex();
            Double dscto = listaOrdenCompraProductos.get(index).getDescuento()/100d;
            
            Double subTotalCalculo=listaOrdenCompraProductos.get(index).getPrecioUnidad()*listaOrdenCompraProductos.get(index).getCantidad();
            
            listaOrdenCompraProductos.get(index).setSubTotal(subTotalCalculo-(subTotalCalculo*dscto));
            calcularValoresTotales();
            RequestContext.getCurrentInstance().update("IdFormOrdenCompra:opProductos");
            RequestContext.getCurrentInstance().update("IdFormOrdenCompra:opTotales");
        }catch(Exception e){
            
        }
    }
    
    public void seleccionarProducto(){
        if (producto != null) {
            InvProductos p = producto;
            InvOrdenCompraProductos ordenCompraProductos = new InvOrdenCompraProductos();
            ordenCompraProductos.setIdOrdenCompra(ordenCompra);
            ordenCompraProductos.setIdProducto(p);
            ordenCompraProductos.setCantidad(1d);
            ordenCompraProductos.setDescuento(0d);
            ordenCompraProductos.setPrecioUnidad(p.getCosto());
            ordenCompraProductos.setSubTotal(ordenCompraProductos.getCantidad() * ordenCompraProductos.getPrecioUnidad());
            subTotal = subTotal + ordenCompraProductos.getSubTotal();
            descuento = 0d;

            for (FacImpuestos impuesto : listaImpuestos) {
                iva = impuesto.getValor() * subTotal;
                break;
            }
            total = subTotal - descuento + iva;
            listaOrdenCompraProductos.add(ordenCompraProductos);
            Collections.sort(listaOrdenCompraProductos);
        }
    }
    
    public void updateTabla() {
        RequestContext.getCurrentInstance().update("IdFormOrdenCompra:opProductos");
        RequestContext.getCurrentInstance().update("IdFormOrdenCompra:opTotales");
    }


    public InvOrdenCompra getOrdenCompra() {
        return ordenCompra;
    }

    public void setOrdenCompra(InvOrdenCompra ordenCompra) {
        this.ordenCompra = ordenCompra;
    }

    public InvConsecutivos getConsecutivo() {
        return consecutivo;
    }

    public void setConsecutivo(InvConsecutivos consecutivo) {
        this.consecutivo = consecutivo;
    }

    public InvProveedores getProveedor() {
        return proveedor;
    }

    public void setProveedor(InvProveedores proveedor) {
        this.proveedor = proveedor;
    }

    public InvProveedores getProveedorSeleccionada() {
        return proveedorSeleccionada;
    }

    public void setProveedorSeleccionada(InvProveedores proveedorSeleccionada) {
        this.proveedorSeleccionada = proveedorSeleccionada;
    }

    public List<InvProveedores> getListaProveedores() {
        return listaProveedores;
    }

    public void setListaProveedores(List<InvProveedores> listaProveedores) {
        this.listaProveedores = listaProveedores;
    }

    public List<InvOrdenCompraProductos> getListaOrdenCompraProductos() {
        return listaOrdenCompraProductos;
    }

    public void setListaOrdenCompraProductos(List<InvOrdenCompraProductos> listaOrdenCompraProductos) {
        this.listaOrdenCompraProductos = listaOrdenCompraProductos;
    }

    public Double getSubTotal() {
        return subTotal;
    }

    public void setSubTotal(Double subTotal) {
        this.subTotal = subTotal;
    }

    public Double getDescuento() {
        return descuento;
    }

    public void setDescuento(Double descuento) {
        this.descuento = descuento;
    }

    public Double getIva() {
        return iva;
    }

    public void setIva(Double iva) {
        this.iva = iva;
    }

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }

    public List<FacImpuestos> getListaImpuestos() {
        return listaImpuestos;
    }

    public void setListaImpuestos(List<FacImpuestos> listaImpuestos) {
        this.listaImpuestos = listaImpuestos;
    }

    public List<InvProductos> getListaProductos() {
        return listaProductos;
    }

    public void setListaProductos(List<InvProductos> listaProductos) {
        this.listaProductos = listaProductos;
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

    public boolean isRenderBotonGuardar() {
        return renderBotonGuardar;
    }

    public void setRenderBotonGuardar(boolean renderBotonGuardar) {
        this.renderBotonGuardar = renderBotonGuardar;
    }

    public List<InvOrdenCompra> getListaOrdenes() {
        return listaOrdenes;
    }

    public void setListaOrdenes(List<InvOrdenCompra> listaOrdenes) {
        this.listaOrdenes = listaOrdenes;
    }

    public String getNumeroDocumento() {
        return numeroDocumento;
    }

    public void setNumeroDocumento(String numeroDocumento) {
        this.numeroDocumento = numeroDocumento;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public InvOrdenCompra getOrdenCompraSeleccionado() {
        return ordenCompraSeleccionado;
    }

    public void setOrdenCompraSeleccionado(InvOrdenCompra ordenCompraSeleccionado) {
        this.ordenCompraSeleccionado = ordenCompraSeleccionado;
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

    public CfgCorreo getCorreo() {
        return correo;
    }

    public void setCorreo(CfgCorreo correo) {
        this.correo = correo;
    }
    
    private class MyAuthenticator extends Authenticator {  
        
        public PasswordAuthentication getPasswordAuthentication() {  
            String username = "openmedicalinfo@gmail.com";  
            String password = "open2017";  
       
            return new PasswordAuthentication(username, password);  
        }  
    }
}
