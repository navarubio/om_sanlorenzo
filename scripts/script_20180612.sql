//Scripts para "-HISTORIA CLINICA MATERNA"
update hc_campos_Reg set posicion=-1 where id_Tipo_reg=18 and posicion is null;

//Script para enfermedades crónicas
update hc_tipo_reg set url_pagina='s_historia_prevencion_enfermedades_cronicas.xhtml' where id_tipo_reg=83;
