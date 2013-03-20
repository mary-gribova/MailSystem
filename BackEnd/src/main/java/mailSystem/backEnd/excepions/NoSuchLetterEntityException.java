package mailSystem.backEnd.excepions;

public class NoSuchLetterEntityException extends Exception {
    public NoSuchLetterEntityException() {
        super();
    }

    @Override
    public String getMessage() {
        return "No such letter entity";
    }
}
