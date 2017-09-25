/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managedBeans.configuraciones;

import beans.utilidades.MetodosGenerales;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.model.SelectItem;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import modelo.entidades.CfgCamposArchivosPaciente;
import modelo.entidades.FacAdministradora;
import modelo.fachadas.CfgCamposArchivosPacienteFacade;
import org.primefaces.context.RequestContext;

/**
 *
 * @author Familia Ibañez Duran
 */
@Named(value = "camposArchivosMB")
@ViewScoped
public class CamposArchivosMB extends MetodosGenerales implements Serializable {
    //---------------------------------------------------
    //-----------------FACHADAS -------------------------
    //--------------------------------------------------- 

    @EJB
    private CfgCamposArchivosPacienteFacade archivoFacade;

    
    private FacAdministradora administradora;
    private CfgCamposArchivosPaciente campo;
    private List<CfgCamposArchivosPaciente> lstCampoArchivos;
    private List<SelectItem> listaCampo;
    /**
     * Creates a ne w instance of CamposArchivosMB
     */
    public CamposArchivosMB() {
        administradora = new FacAdministradora();
        campo =new CfgCamposArchivosPaciente();
        lstCampoArchivos = new ArrayList<>();
        listaCampo = new ArrayList();
        cargarListaCampo();
        
    }

    @PostConstruct
    public void init() {

    }
    
    public FacAdministradora getAdministradora() {
        return administradora;
    }

    public void setAdministradora(FacAdministradora administradora) {
        this.administradora = administradora;
    }

    public void buscar() {
        lstCampoArchivos = archivoFacade.getArchivosXAdministrado(administradora.getIdAdministradora());
        if(lstCampoArchivos.isEmpty()){
            lstCampoArchivos = new ArrayList();
            for(int i=0;i<listaCampo.size();i++){
                campo = new CfgCamposArchivosPaciente();
                campo.setOrden(i);
                campo.setNombreCampo(listaCampo.get(i).getValue().toString());
                campo.setIdAdministradora(administradora);
                archivoFacade.create(campo);
                lstCampoArchivos.add(campo);
            }
        }
            
    }
    public List<CfgCamposArchivosPaciente> getLstCampoArchivos() {
        return lstCampoArchivos;
    }

    public void setLstCampoArchivos(List<CfgCamposArchivosPaciente> lstCampoArchivos) {
        this.lstCampoArchivos = lstCampoArchivos;
    }

    public List<SelectItem> getListaCampo() {
        return listaCampo;
    }

    public void setListaCampo(List<SelectItem> listaCampo) {
        this.listaCampo = listaCampo;
    }

    public CfgCamposArchivosPaciente getCampo() {
        return campo;
    }

    public void setCampo(CfgCamposArchivosPaciente campo) {
        this.campo = campo;
    }

