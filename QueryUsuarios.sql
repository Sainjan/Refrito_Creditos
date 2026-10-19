
CREATE USER IF NOT EXISTS 'Comun'@'localhost' IDENTIFIED BY 'userComun1#';

GRANT EXECUTE ON PROCEDURE Credito.Inicializador TO 'Comun'@'localhost';
GRANT EXECUTE ON PROCEDURE Credito.Crear_Cliente TO 'Comun'@'localhost';
GRANT EXECUTE ON PROCEDURE Credito.Modificar_Cliente TO 'Comun'@'localhost';
GRANT EXECUTE ON PROCEDURE Credito.Eliminar_Cliente TO 'Comun'@'localhost';
GRANT EXECUTE ON PROCEDURE Credito.Buscar_Usuarios TO 'Comun'@'localhost';
GRANT EXECUTE ON PROCEDURE Credito.Extractor_Usuarios TO 'Comun'@'localhost';
GRANT EXECUTE ON PROCEDURE Credito.Reiniciador_Intentos TO 'Comun'@'localhost';
GRANT EXECUTE ON PROCEDURE Credito.Cargar_Log TO 'Comun'@'localhost';
GRANT EXECUTE ON PROCEDURE Credito.Asignado_Estado TO 'Comun'@'localhost';

FLUSH PRIVILEGES;

CALL Inicializador();