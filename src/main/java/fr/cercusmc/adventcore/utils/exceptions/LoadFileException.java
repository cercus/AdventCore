package fr.cercusmc.adventcore.utils.exceptions;

public class LoadFileException extends RuntimeException {
    public LoadFileException(String message) {
        super(message);
    }
    public LoadFileException(String message, Throwable cause) {
        super(message, cause);
    }
}
