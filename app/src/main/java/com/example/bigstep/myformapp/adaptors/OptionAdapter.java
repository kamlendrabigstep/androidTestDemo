/*
 *   Copyright (c) 2016 BigStep Technologies Private Limited.
 *
 *   You may not use this file except in compliance with the
 *   SocialEngineAddOns License Agreement.
 *   You may obtain a copy of the License at:
 *   https://www.socialengineaddons.com/android-app-license
 *   The full copyright and license information is also mentioned
 *   in the LICENSE file that was distributed with this
 *   source code.
 *
 */

package com.example.bigstep.myformapp.adaptors;

import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.bigstep.myformapp.R;
import com.example.bigstep.myformapp.model.Option;

import java.util.List;

public class OptionAdapter extends RecyclerView.Adapter<OptionAdapter.ItemHolder> {

    private Context mContext;
    private List<Option> list;
    private OnItemClickListener onItemClickListener;
    private boolean mIsOptionsWithIcon;
    private String mDefaultValue;

    public OptionAdapter(Context context, List<Option> list) {
        this.mContext = context;
        this.list = list;
    }

    public OptionAdapter(Context context, List<Option> list, boolean isOptionWithIcon) {
        this.mContext = context;
        this.list = list;
        this.mIsOptionsWithIcon = isOptionWithIcon;
    }


    public OnItemClickListener getOnItemClickListener() {
        return onItemClickListener;
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        onItemClickListener = listener;
    }

    public void setDefaultKey(String defaultValue) {
        mDefaultValue = defaultValue;
    }

    @Override
    public OptionAdapter.ItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.option_menu_item, parent, false);
        return new ItemHolder(itemView, this);
    }

    @Override
    public void onBindViewHolder(final OptionAdapter.ItemHolder holder, int position) {
        final Option item = list.get(position);
        if (item.getName() != null) {
            holder.tvMenuTitle.setText(item.getName());
        }

        // Showing the selected option with check mark.
        if (mContext != null && mDefaultValue != null && !mDefaultValue.equals("0")
                && mDefaultValue.equals(item.getKey())) {
            holder.viewContainer.getLayoutParams().width = ViewGroup.LayoutParams.MATCH_PARENT;
            holder.tvMenuTitle.getLayoutParams().width = ViewGroup.LayoutParams.MATCH_PARENT;
            Drawable drawable = ContextCompat.getDrawable(mContext, R.drawable.ic_done_24dp).mutate();
            drawable.setColorFilter(new PorterDuffColorFilter(ContextCompat.getColor(mContext, R.color.colorPrimary),
                    PorterDuff.Mode.SRC_ATOP));
            holder.tvMenuTitle.setPadding(0, mContext.getResources().getDimensionPixelSize(R.dimen.dimen_5dp),
                    mContext.getResources().getDimensionPixelSize(R.dimen.dimen_5dp), 0);
            holder.tvMenuTitle.setCompoundDrawablesWithIntrinsicBounds(null, null, drawable, null);
        } else {
            holder.tvMenuTitle.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
        }

        if (mIsOptionsWithIcon) {
            holder.tvMenuIcon.setVisibility(View.VISIBLE);
            Typeface typeFace = Typeface.createFromAsset(mContext.getAssets(), "fontIcons/fontawesome-webfont.ttf");
            holder.tvMenuIcon.setTypeface(typeFace);
            holder.tvMenuTitle.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                    mContext.getResources().getDimension(R.dimen.size_20sp));
            if (mContext != null) {
                holder.tvMenuIcon.setMinWidth(mContext.getResources().getDimensionPixelSize(R.dimen.dimen_30dp));
            }
            if (item.getIcon() != null && !item.getIcon().isEmpty()) {
                holder.tvMenuIcon.setVisibility(View.VISIBLE);
                try {
                    holder.tvMenuIcon.setText(new String(Character.toChars(Integer.parseInt(
                            item.getIcon(), 16))));
                } catch (NumberFormatException e) {
                    holder.tvMenuIcon.setText("\uf08b");
                }
            } else {
                switch (item.getKey()) {
                    case "1":
                        holder.tvMenuIcon.setText("\uf167");
                        break;

                    case "2":
                        holder.tvMenuIcon.setText("\uf27d");
                        break;

                    case "3":
                        holder.tvMenuIcon.setText("\uf10b");
                        break;

                    default:
                        holder.tvMenuIcon.setText("\uf01d");
                        break;
                }
            }

        } else {
            holder.tvMenuIcon.setVisibility(View.GONE);
        }
        holder.mainView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final OnItemClickListener listener = holder.adapter.getOnItemClickListener();
                if (listener != null) {
                    listener.onItemClick(item, holder.getAdapterPosition());
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public interface OnItemClickListener {
        void onItemClick(Option value, int position);
    }

    public static class ItemHolder extends RecyclerView.ViewHolder {

        TextView tvMenuTitle, tvMenuIcon;
        View mainView, viewContainer;
        private OptionAdapter adapter;

        public ItemHolder(View itemView, OptionAdapter parent) {
            super(itemView);
            mainView = itemView;
            this.adapter = parent;
            tvMenuTitle = itemView.findViewById(R.id.tvMenuTitle);
            tvMenuTitle.setGravity(Gravity.CENTER_VERTICAL);
            tvMenuIcon = itemView.findViewById(R.id.tvMenuIcon);
            viewContainer = itemView.findViewById(R.id.view_container);

        }
    }
}
