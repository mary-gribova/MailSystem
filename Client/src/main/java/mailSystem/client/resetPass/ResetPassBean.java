package mailSystem.client.resetPass;

import com.sun.mail.smtp.SMTPTransport;
import mailSystem.backEnd.excepions.NoSuchUserEntity;
import mailSystem.backEnd.services.login.LoginService;
import mailSystem.client.constants.ErrorMessages;
import mailSystem.client.constants.MailConstants;
import mailSystem.client.constants.RedirectConstants;
import mailSystem.client.constants.SuccessMessages;
import org.primefaces.json.JSONException;
import org.primefaces.json.JSONObject;

import javax.ejb.ConcurrencyManagement;
import javax.ejb.ConcurrencyManagementType;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

@ManagedBean(name = "resetPass")
@ConcurrencyManagement(ConcurrencyManagementType.CONTAINER)
@SessionScoped
public class ResetPassBean implements Serializable {
    private static final Logger LOG = Logger.getLogger(ResetPassBean.class.getName());

   @EJB
   LoginService loginService;

   private String email;
   private String password;
   private String oldPassword;

    public String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void resetPass() {
      if (email != null && !email.equals("")) {
               try {
                  if (loginService.loginUser(email, oldPassword)) {
                      JSONObject user = null;
                      try {
                          user = loginService.findUserByEmail(email);
                      } catch (NoSuchUserEntity noSuchUserEntity) {
                          noSuchUserEntity.printStackTrace();
                          LOG.log(Level.SEVERE, "Database error");
                      }

                      if (user != null) {

                          try {
                              loginService.changePass(email, password);
                          } catch (NoSuchUserEntity noSuchUserEntity) {
                              noSuchUserEntity.printStackTrace();
                              LOG.log(Level.SEVERE, "Database error");
                          }

                          try {
                              String alternateEmail = user.getString("alternateEmail");

                              String fromEmail = MailConstants.SYSTEM_EMAIL;
                              String fromPass = MailConstants.SYSTEM_EMAIL_PASSWORD;

                              Properties prop = new Properties();
                              prop.put("mail.smtp.host", "localhost");
                              prop.put("mail.smtp.starttls.enable", "true");
                              prop.put("mail.smtp.host", "smtp.gmail.com");
                              prop.put("mail.smtp.user", fromEmail);
                              prop.put("mail.smtp.password", fromPass);
                              prop.put("mail.smtp.port", "587");
                              prop.put("mail.smtp.auth", "true");


                              Session session = Session.getDefaultInstance(prop);
                              String msgBody = MailConstants.USER_MESSAGE;

                              Message msg = new MimeMessage(session);
                              msg.setFrom(new InternetAddress(fromEmail, "My mail system"));
                              msg.addRecipient(Message.RecipientType.TO,
                                      new InternetAddress(alternateEmail, "Mr. User"));
                              msg.setSubject(MailConstants.USER_MESSAGE_SUBJ);
                              msg.setText(msgBody);

                              SMTPTransport t = (SMTPTransport)session.getTransport("smtps");

                              t.connect("smtp.gmail.com", fromEmail, fromPass);
                              t.sendMessage(msg, msg.getAllRecipients());
                              t.close();

                              FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,"Success",
                                      SuccessMessages.SUCCESS_RESET_PASSWORD));

                          } catch (MessagingException e) {
                              e.printStackTrace();
                              LOG.log(Level.SEVERE, "Messaging exception");
                              FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL,"Error",
                                      ErrorMessages.MESSAGING_EXCEPTION));
                          } catch (UnsupportedEncodingException e) {
                              e.printStackTrace();
                              LOG.log(Level.SEVERE, "Unsupported encoding operation");
                              FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL,"Error",
                                      ErrorMessages.UNSUPPORTED_ENCODING_OPERATION));
                          } catch (JSONException e) {
                              e.printStackTrace();
                              LOG.log(Level.SEVERE, "Exception during while getting fields from json objects");
                          }

                      }

                  } else {
                      FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL,"Error",
                              ErrorMessages.LOGIN_FAILED_MESSAGE));
                  }

              } catch (NoSuchUserEntity noSuchUserEntity) {
                  noSuchUserEntity.printStackTrace();
                   FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "Error",
                           ErrorMessages.DATABASE_ERROR_MESSAGE));
              }




      }  else {
          FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "Error",
                  ErrorMessages.NULL_EMAIL));
      }
    }

    public String toMainPage() {
        return RedirectConstants.WELCOME_LOGIN_PAGE;
    }
}
