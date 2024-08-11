package com.example.startingclubbackend.service.email;

public interface EmailSenderService {
    void sendEmail(String to, String email);

    String emailTemplateConfirmation(String name, String link);
}
