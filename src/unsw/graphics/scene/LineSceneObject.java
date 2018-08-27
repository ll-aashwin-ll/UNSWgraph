package unsw.graphics.scene;

import com.jogamp.opengl.GL3;
import unsw.graphics.CoordFrame2D;
import unsw.graphics.Matrix3;
import unsw.graphics.Shader;
import unsw.graphics.Vector3;
import unsw.graphics.geometry.Line2D;
import unsw.graphics.geometry.Point2D;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import static java.lang.Math.abs;

public class LineSceneObject extends SceneObject{
    private Line2D line;
    private Color lineColor;

    public LineSceneObject(SceneObject parent, Color lineColor) {
       this(parent, 0, 0, 1, 0, lineColor);
    }

    public LineSceneObject(SceneObject parent, float x0, float y0, float x1, float y1, Color lineColor) {
        super(parent);
        Point2D p1 = new Point2D(x0, y0);
        Point2D p2 = new Point2D(x1, y1);

        line = new Line2D(p1, p2);

        this.lineColor = lineColor;
    }

    public void setLineWidth(float width) {
        line.setLineWidth(width);
    }

    public void setLineColor(Color c) {
        this.lineColor = c;
    }

    public Color getLineColor() {
        return this.lineColor;
    }

    @Override
    public void drawSelf(GL3 gl, CoordFrame2D frame) {
       if (lineColor != null) {
           Shader.setPenColor(gl, lineColor);
       } else {
           Shader.setPenColor(gl, Color.BLACK);

       }

       line.draw(gl, frame);
    }

    /**
     * detects a collision with this line and a point
     * if the distance from the point both vertcies of the
     * line are the same
     * @param p
     * @return
     */
    @Override
    public Boolean collides(Point2D p) {
        Point2D localStart = line.getStart();
        Point2D localEnd = line.getEnd();

        Matrix3 globalTransform = this.getLocalToGlobalTransform();

         Point2D globalStart = globalTransform.multiply(new Vector3(localStart.getX(), localStart.getY(), 1)).asPoint2D();
         Point2D globalEnd = globalTransform.multiply(new Vector3(localEnd.getX(), localEnd.getY(), 1)).asPoint2D();

         Point2D point = new Vector3(p.getX(), p.getY(), 1).asPoint2D();

        double distanceStartToPoint = Math.sqrt(Math.pow(Math.abs(globalStart.getX() - globalEnd.getX()), 2) + Math.pow(Math.abs(globalStart.getY()-globalEnd.getY()), 2));
        double distanceEndToPoint = Math.sqrt(Math.pow(Math.abs(globalStart.getX() - point.getX()), 2) + Math.pow(Math.abs(globalStart.getY()-point.getY()), 2));
        double distanceStartToEnd = Math.sqrt(Math.pow(Math.abs(point.getX() - globalEnd.getX()), 2) + Math.pow(Math.abs(point.getY()-globalEnd.getY()), 2));

        double diff = abs(distanceStartToEnd - (distanceStartToPoint + distanceEndToPoint));

        if (diff <= 0.00001) {
            return true;
        }

        return false;
    }
}
