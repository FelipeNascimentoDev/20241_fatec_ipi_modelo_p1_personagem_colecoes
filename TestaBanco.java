import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class TestaBanco {
    public static void main(String[] args) {
        
        String url = "jdbc:mysql://avnadmin:AVNS_AdAQnSuhut3F_ocHGND@mysql-3b3fa6b-felipenn-1st-project.c.aivencloud.com:28215/defaultdb?ssl-mode=REQUIRED";
        String user = "avnadmin";
        String password = "AVNS_AdAQnSuhut3F_ocHGND";

        try (Connection conn = DriverManager.getConnection(url, user, password)) {
            
            Personagem personagem = new Personagem();

            personagem.realizarAtividade("comer", conn);
            personagem.realizarAtividade("dormir", conn);
            personagem.realizarAtividade("caçar", conn);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}