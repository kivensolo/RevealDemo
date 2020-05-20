package com.example.revealdemo;

import android.content.Context;
import android.view.View;
import android.widget.PopupWindow;

import androidx.appcompat.widget.ViewUtils;


/**
 * author: King.Z <br>
 * date:  2016/8/23 19:52 <br>
 * description: 一个自定义通用的PopupWindow
 *
 * 因PopupWindow的创建方法比较繁琐,但很多东西是可以复用的，
 * 所以，通过建造者模式构建一个公用的popupWindow
 *
 * https://github.com/crazyqiang/AndroidStudy/blob/master/app/src/main/java/org/ninetripods/mq/study/popup/PopupWindowActivity.java
 */
public class CommonPopupWindow extends PopupWindow {
    final PopupController controller;

    public interface ViewInterface {
        void onChildViewCreate(View popView, int layoutResId);
    }

    private CommonPopupWindow(Context context) {
        controller = new PopupController(context, this);
    }

    @Override
    public int getWidth() {
        return controller.mPopupView.getMeasuredWidth();
    }

    @Override
    public int getHeight() {
        return controller.mPopupView.getMeasuredHeight();
    }
    @Override
    public void dismiss() {
        super.dismiss();
        //if(controller.){
            controller.setBackGroundLevel(1.0f);
        //}
    }

    public static class Builder {
        private final PopupController.PopupParams params;
        private ViewInterface listener;

        public Builder(Context context) {
            params = new PopupController.PopupParams(context);
        }

        /**
         * @param layoutResId 设置PopupWindow 布局ID
         * @return Builder
         */
        public Builder setView(int layoutResId) {
            params.mView = null;
            params.layoutResId = layoutResId;
            return this;
        }

         /**
         * @param view 设置PopupWindow布局
         * @return Builder
         */
        public Builder setView(View view) {
            params.mView = view;
            params.layoutResId = 0;
            return this;
        }

        /**
         * 设置宽度和高度 如果不设置 默认是wrap_content
         * @return Builder
         */
        public Builder setSize(int width, int height) {
            params.mWidth = width;
            params.mHeight = height;
            return this;
        }
        /**
         * 设置背景灰色程度
         *
         * @param level 0.0f-1.0f
         * @return Builder
         */
        public Builder setBackGroundLevel(float level) {
            params.isShowBg = true;
            params.bg_level = level;
            return this;
        }
        /**
         * 是否可点击Outside消失
         *
         * @param touchable 是否可点击
         * @return Builder
         */
        public Builder setOutsideTouchable(boolean touchable) {
            params.isTouchable = touchable;
            return this;
        }

        public Builder setFocusable(boolean focusable) {
            params.isFocusable = focusable;
            return this;
        }

        public Builder setClippingEnabled(boolean enabled) {
            params.mClippingEnabled = enabled;
            return this;
        }

        public Builder setAnimationStyle(int animationStyle) {
            params.isShowAnim = true;
            params.animationStyle = animationStyle;
            return this;
        }

        public CommonPopupWindow create() {
            final CommonPopupWindow popupWindow = new CommonPopupWindow(params.mContext);
            params.apply(popupWindow.controller);
            if (listener != null && params.layoutResId != 0) {
                listener.onChildViewCreate(popupWindow.controller.mPopupView, params.layoutResId);
            }
            measureWidthAndHeight(popupWindow.controller.mPopupView);
            return popupWindow;
        }

        public Builder setListener(ViewInterface listener) {
            this.listener = listener;
            return this;
        }
    }

    public static void measureWidthAndHeight(View view) {
        int w = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        int h = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        view.measure(w, h);
    }
}
