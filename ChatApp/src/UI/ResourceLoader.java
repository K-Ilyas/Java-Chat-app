package UI;



import java.io.InputStream;
import java.net.URL;

import io.github.palexdev.materialfx.css.themes.Themes;
import javafx.scene.Scene;


public class ResourceLoader {

	private ResourceLoader() {
	}

	public static URL loadURL(String path) {
		return ResourceLoader.class.getResource(path);
	}

	public static String load(String path) {
		return loadURL(path).toString();
	}

	public static InputStream loadStream(String name) {
		return ResourceLoader.class.getResourceAsStream(name);
	}

	public static void addOn(Scene scene, Themes default1, Themes legacy) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Unimplemented method 'addOn'");
	}

}