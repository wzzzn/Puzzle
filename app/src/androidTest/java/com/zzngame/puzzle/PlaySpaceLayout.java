package com.zzngame.puzzle;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.zzngame.utils.ImagePiece;
import com.zzngame.utils.ImageSplitterUtil;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by Host on 2015/2/16.
 */
@SuppressLint("HandlerLeak")
public class PlaySpaceLayout extends RelativeLayout implements View.OnClickListener{

    private int mXColumn = 3;
    private int

    // 容器的内边距
    private int mPadding;
    // 每张小图之间的距离（横、纵）
    private int mMagin = 3;
    private ImageView[] mGamePintuItems;
    private int mItemWidth;
    private Bitmap mBitmap;
    private List<ImagePiece> mItemBitmaps;
    private boolean once;
    // 游戏面板宽度
    private int mWidth;
    private boolean isGameSuccess;
    private boolean isGameOver;

    public interface GamePintuListener {
        void nextLevel(int nextLevel);

        void timeChanged(int currentTime);

        void gameOver();
    }

    public GamePintuListener mListener;

    /**
     * 设置接口回调
     *
     * @param mListener
     */
    public void setOnGamePintuListener(GamePintuListener mListener) {
        this.mListener = mListener;
    }
    public class Receiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String content=intent.getStringExtra("val");
            CharSequence string="收到信息:"+content;
            Toast.makeText(context, string, Toast.LENGTH_LONG).show();
        }
    }

    private int mLevel = 1;
    private static final int TIME_CHANGED = 0x110;
    private static final int NEXT_LEVEL = 0x111;

    private Handler mHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case TIME_CHANGED:
                    if (isGameSuccess || isGameOver || isPause) {
                        return;
                    }
                    if (mListener != null) {
                        mListener.timeChanged(mTime);
                        if (mTime == 0) {
                            isGameOver = true;
                            mListener.gameOver();
                            return;
                        }
                    }
                    mTime--;
                    mHandler.sendEmptyMessageDelayed(TIME_CHANGED, 1000);
                    break;
                case NEXT_LEVEL:
                    if (mListener != null) {
                        mLevel = mLevel + 1;
                        mListener.nextLevel(mLevel);
                    } else {
                        nextLevel();
                    }
                    break;

                default:
                    break;
            }

        };
    };

    private boolean isTimeEnabled = false;
    private int mTime;

    // private int mLevel;

    /**
     * 设置是否开启时间
     *
     * @param isTimeEnabled
     */
    public void setTimeEnabled(boolean isTimeEnabled) {
        this.isTimeEnabled = isTimeEnabled;
    }

    public PlaySpaceLayout(Context context) {
        this(context, null);
    }

    public PlaySpaceLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PlaySpaceLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();

    }

    private void init() {
        mMagin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                3, getResources().getDisplayMetrics());
        mPadding = min(getPaddingLeft(), getPaddingRight(), getPaddingTop(),
                getPaddingBottom());
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        // 取宽和高中的小值
        mWidth = Math.min(getMeasuredHeight(), getMeasuredWidth());
