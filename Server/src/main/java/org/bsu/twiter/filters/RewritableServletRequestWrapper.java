package org.bsu.twiter.filters;

import org.bsu.twiter.dao.TwitDAOImpl;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.*;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

public class RewritableServletRequestWrapper extends HttpServletRequestWrapper {

    private static Logger logger;

    static {
        try {
            LogManager.getLogManager().readConfiguration(
                    RewritableServletRequestWrapper.class.getClassLoader().getResourceAsStream("logger.properties"));
            logger = Logger.getLogger(TwitDAOImpl.class.getName());
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

    private final HttpServletRequest request;
    private RewritableServletInputStream servletStream;

    public RewritableServletRequestWrapper(HttpServletRequest request) {
        super(request);
        this.request = request;
    }

    public void resetInputStream(byte[] newRawData) {
        servletStream = new RewritableServletInputStream(new ByteArrayInputStream(newRawData));
    }

    @Override
    public ServletInputStream getInputStream() throws IOException {
        if(servletStream == null) {
            return new RewritableServletInputStream(request.getInputStream());
        }

        return servletStream;
    }

    @Override
    public BufferedReader getReader() throws IOException {
        if(servletStream == null) {
            return request.getReader();
        }

        return new BufferedReader(new InputStreamReader(servletStream));
    }

    private static class RewritableServletInputStream extends ServletInputStream {

        private final InputStream inputStream;

        RewritableServletInputStream(InputStream inputStream) {
            this.inputStream = inputStream;
        }

        @Override
        public int read() throws IOException {
            return inputStream.read();
        }

        @Override
        public void setReadListener(ReadListener readListener) {
        }

        @Override
        public boolean isReady() {
            return true;
        }

        @Override
        public boolean isFinished() {
            try {
                return inputStream.available() == 0;
            } catch (IOException e) {
                logger.log(Level.SEVERE, e.getMessage());
            }

            return false;
        }
    }
}
