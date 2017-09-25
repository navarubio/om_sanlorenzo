/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managedBeans.facturacion;

import java.util.List;

/**
 *
 * @author jcores
 */
public class EstructuraDetalladas { 

    private String titulo = "";
    private String nitEmpresa = "";
    private String regimenEmpresa = "";
    private String piePagina="";
    private String subtitulo = "";
    private String eps = "";
    private String contrato = "";
    private String total_facturas = "";
    private String total_general = "";
    private String logoEmpresa = "";
    private List<EstructuraItemsDetalladas> listaItemsFactura;

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
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

    public String getPiePagina() {
        return piePagina;
    }

    public void setPiePagina(String piePagina) {
        this.piePagina = piePagina;
    }

    public String getSubtitulo() {
        return subtitulo;
    }

    public void setSubtitulo(String subtitulo) {
        this.subtitulo = subtitulo;
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

    public String getTotal_facturas() {
        return total_facturas;
    }

    public void setTotal_facturas(String total_facturas) {
        this.total_facturas = total_facturas;
    }

    public String getTotal_general() {
        return total_general;
    }

    public void setTotal_general(String total_general) {
        this.total_general = total_general;
    }

    public String getLogoEmpresa() {
        return logoEmpresa;
    }

    public void setLogoEmpresa(String logoEmpresa) {
        this.logoEmpresa = logoEmpresa;
    }

    public List<EstructuraItemsDetalladas> getListaItemsFactura() {
        return listaItemsFactura;
    }

    public void setListaItemsFactura(List<EstructuraItemsDetalladas> listaItemsFactura) {
        this.listaItemsFactura = listaItemsFactura;
    }

}
