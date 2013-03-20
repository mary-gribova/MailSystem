package mailSystem.client.login;

import mailSystem.backEnd.excepions.NoSuchUserEntityException;
import mailSystem.backEnd.services.login.LoginService;
import mailSystem.client.constants.ErrorMessages;
import mailSystem.client.constants.RedirectConstants;

import javax.ejb.ConcurrencyManagement;
import javax.ejb.ConcurrencyManagementType;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import java.io.Serializable;


@ManagedBean(name = "login")
@ConcurrencyManagement(ConcurrencyManagementType.CONTAINER)
@SessionScoped
public class LoginFormBean implements Serializable {

    @EJB
    private LoginService loginService;

    private String email;

    private String password;

   public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String login() {
        try {
            if (loginService.loginUser(email, password)) {
                return RedirectConstants.MAILBOX_PAGE;
            }  else {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "Error",
                        ErrorMessages.LOGIN_FAILED_MESSAGE));
            }
        } catch (NoSuchUserEntityException noSuchUserEntity) {
            noSuchUserEntity.printStackTrace();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "Error",
                    ErrorMessages.LOGIN_FAILED_MESSAGE));
        }

        return null;
    }

    public String register() {
        return RedirectConstants.REGISTRATION_PAGE;
    }

    public String resetPass() {
        return RedirectConstants.RESET_PASS_PAGE;
    }
}
