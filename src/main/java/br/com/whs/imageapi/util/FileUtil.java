package br.com.whs.imageapi.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class FileUtil {

	public static String readFile(String fileName) {
		StringBuffer ret = null;
		BufferedReader br = null;
		FileReader fr = null;
		try {
			fr = new FileReader(fileName);
			br = new BufferedReader(fr);
			// read line by line
			String line;
			while ((line = br.readLine()) != null) {
				if( ret == null )
					ret = new StringBuffer();
				ret.append(line);
			}

		} catch (IOException e) {
			System.err.format("IOException: %s%n", e);
		} finally {
			try {
				if (br != null)
					br.close();
				if (fr != null)
					fr.close();
			} catch (IOException ex) {
				System.err.format("IOException: %s%n", ex);
			}
		}

		return ret.toString();
	}
	
	public static boolean writeFile( String path, String fileName, String content ) {
		BufferedWriter bw = null;
		FileWriter fw = null;
		boolean ret = false;
		try {
			fw = new FileWriter(new File(path,fileName));
			bw = new BufferedWriter(fw);
			bw.write(content);
			bw.flush();
			ret = true;
		} catch (Exception e) {
			// TODO: handle exception
		} finally {
			try {bw.close();}catch(Exception e) {}
			try {fw.close();}catch(Exception e) {}
		}
		return ret;
	}
	
	public static String getExtension( String fileName ) {
		if( fileName != null ) {
			return fileName.substring(fileName.lastIndexOf(".")+1);
		} else {
			return null;
		}
	}
}
