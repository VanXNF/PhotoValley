package vanxnf.photovalley.View;

import android.content.Context;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;

/**
 * 使图片缩略图保持1:1比例显示
 */
final class ImagePickerSheetImageView extends AppCompatImageView {

    public ImagePickerSheetImageView(Context context) {
        super(context);
    }

    public ImagePickerSheetImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ImagePickerSheetImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }



    @SuppressWarnings("UnnecessaryLocalVariable")
    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int bothDimensionsSpec = widthMeasureSpec;
        super.onMeasure(bothDimensionsSpec, bothDimensionsSpec);
    }
}