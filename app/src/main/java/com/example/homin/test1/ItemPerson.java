package com.example.homin.test1;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;


    //사람의 대한 마커 클러스터아이템
    public class ItemPerson implements ClusterItem {


        private final LatLng mPosition;
        private String userId;


        public ItemPerson(double lat, double lng, String id) {
            mPosition = new LatLng(lat, lng);
            this.userId = id;

        }

        public String getTitle() {
            return userId;
        }

        @Override
        public LatLng getPosition() {
            return mPosition;
        }
    }

