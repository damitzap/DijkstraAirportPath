import javax.swing.*;
import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.*;
//Classe que implementa a Interface Gráfica da Aplicação e faz a execução dos cálculos de menor caminho entre dois aeroportos
public class MainScreen extends JFrame {
    private JLabel sourceAirpot;
    private JComboBox cbAirportOrigin;
    private JLabel destinyAirport;
    private JComboBox cbAirportDestiny;
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
        //Definição o painel principal e seus parâmetros
        setContentPane(mainPanel);
        setTitle("Shortest Path Between Two Airports");
        setSize(600,500);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setVisible(true);

        //Mostrar informações comboBox selecao de estado
        cbState.addAncestorListener(new AncestorListener() {
            public void ancestorAdded(AncestorEvent event) {
                HashMap<String,Airport> airportsList = new AirportDAO().getAirports();
                HashSet<String> stateSet = new HashSet<>();
                //Adição dos estados ao menu de seleção do estado de interesse
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

        //Mostrar cidades no menu de seleção de acordo com o estado selecionado previamente
        cbState.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                HashMap<String, Airport> airportsList = new AirportDAO().getAirports();
                HashSet<String> citySet = new HashSet<>();
                //Captura da mudança de estado do menu de seleção de estados
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    cbStateDestiny.removeItem(cbState.getSelectedItem());
                    cbCity.removeAllItems();
                    citySet.clear();
                    //Inclusão das cidades que possuem aeroportos de acordo com o estado selecionado anteriormente
                    for (Airport a : airportsList.values()) {
                        //pego somente os aeroportos que estão no estado selecionado anteriormente
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
                //Captura a mudança de cidade no menu de seleção de cidades
                if(e.getStateChange() == ItemEvent.SELECTED){
                    cbAirportOrigin.removeAllItems();
                    for(Airport ap : airportsList.values()){
                        //Adiciona os aeroportos que ficam na cidade escolhida
                        if(ap.getCity().equals(cbCity.getSelectedItem())){
                            cbAirportOrigin.addItem(ap.getIata());
                            lbIataSource.setText(ap.getNome());
                        }
                    }
                }
            }
        });

        //Parametros para a selecao do aeroporto de destino
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
                    cbAirportDestiny.removeAllItems();
                    for(Airport ap : airportsList.values()){
                        if(ap.getCity().equals(cbCityDestiny.getSelectedItem())){
                            cbAirportDestiny.addItem(ap.getIata());
                            lbIataDestiny.setText(ap.getNome());
                        }
                    }
                }
            }
        });
//
        //Acao realizada apos o usuario apertar o botao para Calcular a menor rota
        btnCalculate.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                //Captura do Aeroporto Selecionado
                String sourceAirport = (String) cbAirportOrigin.getSelectedItem();
                String destinyAirport = (String) cbAirportDestiny.getSelectedItem();

                //Criacao da rota
                Route routeSearch = new Route(sourceAirport,destinyAirport);
                //Criacao do conjunto de nós
                HashMap<String, Node> nodes = new HashMap<>();
                //Criacao do objeto grafo
                Graph graph = new Graph();
                //Criacao do objeto routeDAO para recuperar as rotas do banco de dados
                RouteDAO routeDAO = new RouteDAO();
                //Cricao do objeto consultDAO para armazenar as consultas no BD
                ConsultDAO consultDAO = new ConsultDAO();
                //Criacao do objeto consultResultsDAO para armazenar os resultados das consultas no banco de dados
                ConsultResultsDAO consultResultsDAO = new ConsultResultsDAO();
                //Criacao do objeto airportDAO para consulta dos aeroportos a fim de construir os nós e o grafo
                AirportDAO airportDAO = new AirportDAO();
                //Criação do HashMap com todos os aeroportos armazenados no banco de dados
                HashMap<String,Airport> airportsList = airportDAO.getAirports();

                //Criacao do conjunto de Nos que posteriormente sera usado para construcao do grafo
                //O IATA identificara o objeto aeroporto dentro do Map
                for(Map.Entry<String,Airport> airport : airportsList.entrySet()) {
                    nodes.put(airport.getKey(), new Node(airport.getValue()));
                }

                //Adicionar os nos e arestas ao grafo
                for(Map.Entry<String,Node> source : nodes.entrySet()){
                    //Obtencao das rotas do aeroporto
                    ArrayList<String> routes = routeDAO.getRoutes(source.getKey());
                    //Percorrer cada aeroporto que tem conexao com o aeroporto atual
                    for(String route : routes){
                        //definicao do destino a partir do aeroporto atual, como base na lista de aeroportos que ele tem conexao
                        Node destiny = nodes.get(route);
                        //Condicional para evitar a adição da aresta de caminho direto entre o aeroporto de ORIGEM e o de DESTINO
                        if((source.getKey().equals(sourceAirport) && destiny.getAirport().getIata().equals(destinyAirport)) || (source.getKey().equals(destinyAirport) && destiny.getAirport().getIata().equals(sourceAirport))){
                            continue;
                        }
                        //adicionar uma aresta entre o aeroporto atual e entre o que ele faz conexao
                        source.getValue().addDestination(destiny);
                    }
                    //adicionar o nó com todas as suas arestas ao grafo
                    graph.addNode(source.getValue());
                }
                //Execucao do algoritmo de Dijkstra para a obtencao do menor caminho a partir de uma origem
                //o metodo recebe como parametro o grafo montado anteriormente e o nó de origem (aeroporto de origem)
                //O algoritmo calculara a menor distancia da origem para todos os nós do grafo, em particular usaremos a menor distancia ate o destino
                graph = Dijkstra.calculateShortestPathFromSource(graph, nodes.get(sourceAirport));

                //ARMAZENAMENTO DA CONSULTA NO BANCO DE DADOS
                consultDAO.save(routeSearch);

                lbResult.setText("***Shortest Path Between Two Airports***"+ "\n");

                //Percorrer no grafo o menor caminho até o destino
                if((graph.getNodes() != null) && !(sourceAirport.equals(destinyAirport))) {
                    lbResult.append("Source Airport: " + nodes.get(sourceAirport).getAirport().getNome() + " - " + nodes.get(sourceAirport).getAirport().getIata()+ "\n");
                    //percorrer os nós do grafo
                    for(Node a : graph.getNodes()){
                        //encontra o nó de destino
                        if(a.getAirport().getIata().equals(destinyAirport)){
                            //chamar o metodo de obtencao de menor caminho até a origem
                            for(Node b : a.getShortestPath()){
                                //mostrar na tela os nós que fazem parte do menor caminho
                                if(b.getAirport().getIata().equals(sourceAirport)){continue;}
                                lbResult.append("Scale: "+ b.getAirport().getNome() + " - " + b.getAirport().getIata() + "\n");
                            }
                        }

                    }
                    lbResult.append("Destiny Airport: " + nodes.get(destinyAirport).getAirport().getNome() + " - " + nodes.get(destinyAirport).getAirport().getIata() + "\n");
                    lbResult.append("\nShortest Path: ");
                    //Mostra o caminho com os IATAs dos aeroportos
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
                    //Caso não haja caminho entre os aeroportos
                    lbResult.setText("Unknown Path");
                }
            }
        });

        //Acao realizada pelo botao Clear
        btnClear.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                lbResult.setText("");
            }
        });
    }
    //Metodo que inicializa a interface gráfica
    public static void main(String[] args) {
        MainScreen mainScreen = new MainScreen();
    }
}
