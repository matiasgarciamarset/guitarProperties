package com.example.propiedadesguitarra2;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.propiedadesguitarra2.components.ViewPagerAdapter;
import com.example.propiedadesguitarra2.ui.cargarguardar.BluetoothService;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager.widget.ViewPager;

public class MainActivity extends AppCompatActivity {

    private MenuItem prevMenuItem;
    private View viewBt;

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

        View viewBt = (View) findViewById(R.id.bt_view);

        // Creo StateManager para utlizar en toda la APP
        StateManager.get(this).setBtHandler(new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if (msg.what == BluetoothService.CONNECTION_STATUS) {
                    switch (msg.arg1) {
                        case 3:
                            viewBt.setBackgroundColor(Color.GREEN);
                            Toast.makeText(getBaseContext(), "Conectado", Toast.LENGTH_SHORT).show();
                            // Sincronizo toda la informacion al establecer conexion
                            StateManager.get(getBaseContext()).sendAllByBluetooth();
                            Toast.makeText(getBaseContext(), "Sincronizado", Toast.LENGTH_SHORT).show();
                            break;
                        case 2:
                            viewBt.setBackgroundColor(Color.BLUE);
                            Toast.makeText(getBaseContext(), "Conectando..", Toast.LENGTH_SHORT).show();
                            break;
                        case 0:
                            viewBt.setBackgroundColor(Color.RED);
                            String text = msg.getData().getString("error");
                            if (text != null)
                                Toast.makeText(getBaseContext(), "Error: " + text, Toast.LENGTH_SHORT).show();
                            break;
                    }
                }
            }
        });
    }

}
