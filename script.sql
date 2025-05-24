--  Ce script SQL décrit la structure de la base de données avec la création des tables.

--  NOTE IMPORTANTE :
--  Ce script n'est pas exécuté directement par l'application.
--  En réalité, la création et la mise à jour des tables sont gérées automatiquement
--  par Hibernate lors du démarrage de l'application, à partir des entités Java définies.


-- Création de la table account
CREATE TABLE account (
    id BIGINT NOT NULL AUTO_INCREMENT,
    balance INTEGER NOT NULL,
    PRIMARY KEY (id)
) ENGINE=InnoDB;

-- Création de la table user
CREATE TABLE user (
    id BIGINT NOT NULL AUTO_INCREMENT,
    account_id BIGINT,
    username VARCHAR(50) NOT NULL,
    email VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL,
    PRIMARY KEY (id),
    UNIQUE KEY UKnrrhhb0bsexvi8ch6wnon9uog (account_id),
    UNIQUE KEY UKob8kqyqqgmefl0aco34akdtpe (email),
    CONSTRAINT FKc3b4xfbq6rbkkrddsdum8t5f0 FOREIGN KEY (account_id) REFERENCES account (id)
) ENGINE=InnoDB;

-- Création de la table connections
CREATE TABLE connections (
    connection_id BIGINT NOT NULL,
    user_id BIGINT NOT NULL,
    UNIQUE KEY UKtlr24jyol093cmq4m9vhjc5e3 (connection_id),
    CONSTRAINT FKll23ohqygyfb7ev1ob06m1c0k FOREIGN KEY (connection_id) REFERENCES user (id),
    CONSTRAINT FKatesnk9iuu3hcyyo85p75blww FOREIGN KEY (user_id) REFERENCES user (id)
) ENGINE=InnoDB;

-- Création de la table transactions
CREATE TABLE transactions (
    id BIGINT NOT NULL AUTO_INCREMENT,
    amount INTEGER NOT NULL,
    receiver_id BIGINT,
    sender_id BIGINT,
    description VARCHAR(255) NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT FKla146pv6wr3osvib36521op7e FOREIGN KEY (receiver_id) REFERENCES user (id),
    CONSTRAINT FK2pxrw8potri9u5axcav3hhi6q FOREIGN KEY (sender_id) REFERENCES user (id)
) ENGINE=InnoDB;