/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managedBeans.inventario;

import beans.enumeradores.ClasificacionesEnum;
import beans.enumeradores.TipoInventarioEnum;
import beans.utilidades.MetodosGenerales;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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
import managedBeans.historias.DatosFormularioHistoria;
import managedBeans.seguridad.LoginMB;
import modelo.entidades.CfgClasificaciones;
import modelo.entidades.CfgMedicamento;
import modelo.entidades.CfgPacientes;
import modelo.entidades.HcCamposReg;
import modelo.entidades.HcDetalle;
import modelo.entidades.HcItems;
import modelo.entidades.HcRegistro;
import modelo.entidades.InvBodegaProductos;
import modelo.entidades.InvBodegas;
import modelo.entidades.InvConsecutivos;
import modelo.entidades.InvEntregaMedicamentos;
import modelo.entidades.InvEntregaMedicamentosDetalle;
import modelo.entidades.InvLotes;
import modelo.entidades.InvMovimientoProductos;
import modelo.entidades.InvMovimientos;
import modelo.entidades.InvProductos;
import modelo.fachadas.CfgClasificacionesFacade;
import modelo.fachadas.CfgMedicamentoFacade;
import modelo.fachadas.CfgPacientesFacade;
import modelo.fachadas.HcCamposRegFacade;
import modelo.fachadas.HcItemsFacade;
import modelo.fachadas.HcRegistroFacade;
import modelo.fachadas.InvBodegaProductosFacade;
import modelo.fachadas.InvBodegasFacade;
import modelo.fachadas.InvConsecutivosFacade;
import modelo.fachadas.InvEntregaMedicamentosDetalleFacade;
import modelo.fachadas.InvEntregaMedicamentosFacade;
import modelo.fachadas.InvLotesFacade;
import modelo.fachadas.InvMovimientosFacade;
import modelo.fachadas.InvProductosFacade;
import org.primefaces.context.RequestContext;

/**
 *
 * @author miguel
 */
@Named(value = "entregaMedicamentosMB")
@ViewScoped
public class EntregaMedicamentosMB extends MetodosGenerales implements java.io.Serializable{

    @EJB
    private InvConsecutivosFacade consecutivoFacade;
    @EJB
    private CfgClasificacionesFacade tipoIdentificacionFacade;
    @EJB
    private CfgPacientesFacade pacienteFacade;
    @EJB
    private HcRegistroFacade registroFacade;
    @EJB
    private HcCamposRegFacade camposRegFacade;
    @EJB
    private HcItemsFacade hcitemFacade;
    @EJB
    private InvProductosFacade productoFacade;
    @EJB
    private InvBodegasFacade  bodegaFacade;
    @EJB
    private InvEntregaMedicamentosFacade entregaMedicamentoFacade;
    @EJB
    private InvMovimientosFacade movimientoFacade;
    @EJB
    private InvEntregaMedicamentosDetalleFacade entregaMedicamentoDetalleFacade;
    @EJB
    private InvBodegaProductosFacade bodegaProductosFacade;
    @EJB
    private InvLotesFacade loteFachada;
    @EJB
    private CfgMedicamentoFacade medicamentoFacade;
    private InvEntregaMedicamentos entregaMedicamentos;
    private InvMovimientos movimiento;
    private InvConsecutivos consecutivo;
    private CfgPacientes paciente;
    private HcRegistro registro;
    private InvBodegas bodega;
    private DatosFormularioHistoria datosFormulario;//valores de cada uno de los campos de cualquier registro clinico
    private final SimpleDateFormat sdfDateHour = new SimpleDateFormat("dd/MM/yyyy HH:mm");
    
    private boolean entregaCerrada;
    private Date fecha;
    private String numeroDocumento;
    private String identificación;
    private String centroAtencion;
    private String observaciones;
    private int tipoIdentificacion;
    private int idSede;
    private boolean renderBoton;
    private boolean renderForm;
    private List<CfgClasificaciones> listaTipoIdentificacion;
    private List<InvEntregaMedicamentosDetalle> listaProductos;
    private List<InvEntregaMedicamentosDetalle> listaProductosPendientes;
    
