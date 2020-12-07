package edu.temple.webbrowserapp;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Parcel;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;



public class PageViewFragment extends Fragment implements Parcelable {


    View l;
    public WebView webView;

    String defaultURL = "https://www.google.com";

    PageViewInterface parentActivity;

    String Url;

    public PageViewFragment() {
        // Required empty public constructor
    }

    protected PageViewFragment(Parcel in) {
        Url = in.readString();
    }


    public static PageViewFragment newInstance(String url){
        Bundle args = new Bundle();
        args.putString("url", url);
        PageViewFragment pageViewFragment = new PageViewFragment();
        pageViewFragment.setArguments(args);
        return pageViewFragment;
    }

    public static final Creator<PageViewFragment> CREATOR = new Creator<PageViewFragment>() {
        @Override
        public PageViewFragment createFromParcel(Parcel in) {
            return new PageViewFragment(in);
        }

        @Override
        public PageViewFragment[] newArray(int size) {
            return new PageViewFragment[size];
        }
    };

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        webView.saveState(outState);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        if(context instanceof PageViewFragment.PageViewInterface)
        {
            parentActivity = (PageViewFragment.PageViewInterface) context;
        }
        else
        {
            throw new RuntimeException("implement PageViewInterface");
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        if(getArguments() != null){
            defaultURL = getArguments().getString("url");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        l = inflater.inflate(R.layout.fragment_page_display, container, false);
        webView = l.findViewById(R.id.webView);

        if(savedInstanceState != null)
        {
            webView.restoreState(savedInstanceState);
        }
        else {
            webView.loadUrl(defaultURL);
        }

        webView.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                parentActivity.updateURL(webView.getUrl());
            }
        });

        return l;
    }

    private String checkURL(String url){
        if(url.toLowerCase().contains("https://www.")){
            return url;
        }
        else if(url.toLowerCase().contains("https://")){
            return url;
        }
        else{
            return "https://www." + url;
        }
    }

    public void pressedGo(String url) {
        Url = url;
        webView.loadUrl(checkURL(Url));
    }

    public void pressedBack() {
        webView.goBack();
    }

    public void pressedForward() {
        webView.goForward();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(Url);
    }

    interface PageViewInterface{
        void updateURL(String url);
    }
}