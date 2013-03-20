package mailSystem.backEnd.excepions;

public class NoSuchUserEntityException extends Exception {
    public NoSuchUserEntityException() {
        super();
    }

    @Override
    public String getMessage() {
        return "No such user entity";
    }
}
