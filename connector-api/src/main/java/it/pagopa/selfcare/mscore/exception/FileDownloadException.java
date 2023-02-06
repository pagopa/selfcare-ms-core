package it.pagopa.selfcare.mscore.exception;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class FileDownloadException extends RuntimeException {

    public FileDownloadException(Throwable cause) {
        super(cause);
    }
}
