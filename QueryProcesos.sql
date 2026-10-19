DELIMITER $$

CREATE DEFINER=`root`@`localhost` PROCEDURE `Cargar_Log`(
	IN `Numero_Error_IN` INT,
	IN `Mensaje_IN` VARCHAR(500),
	IN `Proceso_IN` VARCHAR(50),
	IN `ID_Usuario_IN` INT
)
LANGUAGE SQL
NOT DETERMINISTIC
CONTAINS SQL
SQL SECURITY DEFINER
COMMENT ''
BEGIN

   INSERT INTO infoLog (numeroError, error, idUsuarioAcceso)
   VALUES (Numero_Error_IN, LEFT(CONCAT(Proceso_IN,' ',Mensaje_IN),300), ID_Usuario_IN);

END$$


CREATE DEFINER=`root`@`localhost` PROCEDURE `Inicializador`(
)
LANGUAGE SQL
NOT DETERMINISTIC
CONTAINS SQL
SQL SECURITY DEFINER
COMMENT ''
BEGIN

	DECLARE id_admin INT DEFAULT 0;

	DECLARE EXIT HANDLER FOR SQLEXCEPTION
   BEGIN

   	DECLARE validador TINYINT DEFAULT 0;

      GET DIAGNOSTICS CONDITION 1 @sqlstate = RETURNED_SQLSTATE, @errno = MYSQL_ERRNO, @text = MESSAGE_TEXT;

      ROLLBACK;

      BEGIN

      	DECLARE CONTINUE HANDLER FOR SQLEXCEPTION
         	SET validador = 1;

         CALL Cargar_Log(@errno,CONCAT('ERROR: [',@sqlstate,':',@errno,']\n',LEFT(@text,200)),'[SQL] Inicializador()',NULL);

      END;

      IF validador THEN

      	SELECT CONCAT('ERROR: [',@sqlstate,':',@errno,']\n',@text,'\n(Además falló el registro en infoLog)') AS 'ERROR';

      END IF;

      RESIGNAL;

   END;

   IF NOT EXISTS (SELECT 1 FROM UsuarioAcceso) THEN

   	START TRANSACTION;

      	INSERT INTO UsuarioAcceso (nombreUsuario, clave, numeroIntentos, ultimoIntento)
         VALUES ('admin', 'CAMBIAR_CLAVE', 0, CURRENT_DATE());

         SET id_admin = LAST_INSERT_ID();

         CALL Cargar_Log(0,CONCAT('Se creó el usuario administrador inicial con ID = ',id_admin),'[SQL] Inicializador()',id_admin);

      COMMIT;

   END IF;

END$$


