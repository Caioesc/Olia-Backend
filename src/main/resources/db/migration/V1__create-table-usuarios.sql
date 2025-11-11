create table usuarios(

    id bigint not null auto_increment,
    nome varchar(100) not null,
    email varchar(100) not null unique,
    cpf char(11) not null unique,
    logradouro varchar(150) not null,
    bairro varchar(100) not null,
    cidade varchar(100) not null,
    ativo tinyint not null,
    numeroNis char(11),
    temBolsaFamilia tinyint not null,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    primary key(id)

);