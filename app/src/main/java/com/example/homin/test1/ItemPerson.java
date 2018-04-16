package com.example.homin.test1;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;


    //사람의 대한 마커 클러스터아이템
    public class ItemPerson implements ClusterItem {


        private final LatLng mPosition;
        private String title;


        public ItemPerson(double lat, double lng, String title) {
            mPosition = new LatLng(lat, lng);
            this.title = title;

        }

        public String getTitle() {
            return title;
        }

        @Override
        public LatLng getPosition() {
            return mPosition;
        }
    }

