package com.example.propiedadesguitarra2;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.viewpager.widget.ViewPager;

public class MainActivity extends AppCompatActivity {

    private StateManager stateManager;
    private MenuItem prevMenuItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView navView = findViewById(R.id.nav_view);

        ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(new ViewPagerAdapter(getSupportFragmentManager()));

        navView.setOnNavigationItemSelectedListener(
            item -> {
                switch (item.getItemId()) {
                    case R.id.navigation_propiedades_cuerda:
                        viewPager.setCurrentItem(0);
                        break;
                    case R.id.navigation_propiedades:
                        viewPager.setCurrentItem(1);
                        break;
                    case R.id.navigation_configuraciones:
                        viewPager.setCurrentItem(2);
                        break;
                    case R.id.navigation_cargar_guardar:
                        viewPager.setCurrentItem(3);
                        break;
                }
                return false;
            });

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

            @Override
            public void onPageSelected(int position) {
                if (prevMenuItem != null) {
                    prevMenuItem.setChecked(false);
                }
                else
                {
                    navView.getMenu().getItem(0).setChecked(false);
                }
                navView.getMenu().getItem(position).setChecked(true);
                prevMenuItem = navView.getMenu().getItem(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {}
        });

        // Creo StateManager para utlizar en toda la APP
        stateManager = StateManager.get(this);
    }
}
