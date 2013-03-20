package mailSystem.backEnd.entities;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "Folder")
@NamedQueries({
@NamedQuery(name = "Folder.findByAddressAndName",
query = "select f from Folder f where f.folderAddress.addressName = :addressName and f.folderName = :folderName"),
@NamedQuery(name = "Folder.findFoldersByEmail",
query = "select f from Folder f where f.folderAddress.addressName = :addressName"),
})
public class Folder implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long folderID;

    @Column(name = "folderName")
    private String folderName;

    @ManyToOne
    private Email folderAddress;

    @OneToMany(mappedBy = "letterFolder")
    private List<Letter> folderLetters;

    public Folder() {
    }

    public Folder(String folderName, Email folderAddress) {
        this.folderName = folderName;
        this.folderAddress = folderAddress;
    }


    public long getFolderID() {
        return folderID;
    }

    public String getFolderName() {
        return folderName;
    }

    public Email getFolderAddress() {
        return folderAddress;
    }


    public void setFolderID(long folderID) {
        this.folderID = folderID;
    }

    public void setFolderName(String folderName) {
        this.folderName = folderName;
    }

    public void setFolderAddress(Email folderAddress) {
        this.folderAddress = folderAddress;
    }

    public List<Letter> getFolderLetters() {
        return folderLetters;
    }

    public void setFolderLetters(List<Letter> folderLetters) {
        this.folderLetters = folderLetters;
    }

    public void addLetter(Letter letter) {
        folderLetters.add(letter);
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof Folder) {
           return ((Folder) obj).getFolderName().equals(folderName) &&
                   ((Folder) obj).getFolderAddress().getAddressName().equals(folderAddress.getAddressName());
        }

        return false;
    }
}
