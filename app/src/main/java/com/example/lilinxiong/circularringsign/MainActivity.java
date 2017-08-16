package com.example.lilinxiong.circularringsign;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import com.example.lilinxiong.circularringsign.bean.TextImageBean;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Context context = MainActivity.this;

    private TextSwitcher ts_hint_sign_text;//提醒签到，滚动
    private ImageSwitcher is_hint_sign_image;//对应的颜色滚动

    private ImageView iv_sign_one;
    private TextView tv_integral_one;

    private ImageView iv_sign_two;
    private TextView tv_integral_two;
    private TextView tv_two_image_center_one;

    private ImageView iv_sign_three;
    private TextView tv_integral_three;
    private TextView tv_two_image_center_two;

    private ImageView iv_sign_four;
    private TextView tv_integral_four;
    private TextView tv_two_image_center_three;

    private ImageView iv_sign_five;
    private TextView tv_integral_five;
    private TextView tv_two_image_center_four;

    private ImageView iv_sign_six;
    private TextView tv_integral_six;
    private TextView tv_two_image_center_five;

    private ImageView iv_sign_seven;
    private TextView tv_integral_seven;
    private TextView tv_two_image_center_six;

    private TextView tv_sign_below_hint;//签到下面的提醒

    private List<TextImageBean> textImageList;//签到和积分
    private List<TextView> textList;//图片和图片中间的内容
    //翻转动画
    private Animation back_anim;
    private Animation front_anim;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //绑定控件
        initView();
        //初始化监听
        initEvent();
        //初始数据
        initData();
        //动画初始化
        initAnim();
    }
    //动画初始化
    private void initAnim() {
        //左上角提示动画
        Animation in = AnimationUtils.loadAnimation(this, R.anim.in_animation);
        Animation out = AnimationUtils.loadAnimation(this, R.anim.out_animation);

        is_hint_sign_image.setInAnimation(in);
        is_hint_sign_image.setOutAnimation(out);

        ts_hint_sign_text.setInAnimation(in);
        ts_hint_sign_text.setOutAnimation(out);
        //左上角提示初始化
        ts_hint_sign_text.setText(getResources().getString(R.string.today_sign));
        is_hint_sign_image.setImageResource(textImageList.get(0).getColorID());

        //翻转动画
        back_anim = AnimationUtils.loadAnimation(this, R.anim.back_scale);
        front_anim = AnimationUtils.loadAnimation(this, R.anim.front_scale);
        //第一个图片
        doAnimateZoom(textImageList.get(0));
        int imageView = 1;
        int textView = 0;
        //周围比较大的动画(不包括第一一天的)
        for (int i = 1; i < textImageList.size(); i++) {
            doAnimateOpen(textImageList.get(i), i + (imageView++), 13, 260);
        }
        for (int i = 0; i < textList.size(); i++) {
            doAnimateOpen(textList.get(i), i + (++textView), 13, 260);
        }
        randomAnim();
    }

    private void randomAnim() {
        //随机值范围
        int min = 10;
        int max = 20;
        Random random = new Random();
        for (int i = 1; i < textImageList.size(); i++) {

            ImageView roleImageView = textImageList.get(i).getIv();
            //取随机值
            int x = random.nextInt(max) % (max - min + 1) + min;
            int y = random.nextInt(max) % (max - min + 1) + min;

            int originalX = textImageList.get(i).getMoveX();
            int originalY = textImageList.get(i).getMoveY();


            textImageList.get(i).setRandomX(x);
            textImageList.get(i).setRandomY(y);

            AnimatorSet set = new AnimatorSet();
            //设置图片的动画
            ObjectAnimator moveX = ObjectAnimator.ofFloat(roleImageView, "translationX",
                    originalX, originalX - x, originalX,//X坐标的运动轨迹为:原点，左，原点，右，原点
                    originalX + x, originalX);
            ObjectAnimator moveY = ObjectAnimator.ofFloat(roleImageView, "translationY",
                    originalY, originalY - y, originalY,//Y坐标的运动轨迹为:原点，上，原点，下，原点
                    originalY + y, originalY);

            //设置动画循环
            moveX.setRepeatCount(-1);
            moveY.setRepeatCount(-1);
            set.playTogether(moveX, moveY);
            set.setDuration(4000);
            textImageList.get(i).setRandomSet(set);
        }
        //利用线程制造时间差，实现周围圆的不同步
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    for (int i = 1; i < textImageList.size(); i++) {
                        Thread.sleep(500 / 7);
                        final int finalI = i;
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                //更新UI
                                AnimatorSet set = textImageList.get(finalI).getRandomSet();
                                set.start();
                            }
                        });
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        }).start();
    }

    //初始化数据
    private void initData() {
        textImageList = new ArrayList<>();
        textList = new ArrayList<>();
        //添加
        textImageList.add(new TextImageBean(tv_integral_one, iv_sign_one, R.mipmap.icon_test_2_checked));
        textImageList.add(new TextImageBean(tv_integral_two, iv_sign_two, R.mipmap.icon_test_1_checked));
        textImageList.add(new TextImageBean(tv_integral_three, iv_sign_three, R.mipmap.icon_test_5_checked));
        textImageList.add(new TextImageBean(tv_integral_four, iv_sign_four, R.mipmap.icon_test_4_checked));
        textImageList.add(new TextImageBean(tv_integral_five, iv_sign_five, R.mipmap.icon_test_7_checked));
        textImageList.add(new TextImageBean(tv_integral_six, iv_sign_six, R.mipmap.icon_test_8_checked));
        textImageList.add(new TextImageBean(tv_integral_seven, iv_sign_seven, R.mipmap.icon_test_3_checked));
        //第一个是可点击的
        textImageList.get(0).setClick(true);
        //添加
        textList.add(tv_two_image_center_one);
        textList.add(tv_two_image_center_two);
        textList.add(tv_two_image_center_three);
        textList.add(tv_two_image_center_four);
        textList.add(tv_two_image_center_five);
        textList.add(tv_two_image_center_six);
    }

    //初始化监听
    private void initEvent() {
        ts_hint_sign_text.setFactory(new ViewSwitcher.ViewFactory() {
            @Override
            public View makeView() {
                TextView tv = new TextView(context);
                tv.setTextSize(16);
                tv.setTextColor(Color.WHITE);
                return tv;
            }
        });
        is_hint_sign_image.setFactory(new ViewSwitcher.ViewFactory() {
            @Override
            public View makeView() {
                ImageView iv = new ImageView(context);
                iv.setMaxWidth(20);
                iv.setMaxHeight(20);
                return iv;
            }
        });
        //图片监听
        iv_sign_one.setOnClickListener(this);
        iv_sign_two.setOnClickListener(this);
        iv_sign_three.setOnClickListener(this);
        iv_sign_four.setOnClickListener(this);
        iv_sign_five.setOnClickListener(this);
        iv_sign_six.setOnClickListener(this);
        iv_sign_seven.setOnClickListener(this);
    }

    //绑定控件
    private void initView() {

        ts_hint_sign_text = (TextSwitcher) findViewById(R.id.ts_hint_sign_text);
        is_hint_sign_image = (ImageSwitcher) findViewById(R.id.is_hint_sign_image);

        iv_sign_one = (ImageView) findViewById(R.id.iv_sign_one);
        tv_integral_one = (TextView) findViewById(R.id.tv_integral_one);

        iv_sign_two = (ImageView) findViewById(R.id.iv_sign_two);
        tv_integral_two = (TextView) findViewById(R.id.tv_integral_two);
        tv_two_image_center_one = (TextView) findViewById(R.id.tv_two_image_center_one);

        iv_sign_three = (ImageView) findViewById(R.id.iv_sign_three);
        tv_integral_three = (TextView) findViewById(R.id.tv_integral_three);
        tv_two_image_center_two = (TextView) findViewById(R.id.tv_two_image_center_two);

        iv_sign_four = (ImageView) findViewById(R.id.iv_sign_four);
        tv_integral_four = (TextView) findViewById(R.id.tv_integral_four);
        tv_two_image_center_three = (TextView) findViewById(R.id.tv_two_image_center_three);

        iv_sign_five = (ImageView) findViewById(R.id.iv_sign_five);
        tv_integral_five = (TextView) findViewById(R.id.tv_integral_five);
        tv_two_image_center_four = (TextView) findViewById(R.id.tv_two_image_center_four);

        iv_sign_six = (ImageView) findViewById(R.id.iv_sign_six);
        tv_integral_six = (TextView) findViewById(R.id.tv_integral_six);
        tv_two_image_center_five = (TextView) findViewById(R.id.tv_two_image_center_five);

        iv_sign_seven = (ImageView) findViewById(R.id.iv_sign_seven);
        tv_integral_seven = (TextView) findViewById(R.id.tv_integral_seven);
        tv_two_image_center_six = (TextView) findViewById(R.id.tv_two_image_center_six);

        tv_sign_below_hint = (TextView) findViewById(R.id.tv_sign_below_hint);
    }

    //初始化动画
    private void doAnimateOpen(View view, int index, int total, int radius) {
        if (view.getVisibility() != View.VISIBLE) {
            view.setVisibility(View.VISIBLE);//将显示的模式改变为INVISIBLE
        }
        double degree = Math.toRadians(360) / (total - 1) * index;
        int translationX = -(int) (radius * Math.sin(degree));
        int translationY = -(int) (radius * Math.cos(degree));
        AnimatorSet set = new AnimatorSet();
        //包含平移、缩放和透明度动画
        set.playTogether(
                ObjectAnimator.ofFloat(view, "translationX", 0, translationX),
                ObjectAnimator.ofFloat(view, "translationY", 0, translationY),
                ObjectAnimator.ofFloat(view, "scaleX", 0f, 1f),
                ObjectAnimator.ofFloat(view, "scaleY", 0f, 1f),
                ObjectAnimator.ofFloat(view, "alpha", 0f, 1)
        );
        //动画周期为500ms
        set.setDuration(1000).start();
    }

    /**
     * 初始化时，第一天的就在中间，直接放大就好
     *
     * @param image
     */
    private void doAnimateZoom(TextImageBean image) {
        AnimatorSet set = new AnimatorSet();
        //包含缩放和透明度动画
        set.playTogether(
                ObjectAnimator.ofFloat(image.getIv(), "scaleX", 0f, 2f),
                ObjectAnimator.ofFloat(image.getIv(), "scaleY", 0f, 2f),
                ObjectAnimator.ofFloat(image.getIv(), "alpha", 0f, 1)
        );
        //动画周期为500ms
        set.setDuration(1000).start();
    }

    //初始化动画
    private void doAnimateOpen(TextImageBean view, int index, int total, int radius) {
        if (view.getIv().getVisibility() != View.VISIBLE) {
            view.getIv().setVisibility(View.VISIBLE);//ImageView显示
        }
        double degree = Math.toRadians(360) / (total - 1) * index;
        int translationX = -(int) (radius * Math.sin(degree));
        int translationY = -(int) (radius * Math.cos(degree));
        //存入属于有背景图的坐标的X，Y坐标
        view.setMoveX(translationX);
        view.setMoveY(translationY);
        AnimatorSet set = new AnimatorSet();
        //包含平移、缩放和透明度动画
        set.playTogether(
                ObjectAnimator.ofFloat(view.getIv(), "translationX", 0, translationX),
                ObjectAnimator.ofFloat(view.getIv(), "translationY", 0, translationY),
                ObjectAnimator.ofFloat(view.getIv(), "scaleX", 0f, 1f),
                ObjectAnimator.ofFloat(view.getIv(), "scaleY", 0f, 1f),
                ObjectAnimator.ofFloat(view.getIv(), "alpha", 0f, 1)
        );
        //动画周期为500ms
        set.setDuration(500).start();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_sign_one:
                if (textImageList.get(0).getClick()) {
                    //动画效果先取消
                    textImageList.get(1).getRandomSet().cancel();
                    //传入主次ImageView和图片显示中间的TextView
                    imageViewFlip(textImageList.get(0), textImageList.get(1), textList.get(1));

                    is_hint_sign_image.setImageResource(textImageList.get(1).getColorID());
                    ts_hint_sign_text.setText(getResources().getString(R.string.tomorrow_sign));

                    textImageList.get(0).setClick(false);
                    textImageList.get(1).setClick(true);
                }
                break;
            case R.id.iv_sign_two:
                if (textImageList.get(1).getClick()) {
                    //动画效果先取消
                    textImageList.get(2).getRandomSet().cancel();
                    //传入主次ImageView
                    imageViewFlip(textImageList.get(1), textImageList.get(2), textList.get(2));

                    is_hint_sign_image.setImageResource(textImageList.get(2).getColorID());
                    ts_hint_sign_text.setText(getResources().getString(R.string.tomorrow_sign));

                    textImageList.get(1).setClick(false);
                    textImageList.get(2).setClick(true);
                }
                break;
            case R.id.iv_sign_three:
                if (textImageList.get(2).getClick()) {
                    //动画效果先取消
                    textImageList.get(3).getRandomSet().cancel();
                    //传入主次ImageView
                    imageViewFlip(textImageList.get(2), textImageList.get(3), textList.get(3));

                    is_hint_sign_image.setImageResource(textImageList.get(3).getColorID());
                    ts_hint_sign_text.setText(getResources().getString(R.string.tomorrow_sign));

                    textImageList.get(2).setClick(false);
                    textImageList.get(3).setClick(true);
                }
                break;
            case R.id.iv_sign_four:
                if (textImageList.get(3).getClick()) {
                    //动画效果先取消
                    textImageList.get(4).getRandomSet().cancel();
                    //传入主次ImageView
                    imageViewFlip(textImageList.get(3), textImageList.get(4), textList.get(4));

                    is_hint_sign_image.setImageResource(textImageList.get(4).getColorID());
                    ts_hint_sign_text.setText(getResources().getString(R.string.tomorrow_sign));

                    textImageList.get(3).setClick(false);
                    textImageList.get(4).setClick(true);
                }
                break;
            case R.id.iv_sign_five:
                if (textImageList.get(4).getClick()) {
                    //动画效果先取消
                    textImageList.get(5).getRandomSet().cancel();
                    //传入主次ImageView
                    imageViewFlip(textImageList.get(4), textImageList.get(5), textList.get(5));

                    is_hint_sign_image.setImageResource(textImageList.get(5).getColorID());
                    ts_hint_sign_text.setText(getResources().getString(R.string.tomorrow_sign));

                    textImageList.get(4).setClick(false);
                    textImageList.get(5).setClick(true);
                }
                break;
            case R.id.iv_sign_six:
                if (textImageList.get(5).getClick()) {
                    //动画效果先取消
                    textImageList.get(6).getRandomSet().cancel();
                    //传入主次ImageView
                    imageViewFlip(textImageList.get(5), textImageList.get(6), textList.get(0));

                    is_hint_sign_image.setImageResource(textImageList.get(6).getColorID());
                    ts_hint_sign_text.setText(getResources().getString(R.string.tomorrow_sign));

                    textImageList.get(5).setClick(false);
                    textImageList.get(6).setClick(true);
                }
                break;
            case R.id.iv_sign_seven:
                if (textImageList.get(6).getClick()) {
                    //最后一个只翻盘
                    imageViewFlip(textImageList.get(6), null, null);
                    textImageList.get(6).setClick(false);
                }
                break;
            default:
                break;
        }
    }

    /**
     * 图片交换位置相关动画
     *
     * @param mainTextImageMap
     * @param minorTextImageMap
     */
    private void imageViewFlip(final TextImageBean mainTextImageMap, final TextImageBean minorTextImageMap,
                               final TextView centerTextView) {
        final ImageView MainImage = mainTextImageMap.getIv();
        //动画绑定
        MainImage.startAnimation(back_anim);
        //动画监听
        back_anim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                //翻转动画开始，签到下面的提示词，渐渐消失
                AnimatorSet set = new AnimatorSet();
                //包含缩放和透明度动画
                set.playTogether(
                        ObjectAnimator.ofFloat(tv_sign_below_hint, "scaleX", 1f, 0f),
                        ObjectAnimator.ofFloat(tv_sign_below_hint, "scaleY", 1f, 0f),
                        ObjectAnimator.ofFloat(tv_sign_below_hint, "alpha", 1f, 0)
                );
                //动画周期为500ms
                set.setDuration(500).start();
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                if (MainImage.getId() != iv_sign_seven.getId()) {
                    MainImage.setBackgroundResource(R.drawable.shape_circle_transparence);
                    MainImage.setImageResource(R.drawable.shape_circle_transparence);
                } else {
                    MainImage.setBackgroundResource(R.drawable.shape_circle_gray);
                    MainImage.setImageResource(R.drawable.shape_circle_gray);
                }
                MainImage.startAnimation(front_anim);

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        //翻转动画监听
        front_anim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                TextView tv_integral = mainTextImageMap.getTv();
                tv_integral.setVisibility(View.VISIBLE);
                tv_integral.setText("100积分");
                if (mainTextImageMap.getIv().getId() != iv_sign_seven.getId()) {
                    //如果不是最后一个则，去交换位置
                    doAnimateMoveSwapPlaces(mainTextImageMap, minorTextImageMap, centerTextView);
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    /**
     * 交换位置
     *
     * @param mainTextImageMap
     * @param minorTextImageMap
     */
    private void doAnimateMoveSwapPlaces(TextImageBean mainTextImageMap, TextImageBean minorTextImageMap,
                                         TextView centerView) {
        final ImageView MainImage = mainTextImageMap.getIv();
        final ImageView MinorImage = minorTextImageMap.getIv();

        final TextView MainText = mainTextImageMap.getTv();

        final int moveX = minorTextImageMap.getMoveX();
        final int moveY = minorTextImageMap.getMoveY();

        final int RandomX = minorTextImageMap.getRandomX();
        final int RandomY = minorTextImageMap.getRandomY();

        final AnimatorSet set = new AnimatorSet();

//        ObjectAnimator anim_scale_rounde_X = new ObjectAnimator();
//        ObjectAnimator anim_scale_rounde_Y = new ObjectAnimator();
        //缩小，平移到周围
        ObjectAnimator anim_roundeX = ObjectAnimator.ofFloat(MainImage, "translationX", 0, moveX);
        ObjectAnimator anim_roundeY = ObjectAnimator.ofFloat(MainImage, "translationY", 0, moveY);
        ObjectAnimator anim_scale_rounde_X = ObjectAnimator.ofFloat(MainImage, "scaleX", 2f, 1f);
        ObjectAnimator anim_scale_rounde_Y = ObjectAnimator.ofFloat(MainImage, "scaleY", 2f, 1f);
        //平移TextView
        ObjectAnimator anim_move_textview_X = ObjectAnimator.ofFloat(MainText, "translationX", 0, moveX);
        ObjectAnimator anim_move_textview_Y = ObjectAnimator.ofFloat(MainText, "translationY", 0, moveY);
        //放大，平移到中间
        ObjectAnimator anim_centerX = ObjectAnimator.ofFloat(MinorImage, "translationX", moveX, 0);
        ObjectAnimator anim_centerY = ObjectAnimator.ofFloat(MinorImage, "translationY", moveY, 0);
        ObjectAnimator anim_scale_center_X = ObjectAnimator.ofFloat(MinorImage, "scaleX", 1f, 2f);
        ObjectAnimator anim_scale_center_Y = ObjectAnimator.ofFloat(MinorImage, "scaleY", 1f, 2f);
        //签到下方的提示词，显示
        tv_sign_below_hint.setText(getResources().getString(R.string.tomorrow_sign));
        ObjectAnimator anim_sign_hint_tv_scale_X = ObjectAnimator.ofFloat(tv_sign_below_hint, "scaleX", 0f, 1f);
        ObjectAnimator anim_sign_hint_tv_scale_Y = ObjectAnimator.ofFloat(tv_sign_below_hint, "scaleY", 0f, 1f);
        ObjectAnimator anim_sign_hint_tv_alpha = ObjectAnimator.ofFloat(tv_sign_below_hint, "alpha", 0f, 1f);
        //两个图片中间的TextView
        centerView.setText("100分");
        ObjectAnimator anim_tv_center_scale_X = ObjectAnimator.ofFloat(centerView, "scaleX", 0f, 1f);
        ObjectAnimator anim_tv_center_scale_Y = ObjectAnimator.ofFloat(centerView, "scaleY", 0f, 1f);
        ObjectAnimator anim_tv_center_scale_alpha = ObjectAnimator.ofFloat(centerView, "alpha", 0f, 1f);

        //动画同时启动
        set.playTogether(
                anim_roundeX, anim_roundeY, anim_scale_rounde_X, anim_scale_rounde_Y,//中间的大圆
                anim_move_textview_X, anim_move_textview_Y,//要移动的TextView
                anim_centerX, anim_centerY, anim_scale_center_X, anim_scale_center_Y,//周围的小圆
                anim_sign_hint_tv_scale_X, anim_sign_hint_tv_scale_Y, anim_sign_hint_tv_alpha,//签到下方提示词，跟随交换动画，一起启动
                anim_tv_center_scale_X, anim_tv_center_scale_Y, anim_tv_center_scale_alpha//两个图片中间，显示
        );
        //设置动画完成的时间
        set.setDuration(2000);
        //启动动画
        set.start();

        anim_centerX.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                //更换显示的图片和从中间到周围去的大圆的背景
                MinorImage.setImageResource(R.mipmap.zhiwen);
                MainImage.setBackgroundResource(R.drawable.shape_circle_gray);
                AnimatorSet set = new AnimatorSet();
                //ImageView
                ObjectAnimator Image_moveX = ObjectAnimator.ofFloat(MainImage, "translationX",
                        moveX - RandomX, moveX, moveX + RandomX, moveX, moveX - RandomX);
                ObjectAnimator Image_moveY = ObjectAnimator.ofFloat(MainImage, "translationY",
                        moveY - RandomY, moveY, moveY + RandomY, moveY, moveY - RandomY);
                //TextView
                ObjectAnimator text_moveX = ObjectAnimator.ofFloat(MainText, "translationX",
                        moveX - RandomX, moveX, moveX + RandomX, moveX, moveX - RandomX);
                ObjectAnimator text_moveY = ObjectAnimator.ofFloat(MainText, "translationY",
                        moveY - RandomY, moveY, moveY + RandomY, moveY, moveY - RandomY);
                //设置动画循环
                Image_moveX.setRepeatCount(-1);
                Image_moveY.setRepeatCount(-1);
                text_moveX.setRepeatCount(-1);
                text_moveY.setRepeatCount(-1);
                set.playTogether(
                        Image_moveX, Image_moveY,
                        text_moveX, text_moveY
                );
                set.setDuration(4000).start();
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
    }
}
