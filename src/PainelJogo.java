import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Objects;
import java.util.Random;

public class PainelJogo extends JPanel {

    private Nivel nivel;
    private Celula[][] celulas;

    private JPanel painelTabuleiro;
    private JLabel labelMinas;
    private JButton botaoReiniciar;

    private ImageIcon iconeFeliz;
    private ImageIcon iconePerdedor;
    private ImageIcon iconeBandeira;
    private ImageIcon iconeMina;
    private ImageIcon iconeVencedor;

    private int marcacoesRestantes;
    private boolean jogoFinalizado;

    public PainelJogo(Nivel nivel) {
        this.nivel = nivel;

        carregarIcones();

        setLayout(new BorderLayout(0, 6));
        EstiloJogo.estilizarPainelPrincipal(this);

        criarTopo();
        criarTabuleiro();
    }

    private void carregarIcones() {
        iconeFeliz = carregarIcone("/assets/feliz.png", 26, 26);
        iconePerdedor = carregarIcone("/assets/perdedor.png", 24, 24);
        iconeBandeira = carregarIcone("/assets/bandeira.png", 18, 18);
        iconeMina = carregarIcone("/assets/mina.png", 18, 18);
        iconeVencedor = carregarIcone("/assets/vencedor.png", 40, 40);
    }

    private ImageIcon carregarIcone(String caminho, int largura, int altura) {
        ImageIcon icone = new ImageIcon(Objects.requireNonNull(getClass().getResource(caminho)));

        Image imagem = icone.getImage().getScaledInstance(
                largura,
                altura,
                Image.SCALE_SMOOTH
        );

        return new ImageIcon(imagem);
    }

    public JMenuBar criarMenu() {
        JMenuBar menuBar = new JMenuBar();
        JMenu menuJogo = new JMenu("Jogo");

        JMenuItem facil = new JMenuItem("Fácil");
        JMenuItem medio = new JMenuItem("Médio");
        JMenuItem avancado = new JMenuItem("Avançado");
        JMenuItem sair = new JMenuItem("Sair");

        facil.addActionListener(e -> reiniciar(Nivel.FACIL));
        medio.addActionListener(e -> reiniciar(Nivel.MEDIO));
        avancado.addActionListener(e -> reiniciar(Nivel.AVANCADO));
        sair.addActionListener(e -> System.exit(0));

        menuJogo.add(facil);
        menuJogo.add(medio);
        menuJogo.add(avancado);
        menuJogo.addSeparator();
        menuJogo.add(sair);

        menuBar.add(menuJogo);

        return menuBar;
    }

    private void criarTopo() {
        JPanel topo = new JPanel(new BorderLayout());

        EstiloJogo.estilizarTopo(topo);

        labelMinas = new JLabel();
        EstiloJogo.estilizarDisplayMinas(labelMinas);

        botaoReiniciar = new JButton(iconeFeliz);
        EstiloJogo.estilizarBotaoReiniciar(botaoReiniciar);

        botaoReiniciar.addActionListener(e -> reiniciar(nivel));

        topo.add(botaoReiniciar, BorderLayout.CENTER);
        topo.add(labelMinas, BorderLayout.EAST);

        add(topo, BorderLayout.NORTH);
    }

