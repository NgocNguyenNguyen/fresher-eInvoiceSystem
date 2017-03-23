package csc.config;

import java.util.List;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SendMail {
	private static final Logger log = LoggerFactory.getLogger(SendMail.class);

	public static void sendMailSMTP(String username, String password, List<String> to) {

		// String[] to = { "hailt09111994@gmail.com",
		// // "quoctc11@gmail.com",
		// // "letuananhuit@gmail.com",
		// // "ngocnguyen552015@gmail.com",
		// // "nqthai95@gmail.com"
		// };
		// String from = "nguyenvanantest123@gmail.com";

		Properties props = new Properties();
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.host", "smtp.gmail.com");
		props.put("mail.smtp.port", "587");

		Session session = Session.getInstance(props, new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(username, password);
			}
		});

		try {
			session.setDebug(true);
			Message message = new MimeMessage(session);

			InternetAddress[] addressTo = new InternetAddress[to.size()];
			for (int i = 0; i < to.size(); i++) {
				addressTo[i] = new InternetAddress(to.get(i));
			}

			message.setFrom(new InternetAddress(username));
			// type and address to send mail
			message.setRecipients(Message.RecipientType.BCC, addressTo);
			// email subject
			message.setSubject("[Limit Grand Total][E-Invoice System]");
			// content email
			message.setText("Dear customer," + "\n\n Your account are over limit grand total."
					+ "\n\n No spam to my email, please!");
			// send mail
			Transport.send(message);

			log.info("sendMailSMTP Done");

		} catch (MessagingException e) {
			throw new RuntimeException(e);
		}
	}
}
