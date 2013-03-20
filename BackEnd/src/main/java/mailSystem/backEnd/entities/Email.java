package mailSystem.backEnd.entities;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"addressName"}), name = "Address")
@NamedQueries({
@NamedQuery(name = "Address.findByName",
            query = "SELECT a FROM Email a where a.addressName = :addressName")
})
public class Email implements Serializable{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long addressID;

    @Column(name = "addressDate", nullable = false)
    private Date addressDate;

    @Column(name = "addressName", nullable = false, length = 75)
    private String addressName;

    @OneToOne
    private User addressUser;

    @OneToMany(mappedBy = "folderAddress")
    private List<Folder> addressFolder;

    @OneToMany(mappedBy = "letterFrom")
    private List<Letter> addressSendedLetters;

    @OneToMany(mappedBy = "letterTo")
    private List<Letter> addressRecievedLetters;

    public Email() {
    }

    public Email(Date addressDate, String addressName, User addressUser) {
        this.addressDate = addressDate;
        this.addressName = addressName;
        this.addressUser = addressUser;
    }

    public void setAddressID(long addressID) {
        this.addressID = addressID;
    }

    public void setAddressDate(Date addressDate) {
        this.addressDate = addressDate;
    }

    public void setAddressName(String addressName) {
        this.addressName = addressName;
    }

    public void setAddressUser(User addressUser) {
        this.addressUser = addressUser;
    }

    public void setAddressFolder(List<Folder> addressFolder) {
        this.addressFolder = addressFolder;
    }

    public void setAddressSendedLetters(List<Letter> addressSendedLetters) {
        this.addressSendedLetters = addressSendedLetters;
    }

    public long getAddressID() {
        return addressID;
    }

    public Date getAddressDate() {
        return addressDate;
    }

    public String getAddressName() {
        return addressName;
    }

    public User getAddressUser() {
        return addressUser;
    }

    public List<Folder> getAddressFolder() {
        return addressFolder;
    }

    public List<Letter> getAddressSendedLetters() {
        return addressSendedLetters;
    }

    public List<Letter> getAddressRecievedLetters() {
        return addressRecievedLetters;
    }

    public void setAddressRecievedLetters(List<Letter> addressRecievedLetters) {
        this.addressRecievedLetters = addressRecievedLetters;
    }
}
