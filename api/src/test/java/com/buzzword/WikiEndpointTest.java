package com.buzzword;

import java.util.ArrayList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedConstruction;
import org.mockito.Mockito;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.MediaType;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Disabled // Unable to do unit testing on API Endpoint at the moment
public class WikiEndpointTest {

    // private MockMvc mockMvc;

    // private Authenticator mockAuth;
    // private ResourceDAO mockResourceDAO;
    // private CommentDAO mockCommentDAO;
    // private UpvoteDAO mockUpvoteDAO;
    // private FlagDAO mockFlagDAO;
    // private WikiEndpoint wikiEndpoint;

    // private Credentials managerCredentials;
    // private Credentials commenterCredentials;

    // final private String tokenStr = "eyJhbGciOiJIUzI1NiJ9.eyJsYXN0X25hbWUiOiJHcmVzd2VsbCIsImxvY2F0aW9uIjoiSmFwYW4iLCJpZCI6MzEsImRlcGFydG1lbnQiOiJJbmZvcm1hdGlvbiBUZWNobm9sb2d5IiwidGl0bGUiOiJNYW5hZ2VyIiwiZmlyc3RfbmFtZSI6IlRpbW90aGVlIiwic3ViIjoiVGltb3RoZWUgR3Jlc3dlbGwiLCJpYXQiOjE3NjIyMjQ0OTgsImV4cCI6MTc2MjIyODA5OH0.9uPEIpUtJmrfmCnsFyK3pZXRhSFyIxe5JuHmb4WSyAk";
    // final private String managerCredJSONStr = "{\"fName\": \"John\", \"lName\": \"Smith\", \"loc\": \"US\", \"id\": 10, \"dept\": \"Information Technology\", \"title\": \"Manager\"}";
    // final private String commenterCredJSONStr = "{\"fName\": \"Tadano\", \"lName\": \"Hitohito\", \"loc\": \"Japan\", \"id\": 42, \"dept\": \"Information Technology\", \"title\": \"Aide\"}";
    // final private String resourceJSONStrGood = "{\"title\": \"Title\", \"description\": \"Description\", \"url\": \"http://www.example.com\"}";
    // final private String resourceJSONStrBad = "{\"TiTlE\": 0, \"description\": null, \"url\": #FFEE8C}";
    // final private String commentJSONStrGood = "{\"contents\": \"This is a comment. How neat.\"}";

    // @BeforeEach
    // void setup() throws Exception {
    //     ObjectMapper objectMapper = new ObjectMapper();
    //     try{
    //         managerCredentials = objectMapper.readValue(managerCredJSONStr, Credentials.class);
    //         commenterCredentials = objectMapper.readValue(commenterCredJSONStr, Credentials.class);
    //     } catch (JsonProcessingException e) {
    //         System.out.println(e);
    //         throw new Exception();
    //     }
    //     mockAuth = mock(AuthenticatorImpl.class);
    //     when(mockAuth.Authenticate(any())).thenReturn(managerCredentials);
    //     mockResourceDAO = mock(ResourceDAOImpl.class);
    //     mockCommentDAO = mock(CommentDAOImpl.class);
    //     mockUpvoteDAO = mock(UpvoteDAOImpl.class);
    //     mockFlagDAO = mock(FlagDAOImpl.class);

    //     wikiEndpoint = new WikiEndpoint();

    //     this.mockMvc = MockMvcBuilders.standaloneSetup(wikiEndpoint).setControllerAdvice(new EndpointExceptionHandler()).build();
    // }


    // /*
    //  * =======================================================================================
    //  *      GET retrieveAllResources tests (medium testing)
    //  * =======================================================================================
    //  */

    // @Test
    // void testRetrieveAllResourcesGood() throws Exception {
    //     when(mockResourceDAO.listAllResources(any())).thenReturn(new ArrayList<Resource>());

    //     mockMvc.perform(get("/wiki/resource")
    //                     .contentType(MediaType.APPLICATION_JSON)
    //                     .header("Bearer", tokenStr))
    //         .andExpect(status().isOk());
    // }

