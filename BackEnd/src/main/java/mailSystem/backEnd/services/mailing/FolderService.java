package mailSystem.backEnd.services.mailing;

import mailSystem.backEnd.dao.EmailBean;
import mailSystem.backEnd.dao.FolderBean;
import mailSystem.backEnd.dao.LetterBean;
import mailSystem.backEnd.entities.Email;
import mailSystem.backEnd.entities.Folder;
import mailSystem.backEnd.excepions.NoSuchEmailEntityException;
import mailSystem.backEnd.excepions.NoSuchFolderEntityException;
import org.primefaces.json.JSONArray;
import org.primefaces.json.JSONException;
import org.primefaces.json.JSONObject;

import javax.ejb.ConcurrencyManagement;
import javax.ejb.ConcurrencyManagementType;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import java.util.List;

@Stateless
@ConcurrencyManagement(ConcurrencyManagementType.CONTAINER)
public class FolderService {
    @EJB
    FolderBean folderBean;

    @EJB
    EmailBean emailBean;

    @EJB
    LetterBean letterBean;

    /**
     *  Create new folder
     * @param folderName folder's name
     * @param folderEmail email address
     * @throws mailSystem.backEnd.excepions.NoSuchEmailEntityException
     */
    public void createNewFolder(String folderName, String folderEmail) throws NoSuchEmailEntityException {
        Email email = emailBean.getEmailByName(folderEmail);
        folderBean.createFolder(folderName, email, null);
    }

    /**
     * Remove specified folder
     * @param folderName folder's name
     * @param folderEmail email address
     * @throws mailSystem.backEnd.excepions.NoSuchEmailEntityException, NoSuchFolderEntityException
     */
    public void deleteFolder(String folderName, String folderEmail) throws NoSuchEmailEntityException, NoSuchFolderEntityException {
        Email email = emailBean.getEmailByName(folderEmail);
        Folder folder = folderBean.findByAddressAndName(email.getAddressName(), folderName).get(0);

        if (folder != null) {
           if (folder.getFolderLetters() == null || folder.getFolderLetters().size() == 0) {
               folderBean.removeFolder(folder);
           } else {
               letterBean.delLetters(folder.getFolderLetters());
               folderBean.removeFolder(folder);
           }
        }

    }

    /**
     *  Find list of folders' names of specified email
     * @param email email address
     * @return json array of folder's names
     * @throws mailSystem.backEnd.excepions.NoSuchFolderEntityException
     */
    public JSONArray findFolderNamesByEmail(String email) throws NoSuchFolderEntityException {
       JSONArray folderNames = new JSONArray();

        List<Folder> folders = folderBean.findFolderByEmail(email);

        for (Folder folder : folders) {
            try {
                JSONObject jsonFolder = new JSONObject();
                jsonFolder.put("name", folder.getFolderName());
                folderNames.put(jsonFolder);
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

       return folderNames;
    }

    /**
     * Rename folder
     * @param emailName email address
     * @param oldName old folder's name
     * @param newName new folder's name
     */
    public void renameFolder(String emailName, String oldName, String newName) throws NoSuchEmailEntityException {
        Email email = emailBean.getEmailByName(emailName);
        List<Folder> emailFolders = email.getAddressFolder();
        for (Folder folder : emailFolders) {
           if (folder.getFolderName().equals(oldName)) {
               folder.setFolderName(newName);
               folderBean.updateFolder(folder);
           }
        }
    }
}
