import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class RouteDAO {

    public void save(Route route){
        String sql = "INSERT INTO routes(iata_origem, iata_destino) VALUES (?, ?)";
        Connection connection = null;
        PreparedStatement pstm = null;

        try{
            //Criar conexao com o Banco de Dados
            connection = ConnectionModule.connector();
            //Criar um PreparedStatemente para executar uma query;
            pstm = (PreparedStatement) connection.prepareStatement(sql);
            //Adicao de valores que sao esperados pela query
            pstm.setString(1,route.getIataOrigem());
            pstm.setString(2, route.getIataDestino());

            //Execucao da query
            pstm.execute();

            System.out.println("Rota Salva com Sucesso");
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            //fechar conexoes
            try{
                if(pstm!=null){
                    pstm.close();
                }
                if(connection!=null){
                    connection.close();
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    public ArrayList<String> getRoutes(String key){
        String sql = "SELECT * FROM airportlist.routes AS R WHERE R.iata_origem=?";

        Connection connection = null;
        PreparedStatement pstm = null;
        //Classe que ira recuperar os dados do Banco de Dados ***SELECT****
        ResultSet rset = null;
        Airport airport = null;
        ArrayList<String> routes = new ArrayList<>();
        try{
            connection = ConnectionModule.connector();

            pstm = (PreparedStatement) connection.prepareStatement(sql);
            pstm.setString(1,key);
            rset = pstm.executeQuery();
            //Percorrer os dados do Banco
            while (rset.next()){
                routes.add(rset.getString("iata_destino"));
            }
        }catch (Exception  e){
            e.printStackTrace();
        }finally {
            try{//tratamento para fechar as conexoes
                if (rset != null) {
                    rset.close();
                }

                if (pstm != null) {
                    pstm.close();
                }

                if (connection != null) {
                    connection.close();
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return routes;
    }


}
