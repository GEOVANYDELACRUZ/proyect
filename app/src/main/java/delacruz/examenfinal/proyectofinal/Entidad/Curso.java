package delacruz.examenfinal.proyectofinal.Entidad;

public class Curso {
    private String codigo;
    private String curso;

    public Curso() {
    }

    public Curso(String codigo, String curso) {
        this.codigo = codigo;
        this.curso = curso;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getCurso() {
        return curso;
    }

    public void setCurso(String curso) {
        this.curso = curso;
    }

    @Override
    public String toString() {
        return curso;
    }
}
