package vanxnf.photovalley;

import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.ViewTarget;
import com.bumptech.glide.request.transition.Transition;
import com.shizhefei.view.largeimage.LargeImageView;

import vanxnf.photovalley.Util.Utility;


public class EditActivity extends AppCompatActivity {

    private ImageView editImage;

    private Uri imageUri;

    private ViewTarget viewTarget;


    private LargeImageView largeImageView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utility.setStatusBarTransparent(getWindow());
        setContentView(R.layout.activity_edit);
        loadLargeImageViewTarget();
//        loadImageViewTarget();
//        editImage = (ImageView) findViewById(R.id.image_edit);
//        shadow = (ShadowImageView) findViewById(R.id.shadow);
//        largeImageView = (LargeImageView) findViewById(R.id.imageView);
//        String image = getIntent().getStringExtra("image");
//        if (image != null) {
//            editImage.setVisibility(View.VISIBLE);
//            Glide.with(this).load(Uri.parse(image)).into(largeImageView);
//        }

    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    private void loadLargeImageViewTarget() {
        final LargeImageView customView = (LargeImageView) findViewById( R.id.large_image_view);

        viewTarget = new ViewTarget<LargeImageView, BitmapDrawable>( customView ) {
            @Override
            public void onResourceReady(@NonNull BitmapDrawable resource, @Nullable Transition<? super BitmapDrawable> transition) {
                customView.setImageDrawable(resource);
            }
        };
        String image = getIntent().getStringExtra("image");
        Glide.with(this).load(image).into(viewTarget);

    }
}
