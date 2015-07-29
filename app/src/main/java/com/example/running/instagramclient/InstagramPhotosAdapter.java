package com.example.running.instagramclient;

import android.content.Context;
import android.graphics.Color;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Transformation;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.makeramen.roundedimageview.RoundedImageView;
import com.makeramen.roundedimageview.RoundedTransformationBuilder;
import com.makeramen.roundedimageview.RoundedTransformationBuilder;
import com.squareup.picasso.Picasso;

import java.util.List;

import javax.xml.transform.Transformer;

/**
 * Created by running on 7/26/15.
 */
public class InstagramPhotosAdapter extends ArrayAdapter<InstagramPhoto> {

    public InstagramPhotosAdapter(Context context, List<InstagramPhoto> objects){
        super(context, android.R.layout.simple_list_item_1, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        InstagramPhoto photo = getItem(position);
        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_photo,parent, false);
        }
        TextView tvCaption = (TextView) convertView.findViewById(R.id.tvCaption);
        ImageView ivPhoto = (ImageView) convertView.findViewById(R.id.ivPhoto);
        TextView tvUsername = (TextView) convertView.findViewById(R.id.tvUsername);
        RoundedImageView ivProfilePicture = (RoundedImageView) convertView.findViewById(R.id.ivProfilePicture);
        TextView likesCoutn = (TextView) convertView.findViewById(R.id.likesCount);
        TextView relativeTime = (TextView) convertView.findViewById(R.id.relativeTime);

        tvCaption.setText(photo.getCaption());
        tvUsername.setText(photo.getUsername());
        relativeTime.setText(photo.getRelativeTime());

        ivPhoto.setImageResource(0);
        Picasso.with(getContext()).load(photo.getImageUrl()).into(ivPhoto);

        ivProfilePicture.setImageResource(0);
        Picasso.with(getContext()).load(photo.getProfilePicture()).into(ivProfilePicture);

        likesCoutn.setText("    " + photo.getLikesCount() + "   likes");

        TextView tvComment = (TextView) convertView.findViewById(R.id.comment2);
        tvComment.setText(Html.fromHtml(photo.getComments().get(0)));

        TextView tvComment1 = (TextView) convertView.findViewById(R.id.comment1);
        tvComment1.setText(Html.fromHtml(photo.getComments().get(1)));


        return convertView;


    }


}
