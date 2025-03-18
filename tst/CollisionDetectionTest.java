import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CollisionDetectionTest {
    @Test
    public void someTest(){
        Position p1 = new Position(10,10);
        Position p2 = new Position(15, 15);
        Position p3 = new Position(400, 400);

        boolean shouldNotCollide = CollisionDetection.DoThingsCollide(p1, 10, 10, p3, 10, 10);
        assertFalse(shouldNotCollide);


          boolean shouldCollide = CollisionDetection.DoThingsCollide(p1, 10, 10, p2, 10, 10);
          assertTrue(shouldCollide);
    }
}
