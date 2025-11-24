package hu.ekke.receptmegoszto.dto;

public record UserDto(
        Integer id,
        String name,
        String userName,
        String email,
        String profileImageRef
) {
}

