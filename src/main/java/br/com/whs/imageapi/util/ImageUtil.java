package br.com.whs.imageapi.util;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class ImageUtil {

	public static BufferedImage autoCrop(BufferedImage sourceImage) {
		int left = 0;
		int right = 0;
		int top = 0;
		int bottom = 0;
		boolean firstFind = true;
		for (int x = 0; x < sourceImage.getWidth(); x++) {
			for (int y = 0; y < sourceImage.getHeight(); y++) {
				try {
					// pixel is not empty
					if (sourceImage.getRGB(x, y) != 0) {

						// we walk from left to right, thus x can be applied as left on first finding
						if (firstFind) {
							left = x;
						}

						// update right on each finding, because x can grow only
						right = x;

						// on first find apply y as top
						if (firstFind) {
							top = y;
						} else {
							// on each further find apply y to top only if a lower has been found
							top = Math.min(top, y);
						}

						// on first find apply y as bottom
						if (bottom == 0) {
							bottom = y;
						} else {
							// on each further find apply y to bottom only if a higher has been found
							bottom = Math.max(bottom, y);
						}
						firstFind = false;
					}
				} catch (ArrayIndexOutOfBoundsException e) {
					System.err.println("X: " + x + " Y: " + y + " width: " + sourceImage.getWidth() + " height: "
							+ sourceImage.getHeight());
				}
			}
		}

		return sourceImage.getSubimage(left, top, right - left, bottom - top);
	}

	public static BufferedImage getCroppedImage(BufferedImage source, double tolerance) {
		// Get our top-left pixel color as our "baseline" for cropping
		int baseColor = source.getRGB(0, 0);

		int width = source.getWidth();
		int height = source.getHeight();

		int topY = Integer.MAX_VALUE, topX = Integer.MAX_VALUE;
		int bottomY = -1, bottomX = -1;
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				if (colorWithinTolerance(baseColor, source.getRGB(x, y), tolerance)) {
					if (x < topX)
						topX = x;
					if (y < topY)
						topY = y;
					if (x > bottomX)
						bottomX = x;
					if (y > bottomY)
						bottomY = y;
				}
			}
		}

		BufferedImage destination = new BufferedImage((bottomX - topX + 1), (bottomY - topY + 1),
				BufferedImage.TYPE_INT_ARGB);

		destination.getGraphics().drawImage(source, 0, 0, destination.getWidth(), destination.getHeight(), topX, topY,
				bottomX, bottomY, null);

		return destination;
	}

	private static boolean colorWithinTolerance(int a, int b, double tolerance) {
		int aAlpha = (int) ((a & 0xFF000000) >>> 24); // Alpha level
		int aRed = (int) ((a & 0x00FF0000) >>> 16); // Red level
		int aGreen = (int) ((a & 0x0000FF00) >>> 8); // Green level
		int aBlue = (int) (a & 0x000000FF); // Blue level

		int bAlpha = (int) ((b & 0xFF000000) >>> 24); // Alpha level
		int bRed = (int) ((b & 0x00FF0000) >>> 16); // Red level
		int bGreen = (int) ((b & 0x0000FF00) >>> 8); // Green level
		int bBlue = (int) (b & 0x000000FF); // Blue level

		double distance = Math.sqrt((aAlpha - bAlpha) * (aAlpha - bAlpha) + (aRed - bRed) * (aRed - bRed)
				+ (aGreen - bGreen) * (aGreen - bGreen) + (aBlue - bBlue) * (aBlue - bBlue));

		// 510.0 is the maximum distance between two colors
		// (0,0,0,0 -> 255,255,255,255)
		double percentAway = distance / 510.0d;

		return (percentAway > tolerance);
	}
	
	public static BufferedImage autoCrop(final BufferedImage image, final int fuzziness) throws IOException {
		final Color color = new Color(image.getRGB(0, 0));
		boolean stop = false;
		int cropTop = 0;
		for (int y = 0; y < image.getHeight(); y++) {
			for (int x = 0; x < image.getWidth(); x++) {
				if (!ColorUtil.match(color, image.getRGB(x, y), fuzziness)) {
					stop = true;
					break;
				}
			}
			if (stop) {
				if (y > 0) {
					cropTop = y - 1;
				}
				break;
			}
		}

		stop = false;
		int cropBot = image.getHeight();
		for (int y = (image.getHeight() - 1); y >= 0; y--) {
			for (int x = 0; x < image.getWidth(); x++) {
				if (!ColorUtil.match(color, image.getRGB(x, y), fuzziness)) {
					stop = true;
					break;
				}
			}
			if (stop) {
				if (y < image.getHeight()) {
					cropBot = y + 1;
				}
				break;
			}
		}


		stop = false;
		int cropLeft = 0;
		for (int x = 0; x < image.getWidth(); x++) {
			for (int y = 0; y < image.getHeight(); y++) {
				if (!ColorUtil.match(color, image.getRGB(x, y), fuzziness)) {
					stop = true;
					break;
				}
			}
			if (stop) {
				if (x > 0) {
					cropLeft = x - 1;
				}
				break;
			}
		}


		stop = false;
		int cropRight = 0;
		for (int x = (image.getWidth() - 1); x >= 0; x--) {
			for (int y = 0; y < image.getHeight(); y++) {
				if (!ColorUtil.match(color, image.getRGB(x, y), fuzziness)) {
					stop = true;
					break;
				}
			}
			if (stop) {
				if (x < image.getWidth()) {
					cropRight = x + 1;
				}
				break;
			}
		}

		final BufferedImage cropped = image.getSubimage(cropLeft, cropTop, cropRight - cropLeft, cropBot - cropTop);
		return cropped;
	}
	
	public static BufferedImage resize(BufferedImage source, int targetW, int targetH) {

        int type = source.getType();
        BufferedImage target = null;
        double sx = (double) targetW / source.getWidth();
        double sy = (double) targetH / source.getHeight();
        if (sx < sy) {
            sx = sy;
            targetW = (int) (sx * source.getWidth());
        } else {
            sy = sx;
            targetH = (int) (sy * source.getHeight());
        }

        target = new BufferedImage(targetW, targetH, BufferedImage.TYPE_INT_BGR);

        Graphics2D g = target.createGraphics();
        // smoother than exlax:
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                RenderingHints.VALUE_INTERPOLATION_BICUBIC);
        g.drawRenderedImage(source, AffineTransform.getScaleInstance(sx, sy));
        g.dispose();
        return target;
    }
	
	public static BufferedImage resize(BufferedImage source, float ratio) {
        BufferedImage target = null;
        double sx = (double) ratio * source.getWidth();
        double sy = (double) ratio * source.getHeight();
        int targetW = source.getWidth();
        int targetH = source.getHeight();
        if( sx > 0 ) {
            targetW = (int)sx;
        } 
        if( sy > 0 ){
            targetH = (int)sy;
        }
        target = new BufferedImage(targetW, targetH, source.getType());
        Graphics2D g = target.createGraphics();
        // smoother than exlax:
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
        g.drawRenderedImage(source, AffineTransform.getScaleInstance(ratio, ratio));
        g.dispose();
        return target;
    }
	
	public static BufferedImage toGrayScale( BufferedImage source ) {
		BufferedImage result = new BufferedImage(
			source.getWidth(),
			source.getHeight(),
            BufferedImage.TYPE_INT_RGB);
        Graphics2D graphic = result.createGraphics();
        graphic.drawImage(source, 0, 0, Color.WHITE, null);
        for (int i = 0; i < result.getHeight(); i++) {
            for (int j = 0; j < result.getWidth(); j++) {
                Color c = new Color(result.getRGB(j, i));
                int red = (int) (c.getRed() * 0.299);
                int green = (int) (c.getGreen() * 0.587);
                int blue = (int) (c.getBlue() * 0.114);
                Color newColor = new Color(
	                red + green + blue,
	                red + green + blue,
	                red + green + blue);
                result.setRGB(j, i, newColor.getRGB());
            }
        }
        return result;
	}
	
	public static BufferedImage load( File file ) throws IOException {
		return ImageIO.read(file);
	}
}
