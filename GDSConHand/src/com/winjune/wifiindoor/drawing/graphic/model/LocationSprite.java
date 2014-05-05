package com.winjune.wifiindoor.drawing.graphic.model;

import org.andengine.entity.sprite.TiledSprite;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.opengl.texture.region.TiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.debug.Debug;

public class LocationSprite extends TiledSprite {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	private final int mStateCount;
	private OnClickListener mOnClickListener;

	private State mState;

	// ===========================================================
	// Constructors
	// ===========================================================

	public LocationSprite(final float pX, final float pY, final ITextureRegion pNormalTextureRegion, final VertexBufferObjectManager pVertexBufferObjectManager) {
		this(pX, pY, pNormalTextureRegion, pVertexBufferObjectManager, (OnClickListener) null);
	}

	public LocationSprite(final float pX, final float pY, final ITextureRegion pNormalTextureRegion, final VertexBufferObjectManager pVertexBufferObjectManager, final OnClickListener pOnClickListener) {
		this(pX, pY, new TiledTextureRegion(pNormalTextureRegion.getTexture(), pNormalTextureRegion), pVertexBufferObjectManager, pOnClickListener);
	}

	public LocationSprite(final float pX, final float pY, final ITextureRegion pNormalTextureRegion, final ITextureRegion pPressedTextureRegion, final VertexBufferObjectManager pVertexBufferObjectManager) {
		this(pX, pY, pNormalTextureRegion, pPressedTextureRegion, pVertexBufferObjectManager, (OnClickListener) null);
	}

	public LocationSprite(final float pX, final float pY, final ITextureRegion pNormalTextureRegion, final ITextureRegion pPressedTextureRegion, final VertexBufferObjectManager pVertexBufferObjectManager, final OnClickListener pOnClickListener) {
		this(pX, pY, new TiledTextureRegion(pNormalTextureRegion.getTexture(), pNormalTextureRegion, pPressedTextureRegion), pVertexBufferObjectManager, pOnClickListener);
	}

	public LocationSprite(final ITextureRegion pNormalTextureRegion, final ITextureRegion pPressedTextureRegion, final VertexBufferObjectManager pVertexBufferObjectManager) {
		this(0, 0, new TiledTextureRegion(pNormalTextureRegion.getTexture(), pNormalTextureRegion, pPressedTextureRegion), pVertexBufferObjectManager, (OnClickListener)null);
	}	
	
	public LocationSprite(final float pX, final float pY, final ITiledTextureRegion pTiledTextureRegion, final VertexBufferObjectManager pVertexBufferObjectManager) {
		this(pX, pY, pTiledTextureRegion, pVertexBufferObjectManager, (OnClickListener) null);
	}

	public LocationSprite(final float pX, final float pY, final ITiledTextureRegion pTiledTextureRegion, final VertexBufferObjectManager pVertexBufferObjectManager, final OnClickListener pOnClickListener) {
		super(pX, pY, pTiledTextureRegion, pVertexBufferObjectManager);

		this.mOnClickListener = pOnClickListener;
		this.mStateCount = pTiledTextureRegion.getTileCount();

		this.changeState(State.NORMAL);
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	public State getState() {
		return this.mState;
	}

	public void setOnClickListener(final OnClickListener pOnClickListener) {
		this.mOnClickListener = pOnClickListener;
	}
	
	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	@Override
	public boolean onAreaTouched(final TouchEvent pSceneTouchEvent, final float pTouchAreaLocalX, final float pTouchAreaLocalY) {
		if(pSceneTouchEvent.isActionUp() && this.mState == State.NORMAL) {
			this.changeState(State.FOCUSED);
		}

		if(this.mOnClickListener != null) {
				this.mOnClickListener.onClick(this, pTouchAreaLocalX, pTouchAreaLocalY);
		}
		
		return true;
	}

	@Override
	public boolean contains(final float pX, final float pY) {
		if(!this.isVisible()) {
			return false;
		} else {
			return super.contains(pX, pY);
		}
	}

	// ===========================================================
	// Methods
	// ===========================================================

	public void changeState(final State pState) {
		if(pState == this.mState) {
			return;
		}

		this.mState = pState;

		final int stateTiledTextureRegionIndex = this.mState.getTiledTextureRegionIndex();
		if(stateTiledTextureRegionIndex >= this.mStateCount) {
			this.setCurrentTileIndex(0);
			Debug.w(this.getClass().getSimpleName() + " changed its " + State.class.getSimpleName() + " to " + pState.toString() + ", which doesn't have a " + ITextureRegion.class.getSimpleName() + " supplied. Applying default " + ITextureRegion.class.getSimpleName() + ".");
		} else {
			this.setCurrentTileIndex(stateTiledTextureRegionIndex);
		}
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================

	public interface OnClickListener {
		// ===========================================================
		// Constants
		// ===========================================================

		// ===========================================================
		// Methods
		// ===========================================================

		public void onClick(final LocationSprite pButtonSprite, final float pTouchAreaLocalX, final float pTouchAreaLocalY);
	}

	public static enum State {
		// ===========================================================
		// Elements
		// ===========================================================

		NORMAL(0),
		FOCUSED(1);

		// ===========================================================
		// Constants
		// ===========================================================

		// ===========================================================
		// Fields
		// ===========================================================

		private final int mTiledTextureRegionIndex;

		// ===========================================================
		// Constructors
		// ===========================================================

		private State(final int pTiledTextureRegionIndex) {
			this.mTiledTextureRegionIndex = pTiledTextureRegionIndex;
		}

		// ===========================================================
		// Getter & Setter
		// ===========================================================

		public int getTiledTextureRegionIndex() {
			return this.mTiledTextureRegionIndex;
		}

		// ===========================================================
		// Methods for/from SuperClass/Interfaces
		// ===========================================================

		// ===========================================================
		// Methods
		// ===========================================================

		// ===========================================================
		// Inner and Anonymous Classes
		// ===========================================================
	}
}
