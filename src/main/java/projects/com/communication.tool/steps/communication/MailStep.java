package projects.com.communication.tool.steps.communication;

import io.qameta.allure.Step;

import javax.mail.*;
import javax.mail.search.SearchTerm;
import java.io.IOException;
import java.util.Properties;

import static common.ConciseApi.sleep;
import static settings.SeleniumListener.LOG;

public class MailStep {

    private static Folder folder;
    private static Store store;
    private static Message[] messages;
    public static final String INBOX = "inbox";
    private static final String JUNK = "junk";
    private static final String SENT = "sent";
    public static final String TEST_EMAIL = "communication.tool@outlook.com";

    @Step("Open mail folder")
    public static void openMailFolder(String folderName){
        LOG.info("Open mail folder: " + folderName.toUpperCase());
        try {
            Properties props = new Properties();
            props.put("mail.store.protocol", "imaps");
            Session session = Session.getInstance(props);
            store = session.getStore();
            store.connect("imap.outlook.com", TEST_EMAIL, "Passcommunication1");
            folder = store.getFolder(folderName);
            folder.open(Folder.READ_WRITE);
            messages = folder.getMessages();
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    @Step("Receive all messages")
    public static Message[] receiveMail(String fromEmail, int count) {
        LOG.info("Receive messages in " + folder.getFullName().toUpperCase() + " folder from email: " + fromEmail);
        try {
            SearchTerm searchCondition = getSearchTerm(fromEmail);
            messages = folder.search(searchCondition);
            while (messages.length != count) {
                LOG.info("actual count: " + messages.length + ", expected count: " + count);
                sleep(5000);
                messages = folder.search(searchCondition);
            }
            LOG.info("actual count: " + messages.length + ", expected count: " + count);
            getCountOfMessageInFolders();
            return messages;
        } catch (MessagingException e) {
            e.printStackTrace();
        }
        return new Message[0];
    }

    @Step("Clean mail folders")
    public static void cleanMailFolders(){
        openMailFolder(INBOX);
        deleteMessages();
        openMailFolder(JUNK);
        deleteMessages();
        openMailFolder(SENT);
        deleteMessages();
    }

    public static String getMessageBody(Part p)  {
        try {
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
        } catch (MessagingException | IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Step("Delete all messages in folder")
    private static void deleteMessages(){
        LOG.info("Delete all messages in: " + folder.getFullName().toUpperCase());
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

    private static void getCountOfMessageInFolders(){
        try {
            Folder[] folders = store.getDefaultFolder().list();
            for (Folder folder : folders) {
                LOG.info(folder.getFullName() + ": " + folder.getMessageCount());
            }
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}
