package com.petverse.domain.service;

import com.petverse.domain.dto.AuthResponseDTO;
import com.petverse.domain.dto.LoginRequestDTO;
import com.petverse.domain.dto.RegisterRequestDTO;
import com.petverse.domain.dto.UserAuthResponseDTO;
import com.petverse.domain.entity.User;
import com.petverse.exception.BusinessException;
import io.smallrye.jwt.build.Jwt;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.mindrot.jbcrypt.BCrypt;

import java.time.Duration;
import java.util.Set;

@ApplicationScoped
public class AuthService {
    @ConfigProperty(name = "petverse.jwt.expiration-hours", defaultValue = "24")
    long expirationHours;

    @Transactional
    public AuthResponseDTO register(RegisterRequestDTO dto) {
        if (User.findByEmail(dto.email) != null) {
            throw new BusinessException("User already exists with this e-mail");
        }

        User user = new User();
        user.name = dto.name;
        user.email = dto.email.toLowerCase().trim();
        user.password = BCrypt.hashpw(dto.password, BCrypt.gensalt());
        user.phone = dto.phone;
        user.bio = "";
        user.active = true;
        user.persist();

        String token = generateToken(user);
        return new AuthResponseDTO(token, UserAuthResponseDTO.from(user));
    }

    public AuthResponseDTO login(LoginRequestDTO dto) {
        User user = User.findByEmail(dto.email.toLowerCase().trim());

        if (user == null || !user.active) {
            throw new BusinessException("Invalid user");
        }

        if (!BCrypt.checkpw(dto.password, user.password)) {
            throw new BusinessException("Invalid credentials");
        }

        String token = generateToken(user);

        return new AuthResponseDTO(token, UserAuthResponseDTO.from(user));
    }

    public UserAuthResponseDTO me(String email) {
        User user = User.findByEmail(email);

        if (user == null || !user.active) {
            throw new BusinessException("User not found");
        }

        return UserAuthResponseDTO.from(user);
    }

    private String generateToken(User user) {
        return Jwt.issuer("petverse")
                .subject(user.email)
                .groups(Set.of("user"))
                .claim("userId", user.id)
                .claim("name", user.name)
                .expiresIn(Duration.ofHours(expirationHours))
                .sign();
    }
}
