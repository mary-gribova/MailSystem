package mailSystem.backEnd.excepions;

public class NoSuchLetterEntity extends Exception {
    public NoSuchLetterEntity() {
        super();
    }

    @Override
    public String getMessage() {
        return "No such letter entity";
    }
}
