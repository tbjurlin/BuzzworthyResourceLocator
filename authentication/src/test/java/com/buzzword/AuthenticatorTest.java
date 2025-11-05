package com.buzzword;


import org.mockserver.integration.ClientAndServer;
import org.mockserver.client.MockServerClient;
import static org.mockserver.integration.ClientAndServer.startClientAndServer;
import static org.mockserver.model.HttpRequest.request;
import static org.mockserver.model.HttpResponse.response;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Nested;

public class AuthenticatorTest {

    private Authenticator auth;

    @BeforeEach
    void setup() {
        auth = null;
    }

    @Test
    void ConstructorTestGood() throws Exception {
        auth = new AuthenticatorImpl("http://www.validurl.com");
    }

    @Test
    void ConstructorTestBadURISyntax() throws Exception {

        AuthenticationException e = assertThrows(AuthenticationException.class, 
                    () -> auth = new AuthenticatorImpl("{http://www.invaliduri}<script></script>"));

        System.out.println(e.getMessage());
    }

    @Test
    void ConstructorTestMalformedURL() throws Exception {

        AuthenticationException e = assertThrows(AuthenticationException.class, 
                    () -> auth = new AuthenticatorImpl("urn:isbn:0-330-25864-8"));

        System.out.println(e.getMessage());
    }

    @Nested
    class AuthenticatorAuthenticateMethodTest {

        private ClientAndServer mockAuthServer;
        private final String mockAuthServerURL = "http://localhost:1080/auth-service/api/auth/verify";
        private final String mockAuthServerURLPath = "/auth-service/api/auth/verify";
        private MockServerClient mockServerClient;
        private Token testToken;
        private final String testTokenStr = "eyJhbGciOiJIUzI1NiJ9.eyJsYXN0X25hbWUiOiJHcmVzd2VsbCIsImxvY2F0aW9uIjoiSmFwYW4iLCJpZCI6MzEsImRlcGFydG1lbnQiOiJJbmZvcm1hdGlvbiBUZWNobm9sb2d5IiwidGl0bGUiOiJNYW5hZ2VyIiwiZmlyc3RfbmFtZSI6IlRpbW90aGVlIiwic3ViIjoiVGltb3RoZWUgR3Jlc3dlbGwiLCJpYXQiOjE3NjIyMjQ0OTgsImV4cCI6MTc2MjIyODA5OH0.9uPEIpUtJmrfmCnsFyK3pZXRhSFyIxe5JuHmb4WSyAk";
        final private String managerCredJSONStr = "{\"fName\": \"John\", \"lName\": \"Smith\", \"loc\": \"US\", \"id\": 10, \"dept\": \"Information Technology\", \"title\": \"Manager\"}";
    
        @BeforeEach
        void setup() {
            testToken = new Token();
            testToken.setToken(testTokenStr);

            mockAuthServer = startClientAndServer(1080);

            mockServerClient = new MockServerClient("localhost", 1080);
        }

        @AfterEach
        void teardown() {
            mockAuthServer.stop();
        }

        @Test
        void AuthenticateTestGood() throws Exception {
            mockServerClient.when(request()
                                    .withMethod("POST")
                                    .withPath(mockAuthServerURLPath)
                                    .withBody("{\"token\": \""+ testToken.getToken() +"\"}")
                                )
                        .respond(response()
                                    .withStatusCode(201)
                                    .withBody(managerCredJSONStr)
                                );
            
            auth = new AuthenticatorImpl(mockAuthServerURL);

            assertNotEquals(auth.Authenticate(testToken), null);
        }

        @Test
        void AuthenticateTestBadServerResponseCode() throws Exception {
            mockServerClient.when(request()
                                    .withMethod("POST")
                                    .withPath(mockAuthServerURLPath)
                                    .withBody("{\"token\": \""+ testToken.getToken() +"\"}")
                                )
                        .respond(response()
                                    .withStatusCode(404)
                                );

            auth = new AuthenticatorImpl(mockAuthServerURL);

            AuthenticationException e = assertThrows(AuthenticationException.class, 
                      () -> auth.Authenticate(testToken));

            System.out.println(e.getMessage());
        }

        @Test
        void AuthenticateTestNullToken() throws Exception {
            mockServerClient.when(request()
                                    .withMethod("POST")
                                    .withPath(mockAuthServerURLPath)
                                    .withBody("{\"token\": \""+ testToken.getToken() +"\"}")
                                )
                        .respond(response()
                                    .withStatusCode(201)
                                    .withBody(managerCredJSONStr)
                                );

            auth = new AuthenticatorImpl(mockAuthServerURL);

            AuthenticationException e = assertThrows(AuthenticationException.class, 
                      () -> auth.Authenticate(null));

            System.out.println(e.getMessage());
        }

        @Test
        void AuthenticateTestNoServerResponseBody() throws Exception {
            mockServerClient.when(request()
                                    .withMethod("POST")
                                    .withPath(mockAuthServerURLPath)
                                    .withBody("{\"token\": \""+ testToken.getToken() +"\"}")
                                )
                        .respond(response()
                                    .withStatusCode(201)
                                );

            auth = new AuthenticatorImpl(mockAuthServerURL);

            AuthenticationException e = assertThrows(AuthenticationException.class, 
                      () -> auth.Authenticate(testToken));

            System.out.println(e.getMessage());
        }

    }

}
