/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beans.enumeradores;

/**
 *
 * @author miguel
 */
public enum TipoInventarioEnum {
    //Tipo de consecutivo de la tabla inv_consecutivos
    B,//Tipo Bodega
    F,//Tipo Farma
    O,//Tipo Orden de compras,
    E,//Tipo entrada orden de compra,
    M,//Entrega de Medicamentos
    //ESTADOS ORDEN
    P,A,N,C, //P:Pendiente,A:Parcial, N:Anulado,C:Cerrada,
    S,//Salida,
    
    
}
