package ma.enset.tp_jwt_spring.dto;

public record RegisterRequest(
        String email,
        String password
) {
}