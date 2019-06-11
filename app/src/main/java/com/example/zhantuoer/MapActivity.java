package com.example.zhantuoer;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.Poi;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;

import java.util.ArrayList;
import java.util.List;

public class MapActivity extends AppCompatActivity {
    protected static final String URL_JinWei = JsonTools.CONSTURL+"zhantuoer/acceptEarth";
    private static final String TAG = "MapActivity";
    private MapView mapView;
    private TextView textView;
    LocationClient locationClient;
    public BDLocationListener myListener = new MyLocationListener();
    BaiduMap mBaiduMap;
    boolean isFirstLoc = true;
    private ImageView Back;
    //用户名
    private String user_phone;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SDKInitializer.initialize(getApplicationContext());
        //沉浸式状态栏
        if (Build.VERSION.SDK_INT >= 21) {
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
        setContentView(R.layout.activity_map);
        init();
        mapView = (MapView) findViewById(R.id.bmapView);
        Back=(ImageView)findViewById(R.id.back);
        Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MapActivity.this,RecyclerActivity.class);
                startActivity(intent);
                finish();
            }
        });
        textView=(TextView)findViewById(R.id.place);
    }

    private void init(){
        Intent intent = getIntent();
        user_phone = intent.getStringExtra("user_phone");
        Log.e(TAG, "init: "+user_phone );
    }

    public void  LocationStart(){
      LocationClientOption option = new LocationClientOption();
      option.setCoorType("bd09ll");//设置返回的定位结果坐标系
      int span=3000;
      option.setCoorType("bd09ll"); // 设置坐标类型
      option.setScanSpan(span);//定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
      option.setIsNeedAddress(true);//设置是否需要地址信息
      option.setOpenGps(true);//设置是否使用gps
      option.setLocationNotify(true);//设置是否当GPS有效时按照1S/1次频率输出GPS结果
      option.setIsNeedLocationDescribe(true);//设置是否需要位置语义化结果
      option.setIsNeedLocationPoiList(true);//设置是否需要POI结果，可以在BDLocation.getPoiList里得到
      option.setIgnoreKillProcess(false);//定位SDK内部是一个SERVICE
      option.SetIgnoreCacheException(false);//设置是否收集CRASH信息
      option.setEnableSimulateGps(false);//设置是否需要过滤GPS仿真结果
     // option.setLocationMode(LocationClientOption.LocationMode.Device_Sensors);//强制开启GPS定位
      List<String> permissionList = new ArrayList<>();    //动态获取权限
      if (ContextCompat.checkSelfPermission(MapActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
          permissionList.add(Manifest.permission.ACCESS_FINE_LOCATION);
      }
      if (ContextCompat.checkSelfPermission(MapActivity.this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
          permissionList.add(Manifest.permission.READ_PHONE_STATE);
      }
      if (ContextCompat.checkSelfPermission(MapActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
          permissionList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
      }
      if (!permissionList.isEmpty()) {
          String [] permissions = permissionList.toArray(new String[permissionList.size()]);
          ActivityCompat.requestPermissions(MapActivity.this, permissions, 1);
      } else {
          locationClient.start();
      }
      locationClient.setLocOption(option);
  }
    private class MyLocationListener implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location) {
            StringBuffer stringBuffer = new StringBuffer(256);
            StringBuffer lotitude=new StringBuffer(256);
            StringBuffer longitude=new StringBuffer(256);
            StringBuffer NativePlace=new StringBuffer(256);
            jinweidu(user_phone,location.getLongitude(),location.getLatitude());
            getOtherMap();
            stringBuffer.append("time : ");
            stringBuffer.append(location.getTime());
            stringBuffer.append("\nerror code : ");
            stringBuffer.append(location.getLocType());
            stringBuffer.append("\nlatitude : ");
            stringBuffer.append(location.getLatitude());
            lotitude.append(location.getLatitude());
            stringBuffer.append("\nlontitude : ");
            stringBuffer.append(location.getLongitude());
            longitude.append(location.getLongitude());
            stringBuffer.append("\nradius : ");
            stringBuffer.append(location.getRadius());
            // map view 销毁后不在处理新接收的位置
            if (location == null || mapView == null) {
                return;
            }
            MyLocationData locData = new MyLocationData.Builder()
                    .accuracy(location.getRadius())
                    // 此处设置开发者获取到的方向信息，顺时针0-360
                    .direction(100).latitude(location.getLatitude())
                    .longitude(location.getLongitude()).build();
            // 设置定位数据
            mBaiduMap.setMyLocationData(locData);

            if (isFirstLoc) {
                isFirstLoc = false;
                LatLng ll = new LatLng(location.getLatitude(),
                        location.getLongitude());
                MapStatus.Builder builder = new MapStatus.Builder();
                builder.target(ll).zoom(18.0f);
                mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));
            }
            stringBuffer.append("\nlocationdescribe : ");
            stringBuffer.append(location.getLocationDescribe());// 语义化位置信息
            List<Poi> list = location.getPoiList();// POI数据
            NativePlace.append(list.get(1).getName());
            if (list != null) {
                stringBuffer.append("\npoilist size = : ");
                stringBuffer.append(list.size());
                for (Poi p : list) {
                    stringBuffer.append("\npoi= : ");
                    stringBuffer.append(p.getId() + " " + p.getName() + " " + p.getRank());
                }
                textView.setText(NativePlace.toString());
            }
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        //在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
        mapView.onDestroy();
    }
    @Override
    protected void onResume() {
        super.onResume();
        mBaiduMap = mapView.getMap();
        // 开启定位图层
        mBaiduMap.setMyLocationEnabled(true);
        locationClient = new LocationClient(this);//声明LocationClient类
        locationClient.registerLocationListener(myListener);//注册监听函数
        LocationStart();
        //在activity执行onResume时执行mMapView. onResume ()，实现地图生命周期管理
        mapView.onResume();
    }
    @Override
    protected void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView. onPause ()，实现地图生命周期管理
        mapView.onPause();
    }
    //在地图上显示其他位置的坐标
    private void setOtherLocation(double latiuate, double longtitute){
        Resources resources=this.getResources();
        Bitmap bitmap= BitmapFactory.decodeResource(resources,R.drawable.coordinate52);
        BitmapDescriptor otherspoint;
        otherspoint= BitmapDescriptorFactory.fromBitmap(bitmap);
        LatLng latLng=new LatLng(latiuate,longtitute);
        OverlayOptions options=new MarkerOptions()
                .position(latLng)
                .icon(otherspoint);
        mBaiduMap.addOverlay(options);
    }

    private void jinweidu(String account, Double jindu,Double weidu) {
        String sendmyMapUrl = URL_JinWei + "?phoneNumber=" + account + "&longitude=" +jindu+"&latitude="+weidu;
        Log.w(TAG, "jinweidu1: "+account+""+jindu+""+weidu );
        new MyAsyncTask().execute(sendmyMapUrl);
    }

    private void getOtherMap() {
        String MapUrl = JsonTools.CONSTURL+"zhantuoer/readEarth";
        new GetOtherTask().execute(MapUrl);
    }

    private class MyAsyncTask extends AsyncTask<String, Integer, String> {
        @Override
        protected String doInBackground(String... params) {
            if (params.length < 1 || params[0] == null) {
                return null;
            }
            return JsonTools.send_short_help(params[0]);
        }

        //运行在UI线程中，所以可以直接操作UI元素
        @Override
        protected void onPostExecute(String s) {
            Log.w(TAG, "JinDu "+s );
        }
    }

    private class GetOtherTask extends AsyncTask<String, Void, List<Map_structor>> {

        @Override
        protected List<Map_structor> doInBackground(String... urls) {
            // 如果不存在任何 URL 或第一个 URL 为空，切勿执行请求。
            if (urls.length < 1 || urls[0] == null) {
                return null;
            }

            List<Map_structor> result = JsonTools.dituHelp(urls[0]);
            return result;
        }

        @Override
        protected void onPostExecute(List<Map_structor> data) {
            Log.w(TAG, "onPostExecute: "+data );
            if (data != null && !data.isEmpty()) {
                mBaiduMap.clear();
                for(int i = 0;i<data.size();i++){
                    Map_structor map_str = data.get(i);
                    if(map_str.getPhoneNumber().equals(user_phone)){
                        continue;
                    }
                    Log.w(TAG, "onPostExecute:map "+map_str.getPhoneNumber()+" "+ map_str.getLatitude()+" "+map_str.getLongitude());
                    setOtherLocation(map_str.getLatitude(),map_str.getLongitude());
                }
            }
        }
    }
}
