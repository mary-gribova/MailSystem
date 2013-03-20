package mailSystem.client.mailBox.folders;

import mailSystem.backEnd.excepions.NoSuchEmailEntityException;
import mailSystem.backEnd.excepions.NoSuchFolderEntityException;
import mailSystem.backEnd.services.mailing.FolderService;
import mailSystem.client.constants.ErrorMessages;
import mailSystem.client.constants.SuccessMessages;
import mailSystem.client.constants.WarningMessages;

import javax.ejb.ConcurrencyManagement;
import javax.ejb.ConcurrencyManagementType;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import java.io.Serializable;
import java.util.Iterator;
import java.util.List;

@ManagedBean(name = "createRemoveFolder")
@ConcurrencyManagement(ConcurrencyManagementType.CONTAINER)
@SessionScoped
public class CreateRemoveFolderBean implements Serializable {
    @ManagedProperty(value = "#{login.email}")
    private String mailboxEmail;

    @ManagedProperty(value = "#{foldersList}")
    FoldersListBean foldersListBean;

    @EJB
    FolderService folderService;

    private String folderName;
    private String newReanamingFolderName;

    public FoldersListBean getFoldersListBean() {
        return foldersListBean;
    }

    public void setFoldersListBean(FoldersListBean foldersListBean) {
        this.foldersListBean = foldersListBean;
    }

    public String getNewReanamingFolderName() {
        return newReanamingFolderName;
    }

    public void setNewReanamingFolderName(String newReanamingFolderName) {
        this.newReanamingFolderName = newReanamingFolderName;
    }

    public String getFolderName() {
        return folderName;
    }

    public void setFolderName(String folderName) {
        this.folderName = folderName;
    }

    public String getMailboxEmail() {
        return mailboxEmail;
    }

    public void setMailboxEmail(String mailboxEmail) {
        this.mailboxEmail = mailboxEmail;
    }

    public void createFolder() {
        if (folderService != null)  {
            int contain = 0;

            List<Folder> f = foldersListBean.getAllFolders();
            Iterator<Folder> iter = f.iterator();

            while (iter.hasNext()) {
                if(iter.next().getName().equals(folderName)) {
                    contain++;
                    break;
                }
            }

            if (contain == 0) {
                try {
                    folderService.createNewFolder(folderName, mailboxEmail);
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,"Success",
                            SuccessMessages.SUCCESS_CREATING_FOLDER_MESSAGE));
                } catch (NoSuchEmailEntityException noSuchEmailEntity) {
                    noSuchEmailEntity.printStackTrace();
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL,"Error",
                            ErrorMessages.DATABASE_ERROR_MESSAGE));
                }
            } else {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL,"Error",
                        ErrorMessages.DUPLICATE_FOLDER_NAME_MESSAGE));
            }
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL,"Error",
                    ErrorMessages.DATABASE_ERROR_MESSAGE));
        }
    }

    public void removeFolder() {
       Folder selectedFolder = foldersListBean.getSelectedFolder();

       if (selectedFolder != null) {
           if (!selectedFolder.getName().equals("Inbox") && !selectedFolder.getName().equals("Send")) {
              if (folderService != null) {
                  try {
                      folderService.deleteFolder(selectedFolder.getName(), mailboxEmail);
                  } catch (NoSuchEmailEntityException noSuchEmailEntity) {
                      noSuchEmailEntity.printStackTrace();
                      FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL,"Error",
                              ErrorMessages.DATABASE_ERROR_MESSAGE));
                  } catch (NoSuchFolderEntityException noSuchFolderEntity) {
                      noSuchFolderEntity.printStackTrace();
                      FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL,"Error",
                              ErrorMessages.DATABASE_ERROR_MESSAGE));
                  }

                  FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,"Success",
                              SuccessMessages.SUCCESS_REMOVING_FOLDER_MESSAGE));

              }  else {
                  FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL,"Error",
                          ErrorMessages.DATABASE_ERROR_MESSAGE));
              }
           }  else {
               FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL,"Error",
                       ErrorMessages.REMOVE_SYSTEM_FOLDERS_MESSAGE));
           }
       } else {
           FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Warning",
                   WarningMessages.FOLDER_NOT_CHOSEN_REMOVE_MESSAGE));
       }
    }

    public void renameFolder() {
        Folder selectedFolder = foldersListBean.getSelectedFolder();

        if (selectedFolder != null)  {
            String oldName = selectedFolder.getName();

            if (!oldName.equals("Inbox") && !oldName.equals("Send")) {
                int contain = 0;
                List<Folder> allFolders = foldersListBean.getAllFolders();
                Iterator<Folder> folderIterator = allFolders.iterator();

                while (folderIterator.hasNext()) {
                    if (folderIterator.next().getName().equals(newReanamingFolderName)) {
                        contain++;
                        break;
                    }
                }

                if (contain != 0) {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error",
                            ErrorMessages.DUPLICATE_FOLDER_NAME_MESSAGE));
                }  else {
                    try {
                        folderService.renameFolder(mailboxEmail, oldName, newReanamingFolderName);
                    } catch (NoSuchEmailEntityException noSuchEmailEntity) {
                        noSuchEmailEntity.printStackTrace();
                        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error",
                                ErrorMessages.DATABASE_ERROR_MESSAGE));
                    }

                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,"Success",
                               SuccessMessages.SUCCESS_RENAMING_FOLDER_MESSAGE));
                }
            } else {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error",
                        ErrorMessages.RENAME_SYSTEM_FOLDERS_MESSAGE));
            }
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Warning",
                    WarningMessages.FOLDER_NOT_CHOSEN_RENAME_MESSAGE));
        }
    }
}
