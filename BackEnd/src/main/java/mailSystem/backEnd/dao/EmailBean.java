package mailSystem.backEnd.dao;

import mailSystem.backEnd.entities.Email;
import mailSystem.backEnd.excepions.NoSuchEmailEntityException;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.logging.Level;
import java.util.logging.Logger;

@Stateless
public class EmailBean {
    private static final Logger LOG = Logger.getLogger(EmailBean.class.getName());

    @PersistenceContext(unitName = "server")
    private EntityManager em;

    /**
     *  Get email entity by email address
     * @param email email address (string)
     * @return Email entity, if such exists
     * @throws mailSystem.backEnd.excepions.NoSuchEmailEntityException (if there is no such entity)
     */
    public Email getEmailByName(String email) throws NoSuchEmailEntityException {
        TypedQuery<Email> query = em.createNamedQuery("Address.findByName", Email.class);
        query.setParameter("addressName", email);

        try {
            Email resultEmail = query.getSingleResult();
            LOG.info("Success getting email entity");
            return resultEmail;
        } catch (NoResultException e) {
           e.printStackTrace();
           LOG.log(Level.SEVERE, "No result for query!");
           throw new NoSuchEmailEntityException();
        }
    }
}
