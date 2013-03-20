package mailSystem.backEnd.dao;


import mailSystem.backEnd.entities.Email;
import mailSystem.backEnd.entities.User;
import mailSystem.backEnd.excepions.DublicateEntityException;
import mailSystem.backEnd.excepions.NoSuchUserEntityException;

import javax.ejb.Stateless;
import javax.persistence.*;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

@Stateless
public class UserBean {

    @PersistenceContext(unitName = "server")
    private EntityManager em;

    private final static Logger LOG = Logger.getLogger(UserBean.class.getName());

    /**
     * Add new user to database
     * @param userFirstName first name of user
     * @param userLastName last name of user
     * @param userBirthDate user birth date
     * @param userPhone user phone
     * @param userPassword user password
     * @param userAddress user email address
     * @throws DublicateEntityException
     */
    public boolean addUser(String userFirstName, String userLastName, String userPassword, Date userBirthDate,
                           String userPhone, String userAddress, String alternateEmail) throws DublicateEntityException {

        User newUser = new User(userFirstName, userLastName, userBirthDate,
                                        userPhone, userPassword, alternateEmail);

        Email newAddresss = new Email();
        newAddresss.setAddressDate(new Date());
        newAddresss.setAddressUser(newUser);
        newAddresss.setAddressName(userAddress);
        newUser.setUserAddress(newAddresss);


        try {
            em.persist(newUser);
            em.persist(newAddresss);
            LOG.info("New user added: email - " + userAddress);
            return true;
        } catch (PersistenceException e) {
            LOG.log(Level.SEVERE, "Database error during persist: " + e.getMessage());
            throw new DublicateEntityException();
        }
    }


    /**
     * Get user entity by email address
     * @param email email address
     * @throws mailSystem.backEnd.excepions.NoSuchUserEntityException
     */
   public User getUserByEmail(String email) throws NoSuchUserEntityException {
       try {
           TypedQuery<User> queryUser = em.createNamedQuery("User.findByAddressName",
                   User.class);
           queryUser.setParameter("addressName", email);
           User user =  queryUser.getSingleResult();
           LOG.info("Success getting user entity");
           return user;
       } catch (NoResultException e) {
           e.printStackTrace();
           LOG.log(Level.SEVERE, "No sucj entity!");
           throw new NoSuchUserEntityException();
       }
   }

    /**
     * Update specified user entity
     * @param user user entity to update
     */
    public void updateUser(User user) {
       em.merge(user);
       LOG.info("User entity with email - " + user.getUserAddress().getAddressName()
               + "- was updated");
    }

}
