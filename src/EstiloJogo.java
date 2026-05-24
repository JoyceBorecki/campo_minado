import javax.swing.*;
import javax.swing.border.BevelBorder;
import java.awt.*;

public class EstiloJogo {

    public static final Color CINZA_CLARO = new Color(192, 192, 192);
    public static final Color CINZA_ESCURO = new Color(128, 128, 128);

    public static void estilizarPainelPrincipal(JPanel painel) {
        painel.setBackground(CINZA_CLARO);
        painel.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
    }

    public static void estilizarTopo(JPanel topo) {
        topo.setBackground(CINZA_CLARO);
        topo.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createBevelBorder(BevelBorder.LOWERED),
                BorderFactory.createEmptyBorder(6, 6, 6, 6)
        ));
    }

    public static void estilizarTabuleiro(JPanel painelTabuleiro) {
        painelTabuleiro.setBackground(CINZA_ESCURO);
        painelTabuleiro.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
    }

    public static void estilizarCelulaFechada(Celula celula) {
        celula.setPreferredSize(new Dimension(26, 26));
        celula.setMargin(new Insets(0, 0, 0, 0));
        celula.setFont(new Font("Arial", Font.BOLD, 14));
        celula.setBackground(CINZA_CLARO);
        celula.setForeground(Color.BLACK);
        celula.setFocusPainted(false);
        celula.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
    }

    public static void estilizarCelulaAberta(Celula celula) {
        celula.setEnabled(false);
        celula.setBackground(CINZA_CLARO);
        celula.setBorder(BorderFactory.createLineBorder(CINZA_ESCURO));
    }

    public static void estilizarDisplayMinas(JLabel label) {
        label.setFont(new Font("Monospaced", Font.BOLD, 22));
        label.setForeground(Color.RED);
        label.setBackground(Color.BLACK);
        label.setOpaque(true);
        label.setHorizontalAlignment(SwingConstants.CENTER);
        label.setPreferredSize(new Dimension(70, 34));
    }

    public static void estilizarBotaoReiniciar(JButton botao) {
        botao.setFont(new Font("Arial", Font.BOLD, 14));
        botao.setPreferredSize(new Dimension(42, 34));
        botao.setFocusPainted(false);
        botao.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
    }

    public static Color corDoNumero(int numero) {
        return switch (numero) {
            case 1 -> Color.BLUE;
            case 2 -> new Color(0, 128, 0);
            case 3 -> Color.RED;
            case 4 -> new Color(0, 0, 128);
            case 5 -> new Color(128, 0, 0);
            case 6 -> new Color(0, 128, 128);
            case 7 -> Color.BLACK;
            default -> Color.DARK_GRAY;
        };
    }
}