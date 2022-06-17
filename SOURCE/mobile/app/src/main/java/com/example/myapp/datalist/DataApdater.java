package com.example.myapp.datalist;

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
import com.example.myapp.outstationlist.Outstation;

import java.util.ArrayList;
import java.util.List;

public class DataApdater extends RecyclerView.Adapter<DataApdater.DataViewHolder> {

    private List<Data> mListData;
    private Outstation outst;
    public DataApdater(List<Data> mListData) {
        this.mListData = mListData;
    }

    @NonNull
    @Override
    public DataViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_data_list, parent, false);

        return new DataViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DataViewHolder holder, int position) {
        final Data data = mListData.get(position);
        if (data == null)
            return;
        if(outst.getOS_Type() == 4 && (data.getId() == 0 || data.getId() == 1)) {
            if(data.getValue() == 0.0) {
                holder.tvValue.setText("OFF");
            } else if(data.getValue() == 1.0) {
                holder.tvValue.setText("ON");
            }
        } else
            holder.tvValue.setText(Float.toString(data.getValue()));

        holder.tvIndicator.setText(data.getIndicator());
        holder.tvUnit.setText(data.getUnit());
    }

    public void setOutstation(Outstation outstation) {
        this.outst = outstation;
    }

    @Override
    public int getItemCount() {
        if(mListData != null)
            return mListData.size();
        return 0;
    }

    public List<Data> getListData() {
        return this.mListData;
    }

    public Data getDataByIndex(int index) {

        return this.mListData.get(index);
    }

//    public void updateData(int index, Data data) {
//        this.mListData.get(index) = data;
//        notifyDataSetChanged();
//    }

    public class DataViewHolder extends RecyclerView.ViewHolder {

        private RelativeLayout rlDataList;
        private TextView tvIndicator;
        private TextView tvValue;
        private TextView tvUnit;

        public DataViewHolder(@NonNull View itemView) {
            super(itemView);
            rlDataList = itemView.findViewById(R.id.rl_data_list);
            tvIndicator = itemView.findViewById(R.id.tv_indicator);
            tvValue = itemView.findViewById(R.id.tv_value);
            tvUnit = itemView.findViewById(R.id.tv_unit);

        }


    }
}
