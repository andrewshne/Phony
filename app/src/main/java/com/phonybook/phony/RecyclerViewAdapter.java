package com.phonybook.phony;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.media.Image;
import android.net.Uri;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SwitchCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder> implements Filterable {
    List<ContactModel> contactModelList;
    List<ContactModel> mArrayList;
    Context context;
    DataBaseHelper dataBaseHelper;

    public RecyclerViewAdapter(Context context, List<ContactModel> contactModelList) {
        this.contactModelList = contactModelList;
        this.mArrayList = contactModelList;
        this.context = context;
        dataBaseHelper = new DataBaseHelper(context);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.one_line_contact, parent, false);
        final MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        final ContactModel contactModel = contactModelList.get(position);
        holder.tv_nameview.setText(contactModel.getName());
        holder.tv_phonenumview.setText(contactModel.getPhonenum());
        holder.tv_emailview.setText(contactModel.getEmail());
        if (contactModel.isWork()) {
            holder.tv_workview.setText(R.string.yes);
        }
        else {
            holder.tv_workview.setText(R.string.no);
        }
        if (contactModel.getImage() == null) {
            holder.iv_photoview.setImageResource(R.drawable.ic_action_person);
        }
        else {
            holder.iv_photoview.setImageURI(Uri.parse(contactModel.getImage()));
        }
        holder.btn_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity) context).editTaskDialog(contactModel);
            }
        });
        holder.btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dataBaseHelper.deleteOne(contactModel.getId());
                ((Activity) context).finish();
                context.startActivity(((Activity) context).getIntent());
            }
        });
    }


    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String charString = constraint.toString();
                if (charString.isEmpty()) {
                    contactModelList = mArrayList;
                } else {
                    ArrayList<ContactModel> fillteredList = new ArrayList<>();
                    for (ContactModel contactModel : mArrayList) {
                        if (contactModel.getName().toLowerCase().contains(charString)) {
                            fillteredList.add(contactModel);
                        }
                    }
                    contactModelList = fillteredList;
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = contactModelList;
                return filterResults;
            }

            @SuppressWarnings("unchecked")
            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                contactModelList = (List<ContactModel>) results.values;
                notifyDataSetChanged();
            }
        };
    }

    @Override
    public int getItemCount() {
        return contactModelList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tv_nameview, tv_phonenumview, tv_emailview, tv_workview;
        ImageView btn_delete, btn_edit, iv_photoview;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setClickable(true);
            tv_nameview = itemView.findViewById(R.id.tv_nameview);
            tv_phonenumview = itemView.findViewById(R.id.tv_phonenumview);
            tv_emailview = itemView.findViewById(R.id.tv_emailview);
            tv_workview = itemView.findViewById(R.id.tv_workview);
            iv_photoview = itemView.findViewById(R.id.iv_photoview);
            btn_edit = itemView.findViewById(R.id.btn_edit);
            btn_delete = itemView.findViewById(R.id.btn_delete);
        }
    }
}
