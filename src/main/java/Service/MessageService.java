package Service;
import DAO.MessageDAO;
import Model.Message;

import java.util.List;
public class MessageService {
    private MessageDAO messageDAO = new MessageDAO();
    private AccountService accountService = new AccountService();

    public MessageService() {
        this.messageDAO = messageDAO;
        this.accountService = accountService;
    }

    public Message createMessage(Message message) {
        if (message.getMessage_text() == null || message.getMessage_text().isEmpty() || message.getMessage_text().length() > 255 || accountService.accountDAO.getAccountById(message.getPosted_by()) == null) {
            return null;
        }
       
        return messageDAO.createMessage(message);
    }

    public List<Message> getAllMessages() {
        return messageDAO.getAllMessages();
    }

    public Message getMessageById(int messageId) {
        return messageDAO.getMessageById(messageId);
    }

    public Message deleteMessage(int messageId) {
        return messageDAO.deleteMessage(messageId);
    }

    public Message updateMessage(int messageId, String messageText) {
        if (messageText == null || messageText.isEmpty() || messageText.length() > 255) {
            return null;
        }
        return messageDAO.updateMessage(messageId, messageText);
    }

    public List<Message> getMessagesByAccountId(int accountId) {
        return messageDAO.getMessagesByAccountId(accountId);
    }
}