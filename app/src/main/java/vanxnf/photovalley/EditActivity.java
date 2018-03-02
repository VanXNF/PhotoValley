package vanxnf.photovalley;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import vanxnf.photovalley.Util.Utility;

public class EditActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utility.setStatusBarTransparent(getWindow());
        setContentView(R.layout.activity_edit);
    }
}
