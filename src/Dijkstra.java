import java.util.*;

public class Dijkstra {

    //Metodo que calcula o menor caminho da origem, recebe como parâmetro o nó de origem e o grafo com todos os nós
    public static Graph calculateShortestPathFromSource(Graph graph, Node source) {

        //A distancia do nó de origem para ele mesmo é setada como 0
        source.setDistance(0.0);

        //Criação do conjunto de nós os quais já sabemos a distancia minima da origem - settledNodes
        Set<Node> settledNodes = new HashSet<>();

        //Criação do conjunto de nós que podemos chegar a partir da origem,
        // mas ainda não sabemos a distancia mínima - unsettledNodes
        Set<Node> unsettledNodes = new HashSet<>();

        //Adicão do nó de origem ao conjunto de nós unsettled
        unsettledNodes.add(source);

        while (unsettledNodes.size() != 0) {
            //
            Node currentNode =  getLowestDistanceNode(unsettledNodes);
            unsettledNodes.remove(currentNode);
            for (Map.Entry < Node, Double> adjacencyPair: currentNode.getAdjacentNodes().entrySet()) {
                Node adjacentNode = adjacencyPair.getKey();
                Double edgeWeight = adjacencyPair.getValue();
                if (!settledNodes.contains(adjacentNode)) {
                    calculateMinimumDistance(adjacentNode, edgeWeight, currentNode);
                    unsettledNodes.add(adjacentNode);
                }
            }
            settledNodes.add(currentNode);
        }
        return graph;
    }
    //O método getLowestDistanceNode(), retorna o nó com a menor distância do conjunto unsettledNodes
    //Recebe como parâmetro o conjunto unsettledNodes
    private static Node getLowestDistanceNode(Set < Node > unsettledNodes) {
        Node lowestDistanceNode = null;
        double lowestDistance = Double.MAX_VALUE;
        for (Node node: unsettledNodes) {
            Double nodeDistance = node.getDistance(); //distancia total atual
            if (nodeDistance < lowestDistance) {
                lowestDistance = nodeDistance;
                lowestDistanceNode = node;
            }
        }
        return lowestDistanceNode;
    }
    //método calculateMinimumDistance() compara a distância real com a recém calculada enquanto segue o caminho recém explorado
    //recebe como parâmetro o nó que está sendo o no atual(sourceNode), o no de destino(evalutaionNode) e a distancia entre os dois(edgeWeigh).
    private static void calculateMinimumDistance(Node evaluationNode, Double edgeWeigh, Node sourceNode) {
        double sourceDistance = sourceNode.getDistance();
        if (sourceDistance + edgeWeigh < evaluationNode.getDistance()) {
            evaluationNode.setDistance(sourceDistance + edgeWeigh);
            LinkedList<Node> shortestPath = new LinkedList<>(sourceNode.getShortestPath());
            shortestPath.add(sourceNode);
            evaluationNode.setShortestPath(shortestPath);
        }
    }

}
