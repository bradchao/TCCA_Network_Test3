package tw.org.tcca.apps.test3;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Contacts;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.LinkedList;

public class MainActivity extends AppCompatActivity {
    private RequestQueue queue;
    private ListView list;
    private LinkedList<HashMap<String,String>> data = new LinkedList<>();
    private SimpleAdapter adapter;
    private String[] from = {"mesg"};
    private int[] to = {R.id.mesg};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        list = findViewById(R.id.list);
        queue = Volley.newRequestQueue(this);
        initList();
    }

    private void initList(){
        adapter = new SimpleAdapter(this, data, R.layout.item, from, to);
        list.setAdapter(adapter);
    }

    public void test1(View view) {
        test0();
//        StringRequest request = new StringRequest(
//                Request.Method.GET,
//                "https://data.nhi.gov.tw/resource/mask/maskdata.csv",
//                new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String response) {
//                        Log.v("bradlog", response);
//                    }
//                },
//                new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        Log.v("bradlog", error.toString());
//                    }
//                }
//        );
//        queue.add(request);
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

    private class UIHandler extends Handler {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            adapter.notifyDataSetChanged();
        }
    }

}