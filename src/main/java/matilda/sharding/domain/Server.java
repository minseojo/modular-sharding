package matilda.sharding.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;

@Entity(name = "servers")
@Getter
public class Server {
    @Id
    private String serverIp;
    private String cpu;
    private String volume;
    boolean isRunning;

    public Server(String serverIp, String cpu, String volume, boolean isRunning) {
        this.serverIp = serverIp;
        this.cpu = cpu;
        this.volume = volume;
        this.isRunning = isRunning;
    }

    protected Server() {

    }
}
