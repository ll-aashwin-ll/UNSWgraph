package unsw.graphics.scene;

import com.jogamp.opengl.GL3;
import unsw.graphics.CoordFrame2D;
import unsw.graphics.Shader;
import unsw.graphics.geometry.Line2D;
import unsw.graphics.geometry.Point2D;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

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
}
