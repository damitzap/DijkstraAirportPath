import java.sql.*;

public class ConnectionModule {
    //Metodo para estabelecer a conexao com o MySql
    public static Connection connector() {
        java.sql.Connection connection = null;
        //driver de conexão do java com o mysql
        String driver = "com.mysql.cj.jdbc.Driver";
        // Informações referentes ao banco de dados
        String url = "jdbc:mysql://localhost:3306/airportlist";
        String user = "root";
        String password = "123456";
        //Estabelecimento da Conexão com o Banco de Dados
        try {
            //Metodo para fazer com que a classe seja carregada pela JVM
            Class.forName(driver);
            //Criacao da conexao com o banco de dados
            connection = DriverManager.getConnection(url, user, password);
            return connection;
        } catch (Exception e) {
            //Impressão do erro
            System.out.println(e);
            return null;
        }
    }
}