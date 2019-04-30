package com.sayeedul.easy_find_3.Adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sayeedul.easy_find_3.Pojo.SmsHistoryPojo;
import com.sayeedul.easy_find_3.Pojo.UserProfile;
import com.sayeedul.easy_find_3.R;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class SmsHistoryAdapter extends RecyclerView.Adapter<SmsHistoryAdapter.StudentUI> {

    private ArrayList<SmsHistoryPojo> userList = new ArrayList<>();
    private ArrayList<SmsHistoryPojo> userListCpy = new ArrayList<>();

    private SmsHistoryAdapter.OnItemClickListener mListener;
    public interface OnItemClickListener
    {
        void onItemClick(int position);
    }

    public void setOnClickListener(SmsHistoryAdapter.OnItemClickListener listener)
    {
        mListener = listener;
    }

    public void addItem(SmsHistoryPojo obj)
    {
        userList.add(obj);
        //super.add(obj);
        userListCpy.add(obj);
    }

    @NonNull
    @Override
    public SmsHistoryAdapter.StudentUI onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.row_layout,parent,false);

        return new SmsHistoryAdapter.StudentUI(view,mListener);
    }

    @Override
    public void onBindViewHolder(@NonNull SmsHistoryAdapter.StudentUI holder, int position) {
        holder.bindData(userList.get(position));

        // userListCpy.addAll(userList); // SEE HERE>...............
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }


    public static class StudentUI extends RecyclerView.ViewHolder
    {
        TextView txt_from;
        TextView txt_sent_number;
        TextView txt_sent_name;
        TextView txt_date;


        public StudentUI(View itemView,final SmsHistoryAdapter.OnItemClickListener listener )
        {
            super(itemView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(listener != null)
                    {
                        int position = getAdapterPosition();
                        if(position !=RecyclerView.NO_POSITION)
                        {
                            listener.onItemClick(position);
                        }
                    }
                }
            });

            txt_from = itemView.findViewById(R.id.FromSMSTV);
            txt_sent_number = itemView.findViewById(R.id.SentSMSTV);
            txt_sent_name = itemView.findViewById(R.id.SentNameSMSTV);
            txt_date = itemView.findViewById(R.id.DateSMSTV);
        }

        public void bindData(SmsHistoryPojo ob)
        {

            txt_from.setText(ob.getNumber_in());
            txt_sent_name.setText(ob.getName_out());
            txt_sent_number.setText(ob.getNumber_out());
            txt_date.setText(ob.getDate());

        }

    }

    public ArrayList<SmsHistoryPojo> filter(String text) {
        userList.clear();
        if(text.isEmpty()){
            userList.addAll(userListCpy);
        } else{
            text = text.toLowerCase();
            for(SmsHistoryPojo item: userListCpy){
                if(item.name_out.toLowerCase().contains(text) || item.number_out.toLowerCase().contains(text)){
                    userList.add(item);
                }
            }
        }
        notifyDataSetChanged();
        return userList;
    }


}

