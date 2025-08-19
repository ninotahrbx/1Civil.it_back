package fr.civilIteam.IncivilitiesTrack.service;

import jakarta.mail.MessagingException;

import java.io.IOException;
import java.util.HashMap;

public interface IEmailService {
    void sendEmail(String to, String subject, String body);
    void sendHtmlEmail(String to,String template, HashMap<String,String> elements) throws MessagingException, IOException;
}
