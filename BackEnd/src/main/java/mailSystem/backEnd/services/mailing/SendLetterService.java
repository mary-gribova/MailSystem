package mailSystem.backEnd.services.mailing;

import mailSystem.backEnd.dao.EmailBean;
import mailSystem.backEnd.dao.FolderBean;
import mailSystem.backEnd.dao.LetterBean;
import mailSystem.backEnd.entities.Email;
import mailSystem.backEnd.entities.Folder;
import mailSystem.backEnd.entities.Letter;
import mailSystem.backEnd.excepions.NoSuchEmailEntityException;
import mailSystem.backEnd.excepions.NoSuchFolderEntityException;
import mailSystem.backEnd.excepions.NoSuchLetterEntityException;
import mailSystem.backEnd.excepions.NoSuchRecipientException;
import org.primefaces.json.JSONObject;

import javax.ejb.ConcurrencyManagement;
import javax.ejb.ConcurrencyManagementType;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import java.util.Date;

@Stateless
@ConcurrencyManagement(ConcurrencyManagementType.CONTAINER)
public class SendLetterService {
    @EJB
    EmailBean emailBean;

    @EJB
    FolderBean folderBean;

    @EJB
    LetterBean letterBean;

    /**
     *
     * @param letterFrom email of sender
     * @param letterTo email of recipient
     * @param letterTheme letter's theme
     * @param letterDate letter's send date
     * @param letterBody letter's body
     * @throws NoSuchRecipientException, NoSuchEmailEntityException, NoSuchFolderEntityException
     */
    public void sendLetter(String letterFrom, String letterTo, String letterTheme,
                             Date letterDate, String letterBody) throws NoSuchEmailEntityException, NoSuchFolderEntityException, NoSuchRecipientException {

        Email addresFrom = emailBean.getEmailByName(letterFrom);
        Email addressTo = emailBean.getEmailByName(letterTo);

        if (addressTo != null) {

            /*Inbox letter*/
            Letter newLetterInbox = new Letter();
            newLetterInbox.setLetterBody(letterBody);
            newLetterInbox.setLetterDate(letterDate);
            newLetterInbox.setLetterTheme(letterTheme);
            newLetterInbox.setLetterTo(addressTo);
            newLetterInbox.setLetterFrom(addresFrom);
            newLetterInbox.setNewLetter(true);

            /*Send letter*/
            Letter newLetterSend = new Letter();
            newLetterSend.setLetterBody(letterBody);
            newLetterSend.setLetterDate(letterDate);
            newLetterSend.setLetterTheme(letterTheme);
            newLetterSend.setLetterTo(addressTo);
            newLetterSend.setLetterFrom(addresFrom);
            newLetterSend.setNewLetter(false);


            Folder folderEntityInbox = folderBean.findByAddressAndName(addressTo.getAddressName(), "Inbox").get(0);
            Folder folderEntitySend = folderBean.findByAddressAndName(addresFrom.getAddressName(), "Send").get(0);

            folderEntityInbox.addLetter(newLetterInbox);
            folderEntitySend.addLetter(newLetterSend);

            newLetterInbox.setLetterFolder(folderEntityInbox);
            newLetterSend.setLetterFolder(folderEntitySend);

            folderBean.updateFolder(folderEntityInbox);
            folderBean.updateFolder(folderEntitySend);

            letterBean.addLetter(newLetterInbox);
            letterBean.addLetter(newLetterSend);

        } else throw new NoSuchRecipientException();

    }

    /**
     *
     * @param jsonLetter letter to update
     * @param newState new state of letter (new or read)
     * @throws mailSystem.backEnd.excepions.NoSuchLetterEntityException
     */
    public void updateLetterState(JSONObject jsonLetter, boolean newState) throws NoSuchLetterEntityException {
        Letter letter = letterBean.findSingleLetter(jsonLetter);
        letter.setNewLetter(newState);

        letterBean.updateLetter(letter);
    }
}
