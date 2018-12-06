package com.kipdev.taigabugreport.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.kipdev.taigabugreport.R;

/**
 * Created by ik on 26.03.2018.
 */

public abstract class BaseFragment extends Fragment {

    protected String TAG = this.getClass().getSimpleName();
    protected String mSubTitle;
    private String mTitle;
    private View fragmentView;
    private View baseView;
    private View loadingView;

    protected abstract @LayoutRes
    int getLayoutId();

    public abstract void onCreateFragment(Bundle instance);

    public abstract void onCreateViewFragment(View view);

    public abstract void onViewCreatedFragment(View view, Bundle savedInstanceState);

    public abstract void onAttachFragment(Context context);

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, " *** OnCreate ***");
        onCreateFragment(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d(TAG, " *** OnCreateView ***");

        baseView = inflater.inflate(R.layout.taigabugreport_fragment_base, container, false);

        fragmentView = inflater.inflate(getLayoutId(), null, false);
        loadingView = baseView.findViewById(R.id.loading);

        ((FrameLayout)baseView.findViewById(R.id.content)).addView(fragmentView);

        onCreateViewFragment(fragmentView);

        return baseView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        Log.d(TAG, " *** onViewCreated ***");
        onViewCreatedFragment(view, savedInstanceState);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        onAttachFragment(context);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, " *** onDestroy ***");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG, " *** onPause ***");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, " *** onResume ***");
    }

    protected void showToast(String text) {
        Log.d(TAG, "Toast: " + text);
        Toast.makeText(getContext(), text, Toast.LENGTH_SHORT).show();
    }

    protected void showToast(int textId) {
        showToast(getString(textId));
    }

    public void hideSoftKeyboard() {
        if (getActivity().getCurrentFocus() != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
        }
    }

    protected void showProgressDialog() {
        if(loadingView != null)
            loadingView.setVisibility(View.VISIBLE);
    }

    protected void closeProgressDialog() {
        if(loadingView != null)
            loadingView.setVisibility(View.GONE);
    }

    public Boolean onBackPressUsed() {
        return false;
    }

}
