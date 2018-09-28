package com.iprogrammerr.riddle.email;

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

public final class RiddleEmailServer implements EmailServer {

    private final String adminEmail;
    private final String adminEmailPassword;
    private final Properties properties;

    public RiddleEmailServer(String adminEmail, String adminEmailPassword, String smtpHost, int smtpPort) {
	this.adminEmail = adminEmail;
	this.adminEmailPassword = adminEmailPassword;
	properties = new Properties();
	properties.put("mail.smtp.auth", true);
	properties.put("mail.smtp.starttls.enable", true);
	properties.put("mail.smtp.host", smtpHost);
	properties.put("mail.stmp.port", String.valueOf(smtpPort));
    }

    @Override
    public void send(Email email) throws Exception {
	Session session = Session.getInstance(properties, new Authenticator() {
	    @Override
	    protected PasswordAuthentication getPasswordAuthentication() {
		return new PasswordAuthentication(adminEmail, adminEmailPassword);
	    }
	});
	Message message = new MimeMessage(session);
	message.setFrom(new InternetAddress(adminEmail));
	message.setRecipient(Message.RecipientType.TO, new InternetAddress(email.recipent()));
	message.setSubject(email.subject());
	MimeBodyPart mimeBodyPart = new MimeBodyPart();
	mimeBodyPart.setContent(email.text(), "text/html");
	Multipart multipart = new MimeMultipart();
	multipart.addBodyPart(mimeBodyPart);
	message.setContent(multipart);
	Transport.send(message);
    }

    @Override
    public void sendSigningUp(String recipent, String activatingLink) throws Exception {
	send(new SignUpEmail(adminEmail, recipent, activatingLink));
    }

}
