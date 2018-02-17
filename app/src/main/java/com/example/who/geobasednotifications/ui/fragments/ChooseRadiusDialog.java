package com.example.who.geobasednotifications.ui.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import com.example.who.geobasednotifications.R;
import com.example.who.geobasednotifications.utils.GetObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ChooseRadiusDialog extends DialogFragment {

    @BindView (R.id.et_radius_input)
    EditText inputText;
    @BindView (R.id.tv_radius_submit)
    TextView submitButton;

    private GetObject<String> radiusCallback;

    public static ChooseRadiusDialog newInstance(GetObject<String> radiusCallback) {
        ChooseRadiusDialog fragment = new ChooseRadiusDialog();
        fragment.setRadiusCallback(radiusCallback);
        return fragment;
    }

    private void setRadiusCallback(GetObject<String> radiusCallback){
        this.radiusCallback = radiusCallback;
    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Window window = getDialog().getWindow();
        if (window != null) window.setGravity(Gravity.CENTER);
        View v = inflater.inflate(R.layout.popup_choose_radius, container, false);
        ButterKnife.bind(this, v);
        return v;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if(getDialog().getWindow()!=null)
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
    }

    @OnClick(R.id.tv_radius_submit)
    void click(){
        String input = inputText.getText().toString();
        if(!TextUtils.isEmpty(input)){
            radiusCallback.get(input);
            dismiss();
        } else {inputText.setError(getString(R.string.radius_empty));}
    }
}