CREATE DEFINER=`root`@`localhost` PROCEDURE `Crear_Cliente`(
	IN `Nombre_IN` VARCHAR(40),
	IN `Ingreso_IN` DECIMAL(10,3),
	IN `Gastos_IN` DECIMAL(10,3),
	IN `Historial_IN` VARCHAR(100),
	IN `Tipo_IN` CHAR(1),
	IN `Rubro_IN` VARCHAR(50),
	IN `Director_IN` VARCHAR(40),
	IN `ID_Usuario_IN` INT
)
LANGUAGE SQL
NOT DETERMINISTIC
CONTAINS SQL
SQL SECURITY DEFINER
COMMENT ''
BEGIN

	DECLARE id_cliente INT DEFAULT 0;

	DECLARE EXIT HANDLER FOR SQLEXCEPTION
   BEGIN

   	DECLARE validador TINYINT DEFAULT 0;

      GET DIAGNOSTICS CONDITION 1 @sqlstate = RETURNED_SQLSTATE, @errno = MYSQL_ERRNO, @text = MESSAGE_TEXT;

      ROLLBACK;

      BEGIN

      	DECLARE CONTINUE HANDLER FOR SQLEXCEPTION
         	SET validador = 1;

         CALL Cargar_Log(@errno,CONCAT('ERROR: [',@sqlstate,':',@errno,']\n',LEFT(@text,200)),'[SQL] Crear_Cliente()',ID_Usuario_IN);

      END;

      IF validador THEN

      	SELECT CONCAT('ERROR: [',@sqlstate,':',@errno,']\n',@text,'\n(Además falló el registro en infoLog)') AS 'ERROR';

      END IF;

      RESIGNAL;

   END;

   IF ((Tipo_IN IS NULL) OR (UPPER(Tipo_IN) NOT IN ('P','E'))) THEN

   	SELECT 'El tipo de cliente debe ser P (Persona) o E (Empresa).' AS 'ADVERTENCIA';

      CALL Cargar_Log(45000,'Se intentó crear un cliente con un tipo inválido','[SQL] Crear_Cliente()',ID_Usuario_IN);

   ELSEIF ((UPPER(Tipo_IN) = 'E') AND ((Rubro_IN IS NULL) OR (Director_IN IS NULL))) THEN

   	SELECT 'Una empresa necesita rubro y director.' AS 'ADVERTENCIA';

      CALL Cargar_Log(45000,'Se intentó crear una empresa sin rubro o sin director','[SQL] Crear_Cliente()',ID_Usuario_IN);

   ELSE

   	START TRANSACTION;

      	INSERT INTO UsuarioCliente (nombre, ingreso, gastos, historial)
         VALUES (Nombre_IN, Ingreso_IN, Gastos_IN, Historial_IN);

         SET id_cliente = LAST_INSERT_ID();

         IF UPPER(Tipo_IN) = 'E' THEN

         	INSERT INTO Empresa (rubro, director, idCliente)
            VALUES (Rubro_IN, Director_IN, id_cliente);

         ELSE

         	INSERT INTO Persona (idCliente)
            VALUES (id_cliente);

         END IF;

         CALL Cargar_Log(0,CONCAT('Se creó el cliente ',Nombre_IN,' (tipo ',UPPER(Tipo_IN),') con ID = ',id_cliente),'[SQL] Crear_Cliente()',ID_Usuario_IN);

      COMMIT;

   END IF;

END$$

CREATE DEFINER=`root`@`localhost` PROCEDURE `Modificar_Cliente`(
	IN `ID_Cliente_IN` INT,
	IN `Nuevo_Nombre_IN` VARCHAR(40),
	IN `Nuevo_Ingreso_IN` DECIMAL(10,3),
	IN `Nuevos_Gastos_IN` DECIMAL(10,3),
	IN `Nuevo_Historial_IN` VARCHAR(100),
	IN `Nuevo_Rubro_IN` VARCHAR(50),
	IN `Nuevo_Director_IN` VARCHAR(40),
	IN `ID_Usuario_IN` INT
)
LANGUAGE SQL
NOT DETERMINISTIC
CONTAINS SQL
SQL SECURITY DEFINER
COMMENT ''
BEGIN

	DECLARE EXIT HANDLER FOR SQLEXCEPTION
   BEGIN

   	DECLARE validador TINYINT DEFAULT 0;

      GET DIAGNOSTICS CONDITION 1 @sqlstate = RETURNED_SQLSTATE, @errno = MYSQL_ERRNO, @text = MESSAGE_TEXT;

      ROLLBACK;

      BEGIN

      	DECLARE CONTINUE HANDLER FOR SQLEXCEPTION
         	SET validador = 1;

         CALL Cargar_Log(@errno,CONCAT('ERROR: [',@sqlstate,':',@errno,']\n',LEFT(@text,200)),'[SQL] Modificar_Cliente()',ID_Usuario_IN);

      END;

      IF validador THEN

      	SELECT CONCAT('ERROR: [',@sqlstate,':',@errno,']\n',@text,'\n(Además falló el registro en infoLog)') AS 'ERROR';

      END IF;

      RESIGNAL;

   END;

   IF EXISTS (SELECT 1 FROM UsuarioCliente WHERE idCliente = ID_Cliente_IN) THEN

   	START TRANSACTION;

      	UPDATE UsuarioCliente SET nombre = Nuevo_Nombre_IN, ingreso = Nuevo_Ingreso_IN, gastos = Nuevos_Gastos_IN, historial = Nuevo_Historial_IN
         WHERE idCliente = ID_Cliente_IN;

         -- Si el cliente es Persona no hay fila en Empresa y este UPDATE no toca nada
         UPDATE Empresa SET rubro = COALESCE(Nuevo_Rubro_IN,rubro), director = COALESCE(Nuevo_Director_IN,director)
         WHERE idCliente = ID_Cliente_IN;

         CALL Cargar_Log(0,CONCAT('Se modificó correctamente al cliente ID = ',ID_Cliente_IN),'[SQL] Modificar_Cliente()',ID_Usuario_IN);

      COMMIT;

   ELSE

   	SELECT 'Cliente indicado no existente en el sistema' AS 'ADVERTENCIA';

      CALL Cargar_Log(45000,CONCAT('Se intentó modificar un cliente inexistente, ID = ',ID_Cliente_IN),'[SQL] Modificar_Cliente()',ID_Usuario_IN);

   END IF;

