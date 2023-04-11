package org.gitlab4j.api.utils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Enumeration;

public class HttpRequestUtils {

    /**
     * Build a String containing a very short multi-line dump of an HTTP request.
     * 
     * @param fromMethod the method that this method was called from
     * @param request the HTTP request build the request dump from
     * @return a String containing a very short multi-line dump of the HTTP request
     */
    public static String getShortRequestDump(String fromMethod, HttpServletRequest request) {
        return (getShortRequestDump(fromMethod, false, request));
    }

    /**
     * Build a String containing a short multi-line dump of an HTTP request.
     * 
     * @param fromMethod the method that this method was called from
     * @param request the HTTP request build the request dump from
     * @param includeHeaders if true will include the HTTP headers in the dump
     * @return a String containing a short multi-line dump of the HTTP request
     */
    public static String getShortRequestDump(String fromMethod, boolean includeHeaders, HttpServletRequest request) {

        StringBuilder dump = new StringBuilder();
        dump.append("Timestamp     : ").append(ISO8601.getTimestamp()).append("\n");
        dump.append("fromMethod    : ").append(fromMethod).append("\n");
        dump.append("Method        : ").append(request.getMethod()).append('\n');
        dump.append("Scheme        : ").append(request.getScheme()).append('\n');
        dump.append("URI           : ").append(request.getRequestURI()).append('\n');
        dump.append("Query-String  : ").append(request.getQueryString()).append('\n');
        dump.append("Auth-Type     : ").append(request.getAuthType()).append('\n');
        dump.append("Remote-Addr   : ").append(request.getRemoteAddr()).append('\n');
        dump.append("Scheme        : ").append(request.getScheme()).append('\n');
        dump.append("Content-Type  : ").append(request.getContentType()).append('\n');
        dump.append("Content-Length: ").append(request.getContentLength()).append('\n');

        if (includeHeaders) {
            dump.append("Headers       :\n");
            Enumeration<String> headers = request.getHeaderNames();
            while (headers.hasMoreElements()) {
                String header = headers.nextElement();
                dump.append("\t").append(header).append(": ").append(request.getHeader(header)).append('\n');
            }
        }

        return (dump.toString());
    }

    /**
     * Build a String containing a multi-line dump of an HTTP request.
     * 
     * @param fromMethod the method that this method was called from
     * @param request the HTTP request build the request dump from
     * @param includePostData if true will include the POST data in the dump
     * @return a String containing a multi-line dump of the HTTP request, If an error occurs,
     * the message from the exception will be returned
     */
    public static String getRequestDump(String fromMethod, HttpServletRequest request, boolean includePostData) {

        String shortDump = getShortRequestDump(fromMethod, request);
        StringBuilder buf = new StringBuilder(shortDump);
        try {

            buf.append("\nAttributes:\n");
            Enumeration<String> attrs = request.getAttributeNames();
            while (attrs.hasMoreElements()) {
                String attr = attrs.nextElement();
                buf.append("\t").append(attr).append(": ").append(request.getAttribute(attr)).append('\n');
            }

            buf.append("\nHeaders:\n");
            Enumeration<String> headers = request.getHeaderNames();
            while (headers.hasMoreElements()) {
                String header = headers.nextElement();
                buf.append("\t").append(header).append(": ").append(request.getHeader(header)).append('\n');
            }

            buf.append("\nParameters:\n");
            Enumeration<String> params = request.getParameterNames();
            while (params.hasMoreElements()) {
                String param = params.nextElement();
                buf.append("\t").append(param).append(": ").append(request.getParameter(param)).append('\n');
            }

            buf.append("\nCookies:\n");
            Cookie[] cookies = request.getCookies();
            if (cookies != null) {
                for (Cookie cookie : cookies) {
                    String cstr = "\t" + cookie.getDomain() + "." + cookie.getPath() + "." + cookie.getName() + ": " + cookie.getValue() + "\n";
                    buf.append(cstr);
                }
            }

            if (includePostData) {
                buf.append(getPostDataAsString(request)).append("\n");
            }

            return (buf.toString());

        } catch (IOException e) {
            return e.getMessage();
        }
    }

    /**
     * Reads the POST data from a request into a String and returns it.
     * 
     * @param request the HTTP request containing the POST data
     * @return the POST data as a String instance
     * @throws IOException if any error occurs while reading the POST data
     */
    public static String getPostDataAsString(HttpServletRequest request) throws IOException {

        try (InputStreamReader reader = new InputStreamReader(request.getInputStream(), "UTF-8")) {
            return (getReaderContentAsString(reader));
        }
    }

    /**
     * Reads the content of a Reader instance and returns it as a String.
     * 
     * @param reader the Reader instance to read the data from
     * @return the content of a Reader instance as a String
     * @throws IOException if any error occurs while reading the POST data
     */
    public static String getReaderContentAsString(Reader reader) throws IOException {

        int count;
        final char[] buffer = new char[2048];
        final StringBuilder out = new StringBuilder();
        while ((count = reader.read(buffer, 0, buffer.length)) >= 0) {
            out.append(buffer, 0, count);
        }

        return (out.toString());
    }

    /**
     * Masks the PRIVATE-TOKEN header value with "********".
     * 
     * @param s a String containing HTTP request info, usually logging info
     * @return a String with the PRIVATE-TOKEN header value masked with asterisks
     */
    public static String maskPrivateToken(String s) {

        if (s == null || s.isEmpty()) {
            return (s);
        }

        return (s.replaceAll("PRIVATE\\-TOKEN\\: [\\S]*", "PRIVATE-TOKEN: ********"));
    }
}