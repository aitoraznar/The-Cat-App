package com.aitoraznar.thecatapp.views;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.aitoraznar.thecatapp.R;
import com.aitoraznar.thecatapp.di.MVPApplication;
import com.aitoraznar.thecatapp.models.catapi.CatModel;
import com.aitoraznar.thecatapp.models.catapi.CategoryModel;
import com.aitoraznar.thecatapp.presenters.CategoryPresenter;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.squareup.picasso.Picasso;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by aitor on 28/09/15.
 */
public class CategoryFragment extends Fragment implements ICategoryView {

    private CategoryModel category;
    protected CategoryPresenter presenter;
    protected CatModel currentCat;

    protected @Bind(R.id.mCatImage) ImageView mCatImage;
    protected @Bind(R.id.mSourceText) TextView mSourceText;
    protected @Bind(R.id.mLikeButton) Button mLikeButton;
    protected @Bind(R.id.mHateButton) Button mHateButton;
    protected @Bind(R.id.mFavouriteButton) ToggleButton mFavouriteButton;
    protected @Bind(R.id.mShareButton) Button mShareButton;

    private ProgressDialog pd;
    private Tracker tracker;

    public CategoryFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        tracker = ((MVPApplication) getActivity().getApplication()).getTracker(MVPApplication.TrackerName.APP_TRACKER);
        tracker.setScreenName("CategoryView");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);

        // Inject views
        ButterKnife.bind(this, rootView);

        // Show a progress dialog
        pd = new ProgressDialog(getActivity());
        pd.setCancelable(false);
        pd.setMessage("Loading content ...");
        pd.show();

        // Set presenter
        presenter = new CategoryPresenter(this);
        // Call presenter method to get json data from url
        presenter.getCatFromCategory(getActivity(), getCategory());

        // Google analytics Screen View
        tracker.send(new HitBuilders.ScreenViewBuilder().build());

        // Buttons
        mLikeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().recreate();
            }
        });

        mLikeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().recreate();
            }
        });

        mFavouriteButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton toggleButton, boolean isChecked) {
                if (isChecked && currentCat != null) {
                    presenter.addFavourite(getActivity(), currentCat);


                } else if (!isChecked && currentCat != null) {
                    presenter.removeFavourite(getActivity(), currentCat);


                }
            }
        });

        mShareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Share
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, "Checkout this kitty ");
                sendIntent.setType("text/plain");
                startActivity(Intent.createChooser(sendIntent, getResources().getText(R.string.send_to)));
            }
        });

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
    public void setCat(CatModel cat) {
        currentCat = cat;

        Picasso.with(getActivity())
                .load(Uri.parse(cat.getUrl()))
                .into(mCatImage);

        mSourceText.setText(cat.getSource_url());

        pd.dismiss();
    }

    @Override
    public void addFavourite(CatModel cat) {
        // Do something when adding the cat to favourites
    }

    @Override
    public void removeFavourite(CatModel cat) {
        // Do something when removing the cat from favourites
    }

    @Override
    public void showErrorToast() {
        pd.dismiss();
        Toast.makeText(getActivity(), "Error while loading content",
                Toast.LENGTH_LONG).show();
    }

    public CategoryModel getCategory() {
        return category;
    }

    public void setCategory(CategoryModel category) {
        this.category = category;
    }
}
