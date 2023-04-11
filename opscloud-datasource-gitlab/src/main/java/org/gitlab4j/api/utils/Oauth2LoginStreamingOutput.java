package org.gitlab4j.api.utils;

import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.StreamingOutput;
import java.io.*;
import java.nio.charset.StandardCharsets;

/**
 * This StreamingOutput implementation is utilized to send a OAuth2 token request
 * in a secure manner.  The password is never copied to a String, instead it is
 * contained in a SecretString that is cleared when an instance of this class is finalized.
 */
public class Oauth2LoginStreamingOutput implements StreamingOutput, AutoCloseable {

    private final String username;
    private final SecretString password;

    public Oauth2LoginStreamingOutput(String username, CharSequence password) {
        this.username = username;
        this.password = new SecretString(password);
    }

    public Oauth2LoginStreamingOutput(String username, char[] password) {
        this.username = username;
        this.password = new SecretString(password);
    }

    @Override
    public void write(OutputStream output) throws IOException, WebApplicationException {

        Writer writer = new BufferedWriter(new OutputStreamWriter(output, StandardCharsets.UTF_8));
        writer.write("{ ");
        writer.write("\"grant_type\": \"password\", ");
        writer.write("\"username\": \"" + username + "\", ");
        writer.write("\"password\": ");

        // Output the quoted password
        writer.write('"');
        for (int i = 0, length = password.length(); i < length; i++) {

            char c = password.charAt(i);
            if (c == '"' || c == '\\') {
                writer.write('\\');
            }

            writer.write(c);
        }

        writer.write('"');

        writer.write(" }");
        writer.flush();
        writer.close();
    }

    /**
     * Clears the contained password's data.
     */
    public void clearPassword() {
        password.clear();
    }

    @Override
    public void close() {
        clearPassword();
    }

    @Override
    public void finalize() throws Throwable {
        clearPassword();
        super.finalize();
    }
}