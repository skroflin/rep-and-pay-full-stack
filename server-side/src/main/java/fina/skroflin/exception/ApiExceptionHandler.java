/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package fina.skroflin.exception;

import com.stripe.exception.SignatureVerificationException;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import javax.management.InstanceNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.BadRequestException;
import org.apache.tomcat.util.json.ParseException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.HttpClientErrorException;

/**
 *
 * @author skroflin
 */
@Slf4j
@ControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(value = {BadRequestException.class, IllegalArgumentException.class, IllegalStateException.class, ParseException.class})
    public ResponseEntity<Object> handleBadRequestException(Exception e) {
        HttpStatus httpStatus = HttpStatus.BAD_REQUEST;
        log.error("BAD_REQUEST: ", e);
        ApiException apiException = new ApiException(
                e.getMessage(),
                httpStatus,
                ZonedDateTime.now(ZoneId.of("Z"))
        );
        return new ResponseEntity<>(apiException, httpStatus);
    }

    @ExceptionHandler(value = {InstanceNotFoundException.class, HttpClientErrorException.NotFound.class})
    public ResponseEntity<Object> handleNotFoundException(Exception e) {
        HttpStatus httpStatus = HttpStatus.NOT_FOUND;
        log.error("NOT_FOUND: ", e);
        ApiException apiException = new ApiException(
                e.getMessage(), 
                httpStatus, 
                ZonedDateTime.now(ZoneId.of("Z"))
        );
        return new ResponseEntity<>(apiException, httpStatus);
    }
    
    @ExceptionHandler(value = {SignatureVerificationException.class})
    public ResponseEntity<Object> handleStripeSignatureException(SignatureVerificationException e) {
        HttpStatus httpStatus = HttpStatus.BAD_REQUEST;
        log.error("STRIPE_SIGNATURE_INVALID: ", e);
        ApiException apiException = new ApiException(
                "Stripe webhook signature verification failed:" + " " + e.getMessage(),
                httpStatus,
                ZonedDateTime.now(ZoneId.of("Z"))
        );
        return new ResponseEntity<>(apiException, httpStatus);
    }
}
