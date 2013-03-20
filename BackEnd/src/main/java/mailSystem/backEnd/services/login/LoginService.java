package mailSystem.backEnd.services.login;

import mailSystem.backEnd.dao.UserBean;
import mailSystem.backEnd.entities.User;
import mailSystem.backEnd.excepions.NoSuchUserEntityException;
import org.mindrot.jbcrypt.BCrypt;
import org.primefaces.json.JSONException;
import org.primefaces.json.JSONObject;

import javax.ejb.ConcurrencyManagement;
import javax.ejb.ConcurrencyManagementType;
import javax.ejb.EJB;
import javax.ejb.Stateless;

@Stateless(mappedName = "LoginService")
@ConcurrencyManagement(ConcurrencyManagementType.CONTAINER)
public class LoginService {
    @EJB
    private UserBean userBean;

    /**
     * Login
     * @param userEmail user's email to log in
     * @param userPass user's password to log in
     * @return true if login is successful, false otherwise
     * @throws mailSystem.backEnd.excepions.NoSuchUserEntityException
     */
    public boolean loginUser(String userEmail, String userPass) throws NoSuchUserEntityException {
        User user = userBean.getUserByEmail(userEmail);
        return user != null && BCrypt.checkpw(userPass, user.getUserPassword());
    }

    /**
     *  Find user entity by email address
     * @param email user's email address
     * @return JSONObject user, who has such email, with fields:
     * firstName, lastName, birthDate, userPhone, userMail, alternateEmail  (can be null)
     * @throws mailSystem.backEnd.excepions.NoSuchUserEntityException
     */
    public JSONObject findUserByEmail(String email) throws NoSuchUserEntityException {
        User userEntity = userBean.getUserByEmail(email);

        JSONObject user = new JSONObject();
        try {
            user.put("firstName", userEntity.getUserFirstName());
            user.put("lastName", userEntity.getUserLastName());
            user.put("birthDate", userEntity.getUserBirthDate());
            user.put("userPhone", userEntity.getUserPhone());
            user.put("userMail", userEntity.getUserAddress());
            user.put("alternateEmail", userEntity.getAlternateEmail());
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }

        return user;
    }

    /**
     * Change user's password
     * @param email user's email
     * @param newPass new user's password
     */
    public void changePass(String email, String newPass) throws NoSuchUserEntityException {
            User user = userBean.getUserByEmail(email);
            String hashed = BCrypt.hashpw(newPass, BCrypt.gensalt());
            user.setUserPassword(hashed);
            userBean.updateUser(user);
    }


}
