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
            int id = login(conn);
            if (id > -1) {
                exibirMenu(conn, id);
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


    public static void exibirMenu(Connection conn, int id) {
        Personagem personagem = new Personagem("Herói", 10, 5, 3);

        while (true) {
            String[] opcoes = {"Jogar", "Consultar Log", "Mostrar Rank", "Sair"};
            int escolha = JOptionPane.showOptionDialog(null, "Menu de Opções", "Menu",
                    JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, opcoes, opcoes[0]);

            switch (escolha) {
                case 0:
                    jogar(personagem, conn, id);
                    break;
                case 1:
                    consultarLog(conn);
                    break;
                case 2:
                    consultarRanking(conn);
                    break;
                case 3:
                    JOptionPane.showMessageDialog(null, "Saindo...Até mais!");
                    return;
                default:
                    JOptionPane.showMessageDialog(null, "Opção inválida. Tente novamente.");
            }
        }
    }


    public static int login(Connection conn) {
        String nome_usuario = JOptionPane.showInputDialog("Digite o nome de usuário:");
        String senha_usuario = JOptionPane.showInputDialog("Digite a senha:");

        String sql = "SELECT id_usuario FROM tb_usuario WHERE nome_usuario = ? AND senha_usuario = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, nome_usuario);
            ps.setString(2, senha_usuario);

        try (ResultSet rs = ps.executeQuery()) { //ResultSet vai armazenar o que foi encontrado na busca com o ps que a gente fez
            if (rs.next()) { // rs.next verifica se o rs trouxe pelo menos uma linha da tabela de resultado
                int id = rs.getInt("id_usuario");
                JOptionPane.showMessageDialog(null, "Login bem-sucedido!");
                return id;
            }
        }
    }
    catch (SQLException e) {
        e.printStackTrace();
    }

    return -1;
}


    public static void jogar(Personagem personagem, Connection conn, int id) {
        Random random = new Random();
        int pontos_da_jogada = 0;
        String[] atividades = {"comer", "dormir", "caçar"};

        StringBuilder atividadesRealizadas = new StringBuilder("Atividades realizadas:\n");

        for (int i = 0; i < 10; i++) {
            int opcao = random.nextInt(atividades.length);
            String atividade = atividades[opcao];
            switch(opcao){
                case 0:
                    personagem.comer(conn);
                    break;
                case 1:
                    personagem.dormir(conn);
                    pontos_da_jogada += -1;
                    break;
                case 2:
                    personagem.cacar(conn);
                    pontos_da_jogada += 2;
                    break;
            }
            personagem.logAtividade(atividade, conn, id);
            atividadesRealizadas.append(atividade).append("\n");
        }
        inserirRanking(conn, pontos_da_jogada, id);

        JOptionPane.showMessageDialog(null, atividadesRealizadas.toString());
    }

    
    public static void consultarLog(Connection conn) {
        String sql = "SELECT ta.*, tu.nome_usuario FROM tb_atividade AS ta JOIN tb_usuario AS tu ON tu.id_usuario = ta.fk_id_usuario ORDER BY cod_atividade DESC";
        StringBuilder logAtividades = new StringBuilder("Log de atividades:\n");

        try (PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                int id = rs.getInt("cod_atividade");
                String descricao = rs.getString("descricao");
                String dataDeOcorrencia = rs.getString("data_de_ocorrencia");
                String nomeUsuario = rs.getString("nome_usuario");

                logAtividades.append(String.format("ID: %d, Descrição: %s, Data de Ocorrência: %s, Nome: %s%n", id, descricao, dataDeOcorrencia, nomeUsuario));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        JOptionPane.showMessageDialog(null, logAtividades.toString());
    }

    public static void inserirRanking(Connection conn, int pontuacao, int id) {

        String sql = "INSERT INTO tb_ranking (valor_pontuacao, fk_id_usuario) VALUES (?,?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, pontuacao);
            stmt.setInt(2, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void consultarRanking(Connection conn) {
        String sql = "SELECT tr.*, tu.nome_usuario FROM tb_ranking AS tr JOIN tb_usuario AS tu ON tu.id_usuario = tr.fk_id_usuario ORDER BY tr.valor_pontuacao DESC, tr.data_de_ocorrencia ASC";
        StringBuilder ranking = new StringBuilder("Ranking:\n");

        try (PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                int pts = rs.getInt("valor_pontuacao"); 
                String dataDeOcorrencia = rs.getString("data_de_ocorrencia");
                String nomeUsuario = rs.getString("nome_usuario");

                ranking.append(String.format("Pts.: %d,Data de Ocorrência: %s, Nome: %s%n", pts, dataDeOcorrencia, nomeUsuario));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        JOptionPane.showMessageDialog(null, ranking.toString());
    }

}