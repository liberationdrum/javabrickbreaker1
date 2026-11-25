import javafx.scene.image.Image;
import java.io.InputStream;
import java.util.HashMap;

public class AssetLoader {
    private static HashMap<String, Image> images = new HashMap<>();

    public static Image loadImage(String path) {
        if (images.containsKey(path)) {
            return images.get(path);
        }

        InputStream input = AssetLoader.class.getResourceAsStream(path);

        if (input == null) {
            return null;
        }

        Image image = new Image(input);
        images.put(path, image);
        return image;
    }
}