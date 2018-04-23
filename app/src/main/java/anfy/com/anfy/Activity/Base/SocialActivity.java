package anfy.com.anfy.Activity.Base;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.DefaultLogger;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterConfig;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterAuthClient;
import com.twitter.sdk.android.core.identity.TwitterLoginButton;
import com.twitter.sdk.android.core.models.User;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import anfy.com.anfy.App.AppController;
import anfy.com.anfy.Model.SocialUser;
import anfy.com.anfy.R;
import butterknife.BindView;
import retrofit2.Call;


/**
 * Created by Ahmed on 9/16/2017.
 */

public abstract class SocialActivity extends FragmentSwitchActivity implements  GoogleApiClient.OnConnectionFailedListener {

    private static final String TAG = "PhoneLoginActivity";



    protected TwitterAuthClient mTwitterAuthClient ;
    @BindView(R.id.fb_login_button)
    public LoginButton loginButton;
    @BindView(R.id.twitter_login_button)
    public TwitterLoginButton TwitterloginButton;
    protected GoogleApiClient mGoogleApiClient;
    @BindView(R.id.google_sign_in)
    public SignInButton google_sn;


    protected CallbackManager mFacebookCallbackManager;
    protected AccessToken accessToken;
    private static final int RC_SIGN_IN = 007;


