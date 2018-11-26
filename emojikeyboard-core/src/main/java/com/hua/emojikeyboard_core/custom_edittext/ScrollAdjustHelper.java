package com.hua.emojikeyboard_core.custom_edittext;

import android.animation.ValueAnimator;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewParent;

/**
 * @author hua
 * @version V1.0
 * @date 2018/11/26 9:24
 */

public class ScrollAdjustHelper {

    private static final int SCROLL_DURATION = 500;
    private View targetView;
    private View popupView;
    private View containerView;
    private ValueAnimator scrollAnimator;
    private int containerScrollY;
    private int tempScrollBy;

    public ScrollAdjustHelper(View targetView, View popupView) {
        this.targetView = targetView;
        this.popupView = popupView;
        this.containerScrollY = 0;
    }

    public void updateView(View targetView, View popupView) {
        this.targetView = targetView;
        this.popupView = popupView;
        this.containerScrollY = 0;
    }

    public void adjust() {
        int offset = getScrollOffset(targetView, popupView);
        if (containerView == null) {
            containerView = findActivityContentViewGroup(targetView);
        }
        if (containerView != null) {
            scrollContainerWithOffset(containerView, offset - containerScrollY);
        }
    }

    private void scrollContainerWithOffset(final View containerView, final int offset) {
        if (scrollAnimator == null) {
            scrollAnimator = ValueAnimator.ofFloat(0, 1);
            scrollAnimator.setDuration(SCROLL_DURATION);
        }

        if (scrollAnimator.isRunning()) {
            scrollAnimator.cancel();
        }

        scrollAnimator.removeAllUpdateListeners();

        tempScrollBy = 0;
        scrollAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float percent = (float) animation.getAnimatedValue();
                float scrollBy = offset * percent - tempScrollBy + 0.5f;
                containerView.scrollBy(0, (int) scrollBy);
                tempScrollBy += scrollBy;
                containerScrollY += scrollBy;
            }
        });
        scrollAnimator.start();
    }

    public void reset() {
        scrollContainerWithOffset(targetView, -containerScrollY);
    }

    void recycle(){
        this.targetView = null;
        this.popupView = null;
        this.containerView = null;
        this.containerScrollY = 0;
        this.tempScrollBy = 0;
    }

    private static @Nullable
    View findActivityContentViewGroup(View target) {
        if (target.getId() == android.R.id.content) {
            return target;
        }

        ViewParent parent = target.getParent();
        if (parent instanceof View) {
            return findActivityContentViewGroup((View) parent);
        }
        return null;
    }

    private static int getScrollOffset(View targetView, View popupView) {
        int[] tempLocation = new int[2];
        targetView.getLocationOnScreen(tempLocation);
        int focusBottom = tempLocation[1] + targetView.getHeight();
        popupView.getLocationOnScreen(tempLocation);
        int popupTop = tempLocation[1];
        int offset = focusBottom - popupTop;
        return offset > 0 ? offset : 0;
    }

}