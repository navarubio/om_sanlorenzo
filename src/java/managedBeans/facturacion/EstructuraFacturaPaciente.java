/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managedBeans.facturacion;

import java.util.List;

/**
 *
 * @author santos
 */
public class EstructuraFacturaPaciente { //clase que contiene los datos para una factura de paciente

    private String tituloFactura = "";//NombreEmpresa,Nit,Regimen <=> Html
    private String nitEmpresa = "";
    private String regimenEmpresa = "";
    private String piePagina="";
    private String tipoDocumento = "";//Factura, Orden, Recibo...
    private String codigoDocumento = "";
    private String subtituloFactura = "";//direccion-telefonos-email
    private String usuario = "";
    private String clienteNombre = "";
    private String clienteDireccion = "";
    private String clienteIdentificacion = "";//Nit o C.C.
    private String clienteTelefono = "";
    private String clienteCiudad = "";
    private String email = "";
    private String sexo = "";
    private String edad = "";
    private String eps = "";
    private String sede = "";
    private String contrato = "";
    private String servicio = "";
    private String medico = "";
    private String historia = "";
    private String fechaFactura = "";
    private String fechaVencimiento = "";
    private String gravadas = "";
    private String noGravadas = "";
    private String observaciones = "";
    private String subtotal = "";
    private String iva = "";
    private String tituloIva = "";
    private String cree = "";
    private String totalEmpresa = "";
    private String totalUsuario = "";
    private String totalCopago = "";
    private String totalCuotaMod = "";
    private String tituloCree = "";
    private String menosCuotaModeradora = "";
    private String menosCopago = "";
    private String total = "";
    private String son = "";//total en palabras
    private String firmaAutoriza = "";
    private String logoEmpresa = "";
    private List<EstructuraItemsPaciente> listaItemsFactura;

    public String getTituloFactura() {
        return tituloFactura;
    }

    public void setTituloFactura(String tituloFactura) {
        this.tituloFactura = tituloFactura;
    }

    public String getTipoDocumento() {
        return tipoDocumento;
    }

    public void setTipoDocumento(String tipoDocumento) {
        this.tipoDocumento = tipoDocumento;
    }

    public String getCodigoDocumento() {
        return codigoDocumento;
    }

    public void setCodigoDocumento(String codigoDocumento) {
        this.codigoDocumento = codigoDocumento;
    }

    public String getSubtituloFactura() {
        return subtituloFactura;
    }

    public void setSubtituloFactura(String subtituloFactura) {
        this.subtituloFactura = subtituloFactura;
    }

    public String getClienteNombre() {
        return clienteNombre;
    }

    public void setClienteNombre(String clienteNombre) {
        this.clienteNombre = clienteNombre;
    }

    public String getClienteDireccion() {
        return clienteDireccion;
    }

    public void setClienteDireccion(String clienteDireccion) {
        this.clienteDireccion = clienteDireccion;
    }

    public String getClienteIdentificacion() {
        return clienteIdentificacion;
    }

    public void setClienteIdentificacion(String clienteIdentificacion) {
        this.clienteIdentificacion = clienteIdentificacion;
    }

    public String getClienteTelefono() {
        return clienteTelefono;
    }

    public void setClienteTelefono(String clienteTelefono) {
        this.clienteTelefono = clienteTelefono;
    }

    public String getClienteCiudad() {
        return clienteCiudad;
    }

    public void setClienteCiudad(String clienteCiudad) {
        this.clienteCiudad = clienteCiudad;
    }

    public String getFechaFactura() {
        return fechaFactura;
    }

    public void setFechaFactura(String fechaFactura) {
        this.fechaFactura = fechaFactura;
    }

    public String getFechaVencimiento() {
        return fechaVencimiento;
    }

    public void setFechaVencimiento(String fechaVencimiento) {
        this.fechaVencimiento = fechaVencimiento;
    }

    public String getGravadas() {
        return gravadas;
    }

    public void setGravadas(String gravadas) {
        this.gravadas = gravadas;
    }

    public String getNoGravadas() {
        return noGravadas;
    }

