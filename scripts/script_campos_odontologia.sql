update hc_tipo_reg set cant_campos=1521 where id_tipo_reg=55;
insert into hc_campos_reg (select nextval('hc_campos_reg_id_campo_seq')+1,55,'ENCIAS:',null,1480,'ENCIAS:');
