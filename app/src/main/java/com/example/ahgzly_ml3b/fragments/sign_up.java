package com.example.ahgzly_ml3b.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.ahgzly_ml3b.R;
import com.google.firebase.database.DatabaseReference;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link sign_up#newInstance} factory method to
 * create an instance of this fragment.
 *
 */
public class sign_up extends Fragment  {
    onBtnNextClicked btnNextlistener;
    DatabaseReference firbase_database;
    SharedPreferences sharedpreferences;
    TextView title;
    TextView location;
    Spinner sp_postion;
    TextView date_picker;
    EditText txt_fisrt_name,txt_last_name;
    Button sign_up_btn_next;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_Date_OF_BIRTH = "birhtdate";

    // TODO: Rename and change types of parameters
    private String mDateOfBirth=null;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param dateOfBith Parameter 1.
     * @return A new instance of fragment sign_in.
     */
    // TODO: Rename and change types and number of parameters
    public static sign_up newInstance(String dateOfBith) {
        sign_up fragment = new sign_up();
        Bundle args = new Bundle();
        args.putString(ARG_Date_OF_BIRTH, dateOfBith);
        fragment.setArguments(args);
        return fragment;
    }

    public sign_up() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof onBtnNextClicked){
            btnNextlistener=(onBtnNextClicked) context;
        }else{
            throw  new RuntimeException("please implement onBtnNextClicked Listener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        btnNextlistener=null;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mDateOfBirth = getArguments().getString(ARG_Date_OF_BIRTH);
        }
    }

    private void saveSharedpreferences(){
        sharedpreferences = getActivity().getSharedPreferences("MyPREFERENCES", Context.MODE_PRIVATE);
        String firstname=txt_fisrt_name.getText().toString().trim();
        String lastname=txt_last_name.getText().toString().trim();
        int position=sp_postion.getSelectedItemPosition();
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString("firstname",firstname);
        editor.putString("lastname",lastname);
        editor.putInt("position",position);
        editor.commit();
        Toast.makeText(getActivity(),"preferences saved successfully", Toast.LENGTH_SHORT).show();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_sign_up, container, false);
        txt_fisrt_name=v.findViewById(R.id.sign_up_first_name);
        txt_last_name=v.findViewById(R.id.sign_up_last_name);
        title=v.findViewById(R.id.titlteFont);
        sign_up_btn_next=v.findViewById(R.id.sign_up_btn_next);
        sp_postion=v.findViewById(R.id.postion_spinner);
        date_picker=v.findViewById(R.id.txt_date_picker);


        //restore data from shared
        SharedPreferences channel=getActivity().getSharedPreferences("MyPREFERENCES", Context.MODE_PRIVATE);
        txt_fisrt_name.setText(channel.getString("firstname",""));
        txt_last_name.setText(channel.getString("lastname",""));
        sp_postion.setSelection(channel.getInt("position",0));


        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this.getContext(),
        R.array.postion_array, android.R.layout.simple_spinner_item);


        // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);


        // Apply the adapter to the spinner
            sp_postion.setAdapter(adapter);
        date_picker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveSharedpreferences();
                date_picker date=new date_picker();
                date.show(getActivity().getSupportFragmentManager(), null);
            }
        });

    //set the date that received from dialog date picker fragment
        if (mDateOfBirth!=null){
            date_picker.setText(mDateOfBirth);
        }
        sign_up_btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnNextlistener.btn_next_clicked(txt_fisrt_name.getText().toString().trim(),
                        txt_last_name.getText().toString().trim(),
                        sp_postion.getSelectedItem().toString().trim(),
                        date_picker.getText().toString().trim());
            }
        });

        return v ;
    }

    public  interface onBtnNextClicked{void btn_next_clicked(String first_name,
                                                             String last_name,
                                                             String postion,
                                                             String date_of_birth);}

}


