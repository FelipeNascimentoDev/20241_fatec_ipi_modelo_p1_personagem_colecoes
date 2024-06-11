-- Active: 1717896789851@@mysql-3b3fa6b-felipenn-1st-project.c.aivencloud.com@28215@defaultdb


CREATE TABLE tb_atividade (
  cod_atividade INT AUTO_INCREMENT PRIMARY KEY,
  descricao VARCHAR(200),
  data_de_ocorrencia TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);


SELECT * FROM tb_atividade;


DROP TABLE tb_atividade;