package enter.task.o2_enter_exam.controller;

import enter.task.o2_enter_exam.dto.AppUserDto;
import enter.task.o2_enter_exam.model.AppUser;
import enter.task.o2_enter_exam.serivce.AppUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.springframework.security.core.annotation.AuthenticationPrincipal;

import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class AppUserController {

    private final AppUserService appUserService;

    @GetMapping
    public ResponseEntity<List<AppUserDto>> getAllUsers(@AuthenticationPrincipal AppUser currentUser) {
        List<AppUserDto> users = appUserService.findAll();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AppUserDto> getUserById(@PathVariable Long id, @AuthenticationPrincipal AppUser currentUser) {
        Optional<AppUserDto> appUserDto = appUserService.findById(id);
        return appUserDto.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body(null));
    }
}
