package delacruz.examenfinal.proyectofinal.Entidad;

public class Profesor {
    private Integer IDProfesor;
    private String profNombre;
    private String profApellidos;
    private String profCorreo;
    private String profContra;

    public Profesor() {
    }

    public Profesor(Integer IDProfesor, String profNombre, String profApellidos, String profCorreo, String profContra) {
        this.IDProfesor = IDProfesor;
        this.profNombre = profNombre;
        this.profApellidos = profApellidos;
        this.profCorreo = profCorreo;
        this.profContra = profContra;
    }

    public Integer getIDProfesor() {
        return IDProfesor;
    }

    public void setIDProfesor(Integer IDProfesor) {
        this.IDProfesor = IDProfesor;
    }

    public String getProfNombre() {
        return profNombre;
    }

    public void setProfNombre(String profNombre) {
        this.profNombre = profNombre;
    }

    public String getProfApellidos() {
        return profApellidos;
    }

    public void setProfApellidos(String profApellidos) {
        this.profApellidos = profApellidos;
    }

    public String getProfCorreo() {
        return profCorreo;
    }

    public void setProfCorreo(String profCorreo) {
        this.profCorreo = profCorreo;
    }

    public String getProfContra() {
        return profContra;
    }

    public void setProfContra(String profContra) {
        this.profContra = profContra;
    }

    @Override
    public String toString() {
        return  profCorreo ;
    }
}
