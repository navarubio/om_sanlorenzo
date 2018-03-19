insert into hc_campos_reg (select nextval('hc_campos_reg_id_campo_seq')+1,85,'TUMORES',null,151,'TUMORES');
insert into hc_campos_reg (select nextval('hc_campos_reg_id_campo_seq')+1,85,'MENTALES',null,152,'MENTALES');
insert into hc_campos_reg (select nextval('hc_campos_reg_id_campo_seq')+1,85,'OBSERVACIONES',null,153,'OBSERVACIONES');
insert into hc_campos_reg (select nextval('hc_campos_reg_id_campo_seq')+1,85,'HIPERTENSION',null,154,'HIPERTENSION');
insert into hc_campos_reg (select nextval('hc_campos_reg_id_campo_seq')+1,85,'DIABETES',null,155,'DIABETES');
insert into hc_campos_reg (select nextval('hc_campos_reg_id_campo_seq')+1,85,'CARDIOPATIAS',null,156,'CARDIOPATIAS');
insert into hc_campos_reg (select nextval('hc_campos_reg_id_campo_seq')+1,85,'HEPATOPATIAS',null,157,'HEPATOPATIAS');
insert into hc_campos_reg (select nextval('hc_campos_reg_id_campo_seq')+1,85,'PROSPECCION',null,158,'PROSPECCION');
insert into hc_campos_reg (select nextval('hc_campos_reg_id_campo_seq')+1,85,'TRATAMIENTO CITOLOGIA',null,159,'TRATAMIENTO CITOLOGIA');
insert into hc_campos_reg (select nextval('hc_campos_reg_id_campo_seq')+1,85,'NARIZ',null,160,'NARIZ');
insert into hc_campos_reg (select nextval('hc_campos_reg_id_campo_seq')+1,85,'OIDO',null,161,'OIDO');
update hc_campos_reg set tabla='date2' where id_Campo=112520;
update hc_tipo_reg set cant_campos=162,url_pagina='g_historia_clinica_planificacion_familiar.xhtml'  where id_Tipo_reg=85;
update hc_Campos_Reg set nombre='TELEFONO', nombre_pdf='TELEFONO' where id_Campo=112490;

insert into hc_campos_reg (select nextval('hc_campos_reg_id_campo_seq')+1,84,'TELEFONO:',null,193,'TELEFONO:');
insert into hc_campos_reg (select nextval('hc_campos_reg_id_campo_seq')+1,84,'TOXICO:',null,194,'TOXICO:');
insert into hc_campos_reg (select nextval('hc_campos_reg_id_campo_seq')+1,84,'FARMACOLOGICO:',null,195,'FARMACOLOGICO:');
insert into hc_campos_reg (select nextval('hc_campos_reg_id_campo_seq')+1,84,'CUANTO MIDIO AL NACER:',null,196,'CUANTO MIDIO AL NACER:');
insert into hc_campos_reg (select nextval('hc_campos_reg_id_campo_seq')+1,84,'QUIRURGICOS:',null,197,'QUIRURGICOS:');
insert into hc_campos_reg (select nextval('hc_campos_reg_id_campo_seq')+1,84,'HEMOCLASIFICACION:',null,198,'HEMOCLASIFICACION:');
update hc_tipo_reg set url_pagina='g_aiepi_menor_2_meses.xhtml' where id_tipo_reg=84;
update hc_tipo_reg set cant_campos=199  where id_Tipo_reg=84;
update hc_campos_reg set nombre='TSH' where id_campo=112471;


insert into hc_campos_reg (select nextval('hc_campos_reg_id_campo_seq')+1,83,'Parentesco:',null,224,'Parentesco:');
insert into hc_campos_reg (select nextval('hc_campos_reg_id_campo_seq')+1,83,'Telefono:',null,225,'Telefono:');
update hc_campos_Reg set nombre='CITA DE CONTROL' where id_campo=112294;
update hc_tipo_reg set url_pagina='s_historia_prevencion_enfermedades_cronicas.xhtml',cant_campos=226 where id_tipo_reg=83;
update hc_campos_reg set tabla='date2' where id_Campo=112462;
