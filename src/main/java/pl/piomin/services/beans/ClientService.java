package pl.piomin.services.beans;

public class ClientService {

    private String url;

    public ClientService(String url) {
        this.url = url;
    }

    public String connect() {
        System.out.println("Connected: " + url);
        return url;
    }
}
