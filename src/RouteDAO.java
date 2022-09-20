import java.sql.*;
import java.util.ArrayList;
//Classe que armazena e consulta as rotas permitidas para cada aeroporto no Banco de Dados
public class RouteDAO {
    //O Metodo getRoutes é utilizado para obter a lista de rotas que um aeroporto possui
    //O Metodo recebe como parâmetro o identificador IATA de 3 letras do aeroporto do qual se quer obter as rotas e retorno a Lista de Rotas
    public ArrayList<String> getRoutes(String key){
        String sql = "SELECT * FROM airportlist.routes AS R WHERE R.iata_origem=?";
        Connection connection = null;
        PreparedStatement pstm = null;
        //Classe que ira recuperar os dados do Banco de Dados
        ResultSet rset = null;
        Airport airport = null;
        //Criação de um ArrayList para armazenar as rotas do aeroporto
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

    //O Método save é utilizado para salvar as rotas de cada aeroporto no banco de dados e recebe como parâmetro um objeto route
    //Esse Método é empregado para popular o BD a partir de arquivo JSON com as rotas de cada aeroporto.
    public void save(Route route){
        String sql = "INSERT INTO routes(iata_origem, iata_destino) VALUES (?, ?)";
        Connection connection = null;
        PreparedStatement pstm = null;
        try{
            //Criar conexao com o Banco de Dados
            connection = ConnectionModule.connector();
            //Criar um PreparedStatement para executar uma query;
            pstm = (PreparedStatement) connection.prepareStatement(sql);
            //Adicao de valores que sao esperados pela query
            pstm.setString(1,route.getIataOrigem());
            pstm.setString(2, route.getIataDestino());
            //Execucao da query
            pstm.execute();
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
}