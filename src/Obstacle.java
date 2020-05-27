import processing.core.PImage;

import java.util.List;
import java.util.Optional;
import java.util.Random;

public class Obstacle extends Entity
{

    public Obstacle(
            String id,
            Point position,
            List<PImage> images)
    {
        super(id, position, images, 0, "obstacle");
    }

}
