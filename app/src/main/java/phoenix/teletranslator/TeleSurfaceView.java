package phoenix.teletranslator;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceView;

/**
 * Created by Phoenix on 2016/5/8.
 */
public class TeleSurfaceView extends SurfaceView {

    public TeleSurfaceView(Context context) {
        super(context);
    }

    public TeleSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.d("enheng", "onTouchEvent, x: " + event.getX() + " y: " + event.getY());
        return super.onTouchEvent(event);
    }
}
