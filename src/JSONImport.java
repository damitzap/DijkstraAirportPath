import com.mysql.cj.xdevapi.JsonString;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.*;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.Iterator;

public class JSONImport {

    public static void main(String[] args) throws IOException, ParseException {
        // parsing file "JSONExample.json"
        Object ob = new JSONParser().parse(new FileReader("C:\\Users\\alexd\\Desktop\\minPath\\djistraTEste\\src\\routes.json"));

        // typecasting ob to JSONObject
        JSONObject js = (JSONObject) ob;

        Route route = new Route();
        RouteDAO routeDAO = new RouteDAO();
        AirportDAO airportDAO = new AirportDAO();
        Airport airport = null;
        for(Iterator<Object> i = js.keySet().iterator(); i.hasNext() ;){

            String key = i.next().toString();

            JSONObject jsData = (JSONObject) js.get(key);
            JSONArray jsRoute = (JSONArray) jsData.get("rotas");

            airport = airportDAO.getAirport(key);

            if(airport == null){
                airport = new Airport();
                airport.setIata(key);
                airport.setNome((String) jsData.get("nome"));
                airport.setLocal("x");
                airport.setLatitude((Double) jsData.get("latitude"));
                airport.setLongitude((Double) jsData.get("longitude"));
                airportDAO.save(airport);
            }


            for(Object a: jsRoute){
                route.setIataOrigem(key);
                route.setIataDestino(a.toString());
                routeDAO.save(route);
            }

        }


    }
}
