package unsw.graphics.scene;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.jogamp.opengl.GL3;

import unsw.graphics.CoordFrame2D;
import unsw.graphics.Matrix3;
import unsw.graphics.Vector3;
import unsw.graphics.geometry.Point2D;

import static com.jogamp.opengl.math.FloatUtil.atan2;

/**
 * A SceneObject is an object that can move around in the world.
 * 
 * SceneObjects form a scene tree.
 * 
 * Each SceneObject is offset from its parent by a translation, a rotation and a scale factor. 
 *
 * TODO: The methods you need to complete are at the bottom of the class
 *
 * @author malcolmr
 * @author Robert Clifton-Everest
 */
public class SceneObject {
    
    // the links in the scene tree
    private SceneObject myParent;
    private List<SceneObject> myChildren;

    // the local transformation
    private Point2D myTranslation;
    private float myRotation; //normalised to the range [-180..180)
    private float myScale;
    
    // Is this part of the tree showing?
    private boolean amShowing;

    /**
     * Special constructor for creating the root node. Do not use otherwise.
     */
    public SceneObject() {
        myParent = null;
        myChildren = new ArrayList<SceneObject>();

        myRotation = 0;
        myScale = 1;
        myTranslation = new Point2D(0,0);

        amShowing = true;
    }

    /**
     * Public constructor for creating SceneObjects, connected to a parent.
     *  
     * New objects are created at the same location, orientation and scale as the parent.
     *
     * @param parent
     */
    public SceneObject(SceneObject parent) {
        myParent = parent;
        myChildren = new ArrayList<SceneObject>();

        parent.myChildren.add(this);

        myRotation = 0;
        myScale = 1;
        myTranslation = new Point2D(0,0);

        // initially showing
        amShowing = true;
    }

    /**
     * Remove an object and all its children from the scene tree.
     */
    public void destroy() {
	    List<SceneObject> childrenList = new ArrayList<SceneObject>(myChildren);
        for (SceneObject child : childrenList) {
            child.destroy();
        }
        if(myParent != null)
                myParent.myChildren.remove(this);
    }

    /**
     * Get the parent of this scene object
     * 
     * @return
     */
    public SceneObject getParent() {
        return myParent;
    }

    /**
     * Get the children of this object
     * 
     * @return
     */
    public List<SceneObject> getChildren() {
        return myChildren;
    }

    /**
     * Get the local rotation (in degrees)
     * 
     * @return
     */
    public float getRotation() {
        return myRotation;
    }

    /**
     * Set the local rotation (in degrees)
     * 
     * @return
     */
    public void setRotation(float rotation) {
        myRotation = MathUtil.normaliseAngle(rotation);
    }

    /**
     * Rotate the object by the given angle (in degrees)
     * 
     * @param angle
     */
    public void rotate(float angle) {
        myRotation += angle;
        myRotation = MathUtil.normaliseAngle(myRotation);
    }

    /**
     * Get the local scale
     * 
     * @return
     */
    public float getScale() {
        return myScale;
    }

    /**
     * Set the local scale
     * 
     * @param scale
     */
    public void setScale(float scale) {
        myScale = scale;
    }

    /**
     * Multiply the scale of the object by the given factor
     * 
     * @param factor
     */
    public void scale(float factor) {
        myScale *= factor;
    }

    /**
     * Get the local position of the object 
     * 
     * @return
     */
    public Point2D getPosition() {
        return myTranslation;
    }

    /**
     * Set the local position of the object
     * 
     * @param x
     * @param y
     */
    public void setPosition(float x, float y) {
        setPosition(new Point2D(x,y));
    }

    /**
     * Set the local position of the object
     * 
     */
    public void setPosition(Point2D p) {
        myTranslation = p;
    }

    /**
     * Move the object by the specified offset in local coordinates
     * 
     * @param dx
     * @param dy
     */
    public void translate(float dx, float dy) {
        myTranslation = myTranslation.translate(dx, dy);
    }

    /**
     * Test if the object is visible
     * 
     * @return
     */
    public boolean isShowing() {
        return amShowing;
    }

    /**
     * Set the showing flag to make the object visible (true) or invisible (false).
     * This flag should also apply to all descendents of this object.
     * 
     * @param showing
     */
    public void show(boolean showing) {
        amShowing = showing;
    }

    /**
     * Update the object and all it's children. This method is called once per frame. 
     * 
     * @param dt The amount of time since the last update (in seconds)
     */
    public void update(float dt) {
        updateSelf(dt);
        
        // Make a copy of all the children to avoid concurrently modification issues if new objects
        // are added to the scene during the update.
        List<SceneObject> children = new ArrayList<SceneObject>(myChildren);
        for (SceneObject so : children) {
            so.update(dt);
        }
    }

    /** 
     * Update the object itself. Does nothing in the default case. Subclasses can override this
     * for animation or interactivity.
     * 
     * @param dt
     */
    public void updateSelf(float dt) {
        // Do nothing by default
    }

    /**
     * Draw the object (but not any descendants)
     * 
     * This does nothing in the base SceneObject class. Override this in subclasses.
     * 
     * @param gl
     */
    public void drawSelf(GL3 gl, CoordFrame2D frame) {
        // Do nothing by default
    }

    
    // ===========================================
    // COMPLETE THE METHODS BELOW
    // ===========================================
    
