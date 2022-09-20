import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;
//Classe que armazena o resultado das consultas dos usuarios no banco de dados
public class ConsultResultsDAO {
    //Metodo save é utilizado para armazenar os aeroportos em que houve parada no menor caminho entre os aeroportos de origem e destino
    //O Metodo recebe como parâmetro uma lista de Nós, que são os aeroportos em que foram feitas escalas a fim de compor o menor caminho do destino até a origem
    public void save(List<Node> shortestPath){
        String sql = "INSERT INTO consult_results (consult_id, airport_iata) VALUES ((SELECT MAX(id) FROM consult), ?);";
        Connection connection = null;
        PreparedStatement pstm = null;
        try{
            //Criar conexao com o Banco de Dados
            connection = ConnectionModule.connector();
            //Percorre a lista de nós do menor caminho e armazena os identificadores de cada aeroporto representado por esses nós na tabela consult_results
            for(int i = 1; i<shortestPath.size(); i++) {
                //Criar um PreparedStatemente para executar uma query;
                pstm = (PreparedStatement) connection.prepareStatement(sql);
                //Adicao de valores que sao esperados pela query
                pstm.setString(1, shortestPath.get(i).getName());
                //Execucao da query
                pstm.execute();
            }
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