package com.aitoraznar.thecatapp.di.components;

import android.content.Context;
import android.content.SharedPreferences;

import com.aitoraznar.thecatapp.di.modules.AppModule;
import com.aitoraznar.thecatapp.views.MainActivity;

import javax.inject.Singleton;

import dagger.Component;


/**
 * @author mariobama
 * @since 7/09/15
 */
@Singleton @Component(
        modules = {AppModule.class}
)

public interface AppComponent {

    void inject(MainActivity mainActivity);

    Context context();

    SharedPreferences prefs();

}