    // @Test
    // void testRetrieveAllResourcesBadAuthentication() throws Exception {
    //     when(mockResourceDAO.listAllResources(any())).thenReturn(new ArrayList<Resource>());
    //     when(mockAuth.Authenticate(any())).thenThrow(new AuthenticationException());

    //     mockMvc.perform(get("/wiki/resource")
    //                         .header("Bearer", tokenStr))
    //            .andExpect(status().isUnauthorized());
    // }

    // @Test
    // void testRetrieveAllResourcesNullReturn() throws Exception {
    //     when(mockResourceDAO.listAllResources(any())).thenReturn(null);

//         mockMvc.perform(get("/wiki/resource")
//                         .contentType(MediaType.APPLICATION_JSON)
//                         .header("Bearer", tokenStr))
//             .andExpect(status().isInternalServerError());
//     }

//     /*
//      * =======================================================================================
//      *      POST addResource tests (heavy testing)
//      * =======================================================================================
//      */

//     @Test
//     void testAddResourceGood() throws Exception {
//         mockMvc.perform(post("/wiki/resource")
//                             .contentType(MediaType.APPLICATION_JSON)
//                             .content(resourceJSONStrGood)
//                             .header("Bearer", tokenStr))
//                .andExpect(status().isCreated());
//     }

//     @Test
//     void testAddResourceNullAuth() throws Exception {
//         wikiEndpoint.setAuthenticator(null);

//         mockMvc.perform(post("/wiki/resource")
//                             .contentType(MediaType.APPLICATION_JSON)
//                             .content(resourceJSONStrGood)
//                             .header("Bearer", tokenStr))
//                .andExpect(status().isInternalServerError());
//     }

//     @Test
//     void testAddResourceNullResourceDAO() throws Exception {
//         wikiEndpoint.setResourceDAO(null);

//         mockMvc.perform(post("/wiki/resource")
//                             .contentType(MediaType.APPLICATION_JSON)
//                             .content(resourceJSONStrGood)
//                             .header("Bearer", tokenStr))
//                .andExpect(status().isInternalServerError());
//     }

//     @Test
//     void testAddResourceMissingParameter() throws Exception {
//         mockMvc.perform(post("/wiki/resource")
//                             .header("Bearer", tokenStr))
//                .andExpect(status().isBadRequest());
//     }

//     @Test
//     void testAddResourceMissingHeader() throws Exception {
//         mockMvc.perform(post("/wiki/resource")
//                             .contentType(MediaType.APPLICATION_JSON)
//                             .content(resourceJSONStrGood))
//                .andExpect(status().isBadRequest());
//     }

//     @Test
//     void testAddResourceBadParameter() throws Exception {
//         mockMvc.perform(post("/wiki/resource")
//                             .contentType(MediaType.APPLICATION_JSON)
//                             .content(resourceJSONStrBad)
//                             .header("Bearer", tokenStr))
//                .andExpect(status().isBadRequest());
//     }

//     @Test
//     void testAddResourceBadHTTPBody() throws Exception {
//         mockMvc.perform(post("/wiki/resource")
//                             .contentType(MediaType.APPLICATION_JSON)
//                             .content("This is not even formatted as JSON == <should be rejected>")
//                             .header("Bearer", tokenStr))
//                .andExpect(status().isBadRequest());
//     }


//     @Test
//     void testAddResourceBadAuthentication() throws Exception {
//         when(mockAuth.Authenticate(any())).thenThrow(new AuthenticationException());
//         mockMvc.perform(post("/wiki/resource")
//                             .contentType(MediaType.APPLICATION_JSON)
//                             .content(resourceJSONStrGood)
//                             .header("Bearer", tokenStr))
//                .andExpect(status().isUnauthorized());
//     }

//     @Test
//     void testAddResourceBadAuthorization() throws Exception {
//         when(mockAuth.Authenticate(any())).thenReturn(commenterCredentials);
//         doThrow(AuthorizationException.class).when(mockResourceDAO).insertResource(eq(commenterCredentials), any());

//         mockMvc.perform(post("/wiki/resource")
//                             .contentType(MediaType.APPLICATION_JSON)
//                             .content(resourceJSONStrGood)
//                             .header("Bearer", tokenStr))
//                .andExpect(status().isForbidden());
//     }

//     @Test
//     void testAddResourceNoHandler() throws Exception {
//         mockMvc.perform(post("/wiki/resource/")
//                             .contentType(MediaType.APPLICATION_JSON)
//                             .content(resourceJSONStrGood)
//                             .header("Bearer", tokenStr))
//                .andExpect(status().isNotFound());
//     }

//     @Test
//     void testAddResourceBadMethod() throws Exception {
//         mockMvc.perform(post("/wiki/resource/1")
//                             .contentType(MediaType.APPLICATION_JSON)
//                             .content(resourceJSONStrGood)
//                             .header("Bearer", tokenStr))
//                .andExpect(status().isMethodNotAllowed());
//     }

//     @Test
//     void testAddResourceBadMediaType() throws Exception {
//         mockMvc.perform(post("/wiki/resource")
//                             .contentType(MediaType.APPLICATION_XML)
//                             .content(resourceJSONStrGood)
//                             .header("Bearer", tokenStr))
//                .andExpect(status().isUnsupportedMediaType());
//     }

    

//     /*
//      * =======================================================================================
//      *      POST addComment tests (light testing)
//      * =======================================================================================
//      */

//     @Test
//     void testAddCommentGood() throws Exception {
//         mockMvc.perform(post("/wiki/resource/1/comment")
//                             .contentType(MediaType.APPLICATION_JSON)
//                             .content(commentJSONStrGood)
//                             .header("Bearer", tokenStr))
//                .andExpect(status().isCreated());
//     }

//     @Test
//     void testAddCommentNullAuth() throws Exception {
//         wikiEndpoint.setAuthenticator(null);

//         mockMvc.perform(post("/wiki/resource/1/comment")
//                             .contentType(MediaType.APPLICATION_JSON)
//                             .content(commentJSONStrGood)
//                             .header("Bearer", tokenStr))
//                .andExpect(status().isInternalServerError());
//     }

//     @Test
//     void testAddCommentNullCommentDAO() throws Exception {
//         wikiEndpoint.setCommentDAO(null);

//         mockMvc.perform(post("/wiki/resource/1/comment")
//                             .contentType(MediaType.APPLICATION_JSON)
//                             .content(commentJSONStrGood)
//                             .header("Bearer", tokenStr))
//                .andExpect(status().isInternalServerError());
//     }

//      /*
//      * =======================================================================================
//      *      POST addUpVote tests (light testing)
//      * =======================================================================================
//      */

//     @Test
//     void testAddUpvoteGood() throws Exception {
//         mockMvc.perform(post("/wiki/resource/1/upvote")
//                             .header("Bearer", tokenStr))
//                .andExpect(status().isCreated());
//     }

//     @Test
//     void testAddUpvoteNullAuth() throws Exception {
//         wikiEndpoint.setAuthenticator(null);

//         mockMvc.perform(post("/wiki/resource/1/upvote")
//                             .header("Bearer", tokenStr))
//                .andExpect(status().isInternalServerError());
//     }

//     @Test
//     void testAddUpvoteNullUpvoteDAO() throws Exception {
//         wikiEndpoint.setUpvoteDAO(null);

//         mockMvc.perform(post("/wiki/resource/1/upvote")
//                             .header("Bearer", tokenStr))
//                .andExpect(status().isInternalServerError());
//     }

//      /*
//      * =======================================================================================
//      *      POST addReviewFlag tests (light testing)
//      * =======================================================================================
//      */

//     @Test
//     void testAddReviewFlagGood() throws Exception {
//         mockMvc.perform(post("/wiki/resource/1/reviewFlag")
//                             .contentType(MediaType.APPLICATION_JSON)
//                             .content(commentJSONStrGood)
//                             .header("Bearer", tokenStr))
//                .andExpect(status().isCreated());
//     }

//     @Test
//     void testAddReviewFlagNullAuth() throws Exception {
//         wikiEndpoint.setAuthenticator(null);

//         mockMvc.perform(post("/wiki/resource/1/reviewFlag")
//                             .contentType(MediaType.APPLICATION_JSON)
//                             .content(commentJSONStrGood)
//                             .header("Bearer", tokenStr))
//                .andExpect(status().isInternalServerError());
//     }

//     @Test
//     void testAddReviewFlagNullFlagDAO() throws Exception {
//         wikiEndpoint.setFlagDAO(null);

//         mockMvc.perform(post("/wiki/resource/1/reviewFlag")
//                             .contentType(MediaType.APPLICATION_JSON)
//                             .content(commentJSONStrGood)
//                             .header("Bearer", tokenStr))
//                .andExpect(status().isInternalServerError());
//     }

//      /*
//      * =======================================================================================
//      *      DELETE removeResource tests (light testing)
//      * =======================================================================================
//      */

//     @Test
//     void testRemoveResourceGood() throws Exception {
//         mockMvc.perform(delete("/wiki/resource/1")
//                             .header("Bearer", tokenStr))
//                .andExpect(status().isOk());
//     }

//      /*
//      * =======================================================================================
//      *      DELETE removeComment tests (light testing)
//      * =======================================================================================
//      */

//     @Test
//     void testRemoveCommentGood() throws Exception {
//         mockMvc.perform(delete("/wiki/resource/1/comment/1")
//                             .header("Bearer", tokenStr))
//                .andExpect(status().isOk());
//     }

//      /*
//      * =======================================================================================
//      *      DELETE removeUpvote tests (light testing)
//      * =======================================================================================
//      */

//     @Test
//     void testRemoveUpvoteGood() throws Exception {
//         mockMvc.perform(delete("/wiki/resource/1/upvote/1")
//                             .header("Bearer", tokenStr))
//                .andExpect(status().isOk());
//     }

//      /*
//      * =======================================================================================
//      *      DELETE removeReviewFlag tests (heavy testing)
//      * =======================================================================================
//      */

//     @Test
//     void testRemoveReviewFlagGood() throws Exception {
//         mockMvc.perform(delete("/wiki/resource/1/reviewFlag/1")
//                             .header("Bearer", tokenStr))
//                .andExpect(status().isOk());
//     }

//     @Test
//     void testRemoveReviewFlagMissingParameter() throws Exception {
//         mockMvc.perform(delete("/wiki/resource/ /reviewFlag/ ")
//                             .header("Bearer", tokenStr))
//                .andExpect(status().isBadRequest());
//     }

//     @Test
//     void testRemoveReviewFlagMissingHeader() throws Exception {
//         mockMvc.perform(delete("/wiki/resource/1/reviewFlag/1"))
//                .andExpect(status().isBadRequest());
//     }

//     @Test
//     void testRemoveReviewFlagBadParameter() throws Exception {
//         mockMvc.perform(delete("/wiki/resource/1/reviewFlag/1a")
//                             .header("Bearer", tokenStr))
//                .andExpect(status().isBadRequest());
//     }

//     @Test
//     void testRemoveReviewFlagBadAuthentication() throws Exception {
//         when(mockAuth.Authenticate(any())).thenThrow(new AuthenticationException());
//         mockMvc.perform(delete("/wiki/resource/1/reviewFlag/1")
//                             .header("Bearer", tokenStr))
//                .andExpect(status().isUnauthorized());
//     }

//     @Test
//     void testRemoveReviewFlagBadAuthorization() throws Exception {
//         when(mockAuth.Authenticate(any())).thenReturn(commenterCredentials);
//         doThrow(AuthorizationException.class).when(mockFlagDAO).removeReviewFlag(commenterCredentials, 2, 1);

//         mockMvc.perform(delete("/wiki/resource/1/reviewFlag/2")
//                             .header("Bearer", tokenStr))
//                .andExpect(status().isForbidden());
//     }

//     @Test
//     void testRemoveReviewFlagNoHandler() throws Exception {
//         mockMvc.perform(delete("/wiki/resource/1/reviewFlag/1/")
//                             .header("Bearer", tokenStr))
//                .andExpect(status().isNotFound());
//     }

//     @Test
//     void testRemoveReviewFlagBadMethod() throws Exception {
//         mockMvc.perform(delete("/wiki/resource/1/reviewFlag")
//                             .header("Bearer", tokenStr))
//                .andExpect(status().isMethodNotAllowed());
//     }
}
