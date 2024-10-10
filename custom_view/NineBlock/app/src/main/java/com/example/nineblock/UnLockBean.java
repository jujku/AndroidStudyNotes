package com.example.nineblock;

public class UnLockBean {
    private float x;
    private float y;
    private int index;

    private JiuGonGeUnLockView.Type type = JiuGonGeUnLockView.Type.ORIGIN;

    public UnLockBean(float x, float y, int index) {
        this.x = x;
        this.y = y;
        this.index = index;
    }

    public UnLockBean(float x, float y, int index, JiuGonGeUnLockView.Type type) {
        this.x = x;
        this.y = y;
        this.index = index;
        this.type = type;
    }

    public JiuGonGeUnLockView.Type getType() {
        return type;
    }

    public void setType(JiuGonGeUnLockView.Type type) {
        this.type = type;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public int getIndex() {
        return index;
    }

    public void setX(float x) {
        this.x = x;
    }

    public void setY(float y) {
        this.y = y;
    }

    public void setIndex(int index) {
        this.index = index;
    }

}
