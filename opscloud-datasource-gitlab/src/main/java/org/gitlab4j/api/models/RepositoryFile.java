
package org.gitlab4j.api.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.gitlab4j.api.Constants.Encoding;
import org.gitlab4j.api.utils.JacksonJson;

import java.util.Base64;

public class RepositoryFile {

    private String fileName; // file name only, Ex. class.rb
    private String filePath; // full path to file. Ex. lib/class.rb
    private Integer size;
    private Encoding encoding;
    private String content;
    private String contentSha256;
    private String ref;
    private String blobId;
    private String commitId;
    private String lastCommitId;

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public Encoding getEncoding() {
        return encoding;
    }

    public void setEncoding(Encoding encoding) {
        this.encoding = encoding;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getContentSha256() {
        return contentSha256;
    }

    public void setContentSha256(String contentSha256) {
        this.contentSha256 = contentSha256;
    }

    public String getRef() {
        return ref;
    }

    public void setRef(String ref) {
        this.ref = ref;
    }

    public String getBlobId() {
        return blobId;
    }

    public void setBlobId(String blobId) {
        this.blobId = blobId;
    }

    public String getCommitId() {
        return commitId;
    }

    public void setCommitId(String commitId) {
        this.commitId = commitId;
    }

    public String getLastCommitId() {
        return lastCommitId;
    }

    public void setLastCommitId(String lastCommitId) {
        this.lastCommitId = lastCommitId;
    }

    /**
     * Returns the content as a String, base64 decoding it if necessary.
     * For binary files it is recommended to use getDecodedContentAsBytes() 
     *
     * @return the content as a String, base64 decoding it if necessary
     */
    @JsonIgnore
    public String getDecodedContentAsString() {

        if (content == null) {
            return (null);
        }

        if (Encoding.BASE64.equals(encoding)) {
            return (new String(Base64.getDecoder().decode(content)));
        }

        return (content);
    }

    /**
     * Returns the content as a byte array, decoding from base64 if necessary.
     * For String content it is recommended to use getDecodedContent().
     *
     * @return the content as a byte array, decoding from base64 if necessary
     */
    @JsonIgnore
    public byte[] getDecodedContentAsBytes() {

        if (content == null) {
            return (null);
        }

        if (encoding == Encoding.BASE64) {
            return (Base64.getDecoder().decode(content));
        }

        return (content.getBytes());
    }

    /**
     * Encodes the provided String using Base64 and sets it as the content. The encoding
     * property of this instance will be set to base64.
     *
     * @param content the String content to encode and set as the base64 encoded String content
     */
    @JsonIgnore
    public void encodeAndSetContent(String content) {
        encodeAndSetContent(content != null ? content.getBytes() : null);
    }

    /**
     * Encodes the provided byte array using Base64 and sets it as the content. The encoding
     * property of this instance will be set to base64.
     *
     * @param byteContent the byte[] content to encode and set as the base64 encoded String content
     */
    @JsonIgnore
    public void encodeAndSetContent(byte[] byteContent) {

        if (byteContent == null) {
            this.content = null;
            return;
        }

        this.content = Base64.getEncoder().encodeToString(byteContent);
        encoding = Encoding.BASE64;
    }

    @Override
    public String toString() {
        return (JacksonJson.toJsonString(this));
    }
}
