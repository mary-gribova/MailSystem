package mailSystem.backEnd.services.userInfo;

import mailSystem.backEnd.dao.EmailBean;
import mailSystem.backEnd.dao.UserBean;
import mailSystem.backEnd.entities.User;
import mailSystem.backEnd.excepions.NoSuchUserEntity;

import javax.ejb.ConcurrencyManagement;
import javax.ejb.ConcurrencyManagementType;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import java.util.Date;

@Stateless
@ConcurrencyManagement(ConcurrencyManagementType.CONTAINER)
public class UserInfoService {
    @EJB
    UserBean userBean;

    @EJB
    EmailBean emailBean;

    /**
     * Update user info
     * @param firstName
     * @param lastName
     * @param birthDate
     * @param phone
     * @param alternateEmail
     * @param mailboxEmail
     * @throws NoSuchUserEntity
     */
    public void updateUserInfo(String firstName, String lastName, Date birthDate, String phone,
                                  String alternateEmail, String mailboxEmail) throws NoSuchUserEntity {

            User user = userBean.getUserByEmail(mailboxEmail);
            user.setUserFirstName(firstName);
            user.setUserLastName(lastName);
            user.setUserPhone(phone);
            user.setUserBirthDate(birthDate);
            user.setAlternateEmail(alternateEmail);

            userBean.updateUser(user);

    }

}
