package com.example.ahgzly_ml3b.fragments;

import android.animation.Animator;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.airbnb.lottie.LottieAnimationView;
import com.example.ahgzly_ml3b.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.concurrent.TimeUnit;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link code_verification#newInstance} factory method to
 * create an instance of this fragment.
 */
public class code_verification extends Fragment {
    LottieAnimationView code_verification_btn_lotti_animation;
    DatabaseReference firebaseDatabase;
    FirebaseUser firebaseUser;
    LinearLayout code_verification_lin_verifiy,code_verification_lin_set_phone;
    btnPressed btnPressedListener;
    userAuthnticationDone authnticationDoneStateListener;
    EditText codeNum1,codeNum2,codeNum3,codeNum4,codeNum5,codeNum6,code_verification_txt_phone;
    Button code_verification_btn_submit,code_verification_btn_send_code;
    TextView code_verification_btn_resend_code;
    private PhoneAuthProvider.ForceResendingToken forceResendingToken;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    private String mVerificationId; //will hold verificationCode
    private static String TAG;
    private FirebaseAuth firebaseAuth;
    ProgressDialog pd;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_FIRSTNAME = "FIRSTNAME";
    private static final String ARG_LASTNAME = "LASTNAME";
    private static final String ARG_POSTION = "POSTION";
    private static final String ARG_DATEOFBIRTH = "DATEOFBIRTH";

    // TODO: Rename and change types of parameters
    private String mfirstname;
    private String mlastname;
    private String mpostion;
    private String mdateofbirht;

    public code_verification() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param firstname Parameter 1.
     * @param lastname Parameter 2.
     * @param postion Parameter 2.
     * @param date_of_birht Parameter 2.
     * @return A new instance of fragment code_verification.
     */
    // TODO: Rename and change types and number of parameters
    public static code_verification newInstance(String firstname, String lastname,String postion,String date_of_birht) {
        code_verification fragment = new code_verification();
        Bundle args = new Bundle();
        args.putString(ARG_FIRSTNAME, firstname);
        args.putString(ARG_LASTNAME, lastname);
        args.putString(ARG_POSTION, postion);
        args.putString(ARG_DATEOFBIRTH, date_of_birht);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof userAuthnticationDone){
            authnticationDoneStateListener=(userAuthnticationDone) context;
        }else {
            throw  new RuntimeException("please implement userAuthnticationDone");
        }
        if (context instanceof btnPressed){
            btnPressedListener=(btnPressed) context;
        }else {
            throw  new RuntimeException("please implement btnPressed");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        btnPressedListener=null;
        authnticationDoneStateListener=null;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mfirstname = getArguments().getString(ARG_FIRSTNAME);
            mlastname= getArguments().getString(ARG_LASTNAME);
            mpostion= getArguments().getString(ARG_POSTION);
            mdateofbirht= getArguments().getString(ARG_DATEOFBIRTH);
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_code_verification, container, false);
        firebaseAuth=FirebaseAuth.getInstance();
        code_verification_lin_set_phone=v.findViewById(R.id.code_verification_lin_set_phone);
        code_verification_lin_verifiy=v.findViewById(R.id.code_verification_lin_verifiy);
        code_verification_txt_phone=v.findViewById(R.id.code_verification_txt_phone);
        code_verification_btn_send_code=v.findViewById(R.id.code_verification_btn_send_code);
        code_verification_btn_submit=v.findViewById(R.id.code_verification_btn_submit);
        code_verification_btn_resend_code=v.findViewById(R.id.code_verification_btn_resend_code);
        codeNum1=v.findViewById(R.id.code_verification_txt_number1);
        codeNum2=v.findViewById(R.id.code_verification_txt_number2);
        codeNum3=v.findViewById(R.id.code_verification_txt_number3);
        codeNum4=v.findViewById(R.id.code_verification_txt_number4);
        codeNum5=v.findViewById(R.id.code_verification_txt_number5);
        codeNum6=v.findViewById(R.id.code_verification_txt_number6);
        code_verification_btn_lotti_animation=v.findViewById(R.id.code_verification_lot_animation);
        firebaseDatabase=FirebaseDatabase.getInstance().getReference().child("playerUsers");
        pd=new ProgressDialog(getActivity());
        pd.setTitle("من فضلك انتظر");
        pd.setCanceledOnTouchOutside(false);



