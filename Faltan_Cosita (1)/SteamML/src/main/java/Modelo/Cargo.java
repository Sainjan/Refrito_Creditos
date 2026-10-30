package Modelo;

public enum Cargo {
    ADMINISTRADOR(1,"Administrador"),
    PERSONA(2,"Persona"),
    EMPRESA(3,"Empresa"); //EN la interfaz hay que hacer que los atributos extra de "EMPRESA" estén escondidos y el Listener del ComboBox los haga aparecer.

    private int idCargo;
    private String descripcion;

    Cargo(int id, String des)
    {
        this.idCargo = id;
        this.descripcion = des;
    }

    public int obtIdCargo()
    {
        return this.idCargo;
    }

    public String obtDescripcion()
    {
        return this.descripcion;
    }
}
