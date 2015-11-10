package com.aitoraznar.thecatapp.views;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.aitoraznar.thecatapp.R;
import com.aitoraznar.thecatapp.adapters.FavouriteListAdapter;
import com.aitoraznar.thecatapp.di.MVPApplication;
import com.aitoraznar.thecatapp.models.FavouriteModel;
import com.aitoraznar.thecatapp.models.catapi.CatModel;
import com.aitoraznar.thecatapp.models.catapi.CategoryModel;
import com.aitoraznar.thecatapp.presenters.FavouritesPresenter;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by aitor on 28/09/15.
 */
public class FavouritesFragment extends Fragment implements IFavouritesView, IHomeView, FavouriteListAdapter.FavoritesIface {

    protected FavouritesPresenter presenter;
    private FavouritesFragmentListener favListener;
    private Tracker tracker;

    protected @Bind(R.id.favouriteList) ListView favouriteList;

    private ProgressDialog pd;

    public FavouritesFragment() {
        // Required empty public constructor
    }

    public void setFavouriteListener(FavouritesFragmentListener listener) {
        this.favListener = listener;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        tracker = ((MVPApplication) getActivity().getApplication()).getTracker(MVPApplication.TrackerName.APP_TRACKER);
        tracker.setScreenName("FavouriteView");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_favourites, container, false);

        // Inject views
        ButterKnife.bind(this, rootView);

        // Show a progress dialog
        pd = new ProgressDialog(getActivity());
        pd.setCancelable(false);
        pd.setMessage("Loading favourites ...");
        pd.show();

        // Set presenter
        presenter = new FavouritesPresenter(this);
        // Call presenter method to get json data from url
        presenter.getFavourites(getActivity());

        // Google analytics Screen View
        tracker.send(new HitBuilders.ScreenViewBuilder().build());

        // Inflate the layout for this fragment
        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    //Interfaces -----------------------------------------------------------------------------------

    @Override
    public void setFavourites(List<FavouriteModel> favs) {
        FavouriteListAdapter adapter = new FavouriteListAdapter(this, getActivity(), favs, this);

        favouriteList.setAdapter(adapter);

        pd.dismiss();
    }

    @Override
    public void setCat(CatModel cat) {

    }

    @Override
    public void addFavourite(CatModel cat) {
        // Do something when adding the cat to favourites

        tracker.send(new HitBuilders.EventBuilder()
                .setCategory("UX")
                .setAction("click")
                .setLabel("addFavourite " + cat.getId())
                .setCategory("AddFavourite")
                .build());
    }

    @Override
    public void removeFavourite(CatModel cat) {
        // Do something when removing the cat from favourites

        tracker.send(new HitBuilders.EventBuilder()
                .setCategory("UX")
                .setAction("click")
                .setLabel("removeFavourite " + cat.getId())
                .setCategory("removeFavourite")
                .build());
    }

    @Override
    public void showErrorToast() {
        pd.dismiss();
        Toast.makeText(getActivity(), "Error while loading content",
                Toast.LENGTH_LONG).show();
    }

    @Override
    public void delete(int position) {
        ((FavouriteListAdapter) favouriteList.getAdapter()).remove(position);
    }

    @Override
    public void onClickListener(View view, int position) {
        FavouriteModel fav = ((FavouriteListAdapter) favouriteList.getAdapter()).getItem(position);

        //
        favListener.onFavouriteSelected(view, fav);
    }

    public interface FavouritesFragmentListener {
        void onFavouriteSelected(View view, FavouriteModel favouriteModel);
    }
}
