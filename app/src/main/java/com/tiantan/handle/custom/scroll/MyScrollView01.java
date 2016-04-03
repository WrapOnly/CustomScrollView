package com.tiantan.handle.custom.scroll;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Scroller;

import com.tiantan.handle.utils.MyScrollerUtil;

/**
 * Created by Administrator on 2016/4/2.
 */
public class MyScrollView01 extends ViewGroup {


    private Context mContext;
    private GestureDetector gestureDetector;

    //显示在屏幕上的子View的下标
    private int currentId = 0;
    //down事件时的x坐标
    private int firstX = 0;
    private int firstY = 0;
    private MyScrollerUtil myScrollerUtil;
    /**
     * 系统的动画加速
     */
    private Scroller scroller;

    //是否发生快速滑动
    private boolean isFling;

    private IMyPageChangedListener iMyPageChangedListener;


    public MyScrollView01(Context context) {
        super(context);
        this.mContext = context;
        myScrollerUtil = new MyScrollerUtil(this.mContext);
        this.initView();
    }

    public MyScrollView01(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        myScrollerUtil = new MyScrollerUtil(this.mContext);
        this.initView();
    }

    public MyScrollView01(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
        myScrollerUtil = new MyScrollerUtil(this.mContext);
        this.initView();
    }

    private void initView(){
       this.gestureDetector = new GestureDetector(this.mContext, new GestureDetector.OnGestureListener() {
           @Override
           public boolean onDown(MotionEvent e) {
               return false;
           }

           @Override
           public void onShowPress(MotionEvent e) {

           }

           @Override
           public boolean onSingleTapUp(MotionEvent e) {
               return false;
           }

           /**
            * 响应手指在屏幕上的滑动事件
            */
           @Override
           public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
               scrollBy((int) distanceX, 0);
//               scrollTo((int) distanceX, 0);
               return false;
           }

           @Override
           public void onLongPress(MotionEvent e) {

           }

           /**
            * 发生快速滑动时的回调
            */
           @Override
           public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
               isFling = true;
                if (velocityX > 0 && currentId > 0){//快速向右
                    currentId --;
                }
               else if (velocityX<0 && currentId<getChildCount()-1){//快速向左
                    currentId ++;
                }
                moveToDestination(currentId);
               return false;
           }
       });
    }


    /**
     * 对子View进行布局，确定子View的位置
     * @param changed 若为true,说明布局发生了变化
     * @param l 指当前ViewGroup在其父View中的位置。
     * @param t
     * @param r
     * @param b
     */
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        for (int i = 0; i < getChildCount(); i++) {
            View subView = this.getChildAt(i);
            //指定子View的位置
            /**
             * 父View会根据子View的需求，和自身的情况，来综合确定子View的位置，(确定它的大小)
             */
            subView.layout(i * this.getWidth(), 0, this.getWidth() + i * this.getWidth(), this.getHeight());

            //subView.getWidth();//得到View的真实大小；

        }
    }

    /**
     * 计算控件的大小
     * 作为ViewGroup 还有一个责任，计算子View的大小
     * @param widthMeasureSpec
     * @param heightMeasureSpec
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        for (int i = 0; i < getChildCount(); i++) {
            View view = getChildAt(i);
            view.measure(widthMeasureSpec, heightMeasureSpec);
            view.getMeasuredWidth();//得到计算后的大小
        }
    }

    /**
     * 事件传递机制：
     * 1.View执行dispatchTouchEvent方法，开始分发事件。
     * 2.执行onInterceptTouchEvent 判断是否中断事件。
     * 3.执行onTouchEvent方法，去处理事件。
     */
    /**
     * 分发事件的方法，最早执行，并且不可以随意改动。
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return super.dispatchTouchEvent(ev);
    }

    /**
     * 执行onInterceptTouchEvent之前有个dispatchTouchEvent事件。
     * 是否中断事件的传递
     * @param ev
     * @return 返回true的时候中断事件，执行自己的onTouchEvent方法。返回false的时候，默认处理，不中断，也不会执行自己的onTouchEvent方法。
     */
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        boolean result = false;
        switch (ev.getAction()){
            case MotionEvent.ACTION_DOWN:
                /**
                 * 解决拖动时，图片跳动的BUG。
                 */
                gestureDetector.onTouchEvent(ev);

                firstX = (int) ev.getX();
                firstY = (int) ev.getY();
                break;
            case MotionEvent.ACTION_MOVE:

                //手指在屏幕上水平位移的绝对值
                int distanceX = (int) Math.abs(ev.getX() - firstX);
                //手指在屏幕上竖直位移的绝对值
                int distanceY = (int) Math.abs(ev.getY() - firstY);

                if (distanceX > distanceY && distanceX > 10){//distance>10是为了防止手指抖动触发的事件
                    result = true;
                }
                else {
                    result = false;
                }
                break;
            case MotionEvent.ACTION_UP:
                break;
            default:
                break;
        }
        return result;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        super.onTouchEvent(event);

        this.gestureDetector.onTouchEvent(event);

        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                this.firstX = (int) event.getX();
                break;
            case MotionEvent.ACTION_MOVE:
                break;
            case MotionEvent.ACTION_UP:
                if (!isFling){//在没有发生滑动的时候，才进行位置判断
                    int nextId = 0;
                    if (event.getX() - this.firstX > this.getWidth()/2){
                        //手指向右滑动，超过屏幕的一半
                        nextId = currentId -1;
                    }
                    else if (firstX - event.getX() > getWidth()/2){
                        nextId = currentId + 1;
                    }
                    else {
                        nextId = currentId;
                    }
                    this.moveToDestination(nextId);
                }
                isFling = false;
                break;
            default:
                break;
        }
        return true;
    }

    //移动到指定的屏幕上面
    public void moveToDestination(int nextId) {

        currentId = nextId >= 0 ?nextId : 0;

        currentId = (nextId < getChildCount() -1) ? nextId : (getChildCount() -1);

        //瞬间移动
//        scrollTo(currentId * getWidth(), 0);

        //触发Listener事件
        if (this.iMyPageChangedListener != null){
            this.iMyPageChangedListener.moveToDestination(currentId);
        }
        int needMoveDistance = currentId*getWidth() - getScrollX();//目标位置 - 初始位置 = 要移动的距离

        this.myScrollerUtil.startScroll(getScrollX(), 0, needMoveDistance, 9);
        /**
         * 刷新view， 会导致onDraw方法的执行
         */
        invalidate();
    }

    /**
     * invalidate()；方法会导致computScroll()方法的执行。
     */
    @Override
    public void computeScroll() {
        super.computeScroll();
        boolean isRunning= myScrollerUtil.computeScrollOffset();
        if (isRunning) {
            int newX = (int) myScrollerUtil.getCurrentX();
            Log.e("----", "newx======"+newX);
            scrollTo(newX, 0);
            invalidate();
        }
    }

    public IMyPageChangedListener getiMyPageChangedListener() {
        return iMyPageChangedListener;
    }

    public void setiMyPageChangedListener(IMyPageChangedListener iMyPageChangedListener) {
        this.iMyPageChangedListener = iMyPageChangedListener;
    }

    public interface IMyPageChangedListener{
        void moveToDestination(int currentId);
    }
}














































