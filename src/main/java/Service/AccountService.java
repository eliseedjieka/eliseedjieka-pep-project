package Service;
import DAO.AccountDAO;
import Model.Account;

public class AccountService {
    AccountDAO accountDAO = new AccountDAO();

    public AccountService() {
        this.accountDAO = accountDAO;
    }

    public Account registerAccount(Account account) {
        if (account.getUsername() == null || account.getUsername().isEmpty() || account.getPassword() == null || account.getPassword().length() < 4) {
            return null;
        }
        if (accountDAO.getAccountByUsername(account.getUsername()) != null) {
            return null;
        }
        return accountDAO.createAccount(account);
    }

    public Account loginAccount(Account account) {
        Account existingAccount = accountDAO.getAccountByUsername(account.getUsername());
        if (existingAccount != null && existingAccount.getPassword().equals(account.getPassword())) {
            return existingAccount;
        }
        return null;
    }

}
