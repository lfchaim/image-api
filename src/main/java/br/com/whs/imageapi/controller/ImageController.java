package br.com.whs.imageapi.controller;

import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.imageio.ImageIO;

import org.apache.commons.imaging.ImageInfo;
import org.apache.commons.imaging.Imaging;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.whs.imageapi.model.request.ImageRequest;
import br.com.whs.imageapi.util.Base64Util;
import br.com.whs.imageapi.util.FileUtil;
import br.com.whs.imageapi.util.ImageUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/image")
@Api(value = "Image", description = "API para tratamento de imagens")
public class ImageController {
	
	@PostMapping(path = "/info")
	@ApiOperation(value = "Obtém informações da imagem Base64")
	@ApiResponses(value = {
	    @ApiResponse(code = 200, message = "Successfully retrieved list"),
	    @ApiResponse(code = 401, message = "You are not authorized to view the resource"),
	    @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
	    @ApiResponse(code = 404, message = "The resource you were trying to reach is not found")
	})
	public Mono<Map<String,Object>> postInfo(@RequestBody ImageRequest imageRequest) {
		Map<String,Object> map = new LinkedHashMap<String,Object>();
		String dir = System.getProperty("java.io.tmpdir");
		File file = new File(dir+System.getProperty("file.separator")+imageRequest.getFileName());
		try {
			Base64Util.base64BinaryToEncodeFile(imageRequest.getData(), file);
			ImageInfo imageInfo = Imaging.getImageInfo(file);
			ObjectMapper om = new ObjectMapper();
			
			//int physicalWidthDpi = imageInfo.getPhysicalWidthDpi();
			//int physicalHeightDpi = imageInfo.getPhysicalHeightDpi();
			
			BufferedImage bi = ImageUtil.load(file);
			//map.put("width", ""+bi.getWidth());
			//map.put("height", ""+bi.getHeight());
			//map.put("physicalWidthDpi", ""+physicalWidthDpi);
			//map.put("physicalHeightDpi", ""+physicalHeightDpi);
			Dimension dim = Imaging.getImageSize(file);
			//map.put("dimension", dim.toString());
			map.put("fileName", imageRequest.getFileName());
			
			String strImageInfo = om.writeValueAsString(imageInfo);
			//strImageInfo = strImageInfo.replace("\\", "");
			map.put("imageInfo",imageInfo);
		}catch( Exception e ) {
			e.printStackTrace();
		}finally {
			if( !file.delete() )
				file.deleteOnExit();
		}
		return Mono.just(map);
	}
	
	@PostMapping(path = "/resize")
	@ApiOperation(value = "Redimensiona uma imagem Base64. Para aumentar 20%, o ratio deve ser 1.2")
	@ApiResponses(value = {
	    @ApiResponse(code = 200, message = "Successfully retrieved list"),
	    @ApiResponse(code = 401, message = "You are not authorized to view the resource"),
	    @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
	    @ApiResponse(code = 404, message = "The resource you were trying to reach is not found")
	})
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
	@ApiOperation(value = "Converte uma imagem Base64 em Grayscale - Tons de cinza")
	@ApiResponses(value = {
	    @ApiResponse(code = 200, message = "Successfully retrieved list"),
	    @ApiResponse(code = 401, message = "You are not authorized to view the resource"),
	    @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
	    @ApiResponse(code = 404, message = "The resource you were trying to reach is not found")
	})
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
	
	@PostMapping(path = "/optimize")
	@ApiOperation(value = "Otimiza uma imagem Base64, melhorando a qualidade")
	@ApiResponses(value = {
	    @ApiResponse(code = 200, message = "Successfully retrieved list"),
	    @ApiResponse(code = 401, message = "You are not authorized to view the resource"),
	    @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
	    @ApiResponse(code = 404, message = "The resource you were trying to reach is not found")
	})
	public Mono<Map<String,String>> optimize(
			@RequestBody ImageRequest imageRequest,
			@RequestParam Float quality,
			@RequestParam Integer dpi) {
		Map<String,String> map = new LinkedHashMap<String,String>();
		String dir = System.getProperty("java.io.tmpdir");
		File file = new File(dir+System.getProperty("file.separator")+imageRequest.getFileName());
		try {
			Base64Util.base64BinaryToEncodeFile(imageRequest.getData(), file);
			BufferedImage bi = ImageUtil.load(file);
			
			//BufferedImage biNew = ImageUtil.optimize(bi,quality,dpi,bi.getWidth(),bi.getHeight(),false);
			
			//File newImage = new File(dir+System.getProperty("file.separator")+"OPTIMIZED_"+imageRequest.getFileName());
			//ImageIO.write(biNew, FileUtil.getExtension(imageRequest.getFileName()), newImage);
			
			File newImage = new File(ImageUtil.optimizeByFile(bi,quality,dpi,bi.getWidth(),bi.getHeight(),false));
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
