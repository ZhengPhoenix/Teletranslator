package phoenix.teletranslator;


import android.app.Activity;
import android.content.Context;
import android.hardware.Camera;
import android.os.Bundle;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

import java.io.IOException;

public class TeletranslatorMain extends Activity implements SurfaceHolder.Callback{

    private static final String TAG = "CvPreiewerActivity";
    private Context mContext;

    private Camera mCamera;
    private TeleSurfaceView mSurfaceView;
    private SurfaceHolder mHolder;


    static{
        try {
            System.loadLibrary("INTSIGBCREngine3");
        } catch (Exception e) {

        }
    }

    public static native int[] MOCR_DetectCardYUV(byte[] paramArrayOfByte, int paramInt1, int paramInt2);

    public static native int MOCR_EnhanceCardImage(int[] paramArrayOfInt, int paramInt1, int paramInt2);

    public static native int[] MOCR_GetBCardRect(int[] paramArrayOfInt, int paramInt1, int paramInt2);

    public static native int[] MOCR_GetBlockIndexArray();

    public static native int[] MOCR_GetLineIndexArray();

    public static native int[] MOCR_GetLinePerWordCount();

    public static native int[] MOCR_GetOriginalBlockIndexArray();

    public static native int[] MOCR_GetOriginalLineIndexArray();

    public static native String[] MOCR_GetOriginalWholeWord();

    public static native int[] MOCR_GetOriginalWholeWordRect();

    public static native String[] MOCR_GetSpecialWord();

    public static native byte[][] MOCR_GetSpecialWordConf();

    public static native byte[][] MOCR_GetSpecialWordConfPercentage();

    public static native int[] MOCR_GetSpecialWordRect();

    public static native int[] MOCR_GetSpecialWordType();

    public static native int[] MOCR_GetTrimedOriginalWholeWordRect();

    public static native int[] MOCR_GetTrimedSpecialWordRect();

    public static native int[] MOCR_GetTrimedWholeWordRect();

    public static native String MOCR_GetVersion();

    public static native String[] MOCR_GetWholeWord();

    public static native int[] MOCR_GetWholeWordRect();

    public static native int[] MOCR_GetWholeWordType();

    public static synchronized native int MOCR_Init(int[] paramArrayOfInt, int paramInt, String paramString1, String paramString2, Context paramContext);

    public static synchronized native int MOCR_Init(int[] paramArrayOfInt, int paramInt, String paramString1, String paramString2, boolean paramBoolean, Context paramContext);

    public static native int MOCR_LookupSpecialWord();

    public static native int MOCR_LookupWholeWord();

    public static native int MOCR_Recognize_Image(int[] paramArrayOfInt, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, boolean paramBoolean1, boolean paramBoolean2);

    public static synchronized native int MOCR_Recognize_Preview(byte[] paramArrayOfByte, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6);

    public static native int[] MOCR_TrimCardImage(int[] paramArrayOfInt1, int paramInt1, int paramInt2, int[] paramArrayOfInt2);

    public native void MOCR_Close();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this.getApplicationContext();
        setContentView(R.layout.activity_cv_previewer);
        initView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mHolder = mSurfaceView.getHolder();
        mHolder.addCallback(this);
        mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        try{
            mCamera = Camera.open(Camera.CameraInfo.CAMERA_FACING_BACK);
        } catch (Exception e) {

        }

        MOCR_Close();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(mCamera != null) {
            mHolder.removeCallback(this);
            mCamera.release();
            mCamera = null;
        }
    }

    private void initView() {
        mSurfaceView = (TeleSurfaceView) findViewById(R.id.main_surfaceview);
    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        if(mCamera != null) {
            try {
                mCamera.setPreviewDisplay(surfaceHolder);
            } catch (IOException e) {

            }
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {
        try{
            setCameraOrientationAndFocus(Camera.CameraInfo.CAMERA_FACING_BACK);
            mCamera.startPreview();
        } catch (Exception e) {

        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {

    }

    private void setCameraOrientationAndFocus(int cameraId) {
        int rotation;
        Camera.CameraInfo info = new Camera.CameraInfo();
        Camera.getCameraInfo(cameraId, info);
        rotation = getWindowManager().getDefaultDisplay().getRotation();
        int degree = 0;
        switch (rotation) {
            case Surface.ROTATION_0:
                degree = 0;
                break;
            case Surface.ROTATION_90:
                degree = 90;
                break;
            case Surface.ROTATION_180:
                degree = 180;
                break;
            case Surface.ROTATION_270:
                degree = 270;
                break;

            default:
                break;
        }
        mCamera.setDisplayOrientation((info.orientation - degree + 360) % 360);

        //setup focus mode
        Camera.Parameters parameters = mCamera.getParameters();
        parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
        mCamera.setParameters(parameters);
    }
}
