DELETE FROM `estagios_grupos_semaforicos`;
DELETE FROM `estagios`;
DELETE FROM `grupos_semaforicos`;
DELETE FROM `aneis`;
DELETE FROM `controladores`;
DELETE FROM `areas`;
DELETE FROM `cidades`;
SET @CidadeId = RANDOM_UUID();
INSERT INTO `cidades` (`id`, `nome`, `data_criacao`, `data_atualizacao`) VALUES (@CidadeId, 'São Paulo', NOW(), NOW());
INSERT INTO `cidades` (`id`, `nome`, `data_criacao`, `data_atualizacao`) VALUES (RANDOM_UUID(), 'Belo Horizonte', NOW(), NOW());
INSERT INTO `areas` (`id`, `descricao`, `cidade_id`, `data_criacao`, `data_atualizacao`) VALUES (RANDOM_UUID(), 51, @CidadeId, NOW(), NOW());
