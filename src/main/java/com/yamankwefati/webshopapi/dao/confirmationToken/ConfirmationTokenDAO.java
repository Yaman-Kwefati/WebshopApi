package com.yamankwefati.webshopapi.dao.confirmationToken;

import com.yamankwefati.webshopapi.dao.user.UserDAO;
import com.yamankwefati.webshopapi.model.ConfirmationToken;
import jakarta.transaction.Transactional;
import javassist.NotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Optional;

@Component
@AllArgsConstructor
public class ConfirmationTokenDAO {

    private final ConfirmationTokenRepository confirmationTokenRepository;
    private final UserDAO userDAO;

    public void saveConfirmationToken(ConfirmationToken token){
        this.confirmationTokenRepository.save(token);
    }

    public ConfirmationToken getToken(String token) {
        Optional<ConfirmationToken> confirmationToken = this.confirmationTokenRepository.findByToken(token);
        if (!confirmationToken.isPresent()){
            try {
                throw new NotFoundException("Token not found");
            } catch (NotFoundException e) {
                throw new RuntimeException(e);
            }
        }
        return confirmationToken.get();
    }

    @Transactional
    public void setConfirmedAt(ConfirmationToken token) {
        token.setConfirmedAt(LocalDateTime.now());
    }

    @Transactional
    public String confirmToken(String token, Long userId){
        ConfirmationToken confirmationToken = getToken(token);
        if (confirmationToken.getConfirmedAt() != null){
            throw new IllegalStateException("email already confirmed");
        }
        LocalDateTime expiredAt = confirmationToken.getExpiresAt();
        if (expiredAt.isBefore(LocalDateTime.now())){
            throw new IllegalStateException("token expired");
        }
        setConfirmedAt(confirmationToken);
        try {
            userDAO.enableUser(userId);
        } catch (NotFoundException e) {
            throw new RuntimeException(e);
        }
        return "Confirmed";
    }
}