        mCallbacks=new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                signInWithPhoneAuthCredentia(phoneAuthCredential);
            }

            @Override
            public void onVerificationFailed(@NonNull FirebaseException e) {
                pd.dismiss();
                Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCodeSent(@NonNull String vrificationId, @NonNull PhoneAuthProvider.ForceResendingToken token) {
                super.onCodeSent(vrificationId, forceResendingToken);
                Log.d(TAG,"onCodeSent: "+ vrificationId );
                mVerificationId=vrificationId;
                forceResendingToken=token;
                pd.dismiss();

                //Hide phone layout and show code layout
                code_verification_lin_set_phone.setVisibility(View.VISIBLE);
                code_verification_lin_verifiy.setVisibility(View.GONE);

                Toast.makeText(getActivity(), "تم ارسال الكود", Toast.LENGTH_SHORT).show();
            }
        };
        code_verification_btn_send_code.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                firebaseAuth.setLanguageCode("ar");
                if (code_verification_txt_phone.getText().toString().trim().isEmpty()){
                    Toast.makeText(getActivity(),"من فضلك ادخل جميع البيانات ",Toast.LENGTH_SHORT).show();
                }else{
                    startPhoneNumberVerfication(code_verification_txt_phone.getText().toString().trim());
                }
            }
        });



        code_verification_btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String code;
                if (TextUtils.isEmpty(codeNum1.getText().toString().trim())||
                        TextUtils.isEmpty(codeNum2.getText().toString().trim())||
                        TextUtils.isEmpty(codeNum3.getText().toString().trim())||
                        TextUtils.isEmpty(codeNum4.getText().toString().trim())||
                        TextUtils.isEmpty(codeNum5.getText().toString().trim())||
                        TextUtils.isEmpty(codeNum6.getText().toString().trim())){

                    Toast.makeText(getActivity(), "من فضلك ادخل الكود", Toast.LENGTH_SHORT).show();

                }
                    code=codeNum1.getText().toString().trim()+
                        codeNum2.getText().toString().trim()+
                        codeNum3.getText().toString().trim()+
                        codeNum4.getText().toString().trim()+
                        codeNum5.getText().toString().trim()+
                        codeNum6.getText().toString().trim();
                submitCodeVerification(code_verification_txt_phone.getText().toString().trim(),code);


            }
        });

        code_verification_btn_resend_code.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resendCodeVerification(code_verification_txt_phone.getText().toString().trim(), forceResendingToken);
            }
        });

        v.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if( keyEvent.getAction()==KeyEvent.ACTION_DOWN )
                {
                    btnPressedListener.btn_Clicked("back");
                    return true;
                }
                return false;
            }
        });
        return v;
    }

    private void startPhoneNumberVerfication(String phoneNumber) {
        pd.setMessage("جاري ارسال الرمز ...");
        pd.show();

        PhoneAuthOptions options=
                PhoneAuthOptions.newBuilder(firebaseAuth)
                .setPhoneNumber("+20"+code_verification_txt_phone.getText().toString().trim())
                .setTimeout(60L,TimeUnit.SECONDS)
                .setActivity(getActivity())
                .setCallbacks(mCallbacks)
                .build();
        PhoneAuthProvider.verifyPhoneNumber(options);



    }

    private void resendCodeVerification(String phoneNumber,PhoneAuthProvider.ForceResendingToken token) {
        pd.setMessage("جاري اعادة ارسال الكود ...");
        pd.show();

        PhoneAuthOptions options=
                PhoneAuthOptions.newBuilder(firebaseAuth)
                        .setPhoneNumber("+20"+code_verification_txt_phone.getText().toString().trim())
                        .setTimeout(60L,TimeUnit.SECONDS)
                        .setActivity(getActivity())
                        .setCallbacks(mCallbacks)
                        .setForceResendingToken(token)
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }

    private void submitCodeVerification(String phoneNumber ,String code) {
        pd.setMessage("جاري التاكد من الكود");
        pd.show();

        PhoneAuthCredential credential=PhoneAuthProvider.getCredential(mVerificationId,code);
        signInWithPhoneAuthCredentia(credential);
    }

    private void signInWithPhoneAuthCredentia(PhoneAuthCredential credential) {
        pd.setMessage("تم التسجيل بنجاح");
        firebaseAuth.signInWithCredential(credential)
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        pd.dismiss();
                        Toast.makeText(getActivity(), "phone = " +firebaseAuth.getCurrentUser().getPhoneNumber(), Toast.LENGTH_SHORT).show();
                        code_verification_lin_set_phone.setVisibility(View.GONE);
                        code_verification_btn_lotti_animation.setVisibility(View.VISIBLE);
                        code_verification_btn_lotti_animation.addAnimatorListener(new Animator.AnimatorListener() {
                            @Override
                            public void onAnimationStart(Animator animator) {

                            }

                            @Override
                            public void onAnimationEnd(Animator animator) {
                                authnticationDoneStateListener.authState("done");
                            }

                            @Override
                            public void onAnimationCancel(Animator animator) {

                            }

                            @Override
                            public void onAnimationRepeat(Animator animator) {

                            }
                        });
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        pd.dismiss();

                        Toast.makeText(getActivity(), "فشل", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public interface btnPressed{void btn_Clicked(String clicked);}
    public interface userAuthnticationDone{void authState(String state);}

}