    public void setNoGravadas(String noGravadas) {
        this.noGravadas = noGravadas;
    }

    public String getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(String subtotal) {
        this.subtotal = subtotal;
    }

    public String getIva() {
        return iva;
    }

    public void setIva(String iva) {
        this.iva = iva;
    }

    public String getMenosCuotaModeradora() {
        return menosCuotaModeradora;
    }

    public void setMenosCuotaModeradora(String menosCuotaModeradora) {
        this.menosCuotaModeradora = menosCuotaModeradora;
    }

    public String getMenosCopago() {
        return menosCopago;
    }

    public void setMenosCopago(String menosCopago) {
        this.menosCopago = menosCopago;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getSon() {
        return son;
    }

    public void setSon(String son) {
        this.son = son;
    }

    public String getFirmaAutoriza() {
        return firmaAutoriza;
    }

    public void setFirmaAutoriza(String firmaAutoriza) {
        this.firmaAutoriza = firmaAutoriza;
    }

    public List<EstructuraItemsPaciente> getListaItemsFactura() {
        return listaItemsFactura;
    }

    public void setListaItemsFactura(List<EstructuraItemsPaciente> listaItemsFactura) {
        this.listaItemsFactura = listaItemsFactura;
    }

    public String getLogoEmpresa() {
        return logoEmpresa;
    }

    public void setLogoEmpresa(String logoEmpresa) {
        this.logoEmpresa = logoEmpresa;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public String getNitEmpresa() {
        return nitEmpresa;
    }

    public void setNitEmpresa(String nitEmpresa) {
        this.nitEmpresa = nitEmpresa;
    }

    public String getRegimenEmpresa() {
        return regimenEmpresa;
    }

    public void setRegimenEmpresa(String regimenEmpresa) {
        this.regimenEmpresa = regimenEmpresa;
    }

    public String getTituloIva() {
        return tituloIva;
    }

    public void setTituloIva(String tituloIva) {
        this.tituloIva = tituloIva;
    }

    public String getCree() {
        return cree;
    }

    public void setCree(String cree) {
        this.cree = cree;
    }

    public String getTituloCree() {
        return tituloCree;
    }

    public void setTituloCree(String tituloCree) {
        this.tituloCree = tituloCree;
    }

    public String getPiePagina() {
        return piePagina;
    }

    public void setPiePagina(String piePagina) {
        this.piePagina = piePagina;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSexo() {
        return sexo;
    }

    public void setSexo(String sexo) {
        this.sexo = sexo;
    }

    public String getEdad() {
        return edad;
    }

    public void setEdad(String edad) {
        this.edad = edad;
    }

    public String getEps() {
        return eps;
    }

    public void setEps(String eps) {
        this.eps = eps;
    }

    public String getContrato() {
        return contrato;
    }

    public void setContrato(String contrato) {
        this.contrato = contrato;
    }

    public String getServicio() {
        return servicio;
    }

    public void setServicio(String servicio) {
        this.servicio = servicio;
    }

    public String getMedico() {
        return medico;
    }

    public void setMedico(String medico) {
        this.medico = medico;
    }

    public String getHistoria() {
        return historia;
    }

    public void setHistoria(String historia) {
        this.historia = historia;
    }

    public String getSede() {
        return sede;
    }

    public void setSede(String sede) {
        this.sede = sede;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getTotalEmpresa() {
        return totalEmpresa;
    }

    public void setTotalEmpresa(String totalEmpresa) {
        this.totalEmpresa = totalEmpresa;
    }

    public String getTotalUsuario() {
        return totalUsuario;
    }

    public void setTotalUsuario(String totalUsuario) {
        this.totalUsuario = totalUsuario;
    }

    public String getTotalCopago() {
        return totalCopago;
    }

    public void setTotalCopago(String totalCopago) {
        this.totalCopago = totalCopago;
    }

    public String getTotalCuotaMod() {
        return totalCuotaMod;
    }

    public void setTotalCuotaMod(String totalCuotaMod) {
        this.totalCuotaMod = totalCuotaMod;
    }
    
}