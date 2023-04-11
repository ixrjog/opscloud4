
package org.gitlab4j.api.utils;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.type.CollectionType;
import org.gitlab4j.api.models.User;
import org.glassfish.jersey.jackson.internal.jackson.jaxrs.json.JacksonJaxbJsonProvider;

import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.ext.ContextResolver;
import java.io.IOException;
import java.io.Reader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Jackson JSON Configuration and utility class.
 */
@Produces(MediaType.APPLICATION_JSON)
public class JacksonJson extends JacksonJaxbJsonProvider implements ContextResolver<ObjectMapper> {

    private static final SimpleDateFormat iso8601UtcFormat;
    static {
        iso8601UtcFormat = new SimpleDateFormat(ISO8601.UTC_PATTERN);
        iso8601UtcFormat.setLenient(true);
        iso8601UtcFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
    }

    private final ObjectMapper objectMapper;

    public JacksonJson() {

        objectMapper = new ObjectMapper();

        objectMapper.setSerializationInclusion(Include.NON_NULL);
        objectMapper.setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE);

        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        objectMapper.configure(SerializationFeature.WRITE_ENUMS_USING_TO_STRING, true);
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.configure(DeserializationFeature.READ_ENUMS_USING_TO_STRING, true);

        SimpleModule module = new SimpleModule("GitLabApiJsonModule");
        module.addSerializer(Date.class, new JsonDateSerializer());
        module.addDeserializer(Date.class, new JsonDateDeserializer());
        objectMapper.registerModule(module);

