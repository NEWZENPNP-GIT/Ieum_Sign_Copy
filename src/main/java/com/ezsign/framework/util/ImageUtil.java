package com.ezsign.framework.util;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Iterator;
import java.util.UUID;

import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.xml.bind.DatatypeConverter;

import com.drew.imaging.ImageMetadataReader;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.exif.ExifIFD0Directory;
import com.drew.metadata.jpeg.JpegDirectory;


public class ImageUtil {
	
	public static void main(String[] args) {
		boolean result = false;
		LocalDateTime sdateTime = LocalDateTime.now();
		System.out.println("start time:"+sdateTime);
		//result = ImageUtil.resizePicture("d:\\1제목 없음.tif", "d:\\KakaoTalk_20200512_114620278_resize.tif", "tif", 0.8f, 1.0);
		result = ImageUtil.rotateJPEG("d:\\KakaoTalk_20200519_100542686.jpg", "d:\\KakaoTalk_20200519_100542686_saved.jpg", "jpg", 1.0f);

		LocalDateTime edateTime = LocalDateTime.now();
		System.out.println("end time : " + result + "-"+edateTime);
	}
	
	public static double clacImageScale(int width, int height) {
		
		double maxSize = 1500, minScale = 0.3, maxScale = 1.0, calcScale = 1.0;
		
		if(width >= height) {
			if(width >= maxSize) {
				calcScale = (maxSize / width);
			}
		} else {
			if(height >= maxSize) {
				calcScale = (maxSize / height);
			}
		}
		if(calcScale >= maxScale) calcScale = maxScale;
		if(calcScale < minScale) calcScale = minScale;
		calcScale = Math.floor(calcScale*10)/10;
		System.out.println("calcScale :"+calcScale+","+width+","+height);
		return calcScale;
	}
	
	public static String writeImage(String localPath, String imageData) throws IOException {
        String docId = UUID.randomUUID().toString();
        
		String fileName = localPath+docId+".png";
		
        FileOutputStream fos = null;
        try {
            imageData = imageData.substring("data:image/png;base64,".length());
            byte[] decodedData = DatatypeConverter.parseBase64Binary(imageData);
            
            fos = new FileOutputStream(fileName);
            fos.write(decodedData);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (fos != null) {
                fos.close();
            }
        }
        /*
		byte[] decodedBytes = DatatypeConverter.parseBase64Binary(imageData);
        BufferedImage bfi = ImageIO.read(new ByteArrayInputStream(decodedBytes));
        File outputfile = new File(fileName);
		ImageIO.write(bfi , "png", outputfile);
        bfi.flush();
        */
        return fileName;
	}
	
	public static boolean rotatePicture(String srcPath, String savePath, String imgFormat, float quality, double scale) {
		boolean result = false;
		
		switch(imgFormat.toUpperCase())		
		{
			case "JPG":
				result = rotateJPEG(srcPath, savePath, imgFormat, quality);
				break;
			default:
				result= FileUtil.fileCopy(srcPath, savePath);
				break;
		}
		
		return result;
	}
	

