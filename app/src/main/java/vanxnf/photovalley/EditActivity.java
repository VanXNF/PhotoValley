package vanxnf.photovalley;

import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.ViewTarget;
import com.bumptech.glide.request.transition.Transition;
import com.shizhefei.view.largeimage.LargeImageView;

import vanxnf.photovalley.Util.Utility;
import vanxnf.photovalley.View.CircleImageView;


public class EditActivity extends AppCompatActivity {

    private ImageView editImage;

    private Uri imageUri;

    private String image;

    private ViewTarget viewTarget;
    private ImageView imageView;
    private CircleImageView circleImageView;

    private LargeImageView largeImageView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utility.setStatusBarTransparent(getWindow());
        setContentView(R.layout.activity_edit);
        image = getIntent().getStringExtra("image");
        largeImageView = (LargeImageView) findViewById( R.id.large_image_view);
        loadLargeImageViewTarget();

        largeImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra("data_return", image);
                setResult(RESULT_OK,intent);
                Log.d("Result", "send success");
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onStop() {
        super.onStop();
        Intent intent = new Intent();
        intent.putExtra("data_return", image);
        setResult(RESULT_OK,intent);
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
