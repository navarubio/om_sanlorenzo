ALTER TABLE inv_bodega_productos
ADD COLUMN id_lote integer,
ADD CONSTRAINT fk_id_lote_inv_bodega_productos FOREIGN KEY (id_lote)
      REFERENCES public.inv_lotes (id_lote) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION;
	  
	  
ALTER TABLE inv_entrega_medicamentos_detalle
ADD COLUMN id_lote integer,
  ADD CONSTRAINT fk_inv_entrega_medicamentos_detalle_id_lote FOREIGN KEY (id_lote)
      REFERENCES public.inv_lotes (id_lote) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION;
	  
ALTER TABLE inv_orden_compra
ADD COLUMN id_lote integer,
  ADD CONSTRAINT fk_inv_orden_compra_id_lote FOREIGN KEY (id_lote)
      REFERENCES public.inv_lotes (id_lote) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION;
	  
ALTER TABLE inv_lotes
ADD COLUMN id_empresa integer,
  ADD CONSTRAINT fk_inv_lote_id_empresa FOREIGN KEY (id_empresa)
      REFERENCES public.cfg_empresa (cod_empresa) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION;	  