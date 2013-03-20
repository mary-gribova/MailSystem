package mailSystem.backEnd.excepions;


public class NoSuchRecipientException extends Exception{
    public NoSuchRecipientException() {
        super();
    }

    @Override
    public String getMessage() {
        return "No such recipient";
    }
}
