package farm.chaos.ppfax.utils;

import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import com.google.apphosting.api.ApiProxy;

public class EmailService {

	private static final Logger LOG = Logger.getLogger(EmailService.class.getName());

	public static void sendEmail(String message, String address) {

		Properties props = new Properties();
		Session session = Session.getDefaultInstance(props, null);

		try {
			Message msg = new MimeMessage(session);

			String htmlBody = "<html><body><h1>FLOW Cron Manager Error Report</h1>"
					+ "<dl><dt>Message</dt><dd>" + message + "</dd>"
					+ "</dl></body></html>";
			Multipart mp = new MimeMultipart();

			MimeBodyPart htmlPart = new MimeBodyPart();
			htmlPart.setContent(htmlBody, "text/html");
			mp.addBodyPart(htmlPart);

			msg.setContent(mp);

			ApiProxy.Environment env = ApiProxy.getCurrentEnvironment();
			String appid = env.getAttributes().get("com.google.appengine.runtime.default_version_hostname").toString();
			int p = appid.indexOf('.');
			if (p > 0)
				appid = appid.substring(0, p);
	        LOG.log(Level.FINE, "APP ID " + appid);

			msg.setFrom(new InternetAddress("no-reply@" + appid + ".appspotmail.com", "FLOW Cron Manager"));
			msg.addRecipient(Message.RecipientType.TO, new InternetAddress(address));
			msg.setSubject("Notification");
			Transport.send(msg);
			LOG.log(Level.INFO, "Email sent to " + address);

		} catch (Exception e) {
			LOG.log(Level.SEVERE, "Failed to send email: " + e.getMessage(), e);
		}
	}

}
