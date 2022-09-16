import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.*;

import java.io.FileReader;
import java.io.IOException;
import java.util.Iterator;
//Classe utilizada para obter dados do arquivo JSON (routes.json) com as rotas realizadas por cada aeroporto
//Nesse caso também foi realizada a complementação da tabela aeroportos, caso algum aeroporto fornecido pelo arquivo de rotas
//não estivesse cadastrado no banco de dados de aeroportos
public class JSONImport {
    public static void main(String[] args) throws IOException, ParseException {
        // parsing file "JSONExample.json"
        Object ob = new JSONParser().parse(new FileReader("C:\\Users\\alexd\\Desktop\\minPath\\djistraTEste\\src\\routes.json"));
        // typecasting ob to JSONObject
        JSONObject js = (JSONObject) ob;

        Route route = new Route();
        //Classes DAO para interface com o banco de dados
        RouteDAO routeDAO = new RouteDAO();
        AirportDAO airportDAO = new AirportDAO();
        Airport airport = null;
        for(Iterator<Object> i = js.keySet().iterator(); i.hasNext() ;){
            //Leitura dos dados do arquivo JSON
            String key = i.next().toString();
            JSONObject jsData = (JSONObject) js.get(key);
            JSONArray jsRoute = (JSONArray) jsData.get("rotas");
            airport = airportDAO.getAirport(key);
            //Armazenamento do Aeroporto
            if(airport == null){
                airport = new Airport();
                airport.setIata(key);
                airport.setNome((String) jsData.get("nome"));
                airport.setState("x");
                airport.setLatitude((Double) jsData.get("latitude"));
                airport.setLongitude((Double) jsData.get("longitude"));
                airport.setCity("x");
                airportDAO.save(airport);
            }
            //Armazenamento das Rotas
            for(Object a: jsRoute){
                route.setIataOrigem(key);
                route.setIataDestino(a.toString());
                routeDAO.save(route);
            }

        }
    }
}
