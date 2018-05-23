
	
	
	
	
	
do $$


declare maxid integer;
begin
    select max(id_cama)+1 from cfg_cama into maxid;
    execute 'alter SEQUENCE cfg_cama_id_seq RESTART with '|| maxid;   

    select max(id_campoarchivo)+1 from cfg_campos_archivos_paciente into maxid;
    execute 'alter SEQUENCE cfg_campos_archivos_paciente_id_seq RESTART with '|| maxid;   

    select max(id_centro_costo)+1 from cfg_centro_costo into maxid;
    execute 'alter SEQUENCE cfg_centro_costo_id_centro_costo_seq RESTART with '|| maxid;  

	select max(id)+1 from cfg_clasificaciones into maxid;
    execute 'alter SEQUENCE cfg_clasificaciones_id_seq RESTART with '|| maxid;  

	select max(id_configuracion)+1 from cfg_configuraciones into maxid;
    execute 'alter SEQUENCE cfg_configuraciones_id_configuracion_seq RESTART with '|| maxid;  

    select max(id_consultorio)+1 from cfg_consultorios into maxid;
    execute 'alter SEQUENCE cfg_consultorios_id_consultorio_seq RESTART with '|| maxid; 

    select coalesce(max(id_copia),1)+1 from cfg_copias_seguridad into maxid;
    execute 'alter SEQUENCE cfg_copias_seguridad_id_copia_seq RESTART with '|| maxid; 


	select max(id_habitacion)+1 from cfg_habitacion into maxid;
    execute 'alter SEQUENCE cfg_habitacion_id_seq RESTART with '|| maxid; 
	
	select coalesce(max(id),1)+1 from cfg_historia_campos_predefinidos into maxid;
    execute 'alter SEQUENCE sq_cfg_historia_campos_predefinidos RESTART with '|| maxid; 

	select max(id_horario)+1 from cfg_horario into maxid;
    execute 'alter SEQUENCE cfg_horario_id_horario_seq RESTART with '|| maxid; 

	select max(id)+1 from cfg_imagenes into maxid;
    execute 'alter SEQUENCE cfg_imagenes_id_seq RESTART with '|| maxid; 
	
	select coalesce(max(id_insumo),1)+1 from cfg_insumo into maxid;
    execute 'alter SEQUENCE cfg_insumo_id_insumo_seq RESTART with '|| maxid; 
	
	select max(id_item_horario)+1 from cfg_items_horario into maxid;
    execute 'alter SEQUENCE cfg_items_horario_id_item_horario_seq RESTART with '|| maxid; 
	
	select coalesce(max(id_maestro),1)+1 from cfg_maestros_txt_predefinidos into maxid;
    execute 'alter SEQUENCE cfg_maestros_txt_predefinidos_id_maestro_seq RESTART with '|| maxid; 
	
	select max(id_medicamento)+1 from cfg_medicamento into maxid;
    execute 'alter SEQUENCE cfg_medicamento_id_medicamento_seq RESTART with '|| maxid; 
	
	select max(id_opcion_menu)+1 from cfg_opciones_menu into maxid;
    execute 'alter SEQUENCE cfg_opciones_menu_seq RESTART with '|| maxid; 
	
	select max(id_paciente)+1 from cfg_pacientes into maxid;
    execute 'alter SEQUENCE cfg_pacientes_id_seq RESTART with '|| maxid; 
	
	select max(id_perfil)+1 from cfg_perfiles_usuario into maxid;
    execute 'alter SEQUENCE cfg_perfiles_usuario_seq RESTART with '|| maxid; 

	select max(id_sede)+1 from cfg_sede into maxid;
    execute 'alter SEQUENCE cfg_sede_id_sede_seq RESTART with '|| maxid; 
	
	select coalesce(max(id_txt_predefinido),1)+1 from cfg_txt_predefinidos into maxid;
    execute 'alter SEQUENCE cfg_txt_predefinidos_id_txt_predefinido_seq RESTART with '|| maxid; 
	
	select max(id)+1 from cfg_unidad into maxid;
    execute 'alter SEQUENCE cfg_unidad_id_seq RESTART with '|| maxid; 
	
	select max(id_usuario)+1 from cfg_usuarios into maxid;
    execute 'alter SEQUENCE cfg_usuarios_id_usuario_seq RESTART with '|| maxid; 
	
	select coalesce(max(id_cita),1)+1 from cit_citas into maxid;
    execute 'alter SEQUENCE cit_citas_id_cita_seq RESTART with '|| maxid; 
	
	select coalesce(max(id_paq_maestro),1)+1 from cit_paq_maestro into maxid;
    execute 'alter SEQUENCE cit_paq_maestro_id_paq_maestro_seq RESTART with '|| maxid; 
	
	select max(id_turno)+1 from cit_turnos into maxid;
    execute 'alter SEQUENCE cit_turnos_id_turno_seq RESTART with '|| maxid; 
	
	select coalesce(max(id_paciente),1)+1 from completa into maxid;
    execute 'alter SEQUENCE cfg_pacientes_id_seq RESTART with '|| maxid; 
	
	select max(id_caja)+1 from fac_caja into maxid;
    execute 'alter SEQUENCE fac_caja_id_caja_seq RESTART with '|| maxid; 
	
	select coalesce(max(id_consecutivo),1)+1 from fac_consecutivo into maxid;
    execute 'alter SEQUENCE fac_consecutivo_id_consecutivo_seq RESTART with '|| maxid; 
	
	select coalesce(max(id_consumo_insumo),1)+1 from fac_consumo_insumo into maxid;
    execute 'alter SEQUENCE fac_consumo_insumo_id_consumo_insumo_seq RESTART with '|| maxid; 

	select coalesce(max(id_consumo_medicamento),1)+1 from fac_consumo_medicamento into maxid;
    execute 'alter SEQUENCE fac_consumo_medicamento_id_consumo_medicamento_seq RESTART with '|| maxid; 
	
	select coalesce(max(id_consumo_paquete),1)+1 from fac_consumo_paquete into maxid;
    execute 'alter SEQUENCE fac_consumo_paquete_id_consumo_paquete_seq RESTART with '|| maxid; 
	
	select coalesce(max(id_consumo_servicio),1)+1 from fac_consumo_servicio into maxid;
    execute 'alter SEQUENCE fac_consumo_servicio_id_consumo_servicio_seq RESTART with '|| maxid; 
	
	select coalesce(max(id_contrato),1)+1 from fac_contrato into maxid;
    execute 'alter SEQUENCE fac_contrato_id_contrato_seq RESTART with '|| maxid; 
	
	select coalesce(max(id_factura_admi),1)+1 from fac_factura_admi into maxid;
    execute 'alter SEQUENCE fac_factura_admi_id_factura_seq RESTART with '|| maxid; 
	
	select coalesce(max(id_factura_paciente),1)+1 from fac_factura_paciente into maxid;
    execute 'alter SEQUENCE fac_factura_id_factura_seq RESTART with '|| maxid; 

	select coalesce(max(id_impuesto),1)+1 from fac_impuestos into maxid;
    execute 'alter SEQUENCE fac_impuestos_id_impuesto_seq RESTART with '|| maxid; 
	
	select coalesce(max(id_manual_tarifario),1)+1 from fac_manual_tarifario into maxid;
    execute 'alter SEQUENCE fac_manual_tarifario_id_manual_tarifario_seq RESTART with '|| maxid; 
	
	select coalesce(max(id_movimiento),1)+1 from fac_movimiento_caja into maxid;
    execute 'alter SEQUENCE fac_movimiento_caja_id_movimiento_seq RESTART with '|| maxid; 
	
	select coalesce(max(id_paquete),1)+1 from fac_paquete into maxid;
    execute 'alter SEQUENCE fac_paquete_id_paquete_seq RESTART with '|| maxid; 
	
	select coalesce(max(id_periodo),1)+1 from fac_periodo into maxid;
    execute 'alter SEQUENCE fac_periodo_id_periodo_seq RESTART with '|| maxid; 
	
	select coalesce(max(id_programa),1)+1 from fac_programa into maxid;
    execute 'alter SEQUENCE fac_programa_id_programa_seq RESTART with '|| maxid; 
	
	select coalesce(max(id_servicio),1)+1 from fac_servicio into maxid;
    execute 'alter SEQUENCE fac_servicio_id_servicio_seq RESTART with '|| maxid; 
	
	select coalesce(max(id_campo),1)+1 from hc_campos_reg into maxid;
    execute 'alter SEQUENCE hc_campos_reg_id_campo_seq RESTART with '|| maxid; 
	
	select coalesce(max(id_estructura_familiar),1)+1 from hc_estructura_familiar into maxid;
    execute 'alter SEQUENCE hc_estructura_familiar_id_estructura_familiar_seq RESTART with '|| maxid; 
	
	select coalesce(max(id_item),1)+1 from hc_items into maxid;
    execute 'alter SEQUENCE hc_items_id_item_seq RESTART with '|| maxid; 
	
	select coalesce(max(id_registro),1)+1 from hc_registro into maxid;
    execute 'alter SEQUENCE hc_registro_id_registro_seq RESTART with '|| maxid; 
	
	select coalesce(max(id_rep_examenes),1)+1 from hc_rep_examenes into maxid;
    execute 'alter SEQUENCE hc_rep_examenes_id_rep_examenes_seq RESTART with '|| maxid; 
	
	select max(id_tipo_reg)+1 from hc_tipo_reg into maxid;
    execute 'alter SEQUENCE hc_tipo_reg_id_tipo_reg_seq RESTART with '|| maxid; 
	
	select max(id_bodega_producto)+1 from inv_bodega_productos into maxid;
    execute 'alter SEQUENCE inv_bodega_productos_id_bodega_producto_seq RESTART with '|| maxid; 
	
	select max(id_bodega)+1 from inv_bodegas into maxid;
    execute 'alter SEQUENCE inv_bodegas_id_bodeha_seq RESTART with '|| maxid; 
	
	select max(id_categoria)+1 from inv_categorias into maxid;
    execute 'alter SEQUENCE inv_categorias_id_categoria_seq RESTART with '|| maxid; 
	
	select coalesce(max(id_entrega),1)+1 from inv_entrega_medicamentos into maxid;
    execute 'alter SEQUENCE inv_entrega_medicamentos_id_entrega_seq RESTART with '|| maxid; 
	
	select coalesce(max(id_entrega_detalle),1)+1 from inv_entrega_medicamentos_detalle into maxid;
    execute 'alter SEQUENCE inv_entrega_medicamentos_detalle_id_entrega_detalle_seq RESTART with '|| maxid; 
	
	select coalesce(max(id_lote_producto),1)+1 from inv_lote_productos into maxid;
    execute 'alter SEQUENCE inv_lote_productos_id_lote_producto_seq RESTART with '|| maxid; 
	
	select coalesce(max(id_lote),1)+1 from inv_lotes into maxid;
    execute 'alter SEQUENCE inv_lotes_id_lote_seq RESTART with '|| maxid; 
	
	select coalesce(max(id_movimiento_producto),1)+1 from inv_movimiento_productos into maxid;
    execute 'alter SEQUENCE inv_movimiento_productos_id_movimiento_producto_seq RESTART with '|| maxid; 
	
	select coalesce(max(id_movimiento),1)+1 from inv_movimientos into maxid;
    execute 'alter SEQUENCE inv_movimientos_id_movimiento_seq RESTART with '|| maxid; 
	
	select coalesce(max(id_orden_compra),1)+1 from inv_orden_compra into maxid;
    execute 'alter SEQUENCE inv_orden_compra_id_seq RESTART with '|| maxid; 
	
	select coalesce(max(id_orden_compra_producto),1)+1 from inv_orden_compra_productos into maxid;
    execute 'alter SEQUENCE inv_proveedor_productos_id_proveedor_producto_seq RESTART with '|| maxid; 
	
	select coalesce(max(id_producto),1)+1 from inv_productos into maxid;
    execute 'alter SEQUENCE inv_productos_id_seq RESTART with '|| maxid; 
	
	select max(id_proveedor_producto)+1 from inv_proveedor_productos into maxid;
    execute 'alter SEQUENCE inv_proveedor_productos_id_proveedor_producto_seq RESTART with '|| maxid; 
	
	select max(id_proveedor)+1 from inv_proveedores into maxid;
    execute 'alter SEQUENCE inv_proveedores_id_proveedor_seq RESTART with '|| maxid; 
	
	select max(id_programa)+1 from pyp_programa into maxid;
    execute 'alter SEQUENCE pyp_programa_id_programa_seq RESTART with '|| maxid; 
	
	select coalesce(max(id_programa_asoc),1)+1 from pyp_programa_asoc into maxid;
    execute 'alter SEQUENCE pyp_programa_asoc_id_programa_asoc_seq RESTART with '|| maxid; 
	
	select coalesce(max(id_pyp_programa_cita),1)+1 from pyp_programa_cita into maxid;
    execute 'alter SEQUENCE pyp_programa_cita_id_pyp_programa_cita_seq RESTART with '|| maxid;
	
	select coalesce(max(id_programa_items),1)+1 from pyp_programa_item into maxid;
    execute 'alter SEQUENCE pyp_programa_item_id_programa_items_seq RESTART with '|| maxid;
	
	select coalesce(max(id_rip_almacenado),1)+1 from rips_almacenados into maxid;
    execute 'alter SEQUENCE rips_almacenados_id_rip_almacenado_seq RESTART with '|| maxid;
	
	select coalesce(max(id_nodo),1)+1 from sin_nodos into maxid;
    execute 'alter SEQUENCE sin_nodos_id_nodo_seq RESTART with '|| maxid;
	
	select coalesce(max(id_tabla),1)+1 from sin_tablas into maxid;
    execute 'alter SEQUENCE sin_tablas_id_tabla_seq RESTART with '|| maxid;
	
	select coalesce(max(id_admision),1)+1 from urg_admision into maxid;
    execute 'alter SEQUENCE urg_admision_id_seq RESTART with '|| maxid;
	
	select coalesce(max(id_consulta),1)+1 from urg_consulta_paciente into maxid;
    execute 'alter SEQUENCE urg_consulta_urgencia_id_seq RESTART with '|| maxid;
	
	select max(id_control_prescripcion)+1 from urg_control_prescripcion_medicamentos into maxid;
    execute 'alter SEQUENCE urg_control_prescripcion_medicamentos_id_seq RESTART with '|| maxid;
	
	select max(id_detalleconsulta)+1 from urg_detalle_consulta_paciente into maxid;
    execute 'alter SEQUENCE urg_detalle_consulta_id_seq RESTART with '|| maxid;
	
	select max(id_nota)+1 from urg_notas_enfermerias into maxid;
    execute 'alter SEQUENCE urg_notas_enfermerias_id_seq RESTART with '|| maxid;
	
	select max(id_nota)+1 from urg_notas_medicas into maxid;
    execute 'alter SEQUENCE urg_notas_medicas_id_seq RESTART with '|| maxid;
	
	select max(id_orden_cobro)+1 from urg_orden_cobro into maxid;
    execute 'alter SEQUENCE urg_orden_cobro_id_seq RESTART with '|| maxid;
	
	select max(id_destino)+1 from urg_plan_manejo_paciente into maxid;
    execute 'alter SEQUENCE urg_destino_paciente_id_seq RESTART with '|| maxid;
	
	select max(id_prescripcion)+1 from urg_prescripcion_medicamentos into maxid;
    execute 'alter SEQUENCE urg_prescripcion_medicamentos_id_seq RESTART with '|| maxid;
	
	select max(id_triage)+1 from urg_triage into maxid;
    execute 'alter SEQUENCE urg_admision_id_seq RESTART with '|| maxid;
	
	select max(id)+1 from xlab_estudio into maxid;
    execute 'alter SEQUENCE xlab_estudio_id_seq RESTART with '|| maxid;
	
	select coalesce(max(id),1)+1 from xlab_orden into maxid;
    execute 'alter SEQUENCE xlab_orden_id_seq1 RESTART with '|| maxid;
	
	select coalesce(max(id),1)+1 from xlab_orden_estudio_prueba_resultados into maxid;
    execute 'alter SEQUENCE xlab_orden_estudio_prueba_resultados_id_seq RESTART with '|| maxid;
	
	select coalesce(max(id),1)+1 from xlab_orden_estudios_pruebas into maxid;
    execute 'alter SEQUENCE xlab_orden_estudios_pruebas_id_seq RESTART with '|| maxid;

	select coalesce(max(id),1)+1 from xlab_prueba into maxid;
    execute 'alter SEQUENCE xlab_prueba_id_seq RESTART with '|| maxid;
	
	select coalesce(max(id),1)+1 from xlab_prueba_referencia into maxid;
    execute 'alter SEQUENCE xlab_prueba_referencia_id_seq RESTART with '|| maxid;
	
	select coalesce(max(id),1)+1 from xlab_tipo_tecnica into maxid;
    execute 'alter SEQUENCE xlab_tipo_tecnica_id_seq RESTART with '|| maxid;
end;
$$ language plpgsql