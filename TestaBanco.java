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
            exibirMenu(conn);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public static void exibirMenu(Connection conn) {
        Personagem personagem = new Personagem("Herói", 10, 5, 3);

        while (true) {
            String[] options = {"Jogar", "Consultar Log", "Sair"};
            int escolha = JOptionPane.showOptionDialog(null, "Menu de Opções", "Menu",
                    JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);

            switch (escolha) {
                case 0:
                    jogar(personagem, conn);
                    break;
                case 1:
                    consultarLog(conn);
                    break;
                case 2:
                    JOptionPane.showMessageDialog(null, "Saindo...");
                    return;
                default:
                    JOptionPane.showMessageDialog(null, "Opção inválida. Tente novamente.");
            }
        }
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