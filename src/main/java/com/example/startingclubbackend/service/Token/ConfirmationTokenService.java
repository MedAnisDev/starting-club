package com.example.startingclubbackend.service.Token;

import com.example.startingclubbackend.model.token.ConfirmationToken;
import com.example.startingclubbackend.model.user.Athlete;
import jakarta.validation.constraints.NotNull;

public interface ConfirmationTokenService {
    ConfirmationToken fetchTokenByToken(final String token);

    void deleteConfirmTokenByUserId(final Long athleteId) ;
    String generateConfirmationToken(@NotNull final Athlete athlete) ;
    void setConfirmedAt(final String token) ;

    String getAlreadyConfirmedPage();
    String getConfirmationPage() ;
}
