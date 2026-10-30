package Modelo;

import java.time.LocalDate;
import java.sql.SQLException;

import Modelo.Usuario;

public class Solicitud{

    Integer idSolicitud; 
    double ingresos, gastos, montoMaximo;
    LocalDate fecha; 
    String estado;
    Usuario solicitante;

    public Solicitud(Integer idSolicitud, double ingresos, double gastos, String estado, LocalDate fecha, Usuario solicitante) throws SQLException {
        this.idSolicitud = idSolicitud;
        this.ingresos = ingresos;
        this.gastos = gastos;

        if (estado.equalsIgnoreCase("Pendiente") || estado.equalsIgnoreCase("Rechazado") || estado.equalsIgnoreCase("Confirmado")) {
            this.estado = estado;
        } else {
            this.estado = "Pendiente";
        }
 
        if (fecha != null) {
            this.fecha = fecha;
        } else {
            this.fecha = LocalDate.now(); 
        }

        this.solicitante = solicitante;
    }

    public double obtIngresos(){
        return this.ingresos;
    }

    public double obtGastos(){
        return this.gastos;
    }

    public String obtEstado(){
        return this.estado;
    }

    public LocalDate obtFecha(){
        return this.fecha;
    }

    public void setEstado(String nuevoEstado){
        this.estado = nuevoEstado;
    }

    public Usuario obtSolicitante() {
        return this.solicitante;
    }

    public boolean evaluarRiesgo() {
        if(this.solicitante.obtEmpresa() == null)
        {
            if(this.solicitante.obtPersona() == null)
            {
                return false; // Si no hay empresa ni persona asociada, se considera Administrador. 
                              // Técnicamente el administrador no debería poder solicitar créditos, 
                              // pero podría suceder o hardcodearse.
            } else
            {
                return this.solicitante.obtPersona().evaluarRiesgo();
            }
        } else
        {
            return this.solicitante.obtEmpresa().evaluarRiesgo();
        }
    }

    public double getMontoMaximo() {
        return calcularMontoRMC3(this.ingresos, this.gastos);
    }

    private double calcularMontoRMC3(Double ingresos, Double gastos) {

        double plataSobrante = ingresos - gastos;
        
        // Sacamos el 30% mensual y lo multiplicamos por 12 
        // para obtener el monto máximo del crédito a 1 año, por ejemplo.
        double montoMaximo = (plataSobrante * 0.30) * 12;

        return montoMaximo;
    }
}
