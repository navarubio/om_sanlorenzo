/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 * Author:  gfernandez
 * Created: 19/04/2018
 */

-- Remuevo ceros a la izquierda sobre las clasificaciones de Código de Pertenencia Etnica.
UPDATE public.cfg_clasificaciones 
	SET codigo = regexp_replace(codigo, '[0]','','gi')
	FROM public.cfg_maestros_clasificaciones 
	WHERE public.cfg_maestros_clasificaciones.maestro = public.cfg_clasificaciones.maestro AND public.cfg_maestros_clasificaciones.maestro = 'Etnia';

