package com.example.testit.adapter.mail;

import com.example.testit.model.Member;

public interface MailService {

    /**
     * Envoie un mail au membre avec sujet et message.
     * @param member le destinataire
     * @param subject le sujet du mail
     * @param message le contenu du message
     */
    void sendMail(Member member, String subject, String message);

    /**
     * Envoie le mail de clôture de l'emprunt (héritage).
     * @param member
     */
    void sendMailCloture(Member member);

}
