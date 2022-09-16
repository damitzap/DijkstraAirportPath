import java.sql.*;
import java.util.HashMap;

public class AirportDAO {
    //Metodo para obter todos os aeroportos armazenados no Banco de Dados
    //Os aeroportos sao armazenados em um HashMap cuja chave é o IATA
    public HashMap<String,Airport> getAirports(){
        String sql = "SELECT * FROM  airports";
        //criacao de list para armazenar os dados dos aeroportos
        HashMap<String,Airport> airportsList = new HashMap<>();
        Connection connection = null;
        PreparedStatement pstm = null;
        //Classe que ira recuperar os dados do Banco de Dados ***SELECT****
        ResultSet rset = null;
        try{
            connection = ConnectionModule.connector();
            pstm = (PreparedStatement) connection.prepareStatement(sql);
            rset = pstm.executeQuery();
            //Percorrer os dados do Banco
            while (rset.next()){
                Airport airport = new Airport();
                //Recuperar o iata
                airport.setIata(rset.getString("iata"));
                //Recuperar o Nome
                airport.setNome(rset.getString("Nome"));
                //Recuperar o Estado
                airport.setState(rset.getString("state"));
                //Recuperar a Latitude
                airport.setLatitude(rset.getDouble("latitude"));
                //Recuperar a Longitude
                airport.setLongitude(rset.getDouble("longitude"));
                //Recuperar a Cidade
                airport.setCity(rset.getString("city"));
                //Inserir dados no HashMap - Chave IATE, Aeroporto
                airportsList.put(airport.getIata(), airport);
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
        return airportsList;
    }
    //metodo para obter um aeroporto especifico atraves do IATA
    public Airport getAirport(String key){
        String sql = "SELECT * FROM airportlist.airports AS A WHERE A.iata=?";
        Connection connection = null;
        PreparedStatement pstm = null;
        //Classe que ira recuperar os dados do Banco de Dados ***SELECT****
        ResultSet rset = null;
        Airport airport = null;
        try{
            connection = ConnectionModule.connector();
            pstm = (PreparedStatement) connection.prepareStatement(sql);
            pstm.setString(1,key);
            rset = pstm.executeQuery();
            //Percorrer os dados do Banco
            while (rset.next()){
                airport = new Airport();
                //Recuperar o iata
                airport.setIata(rset.getString("iata"));
                //Recuperar o Nome
                airport.setNome(rset.getString("Nome"));
                //Recuperar o Local
                airport.setState(rset.getString("state"));
                //Recuperar a Latitude
                airport.setLatitude(rset.getDouble("latitude"));
                //Recuperar a Longitude
                airport.setLongitude(rset.getDouble("longitude"));
                //Recuperar a Cidade
                airport.setCity(rset.getString("city"));

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
        return airport;
    }
    //Metodo para salvar um novo aeroporto no banco de dados
    public void save(Airport airport){
        String sql = "INSERT INTO airports(iata, Nome, state, latitude, longitude, city) VALUES (?, ?, ?, ?, ?, ?)";
        Connection connection = null;
        PreparedStatement pstm = null;
        try{
            //Criar conexao com o Banco de Dados
            connection = ConnectionModule.connector();
            //Criar um PreparedStatemente para executar uma query;
            pstm = (PreparedStatement) connection.prepareStatement(sql);
            //Adicao de valores que sao esperados pela query
            pstm.setString(1,airport.getIata());
            pstm.setString(2,airport.getNome());
            pstm.setString(3,airport.getState());
            pstm.setDouble(4,airport.getLatitude());
            pstm.setDouble(5,airport.getLongitude());
            pstm.setString(6,airport.getCity());
            //Execucao da query
            pstm.execute();
            System.out.println("Aeroporto Salvo com Sucesso");
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
