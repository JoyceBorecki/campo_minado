import javax.swing.JFrame;
import javax.swing.SwingUtilities;

public class CampoMinado extends JFrame {

    public CampoMinado() {
        PainelJogo painelJogo = new PainelJogo(Nivel.FACIL);

        setTitle("Campo Minado");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setJMenuBar(painelJogo.criarMenu());
        add(painelJogo);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(CampoMinado::new);
    }
}
