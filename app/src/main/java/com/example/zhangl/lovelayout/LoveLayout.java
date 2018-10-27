package com.example.zhangl.lovelayout;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.PointF;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import java.util.Random;

public class LoveLayout extends RelativeLayout{


    private Random mRandom;

    private int[] mImageRes;

    private int mWidth,mheight;
    private int ImageWidth,ImageHeight;

    public LoveLayout(Context context) {
        this(context,null);
    }

    public LoveLayout(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public LoveLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        mRandom = new Random();
        mImageRes = new int[]{R.drawable.pl_blue,R.drawable.pl_red,R.drawable.pl_yellow};

    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        mWidth = MeasureSpec.getSize(widthMeasureSpec);
        mheight = MeasureSpec.getSize(heightMeasureSpec);

        Drawable drawable = ContextCompat.getDrawable(getContext(),R.drawable.pl_blue);
        ImageHeight = drawable.getIntrinsicHeight();
        ImageWidth = drawable.getIntrinsicWidth();
    }

    public void addLove(){

        final ImageView loveIv = new ImageView(getContext());


        loveIv.setImageResource(mImageRes[mRandom.nextInt(mImageRes.length -1)]);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);

        params.addRule(ALIGN_PARENT_BOTTOM);
        params.addRule(CENTER_HORIZONTAL);
        loveIv.setLayoutParams(params);
        addView(loveIv);

        AnimatorSet animator = getAnimator(loveIv);
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                // 执行完毕之后移除
                removeView(loveIv);
            }
        });
        animator.start();

    }

    private AnimatorSet getAnimator(ImageView iv) {
        AnimatorSet allSet = new AnimatorSet();
        AnimatorSet innerAnimator = new AnimatorSet();


        ObjectAnimator alphaA = ObjectAnimator.ofFloat(iv,"alpha",0.3f,1.0f);
        ObjectAnimator scaleX = ObjectAnimator.ofFloat(iv,"scaleX",0.3f,1.0f);
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(iv,"scaleY",0.3f,1.0f);
        innerAnimator.playTogether(alphaA,scaleX,scaleY);
        innerAnimator.setDuration(350);




        allSet.playSequentially(innerAnimator,getBezierAnimator(iv));


        return allSet;

    }

    private Animator getBezierAnimator(final ImageView iv) {

        PointF point0 = new PointF(mWidth/2 - ImageHeight/2,mheight);

        // 确保p1点的Y值  一定要大于p1点的y值
        PointF point1 = new PointF(mRandom.nextInt(mWidth),mRandom.nextInt(mheight/2));
        PointF point2 = new PointF(mRandom.nextInt(mWidth),mRandom.nextInt(mheight/2) + mheight/2);


        PointF point3 = new PointF(mRandom.nextInt(mWidth),0);

        LoveTypeEvaluator typeEvaluator = new LoveTypeEvaluator(point1,point2);

        ValueAnimator bezierAnimtor = ObjectAnimator.ofObject(typeEvaluator,point0,point3);
        bezierAnimtor.setDuration(10000);

        bezierAnimtor.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                PointF pointF = (PointF) animation.getAnimatedValue();
                iv.setX(pointF.x);
                iv.setY(pointF.y);
                // 透明度
                float t = animation.getAnimatedFraction();
                iv.setAlpha(1 - t + 0.2f);
            }
        });
        return bezierAnimtor;
    }


}
