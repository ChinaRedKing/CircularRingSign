package com.example.lilinxiong.circularringsign.bean;

import android.animation.AnimatorSet;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * 项目名：  CircularRingSign
 * 包名：    com.example.lilinxiong.circularringsign.bean
 * 文件名:   TextImageMap
 * 创建者:   LLX
 * 创建时间:  2017/8/16 18:42
 * 描述：
 */
public class TextImageBean {
    private TextView tv;
    private ImageView iv;
    //移动的X,Y坐标
    private Integer moveX;
    private Integer moveY;
    //随机生成的x,y
    private Integer randomX;
    private Integer randomY;
    //动画
    private AnimatorSet randomSet;
    //当前的图片是否可点击，默认为false
    private Boolean isClick = false;
    //对应的颜色
    private Integer colorID;

    public TextImageBean(TextView tv, ImageView iv, Integer colorID) {
        this.tv = tv;
        this.iv = iv;
        this.colorID = colorID;
    }

    public Integer getMoveX() {
        return moveX;
    }

    public void setMoveX(Integer moveX) {
        this.moveX = moveX;
    }

    public Integer getMoveY() {
        return moveY;
    }

    public void setMoveY(Integer moveY) {
        this.moveY = moveY;
    }

    public Integer getRandomX() {
        return randomX;
    }

    public void setRandomX(Integer randomX) {
        this.randomX = randomX;
    }

    public Integer getRandomY() {
        return randomY;
    }

    public void setRandomY(Integer randomY) {
        this.randomY = randomY;
    }


    public TextView getTv() {
        return tv;
    }

    public void setTv(TextView tv) {
        this.tv = tv;
    }

    public ImageView getIv() {
        return iv;
    }

    public void setIv(ImageView iv) {
        this.iv = iv;
    }

    public AnimatorSet getRandomSet() {
        return randomSet;
    }

    public void setRandomSet(AnimatorSet randomSet) {
        this.randomSet = randomSet;
    }

    public Boolean getClick() {
        return isClick;
    }

    public void setClick(Boolean click) {
        isClick = click;
    }

    public Integer getColorID() {
        return colorID;
    }
}
