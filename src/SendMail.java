import java.util.*;
import javax.mail.*;
import javax.mail.internet.*;

public class SendMail {
	private String mailTo;
	private String subject;
	private String content;

	public SendMail(String mailTo, String subject, String content) {
		this.mailTo = mailTo;
		this.subject = subject;
		this.content = content;
		send();
	}

	private void send() {
		String username = "username";		// MODIFY HERE
		String password = "password";		// MODIFY HERE

		// Sender's email ID needs to be mentioned
		String from = "mail@example.com";	// MODIFY HERE

		// Assuming you are sending email from localhost
		String host = "mail.host.com";		// MODIFY HERE

		// Get system properties
		Properties props = System.getProperties();
		props.put("mail.smtp.host", "MODIFY_ME");	// MODIFY HERE
		props.put("mail.smtp.port", "25"); // Standard: 25
		props.put("mail.debug", "false");
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable","true");
		props.put("mail.smtp.EnableSSL.enable","true");

		props.setProperty("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
		props.setProperty("mail.smtp.socketFactory.fallback", "false");
		props.setProperty("mail.smtp.port", "465");
		props.setProperty("mail.smtp.socketFactory.port", "465");

		// Setup mail server
		props.setProperty("mail.smtp.host", host);

		// Get the default Session object.
		Session session = Session.getInstance(props, new GMailAuthenticator(username, password));

		try {
			// Create a default MimeMessage object.
			MimeMessage message = new MimeMessage(session);

			// Set From: header field of the header.
			message.setFrom(new InternetAddress(from));

			// Set To: header field of the header.
			message.addRecipient(Message.RecipientType.TO, new InternetAddress(mailTo));

			// Set Subject: header field
			message.setSubject(subject);

			// Now set the actual message
			message.setContent(content, "text/html");

			// Send message
			Transport.send(message);
			System.out.println("Sent message successfully to "+mailTo+"...");
		} catch (MessagingException mex) {
			mex.printStackTrace();
		}
	}
}