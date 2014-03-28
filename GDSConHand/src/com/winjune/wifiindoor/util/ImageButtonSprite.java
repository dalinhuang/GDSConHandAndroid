package com.winjune.wifiindoor.util;

import java.io.IOException;
import java.io.InputStream;

import org.andengine.entity.sprite.ButtonSprite;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.sprite.ButtonSprite.OnClickListener;
import org.andengine.entity.text.Text;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.font.Font;
import org.andengine.opengl.texture.bitmap.BitmapTexture;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.opengl.texture.region.TextureRegionFactory;
import org.andengine.opengl.texture.region.TiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.adt.io.in.IInputStreamOpener;
import org.andengine.util.debug.Debug;

public class ImageButtonSprite extends ButtonSprite {
	private float textX = 0;
	private float textY = 0;
	private Text buttonText;
	private Sprite buttonIcon;

	public ImageButtonSprite(float pX, float pY,
			ITiledTextureRegion pTiledTextureRegion,
			VertexBufferObjectManager pVertexBufferObjectManager) {
		super(pX, pY, pTiledTextureRegion, pVertexBufferObjectManager);
	}

	public ImageButtonSprite(float pX, float pY,
			ITiledTextureRegion pTiledTextureRegion,
			VertexBufferObjectManager pVertexBufferObjectManager,
			OnClickListener pOnClickListener) {
		super(pX, pY, pTiledTextureRegion, pVertexBufferObjectManager,
				pOnClickListener);
	}

    //Text only
	public ImageButtonSprite(float pX, float pY,
			ITiledTextureRegion pTiledTextureRegion,
			VertexBufferObjectManager pVertexBufferObjectManager, String pText,
			Font pFont, float pLeftOffset) {
		super(pX, pY, pTiledTextureRegion, pVertexBufferObjectManager);
		buttonText = new Text(0, 0, pFont, pText, pVertexBufferObjectManager);
		textX = (this.getWidth() - buttonText.getWidth()) / 2;
		textX -= pLeftOffset;
		textY = (this.getHeight() - buttonText.getHeight()) / 2;
		buttonText.setPosition(textX, textY);
		this.attachChild(buttonText);
	}

	//Text only with listener
	public ImageButtonSprite(float pX, float pY,
			ITiledTextureRegion pTiledTextureRegion,
			VertexBufferObjectManager pVertexBufferObjectManager,
			OnClickListener pOnClickListener, String pText, Font pFont, float pLeftOffset) {
		super(pX, pY, pTiledTextureRegion, pVertexBufferObjectManager,
				pOnClickListener);
		buttonText = new Text(0, 0, pFont, pText, pVertexBufferObjectManager);
		textX = (this.getWidth() - buttonText.getWidth()) / 2;
		textX -= pLeftOffset;
		textY = (this.getHeight() - buttonText.getHeight()) / 2;
		buttonText.setPosition(textX, textY);
		this.attachChild(buttonText);
		
	}

    //Text only with status
	public  ImageButtonSprite(float pX, float pY,
			ITextureRegion pTiledTextureRegion,
			VertexBufferObjectManager pVertexBufferObjectManager,
			OnClickListener pOnClickListener, String pText, Font pFont,
			float pWidth, float pHeight, float pTextScale){
		super(pX, pY, pTiledTextureRegion, pVertexBufferObjectManager, pOnClickListener);
		buttonText = new Text(0, 0, pFont, pText, pVertexBufferObjectManager);
		textX = (this.getWidth() - buttonText.getWidth()) / 2;
		textY = (this.getHeight() - buttonText.getHeight()) / 2;
		buttonText.setPosition(textX, textY);
		buttonText.setScale(pTextScale);
		this.attachChild(buttonText);
	}

    //Image only
	public  ImageButtonSprite(float pX, float pY,
			ITextureRegion pNormalTextureRegion,
			ITextureRegion pPressedTextureRegion,
			ITextureRegion pIconTextureRegion,
			VertexBufferObjectManager pVertexBufferObjectManager,
			OnClickListener pOnClickListener,
			float pWidth, float pHeight, float pIconScale){
		super(pX, pY, pNormalTextureRegion, pPressedTextureRegion, pVertexBufferObjectManager, pOnClickListener);
		buttonIcon = new Sprite(0, 0, pIconTextureRegion, pVertexBufferObjectManager);
		buttonIcon.setScale(pIconScale);
		float iconWidth  = pIconTextureRegion.getWidth();
        float iconHeight = pIconTextureRegion.getHeight();
        float marginX = (pWidth-iconWidth)/2;
        float iconY = (pHeight-iconHeight)/1;
		
        buttonIcon.setPosition(marginX, iconY);
		
		this.attachChild(buttonIcon);
        super.setSize(pWidth, pHeight);
	}

