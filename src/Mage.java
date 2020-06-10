import processing.core.PImage;

import java.util.List;
import java.util.Random;

public class Mage extends Entity {

    public Mage(
            String id,
            Point position,
            List<PImage> images) {
        super(id, position, images, 0, "mage");
    }
}