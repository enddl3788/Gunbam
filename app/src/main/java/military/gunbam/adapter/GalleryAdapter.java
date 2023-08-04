package military.gunbam.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import military.gunbam.R;

public class GalleryAdapter extends RecyclerView.Adapter<GalleryAdapter.GalleryViewHolder> {

    private String[] mDataset;

    public static class GalleryViewHolder extends RecyclerView.ViewHolder {
        public CardView cardView;

        public GalleryViewHolder(CardView view) {
            super(view);
            cardView = view;
        }
    }

    public GalleryAdapter(String[] myDataSet) {
        mDataset = myDataSet;
    }

    @Override
    public GalleryAdapter.GalleryViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        CardView view = (CardView) LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_gallery, viewGroup, false);

        GalleryViewHolder vh = new GalleryViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(GalleryViewHolder holder, int position) {
        TextView textView = holder.cardView.findViewById(R.id.itemGalleryTextView);
        textView.setText(mDataset[position]);
    }

    @Override
    public int getItemCount() {
        return mDataset.length;
    }
}
