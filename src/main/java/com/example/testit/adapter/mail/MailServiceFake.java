package com.example.testit.adapter.mail;

import com.example.testit.model.Member;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service

public class MailServiceFake implements MailService {

    private static final Logger logger = LoggerFactory.getLogger(MailServiceFake.class);

    @Override
    public void sendMail(Member member, String subject, String message) {

        logger.info("Fake Mail to " + member.getUsername() + ": " + subject + " - " + message);
    }

    @Override
    public void sendMailCloture(Member member) {
        sendMail(member, "Livre rendu", "Votre livre a été rendu.");
    }
}
