package com.iprogrammerr.riddle.service.email;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import com.iprogrammerr.riddle.exception.EmailException;

public class EmailService {

    private static final String ADMIN_EMAIL = "ceigor94@gmail.com";
    private static final String SIGN_UP_EMAIL_SUBJECT = "Sign Up";
    private Properties properties;

    public EmailService() {
	properties = new Properties();
	properties.put("mail.smtp.auth", true);
	properties.put("mail.smtp.starttls.enable", true);
	properties.put("mail.smtp.host", "smtp.mailtrap.io");
	properties.put("mail.stmp.port", "25");
	properties.put("mail.smtp.ssl.trust", "smtp.mailtrap.io");
	properties.put("mail.debug", "true");
    }

    public void sendSignUpEmail(String recipentEmail, String activatingLink) {
	Session session = Session
		.getInstance(properties);/*
					  * Session.getInstance(properties, new Authenticator() {
					  * 
					  * @Override protected PasswordAuthentication getPasswordAuthentication() {
					  * return new PasswordAuthentication(username, password); } });
					  */
	try {
	    Message message = new MimeMessage(session);
	    message.setFrom(new InternetAddress(ADMIN_EMAIL));
	    message.setRecipient(Message.RecipientType.TO, new InternetAddress(recipentEmail));
	    message.setSubject(SIGN_UP_EMAIL_SUBJECT);
	    String text = "Congratulations, You hava signed up! To activate your account click link: " + activatingLink;
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
