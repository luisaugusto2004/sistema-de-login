import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class LoginForm extends JFrame {

    // Componentes da interface gráfica
    private JTextField txtUsuario;
    private JPasswordField txtSenha;
    private JButton btnLogar;

    public LoginForm() {
        // Configurações da janela
        setTitle("Tela de Login");
        setSize(300, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);  // Centraliza a janela na tela

        // Criação dos componentes
        txtUsuario = new JTextField(20);
        txtSenha = new JPasswordField(20);
        btnLogar = new JButton("Logar");

        // Layout
        setLayout(new FlowLayout());

        // Adicionando os componentes à janela
        add(new JLabel("Usuário:"));
        add(txtUsuario);
        add(new JLabel("Senha:"));
        add(txtSenha);
        add(btnLogar);

        // Ação do botão Logar
        btnLogar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Obter dados de entrada
                String usuario = txtUsuario.getText();
                String senha = new String(txtSenha.getPassword());

                // Verificar o login com o banco de dados
                if (verificarLogin(usuario, senha)) {
                    JOptionPane.showMessageDialog(null, "Login bem-sucedido!");
                } else {
                    JOptionPane.showMessageDialog(null, "Usuário ou senha inválidos.");
                }
            }
        });
    }

    // Método para verificar se o login existe no banco de dados
    private boolean verificarLogin(String usuario, String senha) {
        Connection conn = null;
        try {
            // Obter a conexão com o banco de dados através da classe DatabaseConnection
            conn = DatabaseConnection.getConnection();

            if (conn == null) {
                JOptionPane.showMessageDialog(null, "Erro ao conectar ao banco de dados.");
                return false;
            }

            // SQL para verificar o login
            String sql = "SELECT * FROM clientes WHERE usuario = ? AND senha = ?";

            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, usuario);
                stmt.setString(2, senha);

                // Executa a consulta
                ResultSet rs = stmt.executeQuery();

                // Se houver resultados, significa que o login é válido
                return rs.next();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            // Fechar a conexão com o banco
            try {
                if (conn != null && !conn.isClosed()) {
                    conn.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return false;
    }
}