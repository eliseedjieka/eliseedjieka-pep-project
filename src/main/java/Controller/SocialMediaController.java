package Controller;

import Model.Account;
import Model.Message;
import Service.AccountService;
import Service.MessageService;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.javalin.Javalin;
import io.javalin.http.Context;
import io.javalin.http.HttpStatus;

import java.io.IOException;
import java.util.List;

/**
 * TODO: You will need to write your own endpoints and handlers for your controller. The endpoints you will need can be
 * found in readme.md as well as the test cases. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */
public class SocialMediaController {
    private final AccountService accountService;
    private final MessageService messageService;
    private final ObjectMapper objectMapper;

    public SocialMediaController() {
        this.accountService = new AccountService();
        this.messageService = new MessageService();
        this.objectMapper = new ObjectMapper();
    }

    public Javalin startAPI() {
        Javalin app = Javalin.create();

        app.post("/register", this::registerHandler);
        app.post("/login", this::loginHandler);
        app.post("/messages", this::createMessageHandler);
        app.get("/messages", this::getAllMessagesHandler);
        app.get("/messages/{message_id}", this::getMessageByIdHandler);
        app.delete("/messages/{message_id}", this::deleteMessageHandler);
        app.patch("/messages/{message_id}", this::updateMessageHandler);
        app.get("/accounts/{account_id}/messages", this::getMessagesByAccountIdHandler);

        return app;
    }

    private void registerHandler(Context ctx) {
        try {
            Account account = objectMapper.readValue(ctx.body(), Account.class);
            Account registeredAccount = accountService.registerAccount(account);
            if (registeredAccount != null) {
                ctx.json(registeredAccount);
            } else {
                ctx.status(HttpStatus.BAD_REQUEST);
            }
        } catch (IOException e) {
            ctx.status(HttpStatus.BAD_REQUEST);
        }
    }

    private void loginHandler(Context ctx) {
        try {
            Account account = objectMapper.readValue(ctx.body(), Account.class);
            Account loggedInAccount = accountService.loginAccount(account);
            if (loggedInAccount != null) {
                ctx.json(loggedInAccount);
            } else {
                ctx.status(HttpStatus.UNAUTHORIZED);
            }
        } catch (IOException e) {
            ctx.status(HttpStatus.UNAUTHORIZED);
        }
    }

    private void createMessageHandler(Context ctx) {
        try {
            Message message = objectMapper.readValue(ctx.body(), Message.class);
            Message createdMessage = messageService.createMessage(message);
            if (createdMessage != null) {
                ctx.json(createdMessage);
            } else {
                ctx.status(HttpStatus.BAD_REQUEST);
            }
        } catch (IOException e) {
            ctx.status(HttpStatus.BAD_REQUEST);
        }
    }

    private void getAllMessagesHandler(Context ctx) {
        List<Message> messages = messageService.getAllMessages();
        ctx.json(messages);
    }

    private void getMessageByIdHandler(Context ctx) {
        try {
            int messageId = Integer.parseInt(ctx.pathParam("message_id"));
            Message message = messageService.getMessageById(messageId);
            if (message != null) {
                ctx.json(message);
            } else {
                ctx.json("");
            }

        } catch (NumberFormatException e) {
            ctx.status(HttpStatus.BAD_REQUEST);
        }
    }

    private void deleteMessageHandler(Context ctx) {
        try {
            int messageId = Integer.parseInt(ctx.pathParam("message_id"));
            Message deletedMessage = messageService.deleteMessage(messageId);
            if (deletedMessage != null) {
                ctx.json(deletedMessage);
            } else {
                ctx.json("");
            }
        } catch (NumberFormatException e) {
            ctx.status(HttpStatus.BAD_REQUEST);
        }
    }

    private void updateMessageHandler(Context ctx) {
        try {
            Message message = objectMapper.readValue(ctx.body(), Message.class);
            int messageId = Integer.parseInt(ctx.pathParam("message_id"));
            Message updatedMessage = messageService.updateMessage(messageId, message.getMessage_text());
            if (updatedMessage != null) {
                ctx.json(updatedMessage);
            } else {
                ctx.status(HttpStatus.BAD_REQUEST);
            }
        } catch (IOException | NumberFormatException e) {
            ctx.status(HttpStatus.BAD_REQUEST);
        }
    }

    private void getMessagesByAccountIdHandler(Context ctx) {
        try {
            int accountId = Integer.parseInt(ctx.pathParam("account_id"));
            List<Message> messages = messageService.getMessagesByAccountId(accountId);
            ctx.json(messages);
        } catch (NumberFormatException e) {
            ctx.status(HttpStatus.BAD_REQUEST);
        }
    }
}