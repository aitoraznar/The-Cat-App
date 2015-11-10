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
import com.aitoraznar.thecatapp.presenters.MainPresenter;
import com.aitoraznar.thecatapp.utils.ImageUtils;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.squareup.picasso.Picasso;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by aitor on 28/09/15.
 */
public class HomeFragment extends Fragment implements IHomeView {

    protected MainPresenter presenter;
    protected CatModel currentCat;

    protected @Bind(R.id.mCatImage) ImageView mCatImage;
    protected @Bind(R.id.mSourceText) TextView mSourceText;
    protected @Bind(R.id.mLikeButton) Button mLikeButton;
    protected @Bind(R.id.mHateButton) Button mHateButton;
    protected @Bind(R.id.mFavouriteButton) ToggleButton mFavouriteButton;
    protected @Bind(R.id.mShareButton) Button mShareButton;

    private ProgressDialog pd;
    private Tracker tracker;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        tracker = ((MVPApplication) getActivity().getApplication()).getTracker(MVPApplication.TrackerName.APP_TRACKER);
        tracker.setScreenName("MainView");
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
        presenter = new MainPresenter(this);

        // Google analytics Screen View
        tracker.send(new HitBuilders.ScreenViewBuilder().build());

        if (getArguments() != null && getArguments().get("catId") != null) {
            // Load individual Cat
            String catId = (String) getArguments().get("catId");
            Boolean isFavourite = (getArguments().get("favourite") != null) ? ((Boolean) getArguments().get("favourite")) : false;
            mFavouriteButton.setChecked(isFavourite);
            presenter.getCat(getActivity(), new CatModel(catId, ""));
        } else {
            // Load random Cat
            presenter.getRandomCat(getActivity());
        }

        // Buttons
        mLikeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tracker.send(new HitBuilders.EventBuilder()
                        .setCategory("UX")
                        .setAction("click")
                        .setLabel("like " + currentCat.getId())
                        .setCategory("like")
                        .build());

                //Get another Cat
                getActivity().recreate();
            }
        });

        mHateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tracker.send(new HitBuilders.EventBuilder()
                        .setCategory("UX")
                        .setAction("click")
                        .setLabel("hate " + currentCat.getId())
                        .setCategory("hate")
                        .build());

                //Get another Cat
                getActivity().recreate();
            }
        });

        mFavouriteButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton toggleButton, boolean isChecked) {
                if (isChecked && currentCat != null) {
                    presenter.addFavourite(getActivity(), currentCat);

                    tracker.send(new HitBuilders.EventBuilder()
                            .setCategory("UX")
                            .setAction("click")
                            .setLabel("addFavourite " + currentCat.getId())
                            .setCategory("addFavourite")
                            .build());
                } else if (!isChecked && currentCat != null) {
                    presenter.removeFavourite(getActivity(), currentCat);

                    tracker.send(new HitBuilders.EventBuilder()
                            .setCategory("UX")
                            .setAction("click")
                            .setLabel("removeFavourite " + currentCat.getId())
                            .setCategory("removeFavourite")
                            .build());
                }
            }
        });

        mShareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Share
                Uri bmpUri = ImageUtils.getLocalBitmapUri(mCatImage);
                if (bmpUri != null) {
                    // Construct a ShareIntent with link to image
                    Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_SEND);

                    intent.putExtra(Intent.EXTRA_TEXT, "Checkout this kitty from The Cat App!");
                    intent.putExtra(Intent.EXTRA_STREAM, bmpUri);
                    intent.setType("image/png");
                    // Launch sharing dialog for image
                    startActivity(Intent.createChooser(intent, getResources().getText(R.string.send_to)));

                    tracker.send(new HitBuilders.EventBuilder()
                            .setCategory("UX")
                            .setAction("click")
                            .setLabel("share " + currentCat.getId())
                            .setCategory("share")
                            .build());

                } else {
                    // No BitMap
                    Toast.makeText(getActivity(), "Opps, there is no image to share.",
                            Toast.LENGTH_LONG).show();
                }

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

}
