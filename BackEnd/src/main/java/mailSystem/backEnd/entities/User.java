package mailSystem.backEnd.entities;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "User")
@NamedQueries({
@NamedQuery(name = "User.findByAddressName",
            query = "select u from User u where u.userAddress.addressName = :addressName"),
})
public class User implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long userID;

     @Column(name = "userFirstName", length = 45)
     private String userFirstName;

    @Column(name = "userLastName", length = 45)
    private String userLastName;

    @Column(name = "userBirthDate")
    private Date userBirthDate;

    @Column(name = "userPhone")
    private String userPhone;

    @Column(name = "userPassword")
    private String userPassword;

    @OneToOne(mappedBy = "addressUser")
    private Email userAddress;

    @Column(name = "alternateEmail")
    private String alternateEmail;

    public User() {
    }

    public String getAlternateEmail() {
        return alternateEmail;
    }

    public void setAlternateEmail(String alternateEmail) {
        this.alternateEmail = alternateEmail;
    }

    public User(String userFirstName, String userLastName, Date userBirthDate, String userPhone, String userPassword,
                String alternateEmail) {
        this.userFirstName = userFirstName;
        this.userLastName = userLastName;
        this.userBirthDate = userBirthDate;
        this.userPhone = userPhone;
        this.userPassword = userPassword;
        this.alternateEmail = alternateEmail;
    }

    public long getUserID() {
        return userID;
    }

    public String getUserFirstName() {
        return userFirstName;
    }

    public String getUserLastName() {
        return userLastName;
    }

    public Date getUserBirthDate() {
        return userBirthDate;
    }

    public String getUserPhone() {
        return userPhone;
    }

    public Email getUserAddress() {
        return userAddress;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserID(long userID) {
        this.userID = userID;
    }

    public void setUserFirstName(String userFirstName) {
        this.userFirstName = userFirstName;
    }

    public void setUserLastName(String userLastName) {
        this.userLastName = userLastName;
    }

    public void setUserBirthDate(Date userBirthDate) {
        this.userBirthDate = userBirthDate;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }

    public void setUserAddress(Email userAddress) {
        this.userAddress = userAddress;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }
}
