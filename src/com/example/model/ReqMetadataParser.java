package com.example.model;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

public class ReqMetadataParser extends DefaultHandler {
	
	private Set<MetadataTag> mset;
	private String destination = "";
	private String directory = "";
	
	
	public void startDocument() throws SAXException {
		mset = new HashSet<MetadataTag>();
    }
	
	public void startElement(String namespaceURI,
            String localName,
            String qName, 
            Attributes atts) {
		
		String Name = atts.getValue("name");
		if(localName.equals("destination")) {
			destination = Name;
		}else if(localName.equals("directory")) {
			directory = Name;
		}else if(localName.equals("tag")) {
			if(!destination.isEmpty() && !directory.isEmpty()) {
				MetadataTag newTag = new MetadataTag(destination, directory,
						Name, atts.getValue("type"));
				mset.add(newTag);
			}
		}
			
	}
	
	public void endDocument() throws SAXException {
		
	}
	
	private static String convertToFileURL(String filename) {
		String path = new File(filename).getAbsolutePath();
        if (File.separatorChar != '/') {
            path = path.replace(File.separatorChar, '/');
        }

        if (!path.startsWith("/")) {
            path = "/" + path;
        }
        return "file:" + path;
	}
	
	public Set<MetadataTag> parse(String filename) throws ParserConfigurationException,
											SAXException, IOException{
		SAXParserFactory spf = SAXParserFactory.newInstance();
	    spf.setNamespaceAware(true);
		SAXParser saxParser = spf.newSAXParser();
		XMLReader xmlReader = saxParser.getXMLReader();
		xmlReader.setContentHandler(this);
		xmlReader.parse(convertToFileURL(filename));
		return mset;
	}
		 
	
}