	public static boolean rotateJPEG(String sourcePath, String targetPath, String fileFormat, float quality) {
		boolean result = false ;
		int orientation = 1; 
		int width = 0; 
		int height = 0; 
		int tempWidth = 0; 
		Metadata metadata; 
		Directory directory; 
		JpegDirectory jpegDirectory; 
		
		try {
			File imageFile = new File(sourcePath);

			BufferedImage image = ImageIO.read(imageFile);
			BufferedImage rotatedImage = null;

			metadata = ImageMetadataReader.readMetadata(imageFile);
			directory = metadata.getFirstDirectoryOfType(ExifIFD0Directory.class);
			jpegDirectory = metadata.getFirstDirectoryOfType(JpegDirectory.class);
			if(directory != null){
				if(directory.containsTag(ExifIFD0Directory.TAG_ORIENTATION)){
					orientation = directory.getInt(ExifIFD0Directory.TAG_ORIENTATION);	
				}
				width = jpegDirectory.getImageWidth(); 
				height = jpegDirectory.getImageHeight();
				
				AffineTransform atf = new AffineTransform();
				switch (orientation) {
					case 1: break;
					case 2: // Flip X 
						atf.scale(-1.0, 1.0); 
						atf.translate(-width, 0); 
						break; 
					case 3: // PI rotation 
						atf.translate(width, height); 
						atf.rotate(Math.PI); 
						break; 
					case 4: // Flip Y 
						atf.scale(1.0, -1.0); 
						atf.translate(0, -height); 
						break; 
					case 5: // - PI/2 and Flip X 
						atf.rotate(-Math.PI / 2); 
						atf.scale(-1.0, 1.0); 
						break; 
					case 6: // -PI/2 and -width 
						atf.translate(height, 0); 
						atf.rotate(Math.PI / 2); 
						break; 
					case 7: // PI/2 and Flip \
						atf.scale(-1.0, 1.0);
						atf.translate(-height, 0); 
						atf.translate(0, width); 
						atf.rotate( 3 * Math.PI / 2); 
						break; 
					case 8: // PI / 2 
						atf.translate(0, width); 
						atf.rotate( 3 * Math.PI / 2); 
						break;
				}
				
				switch (orientation) { 
					case 5: case 6: case 7: case 8: 
						tempWidth = width; 
						width = height; 
						height = tempWidth; 
						break; 
				}
				
				final BufferedImage afterImage = new BufferedImage(width, height, image.getType()); 
				final AffineTransformOp rotateOp = new AffineTransformOp(atf, AffineTransformOp.TYPE_BICUBIC); 
				rotatedImage = rotateOp.filter(image, afterImage); 
				Iterator<ImageWriter> iter = ImageIO.getImageWritersByFormatName(fileFormat);
				ImageWriter writer = iter.next(); 
				ImageWriteParam iwp = writer.getDefaultWriteParam(); 
				iwp.setCompressionMode(ImageWriteParam.MODE_EXPLICIT); 
				iwp.setCompressionQuality(1.0f*quality); 

			} else {
				width = image.getWidth();
				height = image.getHeight();
				
				rotatedImage = image;
			}
			
            System.out.println("rotateJPEG image size :" + width + "," + height);
            

			result = ImageIO.write(rotatedImage, fileFormat, new File(targetPath));
			
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		
		
		return result;
	}
	
	public static boolean resizeImage(String sourcePath, String tartgetPath, String imgFormat, double scale) {
		boolean result = false;
		try{

			//파일을 읽는다.
			File imageFile = new File(sourcePath);
			
			BufferedImage image = ImageIO.read(imageFile);

            int nWidth = (int) (image.getWidth(null) * scale);
            int nHeight = (int) (image.getHeight(null) * scale);
            
            System.out.println("resizeImage size :"+nWidth + ","+nHeight);
            Image resizeImage = image.getScaledInstance(nWidth, nHeight, Image.SCALE_SMOOTH);
            
            BufferedImage newImage = new BufferedImage(nWidth, nHeight, BufferedImage.TYPE_BYTE_GRAY);
            
            Graphics g = newImage.getGraphics();
            g.drawImage(resizeImage, 0, 0, null);
            g.dispose();
            
            result = ImageIO.write(newImage, imgFormat, new File(tartgetPath));
        }catch (Exception e){
            e.printStackTrace();
        }
		
		return result;
	}
	
	public static boolean resizePicture(String srcPath, String savePath, String imgFormat, float quality, double scale) {
		boolean result = false;
		
		switch(imgFormat.toUpperCase())		
		{
			case "TIF":
				result = FileUtil.fileCopy(srcPath, savePath);
				break;
			case "JPG":
				result = resizeJPEG(srcPath, savePath, imgFormat, quality);
				break;
			default:
				result= resizePNG(srcPath, savePath, imgFormat);
				break;
		}
		
		return result;
	}
	
	public static boolean resizePNG(String sourcePath, String targetPath, String fileFormat) {
		boolean result = false;

		try {
			File imageFile = new File(sourcePath);
			
			BufferedImage image = ImageIO.read(imageFile);

            int width = 0, resizeWidth = 0;
            int height = 0, resizeHeight = 0;
            width = image.getWidth(null);
            height = image.getHeight(null);
            
            System.out.println("resizePNG image size :" + width + "," + height);
            
            double scale = clacImageScale(width, height);
            resizeWidth = (int)(width * scale);
            resizeHeight = (int)(height * scale);
            
            Image resizeImage = image.getScaledInstance(resizeWidth, resizeHeight, Image.SCALE_SMOOTH);
            
            BufferedImage newImage = new BufferedImage(resizeWidth, resizeHeight, BufferedImage.TYPE_BYTE_GRAY);
            
            Graphics g = newImage.getGraphics();
            g.drawImage(resizeImage, 0, 0, null);
            g.dispose();
            
            result = ImageIO.write(newImage, fileFormat, new File(targetPath));
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		
		return result;		
	}
	
	public static boolean resizeJPEG(String sourcePath, String targetPath, String fileFormat, float quality) {
		boolean result = false ;
		int orientation = 1; 
		int width = 0; 
		int height = 0; 
		int tempWidth = 0; 
		Metadata metadata; 
		Directory directory; 
		JpegDirectory jpegDirectory; 

		try {
			File imageFile = new File(sourcePath);

			BufferedImage image = ImageIO.read(imageFile);
			BufferedImage rotatedImage = null;

			metadata = ImageMetadataReader.readMetadata(imageFile);
			directory = metadata.getFirstDirectoryOfType(ExifIFD0Directory.class);
			jpegDirectory = metadata.getFirstDirectoryOfType(JpegDirectory.class);
			if(directory != null){
				if(directory.containsTag(ExifIFD0Directory.TAG_ORIENTATION)){
					orientation = directory.getInt(ExifIFD0Directory.TAG_ORIENTATION);	
				}
				width = jpegDirectory.getImageWidth(); 
				height = jpegDirectory.getImageHeight();
				
				AffineTransform atf = new AffineTransform();
				switch (orientation) {
					case 1: break;
					case 2: // Flip X 
						atf.scale(-1.0, 1.0); 
						atf.translate(-width, 0); 
						break; 
					case 3: // PI rotation 
						atf.translate(width, height); 
						atf.rotate(Math.PI); 
						break; 
					case 4: // Flip Y 
						atf.scale(1.0, -1.0); 
						atf.translate(0, -height); 
						break; 
					case 5: // - PI/2 and Flip X 
						atf.rotate(-Math.PI / 2); 
						atf.scale(-1.0, 1.0); 
						break; 
					case 6: // -PI/2 and -width 
						atf.translate(height, 0); 
						atf.rotate(Math.PI / 2); 
						break; 
					case 7: // PI/2 and Flip \
						atf.scale(-1.0, 1.0);
						atf.translate(-height, 0); 
						atf.translate(0, width); 
						atf.rotate( 3 * Math.PI / 2); 
						break; 
					case 8: // PI / 2 
						atf.translate(0, width); 
						atf.rotate( 3 * Math.PI / 2); 
						break;
				}
				
				switch (orientation) { 
					case 5: case 6: case 7: case 8: 
						tempWidth = width; 
						width = height; 
						height = tempWidth; 
						break; 
				}
				
				System.out.println("orientation:"+orientation);
				

				final BufferedImage afterImage = new BufferedImage(width, height, image.getType()); 
				final AffineTransformOp rotateOp = new AffineTransformOp(atf, AffineTransformOp.TYPE_BICUBIC); 
				rotatedImage = rotateOp.filter(image, afterImage); 
				Iterator<ImageWriter> iter = ImageIO.getImageWritersByFormatName(fileFormat);
				ImageWriter writer = iter.next(); 
				ImageWriteParam iwp = writer.getDefaultWriteParam(); 
				iwp.setCompressionMode(ImageWriteParam.MODE_EXPLICIT); 
				iwp.setCompressionQuality(1.0f*quality); 

			} else {
				width = image.getWidth();
				height = image.getHeight();
				
				rotatedImage = image;
			}
			
            System.out.println("resizeJPEG image size :" + width + "," + height);
            
			int resizeWidth = width;
			int resizeHeight = height;

            double scale = clacImageScale(width, height);
            resizeWidth = (int)(width * scale);
            resizeHeight = (int)(height * scale);
			
			Image resizeImage = rotatedImage.getScaledInstance(resizeWidth, resizeHeight, Image.SCALE_SMOOTH);
			
			BufferedImage newImage = new BufferedImage(resizeWidth, resizeHeight, BufferedImage.TYPE_BYTE_GRAY);
			
			Graphics g = newImage.getGraphics();
			g.drawImage(resizeImage, 0, 0, null);
			g.dispose();

			result = ImageIO.write(newImage, fileFormat, new File(targetPath));
			
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		
		
		return result;
	}
	
}
