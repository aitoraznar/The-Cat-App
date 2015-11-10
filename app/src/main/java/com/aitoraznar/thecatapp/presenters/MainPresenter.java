package com.aitoraznar.thecatapp.presenters;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.aitoraznar.thecatapp.R;
import com.aitoraznar.thecatapp.models.catapi.CatApiResponseModel;
import com.aitoraznar.thecatapp.models.catapi.CatModel;
import com.aitoraznar.thecatapp.models.catapi.FavouriteResponseModel;
import com.aitoraznar.thecatapp.utils.CatApi;
import com.aitoraznar.thecatapp.views.IHomeView;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.converter.SimpleXMLConverter;


/**
 * @author aitor aznar
 * @since 28/09/15
 */
public class MainPresenter {
    private IHomeView view;

    public MainPresenter(IHomeView mainView) {
        this.view = mainView;
    }

    public void getRandomCat(Context context) {
        // Use retrofit to get data
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(context.getString(R.string.catApiBaseUrl))
                .setConverter(new SimpleXMLConverter())
                .build();

        CatApi catapi = restAdapter.create(CatApi.class);
        catapi.getRandomCat(context.getString(R.string.catApiKey), new Callback<CatApiResponseModel>() {
            @Override
            public void success(CatApiResponseModel responseModel, Response response) {
                // Set the model
                if (responseModel != null) {
                    CatModel cat = responseModel.getData().getCats().get(0);
                    // Call view method to set the name list in the listView
                    view.setCat(cat);
                }
            }

            @Override
            public void failure(RetrofitError error) {
                Log.e("Error parsing XML", error.getMessage());
                // Call view method to show a toast with an error message
                view.showErrorToast();
            }
        });
    }

    public void getCat(Context context, final CatModel cat) {
        // Use retrofit to get data
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(context.getString(R.string.catApiBaseUrl))
                .setConverter(new SimpleXMLConverter())
                .build();

        CatApi catapi = restAdapter.create(CatApi.class);
        catapi.getCat(context.getString(R.string.catApiKey),
                cat.getId(),
                new Callback<CatApiResponseModel>() {
            @Override
            public void success(CatApiResponseModel responseModel, Response response) {
                // Set the model
                if (responseModel != null) {
                    CatModel cat = responseModel.getData().getCats().get(0);
                    // Call view method to set the name list in the listView
                    view.setCat(cat);
                }
            }

            @Override
            public void failure(RetrofitError error) {
                Log.e("Error parsing XML", error.getMessage());
                // Call view method to show a toast with an error message
                view.showErrorToast();
            }
        });
    }

    public void addFavourite(Context context, final CatModel cat) {
        SharedPreferences prefs = context.getSharedPreferences("AppConfig", Context.MODE_PRIVATE);
        String android_id = prefs.getString("android_id", "thecatappid");

        // Use retrofit to get data
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(context.getString(R.string.catApiBaseUrl))
                .setConverter(new SimpleXMLConverter())
                .build();

        CatApi catapi = restAdapter.create(CatApi.class);
        catapi.addFavourite(
                context.getString(R.string.catApiKey),
                android_id,
                cat.getId(),
                new Callback<FavouriteResponseModel>() {
                    @Override
                    public void success(FavouriteResponseModel responseModel, Response response) {
                        // Set the model
                        if (responseModel != null) {
                            // Call view method to set the name list in the listView
                            view.addFavourite(cat);
                        }
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        Log.e("Error parsing XML", error.getMessage());
                        // Call view method to show a toast with an error message
                        view.showErrorToast();
                    }
                });

    }

    public void removeFavourite(Context context, final CatModel cat) {
        SharedPreferences prefs = context.getSharedPreferences("AppConfig", Context.MODE_PRIVATE);
        String android_id = prefs.getString("android_id", "thecatappid");

        // Use retrofit to get data
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(context.getString(R.string.catApiBaseUrl))
                .setConverter(new SimpleXMLConverter())
                .build();

        CatApi catapi = restAdapter.create(CatApi.class);
        catapi.removeFavourite(
                context.getString(R.string.catApiKey),
                android_id,
                cat.getId(),
                new Callback<FavouriteResponseModel>() {
                    @Override
                    public void success(FavouriteResponseModel responseModel, Response response) {
                        // Set the model
                        if (responseModel != null) {
                            // Call view method to set the name list in the listView
                            view.removeFavourite(cat);
                        }
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        Log.e("Error parsing XML", error.getMessage());
                        // Call view method to show a toast with an error message
                        view.showErrorToast();
                    }
                });

    }

}