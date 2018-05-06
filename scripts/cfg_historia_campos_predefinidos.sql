-- Sequence: sq_cfg_historia_campos_predefinidos

-- DROP SEQUENCE sq_cfg_historia_campos_predefinidos;

CREATE SEQUENCE sq_cfg_historia_campos_predefinidos
  INCREMENT 1
  MINVALUE 1
  MAXVALUE 9223372036854775807
  START 1
  CACHE 1;
ALTER TABLE sq_cfg_historia_campos_predefinidos
  OWNER TO postgres;


-- Table: cfg_historia_campos_predefinidos

-- DROP TABLE cfg_historia_campos_predefinidos;

CREATE TABLE cfg_historia_campos_predefinidos
(
  id integer NOT NULL DEFAULT nextval('sq_cfg_historia_campos_predefinidos'::regclass),
  id_campo integer,
  valor text,
  CONSTRAINT pk_cfg_historia_campos_predefinidos_id PRIMARY KEY (id),
  CONSTRAINT fk_id_campo_cfg_historia FOREIGN KEY (id_campo)
      REFERENCES hc_campos_reg (id_campo) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);
ALTER TABLE cfg_historia_campos_predefinidos
  OWNER TO postgres;
GRANT ALL ON TABLE cfg_historia_campos_predefinidos TO public;
GRANT ALL ON TABLE cfg_historia_campos_predefinidos TO postgres;
