CREATE DATABASE IF NOT EXISTS mobile_money_db;
USE mobile_money_db;

CREATE TABLE CLIENT (
    id INT PRIMARY KEY AUTO_INCREMENT,
    nom VARCHAR(50) NOT NULL,
    prenom VARCHAR(50) NOT NULL,
    telephone VARCHAR(20) UNIQUE NOT NULL,
    adresse VARCHAR(100)
);

CREATE TABLE COMPTE (
    id INT PRIMARY KEY AUTO_INCREMENT,
    numero_compte VARCHAR(20) UNIQUE NOT NULL,
    solde DOUBLE DEFAULT 0.0,
    client_id INT,
    FOREIGN KEY (client_id) REFERENCES CLIENT(id)
);

CREATE TABLE OPERATION (
    id INT PRIMARY KEY AUTO_INCREMENT,
    type_operation ENUM('DEPOT', 'RETRAIT', 'TRANSFERT', 'PAIEMENT') NOT NULL,
    montant DOUBLE NOT NULL,
    date_operation DATETIME DEFAULT CURRENT_TIMESTAMP,
    compte_source INT,
    compte_destination INT NULL, -- Pour les transferts
    marchand VARCHAR(50) NULL,    -- Pour les paiements marchands
    FOREIGN KEY (compte_source) REFERENCES COMPTE(id),
    FOREIGN KEY (compte_destination) REFERENCES COMPTE(id)
);