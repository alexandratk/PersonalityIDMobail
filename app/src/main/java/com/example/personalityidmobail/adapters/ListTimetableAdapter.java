package com.example.personalityidmobail.adapters;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.RequiresApi;

import com.example.personalityidmobail.ListMarksActivity;
import com.example.personalityidmobail.ListMovingPupilActivity;
import com.example.personalityidmobail.R;
import com.example.personalityidmobail.models.Lesson;
import com.example.personalityidmobail.models.Pupil;
import com.example.personalityidmobail.models.TimeTableItem;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class ListTimetableAdapter extends BaseAdapter {
    Context ctx;
    LayoutInflater lInflater;
    ArrayList<Lesson> objects;

    TextView textViewId;

    Resources res;

    public ListTimetableAdapter(Context context, ArrayList<Lesson> items) {
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
            view = lInflater.inflate(R.layout.list_timetable_item, viewGroup, false);
        }

        Lesson item = getItemConvert(i);

        String date = parseDateTime(item.dateofstart, item.dateoffinish);

        ((TextView) view.findViewById(R.id.item_timetable_time)).setText(String.valueOf(date));
        ((TextView) view.findViewById(R.id.item_timetable_description)).setText(String.valueOf(item.description));
        ((TextView) view.findViewById(R.id.item_timetable_audience))
                .setText(String.valueOf(item.audience));


        return view;
    }

    Lesson getItemConvert(int position) {
        return ((Lesson) getItem(position));
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public String parseDateTime(String startDateTime, String finishDateTime) {
        LocalDateTime localDateTimeStart = LocalDateTime.parse(startDateTime);
        LocalDateTime localDateTimeFinish = LocalDateTime.parse(finishDateTime);

        DateTimeFormatter aFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        String formattedDate = localDateTimeStart.format(aFormatter);

        aFormatter = DateTimeFormatter.ofPattern("HH:mm");
        String formattedTimeStart = localDateTimeStart.format(aFormatter);
        String formattedTimeFinish = localDateTimeFinish.format(aFormatter);

        return  formattedTimeStart + "-" + formattedTimeFinish;
    }
}
