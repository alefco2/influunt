BEGIN;
INSERT INTO `usuarios` (`id`, `login`,`senha`, `email`, `nome`, `root`, `data_criacao`, `data_atualizacao`) VALUES (RANDOM_UUID(), 'root', '$2a$10$EzudGIqkxquJjLGawuMrOu9K6S28yc/R/YSAVxsvb5bSryOYWd5eq', 'root@influunt.com.br','Administrador Geral', 'true', CURRENT_TIME(), CURRENT_TIME());
COMMIT;
