package vanxnf.photovalley;

import android.content.SharedPreferences;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.ViewTarget;
import com.bumptech.glide.request.transition.Transition;
import com.shizhefei.view.largeimage.LargeImageView;

import vanxnf.photovalley.Util.Utility;


public class EditActivity extends AppCompatActivity {

    private String image;

    private ViewTarget viewTarget;

    private LargeImageView largeImageView;

    private SharedPreferences preferences;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utility.setStatusBarTransparent(getWindow());
        setContentView(R.layout.activity_edit);
        image = getIntent().getStringExtra("image");
        largeImageView = (LargeImageView) findViewById( R.id.large_image_view);
        preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("last_edit_image", image);
        editor.apply();
        MainActivity.isDelete = false;
        loadLargeImageViewTarget();

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void loadLargeImageViewTarget() {

        viewTarget = new ViewTarget<LargeImageView, BitmapDrawable>( largeImageView ) {
            @Override
            public void onResourceReady(@NonNull BitmapDrawable resource, @Nullable Transition<? super BitmapDrawable> transition) {
                largeImageView.setImageDrawable(resource);
            }
        };
        Glide.with(this).load(image).into(viewTarget);

    }
}
