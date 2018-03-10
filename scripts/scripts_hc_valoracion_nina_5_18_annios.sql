insert into hc_campos_reg
values((select max(id_campo)+1 from hc_campos_reg),69,'PESO','',2,'PESO : ');

insert into hc_campos_reg
values((select max(id_campo)+1 from hc_campos_reg),69,'TALLA','',3,'TALLA : ');

update hc_tipo_reg set cant_Campos=4 where id_tipo_reg=69;
