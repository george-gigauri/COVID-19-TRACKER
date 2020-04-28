package ge.s.covid_19live;

import android.content.Context;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

class CoronaAdapter extends RecyclerView.Adapter<CoronaAdapter.VH> {
    private Context context;
    private ArrayList<CoronaItem> arr;

    public CoronaAdapter(Context context, ArrayList<CoronaItem> arr)
    {
        this.context = context;
        this.arr = arr;
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new VH(LayoutInflater.from(parent.getContext()).inflate(R.layout.country_list_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, int position) {
        CoronaItem i = arr.get(position);

        holder.country.setText(i.getCountry());
        holder.total.setText(i.getTotalConfirmed());
        holder.total_rec.setText(i.getTotalRecovered());
        holder.total_death.setText(i.getTotalDeaths());
    }

    @Override
    public int getItemCount() {
        return arr.size();
    }

    static class VH extends RecyclerView.ViewHolder
    {
        TextView country, total, total_rec, total_death;
        VH(@NonNull View itemView) {
            super(itemView);

            country = itemView.findViewById(R.id.country_txt);
            total = itemView.findViewById(R.id.total_txt);
            total_rec = itemView.findViewById(R.id.recovered_txt);
            total_death = itemView.findViewById(R.id.deaths_txt);
        }
    }
}
