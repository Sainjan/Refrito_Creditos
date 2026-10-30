package Modelo;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.time.LocalDate;
import java.util.Base64;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

public class Usuario {
    
    private int idUsuario, numeroIntentos;
    private String nombreUsuario, contrasena, salt;
    private Cargo rol;
    private LocalDate ultimoIntento;
    private Empresa emp;
    private Persona per;

    public Usuario(int idUs, String nomUs, String con, String rando, boolean cargo, int intentos, LocalDate ultInt, Persona perso, Empresa empre) //Constructor para usuarios existentes.
    {
        this.idUsuario = idUs;
        this.nombreUsuario = nomUs;
        this.contrasena = con;
        this.salt = rando; //El salt pertenece a un formato de texto que se implementa a la hora de encriptar un texto.
        if(cargo)
        {
            this.rol = Cargo.EMPRESA;
            this.emp = empre;
        } else
        {
            this.rol = Cargo.PERSONA;
            this.per = perso;
        }
        this.numeroIntentos = intentos;
        this.ultimoIntento = ultInt;
    }

    public Usuario(String nomUs, String con, String rando) //Constructor únicamente usado para el usuario "idUsuario = 1" en la base de datos. Si su ID es distinto, usa el otro constructor.
    {
        this.idUsuario = 1; //Si llamó a este constructor, el ID es obligatoriamente <1>
        this.nombreUsuario = nomUs;
        this.contrasena = con;
        this.salt = rando;
        this.rol = Cargo.ADMINISTRADOR; //Única forma legal de obtener acceso de "Administrador". Es necesario implementar más cosas que validen que está intrínsecamente conectado con el "idUsuario = 1".
        this.numeroIntentos = 999;
        this.ultimoIntento = null;
    }

    public void asignaDatosUs(int id, String nom, String con, String rando, Cargo car, int numInt, LocalDate ultIntento, Persona perso, Empresa empre)
    {
        if((emp != null) && (perso == null))
        {
            this.emp = empre;
        } else if ((emp == null) && (per != null))
        {
            this.per = perso;
        }
        this.idUsuario = id;
        this.nombreUsuario = nom;
        this.contrasena = con;
        this.salt = rando;
        this.rol = car;
        this.numeroIntentos = numInt;
        this.ultimoIntento = ultIntento;
    }

    //INICIO DE HASHEADORES============================================================================================================

    public static byte[] genera_random()
    { //Genera una secuencia random para ser utilizada en el hasheo. Es única por usuario, por lo que se almacena en UsuarioBDD.txt
        SecureRandom random = new SecureRandom(); //SecureRandom es un random aún más aleatorio e impredecible que otros random. Raro, lo sé...
        byte[] saleo = new byte[16];
        random.nextBytes(saleo);
        return saleo;
    }

    //El método "hasheador" se deja en la clase de Modelo porque es necesario para "verificarCon", el cual obligatoriamente pertenece al modelo "Usuario" para mayor seguridad.
    //Es por esto que, debido a que el Modelo nunca debería acceder a Servicio ni a BDD sino al revés según el modelado por capas, se encapsula todo el hasheo en "Usuario".
    public static String hasheador(String contra, byte[] randomizer) throws InvalidKeySpecException, NoSuchAlgorithmException
    {
        try
        {
            int vueltas = 65536; //Cuantas más iteraciones, más tiempo tarda y más difícil sería para un usuario externo averiguar el salt (comando randomizador) correcto rápidamente
            int longitud_final = 256; //Longitud de bytes finales
            char[] caracteres = contra.toCharArray(); //String es una cadena guardada de forma imprecisa; hacer arreglos de char permite mayor definición y seguridad

            PBEKeySpec contenedor = new PBEKeySpec(caracteres, randomizer, vueltas, longitud_final); //Se pasan los parámetros de la contraseña a un "contenedor"
            SecretKeyFactory solicita = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256"); //Se solicita un procesamiento de clave especial para el algoritmo indicado
            
            byte[] encriptador = solicita.generateSecret(contenedor).getEncoded(); //Procesa la contraseña con su randomizer según el algoritmo indicado
            
            return Base64.getEncoder().encodeToString(encriptador); //La contraseña hasheada de por sí es un valor de bytes difícil de manejar.
                                                                    //Pasarla a Base64 permite leerla como texto plano
        } catch (InvalidKeySpecException ikse)
        {
            throw new InvalidKeySpecException("Ocurrió un error inesperado: La contraseña indicada tiene un formato inválido: " + ikse.getMessage(),ikse);
        } catch (NoSuchAlgorithmException nsue)
        {
            throw new NoSuchAlgorithmException("Ocurrió un error inesperado con el algoritmo de encriptación: " + nsue,nsue);
        }
    }
    public boolean verificador_con(String contra) throws InvalidKeySpecException, NoSuchAlgorithmException
    {
        
        try
        {
            byte[] saleo_act = Base64.getDecoder().decode(this.salt); //Se obtiene el formato de randomización para que sea igual al de la contraseña
            
            String hash_contra = hasheador(contra, saleo_act); //Se hashea la contraseña con el formato de randomización para verificar si es igual
            
            return MessageDigest.isEqual(hash_contra.getBytes(StandardCharsets.UTF_8), this.contrasena.getBytes(StandardCharsets.UTF_8)); //El MessageDigest brinda mayor seguridad ante filtraciones,
                                                                                                                                      //pues evita que se identifique el hasheo por tiempo de proceso
        } catch(InvalidKeySpecException ikse) 
        {
            throw new InvalidKeySpecException("Ocurrió un error inesperado al intentar solicitar una clave: ",ikse);
        } catch(NoSuchAlgorithmException nsue) 
        {
            throw new NoSuchAlgorithmException("Ocurrió un error inesperado con el algoritmo de encriptación: ",nsue);
        }
    }

    //FIN DE HASHEADORES===============================================================================================================

    //SETTERS DE LA CLASE==============================================================================================================

    public void modUsuario(String nomNuevo, String contraNueva, String saltNuevo)
    {
        this.nombreUsuario = nomNuevo;

        if(((contraNueva != null) && (!contraNueva.isBlank())) && ((saltNuevo != null) && (!saltNuevo.isBlank())))
        {
            this.contrasena = contraNueva;
            this.salt = saltNuevo;
        }
    }

    //GETTERS DE LA CLASE==============================================================================================================
    public int obtIdUsuario()
    {
        return this.idUsuario;
    }

    public String obtNomUs()
    {
        return this.nombreUsuario;
    }

    public Cargo obtRol()
    {
        return this.rol;
    }

    public int obtNumIntentos()
    {
        return this.numeroIntentos;
    }

    public LocalDate obtUltIntento()
    {
        return this.ultimoIntento;
    }

    public Empresa obtEmpresa()
    {
        return this.emp;
    }

    public Persona obtPersona()
    {
        return this.per;
    }

    public boolean evaluaRiesgo()
    {
        if((this.emp != null) && (this.per == null))
        {
            return this.emp.evaluarRiesgo();
        } else
        {
            return this.per.evaluarRiesgo();
        }
    }
}
