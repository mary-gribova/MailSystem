package mailSystem.client.mailBox.mailing;

import mailSystem.backEnd.excepions.NoSuchEmailEntity;
import mailSystem.backEnd.excepions.NoSuchFolderEntity;
import mailSystem.backEnd.excepions.NoSuchRecipientException;
import mailSystem.backEnd.services.mailing.SendLetterService;
import mailSystem.client.constants.ErrorMessages;
import mailSystem.client.constants.SuccessMessages;
import mailSystem.client.mailBox.lettersList.LettersListBean;

import javax.ejb.ConcurrencyManagement;
import javax.ejb.ConcurrencyManagementType;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import java.io.Serializable;
import java.util.Date;

@ManagedBean(name = "replyLetter")
@ConcurrencyManagement(ConcurrencyManagementType.CONTAINER)
@SessionScoped
public class ReplyLetterBean implements Serializable {
    @ManagedProperty(value = "#{lettersList}")
    LettersListBean lettersListBean;

    @ManagedProperty(value = "#{login.email}")
    private String emailFrom;

    @EJB
    SendLetterService sendLetterService;

    private String body;
    private String theme;
    private String to;

    public void setLettersListBean(LettersListBean lettersListBean) {
        this.lettersListBean = lettersListBean;
    }

    public LettersListBean getLettersListBean() {
        return lettersListBean;
    }

    public String getEmailFrom() {
        return emailFrom;
    }

    public void setEmailFrom(String emailFrom) {
        this.emailFrom = emailFrom;
    }

    public void setTheme(String theme) {
        this.theme = theme;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getTheme() {
        return theme;
    }

    public String getTo() {
        return to;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public void sendLetter() {
        if (lettersListBean.getSelectedLetter() != null) {
            try {
                sendLetterService.sendLetter(emailFrom, lettersListBean.getSelectedLetter().getFrom(),
                        theme, new Date(), body);
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,"Success",
                        SuccessMessages.SUCCESS_SEND_LETTER));
            } catch (NoSuchEmailEntity noSuchEmailEntity) {
                noSuchEmailEntity.printStackTrace();
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,"Error",
                        ErrorMessages.DATABASE_ERROR_MESSAGE));
            } catch (NoSuchFolderEntity noSuchFolderEntity) {
                noSuchFolderEntity.printStackTrace();
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,"Error",
                        ErrorMessages.DATABASE_ERROR_MESSAGE));
            } catch (NoSuchRecipientException e) {
                e.printStackTrace();
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error",
                        ErrorMessages.NO_SUCH_RECIPIENT_MESSAGE));
            }
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error",
                    ErrorMessages.NO_SUCH_RECIPIENT_MESSAGE));
        }

    }

    public void prepareForm() {
       body = null;
       theme = null;
    }

}
