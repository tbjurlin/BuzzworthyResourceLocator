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

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import java.io.IOException;

/**
 * Servlet filter that redirects all HTTP requests to HTTPS.
 * <p>
 * This filter intercepts every incoming request and redirects any 
 * HTTP request to an equivalent HTTPS reqest.
 * 
 * @author Ben Edens
 * @version 1.0
 */
@Component
public class HttpsRedirectFilter implements Filter {

	// Default HTTPS port 8443 for development
    @Value("${server.port:8443}")
	private int httpsPort;

	/**
	 * Initializes the filter.
	 * <p>
	 * No initialization is needed for this filter.
	 * 
	 * @param filterConfig the filter configuration
	 * @throws ServletException if an error occurs during initialization
	 */
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {}

	/**
	 * Filters incoming requests and redirects HTTP to HTTPS.
	 * <p>
	 * If the request is not secure (HTTP), it constructs an HTTPS URL
	 * and sends a 301 (Moved Permanently) redirect response for browser-side caching.
	 * If the request is already secure (HTTPS), it continues the filter chain.
	 * 
	 * @param request the servlet request
	 * @param response the servlet response
	 * @param chain the filter chain
	 * @throws IOException if an I/O error occurs during filtering
	 * @throws ServletException if a servlet error occurs during filtering
	 */
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		// Filter takes generic servlet requests/responses that need to be converted to HTTP requests/responses
		HttpServletRequest httpRequest = (HttpServletRequest) request;
		HttpServletResponse httpResponse = (HttpServletResponse) response;
		// If the request is not secure (HTTP), redirect to HTTPS equivalent request
		if(!httpRequest.isSecure()) {
			String httpsURL = buildHttpsUrl(httpRequest);
			// Use 308 Permanent Redirect to preserve HTTP method and request body
			// This is crucial for POST/PUT/PATCH requests to work correctly after redirect
			httpResponse.setStatus(308);
			httpResponse.setHeader("Location", httpsURL);
			return;
		}
		// Else continue with any other filters
		else {
			chain.doFilter(request, response);
			return;
		}
	}

	/**
	 * Cleans up resources when the filter is destroyed.
	 * <p>
	 * No cleanup is needed for this filter.
	 */
	@Override
	public void destroy() {}

	/**
	 * Builds an HTTPS URL from an HTTP request.
	 * <p>
	 * Recreates the HTTP request URL using the HTTPS scheme.
	 * Only includes the port if it is not the default HTTPS port (443).
	 *  
	 * @param request the HTTP request
	 * @return the equivalent HTTPS URL as a String
	 */
	private String buildHttpsUrl(HttpServletRequest request) {
		// Build HTTPS URL of form https://[server name][:port][URI][?query]
		StringBuilder httpsUrl = new StringBuilder();
		httpsUrl.append("https://");
		httpsUrl.append(request.getServerName());
		// Port only needed if not default 443 for HTTPS
		if(httpsPort != 443) {
			httpsUrl.append(":").append(httpsPort);
		}
		httpsUrl.append(request.getRequestURI());
		String queryString = request.getQueryString();
		if (queryString != null) {
			httpsUrl.append("?").append(queryString);
		}
		return httpsUrl.toString();
	}
}