    private LoginMB loginMB;
    public EntregaMedicamentosMB() {
        loginMB = FacesContext.getCurrentInstance().getApplication().evaluateExpressionGet(FacesContext.getCurrentInstance(), "#{loginMB}", LoginMB.class);
    }
   
    @PostConstruct
    public void init(){
        idSede= 0;
        fecha = new Date();
        this.paciente = new CfgPacientes();
        this.consecutivo = consecutivoFacade.getConsecutivoTipo(TipoInventarioEnum.M.toString());
        int cod = consecutivo.getConsecutivo()+1;
        this.numeroDocumento=String.format("%04d",cod);
        this.listaTipoIdentificacion = tipoIdentificacionFacade.buscarPorMaestro(ClasificacionesEnum.TipoIdentificacion.toString());
        this.listaProductos = new ArrayList<>();
        this.datosFormulario = new DatosFormularioHistoria();
        System.out.println(loginMB.getUsuarioActual().getIdUsuario());
        this.bodega = bodegaFacade.bodegaUsuarioReponsable(loginMB.getUsuarioActual().getIdUsuario());
        if(bodega!=null){
            centroAtencion = bodega.getIdSede().getNombreSede();
        }else{
            imprimirMensaje("Iniciando", "Usuario Actual no este responsable de bodega", FacesMessage.SEVERITY_WARN);
        }
        this.entregaMedicamentos = new InvEntregaMedicamentos();
        this.renderBoton = false;
    }
    