    public void guardar() {
        try {
            int existeCampo = 0;
            if (validacionCampoVacio(campo.getOrden().toString(), "Orden campo")) {
                return;
            }
            if (validacionCampoVacio(campo.getNombreCampo(), "Nombre campo")) {
                return;
            }

            if (campo.getIdCampoarchivo() == null) {//nuevo registro
                existeCampo = archivoFacade.existeCampoAdministradora(administradora.getIdAdministradora(), campo.getOrden().toString(),campo.getNombreCampo());
                if (existeCampo == 0) {
                        campo.setIdAdministradora(administradora);
                        archivoFacade.create(campo);
                        lstCampoArchivos.add(campo);
                        imprimirMensaje("Guardado", "Guardado Correctamente", FacesMessage.SEVERITY_INFO);
                        RequestContext.getCurrentInstance().update("tablaCampos");
                    
                } else {
                    imprimirMensaje("Error al guardar", "El campo ya esta configurado en este orden", FacesMessage.SEVERITY_FATAL);
                }
            } else {//actualizamos
                campo.setIdAdministradora(administradora);
                archivoFacade.edit(campo);
                imprimirMensaje("Guardado", "Guardado Correctamente", FacesMessage.SEVERITY_INFO);
            }
            campo = new CfgCamposArchivosPaciente();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void eliminar(CfgCamposArchivosPaciente campo){
        archivoFacade.remove(campo);
        imprimirMensaje("Eliminado", "Eliminado Correctamente", FacesMessage.SEVERITY_INFO);
        lstCampoArchivos = archivoFacade.getArchivosXAdministrado(administradora.getIdAdministradora());
        RequestContext.getCurrentInstance().update("idFormCamposArchivos");
    }
    
    public void editar(CfgCamposArchivosPaciente campo){
        this.campo = campo;
        RequestContext.getCurrentInstance().update("idFormCamposArchivos");
        RequestContext.getCurrentInstance().execute("PF('modalagregarCampo').show();");
    }
    private void cargarListaCampo() {
       listaCampo.add(new SelectItem("IDENTIFICACION","IDENTIFICACION"));
        listaCampo.add(new SelectItem("TIPO IDENTIFICACION","TIPO IDENTIFICACION"));
        listaCampo.add(new SelectItem("LUGAR DE EXPEDICION","LUGAR DE EXPEDICION"));
        listaCampo.add(new SelectItem("FECHA NACIMIENTO","FECHA NACIMIENTO"));
        listaCampo.add(new SelectItem("EDAD","EDAD"));
        listaCampo.add(new SelectItem("GENERO","GENERO"));
        listaCampo.add(new SelectItem("GRUPO SANGUINEO","GRUPO SANGUINEO"));
        listaCampo.add(new SelectItem("PRIMER NOMBRE","PRIMER NOMBRE"));
        listaCampo.add(new SelectItem("SEGUNDO NOMBRE","SEGUNDO NOMBRE"));
        listaCampo.add(new SelectItem("PRIMER APELLIDO","PRIMER APELLIDO"));
        listaCampo.add(new SelectItem("SEGUNDO APELLIDO","SEGUNDO APELLIDO"));
        listaCampo.add(new SelectItem("OCUPACION","OCUPACION"));
        listaCampo.add(new SelectItem("ESTADO CIVIL","ESTADO CIVIL"));
        listaCampo.add(new SelectItem("TEL RESIDENCIA","TEL RESIDENCIA"));
        listaCampo.add(new SelectItem("TEL OFICINA","TEL OFICINA"));
        listaCampo.add(new SelectItem("CELULAR","CELULAR"));
        listaCampo.add(new SelectItem("DEPARTAMENTO","DEPARTAMENTO"));
        listaCampo.add(new SelectItem("MUNICIPIO","MUNICIPIO"));
        listaCampo.add(new SelectItem("ZONA","ZONA"));
        listaCampo.add(new SelectItem("DIRECCION","DIRECCION"));
        listaCampo.add(new SelectItem("BARRIO","BARRIO"));
        listaCampo.add(new SelectItem("EMAIL","EMAIL"));
        listaCampo.add(new SelectItem("ACTIVO","ACTIVO"));
        listaCampo.add(new SelectItem("ADMINISTRADORA","ADMINISTRADORA"));
        listaCampo.add(new SelectItem("TIPO AFILIADO","TIPO AFILIADO"));
        listaCampo.add(new SelectItem("REGIMEN","REGIMEN"));
        listaCampo.add(new SelectItem("CATEGORIA PACIENTE","CATEGORIA PACIENTE"));
        listaCampo.add(new SelectItem("NIVEL","NIVEL"));
        listaCampo.add(new SelectItem("ETNIA","ETNIA"));
        listaCampo.add(new SelectItem("ESCOLARIDAD","ESCOLARIDAD"));
        listaCampo.add(new SelectItem("RESPONSABLE","RESPONSABLE"));
        listaCampo.add(new SelectItem("TEL RESPONSABLE","TEL RESPONSABLE"));
        listaCampo.add(new SelectItem("PARENTESCO","PARENTESCO"));
        listaCampo.add(new SelectItem("ACOMPAÑANTE","ACOMPAÑANTE"));
        listaCampo.add(new SelectItem("TEL ACOMPAÑANTE","TEL ACOMPAÑANTE"));
        listaCampo.add(new SelectItem("FECHA AFILIACION","FECHA AFILIACION"));
        listaCampo.add(new SelectItem("FECHA SISBEN","FECHA SISBEN"));
        listaCampo.add(new SelectItem("CARNET","CARNET"));
        listaCampo.add(new SelectItem("FECHA VENCE CARNET","FECHA VENCE CARNET"));
        listaCampo.add(new SelectItem("OBSERVACIONES","OBSERVACIONES"));
        
    }

}
