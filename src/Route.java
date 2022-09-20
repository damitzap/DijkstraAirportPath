public class Route {
    private String iataOrigem;
    private String iataDestino;

    public Route() {}
    public Route(String iataOrigem, String iataDestino) {
        this.iataOrigem = iataOrigem;
        this.iataDestino = iataDestino;
    }
    public String getIataOrigem() {
        return iataOrigem;
    }
    public void setIataOrigem(String iataOrigem) {
        this.iataOrigem = iataOrigem;
    }
    public String getIataDestino() {
        return iataDestino;
    }
    public void setIataDestino(String iataDestino) {
        this.iataDestino = iataDestino;
    }
}