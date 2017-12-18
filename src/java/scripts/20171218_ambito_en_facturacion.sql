/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 * Author:  casc
 * Created: 18/12/2017
 */

--Campos para ingresar el ambito
alter table fac_factura_paciente add column ambito integer;
alter table fac_factura_paciente add column fecha_desde_ambito date;
alter table fac_factura_paciente add column fecha_hasta_ambito date;

--Nueva clasificacion de ambito para facturación
INSERT INTO public.cfg_clasificaciones(
            codigo, maestro, descripcion, observacion)
    select '1','AmbitoFactura','Consulta Externa', null;
INSERT INTO public.cfg_clasificaciones(
            codigo, maestro, descripcion, observacion)
    select '2','AmbitoFactura','Urgencia', null;
INSERT INTO public.cfg_clasificaciones(
            codigo, maestro, descripcion, observacion)
    select '3','AmbitoFactura','Hospitalizacion', null;