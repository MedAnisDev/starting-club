package com.example.startingclubbackend.service.email;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.sound.midi.MidiMessage;

@Service
public class EmailSenderServiceImpl implements EmailSenderService {

    @Value("${spring.mail.username}")
    String SenderOfEmail ;

    private final static Logger LOGGER = LoggerFactory
            .getLogger(EmailSenderServiceImpl.class);
    private final JavaMailSender mailSender ;

    public EmailSenderServiceImpl(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }


    @Override
    @Async
    public void sendEmail(final String to, String email) {
        try {
            MimeMessage mimeMessage =mailSender.createMimeMessage() ;
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage , "utf-8") ;
            helper.setText(email ,true);
            helper.setTo(to);
            helper.setSubject(email);
            helper.setFrom(SenderOfEmail);

            mailSender.send(mimeMessage);

        }catch (MessagingException e){
            LOGGER.error("failed to send email", e);
            throw new IllegalStateException("failed to send email");
        }
    }

    @Override
    public String emailTemplateConfirmation(String name, String link) {
        return "<div style=\"font-family: Arial, sans-serif; line-height: 1.6; background-color: #f5f5f5; margin: 0; padding: 0;\">\n" +
                "    <div style=\"max-width: 600px; margin: 0 auto; padding: 20px; background-color: #fff; border-radius: 5px; box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);\">\n" +
                "        <div style=\"background-color: #007bff; color: #fff; text-align: center; padding: 10px; border-top-left-radius: 5px; border-top-right-radius: 5px;\">\n" +
                "            <h2>Email Verification</h2>\n" +
                "        </div>\n" +
                "        <div style=\"padding: 20px;\">\n" +
                "            <p>Dear "+ name +",</p>\n" +
                "            <p>Thank you for signing up with our service. To activate your account, please click the button below:</p>\n" +
                "            <a style=\"display: block; width: 150px; padding: 10px; text-align: center; background-color: #007bff; color: #fff; text-decoration: none; border-radius: 5px; margin: 20px auto;\" href=" + link + ">Activate Account</a>\n" +
                "            <p>If the button above doesn't work, you can also copy and paste the following link into your browser:</p>\n" +
                "            <p>Activation link: " +link+"</p>"+
                "            <p>If you did not sign up for this service, please ignore this email.</p>\n" +
                "        </div>\n" +
                "        <div style=\"text-align: center; padding-top: 20px;\">\n" +
                "            <p>Best regards,<br>Ruspina</p>\n" +
                "        </div>\n" +
                "    </div>\n" +
                "</div>";
    }


}
