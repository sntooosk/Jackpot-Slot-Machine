package com.apostas.view;

import com.apostas.DAO.PartidaDAO;
import com.apostas.factory.ModuloConexao;
import com.apostas.model.FormataMoeda;
import com.apostas.model.Partida;
import lib.som.modelSom;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;
import java.util.Random;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

public class TelaAposta extends javax.swing.JFrame {

    private final modelSom som = new modelSom();
    private final String[] imagens = {
        "D:\\Projetos Completos\\Projeto JackPot777\\JackPot777\\src\\lib\\img\\imagem1.png",
        "D:\\Projetos Completos\\Projeto JackPot777\\JackPot777\\src\\lib\\img\\imagem2.png",
        "D:\\Projetos Completos\\Projeto JackPot777\\JackPot777\\src\\lib\\img\\imagem3.png",
        "D:\\Projetos Completos\\Projeto JackPot777\\JackPot777\\src\\lib\\img\\imagem4.png"
    };

    private final JLabel[] icones = new JLabel[3];
    private int contador = 0;
    public double dinheiro = 0.00;
    private int rodadas;
    private int giros;
    private String nome;
    private String dificuldade;
    private double dinheiroGanho = 0.00;


    FormataMoeda formatador = new FormataMoeda();

    public TelaAposta() {
        initComponents();
        configurarInterface();
        solicitarDadosDoUsuario();
        configurarNotas();
        ModuloConexao.conectar();

        carregarLabels();
        carregaDinheiro(0.00);
        carregaGanho(0.00);

    }

    // Inicializa os JLabels usados para exibir as imagens
    private void carregarLabels() {
        icones[0] = icon1;
        icones[1] = icon2;
        icones[2] = icon3;
    }

    // Método para iniciar a lógica do jogo
    private void começar() {
        if (rodadas > 0) {
            som.play();
            icon1.setIcon(null);
            icon2.setIcon(null);
            icon3.setIcon(null);
            lblAlavancaup.setVisible(false);
            lblAlavancad.setVisible(true);
            lblDinheiro5.setVisible(false);
            lblDinheiro10.setVisible(false);
            lblDinheiro20.setVisible(false);
            lblDinheiro50.setVisible(false);
            lblDinheiro100.setVisible(false);
            lblDinheiro200.setVisible(false);
            girar();
        } else {
            JOptionPane.showMessageDialog(null, "Adicione dinheiro para mais rodadas");
            solicitarDadosDoUsuario();
            configurarNotasConformeDificuldade();

            icon1.setIcon(null);
            icon2.setIcon(null);
            icon3.setIcon(null);
            dinheiro = 0;
            dinheiroGanho = 0;
            lblValorApostado.setText(formatador.ConvMoeda(String.valueOf(dinheiro)));
            lblValorGanho.setText(formatador.ConvMoeda(String.valueOf(dinheiroGanho)));
        }
    }

    // Encerra o jogo
    private void parar() {
        som.stop();
        verificarResultado();
        salvaBilhete();
    }

    private void salvaBilhete() {

        String valorGanho = lblValorGanho.getText();
        String valorApostado = lblValorApostado.getText();
        String data = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        String hora = new SimpleDateFormat("HH:mm:ss").format(new Date());
        String status = "Emitido Java";

        Partida partida = new Partida(nome, data, hora, valorApostado, valorGanho, status);
        PartidaDAO partidaDao = new PartidaDAO();
        partidaDao.salvaPartida(partida);
    }

    // Novo método para ajustar dinamicamente o número de giros com base na dificuldade
    private int calcularGiros() {

        if (dificuldade.equals("Fácil")) {
            return 10;
        } else if (dificuldade.equals("Médio")) {
            return 20;
        } else if (dificuldade.equals("Difícil")) {
            return 30;
        }
        return 10; // Valor padrão
    }

    private double calcularGanho() {
        if (dificuldade.equals("Fácil")) {
            return 2.0;
        } else if (dificuldade.equals("Médio")) {
            return 15.5;
        } else if (dificuldade.equals("Difícil")) {
            return 20.5;
        }
        return 0;
    }

