package mailSystem.client.mailBox.lettersList;

import mailSystem.backEnd.excepions.NoSuchEmailEntityException;
import mailSystem.backEnd.excepions.NoSuchLetterEntityException;
import mailSystem.backEnd.services.mailing.DelLettersService;
import mailSystem.backEnd.services.mailing.MoveLettersService;
import mailSystem.backEnd.services.mailing.SendLetterService;
import mailSystem.client.constants.ErrorMessages;
import mailSystem.client.constants.SuccessMessages;
import mailSystem.client.constants.WarningMessages;
import mailSystem.client.mailBox.folders.Folder;
import mailSystem.client.mailBox.folders.FoldersListBean;
import mailSystem.client.mailBox.movingLetters.MovingLettersBean;
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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@ManagedBean(name = "lettersList")
@ConcurrencyManagement(ConcurrencyManagementType.CONTAINER)
@SessionScoped
public class LettersListBean implements Serializable {
    private static final Logger LOG = Logger.getLogger(LettersListBean.class.getName());

    @ManagedProperty(value = "#{foldersList}")
    private FoldersListBean foldersList;

    @ManagedProperty(value = "#{login.email}")
    String mailBoxEmail;

    @ManagedProperty(value = "#{movingLetters}")
    MovingLettersBean movingLettersBean;

    @EJB
    SendLetterService sendLetterService;

    private int rows;

    public int getRows() {
        return allLetters.size();
    }

    public void setRows(int rows) {
        this.rows = allLetters.size();
    }

    public MovingLettersBean getMovingLettersBean() {
        return movingLettersBean;
    }

    public void setMovingLettersBean(MovingLettersBean movingLettersBean) {
        this.movingLettersBean = movingLettersBean;
    }

    @EJB
    DelLettersService delLettersService;

    @EJB
    MoveLettersService moveLettersService;

    private List<Letter> allLetters;
    private String email;
    private Letter selectedLetter;
    private LetterDataModel letterDataModel;


    private Folder lastSelectedFolder;

    public Folder getLastSelectedFolder() {
        return lastSelectedFolder;
    }

    public void setLastSelectedFolder(Folder lastSelectedFolder) {
        this.lastSelectedFolder = lastSelectedFolder;
    }

    public String getMailBoxEmail() {
        return mailBoxEmail;
    }

    public void setMailBoxEmail(String mailBoxEmail) {
        this.mailBoxEmail = mailBoxEmail;
    }

    public FoldersListBean getFoldersList() {
        return foldersList;
    }

