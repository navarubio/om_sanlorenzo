insert into hc_campos_reg
values((select max(id_campo)+1 from hc_campos_reg),71,'PERIMETRO CEFALICO','',2,'PERIMETRO CEFALICO : ');
update hc_tipo_reg set cant_Campos=3 where id_tipo_reg=71;