END$$


CREATE DEFINER=`root`@`localhost` PROCEDURE `Eliminar_Cliente`(
	IN `ID_Cliente_IN` INT,
	IN `ID_Usuario_IN` INT
)
LANGUAGE SQL
NOT DETERMINISTIC
CONTAINS SQL
SQL SECURITY DEFINER
COMMENT ''
BEGIN

	DECLARE EXIT HANDLER FOR SQLEXCEPTION
   BEGIN

   	DECLARE validador TINYINT DEFAULT 0;

      GET DIAGNOSTICS CONDITION 1 @sqlstate = RETURNED_SQLSTATE, @errno = MYSQL_ERRNO, @text = MESSAGE_TEXT;

      ROLLBACK;

      BEGIN

      	DECLARE CONTINUE HANDLER FOR SQLEXCEPTION
         	SET validador = 1;

         CALL Cargar_Log(@errno,CONCAT('ERROR: [',@sqlstate,':',@errno,']\n',LEFT(@text,200)),'[SQL] Eliminar_Cliente()',ID_Usuario_IN);

      END;

      IF validador THEN

      	SELECT CONCAT('ERROR: [',@sqlstate,':',@errno,']\n',@text,'\n(Además falló el registro en infoLog)') AS 'ERROR';

      END IF;

      RESIGNAL;

   END;

   IF EXISTS (SELECT 1 FROM UsuarioCliente WHERE idCliente = ID_Cliente_IN) THEN

   	START TRANSACTION;

      	DELETE FROM Empresa WHERE idCliente = ID_Cliente_IN;

         DELETE FROM Persona WHERE idCliente = ID_Cliente_IN;

         DELETE FROM UsuarioCliente WHERE idCliente = ID_Cliente_IN;

         CALL Cargar_Log(0,CONCAT('Se eliminó exitosamente al cliente ID = ',ID_Cliente_IN,' del sistema'),'[SQL] Eliminar_Cliente()',ID_Usuario_IN);

      COMMIT;

   ELSE

   	SELECT 'Cliente indicado no existente en el sistema' AS 'ADVERTENCIA';

      CALL Cargar_Log(45000,CONCAT('Se intentó eliminar un cliente inexistente, ID = ',ID_Cliente_IN),'[SQL] Eliminar_Cliente()',ID_Usuario_IN);

   END IF;

END$$


CREATE DEFINER=`root`@`localhost` PROCEDURE `Buscar_Usuarios`(
	IN `Nombre_IN` VARCHAR(40),
	IN `Clave_IN` VARCHAR(200),
	IN `ID_Usuario_IN` INT
)
LANGUAGE SQL
NOT DETERMINISTIC
CONTAINS SQL
SQL SECURITY DEFINER
COMMENT ''
BEGIN

	DECLARE EXIT HANDLER FOR SQLEXCEPTION
   BEGIN

   	DECLARE validador TINYINT DEFAULT 0;

      GET DIAGNOSTICS CONDITION 1 @sqlstate = RETURNED_SQLSTATE, @errno = MYSQL_ERRNO, @text = MESSAGE_TEXT;

      BEGIN

      	DECLARE CONTINUE HANDLER FOR SQLEXCEPTION
         	SET validador = 1;

         CALL Cargar_Log(@errno,CONCAT('ERROR: [',@sqlstate,':',@errno,']\n',LEFT(@text,200)),'[SQL] Buscar_Usuarios()',ID_Usuario_IN);

      END;

      IF validador THEN

      	SELECT CONCAT('ERROR: [',@sqlstate,':',@errno,']\n',@text,'\n(Además falló el registro en infoLog)') AS 'ERROR';

      END IF;

      RESIGNAL;

   END;

   IF (Nombre_IN IS NULL) THEN

   	SELECT * FROM UsuarioAcceso;

   ELSEIF (Clave_IN IS NULL) THEN

   	SELECT * FROM UsuarioAcceso WHERE (nombreUsuario = Nombre_IN);

   ELSE

   	SELECT * FROM UsuarioAcceso WHERE ((nombreUsuario = Nombre_IN) AND (clave = Clave_IN));

   END IF;

