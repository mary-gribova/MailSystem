package mailSystem.backEnd.excepions;

public class DublicateEntityException  extends Exception {
    public DublicateEntityException() {
        super();
    }

    @Override
    public String getMessage() {
        return "Dublicate entity";
    }
}
