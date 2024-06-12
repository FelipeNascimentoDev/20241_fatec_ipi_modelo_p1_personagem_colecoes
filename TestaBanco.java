import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Random;
import javax.swing.JOptionPane;

public class TestaBanco {
    private static final String url = "jdbc:mysql://avnadmin:AVNS_AdAQnSuhut3F_ocHGND@mysql-3b3fa6b-felipenn-1st-project.c.aivencloud.com:28215/defaultdb?ssl-mode=REQUIRED";
    private static final String user = "avnadmin";
    private static final String password = "AVNS_AdAQnSuhut3F_ocHGND";

    public static void main(String[] args) {
        try {
            Connection conn = DriverManager.getConnection(url, user, password);
            if (login(conn)) {
                exibirMenu(conn);
            }
            else {
                int SimNao = JOptionPane.showOptionDialog(null,
                "Nome de usuário ou senha incorretos. O que você gostaria de fazer?",
                    "Login Falhou",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    new Object[]{"Tentar Novamente", "Sair"},
                    "Tentar Novamente");

                if (SimNao == JOptionPane.NO_OPTION) {
                    JOptionPane.showMessageDialog(null, "Saindo... Até mais!");
                }
                else {
                   main(args);
                }

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public static void exibirMenu(Connection conn) {
        Personagem personagem = new Personagem("Herói", 10, 5, 3);

        while (true) {
            String[] opcoes = {"Jogar", "Consultar Log", "Sair"};
            int escolha = JOptionPane.showOptionDialog(null, "Menu de Opções", "Menu",
                    JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, opcoes, opcoes[0]);

            switch (escolha) {
                case 0:
                    jogar(personagem, conn);
                    break;
                case 1:
                    consultarLog(conn);
                    break;
                case 2:
                    JOptionPane.showMessageDialog(null, "Saindo...Até mais!");
                    return;
                default:
                    JOptionPane.showMessageDialog(null, "Opção inválida. Tente novamente.");
            }
        }
    }


    public static boolean login(Connection conn) {
        String nome_usuario = JOptionPane.showInputDialog("Digite o nome de usuário:");
        String senha_usuario = JOptionPane.showInputDialog("Digite a senha:");

        String sql = "SELECT * FROM tb_usuario WHERE nome_usuario = ? AND senha_usuario = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, nome_usuario);
            ps.setString(2, senha_usuario);

        try (ResultSet rs = ps.executeQuery()) { //ResultSet vai armazenar o que foi encontrado na busca com o ps que a gente fez
            if (rs.next()) { // rs.next verifica se o rs trouxe pelo menos uma linha da tabela de resultado
                JOptionPane.showMessageDialog(null, "Login bem-sucedido!");
                return true;
            }
        }
    }
    catch (SQLException e) {
        e.printStackTrace();
    }

    return false;
}


    public static void jogar(Personagem personagem, Connection conn) {
        Random random = new Random();
        String[] atividades = {"comer", "dormir", "caçar"};

        StringBuilder atividadesRealizadas = new StringBuilder("Atividades realizadas:\n");

        for (int i = 0; i < 10; i++) {
            String atividade = atividades[random.nextInt(atividades.length)];
            personagem.realizarAtividade(atividade, conn);
            atividadesRealizadas.append(atividade).append("\n");
        }

        JOptionPane.showMessageDialog(null, atividadesRealizadas.toString());
    }

    
    public static void consultarLog(Connection conn) {
        String sql = "SELECT * FROM tb_atividade ORDER BY cod_atividade DESC";
        StringBuilder logAtividades = new StringBuilder("Log de atividades:\n");

        try (PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                int id = rs.getInt("cod_atividade");
                String descricao = rs.getString("descricao");
                String dataDeOcorrencia = rs.getString("data_de_ocorrencia");

                logAtividades.append(String.format("ID: %d, Descrição: %s, Data de Ocorrência: %s%n", id, descricao, dataDeOcorrencia));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        JOptionPane.showMessageDialog(null, logAtividades.toString());
    }
}