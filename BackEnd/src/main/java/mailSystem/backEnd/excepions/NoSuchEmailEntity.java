package mailSystem.backEnd.excepions;

public class NoSuchEmailEntity extends Exception {
    public NoSuchEmailEntity() {
        super();
    }

    @Override
    public String getMessage() {
        return "No such email entity";
    }
}
