insert into hc_tipo_reg
values(86,'INCAPACIDAD MEDICA','s_incapacidad_medica.xhtml',true,6,0);
insert into hc_campos_reg (select nextval('hc_campos_reg_id_campo_seq')+1,86,'CAUSA EXTERNA:',null,0,'CAUSA EXTERNA:');
insert into hc_campos_reg (select nextval('hc_campos_reg_id_campo_seq')+1,86,'FECHA INICIO INCAPACIDAD:','date2',1,'FECHA INICIO INCAPACIDAD:');
insert into hc_campos_reg (select nextval('hc_campos_reg_id_campo_seq')+1,86,'FECHA FINAL INCAPACIDAD:','date2',2,'FECHA FINAL INCAPACIDAD:');
insert into hc_campos_reg (select nextval('hc_campos_reg_id_campo_seq')+1,86,'DIAS DE INCAPACIDAD:',null,3,'DIAS DE INCAPACIDAD:');
insert into hc_campos_reg (select nextval('hc_campos_reg_id_campo_seq')+1,86,'INCAPACIDAD:',null,4,'INCAPACIDAD:');
insert into hc_campos_reg (select nextval('hc_campos_reg_id_campo_seq')+1,86,'OBSERVACIONES::',null,5,'OBSERVACIONES::');
