package delacruz.examenfinal.proyectofinal.Entidad;

public class Registro {
    private String Cod;
    private String Nombre;

    public Registro() {
    }

    public Registro(String cod, String nombre) {
        Cod = cod;
        Nombre = nombre;
    }

    public String getCod() {
        return Cod;
    }

    public void setCod(String cod) {
        Cod = cod;
    }

    public String getNombre() {
        return Nombre;
    }

    public void setNombre(String nombre) {
        Nombre = nombre;
    }

    @Override
    public String toString() {
        return Nombre;
    }
}
