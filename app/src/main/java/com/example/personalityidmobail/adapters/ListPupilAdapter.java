package com.example.personalityidmobail.adapters;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.personalityidmobail.ListMarksActivity;
import com.example.personalityidmobail.ListMovingPupilActivity;
import com.example.personalityidmobail.R;
import com.example.personalityidmobail.TimetableActivity;
import com.example.personalityidmobail.models.Pupil;
import com.example.personalityidmobail.models.User;

import java.io.Serializable;
import java.util.ArrayList;

public class ListPupilAdapter extends BaseAdapter {
    Context ctx;
    LayoutInflater lInflater;
    ArrayList<Pupil> objects;

    TextView textViewId;

    Resources res;

    public ListPupilAdapter(Context context, ArrayList<Pupil> items) {
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

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        View view = convertView;
        if (view == null) {
            view = lInflater.inflate(R.layout.list_pupil_item, viewGroup, false);
        }

        Pupil item = getItemConvert(i);

        TextView textViewId = (TextView) view.findViewById(R.id.item_list_pupil_id);
        textViewId.setText("" + res.getString(R.string.Id) + ": " + String.valueOf(item.id));

        ((TextView) view.findViewById(R.id.item_list_pupil_name)).setText(String.valueOf(item.name));
        ((TextView) view.findViewById(R.id.item_list_pupil_group_title))
                .setText("" + res.getString(R.string.Group)  + ": " + String.valueOf(item.group.title));

        Button buttonShowMoving = (Button) view.findViewById(R.id.button_view_moving);
        buttonShowMoving.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("this button was clicked");
                System.out.println(String.valueOf(item.id));
                Intent intent = new Intent(viewGroup.getContext(), ListMovingPupilActivity.class);
                intent.putExtra("pupil", (Serializable) item);
                viewGroup.getContext().startActivity(intent);
            }
        });

        Button buttonShowMarks = (Button) view.findViewById(R.id.button_view_marks);
        buttonShowMarks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println(String.valueOf(item.id));
                Intent intent = new Intent(viewGroup.getContext(), ListMarksActivity.class);
                intent.putExtra("pupil", (Serializable) item);
                viewGroup.getContext().startActivity(intent);
            }
        });

        ImageButton imageButtonTimetable = (ImageButton) view.findViewById(R.id.item_pupil_timetable);
        imageButtonTimetable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println(String.valueOf(item.id));
                Intent intent = new Intent(viewGroup.getContext(), TimetableActivity.class);
                User newUser = new User(item.id, item.role);
                intent.putExtra("user", newUser);
                viewGroup.getContext().startActivity(intent);
            }
        });

        return view;
    }

    Pupil getItemConvert(int position) {
        return ((Pupil) getItem(position));
    }

}
