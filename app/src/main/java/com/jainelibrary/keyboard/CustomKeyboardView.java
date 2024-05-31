/*
 * Copyright (c) 2016 - Firoz Memon
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *                       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.jainelibrary.keyboard;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;


import java.util.List;

/**
 * Created by Dhaval Hirpara
 */
public class CustomKeyboardView extends KeyboardView
{

	public CustomKeyboardView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public void showWithAnimation(Animation animation) {
		animation.setAnimationListener(new AnimationListener() {

			@Override
			public void onAnimationStart(Animation animation) {
				/* no-op */
			}

			@Override
			public void onAnimationRepeat(Animation animation) {
				/* no-op */
			}

			@Override
			public void onAnimationEnd(Animation animation) {
				setVisibility(View.VISIBLE);
			}
		});
		
		setAnimation(animation);
	}


	/*@Override
	public void onDraw(Canvas canvas) {
		Paint paint = new Paint();
    	paint.setTextAlign(Paint.Align.CENTER);
    	*//*Typeface font = Typeface.createFromAsset(context.getAssets(),
        "fonts/Hippie.otf"); *//*//Insert your font here.
    	paint.setTypeface(Typeface.DEFAULT);
		paint.setTextSize(24); // in px

    	List<Keyboard.Key> keys = getKeyboard().getKeys();
    	for(Keyboard.Key key: keys) {
        	if(key.label != null) canvas.drawText(key.label.toString(), key.x, key.y, paint);
    	}

		*//*super.onDraw(canvas);
		Paint mpaint = new Paint();
		mpaint.setTypeface(Typeface.DEFAULT_BOLD); //to make all Bold. Choose Default to make all normal font
		mpaint.setTextSize(24); // in px


		List<Keyboard.Key> keys = getKeyboard().getKeys();
		for (Keyboard.Key key : keys) {

			if (key.label != null) {
				String keyLabel = key.label.toString();
				canvas.drawText(keyLabel, key.x + key.width, key.y + key.height, mpaint);
			} else if (key.icon != null) {
				key.icon.setBounds(key.x, key.y, key.x + key.width, key.y + key.height);
				key.icon.draw(canvas);
			}
		}*//*

	}*/

}
