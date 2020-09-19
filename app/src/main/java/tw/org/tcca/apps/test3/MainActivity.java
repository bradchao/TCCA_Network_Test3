package tw.org.tcca.apps.test3;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
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

public class MainActivity extends AppCompatActivity {
    private RequestQueue queue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        queue = Volley.newRequestQueue(this);
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
                    String line;
                    while ((line = reader.readLine()) != null){
                        Log.v("bradlog", line);
                    }
                    reader.close();
                }catch (Exception e){

                }

            }
        }.start();
    }

}