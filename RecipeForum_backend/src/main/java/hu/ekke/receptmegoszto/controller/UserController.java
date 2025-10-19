package hu.ekke.receptmegoszto.controller;

import hu.ekke.receptmegoszto.domain.RecipeUser;
import hu.ekke.receptmegoszto.dto.UserRegisterDto;
import hu.ekke.receptmegoszto.repository.UserDetailsRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserDetailsRepository repository;
    private final PasswordEncoder encoder;

    public UserController(UserDetailsRepository repository, PasswordEncoder encoder) {
        this.repository = repository;
        this.encoder = encoder;
    }

    @PostMapping("/register")
    public String register(@RequestBody UserRegisterDto dto) {
        if (repository.findByUserName(dto.name()).isPresent()) {
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
}
