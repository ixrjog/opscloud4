/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.baiyi.opscloud.datasource.jenkins.client;

import com.baiyi.opscloud.datasource.jenkins.model.BaseModel;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.entity.ContentType;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Dell Green
 */
public interface JenkinsHttpConnection extends Closeable {

    
    
    /**
     * {@inheritDoc}
     */
    @Override
    void close();


    /**
     * Perform a GET request and parse the response to the given class
     *
     * @param path path to request, can be relative or absolute
     * @param cls class of the response
     * @param <T> type of the response
     * @return an instance of the supplied class
     * @throws IOException in case of an error.
     */
    <T extends BaseModel> T get(String path, Class<T> cls) throws IOException;

    /**
     * Perform a GET request and parse the response and return a simple string
     * of the content
     *
     * @param path path to request, can be relative or absolute
     * @return the entity text
     * @throws IOException in case of an error.
     */
    String get(String path) throws IOException;

    /**
     * Perform a GET request and return the response as InputStream
     *
     * @param path path to request, can be relative or absolute
     * @return the response stream
     * @throws IOException in case of an error.
     */
    InputStream getFile(URI path) throws IOException;

    /**
     * Perform a GET request and parse the response to the given class, logging
     * any IOException that is thrown rather than propagating it.
     *
     * @param path path to request, can be relative or absolute
     * @param cls class of the response
     * @param <T> type of the response
     * @return an instance of the supplied class
     */
    <T extends BaseModel> T getQuietly(String path, Class<T> cls);

    /**
     * Check to see if the Jenkins version has been set to something different
     * than the initialisation value from the constructor. This means there has
     * never been made a communication with the Jenkins server.
     * @return true if jenkinsVersion has been set by communication, false
     * otherwise.
     */
    boolean isJenkinsVersionSet();

    /**
     * Perform a POST request and parse the response to the given class
     *
     * @param path path to request, can be relative or absolute
     * @param data data to post
     * @param cls class of the response
     * @param <R> type of the response
     * @param <D> type of the data
     * @return an instance of the supplied class
     * @throws IOException in case of an error.
     */
    <R extends BaseModel, D> R post(String path, D data, Class<R> cls) throws IOException;

    /**
     * Perform a POST request and parse the response to the given class
     *
     * @param path path to request, can be relative or absolute
     * @param data data to post
     * @param cls class of the response
     * @param <R> type of the response
     * @param <D> type of the data
     * @param crumbFlag true / false.
     * @return an instance of the supplied class
     * @throws IOException in case of an error.
     */
    <R extends BaseModel, D> R post(String path, D data, Class<R> cls, boolean crumbFlag) throws IOException;

    /**
     * Perform a POST request and parse the response to the given class
     *
     * @param path path to request, can be relative or absolute
     * @param data data to post
     * @param cls class of the response
     * @param fileParams file parameters
     * @param <R> type of the response
     * @param <D> type of the data
     * @param crumbFlag true / false.
     * @return an instance of the supplied class
     * @throws IOException in case of an error.
     */
    <R extends BaseModel, D> R post(String path, D data, Class<R> cls, Map<String, File> fileParams, boolean crumbFlag) throws IOException;

    /**
     * Perform POST request that takes no parameters and returns no response
     *
     * @param path path to request
     * @throws IOException in case of an error.
     */
    void post(String path) throws IOException;

    
    /**
     * Perform POST request that takes no parameters and returns no response
     *
     * @param path path to request
     * @param crumbFlag true / false.
     * @throws IOException in case of an error.
     */
    void post(String path, boolean crumbFlag) throws IOException;

    /**
     * Perform a POST request using form url encoding.
     *
     * This method was added for the purposes of creating folders, but may be
     * useful for other API calls as well. Unlike post and post_xml, the path is
     * *not* modified by adding "/toJsonApiUri/json". Additionally, the params
     * in data are provided as both request parameters including a json
     * formParameter, *and* in the JSON-formatted StringEntity, because this is what
     * the folder creation call required. It is unclear if any other jenkins
     * APIs operate in this fashion.
     *
     * @param path path to request, can be relative or absolute
     * @param data data to post
     * @param crumbFlag true / false.
     * @throws IOException in case of an error.
     */
    void post_form(String path, Map<String, String> data, boolean crumbFlag) throws IOException;

    /**
     * Perform a POST request using form url encoding and return HttpResponse
     * object This method is not performing validation and can be used for more
     * generic queries to jenkins.
     *
     * @param path path to request, can be relative or absolute
     * @param data data to post
     * @param crumbFlag true / false.
     * @return resulting response
     * @throws IOException in case of an error.
     */
    HttpResponse post_form_with_result(String path, List<NameValuePair> data, boolean crumbFlag) throws IOException;

    /**
     * Post a text entity to the given URL using the default content type
     *
     * @param path The path.
     * @param textData data.
     * @param crumbFlag true/false.
     * @return resulting response
     * @throws IOException in case of an error.
     */
    String post_text(String path, String textData, boolean crumbFlag) throws IOException;

    /**
     * Post a text entity to the given URL with the given content type
     *
     * @param path The path.
     * @param textData The data.
     * @param contentType {@link ContentType}
     * @param crumbFlag true or false.
     * @return resulting response
     * @throws IOException in case of an error.
     */
    String post_text(String path, String textData, ContentType contentType, boolean crumbFlag) throws IOException;

    /**
     * Perform a POST request of XML (instead of using json mapper) and return a
     * string rendering of the response entity.
     *
     * @param path path to request, can be relative or absolute
     * @param xml_data data data to post
     * @return A string containing the xml response (if present)
     * @throws IOException in case of an error.
     */
    String post_xml(String path, String xml_data) throws IOException;

    /**
     * Perform a POST request of XML (instead of using json mapper) and return a
     * string rendering of the response entity.
     *
     * @param path path to request, can be relative or absolute
     * @param xml_data data data to post
     * @param crumbFlag true or false.
     * @return A string containing the xml response (if present)
     * @throws IOException in case of an error.
     */
    String post_xml(String path, String xml_data, boolean crumbFlag) throws IOException;

    /**
     * Get the Jenkins server version.
     *
     * @return the version string
     */
    String getJenkinsVersion();

}
