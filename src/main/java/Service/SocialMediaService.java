package Service;

import java.util.ArrayList;
import java.util.List;

import DAO.SocialMediaAccountDAO;
import DAO.SocialMediaMessageDAO;
import Model.Account;
import Model.Message;

public class SocialMediaService {

    SocialMediaAccountDAO AccountDAO;
    SocialMediaMessageDAO MessageDAO;

    public SocialMediaService() {
        AccountDAO = new SocialMediaAccountDAO();
        MessageDAO = new SocialMediaMessageDAO();
    }

    public Account addAccount(Account account) {
        Account newAccount = AccountDAO.createAccount(account);
        return newAccount;
    }

    public List<Message> getAllMesages() {
        List<Message> messages = new ArrayList<Message>();

        messages = MessageDAO.returnMessages();

        return messages;
    }

    public Message getMesagesbyId(int messageId) {
        Message message = new Message();

        message = MessageDAO.returnMessById(messageId);

        return message;
    }

    public List<Message> getMesagesbyAccId(int messageId) {
        List<Message> messages = new ArrayList<Message>();

        messages = MessageDAO.returnMessByAccId(messageId);

        return messages;
    }

    public Message updateMessById(Message message) {
        Message nMessage = new Message();
        nMessage = message;
        MessageDAO.updateMessById(message);
        return nMessage;
    }

    public Message addMessage(Message message) {
        Message nMessage = MessageDAO.createMessage(message);
        return nMessage;
    }

    public Account loginAccount(Account account) {
        Account nAccount = AccountDAO.loginAccount(account);
        return nAccount;
    }

    public Message deleteMessById(Message message) {
        MessageDAO.deleteMessById(message.getMessage_id());
        return message;
    }
}