    /**
     * Draw the object and all of its descendants recursively.
     * 
     * TODO: Complete this method
     * 
     * @param gl
     */
    public void draw(GL3 gl, CoordFrame2D frame) {
        
        // don't draw if it is not showing
        if (!amShowing) {
            return;
        }


        // compute coord frame for this obj
        CoordFrame2D objFrame = frame
                .translate(myTranslation)
                .rotate(myRotation)
                .scale(myScale, myScale);

        // draw self
        this.drawSelf(gl, objFrame);
        // draw children
        for (SceneObject child: myChildren) {
            child.draw(gl, objFrame);
        }

        
    }

    private CoordFrame2D getLocalCoordFrame() {
        // compute coord frame for this obj
        CoordFrame2D objFrame = CoordFrame2D.identity()
                .translate(myTranslation)
                .rotate(myRotation)
                .scale(myScale, myScale);

        return objFrame;
    }

    private Matrix3 getLocalToGlobalTransform() {

        SceneObject parent = myParent;

        Matrix3 parentTransform;
        Matrix3 localTransform = getLocalCoordFrame().getMatrix();

        if (parent == null) {
            /* at root - no parent, set identity */
            parentTransform = CoordFrame2D.identity().getMatrix();
        } else {
            /* not at parent - recurse */
            /* will return a transform which will transform local frame into global */
            parentTransform = parent.getLocalToGlobalTransform();
        }

        /* multiply to get global transform */
        Matrix3 globalTransform = parentTransform.multiply(localTransform);


        return globalTransform;

    }

    /* TODO Orgainise */

    /**
     *
     * @param mat
     * @return
     */
    public Point2D getTranslationFromMatrix(Matrix3 mat) {
        float matVals[] = mat.getValues();

        float x = matVals[6];
        float y = matVals[7];

        Point2D translation = new Point2D(x, y);

        return translation;
    }


    /**
     *
     * @param mat
     * @return
     */
    public float getRotationFromMatrix(Matrix3 mat) {

        float matVals[] = mat.getValues();

        float i1 = matVals[0];
        float i2 = matVals[1];

        float rot = (float) Math.toDegrees(atan2(i2, i1));

        return rot;
    }

    /**
     *
     * @param mat
     * @return
     */
    public float getScaleFromMatrix(Matrix3 mat) {
        float matVals[] = mat.getValues();

        float scale = (float) Math.sqrt(Math.pow(matVals[0],2) + Math.pow(matVals[1],2));

        return scale;
    }


    /**
     * Compute the object's position in world coordinates
     * 
     * @return a point in world coordinats
     */
    public Point2D getGlobalPosition() {

        Matrix3 globalTransform = getLocalToGlobalTransform();

        Point2D globalPos = getTranslationFromMatrix(globalTransform);

        return globalPos;
    }


    /**
     * Compute the object's rotation in the global coordinate frame
     * 
     * @return the global rotation of the object (in degrees) and 
     * normalized to the range (-180, 180) degrees. 
     */
    public float getGlobalRotation() {

        Matrix3 globalTransform = getLocalToGlobalTransform();

        float rot = getRotationFromMatrix(globalTransform);

        return rot;
    }

    /**
     * Compute the object's scale in global terms
     * 
     * @return the global scale of the object 
     */
    public float getGlobalScale() {
        Matrix3 globalTransform = getLocalToGlobalTransform();

        float scale = getScaleFromMatrix(globalTransform);

        return scale;
    }


    /**
     * Change the parent of a scene object.
     * 
     * @param parent
     */
    public void setParent(SceneObject parent) {
        // TODO: add code so that the object does not change its global position, rotation or scale
        // when it is reparented. You may need to add code before and/or after 
        // the fragment of code that has been provided - depending on your approach

        /* get parent global transform */
        Matrix3 parentTransform = parent.getLocalToGlobalTransform();

        // TODO: consider making this an inverse
        /* get inverse of parent global transform */
        Point2D parentTranslation = getTranslationFromMatrix(parentTransform);
        float parentRoation = getRotationFromMatrix(parentTransform);
        float parentScale = getScaleFromMatrix(parentTransform);

        Point2D parentInverseTranslation = new Point2D(-parentTranslation.getX(), -parentTranslation.getY());
        float parentInverseRotation = -parentRoation;
        float parentInverseScale = 1/parentScale;

        Matrix3 inverseScaleMat = Matrix3.identity().scale(parentInverseScale, parentInverseScale);
        Matrix3 inverseRotMat = Matrix3.identity().rotation(parentInverseRotation);
        Matrix3 inverseTranslationMat = Matrix3.identity().translation(parentInverseTranslation);

        Matrix3 parentInverseTransform = inverseScaleMat.multiply(inverseRotMat);
        parentInverseTransform = parentInverseTransform.multiply(inverseTranslationMat);



        /* get object's global transform */
        Matrix3 objTransform =  getLocalToGlobalTransform();

        /* get the new global transform for this object
        *  this is done by multiplying the parent's global
        *  inverse transform with this object's global transform
        */
        Matrix3 newObjTransform = parentInverseTransform.multiply(objTransform);


        /* set new new position, scale, rotation */
        this.setPosition(getTranslationFromMatrix(newObjTransform));
        this.setRotation(getRotationFromMatrix(newObjTransform));
        this.setScale(getScaleFromMatrix(newObjTransform));


        myParent.myChildren.remove(this);
        myParent = parent;
        myParent.myChildren.add(this);
        
    }
    

}
