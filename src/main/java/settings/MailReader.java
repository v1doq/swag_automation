package settings;

import javax.mail.*;
import javax.mail.search.SearchTerm;
import java.io.IOException;
import java.util.Properties;

import static common.ConciseApi.sleep;
import static settings.SeleniumListener.LOG;

public class MailReader {

    private static Folder folder;
    private static Store store;
    private static Message[] messages;

    public static void openMailFolder(){
        try {
            Properties props = new Properties();
            props.put("mail.store.protocol", "imaps");
            Session session = Session.getInstance(props);
            store = session.getStore();
            store.connect("imap.outlook.com", "communication.tool@outlook.com", "Passcommunication1");
            folder = store.getFolder("inbox");
            folder.open(Folder.READ_WRITE);
        } catch (NoSuchProviderException e) {
            e.printStackTrace();
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    public static Message[] receiveMail(String fromEmail) {
        try {
            SearchTerm searchCondition = getSearchTerm(fromEmail);
            messages = folder.search(searchCondition);
            while (messages.length == 0) {
                LOG.info("messages.length: " + messages.length);
                sleep(500);
            }
        } catch (NoSuchProviderException e) {
            e.printStackTrace();
        } catch (MessagingException e) {
            e.printStackTrace();
        }
        try {
            return folder.getMessages();
        } catch (MessagingException e) {
            e.printStackTrace();
        }
        return new Message[0];
    }

    public static void deleteMessages(){
        try {
            folder.setFlags(messages, new Flags(Flags.Flag.DELETED), true);
            folder.close(true);
            store.close();
        } catch (MessagingException e) {
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
                } catch (MessagingException e) {
                    e.printStackTrace();
                }
                return false;
            }
        };
    }

    public static String getMessageBody(Part p) throws MessagingException, IOException {
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
                        text = getMessageBody(bp);
                } else {
                    return getMessageBody(bp);
                }
            }
            return text;
        } else if (p.isMimeType("multipart/*")) {
            Multipart mp = (Multipart) p.getContent();
            for (int i = 0; i < mp.getCount(); i++) {
                String s = getMessageBody(mp.getBodyPart(i));
                if (s != null)
                    return s;
            }
        }
        return null;
    }
}
