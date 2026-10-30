package Modelo;

import java.time.LocalDate;

public abstract class Cliente {

    protected String nombre;
    protected double ingresos, gastos; //Se hacen protegidos para que únicamente pueda accederlo el paquete en sí. De este modo, solamente lo tratan las hijas de "Cliente".
    protected boolean buenHistorial;
    protected LocalDate fechaHistorial;

    protected Cliente(String nom, double ing, double gastosC, boolean historial, LocalDate fecHis)
    {
        this.nombre = nom;
        this.ingresos = ing;
        this.gastos = gastosC;
        this.buenHistorial = historial;
        this.fechaHistorial = fecHis;
    }

    public String obtNombre()
    {
        return this.nombre;
    }

    public double obtIngresos()
    {
        return this.ingresos;
    }

    public double obtGastos()
    {
        return this.gastos;
    }

    public boolean obtHistorial()
    {
        return this.buenHistorial;
    }

    public LocalDate obtFecHis()
    {
        return this.fechaHistorial;
    }

}