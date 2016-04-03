package com.tiantan.handle.utils;

import android.content.Context;
import android.os.SystemClock;

/**计算位移距离的工具类
 * Created by Administrator on 2016/4/2.
 */
public class MyScrollerUtil {
    private Context context;
    private int startX;
    private int startY;
    private int needMoveDistanceX;
    private int needMoveDistanceY;

    //开始执行动画的时间
    private long startTimes;

    //动画是否还在执行 false:正在执行 true:已经结束
    private boolean isFinish;

    //默认的运行时间，单位：毫秒值
    private long duration = 300;
    private long currentX;
    private long currentY;

    public MyScrollerUtil(Context context) {
        this.context = context;
    }

    public long getCurrentX() {
        return currentX;
    }

    public void setCurrentX(long currentX) {
        this.currentX = currentX;
    }

    public long getCurrentY() {
        return currentY;
    }

    public void setCurrentY(long currentY) {
        this.currentY = currentY;
    }

    public void startScroll(int startX, int startY, int needMoveDistanceX, int needMoveDistanceY) {
        this.startX = startX;
        this.startY = startY;
        this.needMoveDistanceX = needMoveDistanceX;
        this.needMoveDistanceY = needMoveDistanceY;
        this.startTimes = SystemClock.uptimeMillis();
        this.isFinish = false;
    }

    /**
     *计算一下当前的运行状况；
     * 返回值：
     * true：还在执行
     * false:已经结束
     */
    public boolean computeScrollOffset() {

        if (isFinish) {
            return  false;
        }

        //获得所用的时间
        long passTime = SystemClock.uptimeMillis() - this.startTimes;

        if (passTime < this.duration){
            this.currentX = this.startX + this.needMoveDistanceX/duration * passTime;
            this.currentY = this.startY + this.needMoveDistanceY/duration * passTime;
        }
        else {
            this.currentX = this.startX + this.needMoveDistanceX;
            this.currentY = this.startY + this.needMoveDistanceY;
            this.isFinish = true;
        }
        return true;
    }



}
