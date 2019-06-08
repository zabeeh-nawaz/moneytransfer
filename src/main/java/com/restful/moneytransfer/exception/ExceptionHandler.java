package com.restful.moneytransfer.exception;

import static com.restful.moneytransfer.App.LOGGER;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;

/**
 * @author Nawaz
 *
 */
public class ExceptionHandler implements ExceptionMapper<Exception> {
    /**
     * Serial version unique identifier
     */
    private static final long serialVersionUID = 1L;

    /*
     * (non-Javadoc)
     *
     * @see javax.ws.rs.ext.ExceptionMapper#toResponse(java.lang.Throwable)
     */
    @Override
    public Response toResponse(Exception exception) {
        LOGGER.error("ExceptionHandler.toResponse() processing " + exception);
        if (exception instanceof InsufficientFundsException) {
            return Response.status(HttpServletResponse.SC_PRECONDITION_FAILED)
                    .entity(exception.getMessage()).type("text/plain").build();
        }
        if (exception instanceof MissingResourceException
                || exception instanceof NotFoundException) {
            return Response.status(HttpServletResponse.SC_NOT_FOUND)
                    .entity(exception.getMessage()).type("text/plain").build();
        }

        return Response.status(HttpServletResponse.SC_INTERNAL_SERVER_ERROR)
                .entity("Something unexpected happened. "
                        + "Please try after some time")
                .type("text/plain").build();
    }

}
