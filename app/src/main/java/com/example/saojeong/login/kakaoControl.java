package com.example.saojeong.login;

import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.util.Log;
import androidx.annotation.Nullable;
import com.kakao.auth.AuthType;
import com.kakao.auth.ISessionCallback;
import com.kakao.auth.KakaoSDK;
import com.kakao.auth.Session;
import com.kakao.network.ErrorResult;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.LogoutResponseCallback;
import com.kakao.usermgmt.callback.MeV2ResponseCallback;
import com.kakao.usermgmt.response.MeV2Response;
import com.kakao.util.exception.KakaoException;

public class kakaoControl implements ISessionCallback, LoginControl {
    private LoginControl.LoginHandler handler;
    public static void init(Application application) {
        if(KakaoSDK.getAdapter()==null)
            KakaoSDK.init(new kakaoSDKAdapter(application));
    }

    public kakaoControl(LoginControl.LoginHandler loginHandler) {
        this.handler = loginHandler;

        UserManagement.getInstance().me(new MeV2ResponseCallback() {
            @Override
            public void onSessionClosed(ErrorResult errorResult) {

            }

            @Override
            public void onSuccess(MeV2Response result) {
                kakaoControl.this.handler.success();
            }
        });

    }

    public void Login(Activity activity) {
        Session currentSession = Session.getCurrentSession();
        currentSession.addCallback(this);
        currentSession.open(AuthType.KAKAO_TALK, activity);
    }

    public void Logout() {
        UserManagement.getInstance().requestLogout(new LogoutResponseCallback() {
            public void onCompleteLogout() {
            }
        });
    }

    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        Session.getCurrentSession().handleActivityResult(requestCode, resultCode, data);

    }

    public void onSessionOpened() { //콜백
        kakaoControl.this.handler.success();
    }

    public void onSessionOpenFailed(KakaoException kakaoException) {
        kakaoControl.this.handler.error(kakaoException);
    }

}
