package com.example.jsontheory;

import androidx.appcompat.app.AppCompatActivity;

import android.hardware.lights.LightState;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    TextView nam,salar;
    String JSON_STRING="{\"employee\":{\"name\":\"mukesh\",\"salary\":\"46544645\"}";
    String name,salary;

    ListView list;
    String namey,age;
    private static String JSON_URL="";
    ArrayList<String> friendlist;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        nam=findViewById(R.id.name);
        salar=findViewById(R.id.salary);


        list=findViewById(R.id.list);
        friendlist=new ArrayList<>();



        //getting json objects
        try{
            //get json objects from json file
            JSONObject obj=new JSONObject(JSON_STRING);
           //fetJson object named employee
            JSONObject employee=obj.getJSONObject("employee");

            // getting employee name salary inside json objects(employee)
            name=employee.getString("name");
            salary=employee.getString("salray");

            //setting to textview
            nam.setText("name: "+name);
            salar.setText("salary: "+salary);

        } catch (JSONException e) {
            e.printStackTrace();
        }
     GetData getData=new GetData();
        getData.execute();
    }
    public class GetData extends AsyncTask<String,String,String>{

        @Override
        protected String doInBackground(String... strings) {
            String current="";
            try{
                URL url;
                HttpURLConnection urlConnection=null;
                try{
                    url=new URL(JSON_URL);
                    urlConnection=(HttpURLConnection)url.openConnection();
                    InputStream in=urlConnection.getInputStream();
                    InputStreamReader isr=new InputStreamReader(in);
                    int data=isr.read();
                    while(data!=-1){

                        current+=(char)data;
                        data=isr.read();
                    }
                    return current;
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                finally {
                    if(urlConnection!=null)
                        urlConnection.disconnect();
                }
            }catch (Exception e)
            {
                e.printStackTrace();
            }
            return current;
    }

        @Override
        protected void onPostExecute(String s) {
            try{
                JSONObject  jsonObject=new JSONObject(s);
                JSONArray jsonArray=jsonObject.getJSONArray("Friends");
                for(int i=0;i<jsonArray.length();i++)
                {
                    JSONObject jsonObject1=jsonArray.getJSONObject(i);
                    namey =jsonObject1.getString("name");
                    age=jsonObject1.getString("age");

                    HashMap<String,String> friends=new HashMap<>();
                    friends.put("name",namey);
                    friends.put("age",age);
                    friendlist.add(friends);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
            ListAdapter adapter=new SimpleAdapter(
                    MainActivity.this,friendlist, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,
                    new String[]{"name","age"},
                    new int[]{R.id.text1,R.id.text2});
            list.setAdapter(adapter);
        }
    }