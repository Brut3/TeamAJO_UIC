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
		while(count(paramString, '#') < 8) {
			paramString = paramString.concat("#");
		}
		String paramString2 = paramString.replace("#", " # ");	
		List<String> params = Arrays.asList(paramString2.split("#"));
		ArrayList<String> params2 = new ArrayList<String>();
		
		for(int i = 0; i < 8; i++) {
			params2.add(params.get(i).trim());
		}

		System.out.println(params2);
		System.out.println("SIZE: " + params2.size());

		return params2;
	}
	private static int count(String sourceString, char lookFor) {
	    if (sourceString == null) {
	        return -1;
	    }
	    int count = 0;
	    for (int i = 0; i < sourceString.length(); i++) {
	        final char c = sourceString.charAt(i);
	        if (c == lookFor) {
	            count++;
	        }
	    }
	    return count;
	}
}
