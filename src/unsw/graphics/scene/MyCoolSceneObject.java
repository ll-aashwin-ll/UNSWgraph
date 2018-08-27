package unsw.graphics.scene;

import java.awt.*;

import static java.lang.Math.abs;

/**
 * A cool scene object
 *
 */
public class MyCoolSceneObject extends SceneObject {

    private final static int NUM_BALLS = 7;

    private final static float CRADLE_START_X = -.5f;
    private final static float CRADLE_END_X = .5f;
    private final static float CRADLE_START_Y = .25f;
    private final static float CRADLE_END_Y = -.25f;

    private final static float CRADLE_WIDTH = abs(CRADLE_START_X-CRADLE_END_X);

    private final static float WIRE_START_X = CRADLE_START_X + (CRADLE_WIDTH * 0.1f);
    private final static float WIRE_END_X = CRADLE_END_X - (CRADLE_WIDTH * 0.1f);
    private final static float WIRE_START_Y = CRADLE_START_Y;
    private final static float WIRE_END_Y = CRADLE_END_Y+.1f;

    private final static float WIRE_LENGTH = abs(WIRE_START_Y - WIRE_END_Y);

    private final static float TOTAL_WIRE_DISPLACEMENT_X = abs(WIRE_START_X-WIRE_END_X);


    public MyCoolSceneObject(SceneObject parent) {
        super(parent);

        LineSceneObject topBar = new LineSceneObject(this, CRADLE_START_X, CRADLE_START_Y, CRADLE_END_X, CRADLE_START_Y, Color.ORANGE);
        topBar.setLineWidth(5);


        float wireOffset;
        float evenSpacingOffset;
        LineSceneObject wire = null;
        for (int i = 0; i < NUM_BALLS; i++) {
            wireOffset = i * (TOTAL_WIRE_DISPLACEMENT_X/NUM_BALLS);
            evenSpacingOffset = wireOffset / NUM_BALLS;

            float wireX = WIRE_START_X + wireOffset + evenSpacingOffset;

            wire = new LineSceneObject(topBar, 0, -WIRE_LENGTH, 0, 0, Color.DARK_GRAY);
            wire.translate(wireX, WIRE_START_Y );
            CircularSceneObject ball = new CircularSceneObject(wire, .065f, Color.GRAY, Color.DARK_GRAY);
            ball.translate(0, -WIRE_LENGTH);

        }

        wire.rotate(44);

        LineSceneObject leftBar = new LineSceneObject(topBar, CRADLE_START_X, CRADLE_START_Y, CRADLE_START_X, CRADLE_END_Y, Color.orange);
        leftBar.setLineWidth(3);

        LineSceneObject rightBar = new LineSceneObject(topBar, CRADLE_END_X, CRADLE_START_Y, CRADLE_END_X, CRADLE_END_Y, Color.orange);
        rightBar.setLineWidth(3);

    }


}
