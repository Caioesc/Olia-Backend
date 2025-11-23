
-- 1) Renomear numeroNis -> numero_nis (preserva tipo CHAR(11))
ALTER TABLE usuarios
  CHANGE COLUMN numeroNis numero_nis CHAR(11) NULL;

-- 2) Renomear temBolsaFamilia -> tem_bolsa_familia e padronizar TINYINT(1)
ALTER TABLE usuarios
  CHANGE COLUMN temBolsaFamilia tem_bolsa_familia TINYINT(1) NOT NULL DEFAULT 0;

-- 3) Adicionar coluna senha (hash da senha)
ALTER TABLE usuarios
  ADD COLUMN senha VARCHAR(255) NULL AFTER numero_nis;

-- 4) Adicionar coluna telefone
ALTER TABLE usuarios
  ADD COLUMN telefone VARCHAR(20) NULL AFTER senha;

-- 5) (Opcional) ajustar colunas booleanas existentes para TINYINT(1) e defaults
ALTER TABLE usuarios
  MODIFY COLUMN ativo TINYINT(1) NOT NULL DEFAULT 1;
