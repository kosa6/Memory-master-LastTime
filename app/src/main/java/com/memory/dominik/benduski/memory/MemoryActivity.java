package com.memory.dominik.benduski.memory;

import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Stack;


/**
 * Created by domin on 18.11.2017.
 */

public class MemoryActivity extends AppCompatActivity
{

    private FeedReaderDbHelper mReaderDbHelper;
    private TableLayout myLayout;
    private Map<String,Integer> mapOfImage;
    private int score;
    private TextView scoreView;
    private final int TIME = 500;
    private ImageView fullSizeImage;

    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.memory_layout);
        mReaderDbHelper = new FeedReaderDbHelper(this);
        myLayout = (TableLayout) findViewById(R.id.tableLayout);
        int numberOfPhotos = getIntent().getIntExtra("number", -1) / 2;
        toastMessage(Integer.toString(numberOfPhotos));
        createMapOfImage();
        score = 0;
        scoreView = (TextView) findViewById(R.id.scoreView);
        fullSizeImage = (ImageView) findViewById(R.id.FullScreenImage);
        listenerForFullSizeImage();
        List<String> randomImage = new ArrayList<>();
        for(int i=0; i<mReaderDbHelper.getData().size(); i++) {
            randomImage.add(mReaderDbHelper.getData().get(i).toString());
            randomImage.add(mReaderDbHelper.getData().get(i).toString());
        }
        Random random= new Random();
        for(int i = 0; i < numberOfPhotos; i++)
        {
            TableRow tableRow = createTableRow(myLayout);
            for(int j = 0; j < 4; j++)
            {
                int temp = random.nextInt(randomImage.size());
                createImageView(randomImage.get(temp), tableRow);
                randomImage.remove(temp);
            }
        }
    }
    private void listenerForFullSizeImage() {
        fullSizeImage.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                fullSizeImage.setVisibility(View.GONE);
                myLayout.setVisibility(View.VISIBLE);
            }
        });
    }

    private void createMapOfImage()
    {
        mapOfImage = new HashMap<>();
        for(int i = 0; i < mReaderDbHelper.getData().size(); i++)
        {
            mapOfImage.put(mReaderDbHelper.getData().get(i).toString(), i);
        }
    }

    private TableRow createTableRow(TableLayout myLayout)
    {
        TableRow newRow = new TableRow(this);
        TableLayout.LayoutParams lp = new TableLayout.LayoutParams(
                TableLayout.LayoutParams.MATCH_PARENT,
                TableLayout.LayoutParams.MATCH_PARENT);
        lp.weight = 1;
        myLayout.addView(newRow, lp);
        return newRow;
    }

    private ImageView createImageView(String uriPath, TableRow tableRow)
    {
        ImageView imageView = new ImageView(this);
        imageView.setImageResource(R.drawable.ic);
        TableRow.LayoutParams lp = new TableRow.LayoutParams(270, 270);
        lp.weight = 1;
        playInMemory(imageView, uriPath);
        tableRow.addView(imageView, lp);
        return imageView;
    }

    private void playInMemory(ImageView imageView, final String uriPath)
    {
        final MemoryImageView miv = new MemoryImageView(imageView, uriPath, mapOfImage.get(uriPath));
        miv.setOnClickProgress(true);
        miv.setClick(false);
        miv.setMiv(null);
        imageView.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                if(miv.getOnClickProgress())
                {
                    if(miv.getClick())
                    {
                        miv.setOnClickProgress(false);
                        if(miv == miv.getMiv())
                        {
                            fullSizeImage.setImageURI(Uri.parse(uriPath));
                            myLayout.setVisibility(View.GONE);
                            fullSizeImage.setVisibility(View.VISIBLE);
                            miv.setOnClickProgress(true);
                        }
                        else
                        {
                            if(miv.getId() == miv.getMiv().getId())
                            {
                                miv.setOnImage();
                                Runnable mMyRunnable = new Runnable()
                                {
                                    @Override
                                    public void run()
                                    {
                                        score += 2;
                                        scoreView.setText("Score: " + Integer.toString(score));
                                        miv.setInvisible();
                                        miv.getMiv().setInvisible();
                                        miv.setClick(false);
                                        miv.setOnClickProgress(true);
                                    }
                                };
                                Handler myHandler = new Handler();
                                myHandler.postDelayed(mMyRunnable, TIME);

                            }
                            else
                            {
                                miv.setOnImage();
                                Runnable mMyRunnable = new Runnable()
                                {
                                    @Override
                                    public void run()
                                    {
                                        score--;
                                        scoreView.setText("Score: " + Integer.toString(score));
                                        miv.setOnMark();
                                        miv.getMiv().setOnMark();
                                        miv.setClick(false);
                                        miv.setOnClickProgress(true);
                                    }
                                };
                                Handler myHandler = new Handler();
                                myHandler.postDelayed(mMyRunnable, TIME);
                            }
                        }
                    }
                    else
                    {
                        miv.setClick(true);
                        miv.setMiv(miv);
                        miv.setOnImage();
                    }
                }
            }
        });
    }

    private void toastMessage(String message)
    {
        Toast.makeText(this,message, Toast.LENGTH_SHORT).show();
    }
}
