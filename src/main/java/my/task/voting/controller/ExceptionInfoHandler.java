package my.task.voting.controller;

import my.task.voting.util.ValidationUtil;
import my.task.voting.util.exception.ChangeUnacceptableException;
import my.task.voting.util.exception.ErrorInfo;
import my.task.voting.util.exception.NotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@ControllerAdvice(annotations = RestController.class)
@Order(Ordered.HIGHEST_PRECEDENCE + 5)
public class ExceptionInfoHandler {

    private static Logger LOG = LoggerFactory.getLogger(ExceptionInfoHandler.class);

    //  http://stackoverflow.com/a/22358422/548473

    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    @ExceptionHandler(NotFoundException.class)
    @ResponseBody
    public ErrorInfo handleError(HttpServletRequest req, NotFoundException e) {
        return logAndGetErrorInfo(req, e, false);
    }

    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    @ExceptionHandler(ChangeUnacceptableException.class)
    @ResponseBody
    public ErrorInfo handleError(HttpServletRequest req, ChangeUnacceptableException e) {
        return logAndGetErrorInfo(req, e, false);
    }

    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody
    public ErrorInfo restValidationError(HttpServletRequest req, MethodArgumentNotValidException e) {
        return logAndGetValidationErrorInfo(req, e);
    }

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(DataIntegrityViolationException.class)
    @ResponseBody
    public ErrorInfo conflict(HttpServletRequest req, DataIntegrityViolationException e) {
        String rootCause = ValidationUtil.getRootCause(e).getLocalizedMessage();
        String cause = ValidationUtil.getRootCause(e).getClass().getSimpleName();
        String requestURI = req.getRequestURI();
        if (rootCause.contains("MEALS_UNIQUE_CREATED_LUNCH_DISHNAME_IDX")) {
            return new ErrorInfo(requestURI, cause,
                    "Dishes with the same name are not allowed in one lunch");
        } else if (rootCause.contains("LUNCHES_UNIQUE_CREATED_RESTAURANT_IDX")) {
            return new ErrorInfo(requestURI, cause,
                    "Only one lunch with such name of the restaurant is possible for this date");
        } else if (rootCause.contains("VOTES_UNIQUE_VOTINGDATE_USER_IDX")) {
            return new ErrorInfo(requestURI, cause,
                    "Only one vote per day for the user is possible");
        }
        return logAndGetErrorInfo(req, e, true);
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public ErrorInfo handleError(HttpServletRequest req, Exception e) {
        return logAndGetErrorInfo(req, e, true);
    }

    private ErrorInfo logAndGetValidationErrorInfo(HttpServletRequest req, MethodArgumentNotValidException e) {
        String[] details = e.getBindingResult().getFieldErrors().stream()
                .map(fe -> fe.getField() + " " + fe.getDefaultMessage())
                .toArray(String[]::new);
        return new ErrorInfo(req.getRequestURL(), "ValidationException", details);
    }

    private static ErrorInfo logAndGetErrorInfo(HttpServletRequest req, Exception e, boolean logException) {
        Throwable rootCause = ValidationUtil.getRootCause(e);
        if (logException) {
            LOG.error("Exception at request " + req.getRequestURL(), rootCause);
        } else {
            LOG.warn("Exception at request " + req.getRequestURL() + ": " + rootCause.toString());
        }
        return new ErrorInfo(req.getRequestURL(), rootCause);
    }
}
