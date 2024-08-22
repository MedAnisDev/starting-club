package com.example.startingclubbackend.service.Token;

import com.example.startingclubbackend.model.token.ConfirmationToken;
import com.example.startingclubbackend.model.user.athlete.Athlete;
import jakarta.validation.constraints.NotNull;

public interface ConfirmationTokenService {
    ConfirmationToken fetchTokenByToken(String token);

    void deleteConfirmTokenByUserId(Long athleteId);

    String generateConfirmationToken(@NotNull Athlete athlete);

    void setConfirmedAt(String token);

    String getAlreadyConfirmedPage();

    String getConfirmationPage();
}
