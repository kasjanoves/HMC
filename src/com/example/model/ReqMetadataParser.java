package com.example.model;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

public class ReqMetadataParser extends DefaultHandler {
	
	private Map<String,Map<String,Set<String>>> mmap;
	private Map<String,Set<String>> destination = null;
	private Set<String> directory = null;
	
	
	public void startDocument() throws SAXException {
		mmap = new HashMap<String,Map<String,Set<String>>>();
    }
	
	public void startElement(String namespaceURI,
            String localName,
            String qName, 
            Attributes atts) {
		
		String Name = atts.getValue("name");
		if(localName.equals("destination")) {
			destination = mmap.get(Name);
			if(destination == null) {
				destination = new HashMap<String,Set<String>>();
				mmap.put(Name, destination);
			}
		}else if(localName.equals("directory")) {
			if(destination != null) {
				directory = destination.get(Name);
				if(directory == null) {
					directory = new HashSet<String>();
					destination.put(Name, directory);
				}
			}
		}else if(localName.equals("tag")) {
			if(directory != null) {
				directory.add(Name);
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
	
	public Map<String,Map<String,Set<String>>> parse(String filename) throws ParserConfigurationException,
											SAXException, IOException{
		SAXParserFactory spf = SAXParserFactory.newInstance();
	    spf.setNamespaceAware(true);
		SAXParser saxParser = spf.newSAXParser();
		XMLReader xmlReader = saxParser.getXMLReader();
		xmlReader.setContentHandler(this);
		xmlReader.parse(convertToFileURL(filename));
		return mmap;
	}
		 
	
}
