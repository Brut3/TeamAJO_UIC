package src;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class ReturnMessageAssemblerTest {
	
	ReturnMessageAssembler assembler;
	
	@Before
	public void setUp() throws Exception {
		assembler = new ReturnMessageAssembler();
	}

	@Test
	public void testAssembleMessage() {
		fail("Not yet implemented");
	}

	@Test
	public void testGenerateMessageContent() {
		List<String> params = new ArrayList<String>();
		params.add("p1");
		params.add("p2");
		params.add("p3");
		
		String result = this.assembler.generateMessageContent(params);
		assertEquals("p1\np2\np3\n", result);	
	}

	@Test
	public void testGenerateSubject() {
		fail("Not yet implemented");
	}

}