    public void setFoldersList(FoldersListBean foldersList) {
        this.foldersList = foldersList;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<Letter> getAllLetters() {
        return allLetters;
    }

    public void setAllLetters(List<Letter> allLetters) {
        this.allLetters = allLetters;
    }

    private ArrayList<Letter> getAllRecievedLetters(Folder selectedFolder) {
        ArrayList<Letter> recievedLetters = new ArrayList<Letter>();

        /*display letters from selected folder*/
        if (foldersList.getSelectedFolder() != null) {
                List<Folder> folders = foldersList.getAllFolders();
                for (Folder f : folders) {
                    if (f.getName().equals(selectedFolder.getName())) {
                        List<Letter> lettersToAdd = f.getLetters();

                        for (Letter l : lettersToAdd) {
                            recievedLetters.add(l);
                        }
                    }
            }

        /*display letters from inbox folder*/
        } else {
            List<Folder> folders = foldersList.getAllFolders();

            for (Folder f : folders) {
                if (f.getName().equals("Inbox")) {
                    List<Letter> lettersToAdd = f.getLetters();

                    for (Letter l : lettersToAdd) {
                        recievedLetters.add(l);
                    }
                }
            }
        }


        return recievedLetters;
    }

    public void updateLetters() {
        Folder curSelectedFolder = foldersList.getSelectedFolder();
        List<Letter> newLetters = getAllRecievedLetters(curSelectedFolder);

        if (curSelectedFolder != null && !curSelectedFolder.getName().equals(lastSelectedFolder.getName())) {
            allLetters.removeAll(allLetters);
            allLetters.addAll(newLetters);
            lastSelectedFolder = new Folder(curSelectedFolder);
        }  else {
            lastSelectedFolder = new Folder();
            lastSelectedFolder.setName("Inbox");
            if (newLetters.size() < allLetters.size()) {
                if (newLetters.size() == 0) {
                    allLetters.removeAll(allLetters);
                } else {

                    if (allLetters.containsAll(newLetters)) {
                        int i = 0;
                        while (i < allLetters.size()) {
                            if (newLetters.contains(allLetters.get(i))) {
                                i++;
                            } else {
                                Letter l = allLetters.get(i);
                                allLetters.remove(l);
                            }
                        }
                    } else {
                        allLetters.removeAll(allLetters);
                        allLetters.addAll(newLetters);
                    }

                }
            } else if (newLetters.size() > allLetters.size()) {
                if (allLetters.size() == 0) {
                    allLetters.addAll(newLetters);
                }  else {
                    for (int j = 0; j < newLetters.size(); j++) {
                        if (newLetters.containsAll(allLetters)) {
                            for (int i = 0; i < newLetters.size() - allLetters.size(); i++) {
                                allLetters.add(i, newLetters.get(i));
                            }
                        } else {
                            allLetters.removeAll(allLetters);
                            allLetters.addAll(newLetters);
                        }

                    }

                }
            } else if (newLetters.size() == allLetters.size()) {
                for(int i = 0; i < allLetters.size(); i++) {
                    if (!allLetters.get(i).equals(newLetters.get(i))) {
                        allLetters.set(i, newLetters.get(i));
                    }
                }
            }
        }



        if (selectedLetter != null && selectedLetter.getFolder().equals("Inbox")) {
            if (sendLetterService != null) {
                JSONObject jsonLetter = new JSONObject();

                try {
                    jsonLetter.put("date", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S").parse(selectedLetter.getDate()));
                    jsonLetter.put("from", selectedLetter.getFrom());
                    jsonLetter.put("to", selectedLetter.getTo());
                    jsonLetter.put("theme", selectedLetter.getTheme());
                    jsonLetter.put("folder", selectedLetter.getFolder());

                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                try {
                    sendLetterService.updateLetterState(jsonLetter, false);
                } catch (NoSuchLetterEntityException noSuchLetterEntity) {
                    noSuchLetterEntity.printStackTrace();
                }
                selectedLetter.setNewLetter(false);
            }
        }

        letterDataModel = new LetterDataModel(allLetters);
    }

    @PostConstruct
    public void init() {
        allLetters = getAllRecievedLetters(foldersList.getSelectedFolder());
        if (foldersList.getSelectedFolder() != null) {
            lastSelectedFolder = new Folder(foldersList.getSelectedFolder());
        } else {
           lastSelectedFolder = new Folder();
           lastSelectedFolder.setName("Inbox");
        }

        letterDataModel = new LetterDataModel(allLetters);
    }

    public void deleteLetters() {
        if (delLettersService != null) {
            JSONArray lettersToDel = new JSONArray();

            for (Letter letter : allLetters) {
                if (letter.isChecked()) {
                    try {
                        JSONObject jsonLetter = new JSONObject();
                        jsonLetter.put("date", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S").parse(letter.getDate()));
                        jsonLetter.put("from", letter.getFrom());
                        jsonLetter.put("to", letter.getTo());
                        jsonLetter.put("theme", letter.getTheme());
                        jsonLetter.put("folder", letter.getFolder());

                        lettersToDel.put(jsonLetter);
                    } catch (JSONException e) {
                        e.printStackTrace();
                        LOG.log(Level.SEVERE, "Exception while getting fields from json objects");
                    } catch (ParseException e) {
                        e.printStackTrace();
                        LOG.log(Level.SEVERE, "Exception while parsing date");
                    }

                }
            }

              delLettersService.delLetters(lettersToDel);
              FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,"Success",
                      SuccessMessages.SUCCESS_REMOVING_LETTERS));



        }  else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL,"Error",
                    ErrorMessages.DATABASE_ERROR_MESSAGE));
        }
    }

    public Letter getSelectedLetter() {
        return selectedLetter;
    }

    public void setSelectedLetter(Letter selectedLetter) {
        this.selectedLetter = selectedLetter;
    }

    public LetterDataModel getLetterDataModel() {
        return letterDataModel;
    }

    public void setLetterDataModel(LetterDataModel letterDataModel) {
        this.letterDataModel = letterDataModel;
    }

    public void moveLetters() {
        Folder selectedFolder = movingLettersBean.getSelectedFolder();

        if (allLetters != null && moveLettersService != null && selectedFolder != null) {
            if (selectedFolder.getName().equals("Inbox") || selectedFolder.getName().equals("Send")) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL,"Error",
                        ErrorMessages.MOVE_IN_SYSTEM_FOLDERS_MESSAGE));
            } else {
                JSONArray lettersToMove = new JSONArray();

                for (int i = 0; i < allLetters.size(); i++) {
                    Letter l = allLetters.get(i);
                    if (l.isChecked()) {
                        try {
                            JSONObject jsonLetter = new JSONObject();

                            jsonLetter.put("date", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S").parse(l.getDate()));
                            jsonLetter.put("from", l.getFrom());
                            jsonLetter.put("to", l.getTo());
                            jsonLetter.put("theme", l.getTheme());
                            jsonLetter.put("folder", l.getFolder());

                            lettersToMove.put(jsonLetter);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            LOG.log(Level.SEVERE, "Exception while creating json object");
                        } catch (ParseException e) {
                            e.printStackTrace();
                            LOG.log(Level.SEVERE, "Exception while parsing date");
                        }
                    }

                }

                if (lettersToMove.length() != 0) {
                    try {
                        moveLettersService.moveLetters(selectedFolder.getName(), mailBoxEmail, lettersToMove);
                        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,"Success",
                                SuccessMessages.SUCCESS_MOVING_LETTERS));
                    } catch (NoSuchEmailEntityException noSuchEmailEntity) {
                        noSuchEmailEntity.printStackTrace();
                        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL,"Error",
                                ErrorMessages.DATABASE_ERROR_MESSAGE));
                    }

                } else {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Warning",
                            WarningMessages.LETTERS_NOT_CHOSEN_MOVE_MESSAGE));
                }
            }

        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "Error",
                    ErrorMessages.DATABASE_ERROR_MESSAGE));
        }
    }

}
