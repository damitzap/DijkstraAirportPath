import java.sql.*;

public class ConnectionModule {
    //Metodo para estabelecer a conexao com o MySql
    public static Connection connector() {
        java.sql.Connection connection = null;
        //driver to calls db driver
        String driver = "com.mysql.cj.jdbc.Driver";
        // var to store info of db
        String url = "jdbc:mysql://localhost:3306/airportlist";
        String user = "root";
        String password = "123456";
        //establish connection with db
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