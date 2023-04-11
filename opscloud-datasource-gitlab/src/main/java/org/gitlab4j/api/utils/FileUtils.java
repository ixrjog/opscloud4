package org.gitlab4j.api.utils;

import org.gitlab4j.api.Constants.Encoding;

import jakarta.ws.rs.core.Response;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.util.Base64;
import java.util.Scanner;

/**
 * This class provides static utility methods used throughout GitLab4J.
 */
public class FileUtils {

    /**
     * Creates a File that is unique in the specified directory. If the specified
     * filename exists in the directory, "-#" will be appended to the filename until
     * a unique filename can be created.
     * 
     * @param directory the directory to create the file in
     * @param filename the base filename with extension
     * @return a File that is unique in the specified directory
     * @throws IOException if any error occurs during file creation
     */
    public static File createUniqueFile(File directory, String filename) throws IOException {

        File uniqueFile = new File(directory, filename);
        if (uniqueFile.createNewFile()) {
            return (uniqueFile);
        }

        String extension = "";
        int dotIndex = filename.lastIndexOf('.');
        if (dotIndex >= 0) {
            extension = filename.substring(dotIndex);
            filename = filename.substring(0, dotIndex);
        }

        int fileNumber = 0;
        while (!uniqueFile.createNewFile()) {
            fileNumber++;
            String numberedFilename = String.format("%s-%d%s", filename, fileNumber, extension);
            uniqueFile = new File(directory, numberedFilename);
        }

        return (uniqueFile);
    }

    /**
     * Get the filename from the "Content-Disposition" header of a JAX-RS response.
     * 
     * @param response the JAX-RS Response instance  to get the "Content-Disposition" header filename from
     * @return the filename from the "Content-Disposition" header of a JAX-RS response, or null
     * if the "Content-Disposition" header is not present in the response
     */
    public static String getFilenameFromContentDisposition(Response response) {

        String disposition = response.getHeaderString("Content-Disposition");
        if (disposition == null || disposition.trim().length() == 0) {
            return (null);
        }

        return (disposition.replaceFirst("(?i)^.*filename=\"([^\"]+)\".*$", "$1"));
    }
 
    /**
     * Reads the contents of a File to a String.
     * 
     * @param file the File instance to read the contents from
     * @return the contents of file as a String
     * @throws IOException if any errors occur while opening or reading the file
     */
    public static String readFileContents(File file) throws IOException {

        try (Scanner in = new Scanner(file)) {
            in.useDelimiter("\\Z");
            return (in.next());
        }       
    }

    /**
     * Reads the content of a Reader instance and returns it as a String.
     *
     * @param reader the Reader instance to read the content from
     * @return the content of a Reader instance as a String
     * @throws IOException if any error occurs
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
     * Reads the content of a File instance and returns it as a String of either text or base64 encoded text.
     *
     * @param file the File instance to read from
     * @param encoding whether to encode as Base64 or as Text, defaults to Text if null
     * @return the content of the File as a String
     * @throws IOException if any error occurs
     */
    public static String getFileContentAsString(File file, Encoding encoding) throws IOException {

        if (encoding == Encoding.BASE64) {

            try (FileInputStream stream = new FileInputStream(file)) {
                byte data[] = new byte[(int) file.length()];
                stream.read(data);
                return (Base64.getEncoder().encodeToString(data));
            }

        } else {
            return(new String (Files.readAllBytes(file.toPath())));
        }
    }
}
