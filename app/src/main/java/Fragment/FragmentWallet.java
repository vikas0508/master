package Fragment;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.e.ryedup.R;

import Adapter.RVViewAdapter;

public class FragmentWallet extends Fragment {
    RecyclerView rv_viewlist;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_wallet,container,false);
        BindUI(view);
        rv_viewlist.setLayoutManager(new LinearLayoutManager(getActivity()));
        rv_viewlist.setAdapter(new RVViewAdapter(getContext()));
        return view;
    }

    private void BindUI(View view) {
        rv_viewlist= view.findViewById(R.id.rv_viewlist);
    }
}
