package mailSystem.client.mailBox.movingLetters;


import mailSystem.backEnd.excepions.NoSuchFolderEntityException;
import mailSystem.backEnd.services.mailing.FolderService;
import mailSystem.client.constants.ErrorMessages;
import mailSystem.client.mailBox.folders.Folder;
import org.primefaces.json.JSONArray;
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
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@ManagedBean(name = "movingLetters")
@ConcurrencyManagement(ConcurrencyManagementType.CONTAINER)
@SessionScoped
public class MovingLettersBean implements Serializable {
    private static final Logger LOG = Logger.getLogger(MovingLettersBean.class.getName());

    @EJB
    FolderService folderService;

    @ManagedProperty(value = "#{login.email}")
    private String mailBoxEmail;

    private List<Folder> allFolders;
    private Folder selectedFolder;
    private MovingLettersDataModel movingLettersDataModel;

    @PostConstruct
    public void init() {
        if (folderService != null) {
            try {
                allFolders = parseFoldResult(folderService.findFolderNamesByEmail(mailBoxEmail));
            } catch (NoSuchFolderEntityException noSuchFolderEntity) {
                noSuchFolderEntity.printStackTrace();
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "Error",
                        ErrorMessages.DATABASE_ERROR_MESSAGE));
            }
        }
        else allFolders = new ArrayList<Folder>();
        movingLettersDataModel = new MovingLettersDataModel(allFolders);
    }


    public void updateFolders() {
        if (folderService != null) {
            List<Folder> newFolders = null;
            try {
                newFolders = parseFoldResult(folderService.findFolderNamesByEmail(mailBoxEmail));
            } catch (NoSuchFolderEntityException noSuchFolderEntity) {
                noSuchFolderEntity.printStackTrace();
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "Error",
                        ErrorMessages.DATABASE_ERROR_MESSAGE));
            }

            /*something was added*/
            if (newFolders.size() > allFolders.size()) {
                if (allFolders.size() == 0) {
                    allFolders.addAll(newFolders);
                } else {
                   for (int i = 0; i < newFolders.size(); i++) {
                       if (!allFolders.contains(newFolders.get(i))) {
                          allFolders.add(newFolders.get(i));
                       }
                   }
                }
            /*something was deleted*/
            } else if (newFolders.size() < allFolders.size()) {

                    for(int i = 0; i < allFolders.size(); i++) {
                        if (!newFolders.contains(allFolders.get(i)))
                            allFolders.remove(allFolders.get(i));
                }

            } if (newFolders.size() == allFolders.size()) {
                if (!newFolders.equals(allFolders)) {
                    for (int i = 0; i < allFolders.size(); i++) {
                        if (!newFolders.get(i).getName().equals(allFolders.get(i).getName()))
                            allFolders.get(i).setName(newFolders.get(i).getName());
                    }
                }
            }
        }
    }

    public List<Folder> parseFoldResult(JSONArray foldResult) {
        ArrayList<Folder>  folders = new ArrayList<Folder>();

        for (int i = 0; i < foldResult.length(); i++) {
            try {
                JSONObject jsonFolder = foldResult.getJSONObject(i);
                Folder f = new Folder(jsonFolder.getString("name"));
                folders.add(f);
            } catch (JSONException e) {
                e.printStackTrace();
                LOG.log(Level.SEVERE, "Exception while getting fields from json object");
            }
        }

        return folders;
    }

    public void setMailBoxEmail(String mailBoxEmail) {
        this.mailBoxEmail = mailBoxEmail;
    }

    public void setAllFolders(List<Folder> allFolders) {
        this.allFolders = allFolders;
    }

    public void setSelectedFolder(Folder selectedFolder) {
        this.selectedFolder = selectedFolder;
    }

    public void setMovingLettersDataModel(MovingLettersDataModel movingLettersDataModel) {
        this.movingLettersDataModel = movingLettersDataModel;
    }

    public String getMailBoxEmail() {
        return mailBoxEmail;
    }

    public List<Folder> getAllFolders() {
        return allFolders;
    }

    public Folder getSelectedFolder() {
        return selectedFolder;
    }

    public MovingLettersDataModel getMovingLettersDataModel() {
        return movingLettersDataModel;
    }

}
