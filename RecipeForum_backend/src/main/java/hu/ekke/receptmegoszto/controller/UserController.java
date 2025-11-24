package hu.ekke.receptmegoszto.controller;

import hu.ekke.receptmegoszto.domain.RecipeUser;
import hu.ekke.receptmegoszto.dto.UserRegisterDto;
import hu.ekke.receptmegoszto.dto.UserDto;
import hu.ekke.receptmegoszto.repository.UserDetailsRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Optional;

@RestController
@RequestMapping("/user")
public class UserController {

    private final UserDetailsRepository repository;
    private final PasswordEncoder encoder;

    public UserController(UserDetailsRepository repository, PasswordEncoder encoder) {
        this.repository = repository;
        this.encoder = encoder;
    }

    @PostMapping("/register")
    public String register(@RequestBody UserRegisterDto dto) {
        if (repository.findByUserName(dto.userName()).isPresent()) {
            return "Hiba: ilyen felhasználó már létezik!";
        }

        RecipeUser user = new RecipeUser();
        user.setName(dto.name());
        user.setUserName(dto.userName());
        user.setPasswordEncoded(encoder.encode(dto.password()));
        user.setEmail(dto.email());

        repository.save(user);
        return "Sikeres regisztráció!";
    }

    @GetMapping
    public ResponseEntity<UserDto> getCurrentUser(Principal principal) {
        if (principal == null || principal.getName() == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        Optional<RecipeUser> userOpt = repository.findByUserName(principal.getName());
        return userOpt.map(u -> new UserDto(u.getId(), u.getName(), u.getUserName(), u.getEmail(), u.getProfileImageRef()))
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }
}
