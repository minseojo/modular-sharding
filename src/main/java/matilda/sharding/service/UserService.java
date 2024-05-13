//package matilda.sharding.service;
//
//import lombok.RequiredArgsConstructor;
//import matilda.sharding.domain.User;
//import matilda.sharding.repository.UserRepository;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//@Service
//@RequiredArgsConstructor
//@Transactional(readOnly = true)
//public class UserService {
//
//    private final UserRepository userRepository;
//
//    @Transactional
//    public void save(User user) {
//        userRepository.save(user);
//        System.out.println(user.getUserId());
//    }
//}
