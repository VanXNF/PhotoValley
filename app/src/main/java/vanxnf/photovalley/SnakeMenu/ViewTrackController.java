package vanxnf.photovalley.SnakeMenu;

import java.util.List;

/**
 * Created by VanXN on 2018/3/1.
 *
 */

public class ViewTrackController {

    private List<SnakeMenuButton> imageViewList;

    private SnakeMenuButton topView;

    private SnakeMenuButton topFollowerView;

    private int resetPosX, resetPosY;

    private ViewTrackController() {
    }

    public static ViewTrackController create() {
        return new ViewTrackController();
    }

    public void init(List<SnakeMenuButton> imageViewList) {
        this.imageViewList = imageViewList;

        int len = imageViewList.size();
        this.topView = imageViewList.get(len - 1);
        this.topFollowerView = imageViewList.get(len - 2);

        for (int i = 1; i < len; i++) {
            SnakeMenuButton view1 = imageViewList.get(i - 1);
            SnakeMenuButton view2 = imageViewList.get(i);
            view2.getSpringX().addListener(view1.getFollowerListenerX());
            view2.getSpringY().addListener(view1.getFollowerListenerY());
        }
    }

    /**
     * 拖动view的位置改变，后面的view会自动跟着变
     */
    public void onTopViewPosChanged(int xPos, int yPos) {
        // 第一个跟随者移动了，后面的跟随者会自动移动
        topFollowerView.animTo(xPos, yPos);
    }

    /**
     * 手指松开的时候调用
     */
    public void onRelease() {
        topView.onRelease(resetPosX, resetPosY);
    }

    /**
     * 设置view最初的原始位置
     */
    public void setOriginPos(int xPos, int yPos) {
        resetPosX = xPos;
        resetPosY = yPos;

        int len = imageViewList.size();
        for (int i = 0; i < len; i++) {
            imageViewList.get(i).setCurrentSpringPos(xPos, yPos);
        }
    }
}
