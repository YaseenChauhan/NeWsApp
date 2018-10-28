package com.example.yaseen.hackernewsreader;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

   Map<Integer, String> ArticleTitle = new HashMap<Integer, String>();
    Map<Integer, String> Articleurl = new HashMap<Integer, String>();
    ArrayList<Integer> Articleids = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        DownlaodTask task = new DownlaodTask();
        try {


            String result = task.execute("https://hacker-news.firebaseio.com/v0/askstories.json?print=pretty").get();

            JSONArray jsonArray = new JSONArray(result);

            for(int i=0;i<20;i++) {

                String Articleid = jsonArray.getString(i);

                DownlaodTask getArticle = new DownlaodTask();
                String Articleinfo = getArticle.execute("https://hacker-news.firebaseio.com/v0/item/" + Articleid + ".json?print=pretty").get();

                JSONObject jsonObject = new JSONObject(Articleinfo);

                String articletitle = jsonObject.getString("title");

                if (jsonObject.has("url")) {
                    String articleurl = jsonObject.optString("url");
                    Articleurl.put(Integer.valueOf(Articleid),articleurl);
                }




                Articleids.add(Integer.valueOf(Articleid));
                ArticleTitle.put(Integer.valueOf(Articleid),articletitle);







            /*    String Articleid = jsonArray.getString(i);

                DownlaodTask getArticle = new DownlaodTask();
                String Articleinfo = getArticle.execute("https://hacker-news.firebaseio.com/v0/item/" + Articleid + ".json?print=pretty").get();

                JSONObject jsonObject = new JSONObject(Articleinfo);

                if(jsonObject.has("url")){
                    String jsonObjectTitle = jsonObject.getString("title");
                    String jsonObjectUrl = jsonObject.getString("url");
                   // Log.d("ArticleInfo", jsonArray.getString(i));
                    Log.d("ArticleTitle", jsonObjectTitle);

                   Log.d("ArticleUrl" , jsonObjectUrl);*/
                }

            Log.i("articleIds",Articleids.toString());


            Log.i("articletitle",ArticleTitle.toString());



            Log.i("articleturl", Articleurl.toString());




        }
        catch (Exception e)
        {
            e.printStackTrace();
        }


    }

        public class DownlaodTask extends AsyncTask<String,Void,String>
        {
            String result = "";
            HttpURLConnection urlConnection = null;
            URL url;


            @Override
            protected String doInBackground(String... urls) {

                try {

                    url = new URL(urls[0]);
                    urlConnection = (HttpURLConnection)url.openConnection();
                    InputStream in = urlConnection.getInputStream();
                    InputStreamReader reader = new InputStreamReader(in);
                    int data = reader.read();
                    while(data != -1)
                    {
                        char current = (char)data;
                        result+=current;
                        data=reader.read();
                    }
                   return result;

                }
                catch (Exception e)
                {
                    e.printStackTrace();

                }


                return result;
            }
        }





    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
