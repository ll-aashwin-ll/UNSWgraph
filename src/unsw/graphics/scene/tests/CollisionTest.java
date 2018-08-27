package unsw.graphics.scene.tests;

import junit.framework.TestCase;
import org.junit.Test;
import unsw.graphics.geometry.Point2D;
import unsw.graphics.scene.LineSceneObject;
import unsw.graphics.scene.Scene;
import unsw.graphics.scene.SceneObject;

import java.awt.*;

public class CollisionTest extends TestCase {
    @Test
    public void testLineCollision1() {
        Scene scene = new Scene();
        //Line from (0,0) to (5,0), rotated 45% (ie line through origin)
        //Point (3,3) lies on this line
        LineSceneObject lgo = new LineSceneObject(scene.getRoot(), 0, 0, 1, 1, Color.WHITE);
        Point2D point = new Point2D(0, 0);

        assertTrue(lgo.collides(point));
    }
    @Test
    public void testLineCollision2() {
        Scene scene = new Scene();
        //Line from (0,0) to (5,0), rotated 45% (ie line through origin)
        //Point (3,3) lies on this line
        LineSceneObject lgo = new LineSceneObject(scene.getRoot(), 0, 0, 5, 0, Color.WHITE);
        lgo.setRotation(45);
        Point2D point = new Point2D(3, 3);

        assertFalse(lgo.collides(point));
    }
    }
