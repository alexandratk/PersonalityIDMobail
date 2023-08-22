package com.example.personalityidmobail.adapters;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Build;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.RequiresApi;

import com.example.personalityidmobail.ListMovingPupilActivity;
import com.example.personalityidmobail.R;
import com.example.personalityidmobail.models.MovingPupil;
import com.example.personalityidmobail.models.Pupil;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class ListMovingPupilAdapter extends BaseAdapter {
    Context ctx;
    LayoutInflater lInflater;
    ArrayList<MovingPupil> objects;

    Resources res;

    public ListMovingPupilAdapter(Context context, ArrayList<MovingPupil> items) {
        ctx = context;
        objects = items;
        lInflater = (LayoutInflater) ctx
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        res = context.getResources();
    }


    @Override
    public int getCount() {
        return objects.size();
    }

    @Override
    public Object getItem(int i) {
        return objects.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        View view = convertView;
        if (view == null) {
            view = lInflater.inflate(R.layout.list_moving_pupil_item, viewGroup, false);
        }

        MovingPupil item = getItemConvert(i);

        String message = "";
        if (item.message.equals("in")) {
            message = res.getString(R.string.pupil_in_the_school);
        }
        if (item.message.equals("out")) {
            message = res.getString(R.string.pupil_out_of_school);
        }

        TextView textViewId = (TextView) view.findViewById(R.id.item_moving_pupil_message);
        textViewId.setText(String.valueOf(res.getString(R.string.Message) + ": " + message));

        String dateTime = parseDateTime(item.time);

        ((TextView) view.findViewById(R.id.item_moving_pupil_time)).setText(String.valueOf(dateTime));

        if (item.message.equals("in")) {
            ((ImageView) view.findViewById(R.id.item_moving_pupil_icon))
                    .setImageResource(R.drawable.ic_outline_run_circle_green);
        }
        if (item.message.equals("out")) {
            ((ImageView) view.findViewById(R.id.item_moving_pupil_icon))
                    .setImageResource(R.drawable.ic_outline_run_circle_red);
        }

        return view;
    }

    MovingPupil getItemConvert(int position) {
        return ((MovingPupil) getItem(position));
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public String parseDateTime(String dateTime) {
        LocalDateTime localDateTime = LocalDateTime.parse(dateTime);

        DateTimeFormatter aFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        String formattedDate = localDateTime.format(aFormatter);

        aFormatter = DateTimeFormatter.ofPattern("HH:mm");
        String formattedTime = localDateTime.format(aFormatter);

        return  formattedTime + " " + formattedDate;
    }
}
