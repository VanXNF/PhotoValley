package vanxnf.photovalley.Util;



import android.content.Context;

import android.graphics.Color;
import android.net.Uri;
import android.os.Build;

import android.os.Environment;
import android.os.storage.StorageManager;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


/**
 * Created by VanXN on 2018/3/2.
 * 工具类
 */

public class Utility {

    /**实现透明状态栏*/
    public static void setStatusBarTransparent(Window window) {
        if (Build.VERSION.SDK_INT >= 21) {
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        }
    }

    /**使爆炸后的控件复现*/
    public static void reSetView(View view) {
        view.setScaleX(1);
        view.setScaleY(1);
        view.setAlpha(1.0f);
    }

    /**获取文件Uri*/
    public static Uri getFileUri(Context context, String authority, File file ) {
        Uri uri;
        if (Build.VERSION.SDK_INT >= 24) {
           uri = FileProvider.getUriForFile(context, authority, file);
            Log.d("MainActivity", uri.toString());
        } else {
           uri = Uri.fromFile(file);
        }
        return uri;
    }

    /**创建相机图片*/
    public static File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_.jpg";
        File cameraImageFile = new File(Environment.getExternalStorageDirectory().getAbsolutePath()
                + "/Pictures/Valley/" + imageFileName);
        cameraImageFile.getParentFile().mkdirs();
        return cameraImageFile;
    }


}
