Create database Credito;
use Credito;

Create table UsuarioAcceso(
    idUsuarioAcceso int not null auto_increment,
    nombreUsuario varchar(50) UNIQUE not null,
    contrasena varchar(50) not null,    
)

Create table UsuarioCliente(
    idCliente int not null auto_increment,
    nombre varchar(50) not null,
    ingreso decimal(10,2) not null,
    gastos decimal(10,2) not null,
    historial varchar(50) not null
)

Create table Empresa (
    idEmpresa int not null auto_increment,
    rubro varchar(50) not null,
    director varchar(50) not null,
    foreign key (idEmpresa) references UsuarioCliente(idCliente)
)

Create table Persona (
    idPersona int not null auto_increment,
    foreign key (idPersona) references UsuarioCliente(idCliente)
)
Create table InfoLog (
    idInfoLog int not null auto_increment,
    numeroError int not null,
    error varchar(50) not null,
    foreign key (idInfoLog) references usuarioAccesi(idUsuarioAcceso)
)

insert into UsuarioAcceso(nombreUsuario, contrasena) values('Edgar', '1234');
insert into UsuarioCliente(nombre, ingreso, gastos, historial) values('raul', 10000.00, 5000.00, 'Bueno');
insert into Empresa(rubro, director) values('Tecnologia', 'Mario');
insert into Persona() values();