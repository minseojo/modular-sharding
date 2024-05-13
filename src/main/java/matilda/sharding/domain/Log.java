//package matilda.sharding.domain;
//
//import jakarta.persistence.*;
//import lombok.Getter;
//
//@Entity(name = "logs")
//@Getter
//public class Log {
//
//    @Id @GeneratedValue
//    private Long logId;
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "linker_id")
//    private Linker linker;
//
//    public Log(Linker linker) {
////        this.logId = logId;
//        this.linker = linker;
//    }
//
//    public Log() {
//
//    }
//}
