package com.buzzword;

/*
 * This is free and unencumbered software released into the public domain.
 * Anyone is free to copy, modify, publish, use, compile, sell, or distribute this software,
 * either in source code form or as a compiled binary, for any purpose, commercial or
 * non-commercial, and by any means.
 *
 * In jurisdictions that recognize copyright laws, the author or authors of this
 * software dedicate any and all copyright interest in the software to the public domain.
 * We make this dedication for the benefit of the public at large and to the detriment of
 * our heirs and successors. We intend this dedication to be an overt act of relinquishment in
 * perpetuity of all present and future rights to this software under copyright law.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR
 * PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS BE LIABLE FOR ANY CLAIM, DAMAGES
 * OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 *
 * For more information, please refer to: https://unlicense.org/
*/

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

	@Test
	public void testSanitizeTrimsInput()
	{
		String bad = "   Some data <script>alert()</script> more data   ";
		String expected = "Some data more data";
		String actual = testXssSanitizer.sanitizeInput(bad);
		assertEquals(expected, actual);
	}

	@Test
	public void testWhitespaceOnlyInput()
	{
		String onlySpaces = "    ";
		String expected = ""; // Jsoup.clean on whitespace-only returns empty, then trimmed
		String actual = testXssSanitizer.sanitizeInput(onlySpaces);
		assertEquals(expected, actual);
	}

	@Test
	public void testSanitizeOutputTrims()
	{
		String data = "   Some data <b>more</b> data   ";
		String expected = "Some data &lt;b&gt;more&lt;/b&gt; data"; // trimmed and HTML-escaped
		String actual = testXssSanitizer.sanitizeOutput(data);
		assertEquals(expected, actual);
	}
}
