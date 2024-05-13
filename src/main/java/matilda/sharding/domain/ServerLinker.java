//package matilda.sharding.domain;
//
//import jakarta.persistence.*;
//
//@Entity(name = "server_linkers")
//public class ServerLinker {
//
//    @Id @GeneratedValue
//    private Long id;
//
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "server_ip")
//    private Server server;
//
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "linker_id")
//    private Linker linker;
//
//    protected ServerLinker() {
//    }
//
//    public ServerLinker(Server server, Linker linker) {
//        this.server = server;
//        this.linker = linker;
//    }
//}