    public void nuevo(){
        
    }
    public void buscar(){
        
    }
    public void imprimir(){
        
    }
    public void guardar(){
        try {
            if(validarDatos()){
                if(entregaMedicamentos.getIdEntrega()==null){
                    this.consecutivo = consecutivoFacade.getConsecutivoTipo(TipoInventarioEnum.M.toString());
                    int cod = consecutivo.getConsecutivo()+1;
                    this.numeroDocumento=String.format("%04d",cod);
                    entregaMedicamentos.setNumeroEntrega(numeroDocumento);
                    entregaMedicamentos.setEstado(entregaCerrada?"C":"P");
                    entregaMedicamentos.setFechaEntrega(new Date());
                    entregaMedicamentos.setFechaActualizacion(new Date());
                    entregaMedicamentos.setIdPaciente(paciente);
                    entregaMedicamentos.setIdSede(bodega!=null?bodega.getIdSede():null);
                    entregaMedicamentos.setIdHistoriaClinica(registro);
                    entregaMedicamentos.setIdBodega(bodega);
                    entregaMedicamentos.setIdEmpresa(loginMB.getEmpresaActual());
                    entregaMedicamentos.setUsuarioActualiza(loginMB.getUsuarioActual());
                    entregaMedicamentos.setUsuarioElabora(loginMB.getUsuarioActual());
                    entregaMedicamentos.setInvEntregaMedicamentosDetalleList(listaProductos);
                    entregaMedicamentos.setObservaciones(observaciones);
                    entregaMedicamentoFacade.create(entregaMedicamentos);
                    //actualizamos consecutivo
                    consecutivo.setConsecutivo(consecutivo.getConsecutivo() + 1);
                    consecutivoFacade.edit(consecutivo);
                    
                    //Realizamos el movimiento de salida de la bodega
                    movimiento = new InvMovimientos();
                    movimiento.setFechaMovimiento(new Date());
                    movimiento.setObservaciones(observaciones);
                    movimiento.setIdEmpresa(loginMB.getEmpresaActual());
                    movimiento.setIdUsuarioCrea(loginMB.getUsuarioActual());
                    movimiento.setTipoMovimiento(TipoInventarioEnum.S.toString());
                    movimiento.setTipoProceso(8);
                    movimiento.setEstado(TipoInventarioEnum.C.toString());
                    movimiento.setUsuarioAprueba(loginMB.getUsuarioActual());
                    movimiento.setFechaAprobacion(new Date());
                    movimiento.setIdBodegaOrigen(bodega);
                    movimiento.setIdEntrega(entregaMedicamentos);
                    movimiento.setIdBodegaOrigen(bodega);
                    movimiento.setObservaciones(observaciones);
                    //for lista
                    List<InvMovimientoProductos> listaProductosMovimiento = new ArrayList();
                    for(InvEntregaMedicamentosDetalle detalle:listaProductos){
                        InvMovimientoProductos movimientoProducto = new InvMovimientoProductos();
                        movimientoProducto.setIdMovimiento(movimiento);
                        movimientoProducto.setIdProducto(detalle.getIdProducto());
                        movimientoProducto.setCantidadSolicitada(detalle.getCantidadRecetada());
                        movimientoProducto.setCantidadRecibida(detalle.getCantidadRecibida());
                        //Validar existencia
                        InvBodegaProductos bodegaProductos = bodegaProductosFacade.getBodegaProductoLote(bodega.getIdBodega(), detalle.getIdProducto().getIdProducto(),(detalle.getIdLote()!=null?detalle.getIdLote().getIdLote():0));
                        System.out.println(bodega.getIdBodega());
                        System.out.println(detalle.getIdProducto().getIdProducto());
                        System.out.println(detalle.getIdLote()!=null?detalle.getIdLote().getIdLote():0);
                        System.out.println("********"+bodegaProductos);
                        if(bodegaProductos!=null){
                            movimientoProducto.setExistencia(bodegaProductos.getExistencia());
                            //descontamos existencia
                            //descontamos el inventario de la bodega
                            bodegaProductos.setExistencia(bodegaProductos.getExistencia()-detalle.getCantidadRecibida());
                            bodegaProductosFacade.edit(bodegaProductos);
                        }
                        listaProductosMovimiento.add(movimientoProducto);
                        
                    }
                    movimiento.setInvMovimientoProductosList(listaProductosMovimiento);
                    this.consecutivo = consecutivoFacade.getConsecutivoTipo("S");
                    cod = consecutivo.getConsecutivo()+1;
                    movimiento.setNumeroDocumento(String.format("%04d",cod));
                    movimientoFacade.create(movimiento);
                    consecutivo.setConsecutivo(consecutivo.getConsecutivo()+1);
                    consecutivoFacade.edit(consecutivo);
                    
                }else{//fin creación
                    //actualizamos si hay una entrega pendiente
                    entregaMedicamentos.setEstado(entregaCerrada?"C":"P");
                    entregaMedicamentos.setIdBodega(bodega);
                    entregaMedicamentos.setUsuarioActualiza(loginMB.getUsuarioActual());
                    entregaMedicamentos.setObservaciones(observaciones);
                    entregaMedicamentoFacade.edit(entregaMedicamentos);
                    
                    
                    //Realizamos el movimiento de salida de la bodega
                    movimiento = new InvMovimientos();
                    movimiento.setFechaMovimiento(new Date());
                    movimiento.setObservaciones(observaciones);
                    movimiento.setIdEmpresa(loginMB.getEmpresaActual());
                    movimiento.setIdUsuarioCrea(loginMB.getUsuarioActual());
                    movimiento.setTipoMovimiento(TipoInventarioEnum.S.toString());
                    movimiento.setTipoProceso(8);
                    movimiento.setEstado(TipoInventarioEnum.C.toString());
                    movimiento.setUsuarioAprueba(loginMB.getUsuarioActual());
                    movimiento.setFechaAprobacion(new Date());
                    movimiento.setIdBodegaOrigen(bodega);
                    movimiento.setIdEntrega(entregaMedicamentos);
                    movimiento.setIdBodegaOrigen(bodega);
                    movimiento.setObservaciones(observaciones);
                    //for lista
                    List<InvMovimientoProductos> listaProductosMovimiento = new ArrayList();
                    InvEntregaMedicamentos medicamentos = entregaMedicamentoFacade.find(entregaMedicamentos.getIdEntrega());
                    listaProductosPendientes = medicamentos.getInvEntregaMedicamentosDetalleList();
                    for(InvEntregaMedicamentosDetalle detalle2:listaProductosPendientes){
                        System.out.println(detalle2.getIdProducto().getCodigo()+" "+detalle2.getCantidadRecibida());
                    }
                    for(InvEntregaMedicamentosDetalle detalle:listaProductos){
                        for(InvEntregaMedicamentosDetalle detalle2:listaProductosPendientes){
                            if(detalle2.getIdEntregaDetalle().equals(detalle.getIdEntregaDetalle())){
                                if(!Objects.equals(detalle2.getCantidadRecibida(), detalle.getCantidadRecibida())){
                                    InvMovimientoProductos movimientoProducto = new InvMovimientoProductos();
                                    movimientoProducto.setIdMovimiento(movimiento);
                                    movimientoProducto.setIdProducto(detalle.getIdProducto());
                                    movimientoProducto.setCantidadSolicitada(detalle.getCantidadRecetada());
                                    movimientoProducto.setCantidadRecibida(detalle.getCantidadRecibida());
                                    //Validar existencia
                                    InvBodegaProductos bodegaProductos = bodegaProductosFacade.getBodegaProductoLote(bodega.getIdBodega(), detalle.getIdProducto().getIdProducto(),(detalle.getIdLote()!=null?detalle.getIdLote().getIdLote():0));
                                    if(bodegaProductos!=null){
                                        movimientoProducto.setExistencia(bodegaProductos.getExistencia());
                                        //descontamos existencia
                                        //descontamos el inventario de la bodega
                                        bodegaProductos.setExistencia(bodegaProductos.getExistencia()-(detalle.getCantidadRecibida()-detalle2.getCantidadRecibida()));
                                        bodegaProductosFacade.edit(bodegaProductos);
                                    }
                                    
                                    //actualizamos los medicamentos entregados
                                    listaProductosMovimiento.add(movimientoProducto);
                                    detalle2.setCantidadRecibida(detalle.getCantidadRecibida());
                                    entregaMedicamentoDetalleFacade.edit(detalle2);
                                }
                                break;
                            }
                        }
                        
                        
                    }
                    movimiento.setInvMovimientoProductosList(listaProductosMovimiento);
                    this.consecutivo = consecutivoFacade.getConsecutivoTipo("S");
                    int cod = consecutivo.getConsecutivo()+1;
                    movimiento.setNumeroDocumento(String.format("%04d",cod));
                    movimientoFacade.create(movimiento);
                    consecutivo.setConsecutivo(consecutivo.getConsecutivo()+1);
                    consecutivoFacade.edit(consecutivo);
                }
                imprimirMensaje("Guardado", "Guardado Correctamente", FacesMessage.SEVERITY_INFO);
                limpiar();
            }//fin validación 
            
        } catch (Exception e) {
            e.printStackTrace();
            imprimirMensaje("Error al guardar", e.getLocalizedMessage(), FacesMessage.SEVERITY_ERROR);
        }
    }
    public void limpiar(){
        movimiento = new InvMovimientos();
        renderBoton = true;
    }
    
