package com.restful.moneytransfer;

import org.apache.log4j.Logger;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

import com.restful.moneytransfer.constants.Constants;
import com.restful.moneytransfer.controller.MoneyTransferController;
import com.restful.moneytransfer.exception.ExceptionHandler;

/**
 * @author Nawaz
 *
 */
public final class App {

    /**
     *  Logger instance for the application
     */
    public static final Logger LOGGER = Logger.getLogger(App.class);

    private App() {
    }

    /**
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {

        ServletContextHandler context = new
                ServletContextHandler(ServletContextHandler.SESSIONS);
        context.setContextPath("/");

        Server jettyServer = new Server(Constants.PORT);
        jettyServer.setHandler(context);

        ServletHolder jerseyServlet = context.addServlet(
                org.glassfish.jersey.servlet.ServletContainer.class, "/*");
        jerseyServlet.setInitOrder(0);

        jerseyServlet.setInitParameter(
                "jersey.config.server.provider.classnames",
                MoneyTransferController.class.getCanonicalName()
                + ","
                + ExceptionHandler.class.getCanonicalName());

        try {
            jettyServer.start();
            jettyServer.join();
        } finally {
            jettyServer.destroy();
        }
    }

}
