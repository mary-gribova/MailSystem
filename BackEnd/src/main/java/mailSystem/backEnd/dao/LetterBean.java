package mailSystem.backEnd.dao;

import mailSystem.backEnd.entities.Letter;
import mailSystem.backEnd.excepions.NoSuchLetterEntityException;
import org.primefaces.json.JSONArray;
import org.primefaces.json.JSONException;
import org.primefaces.json.JSONObject;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@Stateless
public class LetterBean {

    @PersistenceContext(unitName = "server")
    private EntityManager em;

    private final static Logger LOG = Logger.getLogger(LetterBean.class.getName());

    /**
     * Remove specified list of letters
     * @param letters list of letters to remove
     */
    public void delLetters(List<Letter> letters) {
        for (Letter letter : letters) {
            em.remove(letter);
        }

       LOG.info("List of letters was removed");
    }


    /**
     * Remove single specified letter
     * @param letter json object letter to be removed
     * @throws mailSystem.backEnd.excepions.NoSuchLetterEntityException
     */
    public void delSingleLetter(JSONObject letter) throws NoSuchLetterEntityException {
        em.remove(findSingleLetter(letter));
        LOG.info("Letter was removed");
    }

    /**
     * Find specified letters in database
     * @param letters JSONArray of letters to be found
     * @return ArrayList of found letters (can be null)
     */
    public List<Letter> findLetters(JSONArray letters) {
        TypedQuery<Letter> query = em.createNamedQuery("Letter.findLetter", Letter.class);
        ArrayList<Letter> findLetters = new ArrayList<Letter>();

        for (int i = 0; i < letters.length(); i++)
        {
            try {
                query.setParameter("letterDate", letters.getJSONObject(i).get("date"));
                query.setParameter("letterFrom", letters.getJSONObject(i).get("from"));
                query.setParameter("letterTo", letters.getJSONObject(i).get("to"));
                query.setParameter("letterTheme", letters.getJSONObject(i).get("theme"));
                query.setParameter("letterFolder", letters.getJSONObject(i).get("folder"));
            } catch (JSONException e) {
                LOG.log(Level.SEVERE, "Error while getting objects from JSONArray");
                e.printStackTrace();
                return null;
            }

            List<Letter> result = query.getResultList();

            if (result != null && result.size() != 0) {
                findLetters.add(result.get(0));
            }
        }

        LOG.info("Success getting letters entities");
       return findLetters;
    }

    /**
     * Add specified letter entity
     * @param letter letter entity to add
     */
    public void addLetter(Letter letter) {
        em.persist(letter);
        LOG.info("Letter was added");
    }


    /**
     * Update specified letter entity
     * @param letter letter entity to update
     */
    public void updateLetter(Letter letter) {
        em.merge(letter);
        LOG.info("Letter was updated");
    }

    /**
     * Find specified leter
     * @param jsonLetter json object of letter
     * @return found letter (can be null)
     * @throws mailSystem.backEnd.excepions.NoSuchLetterEntityException
     */
    public Letter findSingleLetter(JSONObject jsonLetter) throws NoSuchLetterEntityException {
        TypedQuery<Letter> query = em.createNamedQuery("Letter.findLetter", Letter.class);
           Letter l;

            try {
                query.setParameter("letterDate", jsonLetter.get("date"));
                query.setParameter("letterFrom", jsonLetter.get("from"));
                query.setParameter("letterTo", jsonLetter.get("to"));
                query.setParameter("letterTheme", jsonLetter.get("theme"));
                query.setParameter("letterFolder", jsonLetter.get("folder"));
            } catch (JSONException e) {
                e.printStackTrace();
                LOG.log(Level.SEVERE, "Error while getting fields from JSONObject");
                return null;
            }

            try {
                List<Letter> result = query.getResultList();
                l = result.get(0);
            } catch (NoResultException e) {
               e.printStackTrace();
               LOG.log(Level.SEVERE, "No such entity!");
               throw new NoSuchLetterEntityException();
            }

            LOG.info("Letter was successfully found");
            return l;
    }


    /**
     * Update isNew state of specified letter
     * @param l letter entity
     */
    public void setLetterNotNew(Letter l) {
        l.setNewLetter(false);
        em.persist(l);
        LOG.info("Letter marked as read");
    }
}
