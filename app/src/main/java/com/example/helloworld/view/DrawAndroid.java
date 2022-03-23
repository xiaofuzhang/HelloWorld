package com.example.helloworld.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Build;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;

public class DrawAndroid extends View {
    private String name;
    public DrawAndroid(Context context,String name) {
        super(context);
        this.name = "<< "+name+" >>";
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Paint p = new Paint();
        p.setAntiAlias(true);//抗锯齿
        p.setColor(0xFFFF6600);
//        p.setStyle(Paint.Style.STROKE);
        p.setStyle(Paint.Style.FILL);

        //X偏移量
        final float OFFSET_X = 350;
        //Y偏移量
        final float OFFSET_Y = 150;
        //圆角矩形半径
        final float RECT_RADIUS = 75;

        //头
        RectF rectf = new RectF();
        rectf.left = 0;
        rectf.right = 440;
        rectf.top = 0;
        rectf.bottom = 440;
        rectf.offset(OFFSET_X,OFFSET_Y);

//        canvas.drawRect(rectf,p);

        //画弧线，第一个是个矩形，弧线是在矩形里面画出来的，
        //第二个是开始角度，默认0度是3点钟方向,-90度是正上方12点钟方向，作画方向为顺时针
        //第三个是弧线的长短，180就是半个园，360就是整圆，扫描角度
        //第四个是useCenter，绘制的时候是否连线到圆心，如果是，弧线的2端会连线到圆心，这时就会看到是一个扇形。
        // 否则就会把弧线2个端点用直线连接。
        // 注意，只有使用p.setStyle(Paint.Style.FILL);才会看出效果，且半圆和整圆不能看出效果
        canvas.drawArc(rectf,0,-180,true,p);

        //负数取模数，r = a - (a / b) x b
        Log.i("zxf","-1000%360:"+(-1000%360));//-280
        Log.i("zxf","-180%360:"+(-180%360));//-180

        //画眼睛
        float leftEyeX = rectf.width()*1/4+OFFSET_X;//左眼X起始坐标
        float rightEyeX = rectf.width()*3/4+OFFSET_X;//右眼X起始坐标
        float eyeY = rectf.height()/3+OFFSET_Y;//y坐标加上偏移量
        float eyeRadius = 20;//眼睛大小
        p.setColor(Color.BLACK);//眼睛颜色
        canvas.drawCircle(leftEyeX,eyeY,eyeRadius,p);
        canvas.drawCircle(rightEyeX,eyeY,eyeRadius,p);
        Log.i("zxf","eye:"+leftEyeX+" "+rightEyeX+" "+eyeY
                +" "+rectf.width()+" "+rectf.height());

        //画天线
        p.setStrokeWidth(20);
        p.setColor(0xFFFF6600);//重新将画笔上色
        p.setStrokeCap(Paint.Cap.ROUND);//画笔赋予圆角技能，画直线有圆角效果

        float lineY = rectf.height()/10+OFFSET_Y;//y坐标加上偏移量,天线起始坐标
        float finishlineY = 0;//天线上部端点y坐标,决定天线的长度
        if(OFFSET_Y>rectf.height()/2){
            finishlineY = OFFSET_Y-rectf.height()/4;
        }else{
            finishlineY = OFFSET_Y*2/3;
        }

        float leftFinishLineX = OFFSET_X;//左边天线上部端点X坐标,决定天线的角度
        //左边天线
        canvas.drawLine(leftEyeX,lineY,leftFinishLineX,finishlineY,p);
        Log.i("zxf","line1:"+leftEyeX+" "+lineY+" "+leftFinishLineX+" "+finishlineY);

        float rightFinishLineX = OFFSET_X+rectf.width();//右边天线上部端点X坐标,决定天线的角度
        //右边天线
        canvas.drawLine(rightEyeX,lineY,rightFinishLineX,finishlineY,p);

        //绘制身体
        Rect bodyRect = new Rect();
        bodyRect.left = (int)rectf.left;
        bodyRect.right = (int)rectf.right;
        bodyRect.top = (int)(rectf.top+rectf.height()/2+20);//身体Y轴的偏移量
        bodyRect.bottom = bodyRect.top+440;//身体Y轴的偏移量
        Log.i("zxf","body:"+bodyRect.left+" "+bodyRect.right+" "+bodyRect.top+" "+bodyRect.bottom);
        canvas.drawRect(bodyRect,p);//上半部分

        RectF bRectf = new RectF();
        bRectf.left = bodyRect.left;
        bRectf.right = bodyRect.right;
        bRectf.top = bodyRect.top+bodyRect.height()*4/5;
        bRectf.bottom = bodyRect.bottom+bodyRect.height()*1/5;

        canvas.drawRoundRect(bRectf,RECT_RADIUS,RECT_RADIUS,p);//下半部分
        Log.i("zxf","bodyF:"+bRectf.left+" "+bRectf.right+" "+bRectf.top+" "+bRectf.bottom);

        //绘制手臂
        RectF armLeft = new RectF();
        armLeft.left = bodyRect.left - bodyRect.width()/4 - 20;
        armLeft.right = armLeft.left+bodyRect.width()/4;
        armLeft.top = bodyRect.top;
        armLeft.bottom = bRectf.top;
        canvas.drawRoundRect(armLeft,RECT_RADIUS,RECT_RADIUS,p);//左手臂
        Log.i("zxf","armLeft:"+armLeft.left+" "+armLeft.right+" "+armLeft.top+" "+armLeft.bottom);

        RectF armRight = new RectF();
        armRight.left = bodyRect.right + bodyRect.width()/4 + 20;
        armRight.right = armRight.left-bodyRect.width()/4;
        armRight.top = armLeft.top;
        armRight.bottom = armLeft.bottom;
        canvas.drawRoundRect(armRight,RECT_RADIUS,RECT_RADIUS,p);//右手臂
        Log.i("zxf","armRight:"+armRight.left+" "+armRight.right+" "+armRight.top+" "+armRight.bottom);

        //绘制脚
        RectF footLeft = new RectF();
        footLeft.left = leftEyeX - armLeft.width()/2;
        footLeft.right = footLeft.left+armLeft.width();
        footLeft.top = bRectf.top;
        footLeft.bottom = footLeft.top+armLeft.height();
        canvas.drawRoundRect(footLeft,RECT_RADIUS,RECT_RADIUS,p);//左脚
        Log.i("zxf","footLeft:"+footLeft.left+" "+footLeft.right+" "+footLeft.top+" "+footLeft.bottom);

        RectF footRight = new RectF();
        footRight.left = rightEyeX - armLeft.width()/2;
        footRight.right = footRight.left+armLeft.width();
        footRight.top = footLeft.top;
        footRight.bottom = footLeft.bottom;
        canvas.drawRoundRect(footRight,RECT_RADIUS,RECT_RADIUS,p);//右脚
        Log.i("zxf","footRight:"+footRight.left+" "+footRight.right+" "+footRight.top+" "+footRight.bottom);


        //写名字

        TextPaint textPaint = new TextPaint();
        textPaint.setARGB(0xFF, 0xFF, 66, 00);
        //转换PX到SP
        int spSize = 20;
        float scaledSizeInPixels = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,
                spSize, getResources().getDisplayMetrics());
        textPaint.setTextSize(scaledSizeInPixels);
        textPaint.setAntiAlias(true);

        canvas.drawText("我的名字叫",OFFSET_X,footRight.bottom+OFFSET_Y,textPaint);
        Paint.FontMetrics fm = textPaint.getFontMetrics();
        float offsetTextLineHeight = fm.descent+Math.abs(fm.ascent) + fm.leading;//绘制字符行高
        Log.i("zxf","offsetText:"+fm.ascent+" "+fm.descent+" "+fm.leading+" "+offsetTextLineHeight);

        StaticLayout layout = new StaticLayout(name, textPaint, 500, Layout.Alignment.ALIGN_CENTER,
                1.0F, 0.0F, true);

        canvas.save();
        canvas.translate(OFFSET_X, footRight.bottom+OFFSET_Y);//从脚下加Y轴偏移量开始画
        layout.draw(canvas);
        canvas.restore();
    }
}
