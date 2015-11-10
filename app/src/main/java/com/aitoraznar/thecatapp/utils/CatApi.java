package com.aitoraznar.thecatapp.utils;

import com.aitoraznar.thecatapp.models.catapi.CatApiCategoryListModel;
import com.aitoraznar.thecatapp.models.catapi.CatApiResponseModel;
import com.aitoraznar.thecatapp.models.catapi.FavouriteListResponseModel;
import com.aitoraznar.thecatapp.models.catapi.FavouriteResponseModel;

import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Query;


/**
 * @author aitor aznar
 * @since 27/09/15
 */
public interface CatApi {

    @GET("/categories/list")
    void getCategoryList(Callback<CatApiCategoryListModel> response);

    @GET("/images/get?format=xml")
    void getRandomCat(@Query("api_key") String catApiKey,
                      Callback<CatApiResponseModel> response);

    @GET("/images/get?format=xml")
    void getCat(@Query("api_key") String catApiKey,
                @Query("image_id") String imageId,
                      Callback<CatApiResponseModel> response);

    @GET("/images/get?format=xml")
    void getCatFromCategory(@Query("api_key") String catApiKey,
                      @Query("category") Integer category,
                      Callback<CatApiResponseModel> response);

    @GET("/images/getfavourites")
    void getFavourites(@Query("api_key") String catApiKey,
                      @Query("sub_id") String userId,
                      Callback<FavouriteListResponseModel> response);

    @GET("/images/favourite")
    void addFavourite(@Query("api_key") String catApiKey,
                            @Query("sub_id") String userId,
                            @Query("image_id") String imageId,
                            Callback<FavouriteResponseModel> response);

    @GET("/images/favourite?action=remove")
    void removeFavourite(@Query("api_key") String catApiKey,
                      @Query("sub_id") String userId,
                      @Query("image_id") String imageId,
                      Callback<FavouriteResponseModel> response);

}