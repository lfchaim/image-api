package br.com.whs.imageapi.util;

import java.io.File;
import java.util.Base64;

import org.apache.commons.io.FileUtils;


public class Base64Util {
	public static void main(String[] args) throws Exception {
		File file = new File("D:\\tmp\\img\\CNH\\CNH Fernando Cinza 20181209.png");
		String encodedString = encodeFileToBase64Binary(file);
		System.out.println(encodedString);
		encodedString = FileUtil.readFile("base64image.txt");
		System.out.println(encodedString);
		File newFile = new File("D:\\tmp\\img\\CNH\\CNH Fernando Cinza 20181209 NEW.png");
		base64BinaryToEncodeFile(encodedString, newFile);
	}

	public static String encodeFileToBase64Binary(File file) throws Exception {
		byte[] fileContent = FileUtils.readFileToByteArray(file);
		String encodedString = Base64.getEncoder().encodeToString(fileContent);
		return encodedString;
	}
	
	public static void base64BinaryToEncodeFile(String encodedString, File file) throws Exception {
		byte[] decodedBytes = Base64.getDecoder().decode(encodedString);
		FileUtils.writeByteArrayToFile(file, decodedBytes);
	}
}
