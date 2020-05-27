import processing.core.PImage;

import java.util.List;
import java.util.Optional;
import java.util.Random;

public abstract class Miner extends Moving {
    public Miner(int animationPeriod, int actionPeriod, String id, Point position, List<PImage> images,  String bgnd ) {
        super(animationPeriod, actionPeriod, id, position, images, bgnd);
    }

    abstract boolean moveTo(WorldModel world, Entity target, EventScheduler scheduler);

    public  Point nextPositionMiner( WorldModel world, Point destPos)
    {
        int horiz = Integer.signum(destPos.getX() - this.getPosition().getX());
        Point newPos = new Point(this.getPosition().getX() + horiz, this.getPosition().getY());

        if (horiz == 0 || world.isOccupied( newPos)) {
            int vert = Integer.signum(destPos.getY() - this.getPosition().getY());
            newPos = new Point(this.getPosition().getX(), this.getPosition().getY() + vert);

            if (vert == 0 || world.isOccupied( newPos)) {
                newPos = this.getPosition();
            }
        }

        return newPos;
    }
}
