package src;

import java.util.List;
import java.util.Properties;

import javax.mail.Address;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class ReturnMessageAssembler {

	protected static String SENDER_ADDRESS = "test@elevatorspot.appspotmail.com";
	
	public Message assembleMessage(Address recipient, String msgSubject, String msgBody) {
		Properties props = new Properties();
		Session session = Session.getDefaultInstance(props, null);
		
		Message msg = null;
		
		try {
			InternetAddress recipientAdress = new InternetAddress(recipient.toString());
			msg = new MimeMessage(session);
			msg.setFrom(new InternetAddress(SENDER_ADDRESS, "TEST"));      
			msg.addRecipient(Message.RecipientType.TO, recipientAdress);
			msg.setSubject(msgSubject);
			msg.setText(msgBody);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return msg;
	}

	public String generateMessageContent(List<String> params) {

		//TODO Tanne voi lisailla parametrien kasittelyyn liittyvaa logiikkaa.
		
		String content = "";
		
		for(String param : params) {
			content += param + "\n";
		}
		return content;
	}
	
	public String generateSubject() {
		return "Testing elevatorspotting E-mail client";
	}
}
