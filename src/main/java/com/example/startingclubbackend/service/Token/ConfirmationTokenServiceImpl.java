package com.example.startingclubbackend.service.Token;

import com.example.startingclubbackend.exceptions.custom.ResourceNotFoundCustomException;
import com.example.startingclubbackend.model.token.ConfirmationToken;
import com.example.startingclubbackend.model.user.Athlete;
import com.example.startingclubbackend.repository.ConfirmationTokenRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@Slf4j
public class ConfirmationTokenServiceImpl implements ConfirmationTokenService{
    private final ConfirmationTokenRepository confirmationTokenRepository ;

    public ConfirmationTokenServiceImpl(ConfirmationTokenRepository confirmationTokenRepository) {
        this.confirmationTokenRepository = confirmationTokenRepository;
    }

    @Override
    public ConfirmationToken fetchTokenByToken(final String token) {
        return confirmationTokenRepository.fetchByToken(token)
                .orElseThrow(()->new ResourceNotFoundCustomException("confirmation token not found"));
    }

    @Override

    public void deleteConfirmTokenByUserId(Long athleteId) {
        boolean isTokenExists = confirmationTokenRepository.fetchByUserId(athleteId) ;
        if(isTokenExists){
            confirmationTokenRepository.deleteByUserId(athleteId);
        }
    }


    @Override
    public String generateConfirmationToken(final Athlete athlete) {
        String token = UUID.randomUUID().toString();
        ConfirmationToken confirmationToken = new ConfirmationToken(
                token ,
                LocalDateTime.now(),
                LocalDateTime.now().plusMinutes(15),
                athlete
        );
        confirmationTokenRepository.save(confirmationToken);
        return token ;
    }

    @Override
    public void setConfirmedAt(String token) {
        ConfirmationToken confirmationToken = fetchTokenByToken(token) ;
        confirmationToken.setConfirmedAt(LocalDateTime.now());
        confirmationTokenRepository.save(confirmationToken) ;
    }

    @Override
    public String getConfirmationPage() {
        return "<!DOCTYPE html>\n" +
                "<html lang=\"en\">\n" +
                "<head>\n" +
                "    <meta charset=\"UTF-8\">\n" +
                "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
                "    <title>Email Confirmed</title>\n" +
                "    <style>\n" +
                "        body {\n" +
                "            font-family: Arial, sans-serif;\n" +
                "            background-color: #007bff;\n" +
                "            margin: 0;\n" +
                "            display: flex;\n" +
                "            justify-content: center;\n" +
                "            align-items: center;\n" +
                "            height: 100vh;\n" +
                "        }\n" +
                "        .container {\n" +
                "            text-align: center;\n" +
                "            padding: 30px;\n" +
                "            background-color: white;\n" +
                "            border-radius: 10px;\n" +
                "            box-shadow: 0px 0px 10px rgba(0, 0, 0, 0.2);\n" +
                "        }\n" +
                "        h1 {\n" +
                "            color: #333;\n" +
                "        }\n" +
                "        p {\n" +
                "            color: #666;\n" +
                "            margin-top: 10px;\n" +
                "        }\n" +
                "        .close-button {\n" +
                "            background-color: #007bff;\n" +
                "            color: white;\n" +
                "            border: none;\n" +
                "            padding: 10px 20px;\n" +
                "            border-radius: 5px;\n" +
                "            cursor: pointer;\n" +
                "            font-size: 16px;\n" +
                "            transition: background-color 0.3s;\n" +
                "        }\n" +
                "        .close-button:hover {\n" +
                "            background-color: #0056b3;\n" +
                "        }\n" +
                "    </style>\n" +
                "</head>\n" +
                "<body>\n" +
                "<div class=\"container\">\n" +
                "    <h1>Email Confirmed</h1>\n" +
                "    <p>Your email address has been successfully confirmed. Thank you for joining us!</p>\n" +
                "    <button class=\"close-button\" onclick=\"window.close()\">Close</button>\n" +
                "</div>\n" +
                "</body>\n" +
                "</html>\n";
    }

    @Override
    public String getAlreadyConfirmedPage() {
        return "<!DOCTYPE html>\n" +
                "<html lang=\"en\">\n" +
                "<head>\n" +
                "  <meta charset=\"UTF-8\">\n" +
                "  <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
                "  <title>Email Already Confirmed</title>\n" +
                "  <style>\n" +
                "    body {\n" +
                "      font-family: Arial, sans-serif;\n" +
                "      background-color: #007bff;\n" +
                "      margin: 0;\n" +
                "      display: flex;\n" +
                "      justify-content: center;\n" +
                "      align-items: center;\n" +
                "      height: 100vh;\n" +
                "    }\n" +
                "    .container {\n" +
                "      text-align: center;\n" +
                "      padding: 30px;\n" +
                "      background-color: white;\n" +
                "      border-radius: 10px;\n" +
                "      box-shadow: 0px 0px 10px rgba(0, 0, 0, 0.2);\n" +
                "    }\n" +
                "    h1 {\n" +
                "      color: #333;\n" +
                "    }\n" +
                "    p {\n" +
                "      color: #666;\n" +
                "      margin-top: 10px;\n" +
                "    }\n" +
                "    .close-button {\n" +
                "      background-color: #007bff;\n" +
                "      color: white;\n" +
                "      border: none;\n" +
                "      padding: 10px 20px;\n" +
                "      border-radius: 5px;\n" +
                "      cursor: pointer;\n" +
                "      font-size: 16px;\n" +
                "      transition: background-color 0.3s;\n" +
                "    }\n" +
                "    .close-button:hover {\n" +
                "      background-color: #0056b3;\n" +
                "    }\n" +
                "  </style>\n" +
                "</head>\n" +
                "<body>\n" +
                "  <div class=\"container\">\n" +
                "    <h1>Email Already Confirmed</h1>\n" +
                "    <p>Your email address has already been confirmed. Thank you for your continued support!</p>\n" +
                "    <button class=\"close-button\" onclick=\"window.close()\">Close</button>\n" +
                "  </div>\n" +
                "</body>\n" +
                "</html>\n";
    }
}
