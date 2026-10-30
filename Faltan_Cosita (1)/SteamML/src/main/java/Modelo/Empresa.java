package Modelo;

import java.time.LocalDate;

public class Empresa extends Cliente implements FuncionCalculo {

    private String rubro;    //Se hacen los datos privados para evitar que otros paquetes puedan accederlos y modificarlos directamente.
    private String director; //Si se dejara que otros módulos toquen los atributos sin gestión alguna, puede ingresarse información no prevista y romper el programa.

    public Empresa(String nom, double ing, double gastosC, boolean historial, LocalDate fecHis, String rub, String dir)
    {
        super(nom,ing,gastosC,historial,fecHis);
        this.rubro = rub;
        this.director = dir;
    }

    public String obtRubro()
    {
        return this.rubro;
    }

    public String obtDirector()
    {
        return this.director;
    }

    @Override
    public boolean evaluarRiesgo()
    {
        //Se podría hacer "return" directamente de la comparación, pero así me parece más prolijo.
        if(this.gastos <= (double) 0.0) //Se verifica que el gasto no sea <0.0> para evitar excepción. No se compara si es nulo porque "double" es tipo de dato primitivo, y el gasto no puede ser negativo.
        {
            return false;
        }

        double parte = this.ingresos/this.gastos;

        if(parte >= (double) 1.5)
        {
            return true;
        } else
        {
            return false;
        }

    }
}