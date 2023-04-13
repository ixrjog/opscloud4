package com.baiyi.opscloud.datasource.jenkins.client.util;

import org.apache.http.client.methods.HttpRequestBase;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Releases the http request method if it is closed
 */
public class RequestReleasingInputStream extends FilterInputStream {
    private final HttpRequestBase httpRequestBase;

    /**
     * Creates a <code>FilterInputStream</code> by assigning the argument
     * <code>in</code> to the field <code>this.in</code> so as to remember it
     * for later use.
     *
     * @param in
     *            the underlying input stream, or <code>null</code> if this
     *            instance is to be created without an underlying stream.
     * @param httpRequestBase
     *            The request object that should be released if the stream
     *            closed
     */
    public RequestReleasingInputStream(InputStream in, HttpRequestBase httpRequestBase) {
        super(in);
        this.httpRequestBase = httpRequestBase;
    }

    /**
     * Closes this input stream and releases any system resources associated
     * with the stream. This method simply performs <code>in.close()</code>.
     *
     * @throws IOException
     *             if an I/O error occurs.
     * @see FilterInputStream#in
     */
    @Override
    public void close() throws IOException {
        super.close();
        httpRequestBase.releaseConnection();
    }

}