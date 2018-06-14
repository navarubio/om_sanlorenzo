insert into hc_Tipo_reg values(91,'VALORACION DE CRECIMIENTO PARA NIÑOS DE 0 A 5 AÑOS','s_valoraciondecrecimientoninos0a5.xhtml',true,3,0);

insert into hc_campos_reg (select nextval('hc_campos_reg_id_campo_seq')+1,91,'PESO AL NACER',null,0,'PESO AL NACER:');
insert into hc_campos_reg (select nextval('hc_campos_reg_id_campo_seq')+1,91,'TALLA AL NACER',null,1,'TALLA AL NACER:');
insert into hc_campos_reg (select nextval('hc_campos_reg_id_campo_seq')+1,91,'PERIMETRO CEFALICO',null,2,'PERIMETRO CEFALICO:');