package com.yamankwefati.webshopapi.dao.resetToken;

import com.yamankwefati.webshopapi.model.ConfirmationToken;
import com.yamankwefati.webshopapi.model.User;
import com.yamankwefati.webshopapi.model.auth.PasswordResetToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, Long> {
    Optional<PasswordResetToken> findByToken(String token);
}
