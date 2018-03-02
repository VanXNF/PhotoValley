package vanxnf.photovalley;

import android.app.ActivityOptions;
import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;


import com.fangxu.allangleexpandablebutton.AllAngleExpandableButton;
import com.fangxu.allangleexpandablebutton.ButtonData;
import com.fangxu.allangleexpandablebutton.ButtonEventListener;

import java.util.ArrayList;
import java.util.List;

import tangxiaolv.com.library.EffectiveShapeView;
import tyrantgit.explosionfield.ExplosionField;
import vanxnf.photovalley.Util.Utility;

public class MainActivity extends AppCompatActivity {


    private TextView LuckyText;

    private ExplosionField mExplosionField;

    private EffectiveShapeView lastImage;

    private EffectiveShapeView takePhoto;

    private EffectiveShapeView chooseFromAlbum;

    private AllAngleExpandableButton expandableButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utility.setStatusBarTransparent(getWindow());
        setContentView(R.layout.activity_main);
        lastImage = (EffectiveShapeView) findViewById(R.id.last_image);
        takePhoto = (EffectiveShapeView) findViewById(R.id.take_photo);
        chooseFromAlbum = (EffectiveShapeView) findViewById(R.id.choose_from_album);
        expandableButton = (AllAngleExpandableButton) findViewById(R.id.button_expandable);
        mExplosionField = ExplosionField.attach2Window(this);
        initExpandableButton();

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

    @Override
    protected void onResume() {
        super.onResume();
        //最后编辑图片
        lastImage = (EffectiveShapeView) findViewById(R.id.last_image);
        lastImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: 2018/3/1 点击图片跳转图片编辑页面
                Intent intent = new Intent(MainActivity.this, EditActivity.class);
                startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(MainActivity.this).toBundle());
            }
        });
        //拍照
        takePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mExplosionField.explode(v);
                // TODO: 2018/3/2 跳转相机
            }
        });
        //从相册选择
        chooseFromAlbum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mExplosionField.explode(v);
                // TODO: 2018/3/2 跳转相册选择界面
            }
        });
        //拓展按钮
        expandableButton.setButtonEventListener(new ButtonEventListener() {
            @Override
            public void onButtonClicked(int i) {
                switch (i) {
                    case 1:
                        // TODO: 2018/3/2 同时清除缓存 设置image不可见
                        mExplosionField.explode(lastImage);
                        break;
                    case 2:
                        //还原image
                        // TODO: 2018/3/2 还原删除操作
                        lastImage.setScaleX(1);
                        lastImage.setScaleY(1);
                        lastImage.setAlpha(1.0f);
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
    }

}