        setMapper(objectMapper);
    }

    @Override
    public ObjectMapper getContext(Class<?> objectType) {
        return (objectMapper);
    }

    /**
     * Gets the ObjectMapper contained by this instance.
     * 
     * @return the ObjectMapper contained by this instance
     */
    public ObjectMapper getObjectMapper() {
        return (objectMapper);
    }

    /**
     * Reads and parses the String containing JSON data and returns a JsonNode tree representation.
     *
     * @param postData a String holding the POST data
     * @return a JsonNode instance containing the parsed JSON
     * @throws JsonParseException when an error occurs parsing the provided JSON
     * @throws JsonMappingException if a JSON error occurs
     * @throws IOException if an error occurs reading the JSON data
     */
    public JsonNode readTree(String postData) throws JsonParseException, JsonMappingException, IOException {
        return (objectMapper.readTree(postData));
    }

    /**
     * Reads and parses the JSON data on the specified Reader instance to a JsonNode tree representation.
     *
     * @param reader the Reader instance that contains the JSON data
     * @return a JsonNode instance containing the parsed JSON
     * @throws JsonParseException when an error occurs parsing the provided JSON
     * @throws JsonMappingException if a JSON error occurs
     * @throws IOException if an error occurs reading the JSON data
     */
    public JsonNode readTree(Reader reader) throws JsonParseException, JsonMappingException, IOException {
        return (objectMapper.readTree(reader));
    }

    /**
     * Unmarshal the JsonNode (tree) to an instance of the provided class.
     *
     * @param <T> the generics type for the return value
     * @param returnType an instance of this type class will be returned
     * @param tree the JsonNode instance that contains the JSON data
     * @return an instance of the provided class containing the  data from the tree
     * @throws JsonParseException when an error occurs parsing the provided JSON
     * @throws JsonMappingException if a JSON error occurs
     * @throws IOException if an error occurs reading the JSON data
     */
    public <T> T unmarshal(Class<T> returnType, JsonNode tree) throws JsonParseException, JsonMappingException, IOException {
        ObjectMapper objectMapper = getContext(returnType);
        return (objectMapper.treeToValue(tree, returnType));
    }

    /**
     * Unmarshal the JSON data on the specified Reader instance to an instance of the provided class.
     *
     * @param <T> the generics type for the return value
     * @param returnType an instance of this type class will be returned
     * @param reader the Reader instance that contains the JSON data
     * @return an instance of the provided class containing the parsed data from the Reader
     * @throws JsonParseException when an error occurs parsing the provided JSON
     * @throws JsonMappingException if a JSON error occurs
     * @throws IOException if an error occurs reading the JSON data
     */
    public <T> T unmarshal(Class<T> returnType, Reader reader) throws JsonParseException, JsonMappingException, IOException {
        ObjectMapper objectMapper = getContext(returnType);
        return (objectMapper.readValue(reader, returnType));
    }

    /**
     * Unmarshal the JSON data contained by the string and populate an instance of the provided returnType class.
     *
     * @param <T> the generics type for the return value
     * @param returnType an instance of this type class will be returned
     * @param postData a String holding the POST data
     * @return an instance of the provided class containing the parsed data from the string
     * @throws JsonParseException when an error occurs parsing the provided JSON
     * @throws JsonMappingException if a JSON error occurs
     * @throws IOException if an error occurs reading the JSON data
     */
    public <T> T unmarshal(Class<T> returnType, String postData) throws JsonParseException, JsonMappingException, IOException {
        ObjectMapper objectMapper = getContext(returnType);
        return (objectMapper.readValue(postData, returnType));
    }

    /**
     * Unmarshal the JSON data on the specified Reader instance and populate a List of instances of the provided returnType class.
     *
     * @param <T> the generics type for the List
     * @param returnType an instance of this type class will be contained in the returned List
     * @param reader the Reader instance that contains the JSON data
     * @return a List of the provided class containing the parsed data from the Reader
     * @throws JsonParseException when an error occurs parsing the provided JSON
     * @throws JsonMappingException if a JSON error occurs
     * @throws IOException if an error occurs reading the JSON data
     */
    public <T> List<T> unmarshalList(Class<T> returnType, Reader reader) throws JsonParseException, JsonMappingException, IOException {
        ObjectMapper objectMapper = getContext(null);
        CollectionType javaType = objectMapper.getTypeFactory().constructCollectionType(List.class, returnType);
        return (objectMapper.readValue(reader, javaType));
    }

    /**
     * Unmarshal the JSON data contained by the string and populate a List of instances of the provided returnType class.
     *
     * @param <T> the generics type for the List
     * @param returnType an instance of this type class will be contained in the returned List
     * @param postData a String holding the POST data
     * @return a List of the provided class containing the parsed data from the string
     * @throws JsonParseException when an error occurs parsing the provided JSON
     * @throws JsonMappingException if a JSON error occurs
     * @throws IOException if an error occurs reading the JSON data
     */
    public <T> List<T> unmarshalList(Class<T> returnType, String postData) throws JsonParseException, JsonMappingException, IOException {
        ObjectMapper objectMapper = getContext(null);
        CollectionType javaType = objectMapper.getTypeFactory().constructCollectionType(List.class, returnType);
        return (objectMapper.readValue(postData, javaType));
    }

    /**
     * Unmarshal the JSON data on the specified Reader instance and populate a Map of String keys and values of the provided returnType class.
     *
     * @param <T> the generics type for the Map value
     * @param returnType an instance of this type class will be contained the values of the Map
     * @param reader the Reader instance that contains the JSON data
     * @return a Map containing the parsed data from the Reader
     * @throws JsonParseException when an error occurs parsing the provided JSON
     * @throws JsonMappingException if a JSON error occurs
     * @throws IOException if an error occurs reading the JSON data
     */
    public <T> Map<String, T> unmarshalMap(Class<T> returnType, Reader reader) throws JsonParseException, JsonMappingException, IOException {
        ObjectMapper objectMapper = getContext(null);
        return (objectMapper.readValue(reader, new TypeReference<Map<String, T>>() {}));
    }

    /**
     * Unmarshal the JSON data and populate a Map of String keys and values of the provided returnType class.
     *
     * @param <T> the generics type for the Map value
     * @param returnType an instance of this type class will be contained the values of the Map
     * @param jsonData the String containing the JSON data
     * @return a Map containing the parsed data from the String
     * @throws JsonParseException when an error occurs parsing the provided JSON
     * @throws JsonMappingException if a JSON error occurs
     * @throws IOException if an error occurs reading the JSON data
     */
    public <T> Map<String, T> unmarshalMap(Class<T> returnType, String jsonData) throws JsonParseException, JsonMappingException, IOException {
        ObjectMapper objectMapper = getContext(null);
        return (objectMapper.readValue(jsonData, new TypeReference<Map<String, T>>() {}));
    }

    /**
     * Marshals the supplied object out as a formatted JSON string.
     * 
     * @param <T> the generics type for the provided object
     * @param object the object to output as a JSON string
     * @return a String containing the JSON for the specified object
     */
    public <T> String marshal(final T object) {

        if (object == null) {
            throw new IllegalArgumentException("object parameter is null");
        }

        ObjectWriter writer = objectMapper.writer().withDefaultPrettyPrinter();
        String results = null;
        try {
            results = writer.writeValueAsString(object);
        } catch (JsonGenerationException e) {
            System.err.println("JsonGenerationException, message=" + e.getMessage());
        } catch (JsonMappingException e) {
            e.printStackTrace();
            System.err.println("JsonMappingException, message=" + e.getMessage());
        } catch (IOException e) {
            System.err.println("IOException, message=" + e.getMessage());
        }

        return (results);
    }

    /**
     * JsonSerializer for serializing dates s yyyy-mm-dd in UTC timezone.
     */
    public static class DateOnlySerializer extends JsonSerializer<Date> {

        @Override
        public void serialize(Date date, JsonGenerator gen, SerializerProvider provider) throws IOException, JsonProcessingException {
            String dateString = ISO8601.dateOnly(date);
            gen.writeString(dateString);
        }
    }

    /**
     * JsonSerializer for serializing ISO8601 formatted dates.
     */
    public static class JsonDateSerializer extends JsonSerializer<Date> {

        @Override
        public void serialize(Date date, JsonGenerator gen, SerializerProvider provider) throws IOException, JsonProcessingException {
            String iso8601String = ISO8601.toString(date);
            gen.writeString(iso8601String);
        }
    }

    /**
     * JsonDeserializer for deserializing ISO8601 formatted dates.
     */
    public static class JsonDateDeserializer extends JsonDeserializer<Date> {

        @Override
        public Date deserialize(JsonParser jsonparser, DeserializationContext context) throws IOException, JsonProcessingException {

            try {
                return (ISO8601.toDate(jsonparser.getText()));
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * Serializer for the odd User instances in the "approved_by" array in the merge_request JSON.
     */
    public static class UserListSerializer extends JsonSerializer<List<User>> {

        @Override
        public void serialize(List<User> value, JsonGenerator jgen,
                SerializerProvider provider) throws IOException,
                JsonProcessingException {

            jgen.writeStartArray();
            for (User user : value) {
                jgen.writeStartObject();
                jgen.writeObjectField("user", user);
                jgen.writeEndObject();
            }
            jgen.writeEndArray();
        }
    }

    /**
     * Deserializer for the odd User instances in the "approved_by" array in the merge_request JSON.
     */
    public static class UserListDeserializer extends JsonDeserializer<List<User>> {

        private static final ObjectMapper mapper = new JacksonJson().getObjectMapper();

        @Override
        public List<User> deserialize(JsonParser jsonParser, DeserializationContext context)
                throws IOException, JsonProcessingException {

            JsonNode tree = jsonParser.readValueAsTree();
            int numUsers = tree.size();
            List<User> users = new ArrayList<>(numUsers);
            for (int i = 0; i < numUsers; i++) {
                JsonNode node = tree.get(i);
                JsonNode userNode = node.get("user");
                User user = mapper.treeToValue(userNode,  User.class);
                users.add(user);
            }

            return (users);
        }
    }

    /**
     * This class is used to create a thread-safe singleton instance of JacksonJson customized
     * to be used by
     */
    private static class JacksonJsonSingletonHelper {
        private static final JacksonJson JACKSON_JSON = new JacksonJson();
        static {
            JACKSON_JSON.objectMapper.setPropertyNamingStrategy(PropertyNamingStrategy.LOWER_CAMEL_CASE);
            JACKSON_JSON.objectMapper.setSerializationInclusion(Include.ALWAYS);
        }
    }

    /**
     * Gets a the supplied object output as a formatted JSON string.  Null properties will
     * result in the value of the property being null.  This is meant to be used for
     * toString() implementations of GitLab4J classes.
     *
     * @param <T> the generics type for the provided object
     * @param object the object to output as a JSON string
     * @return a String containing the JSON for the specified object
     */
    public static <T> String toJsonString(final T object) {
        return (JacksonJsonSingletonHelper.JACKSON_JSON.marshal(object));
    }

    /**
     * Parse the provided String into a JsonNode instance.
     *
     * @param jsonString a String containing JSON to parse
     * @return a JsonNode with the String parsed into a JSON tree
     * @throws IOException if any IO error occurs
     */
    public static JsonNode toJsonNode(String jsonString) throws IOException {
        return (JacksonJsonSingletonHelper.JACKSON_JSON.objectMapper.readTree(jsonString));
    }
}
