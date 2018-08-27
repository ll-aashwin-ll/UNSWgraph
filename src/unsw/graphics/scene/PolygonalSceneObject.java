package unsw.graphics.scene;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import com.jogamp.opengl.GL3;

import unsw.graphics.CoordFrame2D;
import unsw.graphics.Matrix3;
import unsw.graphics.Shader;
import unsw.graphics.Vector3;
import unsw.graphics.geometry.Point2D;
import unsw.graphics.geometry.Polygon2D;

/**
 * A game object that has a polygonal shape.
 * 
 * This class extend SceneObject to draw polygonal shapes.
 *
 * TODO: The methods you need to complete are at the bottom of the class
 *
 * @author malcolmr
 * @author Robert Clifton-Everest
 * 
 */
public class PolygonalSceneObject extends SceneObject {

    private Polygon2D myPolygon;
    private Color myFillColor;
    private Color myLineColor;

    /**
     * Create a polygonal scene object and add it to the scene tree
     * 
     * The line and fill colors can possibly be null, in which case that part of the object
     * should not be drawn.
     *
     * @param parent The parent in the scene tree
     * @param fillColor The fill color
     * @param lineColor The outline color
    */
    public PolygonalSceneObject(SceneObject parent, Polygon2D polygon,
            Color fillColor, Color lineColor) {
        super(parent);

        myPolygon = polygon;
        myFillColor = fillColor;
        myLineColor = lineColor;
    }

    public void setPolygon(Polygon2D poly) { myPolygon = poly; }
    /**
     * Get the fill color
     * 
     * @return
     */
    public Color getFillColor() {
        return myFillColor;
    }

    /**
     * Set the fill color.
     * 
     * Setting the color to null means the object should not be filled.
     * 
     * @param fillColor The fill color
     */
    public void setFillColor(Color fillColor) {
        myFillColor = fillColor;
    }

    /**
     * Get the outline color.
     * 
     * @return
     */
    public Color getLineColor() {
        return myLineColor;
    }

    /**
     * Set the outline color.
     * 
     * Setting the color to null means the outline should not be drawn
     * 
     * @param lineColor
     */
    public void setLineColor(Color lineColor) {
        myLineColor = lineColor;
    }

    // ===========================================
    // COMPLETE THE METHODS BELOW
    // ===========================================
    

    /**
     * Draw the polygon
     * 
     * if the fill color is non-null, draw the polygon filled with this color
     * if the line color is non-null, draw the outline with this color
     * 
     */
    @Override
    public void drawSelf(GL3 gl, CoordFrame2D frame) {
        if (myFillColor != null) {
            Shader.setPenColor(gl, myFillColor);
        } else {
            Shader.setPenColor(gl, Color.BLACK);
        }
        myPolygon.draw(gl, frame);

        if (myLineColor != null) {
            Shader.setPenColor(gl, myLineColor);
        } else {
            Shader.setPenColor(gl, Color.BLACK);

        }
        myPolygon.drawOutline(gl, frame);
    }

    @Override
    public Boolean collides(Point2D p) {

        Matrix3 inverseTransform = this.getGlobalToLocalTransform();

        Point2D localPos = inverseTransform.multiply(new Vector3(p.getX(), p.getY(), 1)).asPoint2D();

        List<Point2D> points = myPolygon.getPoints();

        int nvert = points.size();

        double[] vertx = new double[nvert];
        double[] verty = new double[nvert];

        for (int i = 0; i < nvert; i++) {
            Point2D temp = points.get(i);
            vertx[i] = temp.getX();
            verty[i] = temp.getY();
        }



        /* adapated from https://wrf.ecse.rpi.edu//Research/Short_Notes/pnpoly.html */
        int i, j= 0;
        Boolean c = false;
        for (i = 0, j = nvert-1; i < nvert; j = i++) {
            if ( ((verty[i]>localPos.getY()) != (verty[j]>localPos.getY())) &&
                    (localPos.getX()< (vertx[j]-vertx[i]) * (localPos.getY()-verty[i]) / (verty[j]-verty[i]) + vertx[i]) )
                c = !c;
        }
        return c;
    }


}
