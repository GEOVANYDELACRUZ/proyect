package delacruz.examenfinal.proyectofinal.Entidad;

public class Notas {
    private String DNI;
    private String ApPaterno;
    private String alumNombre;
    private String alumCriterio;
    private String alumNota;

    public Notas() {
    }

    public Notas(String DNI, String apPaterno, String alumNombre, String alumCriterio, String alumNota) {
        this.DNI = DNI;
        this.ApPaterno = apPaterno;
        this.alumNombre = alumNombre;
        this.alumCriterio = alumCriterio;
        this.alumNota = alumNota;
    }

    public String getDNI() {
        return DNI;
    }

    public void setDNI(String DNI) {
        this.DNI = DNI;
    }

    public String getApPaterno() {
        return ApPaterno;
    }

    public void setApPaterno(String apPaterno) {
        ApPaterno = apPaterno;
    }

    public String getAlumNombre() {
        return alumNombre;
    }

    public void setAlumNombre(String alumNombre) {
        this.alumNombre = alumNombre;
    }

    public String getAlumCriterio() {
        return alumCriterio;
    }

    public void setAlumCriterio(String alumCriterio) {
        this.alumCriterio = alumCriterio;
    }

    public String getAlumNota() {
        return alumNota;
    }

    public void setAlumNota(String alumNota) {
        this.alumNota = alumNota;
    }

    @Override
    public String toString() {
        return ApPaterno + "," +alumNombre;
    }
}
