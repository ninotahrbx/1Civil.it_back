package fr.civilIteam.IncivilitiesTrack.service.impl;



import fr.civilIteam.IncivilitiesTrack.service.IEmailService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;

@Service
public class EmailServiceImpl implements IEmailService {
    @Value("${FRONT_URL}")
    private String front;
    @Autowired
    private JavaMailSender mailSender;
    private final String FROM = "noreply.1civilit@gmail.com";

    public void sendEmail(String to, String subject, String body) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(FROM);
        message.setTo(to);
        message.setSubject(subject);
        message.setText(body);

        mailSender.send(message);
    }

    public void sendHtmlEmail(String to, String template, HashMap<String,String> elements) throws MessagingException, IOException {
        MimeMessage message = mailSender.createMimeMessage();

        message.setFrom(FROM);
        message.setRecipients(MimeMessage.RecipientType.TO,to);
        if (template == null || template.isBlank()) template = "default";
        String htmlTemplate = readFile("src/main/java/fr/civilIteam/IncivilitiesTrack/template/"+template+".html");
        String htmlContent = "";
        switch (template){
            case "ForgottenPassword":
                message.setSubject("Mot de passe oublié");
                String front_url = front+"/change-password/"+elements.get("token");
                htmlContent = htmlTemplate.replace("${url}",front_url);
                break;
            case "default":
            default:
                message.setSubject("Default Email Testing");
                htmlContent = htmlTemplate.replace("${name}",elements.get("name"));
                break;
        }
        message.setContent(htmlContent, "text/html; charset=utf-8");

        mailSender.send(message);
    }


    private String readFile(String filePath) throws IOException{
        Path path = Paths.get(filePath);
        return Files.readString(path, StandardCharsets.UTF_8);
    }
}
