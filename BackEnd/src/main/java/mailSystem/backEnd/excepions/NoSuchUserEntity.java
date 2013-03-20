package mailSystem.backEnd.excepions;

public class NoSuchUserEntity extends Exception {
    public NoSuchUserEntity() {
        super();
    }

    @Override
    public String getMessage() {
        return "No such user entity";
    }
}
