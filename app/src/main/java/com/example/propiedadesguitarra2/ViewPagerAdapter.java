package com.example.propiedadesguitarra2;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.propiedadesguitarra2.ui.cargarguardar.CargarGuardarFragment;
import com.example.propiedadesguitarra2.ui.configuraciones.ConfiguracionesFragment;
import com.example.propiedadesguitarra2.ui.propiedades.PropiedadesFragment;
import com.example.propiedadesguitarra2.ui.propiedadescuerda.PropiedadesCuerdaFragment;

public class ViewPagerAdapter extends FragmentPagerAdapter {
    private Fragment propiedadesCuerda = new PropiedadesCuerdaFragment();
    private Fragment propiedades = new PropiedadesFragment();
    private Fragment configuraciones = new ConfiguracionesFragment();
    private Fragment cargarGuardar = new CargarGuardarFragment();

    public ViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position)
        {
            case 0:
                return propiedadesCuerda;
            case 1:
                return propiedades;
            case 2:
                return configuraciones;
            case 3:
                return cargarGuardar;
        }
        return null;
    }

    @Override
    public int getCount() {
        return 4;
    }
}
