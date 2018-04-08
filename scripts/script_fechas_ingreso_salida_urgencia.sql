insert into hc_campos_reg values((select max(id_campo)+1 from hc_campos_reg),20,'FECHA DE INGRESO','date',162,'FECHA DE INGRESO : ');
insert into hc_campos_reg values((select max(id_campo)+1 from hc_campos_reg),20,'FECHA DE SALIDA','date',163,'FECHA DE SALIDA : ');
update hc_tipo_reg set  cant_campos=164 where id_tipo_reg=20;
