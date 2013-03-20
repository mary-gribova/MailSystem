package mailSystem.backEnd.services.mailing;

import mailSystem.backEnd.dao.LetterBean;
import mailSystem.backEnd.entities.Letter;
import org.primefaces.json.JSONArray;

import javax.ejb.ConcurrencyManagement;
import javax.ejb.ConcurrencyManagementType;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import java.util.List;

@Stateless
@ConcurrencyManagement(ConcurrencyManagementType.CONTAINER)
public class DelLettersService {
    @EJB
    LetterBean letterBean;

    /**
     * Remove specified list of letters
     * @param letters json array of letters to remove
     */
    public void delLetters(JSONArray letters) {
        List<Letter> result = letterBean.findLetters(letters);

        if (result != null) {
            letterBean.delLetters(result);
        }

    }
}
