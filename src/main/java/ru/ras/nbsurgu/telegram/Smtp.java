package ru.ras.nbsurgu.telegram;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Smtp {

    private static volatile Smtp instance;

    private static final String SMTP_PROPERTIES = "smtp.properties";

    private static final Properties properties = new Properties();

    private static String user = null;
    private static String password = null;

    private Smtp() {}

    public static Smtp getInstance() {
        if (instance == null) {
            synchronized (Smtp.class) {
                if (instance == null) {
                    instance = new Smtp();
                }
            }
        }

        return instance;
    }

    public void init() {
        try {
            properties.load(Smtp.class.getClassLoader().getResourceAsStream(SMTP_PROPERTIES));
        } catch (IOException e) {
            e.printStackTrace();
        }

        user = (String) properties.get("mail.smtp.user");
        password = (String) properties.get("mail.smtp.password");
    }

    public void send(final String email, final long code) {
        final ExecutorService executorService = Executors.newSingleThreadExecutor();

        final Runnable task = () -> {
            final Thread currentThread = Thread.currentThread();
            final String threadName = "thread-email-" + email + "-" + code;

            currentThread.setName(threadName);

            final Session session = Session.getDefaultInstance(properties, new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(user, password);
                }
            });

            final MimeMessage message = new MimeMessage(session);

            try {
                message.setFrom(new InternetAddress(user));
                message.addRecipient(Message.RecipientType.TO, new InternetAddress(email));
                message.setSubject("Код подтверждения", "UTF-8");
                message.setText("Ваш код подтверждения для смены пароля: " + code, "UTF-8");

                Transport.send(message);
            } catch (MessagingException e) {
                e.printStackTrace();
            }
        };

        executorService.submit(task);
        executorService.shutdown();

        try {
            executorService.awaitTermination(5, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}