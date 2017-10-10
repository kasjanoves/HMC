package com.example.model;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.mp4parser.Box;
import org.mp4parser.Container;
import org.mp4parser.IsoFile;
import org.mp4parser.tools.Path;

public class VideoMetadataReaderImpl implements MediaMetadataReader {
	private Map<String, Set<String>> destination;
	private Map<String, Map<String, String>> mmap = new HashMap<String, Map<String, String>>();
				
	public VideoMetadataReaderImpl(Map<String, Map<String, Set<String>>> reqMetadata) {
		destination = reqMetadata.get("video");
	}

	public Map<String, Map<String, String>> extractMetadata(File file) throws IOException {
		
		if (!file.exists()) {
            throw new FileNotFoundException("File " + file.getPath() + " not exists");
        }

        if (!file.canRead()) {
            throw new IllegalStateException("No read permissions to file " + file.getPath());
        }
        FileInputStream fis = new FileInputStream(file);
        IsoFile isoFile = new IsoFile(fis.getChannel());
		
        //Print(isoFile.getMovieBox(), "");
//        Box b = Path.getPath(isoFile, "moov/trak[0]/tkhd");
//        System.out.println(b);
        
        for(Entry<String, Set<String>> dir : destination.entrySet()) {
        	try {
        		Map<String, String> mTags = new HashMap<String, String>();
        		Box box = Path.getPath(isoFile, dir.getKey());
        		//System.out.println(box);
        		Map<String, String> TagPairs = parseTagPairs(box.toString());
        		for(String tag : dir.getValue()) {
        			String value = TagPairs.get(tag);
        			if(value != null) mTags.put(tag, value); 
        		}
        		if(!mTags.isEmpty()) mmap.put(box.getClass().getSimpleName(), mTags);	
        	}catch (Exception e) {
				System.out.println(e);
				continue;
			}	
        }	
        
        isoFile.close();
        fis.close();
        
        System.out.println(mmap);
		return mmap;
	}
	
	private void Print(Box box, String tabs) {
		if(box instanceof Container) {
			System.out.println(tabs+box.getType()+"\\");
			for(Box ch : ((Container) box).getBoxes())
				Print(ch, tabs+"\t");
		}else
			System.out.println(tabs+box.getType()+":"+box);
	}

	private Map<String, String> parseTagPairs(String boxStr) {
		Map<String, String> map = new HashMap<String, String>();
		Matcher m = Pattern.compile("\\[.+\\]").matcher(boxStr);
		Pattern DatePtr = Pattern.compile("\\w{3} \\w{3} \\d\\d \\d\\d:\\d\\d:\\d\\d \\w{3} \\d{4}");
		if(m.find()){
			String pairs = m.group().substring(1, m.group().length()-1);
			for(String pair : pairs.split(";")){
				String[] entry = pair.split("=");
				//SimpleDateFormat
				if(entry.length == 2) {
					String value = entry[1];
					m = DatePtr.matcher(entry[1]);
					if(m.find()) {
						SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy");
						Date date = sdf.parse(value, new ParsePosition(0));
						if(date != null) {
							sdf.applyPattern("yyyy-MM-dd HH:mm:ss");
							value = sdf.format(date);
							System.out.println(value);
						}
					}
					map.put(entry[0], value);
				}	
			}	
		}
		return map;
	}
		

}