    // Método para ajustar dinamicamente o número de giros e taxa de ganho
    private void girar() {
        Thread thread = new Thread(() -> {
            giros = calcularGiros(); // Usando o método para calcular dinamicamente o número de giros
            for (contador = 0; contador < giros; contador++) {
                for (int i = 0; i < 3; i++) {
                    int sorteio = new Random().nextInt(imagens.length);
                    String url = imagens[sorteio];
                    ImageIcon iconeImagem = new ImageIcon(url);
                    Image img = iconeImagem.getImage().getScaledInstance(icones[i].getWidth(), icones[i].getHeight(), Image.SCALE_SMOOTH);
                    icones[i].setIcon(new ImageIcon(img));
                }
                try {
                    Thread.sleep(250);
                } catch (InterruptedException e) {
                    System.out.println("Erro: " + e);
                }
            }

            parar();
        });

        thread.start();
        rodadas--;
        lblTentativas.setText(String.valueOf(rodadas));
    }

    // Método para verificar o resultado do jogo com base nas taxas de ganho ajustadas dinamicamente
    private void verificarResultado() {
        BufferedImage[] img = new BufferedImage[3];
        img[0] = getBufferedImageFromIcon(icon1.getIcon());
        img[1] = getBufferedImageFromIcon(icon2.getIcon());
        img[2] = getBufferedImageFromIcon(icon3.getIcon());

        if (isBufferedImagesEqual(img[0], img[1]) && isBufferedImagesEqual(img[1], img[2])) {
            carregaGanho(dinheiro += calcularGanho()); // Ajustando dinamicamente o ganho com base na taxa
            JOptionPane.showMessageDialog(this, "Parabéns! Você ganhou o jackpot!");

        } else {
            carregaGanho(dinheiro -= calcularGanho()); // Perda total se não houver correspondência
            JOptionPane.showMessageDialog(this, "Tente novamente. Não foram encontradas três imagens iguais.");

        }
        lblAlavancaup.setVisible(true);
        lblAlavancad.setVisible(false);
    }

    // Converte um Icon em BufferedImage
    private BufferedImage getBufferedImageFromIcon(Icon icon) {
        BufferedImage bufferedImage = new BufferedImage(icon.getIconWidth(), icon.getIconHeight(), BufferedImage.TYPE_INT_ARGB);
        Graphics g = bufferedImage.getGraphics();
        icon.paintIcon(null, g, 0, 0);
        g.dispose();
        return bufferedImage;
    }

    // Verifica se duas BufferedImages são iguais
    private boolean isBufferedImagesEqual(BufferedImage img1, BufferedImage img2) {
        if (img1.getWidth() != img2.getWidth() || img1.getHeight() != img2.getHeight()) {
            return false;
        }

        for (int x = 0; x < img1.getWidth(); x++) {
            for (int y = 0; y < img1.getHeight(); y++) {
                if (img1.getRGB(x, y) != img2.getRGB(x, y)) {
                    return false;
                }
            }
        }

        return true;
    }

    // Atualiza o valor do dinheiro
    private void carregaDinheiro(double contador) {
        dinheiro += contador;
        lblValorApostado.setText(formatador.ConvMoeda(String.valueOf(dinheiro)));
        carregaRodadas();
    }

    // Atualiza o valor do ganho
    private void carregaGanho(double entrada) {
        dinheiroGanho = entrada;
        lblValorGanho.setText(formatador.ConvMoeda(String.valueOf(dinheiroGanho)));
    }

