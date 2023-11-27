package delacruz.examenfinal.proyectofinal.Entidad;

public class Alumnos {
    private Integer pos;
    private String CodAlumno;
    private String alumApPaterno;
    private String alumApMaterno;
    private String alumNombres;

    public Alumnos() {
    }

    public Alumnos(Integer pos, String codAlumno, String alumApPaterno, String alumApMaterno, String alumNombres) {
        this.pos = pos;
        CodAlumno = codAlumno;
        this.alumApPaterno = alumApPaterno;
        this.alumApMaterno = alumApMaterno;
        this.alumNombres = alumNombres;
    }

    public Integer getPos() {
        return pos;
    }

    public void setPos(Integer pos) {
        this.pos = pos;
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
        return  "("+pos+") "+CodAlumno+": "+alumApPaterno+", "+alumNombres;}
}
