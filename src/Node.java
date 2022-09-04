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

    private List<Node> shortestPath = new LinkedList<>();

    public String getName() {
        return airport.getIata();
    }

    public List<Node> getShortestPath() {
        return shortestPath;
    }

    public void setShortestPath(List<Node> shortestPath) {
        this.shortestPath = shortestPath;
    }

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

    Map<Node, Double> adjacentNodes = new HashMap<>();

    public void addDestination(Node destination) {
        double distance = this.getAirport().distanceTo(destination.getAirport());
        adjacentNodes.put(destination, distance);
    }

    public Node(Airport airport) {
        this.airport = airport;
    }


}