    // Calcula o número de rodadas disponíveis com base no dinheiro atual
    private void carregaRodadas() {
        rodadas = (int) (dinheiro / 4);
        lblTentativas.setText(String.valueOf(rodadas));
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        icon1 = new javax.swing.JLabel();
        icon2 = new javax.swing.JLabel();
        icon3 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        lblDinheiro10 = new javax.swing.JLabel();
        lblDinheiro5 = new javax.swing.JLabel();
        lblValorGanho = new javax.swing.JLabel();
        lblValorDiculdade = new javax.swing.JLabel();
        lblTentativas = new javax.swing.JLabel();
        lblAlavancad = new javax.swing.JLabel();
        lblAlavancaup = new javax.swing.JLabel();
        lblNome = new javax.swing.JLabel();
        lblValorApostado = new javax.swing.JLabel();
        lblDinheiro20 = new javax.swing.JLabel();
        lblDinheiro50 = new javax.swing.JLabel();
        lblDinheiro100 = new javax.swing.JLabel();
        lblDinheiro200 = new javax.swing.JLabel();
        lblFundo = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("X -  777");
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowOpened(java.awt.event.WindowEvent evt) {
                formWindowOpened(evt);
            }
        });
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        getContentPane().add(icon1, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 230, 50, 120));
        getContentPane().add(icon2, new org.netbeans.lib.awtextra.AbsoluteConstraints(280, 230, 50, 120));
        getContentPane().add(icon3, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 230, 50, 120));

        jLabel1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel1MouseClicked(evt);
            }
        });
        getContentPane().add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 400, 120, 50));

        lblDinheiro10.setFont(new java.awt.Font("Arial", 1, 20)); // NOI18N
        lblDinheiro10.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        lblDinheiro10.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblDinheiro10MouseClicked(evt);
            }
        });
        getContentPane().add(lblDinheiro10, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 10, 82, 32));

        lblDinheiro5.setFont(new java.awt.Font("Arial", 1, 20)); // NOI18N
        lblDinheiro5.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        lblDinheiro5.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblDinheiro5MouseClicked(evt);
            }
        });
        getContentPane().add(lblDinheiro5, new org.netbeans.lib.awtextra.AbsoluteConstraints(410, 10, 82, 32));

        lblValorGanho.setBackground(new java.awt.Color(255, 255, 255));
        lblValorGanho.setFont(new java.awt.Font("Arial", 1, 20)); // NOI18N
        lblValorGanho.setForeground(new java.awt.Color(255, 255, 255));
        lblValorGanho.setText("Ganho\\Perca");
        lblValorGanho.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        getContentPane().add(lblValorGanho, new org.netbeans.lib.awtextra.AbsoluteConstraints(450, 80, 140, -1));

        lblValorDiculdade.setBackground(new java.awt.Color(255, 255, 255));
        lblValorDiculdade.setFont(new java.awt.Font("Arial", 1, 20)); // NOI18N
        lblValorDiculdade.setForeground(new java.awt.Color(255, 255, 255));
        lblValorDiculdade.setText("DIFICULDADE");
        lblValorDiculdade.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        getContentPane().add(lblValorDiculdade, new org.netbeans.lib.awtextra.AbsoluteConstraints(450, 140, 140, -1));

        lblTentativas.setBackground(new java.awt.Color(255, 255, 255));
        lblTentativas.setFont(new java.awt.Font("Arial", 1, 20)); // NOI18N
        lblTentativas.setForeground(new java.awt.Color(255, 255, 255));
        lblTentativas.setText("      ");
        lblTentativas.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        getContentPane().add(lblTentativas, new org.netbeans.lib.awtextra.AbsoluteConstraints(330, 50, 110, 100));

        lblAlavancad.setIcon(new javax.swing.ImageIcon(getClass().getResource("/lib/img/alavancadown.png"))); // NOI18N
        lblAlavancad.setText("jLabel2");
        lblAlavancad.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblAlavancadMouseClicked(evt);
            }
        });
        getContentPane().add(lblAlavancad, new org.netbeans.lib.awtextra.AbsoluteConstraints(530, 240, 50, 180));

        lblAlavancaup.setIcon(new javax.swing.ImageIcon(getClass().getResource("/lib/img/alavancaup.png"))); // NOI18N
        lblAlavancaup.setText("jLabel2");
        lblAlavancaup.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblAlavancaupMouseClicked(evt);
            }
        });
        getContentPane().add(lblAlavancaup, new org.netbeans.lib.awtextra.AbsoluteConstraints(550, 180, 50, 180));

        lblNome.setBackground(new java.awt.Color(255, 255, 255));
        lblNome.setFont(new java.awt.Font("Arial", 1, 20)); // NOI18N
        lblNome.setForeground(new java.awt.Color(255, 255, 255));
        lblNome.setText("Nome");
        lblNome.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        getContentPane().add(lblNome, new org.netbeans.lib.awtextra.AbsoluteConstraints(450, 50, 140, -1));

        lblValorApostado.setBackground(new java.awt.Color(255, 255, 255));
        lblValorApostado.setFont(new java.awt.Font("Arial", 1, 20)); // NOI18N
        lblValorApostado.setForeground(new java.awt.Color(255, 255, 255));
        lblValorApostado.setText("APOSTADO");
        lblValorApostado.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        getContentPane().add(lblValorApostado, new org.netbeans.lib.awtextra.AbsoluteConstraints(450, 110, 140, -1));

        lblDinheiro20.setFont(new java.awt.Font("Arial", 1, 20)); // NOI18N
        lblDinheiro20.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        lblDinheiro20.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblDinheiro20MouseClicked(evt);
            }
        });
        getContentPane().add(lblDinheiro20, new org.netbeans.lib.awtextra.AbsoluteConstraints(410, 10, 82, 32));

        lblDinheiro50.setFont(new java.awt.Font("Arial", 1, 20)); // NOI18N
        lblDinheiro50.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        lblDinheiro50.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblDinheiro50MouseClicked(evt);
            }
        });
        getContentPane().add(lblDinheiro50, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 10, 82, 32));

        lblDinheiro100.setFont(new java.awt.Font("Arial", 1, 20)); // NOI18N
        lblDinheiro100.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        lblDinheiro100.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblDinheiro100MouseClicked(evt);
            }
        });
        getContentPane().add(lblDinheiro100, new org.netbeans.lib.awtextra.AbsoluteConstraints(410, 10, 82, 32));

        lblDinheiro200.setFont(new java.awt.Font("Arial", 1, 20)); // NOI18N
        lblDinheiro200.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        lblDinheiro200.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblDinheiro200MouseClicked(evt);
            }
        });
        getContentPane().add(lblDinheiro200, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 10, 82, 32));

        lblFundo.setFont(new java.awt.Font("Arial", 1, 20)); // NOI18N
        getContentPane().add(lblFundo, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 600, 500));

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void formWindowOpened(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowOpened

    }//GEN-LAST:event_formWindowOpened

    private void configurarInterface() {
        // Configurar a cor de fundo
        getContentPane().setBackground(Color.WHITE);

        // Configurar a imagem de ícone
        setIconImage(new ImageIcon(Objects.requireNonNull(getClass().getResource("/lib/img/Fundo.jpg"))).getImage());

        // Configurar imagem de fundo
        configurarImagemParaLabel(lblFundo, "/lib/img/Fundo.jpg");

        // Configurar imagem padrão para os icones
        configurarImagemParaLabel(lblDinheiro5, "/lib/img/Nota5.jpg");
        configurarImagemParaLabel(lblDinheiro10, "/lib/img/Nota10.jpg");
        configurarImagemParaLabel(lblDinheiro20, "/lib/img/Nota20.jpg");
        configurarImagemParaLabel(lblDinheiro50, "/lib/img/Nota50.jpg");
        configurarImagemParaLabel(lblDinheiro100, "/lib/img/Nota100.jpg");
        configurarImagemParaLabel(lblDinheiro200, "/lib/img/Nota200.jpg");

        // Tornar lblAlavancad invisível
        lblAlavancad.setVisible(false);
    }

    private void configurarImagemParaLabel(JLabel label, String imagePath) {
        ImageIcon imagem = new ImageIcon(Objects.requireNonNull(TelaAposta.class.getResource(imagePath)));
        Image imagemRedimensionada = imagem.getImage().getScaledInstance(label.getWidth(), label.getHeight(), Image.SCALE_DEFAULT);
        label.setIcon(new ImageIcon(imagemRedimensionada));
    }

    private void solicitarDadosDoUsuario() {
        nome = JOptionPane.showInputDialog(null, "Digite seu Nome:", "Nome", JOptionPane.PLAIN_MESSAGE);

        if (nome == null || nome.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Operação cancelada");
            System.exit(0); // Saia do programa se o usuário cancelar
        }

        // Opções para a caixa de combinação
        String[] opcoes = {"Fácil", "Médio", "Difícil"};

        // Criação da caixa de combinação
        JComboBox<String> comboBox = new JComboBox<>(opcoes);

        // Mensagem do JOptionPane com a caixa de combinação para escolher a dificuldade
        int escolha = JOptionPane.showConfirmDialog(
                null,
                comboBox,
                "Escolha a dificuldade:",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE
        );

        if (escolha == JOptionPane.OK_OPTION) {
            dificuldade = (String) comboBox.getSelectedItem();
            lblValorDiculdade.setText(dificuldade);
            lblNome.setText(nome);
        } else {
            JOptionPane.showMessageDialog(null, "Operação cancelada");
            System.exit(0); // Saia do programa se o usuário cancelar
        }
    }

    private void configurarNotas() {
        configurarNotasConformeDificuldade();
    }

    private void configurarNotasConformeDificuldade() {
        lblDinheiro5.setVisible(true);
        lblDinheiro10.setVisible(true);
        lblDinheiro20.setVisible(true);
        lblDinheiro50.setVisible(true);
        lblDinheiro100.setVisible(true);
        lblDinheiro200.setVisible(true);

        if (dificuldade.equals("Fácil")) {
            lblDinheiro20.setVisible(false);
            lblDinheiro50.setVisible(false);
            lblDinheiro100.setVisible(false);
            lblDinheiro200.setVisible(false);
        } else if (dificuldade.equals("Médio")) {
            lblDinheiro10.setVisible(false);
            lblDinheiro5.setVisible(false);
            lblDinheiro100.setVisible(false);
            lblDinheiro200.setVisible(false);
        } else if (dificuldade.equals("Difícil")) {
            lblDinheiro20.setVisible(false);
            lblDinheiro50.setVisible(false);
            lblDinheiro5.setVisible(false);
            lblDinheiro10.setVisible(false);
        }
    }

    private void jLabel1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel1MouseClicked
        // TODO add your handling code here:


    }//GEN-LAST:event_jLabel1MouseClicked

    private void lblAlavancadMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblAlavancadMouseClicked
        // TODO add your handling code here:

    }//GEN-LAST:event_lblAlavancadMouseClicked

    private void lblAlavancaupMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblAlavancaupMouseClicked
        // TODO add your handling code here:
        começar();
    }//GEN-LAST:event_lblAlavancaupMouseClicked

    private void lblDinheiro50MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblDinheiro50MouseClicked
        // TODO add your handling code here:
        carregaDinheiro(50);

    }//GEN-LAST:event_lblDinheiro50MouseClicked

    private void lblDinheiro20MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblDinheiro20MouseClicked
        // TODO add your handling code here:
        carregaDinheiro(20);

    }//GEN-LAST:event_lblDinheiro20MouseClicked

    private void lblDinheiro100MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblDinheiro100MouseClicked
        // TODO add your handling code here:
        carregaDinheiro(100);

    }//GEN-LAST:event_lblDinheiro100MouseClicked

    private void lblDinheiro200MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblDinheiro200MouseClicked
        // TODO add your handling code here:
        carregaDinheiro(200);

    }//GEN-LAST:event_lblDinheiro200MouseClicked

    private void lblDinheiro5MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblDinheiro5MouseClicked

        carregaDinheiro(5);

    }//GEN-LAST:event_lblDinheiro5MouseClicked

    private void lblDinheiro10MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblDinheiro10MouseClicked

        carregaDinheiro(10);

    }//GEN-LAST:event_lblDinheiro10MouseClicked

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;

                }
            }
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(TelaAposta.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);

        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> {
            new TelaAposta().setVisible(true);
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel icon1;
    private javax.swing.JLabel icon2;
    private javax.swing.JLabel icon3;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel lblAlavancad;
    private javax.swing.JLabel lblAlavancaup;
    private javax.swing.JLabel lblDinheiro10;
    private javax.swing.JLabel lblDinheiro100;
    private javax.swing.JLabel lblDinheiro20;
    private javax.swing.JLabel lblDinheiro200;
    private javax.swing.JLabel lblDinheiro5;
    private javax.swing.JLabel lblDinheiro50;
    private javax.swing.JLabel lblFundo;
    private javax.swing.JLabel lblNome;
    private javax.swing.JLabel lblTentativas;
    private javax.swing.JLabel lblValorApostado;
    private javax.swing.JLabel lblValorDiculdade;
    private javax.swing.JLabel lblValorGanho;
    // End of variables declaration//GEN-END:variables
}
