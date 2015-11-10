package com.aitoraznar.thecatapp.adapters;

import android.content.Context;
import android.net.Uri;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.aitoraznar.thecatapp.R;
import com.aitoraznar.thecatapp.models.FavouriteModel;
import com.aitoraznar.thecatapp.models.catapi.CatModel;
import com.aitoraznar.thecatapp.presenters.MainPresenter;
import com.aitoraznar.thecatapp.views.FavouritesFragment;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by aitor on 29/09/15.
 */
public class FavouriteListAdapter extends BaseAdapter {
    private FavouritesFragment favfragment;
    private FragmentActivity activity;
    private LayoutInflater inflater;
    private List<FavouriteModel> list;
    protected MainPresenter presenter;
    private FavoritesIface mFavoritesIface;

    public FavouriteListAdapter(FavouritesFragment favfragment, FragmentActivity activity,
                                List<FavouriteModel> items, FavoritesIface favoritesIface) {
        this.favfragment = favfragment;
        mFavoritesIface = favoritesIface;
        this.activity = activity;
        this.list = items;
    }

    static class ViewHolder {
        @Bind(R.id.thumbnail) ImageView thumbnail;
        //@Bind(R.id.name) TextView name;
        @Bind(R.id.favouriteButton) ToggleButton favouriteButton;

        public ViewHolder(View view){
            // View injection in parent view
            ButterKnife.bind(this, view);
        }
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public FavouriteModel getItem(int location) {
        return list.get(location);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // Set presenter
        presenter = new MainPresenter(favfragment);

        // Getting data for the row
        final FavouriteModel fav = list.get(position);

        if (inflater == null)
            inflater = (LayoutInflater) activity
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.favourite_row, parent, false);
        }
        final ViewHolder holder = new ViewHolder(convertView);

        // Thumbnail image
        Picasso.with(activity)
                .load(Uri.parse(fav.getUrl()))
                .into(holder.thumbnail);

        // Fav Toggle button
        holder.favouriteButton.setChecked(true);

        holder.favouriteButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                CatModel cat = new CatModel(fav.getId(), fav.getUrl());
                if (isChecked) {
                    presenter.addFavourite(activity, cat);
                } else {
                    presenter.removeFavourite(activity, cat);

                    // Remove from list
                    mFavoritesIface.delete(position);
                }

            }
        });

        // Image click
        holder.thumbnail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mFavoritesIface.onClickListener(v, position);
            }
        });

        // Fav source
        //holder.name.setText(fav.getUrl());

        return convertView;
    }

    public void remove(int position){
        list.remove(position);
        notifyDataSetChanged();
    }

    public interface FavoritesIface{
        void delete(int position);
        void onClickListener(View view, int position);
    }

}

