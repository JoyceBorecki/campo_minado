import javax.swing.JButton;

public class Celula extends JButton {

    public int linha;
    public int coluna;

    public boolean temMina;
    public boolean aberta;
    public boolean marcada;

    public int minasAoRedor;

    public Celula(int linha, int coluna) {
        this.linha = linha;
        this.coluna = coluna;
        setFocusable(false);
    }
}
