package com.example.classcolormatrix;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import java.io.FileNotFoundException;

public class MainActivity extends AppCompatActivity {
    final int RQS_IMAGE = 1;

    private Button mLoadImageButton;
    private ImageView mGaleryImageView;
    private SeekBar mRedSeekbar, mGreenSeekbar, mBlueSeekbar, mAlphaSeekbar;
    private TextView mScaleTextView;

    private Uri mSource;
    private Bitmap mBitmap;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mLoadImageButton = (Button) findViewById(R.id.buttonLoadimage);
        mGaleryImageView = (ImageView) findViewById(R.id.imageView);

        mLoadImageButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(
                        Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, RQS_IMAGE);
            }
        });

        mScaleTextView = (TextView) findViewById(R.id.textViewCMScale);
        mRedSeekbar = (SeekBar) findViewById(R.id.seekBarRedScale);
        mGreenSeekbar = (SeekBar) findViewById(R.id.seekBarGreenScale);
        mBlueSeekbar = (SeekBar) findViewById(R.id.seekBarBlueScale);
        mAlphaSeekbar = (SeekBar) findViewById(R.id.seekBarAlphaScale);

        mRedSeekbar.setOnSeekBarChangeListener(seekBarChangeListener);
        mGreenSeekbar.setOnSeekBarChangeListener(seekBarChangeListener);
        mBlueSeekbar.setOnSeekBarChangeListener(seekBarChangeListener);
        mAlphaSeekbar.setOnSeekBarChangeListener(seekBarChangeListener);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case RQS_IMAGE:
                    mSource = data.getData();

                    try {
                        mBitmap = BitmapFactory
                                .decodeStream(getContentResolver().openInputStream(
                                        mSource));

                        mRedSeekbar.setProgress(100);
                        mGreenSeekbar.setProgress(100);
                        mBlueSeekbar.setProgress(100);
                        mAlphaSeekbar.setProgress(100);

                        loadBitmapScaleColor();

                    } catch (FileNotFoundException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
            }
        }
    }

    SeekBar.OnSeekBarChangeListener seekBarChangeListener = new SeekBar.OnSeekBarChangeListener() {

        @Override
        public void onProgressChanged(SeekBar seekBar, int progress,
                                      boolean fromUser) {
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {
        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            loadBitmapScaleColor();
        }
    };

    private void loadBitmapScaleColor() {
        if (mBitmap != null) {

            int progressScaleRed = mRedSeekbar.getProgress();
            int progressScaleGreen = mGreenSeekbar.getProgress();
            int progressScaleBlue = mBlueSeekbar.getProgress();
            int progressScaleAlpha = mAlphaSeekbar.getProgress();

            float scaleRed = (float) progressScaleRed / 100;
            float scaleGreen = (float) progressScaleGreen / 100;
            float scaleBlue = (float) progressScaleBlue / 100;
            float scaleAlpha = (float) progressScaleAlpha / 100;

            mScaleTextView.setText("setScale: " + String.valueOf(scaleRed) + ", "
                    + String.valueOf(scaleGreen) + ", "
                    + String.valueOf(scaleBlue) + ", "
                    + String.valueOf(scaleAlpha));

            Bitmap bitmapColorScaled = updateScale(mBitmap, scaleRed,
                    scaleGreen, scaleBlue, scaleAlpha);

            mGaleryImageView.setImageBitmap(bitmapColorScaled);
        }
    }

    private Bitmap updateScale(Bitmap src, float rScale, float gScale,
                               float bScale, float aScale) {

        int width = src.getWidth();
        int height = src.getHeight();

        Bitmap bitmapResult = Bitmap
                .createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvasResult = new Canvas(bitmapResult);
        Paint paint = new Paint();
        ColorMatrix colorMatrix = new ColorMatrix();
        colorMatrix.setScale(rScale, gScale, bScale, aScale);
        ColorMatrixColorFilter filter = new ColorMatrixColorFilter(colorMatrix);
        paint.setColorFilter(filter);
        canvasResult.drawBitmap(src, 0, 0, paint);

        return bitmapResult;
    }
}