package vanxnf.photovalley;

import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import vanxnf.photovalley.SnakeMenu.RelativeLayoutWithSnakeMenu;

public class MainActivity extends AppCompatActivity {

    private RelativeLayoutWithSnakeMenu snakeMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
