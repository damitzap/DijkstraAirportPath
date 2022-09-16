//Classe que representa os nós no grafo, o qual eh utilizado no algoritmo de Dijkstra
//Cada Node eh considerado um aeroporto
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
//Cada Node armazenará um aeroporto,
//Lista para armazenar do nós com o menor caminho
//distancia do nó a origem
//Lista de nós adjacentes

public class Node {
    //Atributos
    private Airport airport;

    //O atributo shortestPath, é uma lista de nós que guarda o caminho mais curto calculado a partir do nó inicial.
    private List<Node> shortestPath = new LinkedList<>();

    //O atributo adjacentNodes,uma lista de adjacências para o algoritmo de Dijkstra,
    // é usado para associar vizinhos imediatos com comprimento de aresta.
    Map<Node, Double> adjacentNodes = new HashMap<>();

    //Por padrão, todos as distancias dos nós são iniciadas com um valor Double.Max_Value para simular uma distancia infinita
    private Double distance = Double.MAX_VALUE;

    //Metodos
    public Node(Airport airport) {
        this.airport = airport;
    }
    public Airport getAirport() {
        return airport;
    }
    public void setAirport(Airport airport) {
        this.airport = airport;
    }
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