package com.example.healthsupervisor;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.healthsupervisor.Model.Articles;
import com.github.ybq.android.spinkit.style.FoldingCube;
import com.github.ybq.android.spinkit.style.WanderingCubes;
import com.squareup.picasso.Picasso;

import org.ocpsoft.prettytime.PrettyTime;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;

public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {

    Context context;
    List<Articles>articles;

    public Adapter(Context context, List<Articles> articles) {
        this.context = context;
        this.articles = articles;
    }

    @NonNull
    @Override
    public Adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.items,parent,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Adapter.ViewHolder holder, int i) {
        Random random=new Random();
        final int po=random.nextInt(articles.size()-1);
        Articles a = articles.get(po);
        holder.tvTitle.setText(a.getTitle());
        holder.tvSource.setText(a.getSource().getName());
        holder.tvDate.setText("\u2022"+dateTime(a.getPublishedAt()));
        holder.tvDesc.setText(a.getDescription()+a.getContent());
        String [] u = new String[20];
        u[0]="https://image.shutterstock.com/z/stock-vector-simple-set-of-coronavirus-protection-related-vector-line-icons-contains-such-icons-as-protective-1666609990.jpg";
        u[1]="https://thumbs.dreamstime.com/z/health-most-imortnat-related-keywords-39672626.jpg";
        u[3]="https://teowethan.files.wordpress.com/2013/11/healthy-life.jpg";
        u[4]="https://image.shutterstock.com/z/stock-photo-public-health-medical-concept-with-blurred-text-stethoscope-pills-and-syringe-on-grey-background-325208582.jpg";
        u[5]="https://image.shutterstock.com/image-illustration/public-health-word-cloud-cross-600w-1173101590.jpg";
        u[6]="https://image.shutterstock.com/z/stock-photo-exercise-activity-family-outdoors-vitality-healthy-611946263.jpg";
        u[7]="https://image.shutterstock.com/image-photo/health-insurance-concept-doctor-hospital-260nw-1451879171.jpg";
        u[8]="https://image.shutterstock.com/image-photo/chiang-rai-thailand-march-28-600w-1068238544.jpg";
        u[9]="https://image.shutterstock.com/image-photo/hand-arranging-wood-block-stacking-600w-1169616739.jpg";
        u[10]="https://image.shutterstock.com/image-photo/health-food-fitness-immune-boosting-600w-743959498.jpg";
        u[11]="https://image.shutterstock.com/image-vector/fitness-health-icons-white-background-600w-132000299.jpg";
        u[12]="https://image.shutterstock.com/image-photo/exhausted-female-worker-sit-office-600w-1489697975.jpg";
        u[13]="https://image.shutterstock.com/image-photo/free-happy-woman-enjoying-nature-600w-146971178.jpg";
        u[14]="https://image.shutterstock.com/image-photo/mother-daughter-yoga-home-600w-1033938349.jpg";
        u[15]="https://image.shutterstock.com/image-photo/senior-adult-couple-exercise-fitness-600w-583124596.jpg";
        u[16]="https://image.shutterstock.com/z/stock-photo-mental-health-concept-file-with-a-list-of-psychiatric-disorders-d-illustration-758549185.jpg";
        u[17]="https://image.shutterstock.com/image-photo/runner-feet-running-on-road-260nw-103383050.jpg";
        u[18]="https://image.shutterstock.com/image-photo/girl-drinks-water-after-sport-260nw-255907777.jpg";
        u[19]="https://image.shutterstock.com/image-photo/young-depressed-man-doctors-office-600w-572649838.jpg";

        Random r = new Random();

//        Intent intent = new Intent("message_subject_intent");
//        intent.putExtra("url" , a.getUrl());
//        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);


        String imageUrl = a.getUrlToImage();
        if(imageUrl==null || imageUrl==""){
            int x=r.nextInt(19);
            imageUrl=u[x];
        }

        WanderingCubes foldingCube = new WanderingCubes();

        holder.mpb.setIndeterminateDrawable(foldingCube);
        holder.mpb.setIndeterminateDrawable(foldingCube);
        holder.mpb.setVisibility(View.VISIBLE);
            Picasso.get().load(imageUrl).into(holder.imageView, new com.squareup.picasso.Callback() {
                @Override
                public void onSuccess() {
                    if (holder.mpb != null) {
                        holder.mpb.setVisibility(View.GONE);
                    }
                }

                @Override
                public void onError(Exception e) {
                    e.printStackTrace();
                }
            });


    }

    @Override
    public int getItemCount() {
        return 1;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView tvTitle ,tvSource;
        TextView tvDate;
        ImageView imageView;
        CardView cardView;
        TextView tvDesc;
        ProgressBar mpb;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mpb=itemView.findViewById(R.id.prog);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvSource = itemView.findViewById(R.id.tvSource);
            tvDate = itemView.findViewById(R.id.tvDate);
            imageView = itemView.findViewById(R.id.imagedisp);
            cardView  = itemView.findViewById(R.id.cardView);
            tvDesc = itemView.findViewById(R.id.decription);
        }
    }
    public String dateTime(String t){
        PrettyTime prettyTime = new PrettyTime(new Locale(getCountry()));
        String time = null;
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:",Locale.ENGLISH);
            Date date = simpleDateFormat.parse(t);
            time = prettyTime.format(date);
        }catch (ParseException e){
            e.printStackTrace();
        }

        return time;

    }
    public String getCountry(){
        Locale locale = Locale.getDefault();
        String country = locale.getCountry();
        return country.toLowerCase();
    }
}