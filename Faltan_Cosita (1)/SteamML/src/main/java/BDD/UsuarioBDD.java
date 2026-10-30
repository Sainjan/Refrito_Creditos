/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package BDD;

import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;

import Modelo.Usuario;
import Modelo.Persona;
import Modelo.Empresa;
import Modelo.Cargo;
import Servicio.Logs_Intermedio;


public class UsuarioBDD {

    Usuario nuevo;

    public UsuarioBDD() throws SQLException
    {
        llenaBase();
    }
    
    private void llenaBase() throws SQLException
    {
        String consultaInicial = "{CALL Credito.Inicializador()}";
        String buscaUsuarios = "{CALL Credito.Buscar_Usuarios(?,?,?,?)}";
        String verificadorIntentos = "{CALL Credito.Reiniciador_Intentos(?)}";
        try(CallableStatement proceso = ConexionBDD.cn.prepareCall(consultaInicial);
            CallableStatement listador = ConexionBDD.cn.prepareCall(buscaUsuarios);
            CallableStatement verificaUltIntento = ConexionBDD.cn.prepareCall(verificadorIntentos))
        {
            proceso.execute();

            listador.setNull(1,java.sql.Types.VARCHAR);
            listador.setNull(2,java.sql.Types.VARCHAR);
            listador.setNull(3,java.sql.Types.VARCHAR);
            listador.setNull(4, java.sql.Types.INTEGER);

            listador.execute();

            ResultSet resultado_bus = listador.getResultSet();

            if(!resultado_bus.next())
            {
                //this.registrar_datos_usuario("SA", "Sapo_Pepe123", "00000000", true);
            } else
            {
                do
                {
                    int valor_id_fila = resultado_bus.getObject("idCliente",Integer.class);

                    verificaUltIntento.setInt(1,valor_id_fila);

                    verificaUltIntento.execute();
                } while(resultado_bus.next());
            }
        } catch (NumberFormatException nfe) {
            throw new NumberFormatException("Ocurrió un error inesperado con el formato numérico ingresado");
        } catch (DateTimeParseException dtpe)
        {
            throw new DateTimeParseException("Ocurrió un error inesperado con el formato de la fecha cargado", null, 0);
        } catch (SQLException sqle)
        {
            Logs_Intermedio.intermedio_carga_logs(this.nuevo.obtIdUsuario(), "Ocurrió un error inesperado al intentar ejecutar un proceso SQL: " + sqle, "UsuarioBDD: llenaBase", false);
            throw new SQLException("Ocurrió un error inesperado al intentar ejecutar un proceso SQL: " + sqle);
        }
    }

    public Usuario buscar_usuario(int id) throws SQLException 
    {
        String consulta = "{CALL Credito.Extractor_Usuarios(?)}";
        String consPer = "{CALL Credito.Extractor_Personas(?)}";
        String consEmp = "{CALL Credito.Extractor_Empresas(?)}";
        try(CallableStatement buscador = ConexionBDD.cn.prepareCall(consulta);
            CallableStatement buscadorPer = ConexionBDD.cn.prepareCall(consPer);
            CallableStatement buscadorEmp = ConexionBDD.cn.prepareCall(consEmp))
        {

            buscador.setInt(1,id);

            buscador.execute();

            ResultSet resultado_bus = buscador.getResultSet();

            if(resultado_bus.next())
            {
                int idUsSQL = resultado_bus.getInt("idUsuario");
                String nomSQL = resultado_bus.getObject("nombreUsuario",String.class);
                String conSQL = resultado_bus.getObject("contrasena",String.class);
                String ranSQL = resultado_bus.getObject("salt",String.class);
                Integer idRolSQL = resultado_bus.getObject("idEmpresa",Integer.class);
                Integer intentosSQL = resultado_bus.getObject("numeroIntentos",Integer.class);
                LocalDate ultIntentoSQL = resultado_bus.getObject("ultimoIntento",LocalDate.class);
                int idEstUsSQL = resultado_bus.getInt("idEstado");
                Cargo car = Cargo.EMPRESA;
                if(idRolSQL == null)
                {
                    Persona perso;
                    idRolSQL = resultado_bus.getObject("idPersona",Integer.class);
                    car = Cargo.PERSONA;
                    if(idRolSQL == null)
                    {
                        if(idUsSQL == 1)
                        {
                            car = Cargo.ADMINISTRADOR;
                        } else
                        {
                            car = null;
                        }
                    } else
                    {
                        buscadorPer.setInt(1,idRolSQL.intValue());

                        buscadorPer.execute();

                        ResultSet resPer = buscadorPer.getResultSet();
                        if(resPer.next())
                        {
                            String nomPer = resPer.getObject("nombre",String.class);
                            double ingPer = resPer.getObject("ingresos",Double.class);
                            double gasPer = resPer.getObject("gastos",Double.class);
                            boolean histoPer = resPer.getObject("historial",Boolean.class);
                            LocalDate fecHistoPer = resPer.getObject("fechaHistorial",LocalDate.class);
                            perso = new Persona(nomPer,ingPer,gasPer,histoPer,fecHistoPer);

                            this.nuevo = new Usuario(idUsSQL, nomSQL, conSQL, ranSQL, false, intentosSQL, ultIntentoSQL, perso, null);
                        }
                    }
                } else
                {
                    Empresa em;
                    buscadorEmp.setInt(1,idRolSQL.intValue());

                    buscadorEmp.execute();

                    ResultSet resEm = buscadorEmp.getResultSet();
                    if(resEm.next())
                    {
                        String nomEmp = resEm.getObject("nombre",String.class);
                        double ingresos = resEm.getObject("ingresos",Double.class);
                        double gastosEm = resEm.getObject("gastos",Double.class);
                        boolean historial = resEm.getObject("historial",Boolean.class);
                        LocalDate fechaHisEm = resEm.getObject("fechaHistorial",LocalDate.class);
                        String rub = resEm.getObject("rubro",String.class);
                        String dire = resEm.getObject("director",String.class);
                        em = new Empresa(nomEmp,ingresos,gastosEm,historial,fechaHisEm,rub,dire);

                        this.nuevo = new Usuario(idUsSQL, nomSQL, conSQL, ranSQL, true, intentosSQL, ultIntentoSQL, null, em);
                    }

                }

                if(idEstUsSQL == 2)
                {
                    return null;
                }

                return this.nuevo;

            } else
            {
                return null;
            }

        } catch (SQLException sqlex_bus)
        {
            Logs_Intermedio.intermedio_carga_logs(this.nuevo.obtIdUsuario(), "Ocurrió un error inesperado con SQL: " + sqlex_bus, "UsuarioBDD: buscar_usuario(int)", false);
            throw new SQLException("Ocurrió un error inesperado con SQL: " + sqlex_bus,sqlex_bus);
        }
    }

