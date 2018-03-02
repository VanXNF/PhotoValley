package vanxnf.photovalley;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;


import com.fangxu.allangleexpandablebutton.AllAngleExpandableButton;
import com.fangxu.allangleexpandablebutton.ButtonData;
import com.fangxu.allangleexpandablebutton.ButtonEventListener;

import java.security.PrivateKey;
import java.util.ArrayList;
import java.util.List;

import tangxiaolv.com.library.EffectiveShapeView;
import tyrantgit.explosionfield.ExplosionField;
import vanxnf.photovalley.SnakeMenu.RelativeLayoutWithSnakeMenu;

public class MainActivity extends AppCompatActivity {

    private RelativeLayoutWithSnakeMenu snakeMenu;

    private TextView LuckyText;

    private ExplosionField mExplosionField;

    private EffectiveShapeView lastImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //实现透明状态栏
        if (Build.VERSION.SDK_INT >= 21) {
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        }
        setContentView(R.layout.activity_main);
        lastImage = (EffectiveShapeView) findViewById(R.id.last_image);
        mExplosionField = ExplosionField.attach2Window(this);
        initExpandableButton();
//        //设置浮动按钮的点击事件
//        snakeMenu = (RelativeLayoutWithSnakeMenu) findViewById(R.id.relative_layout);
//        snakeMenu.setMenuListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(MainActivity.this, "menu click", Toast.LENGTH_LONG).show();
//            }
//        });

        EffectiveShapeView shapeView = (EffectiveShapeView) findViewById(R.id.last_image);
        shapeView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: 2018/3/1 点击图片跳转滤镜页面待完成，粒子爆炸效果待完成
                Toast.makeText(MainActivity.this, "image click", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void initExpandableButton() {
        //start angle 90 end angle 180
        final AllAngleExpandableButton button = (AllAngleExpandableButton) findViewById(R.id.button_expandable_90_180);
        final List<ButtonData> buttonDatas = new ArrayList<>();
        int[] drawable = {R.drawable.home, R.drawable.fab_like, R.drawable.fab_delete, R.drawable.fab_setting};
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
        button.setButtonDatas(buttonDatas);
        setListener(button);
    }

    private void setListener(AllAngleExpandableButton button) {
        button.setButtonEventListener(new ButtonEventListener() {
            @Override
            public void onButtonClicked(int index) {
                showToast("clicked index:" + index);
                switch (index) {
                    case 1:
                        break;
                    case 2:
                        // TODO: 2018/3/2 同时清除缓存 设置image不可见
                        mExplosionField.explode(lastImage);
                        break;
                    case 3:
                        break;
                    default:
                }
            }

            @Override
            public void onExpand() {
//                showToast("onExpand");

            }

            @Override
            public void onCollapse() {
//                showToast("onCollapse");
            }
        });
    }

    private void showToast(String toast) {
        Toast.makeText(this, toast, Toast.LENGTH_SHORT).show();
    }

}

