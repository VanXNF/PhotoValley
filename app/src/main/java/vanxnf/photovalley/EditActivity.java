package vanxnf.photovalley;

import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.shizhefei.view.largeimage.LargeImageView;

import vanxnf.photovalley.utils.Utility;


public class EditActivity extends AppCompatActivity {

    private String mEditImageString;

    private LargeImageView livDisplayImage;

    private SharedPreferences mPref;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utility.setStatusBarTransparent(getWindow());
        setContentView(R.layout.activity_edit);
        loadImage();

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    /**从上一活动中获取图片Uri并显示*/
    private void loadImage() {

        livDisplayImage = (LargeImageView) findViewById( R.id.large_image_view);
        mEditImageString = getIntent().getStringExtra("image");
        mPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor editor = mPref.edit();
        editor.putString("last_edit_image", mEditImageString);
        editor.apply();
        MainActivity.setIsDelete(false);
        Glide.with(this)
                .load(Uri.parse(mEditImageString))
                .into(new SimpleTarget<Drawable>() {
            @Override
            public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                livDisplayImage.setImage(resource);
            }
        });
    }

}
