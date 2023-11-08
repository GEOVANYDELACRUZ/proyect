package delacruz.examenfinal.alumnoapp.Entidades;

public class Cursos {
    private String codCurso;
    private String codNota;
    private String curNombre;

    public Cursos(String codCurso, String codNota, String curNombre) {
        this.codCurso = codCurso;
        this.codNota = codNota;
        this.curNombre = curNombre;
    }

    public Cursos() {
    }

    public String getCodCurso() {
        return codCurso;
    }

    public void setCodCurso(String codCurso) {
        this.codCurso = codCurso;
    }

    public String getCodNota() {
        return codNota;
    }

    public void setCodNota(String codNota) {
        this.codNota = codNota;
    }

    public String getCurNombre() {
        return curNombre;
    }

    public void setCurNombre(String curNombre) {
        this.curNombre = curNombre;
    }

    @Override
    public String toString() {
        return curNombre;
    }
}
