package mailSystem.client.mailBox;

import mailSystem.client.constants.RedirectConstants;

import javax.annotation.PostConstruct;
import javax.ejb.ConcurrencyManagement;
import javax.ejb.ConcurrencyManagementType;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import java.io.Serializable;

@ManagedBean(name = "mailBox")
@ConcurrencyManagement(ConcurrencyManagementType.CONTAINER)
@SessionScoped
public class MailBoxFormBean implements Serializable {

    @ManagedProperty(value = "#{login.email}")
    private String loginEmail;

    private String email;

    private float update = 1.0f;

    public void setUpdate(float update) {
        this.update = update;
    }

    public float getUpdate() {
        return update;
    }

    public void setLoginEmail(String loginEmail) {
        this.loginEmail = loginEmail;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLoginEmail() {
        return loginEmail;
    }

    public String getEmail() {
        return email;
    }

    @PostConstruct
    public void init() {
        if (loginEmail != null)
          this.email = loginEmail;
    }

    public String logout() {
        FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
        return RedirectConstants.WELCOME_LOGIN_PAGE;
    }
}
