package mailSystem.backEnd.entities;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "Letter")
@NamedQueries({
@NamedQuery(name = "Letter.findLetter",
            query = "select l from Letter l where l.letterFrom.addressName = :letterFrom and l.letterTo.addressName = :letterTo" +
                    " and l.letterDate = :letterDate and l.letterTheme = :letterTheme and l.letterFolder.folderName = :letterFolder"),
})
public class Letter implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long letterID;

    @ManyToOne
    private Email letterFrom;

    @ManyToOne
    private Email letterTo;

    @Column(name = "letterDate", nullable = false)
    private Date letterDate;

    @Column(name = "letterTheme", length = 55, nullable = true)
    private String letterTheme;

    @Column(name = "letterBody", columnDefinition = "LONGTEXT")
    private String letterBody;

    @ManyToOne
    private Folder letterFolder;

    @Column(name = "isNew", nullable = false)
    private boolean newLetter;

    public boolean isNewLetter() {
        return newLetter;
    }

    public void setNewLetter(boolean newLetter) {
        this.newLetter = newLetter;
    }

    public Letter() {
    }

    public long getLetterID() {
        return letterID;
    }

    public Email getLetterFrom() {
        return letterFrom;
    }

    public Email getLetterTo() {
        return letterTo;
    }

    public Date getLetterDate() {
        return letterDate;
    }

    public String getLetterTheme() {
        return letterTheme;
    }

    public String getLetterBody() {
        return letterBody;
    }

    public void setLetterID(long letterID) {
        this.letterID = letterID;
    }

    public void setLetterFrom(Email letterFrom) {
        this.letterFrom = letterFrom;
    }

    public void setLetterTo(Email letterTo) {
        this.letterTo = letterTo;
    }

    public void setLetterDate(Date letterDate) {
        this.letterDate = letterDate;
    }

    public void setLetterTheme(String letterTheme) {
        this.letterTheme = letterTheme;
    }

    public void setLetterBody(String letterBody) {
        this.letterBody = letterBody;
    }

    public Folder getLetterFolder() {
        return letterFolder;
    }

    public void setLetterFolder(Folder letterFolder) {
        this.letterFolder = letterFolder;
    }

    @Override
    public boolean equals(Object obj) {
       if (obj instanceof Letter) {
           return ((Letter) obj).getLetterFolder().getFolderName().equals(letterFolder.getFolderName()) &&
                   ((Letter) obj).getLetterTheme().equals(letterTheme) &&
                   ((Letter) obj).getLetterDate().equals(letterDate) &&
                   ((Letter) obj).getLetterFrom().getAddressName().equals(letterFrom.getAddressName()) &&
                   ((Letter) obj).getLetterTo().getAddressName().equals(letterTo.getAddressName());
       }

       return false;
    }
}