    private void criarTabuleiro() {
        jogoFinalizado = false;
        marcacoesRestantes = nivel.minas;

        atualizarDisplayMinas();

        painelTabuleiro = new JPanel(
                new GridLayout(nivel.linhas, nivel.colunas, 1, 1)
        );

        EstiloJogo.estilizarTabuleiro(painelTabuleiro);
        celulas = new Celula[nivel.linhas][nivel.colunas];

        for (int linha = 0; linha < nivel.linhas; linha++) {
            for (int coluna = 0; coluna < nivel.colunas; coluna++) {
                Celula celula = new Celula(linha, coluna);

                EstiloJogo.estilizarCelulaFechada(celula);

                celula.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mousePressed(MouseEvent e) {
                        tratarClique(celula, e);
                    }
                });

                celulas[linha][coluna] = celula;
                painelTabuleiro.add(celula);
            }
        }

        add(painelTabuleiro, BorderLayout.CENTER);

        sortearMinas();
        calcularNumeros();
    }

    private void reiniciar(Nivel novoNivel) {
        nivel = novoNivel;

        removeAll();

        criarTopo();
        criarTabuleiro();

        revalidate();
        repaint();

        Window janela = SwingUtilities.getWindowAncestor(this);

        if (janela != null) {
            janela.pack();
            janela.setLocationRelativeTo(null);
        }
    }

    private void sortearMinas() {
        Random random = new Random();
        int minasSorteadas = 0;

        while (minasSorteadas < nivel.minas) {
            int linha = random.nextInt(nivel.linhas);
            int coluna = random.nextInt(nivel.colunas);

            if (!celulas[linha][coluna].temMina) {
                celulas[linha][coluna].temMina = true;
                minasSorteadas++;
            }
        }
    }

    private void calcularNumeros() {
        for (int linha = 0; linha < nivel.linhas; linha++) {
            for (int coluna = 0; coluna < nivel.colunas; coluna++) {
                if (!celulas[linha][coluna].temMina) {
                    celulas[linha][coluna].minasAoRedor = contarMinasAoRedor(linha, coluna);
                }
            }
        }
    }

    private int contarMinasAoRedor(int linha, int coluna) {
        int contador = 0;

        for (int l = linha - 1; l <= linha + 1; l++) {
            for (int c = coluna - 1; c <= coluna + 1; c++) {
                if (l >= 0 && l < nivel.linhas && c >= 0 && c < nivel.colunas) {
                    if (celulas[l][c].temMina) {
                        contador++;
                    }
                }
            }
        }

        return contador;
    }

    private void tratarClique(Celula celula, MouseEvent e) {
        if (jogoFinalizado || celula.aberta) {
            return;
        }

        if (SwingUtilities.isLeftMouseButton(e)) {
            marcarCelula(celula);
        } else if (SwingUtilities.isRightMouseButton(e)) {
            abrirCelula(celula);
        }
    }

    private void marcarCelula(Celula celula) {
        if (celula.aberta) {
            return;
        }

        if (celula.marcada) {
            celula.marcada = false;
            celula.setIcon(null);
            celula.setText("");
            marcacoesRestantes++;
        } else {
            if (marcacoesRestantes == 0) {
                return;
            }

            celula.marcada = true;
            celula.setIcon(iconeBandeira);
            celula.setText("");
            marcacoesRestantes--;
        }

        atualizarDisplayMinas();
        verificarVitoriaPorMarcacao();
    }

    private void abrirCelula(Celula celula) {
        if (celula.marcada || celula.aberta) {
            return;
        }

        celula.aberta = true;
        celula.setIcon(null);
        EstiloJogo.estilizarCelulaAberta(celula);

        if (celula.temMina) {
            celula.setIcon(iconeMina);
            celula.setText("");
            perderJogo();
            return;
        }

        if (celula.minasAoRedor > 0) {
            celula.setText(String.valueOf(celula.minasAoRedor));
            celula.setForeground(
                    EstiloJogo.corDoNumero(celula.minasAoRedor)
            );
        } else {
            abrirVizinhas(celula.linha, celula.coluna);
        }

        verificarVitoriaPorAbertura();
    }

    private void abrirVizinhas(int linha, int coluna) {
        for (int l = linha - 1; l <= linha + 1; l++) {
            for (int c = coluna - 1; c <= coluna + 1; c++) {
                if (l >= 0 && l < nivel.linhas && c >= 0 && c < nivel.colunas) {
                    Celula vizinha = celulas[l][c];

                    if (!vizinha.aberta && !vizinha.marcada && !vizinha.temMina) {
                        abrirCelula(vizinha);
                    }
                }
            }
        }
    }

    private void perderJogo() {
        jogoFinalizado = true;
        botaoReiniciar.setIcon(iconePerdedor);
        mostrarMinas();

        JOptionPane.showMessageDialog(this, "Você perdeu!");
    }

    private void mostrarMinas() {
        for (int linha = 0; linha < nivel.linhas; linha++) {
            for (int coluna = 0; coluna < nivel.colunas; coluna++) {
                Celula celula = celulas[linha][coluna];

                if (celula.temMina) {
                    celula.setIcon(iconeMina);
                    celula.setText("");
                }
            }
        }
    }

    private void verificarVitoriaPorMarcacao() {
        int minasMarcadas = 0;

        for (int linha = 0; linha < nivel.linhas; linha++) {
            for (int coluna = 0; coluna < nivel.colunas; coluna++) {
                Celula celula = celulas[linha][coluna];

                if (celula.marcada && !celula.temMina) {
                    return;
                }

                if (celula.marcada && celula.temMina) {
                    minasMarcadas++;
                }
            }
        }

        if (minasMarcadas == nivel.minas) {
            vencerJogo();
        }
    }

    private void verificarVitoriaPorAbertura() {
        int celulasFechadas = 0;

        for (int linha = 0; linha < nivel.linhas; linha++) {
            for (int coluna = 0; coluna < nivel.colunas; coluna++) {
                if (!celulas[linha][coluna].aberta) {
                    celulasFechadas++;
                }
            }
        }

        if (celulasFechadas == nivel.minas) {
            vencerJogo();
        }
    }

    private void vencerJogo() {
        if (jogoFinalizado) {
            return;
        }

        jogoFinalizado = true;
        botaoReiniciar.setIcon(iconeVencedor);

        JOptionPane.showMessageDialog(this, "Parabéns! Você venceu!");
    }

    private void atualizarDisplayMinas() {
        labelMinas.setText(
                String.format("%03d", marcacoesRestantes)
        );
    }
}