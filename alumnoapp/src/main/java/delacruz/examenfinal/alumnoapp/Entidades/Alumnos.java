package delacruz.examenfinal.alumnoapp.Entidades;

public class Alumnos {
    private String CodAlumno;
    private String alumApPaterno;
    private String alumApMaterno;
    private String alumNombres;

    public Alumnos() {
    }

    public Alumnos(String codAlumno, String alumApPaterno, String alumApMaterno, String alumNombres) {
        this.CodAlumno = codAlumno;
        this.alumApPaterno = alumApPaterno;
        this.alumApMaterno = alumApMaterno;
        this.alumNombres = alumNombres;
    }

    public String getCodAlumno() {
        return CodAlumno;
    }

    public void setCodAlumno(String codAlumno) {
        CodAlumno = codAlumno;
    }

    public String getAlumApPaterno() {
        return alumApPaterno;
    }

    public void setAlumApPaterno(String alumApPaterno) {
        this.alumApPaterno = alumApPaterno;
    }

    public String getAlumApMaterno() {
        return alumApMaterno;
    }

    public void setAlumApMaterno(String alumApMaterno) {
        this.alumApMaterno = alumApMaterno;
    }

    public String getAlumNombres() {
        return alumNombres;
    }

    public void setAlumNombres(String alumNombres) {
        this.alumNombres = alumNombres;
    }

    @Override
    public String toString() {
        return  CodAlumno+": "+alumApPaterno+", "+alumNombres;}
}
