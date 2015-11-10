package com.aitoraznar.thecatapp.presenters;

import android.content.Context;
import android.util.Log;

import com.aitoraznar.thecatapp.R;
import com.aitoraznar.thecatapp.models.catapi.CatApiCategoryListModel;
import com.aitoraznar.thecatapp.models.catapi.CategoryModel;
import com.aitoraznar.thecatapp.utils.CatApi;
import com.aitoraznar.thecatapp.views.ICategoryMenuView;

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
public class CategoryMenuPresenter {
    private ICategoryMenuView view;

    public CategoryMenuPresenter(ICategoryMenuView mainView) {
        this.view = mainView;
    }

    public void getCategoryList(Context context) {
        // Use retrofit to get data
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(context.getString(R.string.catApiBaseUrl))
                .setConverter(new SimpleXMLConverter())
                .build();

        CatApi catapi = restAdapter.create(CatApi.class);
        catapi.getCategoryList(new Callback<CatApiCategoryListModel>() {
            @Override
            public void success(CatApiCategoryListModel responseModel, Response response) {
                // Set the model
                if(responseModel != null) {
                    List<CategoryModel> categories = responseModel.getData().getCategories();
                    
                    view.setCategories(categories);
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