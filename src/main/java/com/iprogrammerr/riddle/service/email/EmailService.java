package com.iprogrammerr.riddle.service.email;

import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import com.iprogrammerr.riddle.exception.email.EmailException;

public class EmailService {

    private static final String SIGN_UP_EMAIL_SUBJECT = "Sign Up";
    private static final String SIGN_UP_EMAIL_TEXT = "Congratulations, You have signed up! To activate your account click link:";
    private String adminEmail;
    private String adminEmailPassword;
    private Properties properties;

    public EmailService(String adminEmail, String adminEmailPassword, String smtpHost, int smtpPort) {
	this.adminEmail = adminEmail;
	this.adminEmailPassword = adminEmailPassword;
	properties = new Properties();
	properties.put("mail.smtp.auth", true);
	properties.put("mail.smtp.starttls.enable", true);
	properties.put("mail.smtp.host", smtpHost);
	properties.put("mail.stmp.port", String.valueOf(smtpPort));
    }

    public void sendSignUpEmail(String recipentEmail, String activatingLink) {
	String text = SIGN_UP_EMAIL_TEXT + activatingLink;
	sendEmail(recipentEmail, SIGN_UP_EMAIL_SUBJECT, text);
    }

    private void sendEmail(String recipentEmail, String subject, String text) {
	Session session = Session.getInstance(properties, new Authenticator() {
	    @Override
	    protected PasswordAuthentication getPasswordAuthentication() {
		return new PasswordAuthentication(adminEmail, adminEmailPassword);
	    }
	});
	try {
	    Message message = new MimeMessage(session);
	    message.setFrom(new InternetAddress(adminEmail));
	    message.setRecipient(Message.RecipientType.TO, new InternetAddress(recipentEmail));
	    message.setSubject(subject);
	    MimeBodyPart mimeBodyPart = new MimeBodyPart();
	    mimeBodyPart.setContent(text, "text/html");
	    Multipart multipart = new MimeMultipart();
	    multipart.addBodyPart(mimeBodyPart);
	    message.setContent(multipart);
	    Transport.send(message);
	} catch (Exception exception) {
	    exception.printStackTrace();
	    throw new EmailException(exception);
	}
    }

}
