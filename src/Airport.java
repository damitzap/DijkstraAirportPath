public class Airport {
    //Atributos do Aeroporto
    private String iata; //Id do aeroporto
    private String nome;
    private String state;
    private double latitude;
    private double longitude;
    private String city;
    //Metodos
    public Airport(){}
    public Airport(String iata, String nome, String state, double latitude, double longitude, String city) {
        this.iata = iata;
        this.nome = nome;
        this.state = state;
        this.latitude = latitude;
        this.longitude = longitude;
        this.city = city;
    }
    public String getIata() {
        return iata;
    }
    public void setIata(String iata) {
        this.iata = iata;
    }
    public String getNome() {
        return nome;
    }
    public void setNome(String nome) {
        this.nome = nome;
    }
    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
    public double getLatitude() {
        return latitude;
    }
    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }
    public double getLongitude() {
        return longitude;
    }
    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
    public String getCity() {
        return city;
    }
    public void setCity(String city) {
        this.city = city;
    }

    //Metodo para calculo da distância entre o aeroporto de origem e destino
    public double distanceTo(Airport destino) {
        double lat1 = Math.toRadians(this.getLatitude());
        double lon1 = Math.toRadians(this.getLongitude());
        double lat2 = Math.toRadians(destino.getLatitude());
        double lon2 = Math.toRadians(destino.getLongitude());
        //comprimento do arco de circunferencia em radianos, usando a formula da lei dos cossenos
        double angle = Math.acos(Math.sin(lat1) * Math.sin(lat2)
                + Math.cos(lat1) * Math.cos(lat2) * Math.cos(lon1 - lon2));
        // Conversao para Km
        double relKm = 1.852 * 60 * Math.toDegrees(angle);
        return relKm;
    }
}
