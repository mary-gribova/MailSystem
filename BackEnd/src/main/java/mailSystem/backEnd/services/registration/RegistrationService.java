package mailSystem.backEnd.services.registration;

import mailSystem.backEnd.dao.EmailBean;
import mailSystem.backEnd.dao.FolderBean;
import mailSystem.backEnd.dao.UserBean;
import mailSystem.backEnd.entities.Email;
import mailSystem.backEnd.excepions.DublicateEntityException;
import mailSystem.backEnd.excepions.NoSuchEmailEntityException;
import org.mindrot.jbcrypt.BCrypt;

import javax.ejb.ConcurrencyManagement;
import javax.ejb.ConcurrencyManagementType;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import java.util.Date;


@Stateless
@ConcurrencyManagement(ConcurrencyManagementType.CONTAINER)
public class RegistrationService {
    @EJB
    UserBean userBean;

    @EJB
    FolderBean folderBean;

    @EJB
    EmailBean emailBean;

    /**
     * Register user
     * @param userFirstName
     * @param userLastName
     * @param userPass
     * @param userBirthDate
     * @param userPhone
     * @param userAddress
     * @param alternateEmail
     * @throws DublicateEntityException
     * @throws mailSystem.backEnd.excepions.NoSuchEmailEntityException
     */
    public void register(String userFirstName, String userLastName, String userPass, Date userBirthDate,
                            String userPhone, String userAddress, String alternateEmail) throws DublicateEntityException, NoSuchEmailEntityException {

        String hashed = BCrypt.hashpw(userPass, BCrypt.gensalt());

        userBean.addUser(userFirstName, userLastName, hashed, userBirthDate, userPhone, userAddress, alternateEmail);
        Email email = emailBean.getEmailByName(userAddress);

        folderBean.createFolder("Inbox", email, null);
        folderBean.createFolder("Send", email, null);
    }
}
