package com.betelguese.klassify.activities;

import android.app.AlertDialog;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.betelguese.klassify.R;
import com.betelguese.klassify.appdata.AppController;
import com.betelguese.klassify.connection.AlertDialogForAnything;
import com.betelguese.klassify.connection.ConnectionDetector;
import com.betelguese.klassify.model.UserInfo;
import com.betelguese.klassify.utils.Config;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.fedorvlasov.lazylist.ImageLoader;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.widget.CustomEditText;
import com.widget.CustomTextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

public class UpdateProfileActivity extends ActionBarActivity implements View.OnClickListener,Response.Listener<String> ,Response.ErrorListener {

    private ProgressDialog dialog ;

    UserInfo userInfo = null;


    ImageLoader imageLoder;
    ImageView proPicImageView;
    ImageButton takePicCameraB;
    CustomTextView fullNameTxt,emailTxt,mobileNumberTxt,dateOfCreationTxt,locationTxt;
    ImageButton updateFullNameB,updateMobileNumB,updateLocationB;

    ConnectionDetector cd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_profile);

        imageLoder = new ImageLoader(this,R.drawable.person);
        init();
        addListener();
        setDataToComponent();
    }


    private void init() {
        userInfo = Config.userInfo;

        dialog = new ProgressDialog(this);
        dialog.setMessage("Progressing...");
        dialog.setCancelable(false);

        cd = new ConnectionDetector(this);

        proPicImageView = (ImageView) findViewById(R.id.proPicImgView);
        takePicCameraB = (ImageButton) findViewById(R.id.takePicCameraB);

        fullNameTxt=(CustomTextView)findViewById(R.id.fullNameTxt);
        emailTxt= (CustomTextView)findViewById( R.id.emailTxt);
        mobileNumberTxt = (CustomTextView) findViewById(R.id.mobileNumberTxt);
        dateOfCreationTxt = (CustomTextView) findViewById(R.id.dateOfCreationTxt);
        locationTxt = (CustomTextView) findViewById(R.id.locationTxt);

        updateFullNameB = (ImageButton) findViewById(R.id.updateFullNameB);
        updateMobileNumB = (ImageButton) findViewById(R.id.updateMobileNumB);
        updateLocationB= (ImageButton) findViewById(R.id.updateLocationB);

        show(MapViewGone);
        initMap();
    }

    private void addListener() {
        takePicCameraB.setOnClickListener(this);
        updateFullNameB.setOnClickListener(this);
        updateMobileNumB.setOnClickListener(this);
        updateLocationB.setOnClickListener(this);
    }

    private void setDataToComponent() {
        fullNameTxt.setText(Config.userInfo.getFullname());
        emailTxt.setText(Config.userInfo.getEmail());
        mobileNumberTxt.setText(Config.userInfo.getMobile_number());
        dateOfCreationTxt.setText(Config.userInfo.getDateOfCreation());
        if(Config.userInfo.getLocation()==null || Config.userInfo.getLocation().isEmpty() )
            locationTxt.setText("Please add your location");
        else locationTxt.setText(Config.userInfo.getLocation());

        if(Config.userInfo.getProfile_pic()==null || Config.userInfo.getProfile_pic().isEmpty()){}
        else imageLoder.DisplayImage(Config.userInfo.getProfile_pic() , proPicImageView );
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_update_profile, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
         int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.takePicCameraB:
                takePicCameraBActivity();
                break;
            case R.id.updateFullNameB:
                updateFullNameButtonActivity();
                break;
            case R.id.updateMobileNumB :
                updateMobileNumBActivity();
                break;
            case R.id.updateLocationB :
                updateLocationBActivity();
                break;
        }
    }

    private void takePicCameraBActivity() {
        startDialog();
    }

    private void updateFullNameButtonActivity() {
        alerDialogForEditComponent(UPDATE_FULLNAME);
    }

    private void updateMobileNumBActivity() {
        alerDialogForEditComponent(UPDATE_MOBILE_NUMBER);
    }

    private void updateLocationBActivity() {
        show(MapViewShow);
        YoYo.with(Techniques.SlideInRight).duration(700)
                .playOn(findViewById(R.id.map_fragment_layout_id));
    }

    int UPDATE_FULLNAME=0;
    int UPDATE_MOBILE_NUMBER = 1;
    int UPDATE_LOCATION = 3;
    int UPDATE_PROFILE_PIC = 4;

    public void alerDialogForEditComponent(final int updateComponent){
        LayoutInflater li = LayoutInflater.from(this);
        View promptsView = li.inflate(R.layout.pop_up_layout, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setView(promptsView);
        CustomTextView headerTxt = (CustomTextView) promptsView.findViewById(R.id.header);
        if(updateComponent==UPDATE_FULLNAME){
            headerTxt.setText("Enter your Full Name");
        }else if(updateComponent == UPDATE_MOBILE_NUMBER){
            headerTxt.setText("Update your Mobile Number");
        }

        final CustomEditText userInput = (CustomEditText) promptsView.findViewById(R.id.input_field);
        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton("Update",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                //result.setText(userInput.getText());
                                if (updateComponent == UPDATE_FULLNAME) {
                                    userInfo.setFullname(userInput.getText().toString());
                                    if (cd.isConnectingToInternet()) {
                                        volleyRequest(UPDATE_FULLNAME);
                                    }
                                } else if (updateComponent == UPDATE_MOBILE_NUMBER) {
                                    userInfo.setMobile_number(userInput.getText().toString());

                                    if (cd.isConnectingToInternet()) {
                                        volleyRequest(UPDATE_MOBILE_NUMBER);
                                    }
                                }


                            }


                        })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    private void volleyRequest(final int updateComponent) {
        String tag_string_req = "string_req";

        String url = getString(R.string.update_url);
        dialog = new ProgressDialog(this);
        dialog.setMessage("Progrssing...");
        dialog.show();

        StringRequest strReq = new StringRequest(Request.Method.POST, url, this , this ) {
            @Override
            protected HashMap<String, String> getParams() {
                HashMap<String, String> params = new HashMap<String, String>();
                params.put("fullname", fullNameTxt.getText().toString().trim());
                params.put("email", userInfo.getEmail());
                params.put("password", userInfo.getPassword());
                params.put("mobile_number", mobileNumberTxt.getText().toString().trim());
                params.put("profile_pic", userInfo.getProfile_pic());
                params.put("creation_of_date",  userInfo.getDateOfCreation());

                params.put("location", userInfo.getLocation());

                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    int MapViewGone=0;
    int MapViewShow=1;
    private void show(int i) {
        // TODO Auto-generated method stub
        if (i == MapViewGone) {
            findViewById(R.id.map_fragment_layout_id).setVisibility(View.GONE);
        } else if (i == MapViewShow) {
            findViewById(R.id.map_fragment_layout_id).setVisibility(
                    View.VISIBLE);
        }
    }

    final int RQS_GooglePlayServices = 1;
    private GoogleMap myMap;
    Location myLocation;
    Button closeMapB;
    Marker marker = null;
    Double donor_latitude;
    Double donor_longitude;

    private void initMap() {
        // TODO Auto-generated method stub
        closeMapB = (Button) findViewById(R.id.closeB);
        FragmentManager myFragmentManager = getFragmentManager();
        MapFragment myMapFragment = (MapFragment) myFragmentManager
                .findFragmentById(R.id.map);
        myMap = myMapFragment.getMap();
        myMap.setMyLocationEnabled(true);
        myMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        myLocation();
        myMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {

            @Override
            public void onMapLongClick(LatLng point) {
                // TODO Auto-generated method stub
                if (marker != null) {
                    marker.remove();
                }
                marker = myMap.addMarker(new MarkerOptions().position(point)
                        .draggable(true).visible(true));

                donor_latitude = point.latitude;
                donor_longitude = point.longitude;

                new AlertDialog.Builder(UpdateProfileActivity.this)
                        .setTitle("Setting Position")
                        .setMessage(
                                "Are you sure you want to set this location as your fixed position ?")
                        .setPositiveButton(android.R.string.yes,
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,
                                                        int which) {

                                        YoYo.with(Techniques.SlideOutRight)
                                                .duration(700)
                                                .playOn(findViewById(R.id.map_fragment_layout_id));

                                        userInfo.setLocation(donor_latitude+","+donor_longitude);
                                        volleyRequest(UPDATE_LOCATION);

                                    }
                                })
                        .setNegativeButton(android.R.string.no,
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,
                                                        int which) {
                                        // do nothing

                                        marker.remove();
                                    }
                                }).show();
            }
        });
        closeMapB.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                YoYo.with(Techniques.SlideOutRight).duration(700)
                        .playOn(findViewById(R.id.map_fragment_layout_id));

            }
        });
    }

    private void myLocation() {
        // TODO Auto-generated method stub

        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        Criteria criteria = new Criteria();

        String provider = locationManager.getBestProvider(criteria, true);
        Location myLocation = locationManager.getLastKnownLocation(provider);

        double latitude = myLocation.getLatitude();
        double longitude = myLocation.getLongitude();
        LatLng latLng = new LatLng(latitude, longitude);
        MarkerOptions marker = new MarkerOptions().position(latLng).icon(
                BitmapDescriptorFactory
                        .fromResource(R.drawable.ic_btn_search));
        myMap.addMarker(marker);

        myMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15.0f));

    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        int resultCode = GooglePlayServicesUtil
                .isGooglePlayServicesAvailable(getApplicationContext());
        if (resultCode == ConnectionResult.SUCCESS) {
        } else {
            GooglePlayServicesUtil.getErrorDialog(resultCode, this,
                    RQS_GooglePlayServices);
        }

    }


    protected static final int CAMERA_REQUEST = 0;
    protected static final int GALLERY_PICTURE = 1;
    private Intent pictureActionIntent = null;
    Bitmap bitmap;
    String selectedImagePath;



    private void startDialog() {
        AlertDialog.Builder myAlertDialog = new AlertDialog.Builder(this);
        myAlertDialog.setTitle("Upload Pictures Option");
        myAlertDialog.setMessage("How do you want to set your picture?");

        myAlertDialog.setPositiveButton("Gallery",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {
                        pictureActionIntent = new Intent(
                                Intent.ACTION_GET_CONTENT, null);
                        pictureActionIntent.setType("image/*");
                        pictureActionIntent.putExtra("return-data", true);
                        startActivityForResult(pictureActionIntent, GALLERY_PICTURE);
                    }
                });

        myAlertDialog.setNegativeButton("Camera",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {
                        pictureActionIntent = new Intent(
                                android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                        startActivityForResult(pictureActionIntent,
                                CAMERA_REQUEST);

                    }
                });
        myAlertDialog.show();
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GALLERY_PICTURE) {
            if (resultCode == RESULT_OK) {

                if (data != null) {
                    dialog.show();
                    // our BitmapDrawable for the thumbnail
                    BitmapDrawable bmpDrawable = null;
                    // try to retrieve the image using the data from the intent
                    Cursor cursor = getContentResolver().query(data.getData(),
                            null, null, null, null);
                    if (cursor != null) {

                        cursor.moveToFirst();

                        int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                        String fileSrc = cursor.getString(idx);
                        bitmap = BitmapFactory.decodeFile(fileSrc); // load
                        // preview
                        // image
                        bitmap = Bitmap.createScaledBitmap(bitmap,100, 100, false);
                        saveFile(bitmap);
                        proPicImageView.setImageBitmap(bitmap);
                    } else {

                        bmpDrawable = new BitmapDrawable(getResources(), data
                                .getData().getPath());
                        saveFile(bmpDrawable.getBitmap());
                        proPicImageView.setImageDrawable(bmpDrawable);
                    }

                } else {
                    dialog.hide();
                    Toast.makeText(getApplicationContext(), "Cancelled",
                            Toast.LENGTH_SHORT).show();
                }
            } else if (resultCode == RESULT_CANCELED) {
                dialog.hide();
                Toast.makeText(getApplicationContext(), "Cancelled",
                        Toast.LENGTH_SHORT).show();
            }
        } else if (requestCode == CAMERA_REQUEST) {
            if (resultCode == RESULT_OK) {
                dialog.show();
                if (data.hasExtra("data")) {

                    // retrieve the bitmap from the intent
                    bitmap = (Bitmap) data.getExtras().get("data");


                    Cursor cursor = getContentResolver()
                            .query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                                    new String[] {
                                            MediaStore.Images.Media.DATA,
                                            MediaStore.Images.Media.DATE_ADDED,
                                            MediaStore.Images.ImageColumns.ORIENTATION },
                                    MediaStore.Images.Media.DATE_ADDED, null, "date_added ASC");
                    if (cursor != null && cursor.moveToFirst()) {
                        do {
                            Uri uri = Uri.parse(cursor.getString(cursor
                                    .getColumnIndex(MediaStore.Images.Media.DATA)));
                            selectedImagePath = uri.toString();
                        } while (cursor.moveToNext());
                        cursor.close();
                    }

                    bitmap = Bitmap.createScaledBitmap(bitmap, 100,100, false);
                    saveFile(bitmap);
                    proPicImageView.setImageBitmap(bitmap);
                } else if (data.getExtras() == null) {

                    Toast.makeText(getApplicationContext(),
                            "No extras to retrieve!", Toast.LENGTH_SHORT)
                            .show();

                    BitmapDrawable thumbnail = new BitmapDrawable(
                            getResources(), data.getData().getPath());

                    // update the image view with the newly created drawable

                    saveFile(thumbnail.getBitmap());
                    proPicImageView.setImageDrawable(thumbnail);

                }

            } else if (resultCode == RESULT_CANCELED) {
                dialog.hide();
                Toast.makeText(getApplicationContext(), "Cancelled",
                        Toast.LENGTH_SHORT).show();
            }
        }

    }

    public void saveFile(Bitmap bmp){

        try {
            String file_path = Environment.getExternalStorageDirectory().getAbsolutePath() +
                    "/propic";
            File dir = new File(file_path);
            if (!dir.exists())
                dir.mkdirs();
            File file = new File(dir, "propic" + System.currentTimeMillis() + ".jpg");
            FileOutputStream fOut = new FileOutputStream(file);

            bmp.compress(Bitmap.CompressFormat.PNG, 85, fOut);
            fOut.flush();
            fOut.close();

            uploadFile(file.getAbsolutePath());
        }catch (Exception e){

        }
    }

    private int serverResponseCode = 0;
    private String upLoadServerUri = "http://nxtgeninteractive.com/nxtgenbv/UploadToServer.php";
    private String imagepathServer="http://nxtgeninteractive.com/nxtgenbv/photo/";

    public int uploadFile(String sourceFileUri) {


        final String fileName = sourceFileUri;

        HttpURLConnection conn = null;
        DataOutputStream dos = null;
        String lineEnd = "\r\n";
        String twoHyphens = "--";
        String boundary = "*****";
        int bytesRead, bytesAvailable, bufferSize;
        byte[] buffer;
        int maxBufferSize = 1 * 1024 * 1024;
        File sourceFile = new File(sourceFileUri);

        if (!sourceFile.isFile()) {

            dialog.hide();

            runOnUiThread(new Runnable() {
                public void run() {

                }
            });

            return 0;

        }
        else
        {
            try {

                // open a URL connection to the Servlet
                FileInputStream fileInputStream = new FileInputStream(sourceFile);
                URL url = new URL(upLoadServerUri);

                // Open a HTTP  connection to  the URL
                conn = (HttpURLConnection) url.openConnection();
                conn.setDoInput(true); // Allow Inputs
                conn.setDoOutput(true); // Allow Outputs
                conn.setUseCaches(false); // Don't use a Cached Copy
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Connection", "Keep-Alive");
                conn.setRequestProperty("ENCTYPE", "multipart/form-data");
                conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
                conn.setRequestProperty("uploaded_file", fileName);

                dos = new DataOutputStream(conn.getOutputStream());

                dos.writeBytes(twoHyphens + boundary + lineEnd);
                dos.writeBytes("Content-Disposition: form-data; name=\"uploaded_file\";filename=\""
                        + fileName + "\"" + lineEnd);

                dos.writeBytes(lineEnd);

                // create a buffer of  maximum size
                bytesAvailable = fileInputStream.available();

                bufferSize = Math.min(bytesAvailable, maxBufferSize);
                buffer = new byte[bufferSize];

                // read file and write it into form...
                bytesRead = fileInputStream.read(buffer, 0, bufferSize);

                while (bytesRead > 0) {

                    dos.write(buffer, 0, bufferSize);
                    bytesAvailable = fileInputStream.available();
                    bufferSize = Math.min(bytesAvailable, maxBufferSize);
                    bytesRead = fileInputStream.read(buffer, 0, bufferSize);

                }

                // send multipart form data necesssary after file data...
                dos.writeBytes(lineEnd);
                dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

                // Responses from the server (code and message)
                serverResponseCode = conn.getResponseCode();
                String serverResponseMessage = conn.getResponseMessage();

                Log.i("uploadFile", "HTTP Response is : "
                        + serverResponseMessage + ": " + serverResponseCode);

                if(serverResponseCode == 200){

                    runOnUiThread(new Runnable() {
                        public void run() {
                            String msg = "File Upload Completed.\n\n See uploaded file here : \n\n"
                                    +" F:/wamp/wamp/www/uploads";
                            //messageText.setText(msg);
                            Toast.makeText(UpdateProfileActivity.this, "File Upload Complete.", Toast.LENGTH_SHORT).show();

                            userInfo.setProfile_pic(imagepathServer+fileName);
                            volleyRequest(UPDATE_PROFILE_PIC);

                        }
                    });
                }

                //close the streams //
                fileInputStream.close();
                dos.flush();
                dos.close();

            } catch (MalformedURLException ex) {

                dialog.hide();
                ex.printStackTrace();

                runOnUiThread(new Runnable() {
                    public void run() {
                        //messageText.setText("MalformedURLException Exception : check script url.");
                        Toast.makeText(UpdateProfileActivity.this, "MalformedURLException", Toast.LENGTH_SHORT).show();
                    }
                });

                Log.e("Upload file to server", "error: " + ex.getMessage(), ex);
            } catch (Exception e) {

                dialog.hide();
                e.printStackTrace();

                runOnUiThread(new Runnable() {
                    public void run() {
                        //messageText.setText("Got Exception : see logcat ");
                        Toast.makeText(UpdateProfileActivity.this, "Got Exception : see logcat ", Toast.LENGTH_SHORT).show();
                    }
                });
                Log.e("Upload file to server Exception", "Exception : "  + e.getMessage(), e);
            }
            dialog.hide();
            return serverResponseCode;

        }
    }

    @Override
    public void onErrorResponse(VolleyError volleyError) {
        VolleyLog.d("Error respose", "Error: " + volleyError.getMessage());

        dialog.hide();
    }

    @Override
    public void onResponse(String response) {
        try {
            String responseString = new JSONObject(response).getString("status");
            if (responseString.equals("success")) {


                Config.userInfo.setFullname(userInfo.getFullname());
                Config.userInfo.setMobile_number(userInfo.getMobile_number());
                Config.userInfo.setLocation(userInfo.getLocation());
                Config.userInfo.setProfile_pic(userInfo.getProfile_pic());

                fullNameTxt.setText(userInfo.getFullname());
                mobileNumberTxt.setText(userInfo.getMobile_number());
                locationTxt.setText(userInfo.getLocation());

                Toast.makeText(this, "success", Toast.LENGTH_SHORT).show();
                dialog.hide();
                /*Intent intent = new Intent(this, LogInActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);*/
            } else {
                AlertDialogForAnything.showAlertDialogWhenComplte(this, "Fail", "Fail to sign up.", false);
                dialog.hide();

            }
        }catch (JSONException e){
            e.printStackTrace();
        }


        dialog.hide();
    }
}



