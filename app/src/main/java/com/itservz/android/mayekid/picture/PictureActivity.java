package com.itservz.android.mayekid.picture;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.LinearGradient;
import android.graphics.Rect;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RectShape;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.itservz.android.mayekid.R;
import com.itservz.android.mayekid.mayek.MayekDrawView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class PictureActivity extends Activity implements View.OnClickListener {

    private RecyclerView recycler;
    private PictureAdapter adapter;
    private LinearLayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_picture);

        recycler = (RecyclerView)findViewById(R.id.pictureRecyclerView);
        adapter = new PictureAdapter(this, getPictures(), getListener());
        recycler.setAdapter(adapter);
        layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL,false);
        recycler.setLayoutManager(layoutManager);
        recycler.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                int totalHeight = parent.getHeight();
                int maxCardWidth = 108;
                int sidePadding = ((totalHeight - maxCardWidth) / 4);
                sidePadding = Math.max(0, sidePadding);
                outRect.set(0, sidePadding, 0, sidePadding);
            }
        });

    }

    private List<PictureCard> getPictures(){
        List<PictureCard> pictures = new ArrayList();
        pictures.add(new PictureCard(R.drawable.akok));
        pictures.add(new PictureCard(R.drawable.bsam));
        pictures.add(new PictureCard(R.drawable.clai));
        pictures.add(new PictureCard(R.drawable.dmit));
        pictures.add(new PictureCard(R.drawable.epa));
        pictures.add(new PictureCard(R.drawable.fna));
        pictures.add(new PictureCard(R.drawable.gchil));
        pictures.add(new PictureCard(R.drawable.htil));
        pictures.add(new PictureCard(R.drawable.ikhou));
        pictures.add(new PictureCard(R.drawable.jngou));
        pictures.add(new PictureCard(R.drawable.kthou));
        pictures.add(new PictureCard(R.drawable.lwai));
        pictures.add(new PictureCard(R.drawable.myang));
        pictures.add(new PictureCard(R.drawable.nhuk));
        pictures.add(new PictureCard(R.drawable.ouoon));
        pictures.add(new PictureCard(R.drawable.pee));
        pictures.add(new PictureCard(R.drawable.qpham));
        pictures.add(new PictureCard(R.drawable.ratiya));
        return pictures;
    }

    private PictureAdapterListener getListener(){
        return new PictureAdapterListener() {
            @Override
            public void recyclerViewClick(int imageId) {
                System.out.println("PictureAdapterListener onclick " + imageId);
                setContentView(R.layout.activity_mayek_draw);
                MayekDrawView mayekDrawView = (MayekDrawView) findViewById(R.id.drawing1);
                mayekDrawView.setBackgroundResource(imageId);
                if(!paintInitilised){
                    initPaint();
                    paintInitilised = true;
                }
            }
        };
    }


    ////////////////////////////////
    private boolean paintInitilised = false;
    private MayekDrawView currentDrawView;
    private ImageView currPaint, drawBtn, eraseBtn, newBtn, saveBtn, opacityBtn, nextBtn, previousBtn;
    private float smallBrush, mediumBrush, largeBrush;
    private Animation animation;
    private View animatedView;
    private void initPaint(){
        //get drawing view
        currentDrawView = (MayekDrawView)findViewById(R.id.drawing1);

        float alpha = 0.9f;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            currentDrawView.setAlpha(alpha);
        }

        //get the palette and first color button
        LinearLayout paintLayout = (LinearLayout)findViewById(R.id.paint_colors);
        currPaint = (ImageButton)paintLayout.getChildAt(0);
        currentDrawView.setColor(currPaint.getTag().toString());
        currPaint.setImageDrawable(getResources().getDrawable(R.drawable.paint_pressed));

        //sizes from dimensions
        smallBrush = getResources().getInteger(R.integer.small_size);
        mediumBrush = getResources().getInteger(R.integer.medium_size);
        largeBrush = getResources().getInteger(R.integer.large_size);

        //draw button
        drawBtn = (ImageView)findViewById(R.id.draw_btn);
        drawBtn.setOnClickListener(this);

        //set initial size
        currentDrawView.setBrushSize(mediumBrush);

        //erase button
        eraseBtn = (ImageView)findViewById(R.id.erase_btn);
        eraseBtn.setOnClickListener(this);

        //new button
        newBtn = (ImageView)findViewById(R.id.new_btn);
        newBtn.setOnClickListener(this);

        //save button
        saveBtn = (ImageView)findViewById(R.id.save_btn);
        saveBtn.setOnClickListener(this);

        //opacity
        opacityBtn = (ImageView)findViewById(R.id.opacity_btn);
        opacityBtn.setOnClickListener(this);

        //previous button
        previousBtn = (ImageView)findViewById(R.id.previous_btn);
        previousBtn.setOnClickListener(this);

        //next
        nextBtn = (ImageView)findViewById(R.id.next_btn);
        nextBtn.setOnClickListener(this);

        SeekBar backSeekBar = (SeekBar)findViewById(R.id.back_seek);
        backSeekBar.setProgress(100);
        //backSeekBar.setVisibility(SeekBar.INVISIBLE);

        animation  = AnimationUtils.loadAnimation(this, R.anim.paint_animation);
    }
    //user clicked paint
    public void paintClicked(View view){
        currentDrawView.setErase(false);
        currentDrawView.setPaintAlpha(90);
        currentDrawView.setBrushSize(currentDrawView.getLastBrushSize());

        if(view != currPaint){
            ImageButton imageButton = (ImageButton)view;
            String color = view.getTag().toString();
            currentDrawView.setColor(color);
            //update ui
            imageButton.setImageDrawable(getResources().getDrawable(R.drawable.paint_pressed));
            currPaint.setImageDrawable(getResources().getDrawable(R.drawable.paint));
            currPaint=(ImageButton)view;
        }
    }

    @Override
    public void onClick(View view){
        if(animatedView != null) {
            animatedView.clearAnimation();
        }
        if(view.getId()==R.id.draw_btn){
            animatedView = animate(view);
            //draw button clicked
            final Dialog brushDialog = new Dialog(this);
            brushDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            brushDialog.setContentView(R.layout.brush_chooser);
            //listen for clicks on size buttons
            ImageButton smallBtn = (ImageButton)brushDialog.findViewById(R.id.small_brush);
            smallBtn.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    currentDrawView.setErase(false);
                    currentDrawView.setBrushSize(smallBrush);
                    currentDrawView.setLastBrushSize(smallBrush);
                    brushDialog.dismiss();
                }
            });
            ImageButton mediumBtn = (ImageButton)brushDialog.findViewById(R.id.medium_brush);
            mediumBtn.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    currentDrawView.setErase(false);
                    currentDrawView.setBrushSize(mediumBrush);
                    currentDrawView.setLastBrushSize(mediumBrush);
                    brushDialog.dismiss();
                }
            });
            ImageButton largeBtn = (ImageButton)brushDialog.findViewById(R.id.large_brush);
            largeBtn.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    currentDrawView.setErase(false);
                    currentDrawView.setBrushSize(largeBrush);
                    currentDrawView.setLastBrushSize(largeBrush);
                    brushDialog.dismiss();
                }
            });
            //show and wait for user interaction
            brushDialog.show();
        }
        else if(view.getId()==R.id.erase_btn){
            animatedView = animate(view);
            //switch to erase - choose size
            final Dialog brushDialog = new Dialog(this);
            brushDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            brushDialog.setContentView(R.layout.brush_chooser);
            //size buttons
            ImageButton smallBtn = (ImageButton)brushDialog.findViewById(R.id.small_brush);
            smallBtn.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    currentDrawView.setErase(true);
                    currentDrawView.setBrushSize(smallBrush);
                    brushDialog.dismiss();
                }
            });
            ImageButton mediumBtn = (ImageButton)brushDialog.findViewById(R.id.medium_brush);
            mediumBtn.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    currentDrawView.setErase(true);
                    currentDrawView.setBrushSize(mediumBrush);
                    brushDialog.dismiss();
                }
            });
            ImageButton largeBtn = (ImageButton)brushDialog.findViewById(R.id.large_brush);
            largeBtn.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    currentDrawView.setErase(true);
                    currentDrawView.setBrushSize(largeBrush);
                    brushDialog.dismiss();
                }
            });
            brushDialog.show();
        }
        else if(view.getId()==R.id.new_btn){
            animatedView = animate(view);
            currentDrawView.startNew();

        }
        else if(view.getId()==R.id.save_btn){
            //save drawing
            animatedView = animate(view);
            AlertDialog.Builder saveDialog = new AlertDialog.Builder(this);
            saveDialog.setTitle("Save drawing");
            saveDialog.setMessage("Save drawing to device Gallery?");
            saveDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener(){
                public void onClick(DialogInterface dialog, int which){
                    //save drawing
                    currentDrawView.setDrawingCacheEnabled(true);
                    //attempt to save
                    /*String imgSaved = MediaStore.Images.Media.insertImage(
                            getContentResolver(), currentDrawView.getDrawingCache(),
                            UUID.randomUUID().toString()+".png", "drawing");*/
                    String imgSaved = savePicture(currentDrawView.getDrawingCache(), UUID.randomUUID().toString()+".png");
                    //feedback
                    if(imgSaved!=null){
                        Toast savedToast = Toast.makeText(getApplicationContext(),
                                "Drawing saved to Gallery!", Toast.LENGTH_SHORT);
                        savedToast.show();
                    }
                    else{
                        Toast unsavedToast = Toast.makeText(getApplicationContext(),
                                "Oops! Image could not be saved.", Toast.LENGTH_SHORT);
                        unsavedToast.show();
                    }
                    currentDrawView.destroyDrawingCache();
                }
            });
            saveDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener(){
                public void onClick(DialogInterface dialog, int which){
                    dialog.cancel();
                }
            });
            saveDialog.show();
        }
        else if(view.getId()==R.id.opacity_btn){
            animatedView = animate(view);
            //launch opacity chooser
            final Dialog seekDialog = new Dialog(this);
            seekDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            seekDialog.setContentView(R.layout.opacity_chooser);
            //get ui elements
            final TextView seekTxt = (TextView)seekDialog.findViewById(R.id.opq_txt);
            //R.color.g1, R.color.g2, R.color.g3, R.color.g4, R.color.g5, R.color.g6, R.color.g7, R.color.g8, R.color.g9, R.color.g10
            LinearGradient test = new LinearGradient(0.f, 0.f, 700.f, 0.0f,
                    new int[] { 0xFF000000, 0xFF0000FF, 0xFF00FF00, 0xFF00FFFF, 0xFFFF0000, 0xFFFF00FF, 0xFFFFFF00, 0xFFFFFFFF},
                    null, Shader.TileMode.CLAMP);
            ShapeDrawable shape = new ShapeDrawable(new RectShape());
            shape.getPaint().setShader(test);
            final SeekBar seekOpq = (SeekBar)seekDialog.findViewById(R.id.opacity_seek);
            seekOpq.setProgressDrawable( (Drawable)shape );
            //set max
            seekOpq.setMax(100);
            //show current level
            int currLevel = currentDrawView.getPaintAlpha();
            seekTxt.setText(currLevel+"%");
            seekOpq.setProgress(currLevel);

            //update as user interacts
            seekOpq.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    seekTxt.setText(Integer.toString(progress)+"%");
                    currentDrawView.setPaintAlpha(seekOpq.getProgress());
                    //
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {
                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {}

            });
            //show dialog
            seekDialog.show();
        }
        else if(view.getId() == R.id.next_btn){
            setContentView(R.layout.activity_picture);
        }
        else if(view.getId() == R.id.previous_btn){
            System.out.println("back to list ");
            recycler = (RecyclerView)findViewById(R.id.pictureRecyclerView);
            recycler.setAdapter(adapter);
            recycler.setLayoutManager(layoutManager);
            setContentView(R.layout.activity_picture);
        }
    }


    private View animate(View imageView){
        imageView.startAnimation(animation);
        return imageView;
    }

    private String savePicture(Bitmap bm, String imgName) {
        String s = null;
        OutputStream fOut = null;
        String strDirectory = Environment.getExternalStorageDirectory().toString();

        File f = new File(strDirectory, imgName);
        try {
            fOut = new FileOutputStream(f);

            /**Compress image**/
            bm.compress(Bitmap.CompressFormat.JPEG, 85, fOut);
            fOut.flush();
            fOut.close();

            /**Update image to gallery**/
            s = MediaStore.Images.Media.insertImage(getContentResolver(),
                    f.getAbsolutePath(), f.getName(), f.getName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return s;
    }
}
