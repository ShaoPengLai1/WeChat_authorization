package com.hdzb.wechat_authorization.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.hdzb.wechat_authorization.R;
import com.hdzb.wechat_authorization.api.Apis;
import com.hdzb.wechat_authorization.base.BaseFragment;
import com.hdzb.wechat_authorization.entity.AddressBean;
import com.hdzb.wechat_authorization.mvp.ContractEntity;
import com.hdzb.wechat_authorization.mvp.presenter.IPresenterImpl;
import com.hdzb.wechat_authorization.network.NetUtil;
import com.zaaach.citypicker.CityPicker;
import com.zaaach.citypicker.adapter.OnPickListener;
import com.zaaach.citypicker.model.City;
import com.zaaach.citypicker.model.HotCity;
import com.zaaach.citypicker.model.LocateState;
import com.zaaach.citypicker.model.LocatedCity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class HomeFragment extends BaseFragment implements ContractEntity.IView {
    @BindView(R.id.loaction)
    TextView loaction;
    Unbinder unbinder;
    //声明mlocationClient对象
    public AMapLocationClient mlocationClient;
    //声明mLocationOption对象
    public AMapLocationClientOption mLocationOption = null;
    private String city;
    private String adCode;
    private String cityCode;
    private String province;
    private List<HotCity> hotCities;
    private double longitude;
    private double latitude;
    private String cityName;
    private IPresenterImpl iPresenter;


    @Override
    protected void initData() {
        SharedPreferences location = getActivity().getSharedPreferences("Location", Context.MODE_PRIVATE);
        final String city = location.getString("city", null);
        if(city !=null){
            loaction.setText(city);
        }
        //开始定位，这里模拟一下定位
        mlocationClient = new AMapLocationClient(getActivity());
        //设置定位监听
        mlocationClient.setLocationListener(aMapLocation -> {
            if(aMapLocation!=null){
                if(aMapLocation.getErrorCode() == 0){
                    //获取纬度
                    latitude = aMapLocation.getLatitude();
                    //获取经度
                    longitude = aMapLocation.getLongitude();
                    //城市信息
                    HomeFragment.this.city = aMapLocation.getCity();
                    //省信息
                    province = aMapLocation.getProvince();
                    //城市编码
                    cityCode = aMapLocation.getCityCode();
                    //地区编码
                    adCode = aMapLocation.getAdCode();
                    CityPicker.from(getActivity()).locateComplete(new LocatedCity(HomeFragment.this.city, province, cityCode), LocateState.SUCCESS);
                }
            }
        });
        //初始化定位参数
        mLocationOption = new AMapLocationClientOption();
        //设置定位模式为高精度模式，Battery_Saving为低功耗模式，Device_Sensors是仅设备模式
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        //设置定位间隔,单位毫秒,默认为2000ms
        mLocationOption.setInterval(2000);
        //设置定位参数
        mlocationClient.setLocationOption(mLocationOption);
        // 此方法为每隔固定时间会发起一次定位请求，为了减少电量消耗或网络流量消耗，
        // 注意设置合适的定位时间的间隔（最小间隔支持为1000ms），并且在合适时间调用stopLocation()方法来取消定位请求
        // 在定位结束后，在合适的生命周期调用onDestroy()方法
        // 在单次定位情况下，定位无论成功与否，都无需调用stopLocation()方法移除请求，定位sdk内部会移除
        //启动定位
        boolean b = NetUtil.hasNetwork(getActivity());
        if(b){
            mlocationClient.startLocation();
        }
        loaction.setOnClickListener(v -> {
            hotCities = new ArrayList<>();
            hotCities.add(new HotCity("北京", "北京", "101010100"));
            hotCities.add(new HotCity("上海", "上海", "101020100"));
            hotCities.add(new HotCity("广州", "广东", "101280101"));
            hotCities.add(new HotCity("深圳", "广东", "101280601"));
            hotCities.add(new HotCity("杭州", "浙江", "101210101"));

            CityPicker.from(getActivity())

                    .setLocatedCity(null)
                    .setHotCities(hotCities)
                    .setOnPickListener(new OnPickListener() {

                        @Override
                        public void onPick(int position, City data) {
                            cityName = data.getName();
                            iPresenter.requestGet(String.format(Apis.URL_LOCATION_GET,data.getName()), AddressBean.class);
                        }

                        @Override
                        public void onCancel() {
                            Toast.makeText(getActivity(),"取消选中",Toast.LENGTH_LONG).show();
                        }

                        @Override
                        public void onLocate() {
                            mlocationClient.stopLocation();
                            new Handler().postDelayed(() ->
                                    CityPicker.from(getActivity()).locateComplete(new LocatedCity(HomeFragment.this.city, province, cityCode), LocateState.SUCCESS), 1000);

                        }
                    })
                    .show();
        });
    }

    @Override
    protected void initView(View view) {
        unbinder = ButterKnife.bind(this, view);
        iPresenter=new IPresenterImpl(this);
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_home;
    }



    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void getDataSuccess(Object data) {
        if(data instanceof AddressBean){
            AddressBean object1 = (AddressBean) data;
            AddressBean.ResultBean result = object1.getResult();
            double lat = result.getLocation().getLat();
            double lng = result.getLocation().getLng();
            loaction.setText(cityName);
            SharedPreferences.Editor location = getActivity().getSharedPreferences("Location", Context.MODE_PRIVATE).edit();
            location.putString("latitude",lat+"");
            location.putString("longitude",lng+"");
            location.putString("city",cityName);
            location.commit();
        }
    }

    @Override
    public void getDataFail(String error) {
        Toast.makeText(getActivity(),error,Toast.LENGTH_LONG).show();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        iPresenter.onDetach();
    }
}
