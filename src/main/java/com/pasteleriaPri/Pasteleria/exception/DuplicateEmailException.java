package com.pasteleriaPri.Pasteleria.exception;

/**
 * Thrown when a registration tries to reuse an email that already belongs to a
 * client. Email is the login identifier, so two rows sharing one would make
 * {@code ClientService.login} ambiguous.
 *
 * Deliberately extends RuntimeException, NOT NullPointerException. Its sibling
 * NotFoundException in this package extends NullPointerException, which is a
 * mistake documented in CLAUDE.md: it means a normal
 * {@code catch (RuntimeException e)} silently fails to catch it. Do not copy
 * that here - this one behaves the way a caller expects.
 */
public class DuplicateEmailException extends RuntimeException {
    public DuplicateEmailException(String message) {
        super(message);
    }
}
