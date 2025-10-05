package fr.cercusmc.adventcore.utils.exceptions;

public class RegisterModuleException extends RuntimeException {

    public RegisterModuleException(String message) {
        super(message);
    }

    public RegisterModuleException(String message, Throwable cause) {
        super(message, cause);
    }
}
