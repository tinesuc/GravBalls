package com.slymey.gravballs;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.View;
import android.widget.SeekBar;
import android.widget.Switch;

public class BallViewPane extends View {
    Paint paint;
    Ball balls[];
    private Handler looper;

    private float w;
    private float h;
    private float fx;
    private float fy;
    private static float f = 140;
    public static Switch wrap_ell;
    public static Switch grav_ell;
    public static SeekBar force_ell;
    public static SeekBar drag_ell;
    private boolean touching=false;
    private float rand(float min, float max){
        return (float) (Math.random()*(max-min)+min);
    }
    public BallViewPane(Context context, AttributeSet attr) {
        super(context);
        looper = new Handler();
        this.setOnTouchListener((v, event) -> {
            touching = true;
            fx = event.getX();
            fy = event.getY();
            return true;
        });
    }
    public void init(Canvas canvas){
        paint = new Paint();
        paint.setStyle(Paint.Style.FILL);
        w=canvas.getWidth();
        Ball.w= (int) w;
        h=canvas.getHeight();
        Ball.h= (int) h;
        Ball.gravity0 = new float[Ball.w][Ball.h];
        balls = new Ball[150];
        for(int i=0;i<balls.length;i++){
            balls[i] = new Ball(
                    rand(0, canvas.getWidth()),
                    rand(0, canvas.getHeight()),
                    rand(-8, 8),
                    rand(-8, 8),
                    rand(5, 10),
                    rand(4, 40)
            );
            balls[i].setHue(rand(0,360));
        }


    }

    public void onDraw(Canvas canvas){
        super.onDraw(canvas);
        canvas.drawColor(Color.TRANSPARENT);
        if(balls == null)init(canvas);
        for(int i=0;i<balls.length;i++){
            paint.setColor(balls[i].color);
            canvas.drawCircle(balls[i].x,balls[i].y,balls[i].r,paint);
        }


        looper.postDelayed(this::move, 10);
        looper.postDelayed(Ball::clearGrav0, 10000);
        invalidate();
        //System.out.println("in innit"+x+" "+y);
    }
    public void move(){
        if(force_ell!=(null))f = force_ell.getProgress()+10;
        //System.out.println("bvpm: "+(float) (Math.pow(drag_ell.getProgress()/1000.0f, 2)/10.0));
        if(wrap_ell!=(null)) Ball.wrap = wrap_ell.isChecked();
        if(grav_ell!=(null)) Ball.gravity = grav_ell.isChecked();
        if(drag_ell!=(null)) Ball.d = (float) (Math.pow(drag_ell.getProgress()/1000.0f, 2)/10.0);
        for(int i=0;i<balls.length;i++){
            balls[i].move(w, h, balls);
            //System.out.println(balls[i].dx+" "+balls[i].dy);
            if(Float.isNaN(balls[i].dx))throw new AssertionError("NaN");
            if(touching)
                balls[i].applyForce(fx, fy, f*20);
            if(Ball.gravity)
                balls[i].applyGravity();
        }
        touching=false;
    }

}