Create database Credito;
use Credito;


Create table UsuarioAcceso(
    idUsuarioAcceso int not null auto_increment primary key,
    nombreUsuario varchar(50) UNIQUE not null,
    contrasena varchar(50) not null
);

Create table UsuarioCliente(
    idCliente int not null auto_increment primary key,
    nombre varchar(50) not null,
    ingreso decimal(10,2) not null,
    gastos decimal(10,2) not null,
    historial varchar(50) not null
);

Create table FechaCredito(
    RegistroFecha int not null auto_increment PRIMARY KEY,
    fechaCredito date not null,
    idCliente int not null
);

Create table Empresa (
    idEmpresa int not null primary key,
    rubro varchar(50) not null,
    director varchar(50) not null,
    foreign key (idEmpresa) references UsuarioCliente(idCliente)
);

Create table Persona (
    idPersona int not null primary key,
    foreign key (idPersona) references UsuarioCliente(idCliente)
);
   Create table InfoLog ( 
    idInfoLog int not null auto_increment primary key,
    idUsuarioAcceso int not null,
    numeroError int not null,
    error varchar(50) not null,
    foreign key (idUsuarioAcceso) references UsuarioAcceso(idUsuarioAcceso)

);

ALTER TABLE FechaCredito
  ADD FOREIGN KEY (idCliente) REFERENCES UsuarioCliente(idCliente);


insert into UsuarioAcceso(nombreUsuario, contrasena) values('Edgar', '1234');
insert into UsuarioCliente(nombre, ingreso, gastos, historial) values('raul', 10000.00, 5000.00, 'Bueno');
insert into Empresa(rubro, director) values('Tecnologia', 'Mario');
insert into Persona() values();

create user 'Comun'@'localhost' identified by 'userComun1#';

##GRANT EXECUTE ON PROCEDURE NombreDelProceso TO 'Comun'@'localhost';
##FLUSH PRIVILEGES;