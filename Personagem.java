import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Random;
import lombok.Setter;
import lombok.Getter;

public class Personagem{
  //variáveis de instância(objeto)
  @Getter @Setter private String nome;
  @Getter @Setter private int energia;
  @Getter @Setter private int fome;
  @Getter @Setter private int sono;

  //esse é o construtor padrão
  //criado automaticamente pelo compilador, ainda que não seja escrito explicitamente
  Personagem(){
    nome = null;
    energia = 10;
    fome = 0;
    sono = 0;
  }

  //construtor personalizado
  //o que viabiliza a sua existência é a sobrecarga de construtores
  Personagem(String nome, int energia, int fome, int sono){
    if (energia >= 0 && energia <= 10)
      this.energia = energia;
    if (fome >= 0 && fome <= 10)
      this.fome = fome;
    if (sono >= 0 && sono <= 10)
      this.sono = sono;
    this.nome = nome;
  }

public void realizarAtividade(String atividade, Connection conn) {
  logAtividade(atividade, conn);
}

private void logAtividade(String atividade, Connection conn) {
  String sql = "INSERT INTO tb_atividade (descricao) VALUES (?)";
   try (PreparedStatement stmt = conn.prepareStatement(sql)) {
       stmt.setString(1, atividade);
       stmt.executeUpdate();
   } catch (SQLException e) {
       e.printStackTrace();
   }
}


  void cacar(Connection conn){
    if(energia >= 2){
      System.out.printf("%s esta cacando...\n", nome);
      energia -= 2; // energia = energia - 2;
      realizarAtividade("caçar", conn);
    }
    else{
      System.out.printf("%s sem energia para cacar...\n", nome);
    }
    fome = Math.min(fome + 1, 10);
    //resolver com o ternário
    sono = sono < 10 ? sono + 1 : sono;
    
  }

  void comer(Connection conn) {
    //se tiver fome
      //comer e reduzir o valor de fome de 1
      //aumentar o valor de energia de 1
    //caso contrario
      //so vai avisar que esta sem fome
      switch(fome){
        case 0:
          System.out.printf("%s sem fome....\n", nome);
          break;
        default:
          System.out.printf("%s comendo...\n", nome);
          --fome;
          energia = (energia == 10 ? energia : energia + 1);
          realizarAtividade("comer", conn);
      }
  }

  void dormir(Connection conn){
    if(sono >= 1){
      System.out.printf("%s esta dormindo...\n", nome);
      sono -= 1;
      energia = Math.min(energia + 1, 10);
      realizarAtividade("dormir", conn);
    }
    else{
      System.out.printf("%s sem sono...\n", nome);
    }
  }

  public String toString(){
    return String.format(
      "%s: (e:%d, f:%d, s:%d)",
      nome, energia, fome, sono
    );
  }
}