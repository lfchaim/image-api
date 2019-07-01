package br.com.whs.imageapi.test.util;

import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

import org.junit.Assert;
import org.junit.Test;

import br.com.whs.imageapi.util.ImageUtil;

public class ImageUtilTest {

	@Test
	public void testGrayScale() {
		String fileName = "MG_3081a-copy-768x530";
		String path = "D:/tmp/img/CNH/";
		String ext = "jpg";
		try {
			BufferedImage bi = ImageUtil.load(new File(path+fileName+"."+ext));
			BufferedImage biGray = ImageUtil.toGrayScale(bi);
			File newImage = new File(path+fileName+"_GRAY"+"."+ext);
			ImageIO.write(biGray, ext, newImage);
			Assert.assertNotNull(biGray);
		}catch( Exception e ) {
			e.printStackTrace();
		}
	}

}
