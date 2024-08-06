package com.example.startingclubbackend.service.email;

public interface EmailSenderService {
    void sendEmail(final String to , String email);
    String emailTemplateConfirmation(String name, String link) ;
}
