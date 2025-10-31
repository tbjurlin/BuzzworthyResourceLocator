/**
 *This is the utility class to sanitize String against XSS attacks.
 * 
 * @author Janniebeth Melendez
 * @version 1.0
 */
package com.buzzword;

import org.jsoup.Jsoup;
import org.jsoup.safety.Safelist;
import org.apache.commons.text.StringEscapeUtils;

public class XssSanitizerImpl
    implements XssSanitizer {
    
    private final Logger logger = LoggerFactory.getSecurityLogger();

    private Safelist rules = null;

    public XssSanitizerImpl()
    {
        logger.debug("Starting constructor");
        Safelist defaultRules = Safelist.none();
        logger.trace("Setting default rules to Safelist.none");
        setRules(defaultRules);
    }

    /**
     *{@inheritDoc}
	 */
	@Override
	public Safelist getRules()
	{
		logger.debug("Returnung the current rules: " + rules.toString());
		return rules;
	}

    /**
	 * {@inheritDoc}
	 */
	@Override
	public void setRules(final Safelist rulesIn)
	{
		logger.debug("Setting the rules");
		if (rulesIn == null)
		{
			logger.error("Attempt to set the rules to null");
			throw new IllegalArgumentException("The rules must be provided");
		}
		logger.trace("New rules are: " + rulesIn.toString());
		this.rules = rulesIn;
	}	

    /**
     * {@inheritDoc}
     */
    @Override
    public String sanitizeInput(final String input)
    {
        logger.debug("Sanitizing Input");
        if (input == null)
        {
            logger.error("Attempt to sanitize null string");
            throw new IllegalArgumentException("Input required");
        }

        logger.trace("Pre sanitized string: " + sanitizeOutput(input));
        String results = Jsoup.clean(input, rules);
        logger.trace("Post sanitized string: " + results);
        return results.trim();
    }

    /**
     * {@inheritDoc}
     */
    public String sanitizeOutput(final String input)
    {
        logger.debug("Sanitizing output");
        if(input == null)
        {
            logger.error("Attempt to sanitize null string");
            throw new IllegalArgumentException("Input required");
        }

        String temp = input.trim();
        return StringEscapeUtils.escapeHtml4(temp);
    }
   
}
