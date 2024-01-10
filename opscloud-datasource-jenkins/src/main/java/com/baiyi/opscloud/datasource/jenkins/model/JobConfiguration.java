package com.baiyi.opscloud.datasource.jenkins.model;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Marshaller;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.List;

public class JobConfiguration {

    private String configXml;

    private final Document doc;

    private final SAXReader reader;

    public JobConfiguration(String configXml) throws DocumentException {
        this.configXml = configXml;
        reader = new SAXReader();
        doc = reader.read(new StringReader(configXml));
    }

    public JobConfiguration addStringParam(String name, String desc, String defaultValue)
            throws JAXBException, DocumentException {
        List<Node> nodes = doc.selectNodes("//hudson.model.ParametersDefinitionProperty");
        StringWriter sw = new StringWriter();
        StringParameterDefinition spd = new StringParameterDefinition(name, desc, defaultValue);
        if (null == nodes || nodes.isEmpty()) {
            ParameterDefinitions pd = new ParameterDefinitions();
            pd.addParam(spd);
            ParametersDefinitionProperty pdp = new ParametersDefinitionProperty(pd);
            JAXBContext jaxbContext = JAXBContext.newInstance(ParametersDefinitionProperty.class);
            Marshaller marshaller = jaxbContext.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FRAGMENT, true);
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            marshaller.marshal(pdp, sw);
            Document docInterlude = reader.read(new StringReader(sw.toString()));
            List<Node> propertiesNode = doc.selectNodes("//project/properties");
            for (Node node : propertiesNode) {
                if (node instanceof Element) {
                    Element e = (Element) node;
                    e.add(docInterlude.getRootElement());
                }
            }
        } else {
            JAXBContext jaxbContext = JAXBContext.newInstance(StringParameterDefinition.class);
            Marshaller marshaller = jaxbContext.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FRAGMENT, true);
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            marshaller.marshal(spd, sw);
            Document docInterlude = reader.read(new StringReader(sw.toString()));
            List<Node> propertiesNode = doc.selectNodes("//parameterDefinitions");
            for (Node node : propertiesNode) {
                if (node instanceof Element) {
                    Element e = (Element) node;
                    e.add(docInterlude.getRootElement());
                }
            }
        }
        return this;
    }

    public String asXml() {
        return this.doc.asXML();
    }

    public String getConfigXml() {
        return configXml;
    }

    public JobConfiguration setConfigXml(String configXml) {
        this.configXml = configXml;
        return this;
    }

}