    public String buscar_usuario(String nom, String contra) throws InvalidKeySpecException, NoSuchAlgorithmException, SQLException {

        String consulta = "{CALL Credito.Buscar_Usuarios(?,?,?,?)}";    
        try(CallableStatement buscador = ConexionBDD.cn.prepareCall(consulta))
        {

            if(nom != null)
            {
                buscador.setString(1,nom);
            } else
            {
                buscador.setNull(1,java.sql.Types.VARCHAR);
            }

            //HASHEO DE CONTRASEÑA. NO ES MUY ÓPTIMO, PERO HAY QUE HACERLO
            if(contra != null)
            {
                byte[] rando = Usuario.genera_random();

                String contra_hash = Usuario.hasheador(contra, rando);

                buscador.setString(2,contra_hash);
            } else
            {
                buscador.setNull(2,java.sql.Types.VARCHAR);
            }

            buscador.setNull(3, java.sql.Types.VARCHAR);
            buscador.setNull(4, java.sql.Types.INTEGER);

            /*if(Usuario.id.equals(0))
            {
                buscador.setNull(4,java.sql.Types.INTEGER);
            } else
            {
                int id_us_int = Usuario.id;
                buscador.setInt(4,id_us_int.intValue());
            }*/

            buscador.execute();

            ResultSet resultado = buscador.getResultSet();

            if(!resultado.next())
            {
                return "No se encontró ningún usuario con esos datos";
            } else
            {
                if((nom != null) && (contra == null))
                {
                    int idObtenido = resultado.getInt("idCliente");
                    return "El nombre ingresado está en uso" + String.valueOf(idObtenido);
                } else if((nom != null) && (contra != null))
                {
                    int idObtenido = resultado.getInt("idCliente");
                    int id_est_us_sql = resultado.getInt("idEstado");
                    if(id_est_us_sql == 2)
                    {
                        return "No se encontró ningún usuario con esos datos";
                    }
                    return "¡Se encontró al usuario buscado!" + String.valueOf(idObtenido);
                }

                return "No se encontró ningún usuario con esos datos";
            }
        } catch (SQLException sqlex)
        {
            Logs_Intermedio.intermedio_carga_logs(this.nuevo.obtIdUsuario(), "Ocurrió un error inesperado con SQL: " + sqlex, "UsuarioBDD: buscar_usuario(String,String)", false);
            throw new SQLException("Ocurrió un error inesperado con SQL: " + sqlex,sqlex);
        }
    }

