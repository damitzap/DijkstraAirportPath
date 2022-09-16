import java.util.HashSet;
import java.util.Set;
//Classe que representa um grafo como um conjunto de nós
public class Graph {
    private Set<Node> nodes = new HashSet<>();
    public void addNode(Node nodeA) {
        nodes.add(nodeA);
    }
    public Set<Node> getNodes() {
        return nodes;
    }
    public void setNodes(Set<Node> nodes) {
        this.nodes = nodes;
    }
}