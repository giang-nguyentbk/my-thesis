package com.example.myapp.outstationlist;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapp.R;
import com.example.myapp.my_interface.IClickItemOutstationListener;


import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class OutstationApdater extends RecyclerView.Adapter<OutstationApdater.OutstationViewHolder> implements Filterable {

    private List<Outstation> mListOutstation;
    private List<Outstation> mListOutstationOld;
    private IClickItemOutstationListener iClickItemOutstationListener;

    public OutstationApdater(List<Outstation> mListOutstation, IClickItemOutstationListener iClickItemOutstationListener) {
        this.mListOutstation = mListOutstation;
        this.mListOutstationOld = mListOutstation;
        this.iClickItemOutstationListener = iClickItemOutstationListener;
    }

    public List<Outstation> getmListOutstation() {
        return this.mListOutstation;
    }

    public Outstation getOutstationByIndex(int pos) {
        return mListOutstation.get(pos);
    }

    @NonNull
    @Override
    public OutstationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_outstation_list, parent, false);
        return new OutstationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OutstationViewHolder holder, int position) {
        final Outstation outstation = mListOutstation.get(position);
        if (outstation == null)
            return;

        holder.imgIcon.setImageResource(outstation.getResourceId());
        holder.tvOutstationNum.setText(outstation.getRcv_outstationNumber());
        holder.tvLocation.setText(outstation.getRcv_location());

        holder.rlOutstationList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                iClickItemOutstationListener.onClickItemOutstation(outstation);
            }

        });
    }

    @Override
    public int getItemCount() {
        if(mListOutstation != null)
            return mListOutstation.size();
        return 0;
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String strToSearch = charSequence.toString();

                if(strToSearch.isEmpty()) {
                    mListOutstation = mListOutstationOld;
                } else {
                    List<Outstation> list = new ArrayList<>();
                    for(Outstation outstation: mListOutstationOld) {
                        if(outstation.getRcv_location().toLowerCase().contains(strToSearch.toLowerCase())) {
                            list.add(outstation);
                        }
                        if(outstation.getRcv_outstationNumber().toLowerCase().contains(strToSearch.toLowerCase())) {
                            list.add(outstation);
                        }
                    }
                    mListOutstation = list;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = mListOutstation;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                mListOutstation = (List<Outstation>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public class OutstationViewHolder extends RecyclerView.ViewHolder {

        private RelativeLayout rlOutstationList;
        private ImageView imgIcon;
        private TextView tvOutstationNum;
        private TextView tvLocation;

        public OutstationViewHolder(@NonNull View itemView) {
            super(itemView);
            rlOutstationList = itemView.findViewById(R.id.rl_outstation_list);
            imgIcon = itemView.findViewById(R.id.rcvimg_icon);
            tvOutstationNum = itemView.findViewById(R.id.rcvtv_outstationNum);
            tvLocation = itemView.findViewById(R.id.rcvtv_location);

        }


    }
}
