package com.example.saojeong.adapter;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.recyclerview.widget.RecyclerView;

import com.example.saojeong.MainActivity;
import com.example.saojeong.R;
import com.example.saojeong.login.AllLoginManager;
import com.example.saojeong.login.LoginToken;
import com.example.saojeong.model.TutorialValue;
import com.example.saojeong.util.AlertBuilder;
import com.example.saojeong.util.ForecdTerminationService;

import java.util.List;

import lombok.SneakyThrows;

public class TutorialAdapter extends RecyclerView.Adapter<TutorialAdapter.ViewHolder> {
    private Activity mActivity;
    private Context mContext;

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        RelativeLayout rl_testTuto;
        LinearLayout ll_test0;
        LinearLayout ll_test1;
        Button login_facebook;
        Button login_kakaotalk;
        Button login_naver;
        Button login_google;
        Button login_guest;

        public ViewHolder(View itemView) {
            super(itemView);
            rl_testTuto = itemView.findViewById(R.id.intro_background);
            ll_test0 = itemView.findViewById(R.id.ll_test0);
            login_facebook = itemView.findViewById(R.id.login_facebook);
            login_kakaotalk = itemView.findViewById(R.id.login_kakaotalk);
            login_naver = itemView.findViewById(R.id.login_naver);
            login_google = itemView.findViewById(R.id.login_google);
            login_guest = itemView.findViewById(R.id.login_guest);
            login_facebook.setOnClickListener(this);
            login_kakaotalk.setOnClickListener(this);
            login_naver.setOnClickListener(this);
            login_google.setOnClickListener(this);
            login_guest.setOnClickListener(this);

        }

        @SneakyThrows
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.login_facebook:
                    AllLoginManager.getInstance().login("FACEBOOK", mActivity);

                    break;
                case R.id.login_kakaotalk:
                    AllLoginManager.getInstance().login("KAKAO", mActivity);
                    break;
                case R.id.login_naver:
                    AlertBuilder.createDialog(mActivity, "죄송합니다", "네이버와 협의중입니다.")
                            .show();
                    //loadStores(mActivity, "1");
                    break;
                case R.id.login_google:
                    AlertBuilder.createDialog(mActivity, "죄송합니다", "구글과 협의중입니다.")
                            .show();
//                    AllLoginManager.inst.login("GOOGLE", mActivity);
                    break;
                case R.id.login_guest:
                    AllLoginManager.getInstance().login("guest", mActivity);
                    break;
            }
        }
    }

    private List<TutorialValue> mContacts;

    public TutorialAdapter(List<TutorialValue> contacts, Activity activity, Context context) {
        mContacts = contacts;
        mActivity = activity;
        mContext = context;
    }

    @Override
    public TutorialAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View contactView = inflater.inflate(R.layout.item_tutorial, parent, false);

        TutorialAdapter.ViewHolder viewHolder = new TutorialAdapter.ViewHolder(contactView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(TutorialAdapter.ViewHolder holder, int position) {
        TutorialValue testTutomodel = mContacts.get(position);
        holder.rl_testTuto.setBackgroundResource(testTutomodel.GetImage());
        if (!testTutomodel.GetLogin()) {
            holder.ll_test0.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return mContacts.size();
    }
}