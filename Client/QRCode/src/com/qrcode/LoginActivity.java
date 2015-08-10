package com.qrcode;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.MarshalBase64;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.config.services.MyApp;
import com.config.services.WebService;
import com.qrcode.R;
 
public class LoginActivity extends Activity {
	 
    
	private static final int GET_USER_INFO = 1;
	private static final int SAVE_USER_INFO = 0;
	private UserLoginTask mAuthTask = null;
	private String mName;
	private String mPassword;
	private String userType = null;
	private MyApp myapp;
	private EditText mNameView;
	private EditText mPasswordView;
	private View mLoginFormView;
	private View mLoginStatusView;
	private AutoCompleteTextView mURLView;
	private TextView mLoginStatusMessageView;
	private CheckBox mIsRememberPasswordView;
	
	private SharedPreferences mSP;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_login);

		myapp=(MyApp)getApplication();
		mSP = getSharedPreferences("UserInfo", MODE_PRIVATE);
		//Set up the login form.
		//mName = getIntent().getStringExtra(EXTRA_EMAIL);
		mNameView = (EditText) findViewById(R.id.email);

		mPasswordView = (EditText) findViewById(R.id.password);
		mPasswordView
				.setOnEditorActionListener(new TextView.OnEditorActionListener() {
					@Override
					public boolean onEditorAction(TextView textView, int id,
							KeyEvent keyEvent) {
						if (id == R.id.login || id == EditorInfo.IME_NULL) {
							attemptLogin();
							return true;
						}
						return false;
					}
				});

		mLoginFormView = findViewById(R.id.login_form);
		mLoginStatusView = findViewById(R.id.login_status);
		mURLView = (AutoCompleteTextView)findViewById(R.id.spinner_urls);
		mURLView.setThreshold(0);
		mURLView.setText(mSP.getString("lastURL", "http://192.168.0.116:9998"));
		String[] strURLs = getResources().getStringArray(R.array.urls);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line,strURLs);
		mURLView.setAdapter(adapter);
		
		mLoginStatusMessageView = (TextView) findViewById(R.id.login_status_message);
		mIsRememberPasswordView = (CheckBox)findViewById(R.id.checkIsPassword);
		mIsRememberPasswordView.setChecked(mSP.getBoolean("isChecked", false));
		mIsRememberPasswordView.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				 if(isChecked){
					 mSP.edit().putBoolean("isChecked", true).commit();
				 }else{
					 mSP.edit().putBoolean("isChecked", false).commit();
				 }
			}
		});

		findViewById(R.id.sign_in_button).setOnClickListener(
				new  OnClickListener() {
					@Override
					public void onClick(View view) {
						myapp.setURL(mURLView.getText().toString()+"/Service1.asmx");
						mSP.edit().putString("lastURL", mURLView.getText().toString());
						attemptLogin();
						CheckIsRemember(SAVE_USER_INFO);
					}
				});	
		CheckIsRemember(GET_USER_INFO);
	}

	private void CheckIsRemember(int operate) {
		 if(mIsRememberPasswordView.isChecked()){
			 switch (operate) {
			case 1:
				mName =  mSP.getString("username", "");
				mPassword = mSP.getString("password", "");
				mNameView.setText(mName);
				myapp.setname(mName);
				mPasswordView.setText(mPassword);
				break;
			case 0:
				mName = mNameView.getText().toString();
				mPassword = mPasswordView.getText().toString();
				myapp.setname(mName);
				mSP.edit().putString("username", mName).commit();
				mSP.edit().putString("password", mPassword).commit();
				break;
			}
		 }else{
			 switch (operate) {
			case 1:
				mName =  mSP.getString("username", "");
				mNameView.setText(mName);
				myapp.setname(mName);
				break;
			default:
				break;
			}
		 }
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		//getMenuInflater().inflate(R.menu.patrol_list, menu);
		return true;
	}

	/**
	 * Attempts to sign in or register the account specified by the login form.
	 * If there are form errors (invalid email, missing fields, etc.), the
	 * errors are presented and no actual login attempt is made.
	 */
	public void attemptLogin() {
		if (mAuthTask != null) {
			return;
		}

		// Reset errors.
		mNameView.setError(null);
		mPasswordView.setError(null);

		// Store values at the time of the login attempt.
		mName = mNameView.getText().toString();
		mPassword = mPasswordView.getText().toString();

		boolean cancel = false;
		View focusView = null;

		// Check for a valid password.
		if (TextUtils.isEmpty(mPassword)) {
			mPasswordView.setError(getString(R.string.error_incorrect_password));
			focusView = mPasswordView;
			cancel = true;
		} 
//		else if (mPassword.length() < 4) {
//			mPasswordView.setError(getString(R.string.error_invalid_password));
//			focusView = mPasswordView;
//			cancel = true;
//		}

		// Check for a valid email address.
		if (TextUtils.isEmpty(mName)) {
			mNameView.setError(getString(R.string.error_field_required));
			focusView = mNameView;
			cancel = true;
		} 
//		else if (!mName.contains("@")) {
//			mNameView.setError(getString(R.string.error_invalid_email));
//			focusView = mNameView;
//			cancel = true;
//		}

		if (cancel) {
			// There was an error; don't attempt login and focus the first
			// form field with an error.
			focusView.requestFocus();
		} else {
			// Show a progress spinner, and kick off a background task to
			// perform the user login attempt.
			mLoginStatusMessageView.setText(R.string.login_progress_signing_in);
			showProgress(true);
			mAuthTask = new UserLoginTask();
			mAuthTask.execute((Void) null);
		}
	}

	/**
	 * Shows the progress UI and hides the login form.
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
	private void showProgress(final boolean show) {
		// On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
		// for very easy animations. If available, use these APIs to fade-in
		// the progress spinner.
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
			int shortAnimTime = getResources().getInteger(
					android.R.integer.config_shortAnimTime);

			mLoginStatusView.setVisibility(View.VISIBLE);
			mLoginStatusView.animate().setDuration(shortAnimTime)
					.alpha(show ? 1 : 0)
					.setListener(new AnimatorListenerAdapter() {
						@Override
						public void onAnimationEnd(Animator animation) {
							mLoginStatusView.setVisibility(show ? View.VISIBLE
									: View.GONE);
						}
					});

			mLoginFormView.setVisibility(View.VISIBLE);
			mLoginFormView.animate().setDuration(shortAnimTime)
					.alpha(show ? 0 : 1)
					.setListener(new AnimatorListenerAdapter() {
						@Override
						public void onAnimationEnd(Animator animation) {
							mLoginFormView.setVisibility(show ? View.GONE
									: View.VISIBLE);
						}
					});
		} else {
			// The ViewPropertyAnimator APIs are not available, so simply show
			// and hide the relevant UI components.
			mLoginStatusView.setVisibility(show ? View.VISIBLE : View.GONE);
			mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
		}
	}

	/**
	 * Represents an asynchronous login/registration task used to authenticate
	 * the user.
	 */
	public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {
		@Override
		protected Boolean doInBackground(Void... params) {
			// TODO: attempt authentication against a network service.

			//try {
				// Simulate network access.
				//Thread.sleep(1000);
			//} catch (InterruptedException e) {
				//return false;
			//}
			
			//webservice配置信息从config中读取
			
			SoapObject rpc = new SoapObject(WebService.NAMESPACE, WebService.MethodName_Login);
			
			rpc.addProperty("username", mName);
			rpc.addProperty("password",mPassword);
			
			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
			
			envelope.bodyOut = rpc;  
			// 注册Envelope
			(new MarshalBase64()).register(envelope);
	        // 设置是否调用的是dotNet开发的WebService  
	        envelope.dotNet = true;  
	        // 等价于envelope.bodyOut = rpc;  
	        envelope.setOutputSoapObject(rpc);  
	  
	        HttpTransportSE transport = new HttpTransportSE(myapp.getURL());  
	        transport.debug=true;
	        try {  
	            // 调用WebService  
	            transport.call(WebService.SoapAction_Login, envelope);  
	            Log.e("mcy", "login");
	        } catch (Exception e) {  
	            e.printStackTrace();
	        }  
	  
	        Object response = null;
	        if(envelope.bodyIn!=null){
	        	response = (Object) envelope.bodyIn;
				userType = response.toString().trim();
	        }
			
	        if(userType==null){
	        	return false;
	        }else{
	        	if(userType.equals("登陆失败"))
				{
					return false;
				}else {
					return true;
				}
	        }
		}

		@Override
		protected void onPostExecute(final Boolean success) {
			mAuthTask = null;
			if (success) {
				myapp.setname(mName);
				Intent intent =new Intent();
				intent.setClass(LoginActivity.this, MenuActivity.class);
				startActivity(intent);
				finish();
			} else {
				showProgress(false);
				Toast.makeText(LoginActivity.this, "登录失败...", Toast.LENGTH_LONG).show();
				mPasswordView
						.setError(getString(R.string.error_incorrect_password));
			}
		}

		@Override
		protected void onCancelled() {
			mAuthTask = null;
			showProgress(false);
		}
	}
}
