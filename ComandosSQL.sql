-- Active: 1717896789851@@mysql-3b3fa6b-felipenn-1st-project.c.aivencloud.com@28215@defaultdb

-- Sobre a Tabela Atividades:

CREATE TABLE tb_atividade (
  cod_atividade INT AUTO_INCREMENT PRIMARY KEY,
  descricao VARCHAR(200),
  data_de_ocorrencia TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);


SELECT * FROM tb_atividade;


DROP TABLE tb_atividade;


-- Ajusta a tb_atividade colocando o id_usuario e linkando as tabelas com fk


ALTER TABLE tb_atividade 
ADD COLUMN fk_id_usuario INT;


ALTER TABLE tb_atividade
ADD CONSTRAINT fk_usuario_atividade FOREIGN KEY (fk_id_usuario) REFERENCES tb_usuario(id_usuario);

-- Sobre o Usuario e Senha:


CREATE TABLE tb_usuario (
    id_usuario INT AUTO_INCREMENT PRIMARY KEY,
    nome_usuario VARCHAR(50) NOT NULL UNIQUE,
    senha_usuario VARCHAR(50) NOT NULL
);


INSERT INTO tb_usuario (nome_usuario, senha_usuario) VALUES ('teste', '123');


DROP TABLE tb_usuario;