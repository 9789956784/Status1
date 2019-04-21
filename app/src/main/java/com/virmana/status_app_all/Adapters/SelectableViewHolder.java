package com.virmana.status_app_all.Adapters;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CheckedTextView;
import android.widget.ImageView;
import android.widget.TextView;

import com.virmana.status_app_all.R;
import com.virmana.status_app_all.model.Language;

/**
 * Created by Tamim on 14/10/2017.
 */
public class SelectableViewHolder extends RecyclerView.ViewHolder {

    public static final int MULTI_SELECTION = 2;
    public static final int SINGLE_SELECTION = 1;
    TextView text_view_item_language;
    CheckedTextView textView;
    Language mItem;
    OnItemSelectedListener itemSelectedListener;
    ImageView image_view_iten_language;

    public SelectableViewHolder(View view, OnItemSelectedListener listener) {
        super(view);
        itemSelectedListener = listener;
        text_view_item_language = (TextView) view.findViewById(R.id.text_view_item_language);
        textView = (CheckedTextView) view.findViewById(R.id.checked_text_item);
        image_view_iten_language = (ImageView) view.findViewById(R.id.image_view_iten_language);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (mItem.isSelected() && getItemViewType() == MULTI_SELECTION) {
                    setChecked(false);
                } else {
                    setChecked(true);
                }
                itemSelectedListener.onItemSelected(mItem);

            }
        });
    }

    public void setChecked(boolean value) {
        if (value) {
            textView.setBackgroundColor(Color.parseColor("#6F36B2E2"));
        } else {
            textView.setBackground(null);
        }
        mItem.setSelected(value);
        textView.setChecked(value);
    }

    public interface OnItemSelectedListener {

        void onItemSelected(Language item);
    }

}