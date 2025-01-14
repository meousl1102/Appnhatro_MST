package activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.mstappdemo.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import adapter.SearchAdapter;
import model.Sanpham;
import ultil.CheckConnection;
import ultil.Server;

public class Search_Activity extends AppCompatActivity {
    Toolbar toolbarhotel;
    ListView lvhotel;
    EditText inputsearch;
    Button btnsearch;
    SearchAdapter hotelAdapter;
    ArrayList<Sanpham> manghotel;
    int idhotel =0;
    int page =1;
    String textsearch;
    View footerview;
    boolean isLoading =false;
    boolean limitData =false;
    Search_Activity.mHandler mHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        Anhxa();
        if (CheckConnection.haveNetworkConnection(getApplicationContext())){
            GetIdloaisp();
            ActionToolbar();
//            textsearch = inputsearch.getText().toString();
//            GetData(textsearch);
//            LoadMoreData();
        }else{
            CheckConnection.ShowToast_Short(getApplicationContext(),"Bạn Hãy Kiểm Tra Lại Kết Nối");
            finish();
        }

        btnsearch.setOnClickListener(v -> {
            //Get value input

            textsearch = inputsearch.getText().toString();
            manghotel = new ArrayList<>();
            hotelAdapter = new SearchAdapter(getApplicationContext(),manghotel);
            lvhotel.setAdapter(hotelAdapter);
            GetData(textsearch);
            LoadMoreData();

        });

    }

    private void GetData(String textsearch) {
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        String duongdan = Server.Duongdansanphamsearch;
        StringRequest stringRequest = new StringRequest(Request.Method.POST, duongdan, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                int id=0;
                String Tenhotel="";
                int Giahotel=0;
                String Hinhanhhotel = "";
                String Mota="";
                int Idsphotel=0;
                if (response!=null && response.length()!=2){
                    lvhotel.removeFooterView(footerview);
                    try {
                        JSONArray jsonArray = new JSONArray(response);
                        for (int i=0;i<jsonArray.length();i++){
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            id = jsonObject.getInt("id");
                            Tenhotel=jsonObject.getString("tensp");
                            Giahotel=jsonObject.getInt("giasp");
                            Hinhanhhotel=jsonObject.getString("hinhanhsp");
                            Mota=jsonObject.getString("motasp");
                            Idsphotel=jsonObject.getInt("idsanpham");
                            manghotel.add(new Sanpham(id,Tenhotel,Giahotel,Hinhanhhotel,Mota,Idsphotel));
                            hotelAdapter.notifyDataSetChanged();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }else{
                    limitData=true;
                    lvhotel.removeFooterView(footerview);
                    CheckConnection.ShowToast_Short(getApplicationContext(),"Đã hết dữ liệu");
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> param = new HashMap<String,String>();
                param.put("motasanpham",String.valueOf(textsearch));
                return param;
            }
        };
        requestQueue.add(stringRequest);
    }

    private void LoadMoreData() {
        lvhotel.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(),ChiTietSanPham.class);
                intent.putExtra("thongtinsanpham", manghotel.get(position));
                startActivity(intent);
            }
        });
//        lvhotel.setOnScrollListener(new AbsListView.OnScrollListener() {
//            @Override
//            public void onScrollStateChanged(AbsListView view, int scrollState) {
//
//            }
//
//            @Override
//            public void onScroll(AbsListView view, int FirstItem, int VisibleItem, int TotalItem) {
//                if(FirstItem + VisibleItem == TotalItem && TotalItem != 0 && isLoading == false && limitData == false){
//                    isLoading =true;
//                    Search_Activity.ThreadData threadData = new Search_Activity.ThreadData();
//                    threadData.start();
//                }
//
//            }
//        });
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.menugiohang:
                Intent intent = new Intent(getApplicationContext(), activity.Giohang.class);
                startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    private void GetDataa() {
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        String duongdan = Server.Duongdansanpham;
        StringRequest stringRequest = new StringRequest(Request.Method.POST, duongdan, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                int id=0;
                String Tenhotel="";
                int Giahotel=0;
                String Hinhanhhotel = "";
                String Mota="";
                int Idsphotel=0;

                if (response!=null && response.length()!=2){
                    lvhotel.removeFooterView(footerview);
                    try {
                        JSONArray jsonArray = new JSONArray(response);
                        for (int i=0;i<jsonArray.length();i++){
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            id = jsonObject.getInt("id");
                            Tenhotel=jsonObject.getString("tensp");
                            Giahotel=jsonObject.getInt("giasp");
                            Hinhanhhotel=jsonObject.getString("hinhanhsp");
                            Mota=jsonObject.getString("motasp");
                            Idsphotel=jsonObject.getInt("idsanpham");
                            manghotel.add(new Sanpham(id,Tenhotel,Giahotel,Hinhanhhotel,Mota,Idsphotel));
                            hotelAdapter.notifyDataSetChanged();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }else{
                    limitData=true;
                    lvhotel.removeFooterView(footerview);
                    CheckConnection.ShowToast_Short(getApplicationContext(),"Đã hết dữ liệu");
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> param = new HashMap<String,String>();
                param.put("IDsanpham",String.valueOf(idhotel));
                return param;
            }
        };
        requestQueue.add(stringRequest);
    }

    private void ActionToolbar() {
        setSupportActionBar(toolbarhotel);
        ActionBar actionBar=getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        toolbarhotel.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void GetIdloaisp() {
        idhotel = getIntent().getIntExtra("idloaisanpham",-1);
        Log.d("giatriloaisanpham", idhotel+"");
    }

    private void Anhxa() {
        toolbarhotel = findViewById(R.id.toolbarsearch);
        lvhotel = findViewById(R.id.listviewsearch);
        manghotel = new ArrayList<>();
        hotelAdapter = new SearchAdapter(getApplicationContext(),manghotel);
        lvhotel.setAdapter(hotelAdapter);
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        footerview = inflater.inflate(R.layout.progressbar,null);
        mHandler = new Search_Activity.mHandler();

        inputsearch = findViewById(R.id.textsearch);
        btnsearch = findViewById(R.id.btnsearch);

    }

    public class mHandler extends Handler {
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what){
                case 0:
                    lvhotel.addFooterView(footerview);
                    break;
                case 1:
                    GetData(textsearch);
                    isLoading=false;
                    break;
            }
            super.handleMessage(msg);
        }
    }


    public class ThreadData extends Thread{
        @Override
        public void run() {
            mHandler.sendEmptyMessage(0);
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Message message = mHandler.obtainMessage(1);
            mHandler.sendMessage(message);
            super.run();
        }
    }
}