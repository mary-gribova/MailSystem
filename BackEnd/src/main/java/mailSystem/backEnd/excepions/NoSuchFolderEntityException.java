package mailSystem.backEnd.excepions;

public class NoSuchFolderEntityException extends Exception {
    public NoSuchFolderEntityException() {
        super();
    }

    @Override
    public String getMessage() {
        return "No such folder entity";
    }
}
