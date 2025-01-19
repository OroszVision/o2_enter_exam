package enter.task.o2_enter_exam.serivce;

import enter.task.o2_enter_exam.dto.AppUserDto;
import enter.task.o2_enter_exam.model.AppUser;
import enter.task.o2_enter_exam.repository.AppUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AppUserService {

    private final AppUserRepository repository;

    public List<AppUserDto> findAll() {
        return repository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public Optional<AppUserDto> findById(Long id) {
        return repository.findById(id).map(this::convertToDto);
    }

    private AppUserDto convertToDto(AppUser appUser) {
        return new AppUserDto(
                appUser.getId(),
                appUser.getUsername(),
                appUser.getFirstName(),
                appUser.getLastName(),
                appUser.getEmail(),
                appUser.getAge()
        );
    }
}
