package wrteam.multivendor.shop.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import wrteam.multivendor.shop.R;

public class OfferAdapter extends RecyclerView.Adapter<OfferAdapter.ViewHolder> {
    public final ArrayList<String> offerList;

    public OfferAdapter(ArrayList<String> offerList) {
        this.offerList = offerList;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.offer_lyt, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        if (!offerList.get(position).equals("")) {
            Picasso.get()
                    .load(offerList.get(position))
                    .fit()
                    .centerInside()
                    .placeholder(R.drawable.offer_placeholder)
                    .error(R.drawable.offer_placeholder)
                    .into(holder.offerImage);

            holder.lytOfferImage.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public int getItemCount() {
        return offerList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        final ImageView offerImage;
        final CardView lytOfferImage;

        public ViewHolder(View itemView) {
            super(itemView);
            offerImage = itemView.findViewById(R.id.offerImage);
            lytOfferImage = itemView.findViewById(R.id.lytOfferImage);

        }

    }
}
