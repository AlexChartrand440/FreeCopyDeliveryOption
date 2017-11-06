package com.example.ashutosh.freecopydelivery;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import java.util.*;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLEncoder;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by Ashutosh on 06-11-2017.
 */
public class DeliveryOptions extends AppCompatActivity
{

    String sourcePlace,destinationPlace;
    TextView locationName,destinationName,fileData;
    ImageView colorPage,blackWhite,singleSide,doubleSide;
    Button addDocBtn,submitBtn;
    SeekBar sizeOfPaper;
    CheckBox checkBox;
    Switch deliveryActivator;
    EditText numberOfCopies;

    public static int RESULT_LOAD_IMAGE=100;
    Uri pickedData;

    //Data Selected.
    String chosenColor="",size="",sides="",numcopies="1";
    String delivery="No";


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.store_page);

        sourcePlace=getIntent().getStringExtra("src");
        destinationPlace=getIntent().getStringExtra("destn");

        locationName=(TextView)findViewById(R.id.location_name);
        destinationName=(TextView)findViewById(R.id.dest_name);
        blackWhite=(ImageView)findViewById(R.id.black_white_img);
        colorPage=(ImageView)findViewById(R.id.color_img);
        addDocBtn=(Button)findViewById(R.id.add_btn);
        submitBtn=(Button)findViewById(R.id.submit_btn);
        sizeOfPaper=(SeekBar)findViewById(R.id.seekbar);
        checkBox=(CheckBox)findViewById(R.id.checkbox1);
        singleSide=(ImageView)findViewById(R.id.single_side);
        doubleSide=(ImageView)findViewById(R.id.double_side);
        deliveryActivator=(Switch)findViewById(R.id.delivery_btn);
        numberOfCopies=(EditText) findViewById(R.id.copies_num);
        fileData=(TextView)findViewById(R.id.name_show);

        locationName.setText(sourcePlace);
        deliveryActivator.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                if(deliveryActivator.isChecked())
                {
                    destinationName.setText(destinationPlace);
                    destinationName.setTextColor(getResources().getColor(R.color.dest_color));
                    delivery="Yes";
                }
                if(!deliveryActivator.isChecked())
                {
                    destinationName.setText("Not Selected");
                    destinationName.setTextColor(getResources().getColor(R.color.nodel));
                    delivery="No";
                }
            }
        });
        blackWhite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Toast.makeText(DeliveryOptions.this, "Black & White is Selected", Toast.LENGTH_SHORT).show();
                chosenColor="black and white";
            }
        });
        colorPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(DeliveryOptions.this, "Color Print is Selected", Toast.LENGTH_SHORT).show();
                chosenColor="color print";
            }
        });
        checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                size="A4";
            }
        });

        sizeOfPaper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(sizeOfPaper.getWidth()==0)
                    size="A4";
                else
                    size="A3";
            }
        });

        singleSide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(DeliveryOptions.this, "Single Side is Selected", Toast.LENGTH_SHORT).show();
                sides="single";
            }
        });

        doubleSide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(DeliveryOptions.this, "Double Side is Selected", Toast.LENGTH_SHORT).show();
                sides="double";
            }
        });
        numberOfCopies.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!numberOfCopies.getText().toString().isEmpty()) {
                    numcopies = numberOfCopies.getText().toString();
                }
            }
        });

        addDocBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent uploadImage=new Intent();       //Creating object of Intent.

                uploadImage.setAction(Intent.ACTION_PICK);      //Setting the Action of Intent.

                uploadImage.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                //Setting data to Intent.

                startActivityForResult(uploadImage,RESULT_LOAD_IMAGE);
                //Starting Activity.
            }
        });

        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new SendPostRequest().execute();
                Intent returnBack=new Intent(DeliveryOptions.this,MapsActivity.class);
                startActivity(returnBack);
            }
        });

    }


    public String getRealPathFromURI(Uri contentUri) {
    // can post image
        String [] proj={MediaStore.Images.Media.DATA};
        Cursor cursor = managedQuery( contentUri,
                proj, // Which columns to return
                null, // WHERE clause; which rows to return (all rows)
                null, // WHERE clause selection arguments (none)
                null); // Order-by clause (ascending by name)
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //When image is picked.
        if(requestCode==RESULT_LOAD_IMAGE && resultCode==RESULT_OK && null!=data)
        {
            pickedData=data.getData();
            //Image picked.
            String s= getRealPathFromURI(pickedData);

            String name = s.substring(s.lastIndexOf("/") + 1);
            // instead of "/" you can also use File.sepearator
            //System.out.println("......"+ name);
            fileData.setText(name);
        }
    }


    //Anonymous class to send information to server.
    public class SendPostRequest extends AsyncTask<String, Void, String>
    {
        @Override
        protected void onPreExecute(){}

        @Override
        protected String doInBackground(String... params)
        {
            try
            {
                URL url = new URL("http://35.164.176.81/user/FCIntern/FCInternTask.aspx "); // here is your URL path

                JSONObject postDataParams = new JSONObject();
                postDataParams.put("JobID", "9944440398");
                postDataParams.put("PartenerID", sourcePlace);
                postDataParams.put("IsDelivery",delivery);
                postDataParams.put("Place",destinationPlace);
                Log.e("params",postDataParams.toString());

                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(20000 /* milliseconds */);
                conn.setConnectTimeout(20000 /* milliseconds */);
                conn.setRequestMethod("POST");
                conn.setDoInput(true);
                conn.setDoOutput(true);

                OutputStream os = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(os, "UTF-8"));
                writer.write(getPostDataString(postDataParams));

                writer.flush();
                writer.close();
                os.close();

                int responseCode=conn.getResponseCode();

                if (responseCode == HttpsURLConnection.HTTP_OK) {

                    BufferedReader in=new BufferedReader(new
                            InputStreamReader(
                            conn.getInputStream()));

                    StringBuffer sb = new StringBuffer("");
                    String line="";

                    while((line = in.readLine()) != null) {

                        sb.append(line);
                        break;
                    }

                    in.close();
                    return sb.toString();

                }
                else {
                    return new String("false : "+responseCode);
                }
            }
            catch (Exception e)
            {
                return new String("Exception : "+e.getMessage());
            }

        }

        @Override
        protected void onPostExecute(String result) {

            if(result.substring(0,5)=="false")
            {
                Toast.makeText(getApplicationContext(), result,
                        Toast.LENGTH_LONG).show();
            }
            else
            {
                Toast.makeText(getApplicationContext(), "Data Successfully Taken",
                        Toast.LENGTH_LONG).show();
            }
        }

    }

    public String getPostDataString(JSONObject params) throws Exception {

        StringBuilder result = new StringBuilder();
        boolean first = true;

        Iterator<String> itr = params.keys();

        while(itr.hasNext()){

            String key= itr.next();
            Object value = params.get(key);

            if (first)
                first = false;
            else
                result.append("&");

            result.append(URLEncoder.encode(key, "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(value.toString(), "UTF-8"));

        }
        return result.toString();
    }

}
