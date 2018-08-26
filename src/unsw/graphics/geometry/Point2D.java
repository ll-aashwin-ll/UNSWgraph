package unsw.graphics.geometry;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL3;

import unsw.graphics.CoordFrame2D;
import unsw.graphics.Point2DBuffer;
import unsw.graphics.Shader;
import unsw.graphics.Vector3;

/**
 * A point in 2D space.
 * 
 * This class is immutable.
 * 
 * @author Robert Clifton-Everest
 *
 */
public class Point2D {
    private float x, y;

    /**
     * Construct a point from the given x and y coordinates.
     * 
     * @param x
     * @param y
     */
    public Point2D(float x, float y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Draw this point as a dot in the given coordinate frame.
     * 
     * This is useful for debugging and for very trivial examples.
     * 
     * @param gl
     * @param frame
     */
    public void draw(GL3 gl, CoordFrame2D frame) {
        /* quite literally a list of Point2D */
        Point2DBuffer buffer = new Point2DBuffer(1);
        /* 'this' is a Point2D */
        buffer.put(0, this);
        /* 'names' for buffer objects */
        int[] names = new int[1];
        /* generate '1' name(s) for buffer objects
            store the names in 'names' array
            with and offset of '0'
         */
        gl.glGenBuffers(1, names, 0);

        /* GL_ARRAY_BUFFER is a special global var
            names[0] is now that special GL_ARRAY_BUFFER
            opengl will perform operations on
         */
        gl.glBindBuffer(GL.GL_ARRAY_BUFFER, names[0]);
        /* transfer data from main memory into GPU memory
            GL.GL_ARRAY_BUFFER is the buffer in GPU memory
            transfer '2*float.bytes'
            buffer.getBuffer is the data in main memory
            GL_STATIC_DRAW - will this data change often (no in this case)
         */
        gl.glBufferData(GL.GL_ARRAY_BUFFER, 2 * Float.BYTES, buffer.getBuffer(),
                GL.GL_STATIC_DRAW);

        /* attach data to the vertecies in the buffer
            in this case the data is the position of the verticies
         */
        gl.glVertexAttribPointer(Shader.POSITION, 2, GL.GL_FLOAT, false, 0, 0);
        Shader.setModelMatrix(gl, frame.getMatrix());
        gl.glDrawArrays(GL.GL_POINTS, 0, 1);

        gl.glDeleteBuffers(1, names, 0);
    }
    
    /**
     * Draw this point as a dot on the canvas.
     * 
     * This is useful for debugging and for very trivial examples.
     * 
     * @param gl
     */
    public void draw(GL3 gl) {
        draw(gl, CoordFrame2D.identity());
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }
    
    /**
     * Translate the point by the given vector
     * @param dx
     * @param dy
     * @return
     */
    public Point2D translate(float dx, float dy) {
        return new Point2D(x + dx, y + dy);
    }
    
    /**
     * Convert this point to a homogenous coordinate (1 for the z value)
     * 
     * @return
     */
    public Vector3 asHomogenous() {
        return new Vector3(new float[] {x, y, 1});
    }

}
