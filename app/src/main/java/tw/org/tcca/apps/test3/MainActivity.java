package tw.org.tcca.apps.test3;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Contacts;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.LinkedList;

public class MainActivity extends AppCompatActivity {
    private ListView list;
    private LinkedList<HashMap<String,String>> data = new LinkedList<>();
    private SimpleAdapter adapter;
    private String[] from = {"pname", "porg", "ptel"};
    private int[] to = {R.id.pname, R.id.porg, R.id.ptel};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        list = findViewById(R.id.list);
        initList();
    }

    private void initList(){
        adapter = new SimpleAdapter(this, data, R.layout.item, from, to);
        list.setAdapter(adapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                gotoDetail(i);
            }
        });
    }

    public void test1(View view) {
        //test0(); http://data.nhi.gov.tw/Datasets/Download.ashx?rid=A21030000I-D50001-001&l=https://data.nhi.gov.tw/resource/mask/maskdata.csv
        //https://data.coa.gov.tw/Service/OpenData/ODwsv/ODwsvAgriculturalProduce.aspx
        StringRequest request = new StringRequest(
                Request.Method.GET,
                "https://data.coa.gov.tw/Service/OpenData/ODwsv/ODwsvAgriculturalProduce.aspx",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.v("bradlog", response);
                        parseJSON(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.v("bradlog", error.toString());
                    }
                }
        );
        MainApp.queue.add(request);
    }

    private void parseJSON(String json){
        data.clear();
        try {
            JSONArray root = new JSONArray(json);
            for (int i=0; i<root.length(); i++){
                JSONObject row = root.getJSONObject(i);
                HashMap<String,String> rowData = new HashMap<>();
                rowData.put(from[0], row.getString("Name"));
                rowData.put(from[1], row.getString("ProduceOrg"));
                rowData.put(from[2], row.getString("ContactTel"));
                rowData.put("Feature",row.getString("Feature"));
                rowData.put("SalePlace",row.getString("SalePlace"));
                rowData.put("Column1",row.getString("Column1"));


                data.add(rowData);
            }
            adapter.notifyDataSetChanged();
        } catch (JSONException e) {
            Log.v("bradlog", e.toString());
        }

    }

    private void gotoDetail(int i){
        MainApp.detailData = data.get(i);
        Intent intent = new Intent(this, DetailActivity.class);
        startActivity(intent);
    }

    private void test0(){
        new Thread(){
            @Override
            public void run() {
                try {
                    URL url = new URL("https://data.nhi.gov.tw/resource/mask/maskdata.csv");
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.connect();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    String line; reader.readLine();
                    while ((line = reader.readLine()) != null){
                        String[] rowData = line.split(",");
                        HashMap<String,String> row = new HashMap<>();
                        row.put(from[0],rowData[1]+":"+rowData[4]+":"+rowData[5]);
                        data.add(row);
                    }
                    reader.close();
                    handler.sendEmptyMessage(0);
                }catch (Exception e){
                    Log.v("bradlog", e.toString());
                }

            }
        }.start();
    }

    private UIHandler handler = new UIHandler();

    public void test2(View view) {
        BradInputStreamRequest request = new BradInputStreamRequest(
                Request.Method.GET,
                "https://pdfmyurl.com/?url=http://www.gamer.com.tw",
                null,
                new Response.Listener<byte[]>() {
                    @Override
                    public void onResponse(byte[] response) {
                        try {
                            FileOutputStream fout = openFileOutput("brad.pdf", MODE_PRIVATE);
                            fout.write(response);
                            fout.close();
                            Log.v("bradlog", "save OK");
                        } catch (Exception e) {
                            Log.v("bradlog", "save XX");
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.v("bradlog", error.toString());
                    }
                }
        );

        request.setRetryPolicy(new DefaultRetryPolicy(10*1000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        MainApp.queue.add(request);
    }

    private class UIHandler extends Handler {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            adapter.notifyDataSetChanged();
        }
    }

}