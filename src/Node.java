//Classe que representa os nós no grafo, o qual eh utilizado no algoritmo de Dijkstra
//Cada Node eh considerado um aeroporto
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class Node {
    private Airport airport;

    public Airport getAirport() {
        return airport;
    }

    public void setAirport(Airport airport) {
        this.airport = airport;
    }


    public String getName() {
        return airport.getIata();
    }

    private List<Node> shortestPath = new LinkedList<>();
    public List<Node> getShortestPath() {
        return shortestPath;
    }

    public void setShortestPath(List<Node> shortestPath) {
        this.shortestPath = shortestPath;
    }

    public Node(Airport airport) {
        this.airport = airport;
    }

    //Nós adjacentes
    Map<Node, Double> adjacentNodes = new HashMap<>();
    public Map<Node, Double> getAdjacentNodes() {
        return adjacentNodes;
    }

    public void setAdjacentNodes(Map<Node, Double> adjacentNodes) {
        this.adjacentNodes = adjacentNodes;
    }

    private Double distance = Double.MAX_VALUE;

    public Double getDistance() {
        return distance;
    }

    public void setDistance(Double distance) {
        this.distance = distance;
    }

    public void addDestination(Node destination) {
        double distance = this.getAirport().distanceTo(destination.getAirport());
        adjacentNodes.put(destination, distance);
    }


}