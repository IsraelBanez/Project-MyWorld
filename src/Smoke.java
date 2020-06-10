import processing.core.PImage;

import java.util.List;

public class Smoke extends Entity
{

    public Smoke(
            String id,
            Point position,
            List<PImage> images)
    {
        super(id, position, images, 0, "Smoke");
    }

}
