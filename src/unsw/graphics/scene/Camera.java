package unsw.graphics.scene;

import com.jogamp.opengl.GL3;

import unsw.graphics.CoordFrame2D;
import unsw.graphics.Matrix3;
import unsw.graphics.Shader;
import unsw.graphics.Vector3;
import unsw.graphics.geometry.Point2D;

/**
 * The camera is a SceneObject that can be moved, rotated and scaled like any other, as well as
 * attached to any parent in the scene tree.
 * 
 * TODO: You need to implement the setView() method.
 *
 * @author malcolmr
 * @author Robert Clifton-Everest
 */
public class Camera extends SceneObject {
    
    /**
     * The aspect ratio is the ratio of the width of the window to the height.
     */
    private float myAspectRatio;

    public Camera(SceneObject parent) {
        super(parent);
    }

    public void setView(GL3 gl) {
        // TODO compute a view transform to account for the cameras aspect ratio
        CoordFrame2D viewTransform = CoordFrame2D.identity().scale(1/myAspectRatio, 1);

        // TODO apply further transformations to account for the camera's global position, 
        // rotation and scale
        float scale = getGlobalScale();
        float rotation = getGlobalRotation();
        Point2D translation = getGlobalPosition();

        viewTransform = viewTransform
                .scale(1/scale, 1/scale)
                .rotate(-rotation)
                .translate(-translation.getX(), -translation.getY());



        // TODO set the view matrix to the computed transform
        Shader.setViewMatrix(gl, viewTransform.getMatrix());

    }

    public void reshape(int width, int height) {
        myAspectRatio = (1f * width) / height;            
    }

    /**
     * Transforms a point from camera coordinates to world coordinates. Useful for things like mouse
     * interaction
     * 
     * @param x
     * @param y
     * @return
     */
    public Point2D fromView(float x, float y) {
        Matrix3 mat = Matrix3.translation(getGlobalPosition())
                .multiply(Matrix3.rotation(getGlobalRotation()))
                .multiply(Matrix3.scale(getGlobalScale(), getGlobalScale()))
                .multiply(Matrix3.scale(myAspectRatio, 1));
        return mat.multiply(new Vector3(x,y,1)).asPoint2D();
    }

    public float getAspectRatio() {
        return myAspectRatio;
    }
}
