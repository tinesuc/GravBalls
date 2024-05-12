package com.slymey.gravballs;

import android.graphics.Color;

import java.util.Arrays;
import java.util.stream.IntStream;

public class Ball {
    public float x=200;
    public float y=200;
    public float dx=5;
    public float dy=5;
    public float r = 20;
    public float m = 20;
    public static float g = 0.0001f;
    public static int w;
    public static int h;
    public static float gravity0[][];
    public static boolean grav0 = false;
    public static boolean gravity=false;
    public static boolean wrap=false;
    public static float d = 0.002f;
    public int color;

    public Ball(float x, float y, float dx, float dy, float r, float m) {
        this.x=x;
        this.y=y;
        this.dx=dx;
        this.dy=dy;
        this.r=r;
        this.m=m;
        color = Color.HSVToColor(new float[]{13,14,14});
    }

    public Ball setColor(float h, float s, float v) {
        color = Color.HSVToColor(new float[]{h,s,v});
        return this;
    }
    public Ball setHue(float h) {
        color = Color.HSVToColor(new float[]{h,100,100});
        return this;
    }
    public void applyForce(float fx, float fy, float f) {
        float r = (float) Math.sqrt((x-fx) * (x-fx) + (y-fy) * (y-fy));
        if(r<10)return;
        float phi = (float) Math.atan2(y-fy, x-fx);
        dx -= (float) (f * Math.cos(phi) / r / m);
        dy -= (float) (f * Math.sin(phi) / r / m);
    }
    static int efr = 3;
    public void applyGravity() {
        float fsumx=0;
        float fsumy=0;
        for(int i=-efr;i<=efr;i++){
            if(x+i<0||x+i>=w)continue;
            for(int j=-efr;j<=efr;j++){
                if(y+j<0||y+j>=h)continue;
                if(i==0&&j==0)continue;
                float d= (float)Math.sqrt(i*i+j*j);
                float phi = (float) Math.atan2(j, i);
                float vp = gravity0[(int) (x+i)][(int) (y+j)];
                vp -= m/d;
                //System.out.println(d+" "+vp+" "+phi);
                if(Float.isNaN(d))throw new AssertionError("Nan");
                if(d==0)throw new AssertionError("d=0");
                fsumx += (float) (Math.cos(phi)*vp/d);
                fsumy += (float) (Math.sin(phi)*vp/d);
            }
        }
        //System.out.println("fsum: "+fsumx+","+fsumy+" "+m);
        if(Float.isNaN(fsumx)||Float.isNaN(fsumy))throw new AssertionError("NaN");
        dx += fsumx/m;
        dy += fsumy/m;
    }
    static int psr = 4;
    public static void generateGrav0(int tx, int ty, int x, int y, float m){
        for(int i=-psr;i<=psr;i++){
            if(tx+i<0||tx+i>=w)continue;
            for(int j=-psr;j<=psr;j++){
                if(ty+j<0||ty+j>=h)continue;
                if(i==0 && j==0)gravity0[tx+i][ty+j]= (float) Math.max(gravity0[tx+i][ty+j]-m,0);
                gravity0[tx+i][ty+j]= (float) Math.max(gravity0[tx+i][ty+j]-(m/Math.sqrt(i*i+j*j)),0);
            }
        }
        for(int i=-10;i<=10;i++){
            if(x+i<0||x+i>=w)continue;
            for(int j=-10;j<=10;j++){
                if(y+j<0||y+j>=h)continue;
                if(i==0 && j==0)gravity0[x+i][y+j]+=m;
                gravity0[x+i][y+j]+= (float) (m/Math.sqrt(i*i+j*j));
            }
        }
    }
    public void move(float w, float h, Ball balls[]) {
        if(gravity){
            int tx= (int) x;
            int ty= (int) y;
            this.x += dx;
            this.y += dy;
            generateGrav0(tx, ty, (int) this.x, (int) this.y, m);
        }else{
            this.x += dx;
            this.y += dy;
        }
        float dc = (r/m)*d;
        this.dx = dx*(1-dc);
        this.dy = dy*(1-dc);
        //Do we need to bounce next time?
        if (Float.isInfinite(dx))dx=0;
        if (Float.isInfinite(dy))dy=0;

        if(!wrap) {
            /*dx %= w;
            dy %= h;*/
            if(x<r){
                x=r;
            }
            if(x>w-r){
                x=w-r;
            }
            if(y<r){
                y=r;
            }
            if(y>h-r){
                y=h-r;
            }
            if (this.x + dx - r < 0 || this.x + dx + r > w) {
                dx = -dx;
            }
            if (this.y + dy - r < 0 || this.y + dy + r > h) {
                dy = -dy;
            }
        }else{
            x=(x%w+w)%w;
            y=(y%h+h)%h;
            /*dy %= (h*1.0f);
            dx %= (w*1.0f);*/
        }
    }
    public static void clearGrav0(){
        IntStream.range(0, gravity0.length).forEach(i -> Arrays.fill(gravity0[i], 0));
        grav0=true;
    }

}
