package Modelo;

import java.time.LocalDate;

//TIENE QUE HABER SUPERADMINISTRADOR.

public class Persona extends Cliente implements FuncionCalculo {

    public Persona(String nombre, double ingresos, double gastos, boolean buenHistorial, LocalDate fechaHistorial){
        super(nombre, ingresos, gastos, buenHistorial, fechaHistorial);   
    }
    

    @Override 
    public boolean evaluarRiesgo()
    {
        if(!this.buenHistorial) //En caso de que no tenga buen historial:
        {
            return false; //Se descarta directamente.
        } else //En caso de que tenga buen historial:
        {
            double margen = this.ingresos - this.gastos; //Se calcula el margen entre ingresos y gastos
            if(margen >= 0) //Si la ganancia es positiva o equilibrada:
            {
                return true; //Se permite el  crédito.
            }
            return false; //En caso de que la ganancia sea negativa, se descarta.
        }
    }
}