END$$

-- ----------------------------------------------------------------------------
-- EXTRACTOR USUARIOS

CREATE DEFINER=`root`@`localhost` PROCEDURE `Extractor_Usuarios`(
	IN `ID_Usuario_Buscado_IN` INT
)
LANGUAGE SQL
NOT DETERMINISTIC
CONTAINS SQL
SQL SECURITY DEFINER
COMMENT ''
BEGIN

	DECLARE EXIT HANDLER FOR SQLEXCEPTION
   BEGIN

   	DECLARE validador TINYINT DEFAULT 0;

      GET DIAGNOSTICS CONDITION 1 @sqlstate = RETURNED_SQLSTATE, @errno = MYSQL_ERRNO, @text = MESSAGE_TEXT;

      BEGIN

      	DECLARE CONTINUE HANDLER FOR SQLEXCEPTION
         	SET validador = 1;

         CALL Cargar_Log(@errno,CONCAT('ERROR: [',@sqlstate,':',@errno,']\n',LEFT(@text,200)),'[SQL] Extractor_Usuarios()',ID_Usuario_Buscado_IN);

      END;

      IF validador THEN

      	SELECT CONCAT('ERROR: [',@sqlstate,':',@errno,']\n',@text,'\n(Además falló el registro en infoLog)') AS 'ERROR';

      END IF;

      RESIGNAL;

   END;

   SELECT * FROM UsuarioAcceso WHERE idUsuarioAcceso = ID_Usuario_Buscado_IN;

END$$

CREATE DEFINER=`root`@`localhost` PROCEDURE `Reiniciador_Intentos`(
	IN `ID_Usuario_IN` INT
)
LANGUAGE SQL
NOT DETERMINISTIC
CONTAINS SQL
SQL SECURITY DEFINER
COMMENT ''
BEGIN

	DECLARE ult_fecha DATE;

	DECLARE EXIT HANDLER FOR SQLEXCEPTION
   BEGIN

   	DECLARE validador TINYINT DEFAULT 0;

      GET DIAGNOSTICS CONDITION 1 @sqlstate = RETURNED_SQLSTATE, @errno = MYSQL_ERRNO, @text = MESSAGE_TEXT;

      ROLLBACK;

      BEGIN

      	DECLARE CONTINUE HANDLER FOR SQLEXCEPTION
         	SET validador = 1;

         CALL Cargar_Log(@errno,CONCAT('ERROR: [',@sqlstate,':',@errno,']\n',LEFT(@text,200)),'[SQL] Reiniciador_Intentos()',ID_Usuario_IN);

      END;

      IF validador THEN

      	SELECT CONCAT('ERROR: [',@sqlstate,':',@errno,']\n',@text,'\n(Además falló el registro en infoLog)') AS 'ERROR';

      END IF;

      RESIGNAL;

   END;

   IF EXISTS (SELECT 1 FROM UsuarioAcceso WHERE idUsuarioAcceso = ID_Usuario_IN) THEN

   	SELECT ultimoIntento INTO ult_fecha FROM UsuarioAcceso WHERE idUsuarioAcceso = ID_Usuario_IN;

      IF ((ult_fecha IS NOT NULL) AND (DATEDIFF(CURRENT_DATE(),ult_fecha) > 7)) THEN

      	START TRANSACTION;

         	UPDATE UsuarioAcceso SET numeroIntentos = 0 WHERE idUsuarioAcceso = ID_Usuario_IN;

            CALL Cargar_Log(0,CONCAT('Se reinició el contador de intentos del usuario ID = ',ID_Usuario_IN),'[SQL] Reiniciador_Intentos()',ID_Usuario_IN);

         COMMIT;

      END IF;

   ELSE

   	SELECT 'Usuario indicado no existente en el sistema' AS 'ADVERTENCIA';

      CALL Cargar_Log(45000,CONCAT('Se intentó reiniciar los intentos de un usuario inexistente, ID = ',ID_Usuario_IN),'[SQL] Reiniciador_Intentos()',NULL);

   END IF;

