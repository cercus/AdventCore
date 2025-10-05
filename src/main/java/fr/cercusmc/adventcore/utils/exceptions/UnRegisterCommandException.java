package fr.cercusmc.adventcore.utils.exceptions;

public class UnRegisterCommandException extends RuntimeException {

    public UnRegisterCommandException(String message) {
        super(message);
    }

    public UnRegisterCommandException(String message, Throwable cause) {
        super(message, cause);
    }
}
