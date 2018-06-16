package com.example.bm.werewolf.Activity;

import android.animation.ValueAnimator;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;

import android.Manifest;
import com.example.bm.werewolf.R;
import com.example.bm.werewolf.Service.EventHandler;
import com.example.bm.werewolf.Service.OnClearFromRecentService;
import com.example.bm.werewolf.Service.VoiceCallService;
import com.example.bm.werewolf.Utils.UserDatabase;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.wang.avi.AVLoadingIndicatorView;

import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.agora.rtc.RtcEngine;

public class LoginActivity extends AppCompatActivity {
    private static final int REQUEST_PERMISSION = 1;
    private static final String TAG = "LoginActivity";

    CallbackManager callbackManager;
    @BindView(R.id.login_button)
    LoginButton loginButton;
    @BindView(R.id.background_one)
    ImageView backgroundOne;
    @BindView(R.id.background_two)
    ImageView backgroundTwo;
    @BindView(R.id.avi)
    AVLoadingIndicatorView avi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        setupPermission();

        startService(new Intent(getBaseContext(), OnClearFromRecentService.class));
        OnClearFromRecentService.activity = this;

        if (AccessToken.getCurrentAccessToken() != null && !AccessToken.getCurrentAccessToken().isExpired())
            process(AccessToken.getCurrentAccessToken());

        callbackManager = CallbackManager.Factory.create();

        loginButton.setReadPermissions(Arrays.asList("public_profile"));

        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                process(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                Log.d(TAG, "onCancel: cancel");
            }

            @Override
            public void onError(FacebookException exception) {
                Log.d(TAG, "onError: error");
            }
        });

        ValueAnimator animator = ValueAnimator.ofFloat(0f, 1f);
        animator.setRepeatCount(ValueAnimator.INFINITE);
        animator.setInterpolator(new LinearInterpolator());
        animator.setDuration(20000);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float translationX = -(float) animation.getAnimatedValue() * backgroundOne.getWidth();
                backgroundOne.setTranslationX(translationX);
                backgroundTwo.setTranslationX(translationX + backgroundOne.getWidth());
            }
        });
        animator.start();

        takeKeyHash();
    }

    private void takeKeyHash() {
        PackageInfo info;
        try {
            info = getPackageManager().getPackageInfo("com.example.bm.werewolf", PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md;
                md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                String something = new String(Base64.encode(md.digest(), 0));
                //String something = new String(Base64.encodeBytes(md.digest()));
                Log.e("hash key", something);
            }
        } catch (PackageManager.NameNotFoundException e1) {
            Log.e("name not found", e1.toString());
        } catch (NoSuchAlgorithmException e) {
            Log.e("no such an algorithm", e.toString());
        } catch (Exception e) {
            Log.e("exception", e.toString());
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    public void process(AccessToken accessToken) {
        avi.show();
        GraphRequest graphRequest = GraphRequest.newMeRequest(
                accessToken,
                new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        String id = null;
                        String name = null;
                        try {
                            id = object.getString("id");
                            name = object.getString("name");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        UserDatabase.facebookID = id;
                        UserDatabase.getInstance().accessUser(name, LoginActivity.this);
                    }
                }
        );

        graphRequest.executeAsync();
    }

    private void setupPermission() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_DENIED) {
                ActivityCompat.requestPermissions(
                        this,
                        new String[]{Manifest.permission.RECORD_AUDIO   },
                        REQUEST_PERMISSION
                );
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_PERMISSION) {
            if (grantResults[0] == PackageManager.PERMISSION_DENIED) {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Warning!")
                        .setMessage("Without permission you can not use this app. " +
                                "Do you want to grant permission?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                ActivityCompat.requestPermissions(
                                        LoginActivity.this,
                                        new String[]{Manifest.permission.RECORD_AUDIO},
                                        REQUEST_PERMISSION
                                );
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                LoginActivity.this.finish();
                            }
                        })
                        .show();
            }
        }
    }
}
