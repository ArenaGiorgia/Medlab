public class Sede {
    private Integer codice;
    private String nome;


    public Sede(String nome, Integer codice) {
        this.nome = nome;
        this.codice = codice;

    }

    public Integer getCodice() {
        return codice;
    }

    public String getNome() {
        return nome;
    }

    public void setCodice(Integer codice) {
        this.codice = codice;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    @Override
    public String toString() {
        return "Sede{" +
                "codice=" + codice +
                ", nome='" + nome + '\'' +
                '}';
    }
}
