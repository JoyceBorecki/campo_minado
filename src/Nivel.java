public enum Nivel {
    FACIL(9, 9, 10),
    MEDIO(16, 16, 40),
    AVANCADO(16, 30, 99);

    public int linhas;
    public int colunas;
    public int minas;

    Nivel(int linhas, int colunas, int minas) {
        this.linhas = linhas;
        this.colunas = colunas;
        this.minas = minas;
    }
}
