package vanxnf.photovalley;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;


import vanxnf.photovalley.SnakeMenu.RelativeLayoutWithSnakeMenu;

public class MainActivity extends AppCompatActivity {

    private RelativeLayoutWithSnakeMenu snakeMenu;

    private TextView LuckyText;

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

        LuckyText = (TextView) findViewById(R.id.lucky_text);

        setContentView(R.layout.activity_main);
        //设置浮动按钮的点击事件
        snakeMenu = (RelativeLayoutWithSnakeMenu) findViewById(R.id.relative_layout);
        snakeMenu.setMenuListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "menu click", Toast.LENGTH_LONG).show();
            }
        });
    }
}
