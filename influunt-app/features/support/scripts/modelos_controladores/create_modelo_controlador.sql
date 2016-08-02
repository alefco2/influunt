DELETE FROM `verdes_conflitantes`;
DELETE FROM `transicao`;
DELETE FROM `transicoes_proibidas`;
DELETE FROM `estagios_grupos_semaforicos`;
DELETE FROM `estagios`;
DELETE FROM `tabela_entre_verdes`;
DELETE FROM `grupos_semaforicos`;
DELETE FROM `aneis`;
DELETE FROM `agrupamentos_controladores`;
DELETE FROM `agrupamentos`;
DELETE FROM `controladores`;
DELETE FROM `modelo_controladores`;
DELETE FROM `fabricantes`;
SET @FabricanteId = RANDOM_UUID();
INSERT INTO `fabricantes` (`id`, `nome`, `data_criacao`, `data_atualizacao`) VALUES (@FabricanteId, 'Raro Labs', NOW(), NOW());
INSERT INTO `modelo_controladores` (`id`, `fabricante_id`, `descricao`, `data_criacao`, `data_atualizacao`) VALUES (RANDOM_UUID(), @FabricanteId, 'Mínima', NOW(), NOW());
