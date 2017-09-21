package com.yisipu.chartmap.surfaceView;

import android.graphics.Bitmap;

import com.yisipu.chartmap.bean.ShipBean;

public class MarkObject {
	private double chuanshou=0;
	private Bitmap mBitmap;
	private float mapX;
	private float mapY;
	private MarkClickListener listener;
	/*
	 是否是我的船只
	 */
    private boolean isMyShip=false;
	public MarkObject() {

	}

	public MarkObject(Bitmap mBitmap, float mapX, float mapY, boolean isMyShip) {
		super();
		this.mBitmap = mBitmap;
		this.mapX = mapX;
		this.mapY = mapY;
		this.isMyShip=isMyShip;
	}

	/**
	 * @return the mBitmap
	 */
	public Bitmap getmBitmap() {
		return mBitmap;
	}

	/**
	 * @param mBitmap
	 *            the mBitmap to set
	 */
	public void setmBitmap(Bitmap mBitmap) {
		this.mBitmap = mBitmap;
	}

	/**
	 * @return the mapX
	 */
	public float getMapX() {
		return mapX;
	}

	/**
	 * @param mapX
	 *            the mapX to set
	 */
	public void setMapX(float mapX) {
		this.mapX = mapX;
	}

	/**
	 * @return the mapY
	 */
	public float getMapY() {
		return mapY;
	}

	/**
	 * @param mapY
	 *            the mapY to set
	 */
	public void setMapY(float mapY) {
		this.mapY = mapY;
	}
	
	public MarkClickListener getMarkListener() {
		return listener;
	}

	public void setMarkListener(MarkClickListener listener) {
		this.listener = listener;
	}

	public boolean isMyShip() {
		return isMyShip;
	}

	public void setMyShip(boolean myShip) {
		isMyShip = myShip;
	}

	public double getChuanshou() {
		return chuanshou;
	}

	public void setChuanshou(double chuanshou) {
		this.chuanshou = chuanshou;
	}

	public interface MarkClickListener {
		public void onMarkClick(int x, int y);
	}

}
