
CREATE DATABASE IF NOT EXISTS Credito;
USE Credito;

CREATE TABLE UsuarioAcceso (
    idUsuarioAcceso INT NOT NULL AUTO_INCREMENT,
    clave VARCHAR(200),
    nombreUsuario VARCHAR(40),
    numeroIntentos INT NOT NULL DEFAULT 0, -- EXTRA (Reiniciador_Intentos)
    ultimoIntento DATE, -- EXTRA (Reiniciador_Intentos)
    PRIMARY KEY (idUsuarioAcceso)
);

CREATE TABLE UsuarioCliente (
    idCliente INT NOT NULL AUTO_INCREMENT,
    nombre VARCHAR(40),
    ingreso DECIMAL(10,3),
    gastos DECIMAL(10,3),
    historial VARCHAR(100),
    estado TINYINT NOT NULL DEFAULT 3, -- EXTRA (Asignado_Estado): 1 Aprobado, 2 Rechazado, 3 Pendiente
    PRIMARY KEY (idCliente)
);

CREATE TABLE Empresa (
    idEmpresa INT NOT NULL AUTO_INCREMENT,
    rubro VARCHAR(50),
    director VARCHAR(40),
    idCliente INT NOT NULL,
    FOREIGN KEY (idCliente) REFERENCES UsuarioCliente(idCliente),
    PRIMARY KEY (idEmpresa)
);

CREATE TABLE Persona (
    idPersona INT NOT NULL AUTO_INCREMENT,
    idCliente INT NOT NULL,
    FOREIGN KEY (idCliente) REFERENCES UsuarioCliente(idCliente),
    PRIMARY KEY (idPersona)
);

CREATE TABLE infoLog (
    idLog INT NOT NULL AUTO_INCREMENT,
    numeroError INT,
    error VARCHAR(300),
    idUsuarioAcceso INT,
    FOREIGN KEY (idUsuarioAcceso) REFERENCES UsuarioAcceso(idUsuarioAcceso),
    PRIMARY KEY (idLog)
);

CREATE TABLE FechaCredito(
    RegistroFecha INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    fechaCredito DATE NOT NULL,
    idCliente INT NOT NULL
);

ALTER TABLE FechaCredito
  ADD FOREIGN KEY (idCliente) REFERENCES UsuarioCliente(idCliente);
