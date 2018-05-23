package donkeykong;

import java.awt.*;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

public class SpriteStore {

	private static SpriteStore single = new SpriteStore();
	private HashMap<String, Sprite> sprites = new HashMap<String, Sprite>(); 

	// returns the single instance of this class
	public static SpriteStore get() {
		return single;
	} // get

	public Sprite getSprite(String ref) {

		if (sprites.get(ref) != null) {
			return (Sprite) sprites.get(ref);
		} // if

		BufferedImage sourceImage = null;

		try {
			// get the image location
			URL url = this.getClass().getClassLoader().getResource(ref);
			if (url == null) {
				System.out.println("Failed to load: " + ref);
				System.exit(0); // exit program if file not found
			} // if
			sourceImage = ImageIO.read(url); // get image
		} catch (IOException e) {
			System.out.println("Failed to load: " + ref);
			System.exit(0); // exit program if file not loaded
		} // catch

		// create an accelerated image (correct size) to store our sprite in
		GraphicsConfiguration gc = GraphicsEnvironment
				.getLocalGraphicsEnvironment().getDefaultScreenDevice()
				.getDefaultConfiguration();
		Image image = gc.createCompatibleImage(sourceImage.getWidth(),
				sourceImage.getHeight(), Transparency.BITMASK);

		// draw our source image into the accelerated image
		image.getGraphics().drawImage(sourceImage, 0, 0, null);

		// create a sprite, add it to the cache and return it
		Sprite sprite = new Sprite(image);
		sprites.put(ref, sprite);

		return sprite;
	} // getSprite

} // SpriteStore