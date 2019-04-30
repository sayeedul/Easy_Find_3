package com.sayeedul.easy_find_3.Adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sayeedul.easy_find_3.Pojo.UserProfile;
import com.sayeedul.easy_find_3.R;

import java.util.ArrayList;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.StudentUI> {

    private ArrayList<UserProfile> userList = new ArrayList<>();
    private ArrayList<UserProfile> userListCpy = new ArrayList<>();

    private OnItemClickListener mListener;
    public interface OnItemClickListener
    {
        void onItemClick(int position);
    }

    public void setOnClickListener(OnItemClickListener listener)
    {
        mListener = listener;
    }

    public void addItem(UserProfile obj)
    {
        userList.add(obj);
        //super.add(obj);
        userListCpy.add(obj);
    }

    @NonNull
    @Override
    public StudentUI onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.row_layout,parent,false);

        return new StudentUI(view,mListener);
    }

    @Override
    public void onBindViewHolder(@NonNull StudentUI holder, int position) {
        holder.bindData(userList.get(position));

        // userListCpy.addAll(userList); // SEE HERE>...............
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }


    public static class StudentUI extends RecyclerView.ViewHolder
    {
        TextView txt_name;
        TextView txt_number;


        public StudentUI(View itemView,final OnItemClickListener listener )
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

            txt_name = itemView.findViewById(R.id.nameTV);
            txt_number = itemView.findViewById(R.id.numberTV);
        }

        public void bindData(UserProfile ob)
        {

            txt_name.setText(ob.getName());
            txt_number.setText(ob.getNumber());

        }

    }

    public ArrayList<UserProfile> filter(String text) {
        userList.clear();
        if(text.isEmpty()){
            userList.addAll(userListCpy);
        } else{
            text = text.toLowerCase();
            for(UserProfile item: userListCpy){
                if(item.name.toLowerCase().contains(text) || item.number.toLowerCase().contains(text)){
                    userList.add(item);
                }
            }
        }
        notifyDataSetChanged();
        return userList;
    }


}


