package com.memory.dominik.benduski.memory;

import android.net.Uri;
import android.view.View;
import android.widget.ImageView;

/**
 * Created by domin on 24.11.2017.
 */

public class MemoryImageView {
    private final static int MARK = R.drawable.ic;
    private ImageView imageView;
    private String urlImage;
    private int id;
    private static boolean click = false;
    private static MemoryImageView miv;
    private static boolean onClickProgress = true;

    public MemoryImageView(ImageView imageView, String urlImage, int id) {
        this.imageView = imageView;
        this.urlImage = urlImage;
        this.id = id;
    }

    public boolean getOnClickProgress() {
        return onClickProgress;
    }

    public void setOnClickProgress(boolean onClickProgress) {
        MemoryImageView.onClickProgress = onClickProgress;
    }

    public void setInvisible() {
        imageView.setVisibility(View.INVISIBLE);
    }

    public MemoryImageView getMiv() {
        return miv;
    }

    public void setMiv(MemoryImageView miv) {
        MemoryImageView.miv = miv;
    }

    public void setOnImage() {
        this.imageView.setImageURI(Uri.parse(urlImage));
    }

    public void setOnMark() {
        this.imageView.setImageResource(MARK);
    }

    public int getId() {
        return this.id;
    }

    public boolean getClick() {
        return click;
    }

    public void setClick(boolean b) {
        click = b;
    }
}
