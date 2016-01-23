package uk.co.steffandroid.parkbristol.ui;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import uk.co.steffandroid.parkbristol.R;
import uk.co.steffandroid.parkbristol.data.model.CarPark;

public class CarParkAdapter extends RecyclerView.Adapter<CarParkAdapter.ViewHolder> {

    private List<CarPark> carParkList = new ArrayList<>();

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.element_car_park_card, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        CarPark carPark = carParkList.get(position);
        holder.background.setBackgroundColor(carPark.status().colorRes());
    }

    @Override
    public int getItemCount() {
        return carParkList.size();
    }

    public void addAll(List<CarPark> carParks) {
        carParkList.addAll(carParks);
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.car_park_background)
        View background;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
