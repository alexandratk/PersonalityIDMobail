package com.example.personalityidmobail.adapters;

import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.annotation.RequiresApi;

import com.example.personalityidmobail.R;
import com.example.personalityidmobail.models.Mark;
import com.example.personalityidmobail.models.MovingPupil;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class ListMarkAdapter extends BaseAdapter {

    Context ctx;
    LayoutInflater lInflater;
    ArrayList<Mark> objects;

    public ListMarkAdapter(Context context, ArrayList<Mark> items) {
        ctx = context;
        objects = items;
        lInflater = (LayoutInflater) ctx
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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
            view = lInflater.inflate(R.layout.list_mark_item, viewGroup, false);
        }

        Mark item = getItemConvert(i);

        String dateTimeMark = parseDateTime(item.dateTimeMark);
        String dateTimeLesson = parseDateTime(item.lesson.dateofstart, item.lesson.dateoffinish);

        ((TextView) view.findViewById(R.id.item_mark_lesson_description)).setText(item.lesson.description);
        ((TextView) view.findViewById(R.id.item_mark_date_time_mark)).setText(dateTimeMark);
        ((TextView) view.findViewById(R.id.item_mark_dateofstart)).setText(dateTimeLesson);
        ((TextView) view.findViewById(R.id.item_mark_teacher)).setText(item.lesson.teacher.name);
        ((TextView) view.findViewById(R.id.item_mark_lesson_mark)).setText(item.lessonMark);
        return view;
    }

    Mark getItemConvert(int position) {
        return ((Mark) getItem(position));
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public String parseDateTime(String dateTime) {
        LocalDateTime localDateTime = LocalDateTime.parse(dateTime);

        DateTimeFormatter aFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        String formattedDate = localDateTime.format(aFormatter);

        aFormatter = DateTimeFormatter.ofPattern("HH:mm");
        String formattedTime = localDateTime.format(aFormatter);

        return  formattedTime + " " + formattedDate;
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

        return  formattedTimeStart + "-" + formattedTimeFinish + " " + formattedDate;
    }
}
