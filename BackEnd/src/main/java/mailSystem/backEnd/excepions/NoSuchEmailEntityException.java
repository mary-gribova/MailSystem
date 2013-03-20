package mailSystem.backEnd.excepions;

public class NoSuchEmailEntityException extends Exception {
    public NoSuchEmailEntityException() {
        super();
    }

    @Override
    public String getMessage() {
        return "No such email entity";
    }
}
