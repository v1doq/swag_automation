package settings;

import javax.mail.*;
import javax.mail.search.SearchTerm;
import java.io.IOException;
import java.util.Properties;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;
import static settings.SeleniumListener.LOG;

public class MailReader {
    private static final String USER = "communication.tool@outlook.com";
    private static final String PASS = "Passcommunication1";
    private static final String HOST = "imap.outlook.com";

    public static void readEmail(String fromEmail, int messageCount, String subject, String body) {
        try {
            Properties props = new Properties();
            props.put("mail.store.protocol", "imaps");
            Session session = Session.getInstance(props);
            Store store = session.getStore();
            store.connect(HOST, USER, PASS);

            Folder folder = store.getFolder("Junk");
            folder.open(Folder.READ_WRITE);
            folder.setFlags(folder.getMessages(), new Flags(Flags.Flag.DELETED), true);

            SearchTerm searchCondition = getSearchTerm(fromEmail);
            Message[] messages = folder.search(searchCondition);
            while (messages.length == 0) {
                LOG.info("messages.length: " + messages.length);
                Thread.sleep(500);
            }
            assertEquals(messageCount, messages.length);

            for (int i = 0; i < messages.length; i++) {
                Message message = messages[i];
                LOG.info("Email Number " + (i + 1));
                LOG.info("Subject: " + message.getSubject());
                LOG.info("From: " + message.getFrom()[0]);
                LOG.info("Text: " + getText(message));
                assertEquals(message.getSubject(), subject);
                String text = getText(message);
                if (text != null){
                    assertTrue(text.contains(body));
                }
            }
            folder.setFlags(messages, new Flags(Flags.Flag.DELETED), true);
            folder.close(true);
            store.close();

        } catch (IOException e) {
            e.printStackTrace();
        } catch (NoSuchProviderException e) {
            e.printStackTrace();
        } catch (MessagingException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static SearchTerm getSearchTerm(final String email) {
        return new SearchTerm() {
            @Override
            public boolean match(Message message) {
                try {
                    Address[] fromAddress = message.getFrom();
                    if (fromAddress != null && fromAddress.length > 0) {
                        if (fromAddress[0].toString().contains(email)) {
                            return true;
                        }
                    }
                } catch (MessageRemovedException e){
                    e.printStackTrace();
                } catch (MessagingException e) {
                    e.printStackTrace();
                }
                return false;
            }
        };
    }

    public static String getText(Part p) throws MessagingException, IOException {
        if (p.isMimeType("text/*")) {
            return (String) p.getContent();
        }
        if (p.isMimeType("multipart/alternative")) {
            Multipart mp = (Multipart) p.getContent();
            String text = null;
            for (int i = 0; i < mp.getCount(); i++) {
                Part bp = mp.getBodyPart(i);
                if (bp.isMimeType("text/*")) {
                    if (text == null)
                        text = getText(bp);
                } else {
                    return getText(bp);
                }
            }
            return text;
        } else if (p.isMimeType("multipart/*")) {
            Multipart mp = (Multipart) p.getContent();
            for (int i = 0; i < mp.getCount(); i++) {
                String s = getText(mp.getBodyPart(i));
                if (s != null)
                    return s;
            }
        }
        return null;
    }
}
