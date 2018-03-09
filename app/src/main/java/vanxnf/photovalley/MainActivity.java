package vanxnf.photovalley;


import android.Manifest;
import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.fangxu.allangleexpandablebutton.AllAngleExpandableButton;
import com.fangxu.allangleexpandablebutton.ButtonData;
import com.fangxu.allangleexpandablebutton.ButtonEventListener;
import com.flipboard.bottomsheet.BottomSheetLayout;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import tyrantgit.explosionfield.ExplosionField;
import vanxnf.photovalley.utils.Utility;
import vanxnf.photovalley.widget.CircleImageView;
import vanxnf.photovalley.features.imagepickersheet.ImagePickerSheetView;
import vanxnf.photovalley.features.albumpicker.AlbumPickerActivity;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;

public class MainActivity extends AppCompatActivity {

    public static final int TAKE_PHOTO = 0x10;
    public static final int CHOOSE_PHOTO_FROM_ALBUM = 0x11;
    private static final int CAMERA_PERMISSION = 0x12;
    private static final int ALBUM_PERMISSION = 0x13;
    public static boolean isDelete = true;
    private TextView LuckyText;
    private ExplosionField mExplosionField;
    private CircleImageView lastImage;
    private CircleImageView takePhoto;
    private CircleImageView chooseFromAlbum;
    private AllAngleExpandableButton expandableButton;
    private Uri imageUri;
    private BottomSheetLayout bottomSheetLayout;
    private SharedPreferences preferences;
    private String lastEditedImage;
    private File cameraImage = null;


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
        preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        lastEditedImage = preferences.getString("last_edit_image", null);
        if (lastEditedImage != null) {
             isDelete = false;
             Utility.reSetView(lastImage);
            Glide.with(this).load(Uri.parse(lastEditedImage)).into(new SimpleTarget<Drawable>() {
                @Override
                public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                    lastImage.setImageDrawable(resource);
                }
            });
        }

        //最后编辑图片
        lastImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (lastEditedImage != null) {
                    Intent intent = new Intent(MainActivity.this, EditActivity.class);
                    intent.putExtra("image", lastEditedImage);
                    startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(MainActivity.this).toBundle());
                }

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
                        if (!isDelete) {
                            mExplosionField.explode(lastImage);
                            lastImage.setVisibility(View.GONE);
                            SharedPreferences.Editor editor = preferences.edit();
                            editor.clear();
                            editor.apply();
                            isDelete = true;
                        } else {
                            Toast.makeText(MainActivity.this,"The cache was deleted", Toast.LENGTH_SHORT).show();
                        }
                        break;
                    case 2:
                        // TODO: 2018/3/4 继续上次编辑
                        if (!isDelete) {
                            if (lastEditedImage != null) {
                                Intent intent = new Intent(MainActivity.this, EditActivity.class);
                                intent.putExtra("image", lastEditedImage);
                                startActivity(intent);
                            }
                        } else {
                            Toast.makeText(MainActivity.this,"The cache was deleted", Toast.LENGTH_SHORT).show();
                        }

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
                        notifyMediaUpdate(cameraImage);
                        Intent intent = new Intent(this, EditActivity.class);
                        intent.putExtra("image", tookPhoto.toString());
                        startActivity(intent);
                    } else {
                        genericError(null);
                    }
                }
                break;
            case CHOOSE_PHOTO_FROM_ALBUM:
                //从相册选择图片
                if (resultCode == Activity.RESULT_OK && data != null) {
                    String result = data.getStringExtra(AlbumPickerActivity.SELECT_RESULTS);
                    if (result != null ) {
                        Uri selectedImage = Uri.parse(result);
                        Intent intent = new Intent(MainActivity.this, EditActivity.class);
                        intent.putExtra("image", selectedImage.toString());
                        startActivity(intent);
                    } else {
                        genericError(null);
                    }
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
            cameraImage = Utility.createImageFile();
            imageUri = Utility.getFileUri(MainActivity.this,
                    "vanxnf.photovalley.fileprovider", cameraImage);
            Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
            startActivityForResult(intent, TAKE_PHOTO);

        }
    }

    private void openAlbum() {
        ImagePickerSheetView sheetView = new ImagePickerSheetView.Builder(this)
                .setMaxItems(59)
                .setShowCameraOption(false)
                .setShowPickerOption(true)
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
                            startActivityForResult(new Intent(MainActivity.this, AlbumPickerActivity.class), CHOOSE_PHOTO_FROM_ALBUM);
                        } else if (selectedTile.isImageTile()) {
                            Intent intent = new Intent(MainActivity.this, EditActivity.class);
                            intent.putExtra("image", selectedTile.getImageUri().toString());
                            startActivity(intent);
                        } else {
                            genericError(null);
                        }
                    }
                })
                .setTitle("Choose an image...")
                .create();

        bottomSheetLayout.showWithSheetView(sheetView);
    }

    public void notifyMediaUpdate(File file) {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        Uri contentUri = Uri.fromFile(file);
        mediaScanIntent.setData(contentUri);
        sendBroadcast(mediaScanIntent);
    }

    private void genericError(String message) {
        Toast.makeText(this, message == null ? "Something went wrong." : message, Toast.LENGTH_SHORT).show();
    }
}

