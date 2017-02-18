package com.glam.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.glam.R;
import com.glam.custom.WishListManager;
import com.glam.utils.CurrencyConverter;
import com.glam.utils.ImageUtility;
import com.shopify.buy.model.Product;
import com.shopify.buy.utils.CurrencyFormatter;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.List;


/**
 * Created by ericbasendra on 02/12/15.
 */
public class ProductListAdapter<T> extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private final int VIEW_ITEM = 1;
    private final int VIEW_PROG = 0;
    private Context mContext;
    public List<T> dataSet;
    private int maxwidth;
    private int maxheight;
    private static MyClickListener myClickListener;

    public ProductListAdapter(List<T> productLists, Context mContext,int maxwidth,int maxheight) {
        this.mContext = mContext;
        this.dataSet = productLists;
        this.maxwidth = maxwidth;
        this.maxheight = maxheight;
    }
    public void setOnItemClickListener(MyClickListener myClickListener) {
        this.myClickListener = myClickListener;
    }

    public void animateTo(List<T> models) {
        applyAndAnimateRemovals(models);
        applyAndAnimateAdditions(models);
        applyAndAnimateMovedItems(models);
    }


    private void applyAndAnimateRemovals(List<T> newModels) {
        for (int i = dataSet.size() - 1; i >= 0; i--) {
            final T model = dataSet.get(i);
            if (!newModels.contains(model)) {
                removeItem(i);
            }
        }
    }


    private void applyAndAnimateAdditions(List<T> newModels) {
        for (int i = 0, count = newModels.size(); i < count; i++) {
            final T model = newModels.get(i);
            if (!dataSet.contains(model)) {
                addItem(i, model);
            }
        }
    }


    private void applyAndAnimateMovedItems(List<T> newModels) {
        for (int toPosition = newModels.size() - 1; toPosition >= 0; toPosition--) {
            final T model = newModels.get(toPosition);
            final int fromPosition = dataSet.indexOf(model);
            if (fromPosition >= 0 && fromPosition != toPosition) {
                moveItem(fromPosition, toPosition);
            }
        }
    }

    public void addItem(T item) {
        if (!dataSet.contains(item)) {
            dataSet.add(item);
            notifyItemInserted(dataSet.size() - 1);
        }
    }

    public void addItem(int position, T model) {
        dataSet.add(position, model);
        notifyItemInserted(position);
    }

    public void removeItem(T item) {
        int indexOfItem = dataSet.indexOf(item);
        if (indexOfItem != -1) {
            this.dataSet.remove(indexOfItem);
            notifyItemRemoved(indexOfItem);
        }
    }

    public T removeItem(int position) {
        final T model = dataSet.remove(position);
        notifyItemRemoved(position);
        return model;
    }

    public void clearItem(){
        if (dataSet != null)
            dataSet.clear();
    }

    public void moveItem(int fromPosition, int toPosition) {
        final T model = dataSet.remove(fromPosition);
        dataSet.add(toPosition, model);
        notifyItemMoved(fromPosition, toPosition);
    }

    public T getItem(int index) {
        if (dataSet != null && dataSet.get(index) != null) {
            return dataSet.get(index);
        } else {
            throw new IllegalArgumentException("Item with index " + index + " doesn't exist, dataSet is " + dataSet);
        }
    }


    @Override
    public int getItemViewType(int position) {
        return dataSet.get(position)!=null? VIEW_ITEM: VIEW_PROG;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder vh = null;
        if (viewType == VIEW_ITEM){
            View itemView = LayoutInflater.
                    from(parent.getContext()).
                    inflate(R.layout.grid_item, parent, false);
            vh = new EventViewHolder(itemView);
        }
        else if(viewType == VIEW_PROG){
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.progress_item, parent, false);
            vh = new ProgressViewHolder(v);
        }else {
            throw new IllegalStateException("Invalid type, this type ot items " + viewType + " can't be handled");
        }

        return vh;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        if(holder instanceof EventViewHolder){
            T dataItem = dataSet.get(position);
            ((EventViewHolder) holder).lbl1.setText(((Product)dataItem).getTitle());
//            ((EventViewHolder) holder).lbl1.setVisibility(d.getTexts()[0] == null ? View.GONE
////					: View.VISIBLE);
            double salesPrice = Double.parseDouble(((Product)dataItem).getMinimumPrice()) * (CurrencyConverter.globalCurrencyChangeRate);
            ((EventViewHolder) holder).lbl2.setText("" + new Double(salesPrice).intValue());
               ((EventViewHolder) holder).lbl3.setText(((Product)dataItem).getProductType());
            ((EventViewHolder) holder).lbl4.setText(CurrencyConverter.globalCurrency);
            if (WishListManager.getInstance().isFavourite(((Product)dataItem))){
                ((EventViewHolder) holder).cart.setImageResource(R.drawable.heart_fill);
            }else{
                ((EventViewHolder) holder).cart.setImageResource(R.drawable.heart_blank);
            }
            if (((Product)dataItem).getFirstImageUrl()!=null && !((Product)dataItem).getFirstImageUrl().equals("")){

                final String imageUrl = ((Product)dataItem).getFirstImageUrl();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        ImageUtility.loadRemoteImageIntoViewWithoutSize(Picasso.with(mContext), imageUrl, ((EventViewHolder) holder).img,maxwidth,maxheight,true, new Callback() {
                            @Override
                            public void onSuccess() {
                                ((EventViewHolder) holder).img.setVisibility(View.VISIBLE);
                                try {
                                    Bitmap bitmpa = ((BitmapDrawable) ((ProductListAdapter.EventViewHolder) holder).img.getDrawable()).getBitmap();
                                    Palette.from(bitmpa).generate(new Palette.PaletteAsyncListener() {
                                        public void onGenerated(Palette palette) {
                                            int color = palette.getDarkMutedColor(0);
                                            if (color == 0) {
                                                color = palette.getDarkVibrantColor(0);
                                            }
                                            if (color == 0) {
                                                color = palette.getLightMutedColor(0);
                                            }
                                            if (color == 0) {
                                                color = palette.getLightVibrantColor(0);
                                            }
                                            if (color == 0) {
                                                color = 0;
                                            }
                                            ((ProductListAdapter.EventViewHolder) holder).cardView.setCardBackgroundColor(color);
                                        }
                                    });
                                } catch (Exception ignored) {
                                    ignored.printStackTrace();
                                }
                            }


                            @Override
                            public void onError() {
                            }
                        });
                    }
                }, 100);

            }

        }else{
            ((ProgressViewHolder)holder).progressBar.setIndeterminate(true);
        }

    }

    @Override
    public int getItemCount() {
        if (dataSet!=null)
            return dataSet.size();
        else
            return 0;
    }


    private void setFadeAnimation(View view) {
        ScaleAnimation anim = new ScaleAnimation(0.0f, 1.0f, 0.0f, 1.0f, Animation.RESTART, 0.5f, Animation.RESTART, 0.5f);
        anim.setDuration(1000);
        view.startAnimation(anim);
    }

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class EventViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        /** The textviews. */
        protected TextView lbl1, lbl2, lbl3, lbl4;
        protected CardView cardView;
        /** The img. */
        protected ImageView img;
        protected ImageView cart;
        public EventViewHolder(View v) {
            super(v);
            lbl1 = (TextView) v.findViewById(R.id.lbl1);
            lbl2 = (TextView) v.findViewById(R.id.lbl2);
            lbl3 = (TextView) v.findViewById(R.id.lbl3);
            lbl4 = (TextView)v.findViewById(R.id.lbl4);
            img = (ImageView) v.findViewById(R.id.img);
            cardView = (CardView)v.findViewById(R.id.card_view);
            cart = (ImageView)v.findViewById(R.id.btnAdd);
            img.setOnClickListener(this);
            cart.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            try{
                if(null != myClickListener){
                  myClickListener.onItemClick(getLayoutPosition(), view);
                }else{
                    Toast.makeText(view.getContext(),"Click Event Null", Toast.LENGTH_SHORT).show();
                }
            }catch(NullPointerException e){
                e.printStackTrace();
                Toast.makeText(view.getContext(),"Click Event Null Ex", Toast.LENGTH_SHORT).show();
            }
        }
    }


    public static class ProgressViewHolder extends RecyclerView.ViewHolder {
        public ProgressBar progressBar;
        public ProgressViewHolder(View v) {
            super(v);
            progressBar = (ProgressBar)v.findViewById(R.id.progress_bar);
        }
    }


    /**
     * y Custom Item Listener
     */

    public interface MyClickListener {
        public void onItemClick(int position, View v);
    }


}
