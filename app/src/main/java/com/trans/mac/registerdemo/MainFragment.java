package com.trans.mac.registerdemo;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;


public class MainFragment extends AppCompatActivity {

    TextView tab1, tab2;
    fragment1 fragment1;
    ViewPager pager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_fragment);

        tab1 = findViewById(R.id.tab1);
        tab2 = findViewById(R.id.tab2);
        pager = findViewById(R.id.container);
        fragment1 = new fragment1();

        pager.setAdapter(new PagerAdapterClass(getSupportFragmentManager()));

       // FragmentManager manager= getSupportFragmentManager();
       // FragmentTransaction transaction = manager.beginTransaction();
       // transaction.replace(R.id.container, fragment1);
        //transaction.commit();
    }

    public void tabclick(View view){
        if(view.getId()==R.id.tab1){
            pager.setCurrentItem(0);
        }else{
            pager.setCurrentItem(1);
        }

    }

    public  class PagerAdapterClass extends FragmentPagerAdapter {


        public PagerAdapterClass(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int i) {

            if (i==0 ){
                return new fragment1();
            }
            return new fragment2();
        }

        @Override
        public int getCount() {
            return 2;
        }
    }

    public  void changeTab(View view){

        if(view.getId()==R.id.tab1){
            getSupportFragmentManager().beginTransaction().replace(R.id.container,new fragment1()).commit();

        }else{

            getSupportFragmentManager().beginTransaction().replace(R.id.container,new fragment2()).commit();


        }
    }
}
