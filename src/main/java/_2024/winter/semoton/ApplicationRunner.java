package _2024.winter.semoton;

import _2024.winter.semoton.domain.landmark.entity.Landmark;
import _2024.winter.semoton.domain.landmark.repository.LandmarkRepository;
import _2024.winter.semoton.domain.user.entity.User;
import _2024.winter.semoton.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ApplicationRunner implements CommandLineRunner {

    private final UserRepository userRepository;
    private final LandmarkRepository landmarkRepository;


    @Override
    public void run(String... args) throws Exception {
        User user1 = User.builder()
                .username("testid1")
                .password("testpw1")
                .nickname("testnn1")
                .role("ROLE_USER")
                .totalScore(93)
                .allCleared(true)
                .build();
        User user2 = User.builder()
                .username("testid2")
                .password("testpw2")
                .nickname("testnn2")
                .role("ROLE_USER")
                .totalScore(165)
                .allCleared(true)
                .build();
        User user3 = User.builder()
                .username("testid3")
                .password("testpw3")
                .nickname("testnn3")
                .role("ROLE_USER")
                .totalScore(204)
                .allCleared(true)
                .build();

        userRepository.saveAllAndFlush(List.of(user1, user2, user3));

        Landmark mainGate = Landmark.builder()
                .name("mainGate")
                .latitude(37.247363687976694)
                .longitude(127.07841584411939)
                .build();

        Landmark library = Landmark.builder()
                .name("library")
                .latitude(37.24086172885768)
                .longitude(127.07979251605441)
                .build();

        Landmark peaceHall = Landmark.builder()
                .name("peaceHall")
                .latitude(37.23814278552151)
                .longitude(127.0831226705363)
                .build();

        landmarkRepository.saveAllAndFlush(List.of(mainGate, library, peaceHall));
    }
}
