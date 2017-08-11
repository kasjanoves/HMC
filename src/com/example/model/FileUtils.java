package com.example.model;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.EnumMap;

public class FileUtils {

	public static EnumMap<BasicFileAttrs, String> getBasicFileAttributes(File file) throws IOException {
		BasicFileAttributes attr = Files.readAttributes(file.toPath(), BasicFileAttributes.class);
		EnumMap<BasicFileAttrs, String> attrMap =
				new EnumMap<BasicFileAttrs,String>
					(BasicFileAttrs.class);
		attrMap.put(BasicFileAttrs.CREATION_TIME, attr.creationTime().toString());
		attrMap.put(BasicFileAttrs.LAST_MODIFIED_TIME, attr.lastModifiedTime().toString());
		return attrMap;
	}
}
