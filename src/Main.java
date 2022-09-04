import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class Main {
    public static void main(String[] args) {
        String sourceAirport = "CFB";
        String destinyAirport = "MCP";
        Route routeSearch = new Route(sourceAirport,destinyAirport);

        HashMap<String, Node> nodes = new HashMap<>();
        Graph graph = new Graph();
        RouteDAO routeDAO = new RouteDAO();
        AirportDAO airportDAO = new AirportDAO();
        ConsultDAO consultDAO = new ConsultDAO();
        ConsultResultsDAO consultResultsDAO = new ConsultResultsDAO();
        HashMap<String,Airport> airportsList = airportDAO.getAirports();

        for(Map.Entry<String,Airport> airport : airportsList.entrySet()) {
            nodes.put(airport.getKey(), new Node(airport.getValue()));
        }

        for(Map.Entry<String,Node> source : nodes.entrySet()){
            ArrayList<String> routes = routeDAO.getRoutes(source.getKey());
            for(String route : routes){
                Node destiny = nodes.get(route);

                if((source.getKey().equals(sourceAirport) && destiny.getAirport().getIata().equals(destinyAirport)) || (source.getKey().equals(destinyAirport) && destiny.getAirport().getIata().equals(sourceAirport))){
                    continue;
                }
                source.getValue().addDestination(destiny);
            }
            graph.addNode(source.getValue());
        }

        graph = Dijkstra.calculateShortestPathFromSource(graph, nodes.get(sourceAirport));
        //ARMAZENAMENTO DA CONSULTA NO BANCO DE DADOS
        consultDAO.save(routeSearch);
        System.out.println("ID DA ULTIMA CONSULTA: " + consultDAO.getConsultID());
        System.out.println("***Shortest Path Between Two Airports***");
        if(graph != null) {
            System.out.println("Source Airport: " + nodes.get(sourceAirport).getAirport().getNome());
            System.out.println("Destiny Airport: " + nodes.get(destinyAirport).getAirport().getNome());
            System.out.println("Shortest Path:");
            for (Node node : graph.getNodes()) {
                if (node.getAirport().getIata().equals(destinyAirport)) {
                   // System.out.println(node.getName() + " " + node.getDistance());
                    for (Node node1 : node.getShortestPath()) {
                        System.out.print(node1.getName() + " -> ");
                    }
                    //ARMAZENAMENTO DO RESULTADO NO BANCO DE DADOS
                    consultResultsDAO.save(node.getShortestPath());
                }
            }
            System.out.print(" " + nodes.get(destinyAirport).getAirport().getIata());
            Set<Node> consultResultNodes = graph.getNodes();

        }else{
            System.out.println("Unknown Path");
        }

    }

}
