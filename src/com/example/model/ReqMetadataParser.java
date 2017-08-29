package com.example.model;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

public class ReqMetadataParser extends DefaultHandler {
	
	Map<String,Map<String,String>> mmap;
	
	public void startDocument() throws SAXException {
		mmap = new HashMap<String,Map<String,String>>();
    }
	
	public void startElement(String namespaceURI,
            String localName,
            String qName, 
            Attributes atts) {
		
		if(localName.equals("destination")) {
			Map<String, String> dest = mmap.get(atts.getValue("name"));
			if(dest == null) {
				dest = new HashMap<String, String>();
				mmap.put(atts.getValue("name"), dest);
			}
		}
			
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
	
	public Map<String,Map<String,String>> parse(String filename) throws ParserConfigurationException, SAXException, IOException{
		SAXParserFactory spf = SAXParserFactory.newInstance();
	    spf.setNamespaceAware(true);
		SAXParser saxParser = spf.newSAXParser();
		XMLReader xmlReader = saxParser.getXMLReader();
		xmlReader.setContentHandler(new ReqMetadataParser());
		xmlReader.parse(convertToFileURL(filename));
		return mmap;
	}
	
}
