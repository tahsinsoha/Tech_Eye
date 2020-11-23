package limited.it.planet.pickcameraandgalleryimage;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.rey.material.widget.Button;
import java.io.File;
import java.util.Locale;
import id.zelory.compressor.Compressor;
import me.drakeet.materialdialog.MaterialDialog;

public class MainActivity extends AppCompatActivity {
    Button btnPickPhoto;
    ImageView imgvPhoto;
    Bitmap bitmap;
    TextView colortext;
    TextToSpeech textToSpeech;
    String hex,st,s;
    boolean take_ph, select_ph, cancelled;

    //View colorView;
    private final int SELECT_PHOTO = 101;
    private final int CAPTURE_PHOTO = 102;
    final private int REQUEST_CODE_WRITE_STORAGE = 1;
    Uri uri;

    LinearLayout myLayout;
    AnimationDrawable animationDrawable;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        myLayout = (LinearLayout) findViewById(R.id.myLayout);
        animationDrawable = (AnimationDrawable) myLayout.getBackground();
        animationDrawable.setEnterFadeDuration(1500);
        animationDrawable.setExitFadeDuration(1500);
        animationDrawable.start();
        textToSpeech=new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status != TextToSpeech.ERROR) {
                    textToSpeech.setLanguage(Locale.UK);
                }
            }
        });
        initViews();
    }

    public void initViews(){
        btnPickPhoto = (Button)findViewById(R.id.btn_pick_photo);
        imgvPhoto = (ImageView)findViewById(R.id.imgv_photo);
        colortext = (TextView)findViewById(R.id.textView);

        btnPickPhoto.setOnClickListener(new View.OnClickListener() {


            @SuppressLint("WrongConstant")
            @Override
            public void onClick(View v) {
                int hasWriteStoragePermission = 0;
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                    hasWriteStoragePermission = checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
                }

                if (hasWriteStoragePermission != PackageManager.PERMISSION_GRANTED) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        requestPermissions(new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                REQUEST_CODE_WRITE_STORAGE);
                    }
                    //return;
                }
                textToSpeech.speak("Change photo",TextToSpeech.QUEUE_FLUSH,null);
                listDialogue();
            }

        }

        );


    }



    public void listDialogue(){
        final ArrayAdapter<String> arrayAdapter
                = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);

        arrayAdapter.add("Take Photo");
       // textToSpeech.speak("Take Photo",TextToSpeech.QUEUE_FLUSH,null);
        arrayAdapter.add("Select Photo");
      //  textToSpeech.speak("Select Gallery",TextToSpeech.QUEUE_FLUSH,null);

        ListView listView = new ListView(this);
        listView.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));
        float scale = getResources().getDisplayMetrics().density;
        int dpAsPixels = (int) (8 * scale + 0.5f);
        listView.setPadding(0, dpAsPixels, 0, dpAsPixels);
        listView.setDividerHeight(0);
        listView.setAdapter(arrayAdapter);

        final MaterialDialog alert = new MaterialDialog(this).setContentView(listView);

        alert.setPositiveButton("Cancel", new View.OnClickListener() {

            @Override public void onClick(View v) {

                if(!cancelled)
                {
                    cancelled= true;
                    textToSpeech.speak("Cancel",TextToSpeech.QUEUE_FLUSH,null);
                    return;
                }
                cancelled= false;
                textToSpeech.speak("Cancel",TextToSpeech.QUEUE_FLUSH,null);
                alert.dismiss();
            }

        }

        );
       // textToSpeech.speak("Cancel",TextToSpeech.QUEUE_FLUSH,null);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(position==0){

                    if(!take_ph)
                    {
                        take_ph= true;
                        textToSpeech.speak("Take Photo",TextToSpeech.QUEUE_FLUSH,null);
                        return;
                    }
                    take_ph= false;

                    alert.dismiss();
                    Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    //Uri uri  = Uri.parse("file:///sdcard/photo.jpg");
                    String root = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).toString() + "propic.jpg";
                    uri = Uri.parse(root);
                    //i.putExtra(MediaStore.EXTRA_OUTPUT, uri);
                    textToSpeech.speak("Take Photo",TextToSpeech.QUEUE_FLUSH,null);
                    startActivityForResult(i, CAPTURE_PHOTO);

                }else {

                    if(!select_ph)
                    {
                        select_ph= true;
                        textToSpeech.speak("Select Photo",TextToSpeech.QUEUE_FLUSH,null);
                        return;
                    }

                    alert.dismiss();
                    Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                    photoPickerIntent.setType("image/*");
                    textToSpeech.speak("Select Photo",TextToSpeech.QUEUE_FLUSH,null);
                    startActivityForResult(photoPickerIntent, SELECT_PHOTO);

                }
            }
        });

        alert.show();

        imgvPhoto.setDrawingCacheEnabled(true);
        imgvPhoto.buildDrawingCache(true);

        imgvPhoto.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if(motionEvent.getAction()== MotionEvent.ACTION_DOWN || motionEvent.getAction()== MotionEvent.ACTION_MOVE )
                {
                    bitmap = Bitmap.createBitmap(imgvPhoto.getDrawingCache());
                    // Your Code of bitmap Follows here


                    int pixel= bitmap.getPixel((int)motionEvent.getX(),(int)motionEvent.getY());

                    int r= Color.red(pixel);
                    int g= Color.green(pixel);
                    int b= Color.blue(pixel);

                    hex= "#"+Integer.toHexString(pixel);
                    st=hex+','+r+','+g+','+b;
                  //  colorView.setBackgroundColor(Color.rgb(r,g,b));
                   // colortext.setText(st);
                   // textToSpeech.speak(hex, TextToSpeech.QUEUE_FLUSH, null);
                    if((r>=128 && r<=255) && (g>=0 && g<=42) && (b>=0 && b<=60) )
                    textToSpeech.speak("red", TextToSpeech.QUEUE_FLUSH, null);

                   else  if((r>=0 && r<=154) && (g>=100 && g<=255) && (b>=0 && b<=50) )
                        textToSpeech.speak("green", TextToSpeech.QUEUE_FLUSH, null);

                   else  if((r>=0 && r<=138) && (g>=0 && g<=105) && (b>=112 && b<=255) )
                        textToSpeech.speak("blue", TextToSpeech.QUEUE_FLUSH, null);

                   else if( r==255 && g==255 && b==255)
                        textToSpeech.speak("white", TextToSpeech.QUEUE_FLUSH, null);

                    else if( r==0 && g==0 && b==0)
                        textToSpeech.speak("black", TextToSpeech.QUEUE_FLUSH, null);
                }
                return true;
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);

        switch (requestCode) {
            case SELECT_PHOTO:
                if (resultCode == RESULT_OK) {

                    Uri imageUri = imageReturnedIntent.getData();
                    String selectedImagePath = getPath(imageUri);
                    File f = new File(selectedImagePath);
                    Bitmap bmp = Compressor.getDefault(this).compressToBitmap(f);

                    imgvPhoto.setImageBitmap(bmp);

                }
                break;

            case CAPTURE_PHOTO:
                if (resultCode == RESULT_OK) {

                    Bitmap bmp = imageReturnedIntent.getExtras().getParcelable("data");

                    imgvPhoto.setImageBitmap(bmp);

                }

                break;
        }
    }


    public String getPath(Uri uri) {
        // just some safety built in
        if (uri == null) {
            return null;
        }
        // try to retrieve the image from the media store first
        // this will only work for images selected from gallery
        String[] projection = {MediaStore.Images.Media.DATA};
        @SuppressWarnings("deprecation")
        Cursor cursor = managedQuery(uri, projection, null, null,
                null);
        if (cursor != null) {
            int column_index = cursor
                    .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        }
        // this is our fallback here
        return uri.getPath();
    }


}
