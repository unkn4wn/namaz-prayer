package com.freeislamicapps.athantime.ui.settings;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.freeislamicapps.athantime.R;

import java.util.ArrayList;

public class LocationRecyclerAdapter extends RecyclerView.Adapter<LocationRecyclerAdapter.ViewHolder> {
    private final LocationRecyclerInterface locationRecyclerInterface;
    private final ArrayList<LocationModel> arrayList;

    public LocationRecyclerAdapter(ArrayList<LocationModel> arrayList, LocationRecyclerInterface locationRecyclerInterface) {
        this.arrayList = arrayList;
        this.locationRecyclerInterface = locationRecyclerInterface;
    }

    @NonNull
    @Override
    public LocationRecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View inflate = layoutInflater.inflate(R.layout.fragment_settings_location_recyclerview, null);
        return new ViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull LocationRecyclerAdapter.ViewHolder holder, int position) {
        String data = arrayList.get(position).getLocation();
        holder.title.setText(data);
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView title;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);

            itemView.setOnClickListener(view -> {
                if (locationRecyclerInterface != null) {
                    int position = getAdapterPosition();

                    if (position != RecyclerView.NO_POSITION) {
                        locationRecyclerInterface.onItemClick(position);
                    }
                }
            });
        }
    }
}
