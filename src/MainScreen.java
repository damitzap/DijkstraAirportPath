import javax.swing.*;
import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainScreen extends JFrame {
    private JLabel sourceAirpot;
    private JComboBox cbOrigin;
    private JLabel destinyAirport;
    private JComboBox cbDestiny;
    private JButton btnClear;
    private JButton btnCalculate;
    private JPanel mainPanel;
    private JTextArea lbResult;

    public MainScreen(){
        setContentPane(mainPanel);
        setTitle("Shortest Path Between Two Airports");
        setSize(450,300);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setVisible(true);



        //Mostrar informacoes nas comboBox de Origem e Destino
        cbOrigin.addAncestorListener(new AncestorListener() {
            @Override
            public void ancestorAdded(AncestorEvent event) {
                AirportDAO airportDAO = new AirportDAO();
                HashMap<String,Airport> airportsList = airportDAO.getAirports();
                cbOrigin.removeAll();
                for (Map.Entry<String,Airport> entry : airportsList.entrySet()){
                    cbOrigin.addItem(entry.getValue().getIata());
                }
            }
            @Override
            public void ancestorRemoved(AncestorEvent event) {}
            @Override
            public void ancestorMoved(AncestorEvent event) {}
        });

        cbDestiny.addAncestorListener(new AncestorListener() {
            @Override
            public void ancestorAdded(AncestorEvent event) {
                AirportDAO airportDAO = new AirportDAO();
                HashMap<String,Airport> airportsList = airportDAO.getAirports();
                cbDestiny.removeAll();
                for (Map.Entry<String,Airport> entry : airportsList.entrySet()){
                    cbDestiny.addItem(entry.getValue().getIata());
                }
            }
            @Override
            public void ancestorRemoved(AncestorEvent event) {}
            @Override
            public void ancestorMoved(AncestorEvent event) {}
        });

        //Acao realizada apos o usuario apertar o botao para Calcular a menor rota
        btnCalculate.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String sourceAirport = (String) cbOrigin.getSelectedItem();
                String destinyAirport = (String) cbDestiny.getSelectedItem();

                Route routeSearch = new Route(sourceAirport,destinyAirport);

                HashMap<String, Node> nodes = new HashMap<>();
                Graph graph = new Graph();
                RouteDAO routeDAO = new RouteDAO();

                ConsultDAO consultDAO = new ConsultDAO();
                ConsultResultsDAO consultResultsDAO = new ConsultResultsDAO();
                AirportDAO airportDAO = new AirportDAO();
                HashMap<String,Airport> airportsList = airportDAO.getAirports();


                //Criacao do conjunto de Nos
                for(Map.Entry<String,Airport> airport : airportsList.entrySet()) {
                    nodes.put(airport.getKey(), new Node(airport.getValue()));
                }

                //Adicionar os nos e arestas ao grafo
                for(Map.Entry<String,Node> source : nodes.entrySet()){
                    ArrayList<String> routes = routeDAO.getRoutes(source.getKey());
                    for(String route : routes){
                        Node destiny = nodes.get(route);

                        //remocao do caminho direto entre o aeroporto de origem e de destino
                        if((source.getKey().equals(sourceAirport) && destiny.getAirport().getIata().equals(destinyAirport)) || (source.getKey().equals(destinyAirport) && destiny.getAirport().getIata().equals(sourceAirport))){
                            continue;
                        }
                        source.getValue().addDestination(destiny);
                    }
                    graph.addNode(source.getValue());
                }
                //Execucao do algoritmo de Dijkstra para a obtencao do menor caminho a partir de uma origem
                graph = Dijkstra.calculateShortestPathFromSource(graph, nodes.get(sourceAirport));

                //ARMAZENAMENTO DA CONSULTA NO BANCO DE DADOS
                consultDAO.save(routeSearch);

                lbResult.setText("***Shortest Path Between Two Airports***"+ "\n");
                if(graph.getNodes() != null) {
                    lbResult.append("Source Airport: " + nodes.get(sourceAirport).getAirport().getNome() + "\n");
                    lbResult.append("Destiny Airport: " + nodes.get(destinyAirport).getAirport().getNome()+ "\n");
                    lbResult.append("Shortest Path: ");
                    for (Node node : graph.getNodes()) {
                        if (node.getAirport().getIata().equals(destinyAirport)) {
                            for (Node node1 : node.getShortestPath()) {
                                lbResult.append(node1.getName() + " -> ");
                            }
                            //ARMAZENAMENTO DO RESULTADO NO BANCO DE DADOS
                            consultResultsDAO.save(node.getShortestPath());
                        }
                    }
                    lbResult.append(" " + nodes.get(destinyAirport).getAirport().getIata());

                }else{
                    lbResult.append("Unknown Path");
                }

            }
        });

        //Acao realizada pelo botao Clear
        btnClear.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                lbResult.setText("");
            }
        });
    }

    public static void main(String[] args) {
        MainScreen mainScreen = new MainScreen();

    }
}
