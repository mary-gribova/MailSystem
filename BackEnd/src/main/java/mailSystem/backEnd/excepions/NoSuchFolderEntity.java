package mailSystem.backEnd.excepions;

public class NoSuchFolderEntity extends Exception {
    public NoSuchFolderEntity() {
        super();
    }

    @Override
    public String getMessage() {
        return "No such folder entity";
    }
}
