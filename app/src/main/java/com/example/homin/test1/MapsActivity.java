package com.example.homin.test1;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.Icon;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.PersistableBundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.AttributeSet;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMapOptions;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StreamDownloadTask;
import com.google.maps.android.clustering.Cluster;
import com.google.maps.android.clustering.ClusterItem;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.clustering.algo.GridBasedAlgorithm;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static com.example.homin.test1.WriteActivity.*;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private Context context;
    private MapView view;
    private static final int RESULT_CODE = 20;
    private LatLng addMakerLocation;
    private String email;
    private LinearLayout actionLayout;
    private BottomSheetBehavior bottomSheetBehavior;
    private View bottomview;
    private Menu mMenu;
    private FloatingActionButton actionButton;
    private DatabaseReference reference;
    private List<String> myFriendList;
    private LocationManager locationManager;
    private LocationListener locationListener;
    private LatLng myLatLng;
    private ItemPerson myMarker;
    private ClusterManager<ClusterItem> clusterManager;
    private List<Contact> myFriendContactList;
    private List<Contact> contactList;
    private boolean check;
    private List<Double> friendLocation;
    private Contact yourContact;
    private int count;
    private Location location;


    //자기위치로 되돌리는 버튼
    private FloatingActionButton selfLocationButton;


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    FragmentManager manager = getSupportFragmentManager();
                    FragmentTransaction transaction = manager.beginTransaction();
                    FriendFragment friendFragment = new FriendFragment();
                    transaction.replace(R.id.container_main, friendFragment);
                    transaction.commit();
                    return true;
                case R.id.navigation_dashboard:
                    FragmentManager manager1 = getSupportFragmentManager();
                    FragmentTransaction transaction1 = manager1.beginTransaction();
                    ChatListFragment chatListFragment = new ChatListFragment();
                    transaction1.replace(R.id.container_main, chatListFragment);
                    transaction1.commit();
                    return true;
                case R.id.navigation_notifications:
                    FragmentManager manager2 = getSupportFragmentManager();
                    FragmentTransaction transaction2 = manager2.beginTransaction();
                    MypageFragment mypageFragment = new MypageFragment();
                    transaction2.replace(R.id.container_main, mypageFragment);
                    transaction2.commit();
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        myFriendContactList = new ArrayList<>();
        setContentView(R.layout.activity_maps);
        //자기위치찾아주는 버튼 찾기
        selfLocationButton = findViewById(R.id.selfLocationIdentifier);
        selfLocationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(myLatLng,16));

            }
        });

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        BottomNavigationView navigationView = findViewById(R.id.navigation);
        navigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        actionButton = findViewById(R.id.floatingActionButton);

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        FriendFragment friendFragment = new FriendFragment();
        transaction.replace(R.id.container_main, friendFragment);
        transaction.commit();

        registerForContextMenu(findViewById(R.id.map));

        bottomview = findViewById(R.id.bottom_sheet);
        actionLayout = findViewById(R.id.action_sheet);
        bottomSheetBehavior = BottomSheetBehavior.from(bottomview);
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        bottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                if (newState == BottomSheetBehavior.STATE_COLLAPSED) {
                    actionLayout.setVisibility(View.VISIBLE);

                }
                if (newState == BottomSheetBehavior.STATE_EXPANDED) {
                    actionLayout.setVisibility(View.GONE);
                }

                if (newState == BottomSheetBehavior.STATE_DRAGGING) {
                    actionLayout.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {

            }
        });


    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(final GoogleMap googleMap) {

        email = DaoImple.getInstance().getLoginEmail();
        mMap = googleMap;
        reference = FirebaseDatabase.getInstance().getReference();
        if(clusterManager == null){
            clusterManager = new ClusterManager<>(MapsActivity.this,mMap);
            mMap.setOnCameraIdleListener(clusterManager);
        }
        Log.i("gg6","클러스터 설정");
        Log.i("asd123","onMapReady");
        myLocationUpdate(); // 내 위치 업데이트

        getFriendList(); // 친구 목록 가져오기
        showFriendsOnMap();


        actionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(MapsActivity.this, WatingActivity.class);
                startActivity(intent1);
            }
        });

        // 마커 클릭 리스너 등록
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
//                if(marker.getTitle().equals(DaoImple.getInstance().getLoginId())){
//                    openOptionsMenu();
//                }
//                Log.i("ff",marker.getTitle());

                return false;
            }
        });

        if(clusterManager != null) {
            clusterManager.cluster();
        }
    }


    private void showFriendsOnMap(){

        // 친구 위치정보 받아오기
        reference.child("Contact").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Log.i("ggg2","에드 들어옴");
                if(myFriendList != null){
                    Contact contact = dataSnapshot.getValue(Contact.class);
                    for(int a = 0 ; a < myFriendList.size() ; a++){
                        if(myFriendList.get(a).equals(contact.getUserId())){
                            if(clusterManager == null){
                                clusterManager = new ClusterManager<>(MapsActivity.this,mMap);
                                clusterManager.setRenderer(new PersonItemRenderer(MapsActivity.this, mMap, clusterManager));
                                clusterManager.setAlgorithm(new GridBasedAlgorithm<ClusterItem>());
                                mMap.setOnCameraIdleListener(clusterManager);
                            }
                            if(!(check)){
                                clusterManager.clearItems();
                                check = true;
                                Log.i("ggg2",check+"");
                            }
                            // 친구들 위치정보 받아와서 구글맵에 갱신
                            friendLocation = contact.getUserLocation();

                            ItemPerson friendMarker = new ItemPerson(friendLocation.get(0),
                                    friendLocation.get(1),contact.getUserName(),contact.getPictureUrl());

                            clusterManager.addItem(friendMarker);
                            Log.i("ggg2",contact.getUserId());
                            Log.i("ggg2",friendLocation.get(0) +" "+friendLocation.get(1));
//                            clusterManager.addItem(friendMarker);
                            if(a == myFriendList.size() - 1){
                                Log.i("vvvv1", "갱신 들어옴");
                                clusterManager.cluster();
                            }

                        }

                    }
                }
            }
            // 친구 위치 바뀌었을때 정보 갱신
            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                check = false;
                Log.i("ggg2","체인지 들어옴");
                reference.child("Contact").addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        if(myFriendList != null){
                            Contact contact = dataSnapshot.getValue(Contact.class);
                            for(int a = 0 ; a < myFriendList.size() ; a++){
                                if(myFriendList.get(a).equals(contact.getUserId())){
                                    if(clusterManager == null){
                                        clusterManager = new ClusterManager<>(MapsActivity.this,mMap);
                                        clusterManager.setRenderer(new PersonItemRenderer(MapsActivity.this, mMap, clusterManager));
                                        clusterManager.setAlgorithm(new GridBasedAlgorithm<ClusterItem>());
                                        mMap.setOnCameraIdleListener(clusterManager);
                                    }
                                    if(!(check)){
                                        clusterManager.clearItems();
                                        check = true;
                                        Log.i("ggg2",check+"");
                                    }
                                    // 친구들 위치정보 받아와서 구글맵에 갱신
                                    yourContact = contact;
                                    friendLocation = contact.getUserLocation();
                                    int count = a;
                                    // 친구들 프로필 사진 가져오기
                                    Log.i("vvvv","프로필 가져옴");
                                    ItemPerson friendMarker = new ItemPerson(friendLocation.get(0),
                                            friendLocation.get(1),yourContact.getUserName(),contact.getPictureUrl());
                                    Log.i("vvvv1","이미지 로딩 완료");
                                    clusterManager.addItem(friendMarker);
                                    clusterManager.cluster();
                                    Log.i("ggg2",contact.getUserId());
                                    Log.i("ggg2",friendLocation.get(0) +" "+friendLocation.get(1));
//                            clusterManager.addItem(friendMarker);
                                    if(a == myFriendList.size() - 1){
                                        Log.i("vvvv1", "갱신 들어옴");
                                        clusterManager.cluster();
                                    }

                                }

                            }
                        }
                    }

                    @Override
                    public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                    }

                    @Override
                    public void onChildRemoved(DataSnapshot dataSnapshot) {

                    }

                    @Override
                    public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        if(clusterManager != null) {
            clusterManager.cluster();
        }
    }



    // 내 친구 리스트 받아오기
    private void getFriendList() {
        contactList = new ArrayList<>();
        reference.child("Contact").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Contact contact = dataSnapshot.getValue(Contact.class);
                contactList.add(contact);
                if(contact.getUserId().equals(DaoImple.getInstance().getLoginEmail())){
                    if(contact.getFriendList() != null) {
                        myFriendList = contact.getFriendList();

                    }
                }

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

        });


    }

    // 내 gps 위치 받아오고, firebase에 contact 업데이트
    @SuppressLint("MissingPermission")
    private void myLocationUpdate() {
        Log.i("asd123","myLocationUpdate");
        if (locationManager == null) {
            locationManager = (LocationManager) this.getSystemService(context.LOCATION_SERVICE);

        }
            locationListener = new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {
                    if(location != null) {
                        Log.i("asd123", "리스너 들어옴");
                        Log.i("asd123", location.getProvider());
                        if (clusterManager == null) {
                            clusterManager = new ClusterManager<>(MapsActivity.this, mMap);
                            mMap.setOnCameraIdleListener(clusterManager);

                        }
                        if (myMarker != null) {
                            clusterManager.removeItem(myMarker);
                        }

                        // 파이어베이스에 내 gps 정보 업데이트
                        if (DaoImple.getInstance().getContact() != null) {
                            Contact myContact = DaoImple.getInstance().getContact();
                            List<Double> myLocation = new ArrayList<>();
                            myLocation.add(location.getLatitude());
                            myLocation.add(location.getLongitude());
                            myContact.setUserLocation(myLocation);
                            reference.child("Contact").child(DaoImple.getInstance().getKey()).setValue(myContact);
                            Log.i("ggg2", "mycontact 가져옴");

                        }

                        Log.i("ggg3", DaoImple.getInstance().getKey());

                        // ClusterManagerItmes 이미지 추가/사이즈 줄이기

                        myLatLng = new LatLng(location.getLatitude(), location.getLongitude());
                        Picasso.get().load(DaoImple.getInstance().getContact().getPictureUrl()).resize(250,250)
                                .centerInside().into(new Target() {
                            @Override
                            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                                myMarker = new ItemPerson(myLatLng.latitude,myLatLng.longitude,
                                        DaoImple.getInstance().getLoginId(),DaoImple.getInstance().getContact().getPictureUrl());
                                clusterManager.addItem(myMarker);
                                clusterManager.cluster();
                                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(myLatLng, 16));
                                Log.i("vvvv1","내 마커 생성");
                            }

                            @Override
                            public void onBitmapFailed(Exception e, Drawable errorDrawable) {

                            }

                            @Override
                            public void onPrepareLoad(Drawable placeHolderDrawable) {

                            }
                        });


                        clusterManager.cluster();

                        Log.i("ggg2", "내 클러스터 생성");
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(myLatLng, 16));
                    }

                }

                @Override
                public void onStatusChanged(String provider, int status, Bundle extras) {

                }

                @Override
                public void onProviderEnabled(String provider) {

                }

                @Override
                public void onProviderDisabled(String provider) {

                }
            };
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 100, locationListener);
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,0,100,locationListener);



    }


    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        menu.add(0, 1, 0, "글 남기기");
        menu.add(0, 2, 0, "테스트1");
        menu.add(0, 3, 0, "테스트2");
        super.onCreateContextMenu(menu, v, menuInfo);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case 1:
                Intent intent = new Intent(this, WriteActivity.class);
                startActivityForResult(intent, RESULT_CODE);
                break;
        }
        return super.onContextItemSelected(item);
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.i("gg", requestCode + " " + resultCode);
        if (requestCode == RESULT_CODE && resultCode == RESULT_OK) {
            String title = data.getStringExtra(TITLE_KEY);
            String body = data.getStringExtra(BODY_KEY);
            Log.i("gg", title + body);
            if (!(title.equals("")) && !(body.equals(""))) {
                mMap.addMarker(new MarkerOptions().position(addMakerLocation).title(title).snippet(body)).showInfoWindow();
            }
        }
        if(requestCode == 400 && requestCode == RESULT_OK){
            boolean check = data.getBooleanExtra("check",false);
            if(check){
                actionButton.setImageResource(R.drawable.ddww);
            }else{
                actionButton.setImageResource(R.drawable.ic_notifications_black_24dp);
            }
        }

    }





}