END$$

CREATE DEFINER=`root`@`localhost` PROCEDURE `Asignado_Estado`(
	IN `ID_Cliente_IN` INT,
	IN `Estado_IN` TINYINT,
	IN `ID_Usuario_IN` INT
)
LANGUAGE SQL
NOT DETERMINISTIC
CONTAINS SQL
SQL SECURITY DEFINER
COMMENT ''
BEGIN

	DECLARE estado_actual TINYINT DEFAULT NULL;
	DECLARE texto VARCHAR(100) DEFAULT '';

	DECLARE EXIT HANDLER FOR SQLEXCEPTION
   BEGIN

   	DECLARE validador TINYINT DEFAULT 0;

      GET DIAGNOSTICS CONDITION 1 @sqlstate = RETURNED_SQLSTATE, @errno = MYSQL_ERRNO, @text = MESSAGE_TEXT;

      ROLLBACK;

      BEGIN

      	DECLARE CONTINUE HANDLER FOR SQLEXCEPTION
         	SET validador = 1;

         CALL Cargar_Log(@errno,CONCAT('ERROR: [',@sqlstate,':',@errno,']\n',LEFT(@text,200)),'[SQL] Asignado_Estado()',ID_Usuario_IN);

      END;

      IF validador THEN

      	SELECT CONCAT('ERROR: [',@sqlstate,':',@errno,']\n',@text,'\n(Además falló el registro en infoLog)') AS 'ERROR';

      END IF;

      RESIGNAL;

   END;

   IF ((Estado_IN IS NULL) OR (Estado_IN NOT IN (1,2))) THEN

   	SELECT 'El estado debe ser 1 (Aprobado) o 2 (Rechazado).' AS 'ADVERTENCIA';

      CALL Cargar_Log(45000,'Se intentó asignar un estado inválido','[SQL] Asignado_Estado()',ID_Usuario_IN);

   ELSEIF NOT EXISTS (SELECT 1 FROM UsuarioCliente WHERE idCliente = ID_Cliente_IN) THEN

   	SELECT 'No existe un cliente con dicha ID.' AS 'ADVERTENCIA';

      CALL Cargar_Log(45000,'Se intentó gestionar un cliente inexistente','[SQL] Asignado_Estado()',ID_Usuario_IN);

   ELSE

   	SELECT estado INTO estado_actual FROM UsuarioCliente WHERE idCliente = ID_Cliente_IN;

      IF estado_actual != 3 THEN

      	SELECT 'No se puede cambiar el estado de un cliente ya gestionado.' AS 'ADVERTENCIA';

         CALL Cargar_Log(45000,CONCAT('Se intentó cambiar el estado de un cliente ya gestionado, ID = ',ID_Cliente_IN),'[SQL] Asignado_Estado()',ID_Usuario_IN);

      ELSE

      	START TRANSACTION;

         	UPDATE UsuarioCliente SET estado = Estado_IN WHERE idCliente = ID_Cliente_IN;

            IF Estado_IN = 1 THEN

            	SET texto = CONCAT('Se aprobó de forma exitosa al cliente ID = ',ID_Cliente_IN);

            ELSE

            	SET texto = CONCAT('Se rechazó de forma exitosa al cliente ID = ',ID_Cliente_IN);

            END IF;

            CALL Cargar_Log(0,texto,'[SQL] Asignado_Estado()',ID_Usuario_IN);

         COMMIT;

      END IF;

   END IF;

END$$

DELIMITER ;

CALL Inicializador();