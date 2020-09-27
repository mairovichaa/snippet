package com.amairovi.mail;

import com.sun.mail.imap.IMAPStore;

import javax.mail.*;
import javax.mail.internet.MimeMultipart;
import javax.mail.search.SubjectTerm;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Properties;

public class Main {

    public static void main(String[] args) {
        String host = "host";
        String mailStoreType = "imap";
        String username = "username";
        String password = "pass";

        receiveEmail(host, mailStoreType, username, password);
    }

    public static void receiveEmail(String imapHost, String storeType,
                                    String user, String password) {
        try {
            Properties properties = new Properties();
            properties.put("mail.imap.host", imapHost);
            Session emailSession = Session.getInstance(properties, new javax.mail.Authenticator() {
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(user, password);
                }
            });

            IMAPStore emailStore = (IMAPStore) emailSession.getStore(storeType);
            emailStore.connect(imapHost, 143, user, password);

            Folder emailFolder = emailStore.getFolder("INBOX");
            emailFolder.open(Folder.READ_ONLY);


            Message[] messages = emailFolder.search(new SubjectTerm("pattern"));
            for (int i = 0; i < messages.length; i++) {
                Message message = messages[i];
                System.out.println("---------------------------------");
                System.out.println("Email Number " + (i + 1));
                System.out.println("Subject: " + message.getSubject());
                System.out.println("Date: " + message.getReceivedDate());
                System.out.println("Date: " + message.getSentDate());
                System.out.println("From: " + message.getFrom()[0]);
                int textPartPos = 0;
                Multipart multipart = (Multipart) message.getContent();
                for (int k = 0; k < multipart.getCount(); k++) {
                    System.out.println("part #" + k);

                    BodyPart bodyPart = multipart.getBodyPart(k);
                    if (k == textPartPos) {
                        final Object text = ((MimeMultipart) (bodyPart.getContent())).getBodyPart(0).getContent();
                        System.out.println("Text: " + text);

                    } else {
                        InputStream stream = bodyPart.getInputStream();
                        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(stream));

                        bufferedReader.lines().forEach(System.out::println);
                        System.out.println();
                    }

                }
            }

            emailFolder.close(false);
            emailStore.close();

        } catch (NoSuchProviderException e) {
            e.printStackTrace();
        } catch (MessagingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
