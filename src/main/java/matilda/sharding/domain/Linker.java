//package matilda.sharding.domain;
//
//import jakarta.persistence.*;
//import lombok.Getter;
//
//@Entity(name = "linkers")
//@Getter
//public class Linker {
//    @Id
//    @Column(name = "linker_id")
//    private Long linkerId;
//    @ManyToOne
//    @JoinColumn(name = "user_id")
//    private User user;
//
//    public Linker() {
//    }
//
//    public Linker(Long linkerId, User user) {
//        this.linkerId = linkerId;
//        this.user = user;
//    }
//}
