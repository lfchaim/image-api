package br.com.whs.imageapi.controller;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.imageio.ImageIO;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.whs.imageapi.model.request.ImageRequest;
import br.com.whs.imageapi.util.Base64Util;
import br.com.whs.imageapi.util.FileUtil;
import br.com.whs.imageapi.util.ImageUtil;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("image")
public class ImageController {
	
	@PostMapping(path = "/info")
	public Mono<Map<String,String>> postInfo(@RequestBody ImageRequest imageRequest) {
		Map<String,String> map = new LinkedHashMap<String,String>();
		String dir = System.getProperty("java.io.tmpdir");
		File file = new File(dir+System.getProperty("file.separator")+imageRequest.getFileName());
		try {
			Base64Util.base64BinaryToEncodeFile(imageRequest.getData(), file);
			BufferedImage bi = ImageUtil.load(file);
			map.put("width", ""+bi.getWidth());
			map.put("height", ""+bi.getHeight());
			map.put("fileName", imageRequest.getFileName());
		}catch( Exception e ) {
			e.printStackTrace();
		}finally {
			if( !file.delete() )
				file.deleteOnExit();
		}
		return Mono.just(map);
	}
	
	@PostMapping(path = "/resize")
	public Mono<Map<String,String>> postResize(@RequestBody ImageRequest imageRequest, @RequestParam Float ratio) {
		Map<String,String> map = new LinkedHashMap<String,String>();
		String dir = System.getProperty("java.io.tmpdir");
		File file = new File(dir+System.getProperty("file.separator")+imageRequest.getFileName());
		try {
			Base64Util.base64BinaryToEncodeFile(imageRequest.getData(), file);
			BufferedImage bi = ImageUtil.load(file);
			BufferedImage biNew = ImageUtil.resize(bi, ratio);
			
			File newImage = new File(dir+System.getProperty("file.separator")+"RESIZED_"+imageRequest.getFileName());
			ImageIO.write(biNew, FileUtil.getExtension(imageRequest.getFileName()), newImage);
			String data = Base64Util.encodeFileToBase64Binary(newImage);
			
			map.put("data", data);
			map.put("ratio", ratio.toString());
			map.put("fileName", imageRequest.getFileName());
		}catch( Exception e ) {
			e.printStackTrace();
		}finally {
			if( !file.delete() )
				file.deleteOnExit();
		}
		return Mono.just(map);
	}
	
	@PostMapping(path = "/grayscale")
	public Mono<Map<String,String>> postGray(@RequestBody ImageRequest imageRequest) {
		Map<String,String> map = new LinkedHashMap<String,String>();
		String dir = System.getProperty("java.io.tmpdir");
		File file = new File(dir+System.getProperty("file.separator")+imageRequest.getFileName());
		try {
			Base64Util.base64BinaryToEncodeFile(imageRequest.getData(), file);
			BufferedImage bi = ImageUtil.load(file);
			BufferedImage biNew = ImageUtil.toGrayScale(bi);
			
			File newImage = new File(dir+System.getProperty("file.separator")+"GRAY_"+imageRequest.getFileName());
			ImageIO.write(biNew, FileUtil.getExtension(imageRequest.getFileName()), newImage);
			String data = Base64Util.encodeFileToBase64Binary(newImage);
			
			map.put("data", data);
			map.put("fileName", imageRequest.getFileName());
		}catch( Exception e ) {
			e.printStackTrace();
		}finally {
			if( !file.delete() )
				file.deleteOnExit();
		}
		return Mono.just(map);
	}
}
