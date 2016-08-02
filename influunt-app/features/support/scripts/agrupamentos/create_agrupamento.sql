delete from `verdes_conflitantes`;
delete from `transicao`;
delete from `transicoes_proibidas`;
DELETE FROM `estagios_grupos_semaforicos`;
DELETE FROM `estagios`;
DELETE FROM `tabela_entre_verdes`;
DELETE FROM `grupos_semaforicos`;
DELETE FROM `aneis`;
DELETE FROM `agrupamentos_controladores`;
DELETE FROM `controladores`;
DELETE FROM `areas`;
DELETE FROM `cidades`;
DELETE FROM `modelo_controladores`;
DELETE FROM `configuracao_controladores`;
DELETE FROM `fabricantes`;
DELETE FROM `agrupamentos`;
SET @FabricanteId = RANDOM_UUID();
SET @CidadeId = RANDOM_UUID();
SET @ControladorId = RANDOM_UUID();
SET @ModeloId = RANDOM_UUID();
SET @AreaId = RANDOM_UUID();
SET @AgrupamentoId = RANDOM_UUID();
INSERT INTO `cidades` (`id`, `nome`, `data_criacao`, `data_atualizacao`) VALUES (@CidadeId, 'São Paulo', NOW(), NOW());
INSERT INTO `areas` (`id`, `descricao`, `cidade_id`, `data_criacao`, `data_atualizacao`) VALUES (@AreaId, 1, @CidadeId, NOW(), NOW());
INSERT INTO `fabricantes` (`id`, `nome`, `data_criacao`, `data_atualizacao`) VALUES (@FabricanteId, 'Raro Labs', NOW(), NOW());
INSERT INTO `modelo_controladores` (`id`, `fabricante_id`, `descricao`, `data_criacao`, `data_atualizacao`) VALUES (@ModeloId, @FabricanteId, 'Mínima', NOW(), NOW());
INSERT INTO `controladores` (`id`, `localizacao`, `latitude`, `longitude`, `modelo_id`, `area_id`, `data_criacao`, `data_atualizacao`) VALUES (@ControladorId, 'Esquina rua A com B', '-23.55037588609829', '-46.66511535644531', @ModeloId, @AreaId, NOW(), NOW());
INSERT INTO `agrupamentos` (`id`, `nome`, `tipo`, `data_criacao`, `data_atualizacao`) VALUES (@AgrupamentoId, 'Corredor da Paulista', 'CORREDOR', NOW(), NOW());
INSERT INTO `agrupamentos_controladores` (`agrupamento_id`, `controlador_id`) VALUES (@AgrupamentoId, @ControladorId);
