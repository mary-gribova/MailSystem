package mailSystem.backEnd.services.mailing;

import mailSystem.backEnd.dao.EmailBean;
import mailSystem.backEnd.dao.FolderBean;
import mailSystem.backEnd.dao.LetterBean;
import mailSystem.backEnd.entities.Email;
import mailSystem.backEnd.entities.Folder;
import mailSystem.backEnd.entities.Letter;
import mailSystem.backEnd.excepions.NoSuchEmailEntity;
import org.primefaces.json.JSONArray;

import javax.ejb.ConcurrencyManagement;
import javax.ejb.ConcurrencyManagementType;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import java.util.Iterator;
import java.util.List;

@Stateless
@ConcurrencyManagement(ConcurrencyManagementType.CONTAINER)
public class MoveLettersService {
    @EJB
    EmailBean emailBean;

    @EJB
    LetterBean letterBean;

    @EJB
    FolderBean folderBean;

    /**
     *  Move letters from one folder to another
     * @param folderName folder's name
     * @param emailName email address
     * @param jsonLettersToMove json array of letters to move
     * @throws NoSuchEmailEntity
     */
    public void moveLetters(String folderName, String emailName, JSONArray jsonLettersToMove) throws NoSuchEmailEntity {
        Email email = emailBean.getEmailByName(emailName);

        List<Letter> lettersToMove = letterBean.findLetters(jsonLettersToMove);
        Iterator<Letter> iteratorLetters = lettersToMove.iterator();

        List<Folder> emailFolders =  email.getAddressFolder();
        Iterator<Folder> iteratorFolders = emailFolders.iterator();

        Folder folderMove = null;

        while (iteratorFolders.hasNext()) {
          folderMove = iteratorFolders.next();
          if(folderMove.getFolderName().equals(folderName)) {
              break;
          }
        }

        if (folderMove != null) {
            while (iteratorLetters.hasNext()) {
                Letter l = iteratorLetters.next();
                moveLetterFromToFolder(l, folderMove, l.getLetterFolder());
            }

        }

    }

    /**
     *
     * @param letter letter entity to move
     * @param toFolder folder to
     * @param fromFolder folder from
     */
    private void moveLetterFromToFolder(Letter letter, Folder toFolder, Folder fromFolder) {
        fromFolder.getFolderLetters().remove(letter);
        folderBean.updateFolder(fromFolder);

        toFolder.getFolderLetters().add(letter);
        folderBean.updateFolder(toFolder);

        letter.setLetterFolder(toFolder);
        letterBean.updateLetter(letter);
    }
}
