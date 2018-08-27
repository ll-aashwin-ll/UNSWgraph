package unsw.graphics.scene;

import unsw.graphics.Vector3;
import unsw.graphics.geometry.Point2D;
import unsw.graphics.geometry.Polygon2D;

import java.awt.*;
import java.util.List;
import java.util.ArrayList;


public class CircularSceneObject extends PolygonalSceneObject {
    private static final int VERTICES = 32;
    private float radius;
    private Polygon2D poly;
    /**
     * Create a polygonal scene object and add it to the scene tree
     * <p>
     * The line and fill colors can possibly be null, in which case that part of the object
     * should not be drawn.
     *
     * @param parent    The parent in the scene tree
     * @param fillColor The fill color
     * @param lineColor The outline color
     */

    public CircularSceneObject(SceneObject parent, float radius, Color fillColor, Color lineColor) {
        super(parent, null, fillColor, lineColor);
        List<Point2D> points = new ArrayList<Point2D>();

        this.radius = radius;

        for (int i = 0; i < VERTICES; i++) {
            float a = (float) (i * Math.PI * 2 / VERTICES); // java.util.Math uses radians!!!
            float x = radius * (float) Math.cos(a);
            float y = radius * ((float) Math.sin(a)); // Off center
            Point2D p = new Point2D(x, y);
            points.add(p);
        }

        Polygon2D poly = new Polygon2D(points);

        this.setPolygon(poly);
    }

    public CircularSceneObject(SceneObject parent, Color fillColor, Color lineColor) {
        this(parent, 1, fillColor, lineColor);
    }


    /**
     * check for collisions by checking if the distance
     * from the cetnre of circle to the point is less than
     * the radius. If lesser than there is a collision
     * @param p
     * @return
     */
    @Override
    public Boolean collides(Point2D p) {
        float globalRadius = this.radius * getGlobalScale();

        Point2D centre = getGlobalPosition();

        double distToPoint = Math.sqrt(Math.pow(Math.abs(p.getX() - centre.getX()), 2) + Math.pow(Math.abs(p.getY()-centre.getY()), 2));

        if (distToPoint <= globalRadius) {
            return true;
        }

        return false;
    }
}
