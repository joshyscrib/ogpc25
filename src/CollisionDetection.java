public class CollisionDetection {

    public static boolean DoesPointCollide(Entity e, int x, int y) {

        if (x >= e.x && x <= e.x + e.width && y >= e.y && y <= e.y + e.height) {
            return true;
        }

        return false;
    }

    public static boolean DoesPositionCollide(Position pos, int wdt, int hgt, int x, int y) {

        if (x >= pos.x && x <= pos.x + wdt && y >= pos.y && y <= pos.y + hgt) {
            return true;
        }

        return false;
    }

    public static boolean DoEntitiesCollide(Entity entity1, Entity entity2) {

        if (DoesPointCollide(entity1, entity2.x, entity2.y) || DoesPointCollide(entity1, entity2.x + entity2.width, entity2.y) || DoesPointCollide(entity1, entity2.x, entity2.y + entity2.height) || DoesPointCollide(entity1, entity2.x + entity2.width, entity2.y + entity2.height) ||
                DoesPointCollide(entity2, entity1.x, entity1.y) || DoesPointCollide(entity2, entity1.x + entity1.width, entity1.y) || DoesPointCollide(entity2, entity1.x, entity1.y + entity1.height) || DoesPointCollide(entity2, entity1.x + entity1.width, entity1.y + entity1.height)) {
            return true;
        }

        return false;
    }

    public static boolean DoThingsCollide(Position p1, int w1, int h1, Position p2, int w2, int h2) {

        if (DoesPositionCollide(p1, w1, h1, p2.x, p2.y) || DoesPositionCollide(p1, w1, h1, p2.x + w2, p2.y) || DoesPositionCollide(p1, w1, h1, p2.x, p2.y + h2) || DoesPositionCollide(p1, w1, h1, p2.x + w2, p2.y + h2) ||
                DoesPositionCollide(p2, w2, h2, p1.x, p1.y) || DoesPositionCollide(p2, w2, h2, p1.x + w1, p1.y) || DoesPositionCollide(p2, w2, h2, p1.x, p1.y + h1) || DoesPositionCollide(p2, w2, h2, p1.x + w1, p1.y + h1)) {
            return true;
        }

        return false;
    }
}
