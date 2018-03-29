package projects.com.communication.tool;

import org.testng.annotations.Test;

import javax.mail.Message;
import javax.mail.MessagingException;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;
import static settings.MailReader.*;
import static settings.SeleniumListener.LOG;

public class MailTest {

    @Test(timeOut = 10000)
    public void check() throws MessagingException {
        String fromEmail = "ngervasyuk@speakwithageek.com";
        openMailFolder();
        Message[] messages = receiveMail(fromEmail);
        assertEquals(messages.length, 1);

        for (int i = 0; i < messages.length; i++) {
            Message message = messages[i];
            LOG.info("Email Number " + (i + 1));
            LOG.info("Subject: " + message.getSubject());
            LOG.info("From: " + message.getFrom()[0]);
            assertEquals(message.getSubject(), "subj");
            assertTrue(message.getFrom()[0].toString().contains(fromEmail));
        }
        deleteMessages();
    }
}
