package fr.cercusmc.adventcore.utils.exceptions;

public class RegisterCommandException extends RuntimeException {

    public RegisterCommandException(String message) {
        super(message);
    }

    public RegisterCommandException(String message, Throwable cause) {
        super(message, cause);
    }
}
