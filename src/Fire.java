import processing.core.PImage;

import java.util.List;
import java.util.Random;

public class Fire extends Entity {

    public Fire(
            String id,
            Point position,
            List<PImage> images) {
        super(id, position, images, 0, "fire");
    }
}