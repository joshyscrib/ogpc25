public class CollisionDetection {

    public static boolean DoesPointCollide(Entity e, int x, int y) {

        if(x >= e.x && x <= e.x + e.width && y >= e.y && y <= e.y + e.height){return true;}

        return false;
    }

    public static boolean DoEntitiesCollide(Entity entity1, Entity entity2) {

        if(DoesPointCollide(entity1, entity2.x, entity2.y) || DoesPointCollide(entity1, entity2.x + entity2.width, entity2.y) || DoesPointCollide(entity1, entity2.x, entity2.y + entity2.height) || DoesPointCollide(entity1, entity2.x + entity2.width, entity2.y + entity2.height) ||
           DoesPointCollide(entity2, entity1.x, entity1.y) || DoesPointCollide(entity2, entity1.x + entity1.width, entity1.y) || DoesPointCollide(entity2, entity1.x, entity1.y + entity1.height) || DoesPointCollide(entity2, entity1.x + entity1.width, entity1.y + entity1.height))
        {return true;}

        return false;
    }
}
