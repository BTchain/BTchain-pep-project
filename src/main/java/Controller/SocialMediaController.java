package Controller;

import java.util.List; 

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import DAO.SocialMediaAccountDAO;
import Model.Account;
import Model.Message;
import Service.SocialMediaService;
import io.javalin.Javalin;
import io.javalin.http.Context; 

/**
 * TODO: You will need to write your own endpoints and handlers for your
 * controller. The endpoints you will need can be
 * found in readme.md as well as the test cases. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a
 * controller may be built.
 */
public class SocialMediaController {
    /**
     * In order for the test cases to work, you will need to write the endpoints in
     * the startAPI() method, as the test
     * suite must receive a Javalin object from this method.
     * 
     * @return a Javalin app object which defines the behavior of the Javalin
     *         controller.
     */
    SocialMediaService socialMediaService;

    public SocialMediaController() {
        socialMediaService = new SocialMediaService();
    }

    public Javalin startAPI() {
        Javalin app = Javalin.create();
        app.post("/register", this::newUserHandler);
        app.post("/login", this::loginHandler);
        app.post("/messages", this::newMessageHandler);
        app.get("/messages", this::getAllMessagesHandler);
        app.get("/messages/{message_id}", this::getMessageByIdHandler);
        app.delete("/messages/{message_id}", this::deleteMessByIdHandler);
        app.patch("messages/{message_id}", this::updateMessByIdHandler);
        app.get("/accounts/{account_id}/messages", this::getMessageByUserHandler);

        return app;
    }

    /**
     * This is an example handler for an example endpoint.
     * 
     * @param context The Javalin Context object manages information about both the
     *                HTTP request and response.
     */

    private void newUserHandler(Context context) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Account account = mapper.readValue(context.body(), Account.class);
        Account addedAccount = socialMediaService.addAccount(account);
        if (addedAccount == null || account.getUsername() == "" || account.getPassword().length() < 4) {
            context.status(400);
        } else {
            context.json(mapper.writeValueAsString(addedAccount));
        }
    }

    private void loginHandler(Context context) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Account account = mapper.readValue(context.body(), Account.class);
        Account foundAccount = socialMediaService.loginAccount(account);
        if (foundAccount == null) {
            context.status(401);
        } else {
            context.json(mapper.writeValueAsString(foundAccount));
        }
    }

    private void newMessageHandler(Context context) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Message message = mapper.readValue(context.body(), Message.class);
        Message addedMessage = socialMediaService.addMessage(message);
        SocialMediaAccountDAO acDAO = new SocialMediaAccountDAO();
        if (addedMessage == null || message.getMessage_text() == "" || message.getMessage_text().length() > 254
                || acDAO.returnAccountById(message.getPosted_by()) == null) {
            context.status(400);
        } else {
            context.json(mapper.writeValueAsString(addedMessage));
        }
    }

    private void getAllMessagesHandler(Context context) {
        context.json(socialMediaService.getAllMesages());
    }

    private void getMessageByIdHandler(Context context) {
        int messageId = Integer.parseInt(context.pathParam("message_id"));
        if (socialMediaService.getMesagesbyId(messageId) != null) {
            context.json(socialMediaService.getMesagesbyId(messageId));
        } else {
            context.json("");
        }

    }

    private void deleteMessByIdHandler(Context context) throws JsonProcessingException {
        int messageId = Integer.parseInt(context.pathParam("message_id"));
        Message nMessage = new Message();
        if (socialMediaService.getMesagesbyId(messageId) != null) {
            nMessage = socialMediaService.getMesagesbyId(messageId);
            socialMediaService.deleteMessById(nMessage);
            context.json(nMessage);
        } else {
            context.status(200);
            context.json("");
        }

    }

    private void updateMessByIdHandler(Context context) throws JsonMappingException, JsonProcessingException {
        int messageId = Integer.parseInt(context.pathParam("message_id"));
        ObjectMapper mapper = new ObjectMapper();
        Message message = mapper.readValue(context.body(), Message.class);
        int mensaje = message.getMessage_text().length();
        Message nMessage = new Message();
        if (socialMediaService.getMesagesbyId(messageId) != null && message.getMessage_text() != ""
                && mensaje < 255) {
            nMessage = socialMediaService.getMesagesbyId(messageId);
            nMessage.setMessage_text(message.getMessage_text());
            socialMediaService.updateMessById(nMessage);
            context.json(nMessage);
        } else {
            context.status(400);
        }

    }

    private void getMessageByUserHandler(Context context) {
        int postedBy = Integer.parseInt(context.pathParam("account_id"));
        if (socialMediaService.getMesagesbyAccId(postedBy) != null) {
            context.json(socialMediaService.getMesagesbyAccId(postedBy));
        } else {
            context.json("");
        }

    }

}