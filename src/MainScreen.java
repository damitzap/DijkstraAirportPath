import javax.swing.*;
import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.*;

public class MainScreen extends JFrame {
    private JLabel sourceAirpot;
    private JComboBox cbOrigin;
    private JLabel destinyAirport;
    private JComboBox cbDestiny;
    private JButton btnClear;
    private JButton btnCalculate;
    private JPanel mainPanel;
    private JTextArea lbResult;
    private JComboBox cbState;
    private JComboBox cbCity;
    private JLabel lbIataSource;
    private JComboBox cbStateDestiny;
    private JComboBox cbCityDestiny;
    private JLabel lbIataDestiny;

    public MainScreen(){
        setContentPane(mainPanel);
        setTitle("Shortest Path Between Two Airports");
        setSize(600,400);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setVisible(true);


        //Mostrar informações comboBox selecao de estado
        cbState.addAncestorListener(new AncestorListener() {
            public void ancestorAdded(AncestorEvent event) {
                HashMap<String,Airport> airportsList = new AirportDAO().getAirports();
                HashSet<String> stateSet = new HashSet<>();
                for(Airport a : airportsList.values()){
                    stateSet.add(a.getState());
                }

                for(String a : stateSet){
                    cbState.addItem(a);
                }
            }
            public void ancestorRemoved(AncestorEvent event) {}
            public void ancestorMoved(AncestorEvent event) {}
        });

        //Mostrar cidades de acordo com o estado selecionado
        cbState.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                HashMap<String, Airport> airportsList = new AirportDAO().getAirports();
                HashSet<String> citySet = new HashSet<>();
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    cbStateDestiny.removeItem(cbState.getSelectedItem());
                    cbCity.removeAllItems();
                    citySet.clear();
                    for (Airport a : airportsList.values()) {
                        if (a.getState().equals(cbState.getSelectedItem())) {
                            citySet.add(a.getCity());
                        }
                    }
                    for(String city : citySet){
                        cbCity.addItem(city);
                    }
                }
            }
        });

        //Mostrar Aeroporto conforme cidade escolhida
        cbCity.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                HashMap<String, Airport> airportsList = new AirportDAO().getAirports();
                if(e.getStateChange() == ItemEvent.SELECTED){
                    cbOrigin.removeAllItems();
                    for(Airport ap : airportsList.values()){
                        if(ap.getCity().equals(cbCity.getSelectedItem())){
                            cbOrigin.addItem(ap.getIata());
                            lbIataSource.setText(ap.getNome());
                        }
                    }
                }
            }
        });

        //PARAMETROS PARA Aeroporto DESTINO
        cbStateDestiny.addAncestorListener(new AncestorListener() {
            public void ancestorAdded(AncestorEvent event) {
                HashMap<String,Airport> airportsList = new AirportDAO().getAirports();
                HashSet<String> stateSet = new HashSet<>();
                for(Airport a : airportsList.values()){
                    if(!a.getState().equals(cbState.getSelectedItem())) {
                        stateSet.add(a.getState());
                    }
                }
                for(String a : stateSet){
                    cbStateDestiny.addItem(a);
                }
            }
            public void ancestorRemoved(AncestorEvent event) {}
            public void ancestorMoved(AncestorEvent event) {}
        });

        //Mostrar cidades de acordo com o estado selecionado
        cbStateDestiny.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                HashMap<String, Airport> airportsList = new AirportDAO().getAirports();
                HashSet<String> citySet = new HashSet<>();

                if (e.getStateChange() == ItemEvent.SELECTED) {
                    cbStateDestiny.removeItem(cbState.getSelectedItem());
                    cbCityDestiny.removeAllItems();
                    citySet.clear();
                    for (Airport a : airportsList.values()) {
                        if (a.getState().equals(cbStateDestiny.getSelectedItem())) {
                            citySet.add(a.getCity());
                        }
                    }
                    for(String city : citySet){
                        cbCityDestiny.addItem(city);
                    }
                }
            }
        });

        //Mostrar Aeroporto conforme cidade escolhida
        cbCityDestiny.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                HashMap<String, Airport> airportsList = new AirportDAO().getAirports();
                if(e.getStateChange() == ItemEvent.SELECTED){
                    cbDestiny.removeAllItems();
                    for(Airport ap : airportsList.values()){
                        if(ap.getCity().equals(cbCityDestiny.getSelectedItem())){
                            cbDestiny.addItem(ap.getIata());
                            lbIataDestiny.setText(ap.getNome());
                        }
                    }
                }
            }
        });
//        cbDestiny.addAncestorListener(new AncestorListener() {
//            @Override
//            public void ancestorAdded(AncestorEvent event) {
//                AirportDAO airportDAO = new AirportDAO();
//                HashMap<String,Airport> airportsList = airportDAO.getAirports();
//                cbDestiny.removeAll();
//                for (Map.Entry<String,Airport> entry : airportsList.entrySet()){
//                    cbDestiny.addItem(entry.getValue().getIata());
//                }
//            }
//            @Override
//            public void ancestorRemoved(AncestorEvent event) {}
//            @Override
//            public void ancestorMoved(AncestorEvent event) {}
//        });

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
                if((graph.getNodes() != null) && !(sourceAirport.equals(destinyAirport))) {
                    lbResult.append("Source Airport: " + nodes.get(sourceAirport).getAirport().getNome() + " - " + nodes.get(sourceAirport).getAirport().getIata()+ "\n");
                    for(Node a : graph.getNodes()){
                        if(a.getAirport().getIata().equals(destinyAirport)){
                            for(Node b : a.getShortestPath()){
                                if(b.getAirport().getIata().equals(sourceAirport)){continue;}
                                lbResult.append("Scale: "+ b.getAirport().getNome() + " - " + b.getAirport().getIata() + "\n");
                            }
                        }

                    }
                    lbResult.append("Destiny Airport: " + nodes.get(destinyAirport).getAirport().getNome() + " - " + nodes.get(destinyAirport).getAirport().getIata() + "\n");
                    lbResult.append("\nShortest Path: ");
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
                    lbResult.setText("Unknown Path");
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