	//Image and text	
	public ImageButtonSprite(final float pX, final float pY, final ITextureRegion pNormalTextureRegion, final ITextureRegion pPressedTextureRegion, final ITextureRegion pIconTextureRegion, final VertexBufferObjectManager pVertexBufferObjectManager, final OnClickListener pOnClickListener, String pText, Font pFont, final float pWidth, final float pHeight) {
        this(pX, pY,pNormalTextureRegion, pPressedTextureRegion,
        		pIconTextureRegion, pVertexBufferObjectManager, pOnClickListener,
        		pText, pFont, pWidth, pHeight, 1f, 1f, 0f);
	}

    //Image and text
	public  ImageButtonSprite(float pX, float pY,
			ITextureRegion pNormalTextureRegion,
			ITextureRegion pPressedTextureRegion,
			ITextureRegion pIconTextureRegion,
			VertexBufferObjectManager pVertexBufferObjectManager,
			OnClickListener pOnClickListener, String pText, Font pFont,
			float pWidth, float pHeight, float pIconScale, float pTextScale, float iconTextGap){
		super(pX, pY, pNormalTextureRegion, pPressedTextureRegion, pVertexBufferObjectManager, pOnClickListener);
		buttonText = new Text(0, 0, pFont, pText, pVertexBufferObjectManager);
		buttonIcon = new Sprite(0, 0, pIconTextureRegion, pVertexBufferObjectManager);
		buttonText.setScale(pTextScale);
		buttonIcon.setScale(pIconScale);
		float iconWidth  = pIconTextureRegion.getWidth();
        float iconHeight = pIconTextureRegion.getHeight();
        float textWidth  = buttonText.getWidth();
        float textHeight = buttonText.getHeight();
        float marginX = (pWidth-iconWidth-textWidth-iconTextGap)/2;
        float iconY = (pHeight-iconHeight)/1;
		
        buttonIcon.setPosition(marginX, iconY);
		
		textY = (pHeight - textHeight) / 2;
		buttonText.setPosition(marginX+iconWidth+iconTextGap, textY);

		this.attachChild(buttonIcon);
		this.attachChild(buttonText);
        super.setSize(pWidth, pHeight);
	}

	
	//Text and image without scale
	public ImageButtonSprite(final float pX, final float pY, final ITextureRegion pNormalTextureRegion, final ITextureRegion pPressedTextureRegion, final ITextureRegion pDisabledTextureRegion, final ITextureRegion pIconTextureRegion, final VertexBufferObjectManager pVertexBufferObjectManager, final OnClickListener pOnClickListener, String pText, Font pFont, float pLeftOffset) {
		super(pX, pY, new TiledTextureRegion(pNormalTextureRegion.getTexture(), pNormalTextureRegion, pPressedTextureRegion, pDisabledTextureRegion), pVertexBufferObjectManager, pOnClickListener);
		buttonText = new Text(0, 0, pFont, pText, pVertexBufferObjectManager);
		textX = (this.getWidth() - buttonText.getWidth()) / 2;
		textY = (this.getHeight() - buttonText.getHeight()) / 2;
		buttonText.setPosition(textX, textY);

		
		final Sprite icon = new Sprite(0, 0, pIconTextureRegion, pVertexBufferObjectManager);
		
		this.attachChild(icon);
		this.attachChild(buttonText);
	}

	
	
	public void pressButton() {
		setCurrentTileIndex(1);
		if (this.buttonText != null) {
			buttonText.setX(textX + 5);
			buttonText.setY(textY + 5);
		}
	}

	public void depressButton() {
		setCurrentTileIndex(0);
		if (this.buttonText != null) {
			buttonText.setX(textX);
			buttonText.setY(textY);
		}
	}

	@Override
	public boolean onAreaTouched(TouchEvent pSceneTouchEvent,
			float pTouchAreaLocalX, float pTouchAreaLocalY) {
/*		if (pSceneTouchEvent.getAction() == TouchEvent.ACTION_DOWN) {
			if (this.buttonText != null) {
				buttonText.setX(textX + 5);
				buttonText.setY(textY + 5);
			}
		} else if (pSceneTouchEvent.getAction() == TouchEvent.ACTION_UP) {
			if (this.buttonText != null) {
				buttonText.setX(textX);
				buttonText.setY(textY);
			}
		}*/
		return super.onAreaTouched(pSceneTouchEvent, pTouchAreaLocalX,
				pTouchAreaLocalY);
	}
}