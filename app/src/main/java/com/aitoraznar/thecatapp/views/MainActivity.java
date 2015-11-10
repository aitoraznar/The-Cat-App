package com.aitoraznar.thecatapp.views;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.aitoraznar.thecatapp.di.MVPApplication;
import com.aitoraznar.thecatapp.R;
import com.aitoraznar.thecatapp.models.FavouriteModel;
import com.aitoraznar.thecatapp.models.catapi.CatModel;
import com.aitoraznar.thecatapp.models.catapi.CategoryModel;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;

/**
 * @author aitor aznar
 * @since 28/09/15
 */
public class MainActivity extends AppCompatActivity implements CategoryMenuFragment.FragmentDrawerListener, FavouritesFragment.FavouritesFragmentListener {
    private Toolbar mToolbar;
    private CategoryMenuFragment drawerFragment;

    // Google AdMob
    private AdView mAdView;
    private InterstitialAd mInterstitialAd;
    private boolean isTimeForFullScreenBanner = false;
    private static int numCatsViewed = 0;

    private enum MenuViews {
        HOME(0),
        FAVOURITES(1),
        CAT_CATEGORY(2);

        private final int value;
        MenuViews(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }

        public static MenuViews fromInt(int i) {
            for (MenuViews b : MenuViews .values()) {
                if (b.getValue() == i) { return b; }
            }
            return null;
        }

        public static MenuViews fromString(String i) {
            for (MenuViews b : MenuViews.values()) {
                if (b.toString().equals(i.toUpperCase())) { return b; }
            }
            return null;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Two ways to inject with dagger. The next is one of them, just using the method you want:
        //      ((MVPApplication) getApplication()).getComponent().context());.
        // The other way is put the object as attribute annotated with @Inject
        //      and in the onCreate method type:
        //      ((MVPApplication) getApplication()).getComponent().inject(this); and then you
        //      can use the object injected.
        // There's a third way that is using constructor injection by annotating your constructor
        //      with @Inject, and letting Dagger create your class for you.
        if(getApplicationContext().
                equals(((MVPApplication) getApplication()).getComponent().context())) {
            Log.d("DAGGER", "Correct Injection of Application context, the first way");
        }
        else {
            Log.d("DAGGER", "Incorrect Injection, the first way");
        }
        int count = ((MVPApplication) getApplication()).getComponent().prefs().getInt("Count", 0);
        count++;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
            ((MVPApplication) getApplication()).getComponent().prefs()
                    .edit().putInt("Count", count).apply();
        }
        else {
            ((MVPApplication) getApplication()).getComponent().prefs()
                    .edit().putInt("Count", count).commit();
        }


        // Preferences
        SharedPreferences prefs = getSharedPreferences("AppConfig", Context.MODE_PRIVATE);
        String android_id = Settings.Secure.getString(this.getContentResolver(), Settings.Secure.ANDROID_ID);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("android_id", android_id);
        editor.commit();

        // Toolbar
        mToolbar = (Toolbar) findViewById(R.id.toolbar);

        // Menu
        drawerFragment = (CategoryMenuFragment)
                getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);
        drawerFragment.setUp(R.id.fragment_navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout), mToolbar);
        drawerFragment.setDrawerListener(this);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        // Google AdMod
        mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice("D4EC986EC9A617FF994DF03CEE3C2A08") // Test Nexus 5
                .build();
        mAdView.loadAd(adRequest);

        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId(getResources().getString(R.string.fullscreen_banner_ad_unit_id));

        mInterstitialAd.setAdListener(new AdListener() {
           public void onAdLoaded() {
               isTimeForFullScreenBanner = numCatsViewed % getResources().getInteger(R.integer.catForFullScreenBanner) == 0;
               // Check for Full Screen Ad
               if (mInterstitialAd.isLoaded() && isTimeForFullScreenBanner) {
                   mInterstitialAd.show();
               }

           }
        });

        // Increment cat viewed number
        ++numCatsViewed;
        isTimeForFullScreenBanner =  numCatsViewed % getResources().getInteger(R.integer.catForFullScreenBanner) == 0;
        if (isTimeForFullScreenBanner) {
            requestNewInterstitial();
        }

        // Begin seeing things
        displayView(MenuViews.HOME, null, null);
    }

    // Menu Toolbar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        // Opens he menu when we click the menu hamburger
        if (id == android.R.id.home) {
            DrawerLayout mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
            mDrawerLayout.openDrawer(GravityCompat.START);
            return true;
        }

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        if(id == R.id.action_search) {
            //Toast.makeText(getApplicationContext(), "Search action is selected!", Toast.LENGTH_SHORT).show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDrawerItemSelected(View view, CategoryModel category) {
        Log.i("MENU!!!", category.toString());
        if (category.getId() != null && !category.getId().equals("")) {
            displayView(MenuViews.CAT_CATEGORY, null, category);
        } else {
            displayView(MenuViews.fromString(category.getName()), null, null);
        }
    }

    @Override
    public void onFavouriteSelected(View view, FavouriteModel favouriteModel) {
        displayView(MenuViews.HOME, new CatModel(favouriteModel.getId(), "favourite"), null);
    }

    private void displayView(MenuViews menuView, CatModel cat, CategoryModel category) {
        Fragment fragment = null;
        String title = getString(R.string.app_name);

        Log.i("MENU!!!", menuView.toString());
        if (menuView == MenuViews.HOME) {
            fragment = new HomeFragment();

            if (cat != null) {
                Bundle args = new Bundle();

                args.putString("catId", cat.getId());
                args.putBoolean("favourite", cat.getUrl().equals("favourite"));

                fragment.setArguments(args);
            }

            title = getString(R.string.app_name);
        }

        if (menuView == MenuViews.FAVOURITES) {
            fragment = new FavouritesFragment();
            ((FavouritesFragment) fragment).setFavouriteListener(this);

            title = getString(R.string.title_favourites);
        }

        if (menuView == MenuViews.CAT_CATEGORY) {
            fragment = new CategoryFragment();
            ((CategoryFragment) fragment).setCategory(category);
            title = category.getName().toUpperCase();
        }

        loadFragment(fragment, title);
    }

    public void loadFragment(Fragment fragment, String title) {
        if (fragment != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.container_body, fragment);
            fragmentTransaction.commit();

            // set the toolbar title
            getSupportActionBar().setTitle(title);
        }
    }

    private void requestNewInterstitial() {
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice("D4EC986EC9A617FF994DF03CEE3C2A08") // Test Nexus 5
                .build();

        mInterstitialAd.loadAd(adRequest);
    }

}