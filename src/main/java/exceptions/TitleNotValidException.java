package exceptions;

public class TitleNotValidException extends RuntimeException {

    public TitleNotValidException(String message) {
        super(message);
    }
}
