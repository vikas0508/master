package Adapter;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.e.ryedup.R;

public class RVViewAdapter extends RecyclerView.Adapter<RVViewAdapter.RVHolder> {
    Context context;
    public RVViewAdapter(Context context) {
        this.context=context;
    }

    @NonNull
    @Override
    public RVHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.rv_view_adapter,viewGroup,false);
        return new RVHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RVHolder rvHolder, int i) {

    }

    @Override
    public int getItemCount() {

        return 10;
    }

    public class RVHolder extends RecyclerView.ViewHolder {

        public RVHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
