package com.yamankwefati.webshopapi.dao.email;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.HashMap;
import java.util.Map;

@Component
@AllArgsConstructor
public class EmailDAO implements EmailSender{
    private final static Logger LOGGER = LoggerFactory.getLogger(EmailDAO.class);
    private final JavaMailSender mailSender;
    @Autowired
    private TemplateEngine templateEngine;
    private final Environment environment;

    @Override
    @Async
    public void send(String to, String email, String subject) {
        try {
            String emailAddress = environment.getProperty("spring.mail.username");
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");
            helper.setText(email, false);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setFrom(emailAddress);
            mailSender.send(mimeMessage);
        } catch (MessagingException e){
            LOGGER.error("failed to send email", e);
            throw new IllegalStateException("failed to send email");
        }
    }

    @Async
    public void sendOrderConfirmationEmail(String to, String email, String subject) {
        try {
            String emailAddress = environment.getProperty("spring.mail.username");
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");
            helper.setText(email, true);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setFrom(emailAddress);
            mailSender.send(mimeMessage);
        } catch (MessagingException e){
            LOGGER.error("failed to send email", e);
            throw new IllegalStateException("failed to send email");
        }
    }

    public String buildOrderEmail(String templateName, Map<String, Object> variables) {
        Context context = new Context();
        context.setVariables(variables);
        return templateEngine.process(templateName, context);
    }
}
