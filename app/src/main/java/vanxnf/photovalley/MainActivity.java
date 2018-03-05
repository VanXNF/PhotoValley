package vanxnf.photovalley;


import android.Manifest;
import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.ViewTarget;
import com.bumptech.glide.request.transition.Transition;
import com.fangxu.allangleexpandablebutton.AllAngleExpandableButton;
import com.fangxu.allangleexpandablebutton.ButtonData;
import com.fangxu.allangleexpandablebutton.ButtonEventListener;
import com.flipboard.bottomsheet.BottomSheetLayout;
import com.flipboard.bottomsheet.commons.ImagePickerSheetView;
import com.shizhefei.view.largeimage.LargeImageView;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import tyrantgit.explosionfield.ExplosionField;
import vanxnf.photovalley.Util.Utility;
import vanxnf.photovalley.View.CircleImageView;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;

public class MainActivity extends AppCompatActivity {

    public static final int TAKE_PHOTO = 0x10;
    public static final int CHOOSE_PHOTO_FROM_ALBUM = 0x11;
    public static final int EDIT_IMAGE = 0x12;
    private static final int CAMERA_PERMISSION = 0x13;
    private static final int ALBUM_PERMISSION = 0x14;
    private TextView LuckyText;
    private ExplosionField mExplosionField;
    private CircleImageView lastImage;
    private CircleImageView takePhoto;
    private CircleImageView chooseFromAlbum;
    private AllAngleExpandableButton expandableButton;
    private Uri imageUri;
    private BottomSheetLayout bottomSheetLayout;
    private ViewTarget viewTarget;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utility.setStatusBarTransparent(getWindow());
        setContentView(R.layout.activity_main);
        lastImage = (CircleImageView) findViewById(R.id.last_image);
        takePhoto = (CircleImageView) findViewById(R.id.take_photo);
        chooseFromAlbum = (CircleImageView) findViewById(R.id.choose_from_album);
        expandableButton = (AllAngleExpandableButton) findViewById(R.id.button_expandable);
        bottomSheetLayout = (BottomSheetLayout) findViewById(R.id.bottomsheet);
        bottomSheetLayout.setPeekOnDismiss(true);
        mExplosionField = ExplosionField.attach2Window(this);
        initExpandableButton();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Utility.reSetView(takePhoto);
        Utility.reSetView(chooseFromAlbum);
        //最后编辑图片
        lastImage = (CircleImageView) findViewById(R.id.last_image);
        lastImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: 2018/3/4 获取缓存，设定图片本身显示
                Intent intent = new Intent(MainActivity.this, EditActivity.class);
                startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(MainActivity.this).toBundle());
            }
        });
        //拍照
        takePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mExplosionField.explode(v);
                if (ContextCompat.checkSelfPermission(MainActivity.this,
                        Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(MainActivity.this,
                            new String[]{Manifest.permission.CAMERA},CAMERA_PERMISSION);
                } else {
                    openCamera();
                }

            }
        });
        //从相册选择
        chooseFromAlbum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mExplosionField.explode(v);
                if (ContextCompat.checkSelfPermission(MainActivity.this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(MainActivity.this,
                            new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},CAMERA_PERMISSION);
                } else {
                    openAlbum();
                }


            }
        });
        //拓展按钮
        expandableButton.setButtonEventListener(new ButtonEventListener() {
            @Override
            public void onButtonClicked(int i) {
                switch (i) {
                    case 1:
                        // TODO: 2018/3/4 删除上次图片缓存
                        mExplosionField.explode(lastImage);
                        lastImage.setVisibility(View.GONE);
                        break;
                    case 2:
                        // TODO: 2018/3/4 继续上次编辑
                        Utility.reSetView(lastImage);
                        lastImage.setVisibility(View.VISIBLE);
                        break;
                    case 3:
                        // TODO: 2018/3/2 设置界面 引导界面
                        break;
                    default:
                }
            }

            @Override
            public void onExpand() {}

            @Override
            public void onCollapse() {}
        });
        //监听弹出菜单状态
        bottomSheetLayout.addOnSheetStateChangeListener(new BottomSheetLayout.OnSheetStateChangeListener(){
            @Override
            public void onSheetStateChanged(BottomSheetLayout.State state) {
                if (state == BottomSheetLayout.State.HIDDEN) {
                    Utility.reSetView(chooseFromAlbum);
                    chooseFromAlbum.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case TAKE_PHOTO:
                if (resultCode == RESULT_OK && data != null) {
                    final Uri tookPhoto = imageUri;
                    if (tookPhoto != null) {
                        // TODO: 2018/3/5 完成拍照后及时更新系统相册 
                        Intent broadcastIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                        broadcastIntent.setData(imageUri);
                        this.sendBroadcast(broadcastIntent);
                        Log.d("Result", "Photo");
                        Intent intent = new Intent(this, EditActivity.class);
                        intent.putExtra("image", tookPhoto.toString());
                        startActivityForResult(intent,EDIT_IMAGE);
                    } else {
                        genericError(null);
                    }
                }
                break;
            case CHOOSE_PHOTO_FROM_ALBUM:
                if (resultCode == Activity.RESULT_OK && data != null) {
                    Uri selectedImage = null;
                    selectedImage = data.getData();

                    if (selectedImage != null) {
                        Intent intent = new Intent(MainActivity.this, EditActivity.class);
                        intent.putExtra("image", selectedImage.toString());
                        startActivityForResult(intent,EDIT_IMAGE);
                    } else {
                        genericError(null);
                    }
                }
                break;
            case EDIT_IMAGE:
                if (resultCode == Activity.RESULT_OK && data != null) {
                    String s = data.getStringExtra("data_return");
                    Glide.with(this).load(Uri.parse(s)).into(new SimpleTarget<Drawable>() {
                        @Override
                        public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                            lastImage.setImageDrawable(resource);
                        }
                    });
                }
                break;
            default:
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case ALBUM_PERMISSION:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    openAlbum();
                } else {
                    Toast.makeText(this, getString(R.string.denied_permission), Toast.LENGTH_SHORT).show();
                }
                break;
            case CAMERA_PERMISSION:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    openCamera();
                } else {
                    Toast.makeText(this, getString(R.string.denied_permission), Toast.LENGTH_SHORT).show();
                }
                break;
            default:
        }
    }

    private void initExpandableButton() {
        //start angle 90 end angle 180
        final List<ButtonData> buttonDatas = new ArrayList<>();
        int[] drawable = {R.drawable.home, R.drawable.fab_delete, R.drawable.fab_revert, R.drawable.fab_setting};
        int[] color = {R.color.background, R.color.red, R.color.brown, R.color.orange};
        for (int i = 0; i < 4; i++) {
            ButtonData buttonData;
            if (i == 0) {
                buttonData = ButtonData.buildIconButton(this, drawable[i], 15);
            } else {
                buttonData = ButtonData.buildIconButton(this, drawable[i], 0);
            }
            buttonData.setBackgroundColorId(this, color[i]);
            buttonDatas.add(buttonData);

        }
        expandableButton.setButtonDatas(buttonDatas);

    }

    private void openCamera() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            try {
                File cameraImage = null;
                cameraImage = Utility.createImageFile();
                imageUri = Utility.getFileUri(MainActivity.this,
                        "vanxnf.photovalley.fileprovider", cameraImage);
                Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                startActivityForResult(intent, TAKE_PHOTO);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void openAlbum() {
        ImagePickerSheetView sheetView = new ImagePickerSheetView.Builder(this)
                .setMaxItems(59)
                .setShowCameraOption(false)
                .setShowPickerOption(createPickIntent() != null)
                .setImageProvider(new ImagePickerSheetView.ImageProvider() {
                    @Override
                    public void onProvideImage(ImageView imageView, Uri imageUri, int size) {
                        RequestOptions options = new RequestOptions()
                                .centerCrop();
                        Glide.with(MainActivity.this)
                                .load(imageUri)
                                .apply(options)
                                .transition(withCrossFade())
                                .into(imageView);
                    }
                })
                .setOnTileSelectedListener(new ImagePickerSheetView.OnTileSelectedListener() {
                    @Override
                    public void onTileSelected(ImagePickerSheetView.ImagePickerTile selectedTile) {
                        bottomSheetLayout.dismissSheet();
                        if (selectedTile.isPickerTile()) {
                            startActivityForResult(createPickIntent(), CHOOSE_PHOTO_FROM_ALBUM);
                        } else if (selectedTile.isImageTile()) {
                            Intent intent = new Intent(MainActivity.this, EditActivity.class);
                            intent.putExtra("image", selectedTile.getImageUri().toString());
                            startActivityForResult(intent,EDIT_IMAGE);
                        } else {
                            genericError(null);
                        }
                    }
                })
                .setTitle("Choose an image...")
                .create();

        bottomSheetLayout.showWithSheetView(sheetView);
    }

    @Nullable
    private Intent createPickIntent() {
        Intent picImageIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        if (picImageIntent.resolveActivity(getPackageManager()) != null) {
            return picImageIntent;
        } else {
            return null;
        }
    }

    private void genericError(String message) {
        Toast.makeText(this, message == null ? "Something went wrong." : message, Toast.LENGTH_SHORT).show();
    }
}

