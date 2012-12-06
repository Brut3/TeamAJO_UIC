package src;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.mail.Message;
import javax.mail.Address;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.internet.ContentType;

public class InboundMessageParser {

	public Address parseSender(Message msg) throws MessagingException {
		return msg.getFrom()[0];
	}

	public List<String> parseContent(Message msg) throws MessagingException, IOException {
		List<String> msgParams = new ArrayList<String>();
		//if(msg.getContentType().equals("text/plain")) {
		Multipart content = (Multipart)msg.getContent();
		msgParams = parseParameters(content.getBodyPart(0).getContent().toString());
		//}
		return msgParams;
	}

	private List<String> parseParameters(String paramString) {
		return Arrays.asList(paramString.split("#"));
	}
}
