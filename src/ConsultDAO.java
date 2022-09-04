import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class ConsultDAO {
    public void save(Route route){
        String sql = "INSERT INTO consult(iata_origem, iata_destino) VALUES (?, ?)";
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
//Returns ID from consult table
    public int getConsultID(){
        String sql = "SELECT MAX(id) FROM airportlist.consult";
        Connection connection = null;
        PreparedStatement pstm = null;
        //Classe que ira recuperar os dados do Banco de Dados ***SELECT****
        ResultSet rset = null;
        int id = 0;
        try{
            connection = ConnectionModule.connector();

            pstm = (PreparedStatement) connection.prepareStatement(sql);
            rset = pstm.executeQuery();
            //Percorrer os dados do Banco
            while (rset.next()){
                //Recuperar o ID
                id = rset.getInt("MAX(id)");
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
        return id;
    }
}
