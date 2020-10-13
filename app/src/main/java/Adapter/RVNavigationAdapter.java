package Adapter;

import android.content.Intent;
import android.content.res.TypedArray;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.e.ryedup.NavigationActivity;
import com.e.ryedup.HomeActivity;
import com.e.ryedup.R;

import java.util.List;

public class RVNavigationAdapter extends RecyclerView.Adapter<RVNavigationAdapter.RVNavigationHolder> {
    HomeActivity homeActivity;
    List<String> titles;
    TypedArray nav_image;
    Intent intent;
    String user_type;
    public RVNavigationAdapter(HomeActivity homeActivity, List<String> titles, TypedArray nav_image, String user_type) {
        this.homeActivity=homeActivity;
        this.titles=titles;
        this.nav_image=nav_image;
        this.user_type=user_type;

    }

    @NonNull
    @Override
    public RVNavigationHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.rv_navigation_adapter,viewGroup,false);
        return new RVNavigationHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RVNavigationHolder rvNavigationHolder, final int i) {
            rvNavigationHolder.txt_nav_title.setText(titles.get(i));
            rvNavigationHolder.img_nav_icon.setImageResource(nav_image.getResourceId(i,0));

            rvNavigationHolder.ll_layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    homeActivity.drawerLayout.closeDrawers();
                    if (user_type.equalsIgnoreCase("3")){
                        switch (i){
                            case 0:
                                homeActivity.drawerLayout.closeDrawers();
                                break;
                            case 1:
                                intent=new Intent(homeActivity, NavigationActivity.class);
                                intent.putExtra("type","own_history");
                                homeActivity.startActivity(intent);
                                break;
                            case 2:
                                intent=new Intent(homeActivity, NavigationActivity.class);
                                intent.putExtra("type","add_vihicle");
                                intent.putExtra("id","");
                                intent.putExtra("page"," ");
                                homeActivity.startActivity(intent);
                                break;
                            case 3:
                                intent=new Intent(homeActivity, NavigationActivity.class);
                                intent.putExtra("type","wallet");
                                homeActivity.startActivity(intent);
                                break;
                            case 4:
                                intent=new Intent(homeActivity, NavigationActivity.class);
                                intent.putExtra("type","my_vehicle");
                                homeActivity.startActivity(intent);
                                break;
                            case 5:
                                intent=new Intent(homeActivity, NavigationActivity.class);
                                intent.putExtra("type","all_request");
                                homeActivity.startActivity(intent);
                                break;
                            case 6:
                                intent=new Intent(homeActivity, NavigationActivity.class);
                                intent.putExtra("type","own_setting");
                                homeActivity.startActivity(intent);
                                break;
                            case 7:
                               homeActivity.logout();
                             /*   editor = preference.edit();
                                editor.clear();
                                editor.commit();
                                intent=new Intent(homeActivity, LoginActivity.class);
                                homeActivity.startActivity(intent);
                                homeActivity.finish();*/
                                break;
                        }
                    }else {
                        switch (i){
                            case 0:
                               homeActivity.drawerLayout.closeDrawers();
                                intent=new Intent(homeActivity,HomeActivity.class);
                                homeActivity.startActivity(intent);
                                homeActivity. finish();
                                break;
                           /* case 1:
                                intent=new Intent(homeActivity, NavigationActivity.class);
                                intent.putExtra("type","message");
                                homeActivity.startActivity(intent);
                                break;*/
                            case 1:
                                intent=new Intent(homeActivity, NavigationActivity.class);
                                intent.putExtra("type","history");
                                homeActivity.startActivity(intent);
                                break;
                        /*    case 3:
                                intent=new Intent(homeActivity, NavigationActivity.class);
                                intent.putExtra("type","driver");
                                homeActivity.startActivity(intent);
                                break;*/
                            case 2:
                                intent=new Intent(homeActivity, NavigationActivity.class);
                                intent.putExtra("type","rent");
                                homeActivity.startActivity(intent);
                                break;
                            case 3:
                                intent=new Intent(homeActivity, NavigationActivity.class);
                                intent.putExtra("type","rent_nearby");
                                homeActivity.startActivity(intent);
                                break;
                            case 4:
                                intent=new Intent(homeActivity, NavigationActivity.class);
                                intent.putExtra("type","setting");
                                homeActivity.startActivity(intent);
                                break;
                            case 5:
                                homeActivity.logout();

                                /*editor = preference.edit();
                                editor.clear();
                                editor.commit();
                                intent=new Intent(homeActivity, LoginActivity.class);
                                homeActivity.startActivity(intent);
                                homeActivity.finish();*/
                                break;
                        }
                    }

                }
            });
    }

    @Override
    public int getItemCount() {
        return titles.size();
    }

    public class RVNavigationHolder extends RecyclerView.ViewHolder {
            ImageView img_nav_icon;
            TextView txt_nav_title;
            LinearLayout ll_layout;
        public RVNavigationHolder(@NonNull View itemView) {
            super(itemView);
            img_nav_icon=itemView.findViewById(R.id.img_nav_icon);
            txt_nav_title=itemView.findViewById(R.id.txt_nav_title);
            ll_layout=itemView.findViewById(R.id.ll_layout);
        }
    }
}
