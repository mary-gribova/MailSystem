package mailSystem.backEnd.dao;

import mailSystem.backEnd.entities.Email;
import mailSystem.backEnd.entities.Folder;
import mailSystem.backEnd.entities.Letter;
import mailSystem.backEnd.excepions.NoSuchFolderEntityException;

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
public class FolderBean {
    @PersistenceContext(unitName = "server")
    private EntityManager em;

    private static final Logger LOG = Logger.getLogger(FolderBean.class.getName());

    /**
     * Get folders of specified email with specifed name
     * @param address email address (string)
     * @param name folder's name
     * @return list of found folders
     * @throws mailSystem.backEnd.excepions.NoSuchFolderEntityException (if there is no such entity)
     */
    public List<Folder> findByAddressAndName(String address, String name) throws NoSuchFolderEntityException {
        try {
            TypedQuery<Folder> folderQuery = em.createNamedQuery("Folder.findByAddressAndName", Folder.class);
            folderQuery.setParameter("addressName", address);
            folderQuery.setParameter("folderName", name);
            List<Folder> resultFolders = folderQuery.getResultList();
            LOG.info("Success getting folders entities");
            return resultFolders;
        } catch (NoResultException e) {
            e.printStackTrace();
            LOG.log(Level.SEVERE, "No result for query!");
            throw new NoSuchFolderEntityException();
        }
    }

    /**
     * Create folder with specified name, email and first letter (that can be null)
     * @param folderName folder's name
     * @param folderEmail email entity
     * @param addingLetter first letter in folder (can be null)
     */
    public void createFolder(String folderName, Email folderEmail, Letter addingLetter) {
        Folder folderEntity = new Folder();
        folderEntity.setFolderName(folderName);
        folderEntity.setFolderAddress(folderEmail);
        folderEntity.setFolderLetters(new ArrayList<Letter>());
        folderEntity.addLetter(addingLetter);

        em.persist(folderEntity);
        LOG.info("Adding folder:  name - " + folderName
                + ", email - " + folderEmail);
    }


    /**
     *  Update specified folder entity
     * @param folder folder entity to update
     */
    public void updateFolder(Folder folder) {
      em.merge(folder);

      LOG.info("Updating folder:  name - " + folder.getFolderName()
              + ", email - " + folder.getFolderAddress().getAddressName());
    }

    /**
     * Remove specified folder entity
     * @param folder folder entity to remove
     */
    public void removeFolder(Folder folder) {
        em.remove(folder);

        LOG.info("Remving folder: name - " + folder.getFolderName() + ", email - " +
                folder.getFolderAddress().getAddressName());
    }

    /**
     * Find all folders, that belong to this email
     * @param email email address
     * @return list of folders
     * @throws mailSystem.backEnd.excepions.NoSuchFolderEntityException
     */
    public List<Folder> findFolderByEmail(String email) throws NoSuchFolderEntityException {
        try {
            TypedQuery<Folder> folderQuery = em.createNamedQuery("Folder.findFoldersByEmail", Folder.class);
            folderQuery.setParameter("addressName", email);
            List<Folder> resultFolders = folderQuery.getResultList();
            LOG.info("Success getting folders entities");
            return resultFolders;
        } catch (NoResultException e) {
            e.printStackTrace();
            LOG.log(Level.SEVERE, "No such entity!");
            throw new NoSuchFolderEntityException();
        }
    }

}
