package unsw.graphics.scene;

import unsw.graphics.geometry.Point2D;
import unsw.graphics.geometry.Polygon2D;

import java.awt.*;
import java.util.List;
import java.util.ArrayList;


public class CircularSceneObject extends PolygonalSceneObject {
    private static final int VERTICES = 32;
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
}