//        mWidth=Math.min(getHeight(),getWidth());

        if (!once) {
            // 进行切图以及排序
            initBitmap();
            // 设置ImageView(Item)的宽高等属性
            initItem();
            // 判断时间是否开启
            checkTimeEnable();

            once = true;
        }
        setMeasuredDimension(mWidth, mWidth);
    }

    private void checkTimeEnable() {
        if (isTimeEnabled) {
            // 根据当前等级设置时间
            countTimeBaseLevel();
            mHandler.sendEmptyMessage(TIME_CHANGED);
        }
    }

    private void countTimeBaseLevel() {
        mTime = (int) Math.pow(2, mLevel) * 60;
        // mTime=5;
    }

    /**
     * 进行切图以及排序
     */


    private void initBitmap() {
        if (mBitmap == null) {
            mBitmap = BitmapFactory.decodeResource(getResources(),
                    R.mipmap.image_test);
        }
        mItemBitmaps = ImageSplitterUtil.splitImage(mBitmap, mColum);

        // 使用sort完成我们的乱序
        Collections.sort(mItemBitmaps, new Comparator<ImagePiece>() {
            @Override
            public int compare(ImagePiece a, ImagePiece b) {
                return Math.random() > 0.5 ? 1 : -1;
            }
        });
    }

    /**
     * 设置ImageView(Item)的宽高等属性
     */
    private void initItem() {
        mItemWidth = (mWidth - mPadding * 2 - mMagin * (mColum - 1)) / mColum;
        mGamePintuItems = new ImageView[mColum * mColum];
        // 生成我们的Item，设置Rule
        for (int i = 0; i < mGamePintuItems.length; i++) {
            ImageView item = new ImageView(getContext());
            item.setOnClickListener(this);
            item.setImageBitmap(mItemBitmaps.get(i).getBitmap());
            mGamePintuItems[i] = item;
            item.setId(i + 1);
            // 在Item的tag中存储了index
            item.setTag(i + "_" + mItemBitmaps.get(i).getIndex());

            LayoutParams lp = new LayoutParams(
                    mItemWidth, mItemWidth);

            // 设置Item间横向间隙，通过rightMargin，不是最后一列
            if ((i + 1) % mColum != 0) {
                lp.rightMargin = mMagin;
            }

            // 如果不是第一列
            if (i % mColum != 0) {
                lp.addRule(RelativeLayout.RIGHT_OF,
                        mGamePintuItems[i - 1].getId());
            }

            // 如果不是第一行，设置topMargin和rule
            if ((i + 1) > mColum) {
                lp.topMargin = mMagin;
                lp.addRule(RelativeLayout.BELOW,
                        mGamePintuItems[i - mColum].getId());
            }

            addView(item, lp);
        }
    }

    public void restart() {
        isGameOver = false;
        mColum--;
        nextLevel();
    }

    private boolean isPause;

    public void pause() {
        isPause = true;
        mHandler.removeMessages(TIME_CHANGED);
    }

    public void resume() {
        if (isPause) {
            isPause = false;
            mHandler.sendEmptyMessage(TIME_CHANGED);
        }
    }

    public void nextLevel() {
        this.removeAllViews();
        mAnimLayout = null;
        mColum++;
        isGameSuccess = false;
        checkTimeEnable();
        initBitmap();
        initItem();
    }

    /**
     * 获取参数的最小值
     */
    private int min(int... params) {
        int min = params[0];

        for (int param : params) {
            if (param < min)
                min = param;
        }
        return min;
    }

    private ImageView mFirst;
    private ImageView mSecond;

    @Override
    public void onClick(View v) {
        if (isAniming) {
            return;
        }
        // 两次点击同一个Item，取消高亮
        if (mFirst == v) {
            mFirst.setColorFilter(null);
            mFirst = null;
            return;
        }

        if (mFirst == null) {
            mFirst = (ImageView) v;
            mFirst.setColorFilter(Color.parseColor("#55FF0000"));
        } else {
            mSecond = (ImageView) v;
            // 交换我们的Item
            exchangeView();
        }
    }

    // 动画层
    private RelativeLayout mAnimLayout;
    private boolean isAniming;

    /**
     * 交换我们的Item
     */
    private void exchangeView() {
        mFirst.setColorFilter(null);
        // 构造动画层
        setUpAnimLayout();

        ImageView first = new ImageView(getContext());
        final Bitmap firstBitmap = mItemBitmaps.get(
                getImageIdByTag((String) mFirst.getTag())).getBitmap();
        first.setImageBitmap(firstBitmap);
        LayoutParams lp1 = new LayoutParams(mItemWidth, mItemWidth);
        lp1.leftMargin = mFirst.getLeft() - mPadding;
        lp1.topMargin = mFirst.getTop() - mPadding;
        first.setLayoutParams(lp1);
        mAnimLayout.addView(first);

        ImageView second = new ImageView(getContext());
        final Bitmap secondBitmap = mItemBitmaps.get(
                getImageIdByTag((String) mSecond.getTag())).getBitmap();
        second.setImageBitmap(secondBitmap);
        LayoutParams lp2 = new LayoutParams(mItemWidth, mItemWidth);
        lp2.leftMargin = mSecond.getLeft() - mPadding;
        lp2.topMargin = mSecond.getTop() - mPadding;
        second.setLayoutParams(lp2);
        mAnimLayout.addView(second);

        // 设置动画
        TranslateAnimation animfirst = new TranslateAnimation(0,
                mSecond.getLeft() - mFirst.getLeft(), 0, mSecond.getTop()
                - mFirst.getTop());
        animfirst.setDuration(300);
        animfirst.setFillAfter(true);
        first.startAnimation(animfirst);

        TranslateAnimation animsecond = new TranslateAnimation(0,
                -mSecond.getLeft() + mFirst.getLeft(), 0, -mSecond.getTop()
                + mFirst.getTop());
        animsecond.setDuration(300);
        animsecond.setFillAfter(true);
        second.startAnimation(animsecond);

        // 监听动画
        animfirst.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {
                mFirst.setVisibility(View.INVISIBLE);
                mSecond.setVisibility(View.INVISIBLE);
                isAniming = true;
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                String firstTag = (String) mFirst.getTag();
                String secondTag = (String) mSecond.getTag();

                mFirst.setImageBitmap(secondBitmap);
                mSecond.setImageBitmap(firstBitmap);

                mFirst.setTag(secondTag);
                mSecond.setTag(firstTag);

                mFirst.setVisibility(View.VISIBLE);
                mSecond.setVisibility(View.VISIBLE);

                mFirst = mSecond = null;
                mAnimLayout.removeAllViews();
                // 判断用户游戏是否成功
                checkSuccess();
                isAniming = false;
            }

            /**
             *
             */
            private void checkSuccess() {
                boolean isSuccess = true;
                for (int i = 0; i < mGamePintuItems.length; i++) {
                    ImageView imageView = mGamePintuItems[i];
                    if (getImageIndexByTag((String) imageView.getTag()) != i) {
                        isSuccess = false;
                    }
                }
                if (isSuccess) {
                    isGameSuccess = true;
                    mHandler.removeMessages(TIME_CHANGED);
                    Toast.makeText(getContext(), "Success , Level up!!!",
                            Toast.LENGTH_LONG).show();
                    mHandler.sendEmptyMessage(NEXT_LEVEL);
                }
            }
        });

    }

    /**
     * 根据tag获取Id
     *
     * @param tag
     * @return
     */
    public int getImageIdByTag(String tag) {
        String[] split = tag.split("_");
        return Integer.parseInt(split[0]);
    }

    public int getImageIndexByTag(String tag) {
        String[] split = tag.split("_");
        return Integer.parseInt(split[1]);
    }

    /**
     * 构造动画层
     */
    private void setUpAnimLayout() {
        if (mAnimLayout == null) {
            mAnimLayout = new RelativeLayout(getContext());
            addView(mAnimLayout);
        }

    }
}
