BEGIN;
SET @CidadeId = UUID();
INSERT INTO `cidades` (`id`, `nome`, `data_criacao`, `data_atualizacao`) VALUES (@CidadeId, 'São Paulo', NOW(), NOW());
INSERT INTO `areas` (`id`, `descricao`, `cidade_id`, `data_criacao`, `data_atualizacao`) VALUES (UUID(), 51, @CidadeId, NOW(), NOW());
SET @FabricanteId = UUID();
INSERT INTO `fabricantes` (`id`, `nome`, `data_criacao`, `data_atualizacao`) VALUES (@FabricanteId, 'Raro Labs', NOW(), NOW());
SET @ConfiguracaoId = UUID();
INSERT INTO `configuracao_controladores` (`id`, `descricao`, `limite_estagio`, `limite_grupo_semaforico`, `limite_anel`, `limite_detector_pedestre`, `limite_detector_veicular`, `data_criacao`, `data_atualizacao`) VALUES (@ConfiguracaoId, 'DESC', '5', '5', '5', '5', '5', NOW(), NOW());
INSERT INTO `modelo_controladores` (`id`, `descricao`, `fabricante_id`, `configuracao_id`, `data_criacao`, `data_atualizacao`) VALUES (UUID(), 'Desc modelo', @FabricanteId, @ConfiguracaoId, NOW(), NOW());
COMMIT;
