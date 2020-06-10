import processing.core.PImage;

import java.util.List;

public class Tomb extends Entity
{

    public Tomb(
            String id,
            Point position,
            List<PImage> images)
    {
        super(id, position, images, 0, "Tomb");
    }

}
