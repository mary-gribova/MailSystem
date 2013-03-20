package mailSystem.client.mailBox.folders;

import mailSystem.backEnd.excepions.NoSuchEmailEntityException;
import mailSystem.backEnd.services.mailing.FindLetterService;
import mailSystem.client.constants.ErrorMessages;
import mailSystem.client.mailBox.lettersList.Letter;
import org.primefaces.json.JSONArray;
import org.primefaces.json.JSONException;

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
import java.util.ArrayList;
import java.util.List;

@ManagedBean(name = "foldersList")
@ConcurrencyManagement(ConcurrencyManagementType.CONTAINER)
@SessionScoped
public class FoldersListBean implements Serializable {
    @ManagedProperty(value = "#{login.email}")
    private String mailboxEmail;

    @EJB
    FindLetterService findLetterService;

    private List<Folder> allFolders;
    private Folder selectedFolder;
    private FolderDataModel folderDataModel;


    @PostConstruct
    public void init() {
        if (findLetterService != null)
            allFolders = initFolders();
        else allFolders = new ArrayList<Folder>();

        folderDataModel = new FolderDataModel(allFolders);
    }

    public FoldersListBean() {
        if (findLetterService != null)
            allFolders = initFolders();
        else allFolders = new ArrayList<Folder>();

        folderDataModel = new FolderDataModel(allFolders);
    }

    public String getMailboxEmail() {
        return mailboxEmail;
    }

    public void setMailboxEmail(String mailboxEmail) {
        this.mailboxEmail = mailboxEmail;
    }

    public List<Folder> getAllFolders() {
        return allFolders;
    }

    public void setAllFolders(List<Folder> allFolders) {
        this.allFolders = allFolders;
    }

    public void updateFolders() {
        /*Recieving all folders with their letters*/
        List<Folder> newFolders = initFolders();

        /*There is no new folders, let's see what about letters*/
        if (newFolders.size() == allFolders.size()) {
            if (newFolders.equals(allFolders)) {
                for (int i = 0; i < newFolders.size(); i++) {
                    checkTwoFoldersOnEqualAndChange(newFolders.get(i), allFolders.get(i));
                }
            } else {
                for (int j = 0; j < newFolders.size(); j++) {
                    if (allFolders.get(j).getName().equals(newFolders.get(j).getName())) {
                        checkTwoFoldersOnEqualAndChange(newFolders.get(j), allFolders.get(j));
                    }  else {
                        allFolders.get(j).setName(newFolders.get(j).getName());
                        checkTwoFoldersOnEqualAndChange(newFolders.get(j), allFolders.get(j));
                    }
                }
            }

        /*Some new folder has been added*/
        } else if (newFolders.size() > allFolders.size()) {
           for (int i = allFolders.size(); i < newFolders.size(); i++) {
               allFolders.add(newFolders.get(i));
           }
        /*Some folder was deleted*/
        } else if (newFolders.size() < allFolders.size()) {
           int i = 0;
           int oldI = 0;

           while (i < newFolders.size()) {
              if (newFolders.get(i).getName().equals(allFolders.get(i).getName())) {
                  checkTwoFoldersOnEqualAndChange(newFolders.get(i), allFolders.get(oldI));
                  i++;
                  oldI++;
              } else {
                  allFolders.remove(oldI);
              }

               if (i == newFolders.size() && i < allFolders.size()) {
                   while (allFolders.size() != newFolders.size())
                       allFolders.remove(i);
               }
           }
        }

        folderDataModel = new FolderDataModel(allFolders);
    }

    public void checkTwoFoldersOnEqualAndChange(Folder newFolder, Folder oldFolder) {
        List<Letter> newCurFolderLetters = newFolder.getLetters();
        List<Letter> oldCurFolderLetters = oldFolder.getLetters();

        if (newCurFolderLetters == null || newCurFolderLetters.size() == 0) {
            oldCurFolderLetters.removeAll(oldCurFolderLetters);
        /*if folder was empty at the begining*/
        } else if (oldCurFolderLetters == null || oldCurFolderLetters.size() == 0) {
            if(newCurFolderLetters.size() != 0)
               oldCurFolderLetters.addAll(newCurFolderLetters);
        /*Some letters were deleted from this folder*/
        } else  if (newCurFolderLetters.size() < oldCurFolderLetters.size()) {
            int jOld = 0;
            int j = 0;

            while (j < newCurFolderLetters.size()) {
                if (newCurFolderLetters.get(j).equals(oldCurFolderLetters.get(jOld))) {
                    jOld++;
                    j++;
                }  else {
                    oldCurFolderLetters.remove(jOld);
                }

                if (j == newCurFolderLetters.size() && j < oldCurFolderLetters.size()) {
                    while (oldCurFolderLetters.size() != newCurFolderLetters.size())
                        oldCurFolderLetters.remove(j);
                }
            }

               /*Some letters were added to this folder*/
        } else if (newCurFolderLetters.size() > oldCurFolderLetters.size()) {

            for (int i = 0; i < newCurFolderLetters.size() - oldCurFolderLetters.size(); i++) {
                oldCurFolderLetters.add(i, newCurFolderLetters.get(i));
            }

               /*All letters were deleted from this folder*/
        } else if (newCurFolderLetters.size() == 0) {
            oldCurFolderLetters.removeAll(oldCurFolderLetters);
        }  else if (newCurFolderLetters.size() == oldCurFolderLetters.size()) {
           /*do nothing!*/
        }
    }

    public List<Folder> initFolders() {
        ArrayList<Folder> userFolders = new ArrayList<Folder>();
        JSONArray jsonFolders = null;
        try {
            jsonFolders = findLetterService.getRecievedLetters(mailboxEmail);
        } catch (NoSuchEmailEntityException noSuchEmailEntity) {
            noSuchEmailEntity.printStackTrace();
        }

        if (jsonFolders != null) {
            for (int i = 0; i < jsonFolders.length(); i++) {
                try {
                    Folder folder = new Folder();
                    folder.parseFolderResult(jsonFolders.getJSONObject(i));
                    userFolders.add(folder);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error",
                   ErrorMessages.DATABASE_ERROR_MESSAGE));
        }

        return userFolders;
    }

    public Folder getSelectedFolder() {
        return selectedFolder;
    }

    public FolderDataModel getFolderDataModel() {
        return folderDataModel;
    }

    public void setSelectedFolder(Folder selectedFolder) {
        this.selectedFolder = selectedFolder;
    }

    public void setFolderDataModel(FolderDataModel folderDataModel) {
        this.folderDataModel = folderDataModel;
    }
}
