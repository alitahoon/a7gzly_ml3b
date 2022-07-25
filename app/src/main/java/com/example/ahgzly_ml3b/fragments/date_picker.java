package com.example.ahgzly_ml3b.fragments;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import com.example.ahgzly_ml3b.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link date_picker#newInstance} factory method to
 * create an instance of this fragment.
 */
public class date_picker extends DialogFragment {
    DatePicker dp_bithdate;
    Button btn_ok;
    Button btn_cancel;
    private on_btn_ok_clicked btn_ok_listener;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_TITLE = "title";
    private static final String ARG_MESSAGE = "message";
    private static final String ARG_ICON = "icon";

    // TODO: Rename and change types of parameters
    private String mtitle;
    private String mmessage;
    private int micon;
    private String selected_date;

    public date_picker() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param title Parameter 1.
     * @param message Parameter 2.
     * @param icon Parameter 2.
     * @return A new instance of fragment date_picker.
     */
    // TODO: Rename and change types and number of parameters
    public static date_picker newInstance(String title, String message,int icon) {
        date_picker fragment = new date_picker();
        Bundle args = new Bundle();
        args.putString(ARG_TITLE, title);
        args.putString(ARG_MESSAGE, message);
        args.putInt(ARG_ICON, icon);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof on_btn_ok_clicked){
            btn_ok_listener=(on_btn_ok_clicked) context;
        }
        else{
            throw new RuntimeException("please implement on_btn_ok_clicked interface");
        }

    }

    @Override
    public void onDetach() {
        super.onDetach();
        btn_ok_listener=null;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mtitle = getArguments().getString(ARG_TITLE);
            mmessage = getArguments().getString(ARG_MESSAGE);
            micon = getArguments().getInt(ARG_ICON);
        }
    }

    @NonNull


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //get view
        View v= inflater.inflate(R.layout.fragment_date_picker, container, false);
        //inflate items
        dp_bithdate=v.findViewById(R.id.date_picker_birthdate);
        btn_ok=v.findViewById(R.id.btn_date_picker_ok);
        btn_cancel=v.findViewById(R.id.btn_date_picker_cancel);
        return  v;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        //set items values
        dp_bithdate.setOnDateChangedListener(new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker datePicker, int i, int i1, int i2) {
                i1++;
                selected_date=i+"/"+i1+"/"+i2;
            }
        });
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btn_ok_listener.btn_ok_clicked(selected_date);
                dismiss();
            }
        });
        super.onViewCreated(view, savedInstanceState);
    }

    public  interface on_btn_ok_clicked{void btn_ok_clicked(String date);}

}