    private static final String CONSUMER_KEY = "58zd0SXD4nKsIGnYkjIcTPdDd";
    private static final String CONSUMER_SECRET = "3rdCItPpiJWQaTq16x7GNw418bMe63U6VURort4MmINg1m4fIg";


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSHA1Code();
        TwitterConfig config = new TwitterConfig.Builder(this)
                .logger(new DefaultLogger(Log.DEBUG))
                .twitterAuthConfig(new TwitterAuthConfig(CONSUMER_KEY, CONSUMER_SECRET))
                .debug(true)
                .build();
        Twitter.initialize(config);


        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);


        mFacebookCallbackManager = CallbackManager.Factory.create();

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
    }



    public void getSHA1Code() {

        // Add code to print out the key hash
        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    getApplicationContext().getPackageName(),
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.e("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {
            Log.e("KeyHash:",e.toString());


        } catch (NoSuchAlgorithmException e) {
            Log.e("KeyHash:",e.toString());
        }
    }



    private void handleSignInResult(GoogleSignInResult result) {


        Log.e(TAG, "handleSignInResult:" + result.isSuccess()+"result "+result);
        if (result.isSuccess()) {


            // Signed in successfully, show authenticated UI.
            GoogleSignInAccount acct = result.getSignInAccount();

            Log.e(TAG, "display name: " + acct.getDisplayName() + acct.getId());

            String personName = acct.getDisplayName();
            String personPhotoUrl = AppController.TEMP_IMAGE_URL;
            if(acct.getPhotoUrl() != null) {
                personPhotoUrl = acct.getPhotoUrl().toString();

            }
            String email = acct.getEmail();
            String id = acct.getId();
            String name = acct.getDisplayName();

            Log.e(TAG, "Name: " + personName + ", email: " + email
                    + ", Image: " + personPhotoUrl);
            SocialUser userModel =  new SocialUser() ;
            userModel.setType(SocialUser.GOOGLE_SOCIAL_TYPE);

            userModel.setName(name);
            userModel.setEmail(email);
            userModel.setPhoto(personPhotoUrl);
            userModel.setUid(id);
            social_login(userModel);
            signOut();

        } else {
            // Signed out, show unauthenticated UI.
            Log.e(TAG, "google sign in result :" + result.isSuccess() +" cuz" + result.getStatus() + " ");

//            showLoading(false);
//            Toast.makeText(getApplicationContext() ,getString(R.string.no_conn) , Toast.LENGTH_LONG).show();
        }
    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Make sure that the loginButton hears the result from any
        // Activity that it triggered.
        Log.e("data result",requestCode+"  " );
        Log.e("data result",requestCode+"  " + FacebookSdk.isFacebookRequestCode(requestCode) );

        if (FacebookSdk.isFacebookRequestCode(requestCode))
            mFacebookCallbackManager.onActivityResult(requestCode, resultCode, data);

        else   if(requestCode == TwitterAuthConfig.DEFAULT_AUTH_REQUEST_CODE)
            TwitterloginButton.onActivityResult(requestCode, resultCode, data);

            // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        else   if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }
        else
            showLoading(false);;



    }

    protected void facebook_login(){
        // If the access token is available already assign it.
        accessToken = AccessToken.getCurrentAccessToken();


//        List< String > permissionNeeds = Arrays.asList("user_photos", "email",
//                "user_birthday", "public_profile", "AccessToken");
        loginButton.setReadPermissions("email");
//        loginButton.setReadPermissions(permissionNeeds);
        loginButton.registerCallback(mFacebookCallbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {

                        Log.e(TAG , "face" + "onSuccess");

                        String accessToken = loginResult.getAccessToken()
                                .getToken();
                        Log.e("accessToken", accessToken);

                        GraphRequest request = GraphRequest.newMeRequest(
                                loginResult.getAccessToken(),
                                new GraphRequest.GraphJSONObjectCallback() {@Override
                                public void onCompleted(JSONObject object,
                                                        GraphResponse response) {

                                    Log.e("PhoneLoginActivity",
                                            response.toString());

                                    try {
                                        String id = object.getString("id");
                                        URL profile_pic = null ;
                                        try {
                                            profile_pic = new URL(
                                                    "http://graph.facebook.com/" + id + "/picture?type=large");
                                            Log.e("profile_pic",
                                                    profile_pic + "");

                                        } catch (MalformedURLException e) {
                                            e.printStackTrace();
                                            showLoading(false);;

                                        }
                                        SocialUser userModel =  new SocialUser() ;
                                        userModel.setType(SocialUser.FACEBOOK_SOCIAL_TYPE);


                                        String  name = object.getString("name");
                                        try{
                                            String  email = object.getString("email");
                                            userModel.setEmail(email);
                                        }catch (Exception e){}

                                        userModel.setName(name);

                                        userModel.setUid(id);
                                        if (profile_pic != null)
                                            userModel.setPhoto(String.valueOf(profile_pic));
                                        else
                                            userModel.setPhoto(String.valueOf(AppController.TEMP_IMAGE_URL));

                                        Log.e(TAG , userModel.getUid() + userModel.getName());
                                        social_login(userModel);
//                                    gender = object.getString("gender");
//                                    birthday = object.getString("birthday");
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                        showLoading(false);;

                                    }




                                }
                                });
                        Bundle parameters = new Bundle();
                        parameters.putString("fields",
                                "id,name,email");
                        request.setParameters(parameters);
                        request.executeAsync();
                    }

                    @Override
                    public void onCancel() {
                        showLoading(false);;
                        System.out.println("onCancel");

                    }

                    @Override
                    public void onError(FacebookException exception) {
                        showLoading(false);
                        System.out.println("onError");
                        Log.e("PhoneLoginActivity", exception.toString());
                        Toast.makeText(getApplicationContext() ,getString(R.string.no_internet) , Toast.LENGTH_LONG).show();

                    }

                });


    }
    //en face book
    public void twitter_login(){

        TwitterloginButton.setCallback(new Callback<TwitterSession>() {
            @Override
            public void success(Result<TwitterSession> result) {
                // The TwitterSession is also available through:
                // Twitter.getInstance().core.getSessionManager().getActiveSession()
                TwitterSession session = result.data;

                Call<User> user = TwitterCore.getInstance().getApiClient().getAccountService().verifyCredentials(false, false,true);
                user.enqueue(new Callback<User>() {
                    @Override
                    public void success(Result<User> userResult) {
                        String name = userResult.data.name;
                        long id = userResult.data.id;
                        String photo = userResult.data.profileImageUrlHttps;


                        SocialUser userModelModel =  new SocialUser() ;
                        userModelModel.setType(SocialUser.TWITTER_SOCIAL_TYPE);


                        try{
                            userModelModel.setUid(String.valueOf(id));
                        }catch (Exception e){}

                        try{
                            userModelModel.setName(name);
                        }catch (Exception e){}


                        try{
                            userModelModel.setPhoto(photo);
                        }catch (Exception e){}


                        try{
                            userModelModel.setEmail(userResult.data.email);
                        }catch (Exception e){}

                        social_login(userModelModel);
                        Log.e(photo + name + id,"photo");


                    }

                    @Override
                    public void failure(TwitterException exc) {
                        Log.e("TwitterKit", "Verify Credentials Failure", exc);
                        showLoading(false);;
                    }
                });
                // with your app's user model
                String msg = "@" + session.getUserName() + " logged in! (#" + session.getUserId() + ")";
                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
                CookieSyncManager.createInstance(SocialActivity.this);
                CookieManager cookieManager = CookieManager.getInstance();
                cookieManager.removeSessionCookie();
                TwitterCore.getInstance().getSessionManager().clearActiveSession();
            }
            @Override
            public void failure(TwitterException exception) {
                showLoading(false);;
                Log.e("TwitterKit", "Login with Twitter failure", exception);
            }
        });
    }
    //end twitter


    protected void google_login(){
        signIn();
    }
    protected void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }



    protected void revokeAccess() {
        Auth.GoogleSignInApi.revokeAccess(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                    }
                });
    }
    protected void signOut() {
        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                    }
                });
    }



    @Override
    public void onStart() {
        super.onStart();
        OptionalPendingResult<GoogleSignInResult> opr = Auth.GoogleSignInApi.silentSignIn(mGoogleApiClient);
        if (opr.isDone()) {
            // If the user's cached credentials are valid, the OptionalPendingResult will be "done"
            // and the GoogleSignInResult will be available instantly.
            Log.e(TAG, "Got cached sign-in");
            GoogleSignInResult result = opr.get();
            handleSignInResult(result);


        } else {
            // If the user has not previously signed in on this device or the sign-in has expired,
            // this asynchronous branch will attempt to sign in the user silently.  Cross-device
            // single sign-on will occur in this branch.
            opr.setResultCallback(new ResultCallback<GoogleSignInResult>() {
                @Override
                public void onResult(GoogleSignInResult googleSignInResult) {
                    handleSignInResult(googleSignInResult);

                }
            });

        }


    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        // An unresolvable error has occurred and Google APIs (including Sign-In) will not
        // be available.
//        Log.e(TAG, "onConnectionFailed:" + connectionResult);
//         showLoading(false);
//        Toast.makeText(getApplicationContext() ,getString(R.string.no_conn) , Toast.LENGTH_LONG).show();

    }



    public void twitter_on_click(){
        mTwitterAuthClient = new TwitterAuthClient();
        showLoading(true);
        twitter_login();
        TwitterloginButton.performClick();
    }

    public void facebook_on_click()
    {
       showLoading(true);
        try{
            LoginManager.getInstance().logOut();
        }catch (Exception e){}
        facebook_login();
        loginButton.performClick();
    }


   public void google_on_click(){
        showLoading(true);
        google_login();
    }


    socialLoginInterface socialLoginInterface ;
    public void setSocialLoginInterface(SocialActivity.socialLoginInterface socialLoginInterface) {
        this.socialLoginInterface = socialLoginInterface;
    }

    protected void social_login (SocialUser user){
        if (user != null && socialLoginInterface !=null)
        socialLoginInterface.social_login(user);
    }

    public interface  socialLoginInterface {
        void  social_login(SocialUser user) ;
    }

}
