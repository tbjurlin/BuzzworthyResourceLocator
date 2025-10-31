package com.buzzword;

import org.jsoup.safety.Safelist;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class XssSanitizerTest {
    private XssSanitizer testXssSanitizer;

    @BeforeEach
    public void setUp() throws Exception
    {
        testXssSanitizer = new XssSanitizerImpl();
    }

    @Test
	public void testNullString()
	{
		assertThrows(IllegalArgumentException.class, () ->
			testXssSanitizer.sanitizeInput(null));
	}

	@Test
	public void testInvalidXssString()
	{
		String bad = "Some data <script>alert()</script> more data ";
		String expected = "Some data more data";
		String actual = testXssSanitizer.sanitizeInput(bad);
		assertEquals(expected, actual);
	}

	@Test
	public void testValidXssString()
	{
		String good = "Some data more data";
		String expected = "Some data more data";
		String actual = testXssSanitizer.sanitizeInput(good);
		assertEquals(expected, actual);
	}

	@Test
	public void testDefaultRules()
	{
		Safelist defaultRules = testXssSanitizer.getRules();
		assertNotNull(defaultRules);
	}

	@Test
	public void testNullRules()
	{
		assertThrows(IllegalArgumentException.class, () ->
			testXssSanitizer.setRules(null));
	}
		
	@Test
	public void testAlternateRules()
	{
		testXssSanitizer.setRules(Safelist.relaxed());
		String data = "Some data <b>more</b> data";
		String expected = "Some data <b>more</b> data";
		String actual = testXssSanitizer.sanitizeInput(data);
		assertEquals(expected, actual);
	}
	
	@Test
	public void testNullEncodedString()
	{
		assertThrows(IllegalArgumentException.class, () ->
			testXssSanitizer.sanitizeOutput(null));
	}

	
	@Test
	public void testEncodedOutput()
	{
		String data = "Some data <b>more</b> data";
		String expected = "Some data &lt;b&gt;more&lt;/b&gt; data";
		String actual = testXssSanitizer.sanitizeOutput(data);
		assertEquals(expected, actual);
	}
	
	@Test
	public void testUnEncodedOutput()
	{
		String data = "Some data more data";
		String expected = "Some data more data";
		String actual = testXssSanitizer.sanitizeOutput(data);
		assertEquals(expected, actual);
	}
}
