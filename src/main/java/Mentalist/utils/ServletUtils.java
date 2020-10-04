//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package Mentalist.utils;

import java.io.IOException;
import java.util.Properties;
import javax.mail.Address;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class ServletUtils {
    private static String MAIL_SENDER;

    private static String MAIL_USER;

    private static String MAIL_PASS;

    private static String MAIL_SMTP_AUTH;

    private static String MAIL_SMTP_HOST;

    private static String MAIL_SMTP_PORT;

    private static boolean dataMode_log = false;

    private static boolean dataMode_email = false;

    private static boolean dataMode_db = false;

    public enum DebugLevels {
        DEBUG, WARN, ERROR, SILENCED;
    }

    private static DebugLevels debugLevel = DebugLevels.ERROR;

    public static boolean isDataModeDb() {
        return dataMode_db;
    }

    public static void setDataModeDb(boolean dataMode_db) {
        ServletUtils.dataMode_db = dataMode_db;
    }

    public static boolean isDataModeEmail() {
        return dataMode_email;
    }

    public static void setDataModeEmail(boolean dataMode_email) {
        ServletUtils.dataMode_email = dataMode_email;
    }

    public static boolean isDataModeLog() {
        return dataMode_log;
    }

    public static void setDataModeLog(boolean dataMode_log) {
        ServletUtils.dataMode_log = dataMode_log;
    }

    public static void setCredentials(String user, String pass, String sender, String smtpAuth, String smtpHost, String smtpPort) {
        MAIL_USER = user;
        MAIL_PASS = pass;
        MAIL_SENDER = sender;
        MAIL_SMTP_AUTH = smtpAuth;
        MAIL_SMTP_HOST = smtpHost;
        MAIL_SMTP_PORT = smtpPort;
    }

    public static void sendMail(String to, String subject, String body) throws IOException, Exception {
        Properties props = System.getProperties();
        if (!mailReady())
            throw new Exception("Error: use ServletUtils.setCredentials to set username and password");
        props.put("mail.smtp.starttls.enable","true");
        props.put("mail.smtp.host", MAIL_SMTP_HOST);
        props.put("mail.smtp.auth", MAIL_SMTP_AUTH);
        props.put("mail.debug", Boolean.valueOf((debugLevel == DebugLevels.DEBUG)));
        props.put("mail.smtp.port", MAIL_SMTP_PORT);
        props.put("java.net.preferIPv4Stack", "true");
        Session session = null;
        if (MAIL_SMTP_AUTH.equals("false")) {
            session = Session.getDefaultInstance(props, null);
        } else {
            session = Session.getDefaultInstance(props, new Authenticator() {
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(ServletUtils.MAIL_USER, ServletUtils.MAIL_PASS);
                }
            });
        }
        MimeMessage message = new MimeMessage(session);
        message.setFrom((Address)new InternetAddress(MAIL_SENDER));
        message.addRecipient(Message.RecipientType.TO, (Address)new InternetAddress(to));
        message.setSubject(subject);
        message.setText(body);
        Transport.send((Message)message);
    }

    public static void log(String o, DebugLevels level) {
        switch (level) {
            case DEBUG:
                if (debugLevel == DebugLevels.DEBUG)
                    System.out.println("DEBUG: " + o);
                break;
            case ERROR:
                if (debugLevel == DebugLevels.DEBUG || debugLevel == DebugLevels.WARN || debugLevel == DebugLevels.ERROR)
                    System.out.println("ERROR: " + o);
                break;
            case WARN:
                if (debugLevel == DebugLevels.WARN || debugLevel == DebugLevels.DEBUG)
                    System.out.println("WARN: " + o);
                break;
        }
    }

    public static void setDebug(DebugLevels debug) {
        debugLevel = debug;
    }

    protected static boolean mailReady() {
        return (MAIL_USER != null && MAIL_SENDER != null && MAIL_PASS != null && MAIL_SMTP_AUTH != null && MAIL_SMTP_PORT != null && MAIL_SMTP_HOST != null);
    }
}

