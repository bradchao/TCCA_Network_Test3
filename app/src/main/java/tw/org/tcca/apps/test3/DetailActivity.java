package tw.org.tcca.apps.test3;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.toolbox.ImageRequest;

public class DetailActivity extends AppCompatActivity {
    private TextView salePlace, feature;
    private ImageView img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        img = findViewById(R.id.img);
        salePlace = findViewById(R.id.salePlace);
        feature = findViewById(R.id.feature);

        salePlace.setText(MainApp.detailData.get("SalePlace"));
        feature.setText(MainApp.detailData.get("Feature"));
        fetchImage();
    }

    private void fetchImage(){
        ImageRequest request = new ImageRequest(
                MainApp.detailData.get("Column1"),
                new Response.Listener<Bitmap>() {
                    @Override
                    public void onResponse(Bitmap response) {
                        img.setImageBitmap(response);
                    }
                },
                0,0,
                Bitmap.Config.ARGB_8888,
                null
        );
        MainApp.queue.add(request);
    }

}