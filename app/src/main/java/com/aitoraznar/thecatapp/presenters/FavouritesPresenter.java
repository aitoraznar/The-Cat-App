package com.aitoraznar.thecatapp.presenters;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.aitoraznar.thecatapp.R;
import com.aitoraznar.thecatapp.models.FavouriteModel;
import com.aitoraznar.thecatapp.models.catapi.FavouriteListResponseModel;
import com.aitoraznar.thecatapp.utils.CatApi;
import com.aitoraznar.thecatapp.views.IFavouritesView;

import java.util.List;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.converter.SimpleXMLConverter;


/**
 * @author aitor aznar
 * @since 28/09/15
 */
public class FavouritesPresenter {
    private IFavouritesView view;

    public FavouritesPresenter(IFavouritesView mainView) {
        this.view = mainView;
    }

    public void getFavourites(Context context) {
        SharedPreferences prefs = context.getSharedPreferences("AppConfig", Context.MODE_PRIVATE);
        String android_id = prefs.getString("android_id", "thecatappid");

        // Use retrofit to get data
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(context.getString(R.string.catApiBaseUrl))
                .setConverter(new SimpleXMLConverter())
                .build();

        CatApi catapi = restAdapter.create(CatApi.class);

        catapi.getFavourites(
                context.getString(R.string.catApiKey),
                android_id,
                new Callback<FavouriteListResponseModel>() {
                    @Override
                    public void success(FavouriteListResponseModel responseModel, Response response) {
                        // Set the model
                        if (responseModel != null) {
                            List<FavouriteModel> favs = responseModel.getData().getFavourites();
                            // Call view method to set the name list in the listView
                            view.setFavourites(favs);
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