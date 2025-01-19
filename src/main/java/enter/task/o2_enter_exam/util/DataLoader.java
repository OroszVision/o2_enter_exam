package enter.task.o2_enter_exam.util;

import enter.task.o2_enter_exam.model.AppUser;
import enter.task.o2_enter_exam.repository.AppUserRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataLoader {

    private final AppUserRepository repository;
    private final BCryptPasswordEncoder passwordEncoder;

    @PostConstruct
    public void loadData() {
        repository.save(new AppUser(null, "John", "Doe", "JohnDoe",
                "john.doe@example.com", passwordEncoder.encode("johndoe"), 30));
        repository.save(new AppUser(null, "Jane", "Smith", "JaneSmith",
                "jane.smith@example.com", passwordEncoder.encode("janesmith"), 25));
        repository.save(new AppUser(null, "Alice", "Brown", "AliceBrown",
                "alice.brown@example.com", passwordEncoder.encode("alicebrown"), 28));
    }
}
