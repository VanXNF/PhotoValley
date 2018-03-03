package vanxnf.photovalley;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import com.bumptech.glide.Glide;

import vanxnf.photovalley.Util.Utility;


public class EditActivity extends AppCompatActivity {

    private ImageView editImage;

    private ImageView editPhoto;

    private Uri imageUri;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utility.setStatusBarTransparent(getWindow());
        setContentView(R.layout.activity_edit);
        editImage = (ImageView) findViewById(R.id.image_edit);
        editPhoto = (ImageView) findViewById(R.id.photo_edit);
        String image = getIntent().getStringExtra("image");
        String photo = getIntent().getStringExtra("photo");
        if (image != null) {
            editImage.setVisibility(View.VISIBLE);
            Glide.with(this).load(Uri.parse(image)).into(editImage);
//            editPhoto.setVisibility(View.GONE);
        }
        if (photo != null) {
            editPhoto.setVisibility(View.VISIBLE);
            Glide.with(this).load(Uri.parse(photo)).into(editImage);
//            editImage.setVisibility(View.GONE);
        }

    }

}