    public String registrar_datos_usuario(String nombre, String contrasena, Cargo cargo) throws InvalidKeySpecException, NoSuchAlgorithmException, SQLException
    {
        String consulta_creador = "{CALL Credito.Crear_Cliente(?,?,?,?,?)}";
        try(CallableStatement procedimiento = ConexionBDD.cn.prepareCall(consulta_creador))
        {
            int estado = 2;
            if(cargo.equals(Cargo.EMPRESA))
            {
                estado = 2;
            } else if(cargo.equals(Cargo.PERSONA))
            {
                estado = 1;
            } else if(cargo.equals(Cargo.ADMINISTRADOR))
            {
                estado = 3;
            }

            byte[] rando = Usuario.genera_random();

            String rando64 = Base64.getEncoder().encodeToString(rando); //Se obtiene la versión codificada en String del salt para usar en el constructor.
            String contra_hash = Usuario.hasheador(contrasena, rando); //Se hashea la contraseña haciendo uso del salt en versión byte. Una vez llegado a este punto, la contraseña en texto plano nunca vuelve a ser accesible.

            procedimiento.setString(1,nombre);
            procedimiento.setString(2,contra_hash); //La contraseña se carga directamente hasheada para mayor seguridad.
            procedimiento.setNull(3,java.sql.Types.VARCHAR);
            procedimiento.setString(4,rando64); //Se carga el Salt en formato String para poder gestionarlo fácilmente en la BDD. Es la única forma que existe de que la contraseña pueda ser verificada posteriormente.
            procedimiento.setInt(5,estado);

            procedimiento.execute();

            String resultado_busca = buscar_usuario(nombre, contrasena);

            if(resultado_busca.contains("Se encontró al usuario buscado"))
            {
                return "¡Operación exitosa!";
            } else
            {
                return "Ocurrió un error inesperado intentando cargar al usuario. Revise los Logs para más información";
            }

        } catch (SQLException sqlex_reg)
        {
            Logs_Intermedio.intermedio_carga_logs(this.nuevo.obtIdUsuario(), "Ocurrió un error inesperado con SQL: " + sqlex_reg, "UsuarioBDD: registrar_datos_usuario", false);
            throw new SQLException("Ocurrió un error inesperado con SQL: " + sqlex_reg,sqlex_reg);
        }
    }

    public String eliminar_datos_usuario() throws SQLException
    {
        String consulta_borrador = "{CALL Credito.Eliminar_Usuarios(?)}";
        try(CallableStatement llamador = ConexionBDD.cn.prepareCall(consulta_borrador))
        {
            llamador.setInt(1,this.nuevo.obtIdUsuario());

            llamador.execute();

            Usuario resultado = buscar_usuario(this.nuevo.obtIdUsuario());
            if(resultado == null)
            {
                this.nuevo = null;
                return "¡Operación exitosa!";
            } else
            {
                return "Ocurrió un error inesperado al intentar eliminar al usuario del sistema. Revise el Log para más información";
            }
        } catch(SQLException sqlex_el)
        {
            Logs_Intermedio.intermedio_carga_logs(this.nuevo.obtIdUsuario(), "Ocurrió un error inesperado con SQL al intentar eliminar un usuario: " + sqlex_el, "UsuarioBDD: eliminar_datos_usuario", false);
            throw new SQLException("Ocurrió un error inesperado con SQL al intentar eliminar un usuario: " + sqlex_el,sqlex_el);
        }
    }

    public Usuario modificar_usuariobdd(String nuevoNom, String nuevaCon) throws InvalidKeySpecException, NoSuchAlgorithmException, SQLException
    {
        String consultaModifica = "{CALL Credito.Modificar_Usuario(?,?,?,?)}";

        try(CallableStatement llamador = ConexionBDD.cn.prepareCall(consultaModifica))
        {
            String contra_def = null;
            String salt_def = null;
            if(nuevaCon != null) {
                byte[] rando = Usuario.genera_random();

                String contraHash = Usuario.hasheador(nuevaCon, rando);
                String hash64 = Base64.getEncoder().encodeToString(rando);

                salt_def = hash64;
                contra_def = contraHash;
            }

            String nombre_def;
            if((nuevoNom != null) && (!nuevoNom.isBlank())) {
                nombre_def = nuevoNom;
            } else
            {
                nombre_def = this.nuevo.obtNomUs();
            }

            llamador.setInt(1,this.nuevo.obtIdUsuario());
            llamador.setString(2,nombre_def);
            if(((contra_def != null) && (!contra_def.isBlank())) && ((salt_def != null) && (!salt_def.isBlank())))
            {
                llamador.setString(3,contra_def);
                llamador.setString(4,salt_def);
            } else
            {
                llamador.setNull(3,java.sql.Types.VARCHAR);
                llamador.setNull(4,java.sql.Types.VARCHAR);
            }

            llamador.execute();

            String resultado_consulta = buscar_usuario(nombre_def,contra_def);

            if(resultado_consulta.contains("Se encontró al usuario buscado"))
            {
                this.nuevo.modUsuario(nombre_def, contra_def, salt_def);
                return this.nuevo;
            } else
            {
                return this.nuevo;
            }
        } catch (SQLException sqlex_mod)
        {
            throw new SQLException("Ocurrió un error inesperado con SQL: " + sqlex_mod,sqlex_mod);
        }
    }
}