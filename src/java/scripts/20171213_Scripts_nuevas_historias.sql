/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 * Author:  casc
 * Created: 13/12/2017
 */



--Division de cancer cervyx y seno
INSERT INTO hc_tipo_reg 
SELECT 78, 'HISTORIA CLINICA DE CERVIX' nombre, 
'historiaclinicaprevencioncancercervix.xhtml' url_pagina, activo, cant_campos, consecutivo
  FROM public.hc_tipo_reg where id_tipo_reg=53;
INSERT INTO public.hc_campos_reg(id_tipo_reg, nombre, tabla, posicion, nombre_pdf)
SELECT 78 id_tipo_reg, nombre, tabla, posicion, nombre_pdf
  FROM public.hc_campos_reg where id_tipo_reg=53;

INSERT INTO hc_tipo_reg 
SELECT 79, 'HISTORIA CLINICA DE SENO' nombre, 
'historiaclinicaprevencioncancerseno.xhtml' url_pagina, activo, cant_campos, consecutivo
  FROM public.hc_tipo_reg where id_tipo_reg=53;
  
INSERT INTO public.hc_campos_reg(id_tipo_reg, nombre, tabla, posicion, nombre_pdf)
SELECT 79 id_tipo_reg, nombre, tabla, posicion, nombre_pdf
  FROM public.hc_campos_reg where id_tipo_reg=53;

--Inhailitar cancer de servx y seno
update public.hc_tipo_reg  set activo=false where id_tipo_reg=53

--Nuevo campo EPICRISIS
INSERT INTO public.hc_campos_reg(id_tipo_reg, nombre, tabla, posicion, nombre_pdf) VALUES (29, 'NOTAS ACLARATORIAS' , NULL, 16 , 'NOTAS ACLARATORIAS' );


--ENTREGA DE CITOLOGIA
insert into hc_tipo_reg
SELECT 80, 'ENTREGA RESULTADOS CITOLOGIA' nombre, 
'g_resultados_citologia.xhtml' url_pagina,true as activo, 6 cant_campos, consecutivo
  FROM public.hc_tipo_reg where id_tipo_reg=53;


INSERT INTO public.hc_campos_reg(id_tipo_reg, nombre, tabla, posicion, nombre_pdf) 
VALUES (80, 'Acompañante' , NULL, 0 , 'ACOMPAÑANTE' );
INSERT INTO public.hc_campos_reg(id_tipo_reg, nombre, tabla, posicion, nombre_pdf) 
VALUES (80, 'Parentesco' , 'cfg_clasificaciones', 1 , 'PARENTESCO' );
INSERT INTO public.hc_campos_reg(id_tipo_reg, nombre, tabla, posicion, nombre_pdf) 
VALUES (80, 'TELEFONO', '', 2 , 'TELEFONO' );
INSERT INTO public.hc_campos_reg(id_tipo_reg, nombre, tabla, posicion, nombre_pdf) 
VALUES (80, 'DESCRIPCION DEL RESULTADO' , '', 3 , 'DESCRIPCION DEL RESULTADO' );
INSERT INTO public.hc_campos_reg(id_tipo_reg, nombre, tabla, posicion, nombre_pdf) 
VALUES (80, 'OBSERVACIONES' , '', 4 , 'OBSERVACIONES' );
INSERT INTO public.hc_campos_reg(id_tipo_reg, nombre, tabla, posicion, nombre_pdf) 
VALUES (80, 'CONDUCTA' , '', 5 , 'CONDUCTA' );



--SUMINISTRO  DE PLANIFICACION FAMILIAR
insert into hc_tipo_reg
SELECT 81, 'SUMINISTRO DE PLANIFICACION FAMILIAR' nombre, 
'g_suministro_planificacion_familiar.xhtml' url_pagina,true as activo, 6 cant_campos, consecutivo
  FROM public.hc_tipo_reg where id_tipo_reg=80;


INSERT INTO public.hc_campos_reg(id_tipo_reg, nombre, tabla, posicion, nombre_pdf) 
VALUES (81, 'Acompañante' , NULL, 0 , 'ACOMPAÑANTE' );
INSERT INTO public.hc_campos_reg(id_tipo_reg, nombre, tabla, posicion, nombre_pdf) 
VALUES (81, 'Parentesco' , 'cfg_clasificaciones', 1 , 'PARENTESCO' );
INSERT INTO public.hc_campos_reg(id_tipo_reg, nombre, tabla, posicion, nombre_pdf) 
VALUES (81, 'TELEFONO', '', 2 , 'TELEFONO' );
INSERT INTO public.hc_campos_reg(id_tipo_reg, nombre, tabla, posicion, nombre_pdf) 
VALUES (81, 'MOTIVO DE CONSULTA' , '', 3 , 'MOTIVO DE CONSULTA' );
INSERT INTO public.hc_campos_reg(id_tipo_reg, nombre, tabla, posicion, nombre_pdf) 
VALUES (81, 'ENFERMEDAD ACTUAL' , '', 4 , 'ENFERMEDAD ACTUAL' );
INSERT INTO public.hc_campos_reg(id_tipo_reg, nombre, tabla, posicion, nombre_pdf) 
VALUES (81, 'REVISION POR SISTEMA' , '', 5 , 'REVISION POR SISTEMA' );
INSERT INTO public.hc_campos_reg(id_tipo_reg, nombre, tabla, posicion, nombre_pdf) 
VALUES (81, 'USUARIA REFIERE' , '', 6 , 'USUARIA REFIERE' );
INSERT INTO public.hc_campos_reg(id_tipo_reg, nombre, tabla, posicion, nombre_pdf) 
VALUES (81, 'FRECUENCIA CARDIACA' , '', 7 , 'FRECUENCIA CARDIACA' );
INSERT INTO public.hc_campos_reg(id_tipo_reg, nombre, tabla, posicion, nombre_pdf) 
VALUES (81, 'FRECUENCIA RESPIRATORIA' , '', 8 , 'FRECUENCIA RESPIRATORIA' );
INSERT INTO public.hc_campos_reg(id_tipo_reg, nombre, tabla, posicion, nombre_pdf) 
VALUES (81, 'TEMPERATURA' , '', 9 , 'TEMPERATURA' );
INSERT INTO public.hc_campos_reg(id_tipo_reg, nombre, tabla, posicion, nombre_pdf) 
VALUES (81, 'PESO' , '', 10, 'PESO' );
INSERT INTO public.hc_campos_reg(id_tipo_reg, nombre, tabla, posicion, nombre_pdf) 
VALUES (81, 'TALLA' , '', 11, 'TALLA' );
INSERT INTO public.hc_campos_reg(id_tipo_reg, nombre, tabla, posicion, nombre_pdf) 
VALUES (81, 'IMC' , '', 12, 'IMC' );
INSERT INTO public.hc_campos_reg(id_tipo_reg, nombre, tabla, posicion, nombre_pdf) 
VALUES (81, 'EXAMEN FISICO' , '', 13 , 'EXAMEN FISICO' );
INSERT INTO public.hc_campos_reg(id_tipo_reg, nombre, tabla, posicion, nombre_pdf) 
VALUES (81, 'DIAGNOSTICO' , '', 14 , 'DIAGNOSTICO' );
INSERT INTO public.hc_campos_reg(id_tipo_reg, nombre, tabla, posicion, nombre_pdf) 
VALUES (81, 'CONDUCTA' , '', 15 , 'CONDUCTA' );
INSERT INTO public.hc_campos_reg(id_tipo_reg, nombre, tabla, posicion, nombre_pdf) 
VALUES (81, 'PLAN' , '', 16 , 'PLAN' );
