package mailSystem.client.mailBox;

import mailSystem.backEnd.excepions.NoSuchUserEntityException;
import mailSystem.backEnd.services.login.LoginService;
import mailSystem.backEnd.services.userInfo.UserInfoService;
import mailSystem.client.constants.ErrorMessages;
import mailSystem.client.constants.SuccessMessages;
import org.primefaces.json.JSONException;
import org.primefaces.json.JSONObject;

import javax.annotation.PostConstruct;
import javax.ejb.ConcurrencyManagement;
import javax.ejb.ConcurrencyManagementType;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

@ManagedBean(name = "userInfo")
@ConcurrencyManagement(ConcurrencyManagementType.CONTAINER)
@SessionScoped
public class UserInfoBean implements Serializable {
    private static final Logger LOG = Logger.getLogger(UserInfoBean.class.getName());

    @EJB
    LoginService loginService;

    @EJB
    UserInfoService userInfoService;

    @ManagedProperty(value = "#{login.email}")
    private String loginEmail;

    private String firstName;
    private String lastName;
    private String phone;
    private String birthDate;
    private String alternateEmail;

    public String getAlternateEmail() {
        return alternateEmail;
    }

    public void setAlternateEmail(String alternateEmail) {
        this.alternateEmail = alternateEmail;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getPhone() {
        return phone;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public String getLoginEmail() {
        return loginEmail;
    }

    public void setLoginEmail(String loginEmail) {
        this.loginEmail = loginEmail;
    }

    @PostConstruct
    public void init() {
        if (loginService != null) {
            try {
                JSONObject user = loginService.findUserByEmail(loginEmail);
                firstName = user.getString("firstName");
                lastName = user.getString("lastName");
                birthDate = new SimpleDateFormat("yyyy-MM-dd").format(((Date)user.get("birthDate")));
                phone = user.getString("userPhone");
                alternateEmail = user.getString("alternateEmail");
            } catch (JSONException e) {
                e.printStackTrace();
                LOG.log(Level.SEVERE, "Exception while getting fields from json object");
            } catch (NoSuchUserEntityException noSuchUserEntity) {
                noSuchUserEntity.printStackTrace();
                LOG.log(Level.SEVERE, "Database error");
            }

        }
    }

    public void updateUserInfo() {
        if (userInfoService != null) {
            Date date;
            try {
                date = new SimpleDateFormat("yyyy-MM-dd").parse(birthDate);
                userInfoService.updateUserInfo(firstName, lastName, date, phone, alternateEmail, loginEmail);
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,"Success",
                        SuccessMessages.SUCCESS_UPDATING_USER_INFO));
            } catch (ParseException e) {
                e.printStackTrace();
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "Error",
                        ErrorMessages.ERROR_PARSING_DATE_MESSAGE));
            } catch (NoSuchUserEntityException noSuchUserEntity) {
                noSuchUserEntity.printStackTrace();
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "Error",
                        ErrorMessages.DATABASE_ERROR_MESSAGE));
            }

        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL,"Error",
                   ErrorMessages.DATABASE_ERROR_MESSAGE));
        }
    }
}
