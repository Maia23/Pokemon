package view;

import java.awt.Image;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

/**
 * Class to load Game Images
 */
public class ImageLoader {
	
	private Image image;
	
	/**
	 * Loads image in given folder (using src as starting point) with the given name
	 * @param folderName
	 * @param fileName
	 * @throws IOException if folderName/fileName can't be loaded
	 */
	public ImageLoader(String folderName, String fileName) {
		try {
			//System.out.println("Loading image :" + fileName);
			image = ImageIO.read(new File(new java.io.File( "." ).getCanonicalPath() + "/assets/" + folderName + "/" + fileName));
		} catch (IOException e) {
			System.err.println("Error while loading " + fileName + " image!");
			e.printStackTrace();
		}
	}
	
	/**
	 * Loads image following given path
	 * @param filePath
	 * @throws IOException if filePath can't be loaded
	 */
	public ImageLoader(String filePath) {
		try {
			//System.out.println("Loading image :" + fileName);
			image = ImageIO.read(new File(new java.io.File( "." ).getCanonicalPath() + "/assets/" + filePath));
		} catch (IOException e) {
			System.err.println("Error while loading " + filePath + " image!");
			e.printStackTrace();
		}
	}
	
	public ImageLoader(String folderName, String fileName, int width, int height) {
		this(folderName, fileName);
		image = image.getScaledInstance(width, height, java.awt.Image.SCALE_SMOOTH);
	}
	
	public ImageLoader(String filePath, int width, int height) {
		this(filePath);
		image = image.getScaledInstance(width, height, java.awt.Image.SCALE_SMOOTH);
	}
	
	public ImageIcon getImageIcon() {
		return new ImageIcon(image);
	}

}
