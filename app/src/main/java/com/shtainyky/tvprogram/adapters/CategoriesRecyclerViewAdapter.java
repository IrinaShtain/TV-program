package com.shtainyky.tvprogram.adapters;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.shtainyky.tvprogram.R;
import com.shtainyky.tvprogram.models.models_ui.CategoryUI;
import com.shtainyky.tvprogram.utils.Utils;

import butterknife.BindView;
import butterknife.ButterKnife;

// основна робота адаптера в тому щоб відобразити дані які в нього прийшли і "дати знати" про то що
// користувач взаємодіє з цими даними. Сам адаптер не вирішує нічого, не завантажує нічого,
// не обробляє нічого. Виконує тільки свої дії, дії АДАПТЕРА
public class CategoriesRecyclerViewAdapter extends CursorRecyclerViewAdapter<CategoriesRecyclerViewAdapter.CategoriesHolder> {
    private Context mContext;
    private OnCategoryClickListener mOnCategoryClickListener;

    public CategoriesRecyclerViewAdapter(Context context, Cursor cursor) {
        super(context, cursor);
        mContext = context;
    }

    //  20.02.17 use this listener to let know fragment/activity that use this adapter
    //Done
    public void setOnCategoryClickListener(OnCategoryClickListener listener) {
        mOnCategoryClickListener = listener;
    }

    @Override
    public CategoriesHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.adapter_item_category, parent, false);
        return new CategoriesHolder(view);
    }

    @Override
    public void onBindViewHolder(CategoriesHolder holder, Cursor cursor) {
        cursor.moveToPosition(cursor.getPosition());
        final CategoryUI category = CategoryUI.getCategory(cursor);
        holder.mCategoryNameTextView.setText(category.getTitle());
        Utils.loadImageFromServerWithPicasso(mContext, category.getImage_url(), holder.mImageView);
        holder.mCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnCategoryClickListener.onCategoryClick(category.getId());
            }
        });
    }

    public interface OnCategoryClickListener {
        void onCategoryClick(int categoryID);
    }

    //  20.02.17 simple inner holder class to bind with category
    //  20.02.17 holder is used only for CategoriesRecyclerViewAdapter, can be declared inside as public static class
    //Done
    static class CategoriesHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.category_name)
        TextView mCategoryNameTextView;
        @BindView(R.id.category_logo)
        ImageView mImageView;
        @BindView(R.id.card_view)
        CardView mCardView;

        CategoriesHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
        //  20.02.17 don't handle any actions here, you can simply display data
        //Done
        // and let know about user actions
        // you do not need to care about internet here

    }
}


