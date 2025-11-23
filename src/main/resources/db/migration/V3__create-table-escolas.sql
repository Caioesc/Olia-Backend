create table escolas(

    id bigint not null auto_increment,
    nome varchar(100) not null,
    nome_responsavel varchar(100) not null,
    email varchar(100) not null unique,
    email_acesso varchar(100) not null unique,
    cnpj char(14) not null unique,
    logradouro varchar(150) not null,
    bairro varchar(100) not null,
    cidade varchar(100) not null,
    ativo tinyint not null,
    codigo_inep char(8) not null unique,
    telefone varchar(20) not null,
    capacidade varchar(50) not null,
    senha varchar(255) not null,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    primary key(id)

);