package com.yamankwefati.webshopapi.dao.resetToken;

import com.yamankwefati.webshopapi.model.User;
import com.yamankwefati.webshopapi.model.auth.PasswordResetToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Optional;

@Component
public class PasswordResetTokenDAO {
    @Autowired
    private PasswordResetTokenRepository passwordResetTokenRepository;

    public Optional<PasswordResetToken> getToken(String token){
        return passwordResetTokenRepository.findByToken(token);
    }
    public void createPasswordResetTokenForUser(User user, String token) {
        PasswordResetToken myToken = PasswordResetToken.builder()
                .token(token)
                .createdAt(LocalDateTime.now())
                .expiresAt(LocalDateTime.now().plusMinutes(15))
                .appUser(user)
                .build();
        passwordResetTokenRepository.save(myToken);
    }


    public String validatePasswordResetToken(String token) {
        Optional<PasswordResetToken> passTokenOpt = passwordResetTokenRepository.findByToken(token);

        if (!passTokenOpt.isPresent()) {
            return "invalidToken";
        }

        PasswordResetToken passToken = passTokenOpt.get();
        if (isTokenExpired(passToken)) {
            return "expired";
        }

        return passToken.getToken();
    }


    private boolean isTokenFound(PasswordResetToken passToken) {
        return passToken != null;
    }

    private boolean isTokenExpired(PasswordResetToken passToken) {
        return passToken.getExpiresAt().isBefore(LocalDateTime.now());
    }

    public String buildEmail(String userName, String link){
        return "\n" +
                "Hello "+userName+"\n" +
                "\n" +
                "The following link is to reset your password:\n" +
                ""+link+"\n" +
                "\n" +
                "See you there!\n" +
                "\n" +
                "Best regards, Our Company team";
    }
}
