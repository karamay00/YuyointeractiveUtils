package com.yuyointeractive.view;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;

import net.mwplay.nativefont.NativeFont;
import net.mwplay.nativefont.NativeLabel;

/**
 * Created by Administrator on 2016/11/24.
 */

public class MyTimeLabel extends NativeLabel {
    private int initData = 0;
    private int data = 0;
    private MyListener listener;

    public MyTimeLabel(int time, NativeFont font) {
        super("", font);
        init(time);
    }

    public MyTimeLabel(int time, NativeFont font, Color color) {
        super("", font, color);
        init(time);
    }

    public MyTimeLabel(int time, LabelStyle style) {
        super("", style);
        init(time);
    }

    private void init(int time){
        initData = time;
        postText(time + "", new onCompletedListener(){
            @Override
            public void onCompleted(float width, float height) {
                setX(getX() - width / 2f);
            }
        });
    }

    public void setListener(MyListener listener) {
        this.listener = listener;
    }

    public void start() {
        data = initData;
        clearActions();
        addAction(Actions.forever(Actions.sequence(Actions.delay(1), Actions.run(new Runnable() {
            @Override
            public void run() {
                if (data > 0) {
                    data--;
                    postTextNoChangeSize(get0Str(data));
                } else if (data == 0) {
                    clearActions();
                    if (listener != null) {
                        listener.onTimeOut();
                    }
                }
            }
        }))));
    }

    private String get0Str(int data) {
        return String.format("%02d", data);
    }

    public interface MyListener {
        void onTimeOut();
    }
}
