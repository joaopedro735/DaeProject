package ejbs;

import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import java.util.Date;
import java.util.logging.Logger;

@Stateless(name = "EmailEJB")
public class EmailBean {
    @Resource(name = "java:/jboss/mail/mailgun")
    private Session mailSession;

    private Logger logger = Logger.getLogger(EmailBean.class.getName());

    public void send(String to, String subject, String text) throws
            MessagingException {
        logger.info("Begin to send email to->[" + to + "],subject->[" + subject + "]");
        Runnable r = new Runnable() {
            @Override
            public void run() {
                try {
                    Message message = new MimeMessage(mailSession);
                    message.addFrom(InternetAddress.parse("sports@mail.restaurantte.tk", false));
                    message.setRecipients(Message.RecipientType.TO,
                            InternetAddress.parse(to, false));

                    message.setSubject(subject);

                    message.setText(text);

                    Date timeStamp = new Date();
                    message.setSentDate(timeStamp);
                    Transport.send(message);
                } catch (MessagingException e) {
                    System.out.println("ERROR_SEND_EMAIL" + e.getMessage());
                }
            }
        };

       r.run();

    }
}