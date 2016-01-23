package uk.co.steffandroid.parkbristol.ui;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import uk.co.steffandroid.parkbristol.R;
import uk.co.steffandroid.parkbristol.data.model.CarPark;

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
        holder.background.setBackgroundColor(ContextCompat.getColor(context, carPark.status().colorRes()));
        holder.name.setText(carPark.name());
        holder.status.setText(carPark.statusText());
        if (!TextUtils.isEmpty(carPark.distanceText())) {
            holder.distance.setVisibility(View.VISIBLE);
            holder.distance.setText(carPark.distanceText());
        } else {
            holder.distance.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return carParkList.size();
    }

    public void addAll(List<CarPark> carParks) {
        carParkList.addAll(carParks);
        notifyDataSetChanged();
    }

    public interface OnClickListener {
        void onClick(CarPark carPark);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @Bind(R.id.car_park_background)
        View background;
        @Bind(R.id.car_park_name)
        TextView name;
        @Bind(R.id.car_park_status)
        TextView status;
        @Bind(R.id.car_park_distance)
        TextView distance;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setClickable(true);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            listener.onClick(carParkList.get(getAdapterPosition()));
        }
    }
}
