package src;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import javax.servlet.http.*;
import java.util.Properties; 
import javax.mail.Session; 
import javax.mail.internet.MimeMessage; 
import javax.mail.Address;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;

/**
 * This class receives email messages that are sent to <any string>@elevatorspot.appspotmail.com and
 * sends response message to sender.
 * @author johannes
 *
 */

@SuppressWarnings("serial")
public class MailUIServlet extends HttpServlet {

	public void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {

		Properties props = new Properties(); 
		Session session = Session.getDefaultInstance(props, null); 

		Address recipient = null;
		try {
			MimeMessage messageObject = new MimeMessage(session, req.getInputStream());
			recipient = messageObject.getFrom()[0]; //Haetaan ensimmainen lähettaja
		} catch (MessagingException e) {
			e.printStackTrace();
		}
		System.out.println(recipient.toString());
		sendMail(recipient);
	}

	public void sendMail(Address recipient) {
		Properties props = new Properties();
		Session session = Session.getDefaultInstance(props, null);

		String msgSubject = "Testing elevatorspotting E-mail client";
		String msgBody = "Testing elevatorspotting E-mail client.";

		try {
			InternetAddress recipientAdress = new InternetAddress(recipient.toString());
			Message msg = new MimeMessage(session);
			msg.setFrom(new InternetAddress("test@elevatorspot.appspotmail.com", "TEST"));      
			msg.addRecipient(Message.RecipientType.TO, recipientAdress);
			msg.setSubject(msgSubject);
			msg.setText(msgBody);
			Transport.send(msg);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
