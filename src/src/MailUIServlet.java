package src;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import javax.servlet.http.*;

import java.util.List;
import java.util.Properties; 
import javax.mail.Session; 
import javax.mail.internet.MimeMessage; 
import javax.mail.Address;
import javax.mail.Message;
import javax.mail.Message.RecipientType;
import javax.mail.MessagingException;
import javax.mail.Multipart;
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
	
	private static boolean PRODUCTION_MODE = false;

	public void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {

		MimeMessage inboundMsg = handleIncomingMessage(req);
		
		sendMailtoConsole(inboundMsg);
		
		InboundMessageParser msgParser = new InboundMessageParser();
		
		Address recipient = null;
		List<String> arguments = null;
		
		try {			
			recipient = msgParser.parseSender(inboundMsg);
			arguments = msgParser.parseContent(inboundMsg);
		} catch (MessagingException e) {
			e.printStackTrace();
		}
		
		ReturnMessageAssembler msgAssembler = new ReturnMessageAssembler();
		String msgBody = msgAssembler.generateMessageContent(arguments);
		String msgSubject = msgAssembler.generateSubject();
		Message msg = msgAssembler.assembleMessage(recipient, msgSubject, msgBody);
		
		if(PRODUCTION_MODE) {
			sendMail(msg);
		} else {
			sendMailtoConsole(msg);
		}
	}
	
	private MimeMessage handleIncomingMessage(HttpServletRequest req) {
		Properties props = new Properties(); 
		Session session = Session.getDefaultInstance(props, null);
		
		MimeMessage inboundMsg = null;
		try {
			inboundMsg = new MimeMessage(session, req.getInputStream());
		} catch (MessagingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return inboundMsg;
	}
	
	private void sendMail(Message msg) {
		try {
			Transport.send(msg);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void sendMailtoConsole(Message msg) {
		 try {
			System.out.println("TO: " + msg.getRecipients(RecipientType.TO)[0].toString());
			System.out.println("FROM: " + msg.getFrom()[0].toString());
			System.out.println("SUBJECT: " + msg.getSubject());
			System.out.println("CONTENT TYPE: " + msg.getContentType());
			System.out.println();
			if(msg.isMimeType("multipart/alternative")) {
				Multipart content = (Multipart)msg.getContent();
				String contString = content.getBodyPart(0).getContent().toString();
				System.out.println(contString);
			} else {
			System.out.println(msg.getContent());
			}
			System.out.println();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
