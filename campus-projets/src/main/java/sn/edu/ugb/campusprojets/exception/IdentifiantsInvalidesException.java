
package sn.edu.ugb.campusprojets.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class IdentifiantsInvalidesException
        extends RuntimeException {

    public IdentifiantsInvalidesException(String message) {
        super(message);
    }
}