    public void facturar(){
        RequestContext.getCurrentInstance().execute("window.parent.cargarTab('Facturar Paciente','facturacion/facturarPaciente.xhtml','idEntrega;" + entregaMedicamentos.getIdEntrega() + ";idEntrega;1')");
    }
    private boolean validarDatos(){
        entregaCerrada = true;
        for(InvEntregaMedicamentosDetalle detalle:listaProductos){
            //validamos si hay diferencia en la entrega
            if(!detalle.getCantidadRecetada().equals(detalle.getCantidadRecibida())){
                entregaCerrada = false;
            }
            if(detalle.getCantidadRecibida()>detalle.getCantidadRecetada()){
                imprimirMensaje("Error al guardar", "La cantidad entregada es mayor a la recetada", FacesMessage.SEVERITY_ERROR);
                return false;
            }
        }
        return true;
    
    }
    public void buscarPaciente(){
        paciente = pacienteFacade.findPacienteByTipIden(tipoIdentificacion, identificación);
        if(paciente!=null){
            
            //Buscamos si existe una historia clinica tipo receta medicamento pendiente de entrega
            //44 receta médica
            registro = registroFacade.historiaXFormulaMedicaPaciente(paciente.getIdPaciente(), 19);
            InvEntregaMedicamentosDetalle detalleMedicamento = new InvEntregaMedicamentosDetalle();
            if(registro!=null){
                renderForm = true;
                renderBoton =false;
                List<HcDetalle> listaDetalles = registro.getHcDetalleList();
                
                int i = 0;
                for (HcDetalle detalle : listaDetalles) {
                    HcCamposReg campo = camposRegFacade.find(detalle.getHcDetallePK().getIdCampo());
                    if (campo != null) {
                        if (campo.getTabla() != null && campo.getTabla().length() != 0) {//tiene tabala o tipo de dato
                            switch (campo.getTabla()) {
                                case "date":
                                    break;
                                default://es una categoria
                                    datosFormulario.setValor(campo.getPosicion(), detalle.getValor());
                                    break;
                            }
                        } else {//simplemente es texto
                            datosFormulario.setValor(campo.getPosicion(), detalle.getValor());
                        }System.out.println("Se coloco en datosFormulario." + campo.getPosicion() + " el valor de " + detalle.getValor());
                    } else {
                        System.out.println("Error: no se encontro hc_campos_reg.id_campo= " + detalle.getHcDetallePK().getIdCampo());
                    }
                }//fin for
                
                for (HcDetalle detalle : listaDetalles) {
                    HcCamposReg campo = camposRegFacade.find(detalle.getHcDetallePK().getIdCampo());
                    if (campo != null) {
                        if (campo.getTabla() != null && campo.getTabla().length() != 0) {//tiene tabala o tipo de dato
                            switch (campo.getTabla()) {
                                case "date":
                                    try {
                                        Date f = sdfDateHour.parse(detalle.getValor());
                                        datosFormulario.setValor(campo.getPosicion(), f);
                                    } catch (ParseException ex) {
                                        datosFormulario.setValor(campo.getPosicion(), "");
                                    }
                                    break;
                                default://es una categoria
                                    datosFormulario.setValor(campo.getPosicion(), detalle.getValor());
                                    break;
                            }
                        } else {//simplemente es texto
                            datosFormulario.setValor(campo.getPosicion(), detalle.getValor());
                        }//System.out.println("Se coloco en datosFormulario." + campo.getPosicion() + " el valor de " + detalle.getValor());
                    } else {
                        System.out.println("Error: no se encontro hc_campos_reg.id_campo= " + detalle.getHcDetallePK().getIdCampo());
                    }

                }
                
                //seteamos los valores en su posicion
                List<HcItems> listaMedicamentos = hcitemFacade.findByIdRegistro(registro);
                for(HcItems item: listaMedicamentos){
                    CfgMedicamento medicamento = medicamentoFacade.find(Integer.parseInt(item.getIdTabla()));
                    InvProductos p = medicamento.getIdProducto();
                    if(null != p){
                            int idLote = loteFachada.idLote(p.getIdProducto());
                       // if(idLote != 0 ){
                            detalleMedicamento = new InvEntregaMedicamentosDetalle();
                            detalleMedicamento.setIdProducto(p);
                            detalleMedicamento.setCantidadRecetada(item.getCantidad().doubleValue());
                            detalleMedicamento.setCantidadRecibida(detalleMedicamento.getCantidadRecetada());
                            detalleMedicamento.setObservaciones(datosFormulario.getDato4().toString());
                            detalleMedicamento.setIdEntrega(entregaMedicamentos);
                            detalleMedicamento.setExistencia(bodegaProductosFacade.totalExistenciaProducto(p.getIdProducto(), item.getIdSede()));
                            detalleMedicamento.setIdLote(idLote!=0 ? loteFachada.find(idLote) : null);
                            listaProductos.add(detalleMedicamento);
                        //}else{
                          //  imprimirMensaje("Error al obtener Lote del producto", medicamento.getNombreGenerico(), FacesMessage.SEVERITY_ERROR);
                        //break;
                        //}
                    }else{
                        imprimirMensaje("Error al obtener producto", medicamento.getNombreGenerico(), FacesMessage.SEVERITY_ERROR);
                        break;
                    }
                }
                   
                    
            }else{//si hay historico en entrega de movimiento
                //buscamos registros que esten pendientes
                registro = registroFacade.historiaXFormulaMedicaPacientePendiente(paciente.getIdPaciente(), 44);
                if(registro!=null){
                    //buscamos la entrega pendiente
                    entregaMedicamentos = entregaMedicamentoFacade.entregaMedicamentoXRegistro(registro.getIdRegistro());
                    if(entregaMedicamentos!=null){
                        listaProductos = entregaMedicamentos.getInvEntregaMedicamentosDetalleList();
                        paciente = entregaMedicamentos.getIdPaciente();
                        renderForm = true;
                        renderBoton =false;
                        listaProductosPendientes = entregaMedicamentos.getInvEntregaMedicamentosDetalleList();
                    }else{
                        renderForm = false;
                        renderBoton =false;
                        this.paciente = new CfgPacientes();
                        imprimirMensaje("No hay registros", "No se encontró recetas pendientes", FacesMessage.SEVERITY_INFO);
                    }
                }else{
                    renderForm = false;
                    renderBoton =false;
                    this.paciente = new CfgPacientes();
                    imprimirMensaje("No hay registros", "No se encontró recetas pendientes", FacesMessage.SEVERITY_INFO);
                }
            }
        }else{
            renderForm = false;
            renderBoton =false;
            this.paciente = new CfgPacientes();
            imprimirMensaje("No hay registros", "No se encontró paciente", FacesMessage.SEVERITY_INFO);
        }
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public String getNumeroDocumento() {
        return numeroDocumento;
    }

    public void setNumeroDocumento(String numeroDocumento) {
        this.numeroDocumento = numeroDocumento;
    }

    public InvConsecutivos getConsecutivo() {
        return consecutivo;
    }

    public void setConsecutivo(InvConsecutivos consecutivo) {
        this.consecutivo = consecutivo;
    }

    public CfgPacientes getPaciente() {
        return paciente;
    }

    public void setPaciente(CfgPacientes paciente) {
        this.paciente = paciente;
    }

    public String getIdentificación() {
        return identificación;
    }

    public void setIdentificación(String identificación) {
        this.identificación = identificación;
    }

    public int getTipoIdentificacion() {
        return tipoIdentificacion;
    }

    public void setTipoIdentificacion(int tipoIdentificacion) {
        this.tipoIdentificacion = tipoIdentificacion;
    }

    public List<CfgClasificaciones> getListaTipoIdentificacion() {
        return listaTipoIdentificacion;
    }

    public void setListaTipoIdentificacion(List<CfgClasificaciones> listaTipoIdentificacion) {
        this.listaTipoIdentificacion = listaTipoIdentificacion;
    }

    public boolean isRenderBoton() {
        return renderBoton;
    }

    public void setRenderBoton(boolean renderBoton) {
        this.renderBoton = renderBoton;
    }

    public boolean isRenderForm() {
        return renderForm;
    }

    public void setRenderForm(boolean renderForm) {
        this.renderForm = renderForm;
    }

    public List<InvEntregaMedicamentosDetalle> getListaProductos() {
        return listaProductos;
    }

    public void setListaProductos(List<InvEntregaMedicamentosDetalle> listaProductos) {
        this.listaProductos = listaProductos;
    }

    public HcRegistro getRegistro() {
        return registro;
    }

    public void setRegistro(HcRegistro registro) {
        this.registro = registro;
    }

    public DatosFormularioHistoria getDatosFormulario() {
        return datosFormulario;
    }

    public void setDatosFormulario(DatosFormularioHistoria datosFormulario) {
        this.datosFormulario = datosFormulario;
    }

    public String getCentroAtencion() {
        return centroAtencion;
    }

    public void setCentroAtencion(String centroAtencion) {
        this.centroAtencion = centroAtencion;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public boolean isEntregaCerrada() {
        return entregaCerrada;
    }

    public void setEntregaCerrada(boolean entregaCerrada) {
        this.entregaCerrada = entregaCerrada;
    }

    public InvEntregaMedicamentos getEntregaMedicamentos() {
        return entregaMedicamentos;
    }

    public void setEntregaMedicamentos(InvEntregaMedicamentos entregaMedicamentos) {
        this.entregaMedicamentos = entregaMedicamentos;
    }

    public List<InvEntregaMedicamentosDetalle> getListaProductosPendientes() {
        return listaProductosPendientes;
    }

    public void setListaProductosPendientes(List<InvEntregaMedicamentosDetalle> listaProductosPendientes) {
        this.listaProductosPendientes = listaProductosPendientes;
    }
}
