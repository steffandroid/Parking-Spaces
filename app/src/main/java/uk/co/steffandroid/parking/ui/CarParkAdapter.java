package uk.co.steffandroid.parking.ui;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import uk.co.steffandroid.parking.R;
import uk.co.steffandroid.parking.data.model.CarPark;

public class CarParkAdapter extends RecyclerView.Adapter<CarParkAdapter.ViewHolder> {
    private Context context;
    private OnClickListener listener;
    private List<CarPark> carParkList = new ArrayList<>();

    public CarParkAdapter(Context context, OnClickListener listener) {
        this.context = context;
        this.listener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.element_car_park_card, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        CarPark carPark = carParkList.get(position);
        holder.bind(carPark);
    }

    @Override
    public int getItemCount() {
        return carParkList.size();
    }

    public void addAll(List<CarPark> carParks) {
        carParkList.clear();
        carParkList.addAll(carParks);
        notifyDataSetChanged();
    }

    public interface OnClickListener {
        void onClick(CarPark carPark);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, OnMapReadyCallback {
        @Bind(R.id.car_park_background)
        View background;
        @Bind(R.id.car_park_map)
        MapView mapView;
        @Bind(R.id.car_park_name)
        TextView name;
        @Bind(R.id.car_park_status)
        TextView status;
        @Bind(R.id.car_park_distance)
        TextView distance;

        private GoogleMap map;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setClickable(true);
            itemView.setOnClickListener(this);
            if (mapView != null) {
                mapView.onCreate(null);
                mapView.onResume();
                mapView.getMapAsync(this);
            }
        }

        public void bind(CarPark carPark) {
            background.setBackgroundColor(ContextCompat.getColor(context, carPark.status().colorRes()));
            name.setText(carPark.name());
            status.setText(carPark.statusText());
            if (!TextUtils.isEmpty(carPark.distanceText())) {
                distance.setVisibility(View.VISIBLE);
                distance.setText(carPark.distanceText());
            } else {
                distance.setVisibility(View.GONE);
            }
            bindMap(carPark);
        }

        private void bindMap(CarPark carPark) {
            if (map != null) {
                map.clear();
                map.addMarker(new MarkerOptions().position(carPark.latLng()));
                CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(carPark.latLng(), 15);
                map.moveCamera(cameraUpdate);
            }
        }

        @Override
        public void onClick(View v) {
            listener.onClick(carParkList.get(getAdapterPosition()));
        }

        @Override
        public void onMapReady(GoogleMap googleMap) {
            MapsInitializer.initialize(context);
            map = googleMap;
            map.getUiSettings().setMapToolbarEnabled(false);
            bindMap(carParkList.get(getAdapterPosition()));
        }
    }
}
