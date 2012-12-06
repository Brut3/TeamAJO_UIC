package src;

import static org.junit.Assert.*;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.mail.Address;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.junit.Before;
import org.junit.Test;

public class InboudMessageParserTest {
	
	Message msg;
	
	@Before
	public void setUp() throws Exception {
		Properties props = new Properties();
		Session session = Session.getDefaultInstance(props, null);
		this.msg = new MimeMessage(session);
	}

	@Test
	public void testParseSender() {
		
		try {
			this.msg.setFrom(new InternetAddress("test@mail.com"));
		} catch (Exception e) {			
		}
		
		InboundMessageParser parser = new InboundMessageParser();
		Address result = null;
		try {
			result = parser.parseSender(this.msg);
		} catch (MessagingException e) {
		}
		assertEquals(result.toString(), "test@mail.com");
	}

	@Test
	public void testParseContent() {

		try {
			msg.setContent("Arg1#Arg2#Arg3", "text/plain");
		} catch (MessagingException e) {
			e.printStackTrace();
		}
		InboundMessageParser parser = new InboundMessageParser();
		List<String> result = new ArrayList<String>();
		try {
			result = parser.parseContent(this.msg);
		} catch (Exception e) {
			e.printStackTrace();
		}
		assertEquals(result.get(0), "Arg1");
		assertEquals(result.get(1), "Arg2");
		assertEquals(result.get(2), "Arg3");
	}

}
