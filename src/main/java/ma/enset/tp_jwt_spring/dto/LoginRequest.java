package ma.enset.tp_jwt_spring.dto;

public record LoginRequest(
        String email,
        String password
) {
}