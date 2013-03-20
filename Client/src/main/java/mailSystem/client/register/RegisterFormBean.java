package mailSystem.client.register;

import mailSystem.backEnd.excepions.DublicateEntityException;
import mailSystem.backEnd.excepions.NoSuchEmailEntityException;
import mailSystem.backEnd.services.registration.RegistrationService;
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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

@ManagedBean(name = "register")
@ConcurrencyManagement(ConcurrencyManagementType.CONTAINER)
@SessionScoped
public class RegisterFormBean implements Serializable {
    private static final Logger LOG = Logger.getLogger(RegisterFormBean.class.getName());

    @EJB
    RegistrationService registrationService;

    private String firstName;
      
    private String lastName;
      
    private String email;
      
    private String birthDate;
      
    private String password;

    private String phone;

    private String alternateEmail;

    public String getAlternateEmail() {
        return alternateEmail;
    }

    public void setAlternateEmail(String alternateEmail) {
        this.alternateEmail = alternateEmail;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public String getPassword() {
        return password;
    }

    public String register() {
        Date date = null;
        try {
            date = new SimpleDateFormat("dd/MM/yyyy").parse(birthDate);
        } catch (ParseException e) {
            e.printStackTrace();
            LOG.log(Level.SEVERE, "Exception while parsing date");
        }

        try {
            registrationService.register(firstName, lastName, password,
                        date, phone, email, alternateEmail);
        } catch (DublicateEntityException e) {
            e.printStackTrace();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL,"Error",
                    ErrorMessages.DUPLICATE_USER_MESSAGE));
        } catch (NoSuchEmailEntityException noSuchEmailEntity) {
            noSuchEmailEntity.printStackTrace();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL,"Error",
                    ErrorMessages.DATABASE_ERROR_MESSAGE));
        }

        FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
        return RedirectConstants.WELCOME_LOGIN_PAGE;

    }
}
              