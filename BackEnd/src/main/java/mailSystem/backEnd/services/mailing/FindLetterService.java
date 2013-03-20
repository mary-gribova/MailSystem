package mailSystem.backEnd.services.mailing;

import mailSystem.backEnd.dao.EmailBean;
import mailSystem.backEnd.entities.Email;
import mailSystem.backEnd.entities.Folder;
import mailSystem.backEnd.entities.Letter;
import mailSystem.backEnd.excepions.NoSuchEmailEntityException;
import org.primefaces.json.JSONArray;
import org.primefaces.json.JSONException;
import org.primefaces.json.JSONObject;

import javax.ejb.ConcurrencyManagement;
import javax.ejb.ConcurrencyManagementType;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@Stateless(mappedName = "FindLetterService")
@ConcurrencyManagement(ConcurrencyManagementType.CONTAINER)
public class FindLetterService {

    private static final Logger LOG = Logger.getLogger(FindLetterService.class.getName());

    @EJB
    EmailBean emailBean;

    /**
     *  Get letters by specified email address
     * @param addressName email address
     * @return json array of all letters of specified email (or null)
     */
    public JSONArray getRecievedLetters(String addressName) throws NoSuchEmailEntityException {
        Email email = emailBean.getEmailByName(addressName);
        JSONArray returnedFolders = new JSONArray();
        List<Folder> folders = email.getAddressFolder();

        if (folders != null) {

            for (Folder curFolder : folders) {
                ArrayList<JSONObject> letters = new ArrayList<JSONObject>();

                for (Letter letterEntity : curFolder.getFolderLetters()) {
                     JSONObject letter = new JSONObject();

                      try {
                        letter.put("from", letterEntity.getLetterFrom().getAddressName());
                        letter.put("to", letterEntity.getLetterTo().getAddressName());
                        letter.put("theme", letterEntity.getLetterTheme());
                        letter.put("date", letterEntity.getLetterDate().toString());
                        letter.put("body", letterEntity.getLetterBody());
                        letter.put("folder", letterEntity.getLetterFolder().getFolderName());
                        letter.put("new", letterEntity.isNewLetter());
                      } catch (JSONException e) {
                         e.printStackTrace();
                         LOG.log(Level.SEVERE, "Error while getting fields from json object");
                      }

                    letters.add(letter);
                }

                JSONObject folder = new JSONObject();
                ArrayList<JSONObject> reverseLetter = reverseArray(letters);

                    try {
                       folder.put("folderName", curFolder.getFolderName());
                       folder.put("letters", new JSONArray(reverseLetter));
                    } catch (JSONException e) {
                       e.printStackTrace();
                       LOG.log(Level.SEVERE, "Error while creating json object");
                    }

                  returnedFolders.put(folder);
            }
        }

        return returnedFolders;
    }


    /**
     * Reverse array of json objects
     * @param array list of objects to reverse
     * @return reversed array
     */
    private ArrayList<JSONObject> reverseArray(ArrayList<JSONObject> array) {
        int size = array.size();

        for (int i = 0; i < size / 2; i++) {
                JSONObject ob = array.get(i);
                array.set(i, array.get(size - i - 1));
                array.set(size-i-1, ob);
        }

        return array;
    }
}
