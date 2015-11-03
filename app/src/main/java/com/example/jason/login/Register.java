package com.example.jason.login;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

public class Register extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.register);
		Button registerButton = (Button) this.findViewById(R.id.register_button);
		Button cancelButton = (Button) this.findViewById(R.id.cancel_button);
		ClickListener cl = new ClickListener();
		registerButton.setOnClickListener(cl);
		cancelButton.setOnClickListener(cl);
	}
	
	class ClickListener implements OnClickListener {		 
	    @Override
	    public void onClick(View v) {
	      switch (v.getId()) {
	      	case R.id.register_button:
	      		processRegister();
	      		break;
	      	case R.id.cancel_button:
	      		Intent intent = new Intent(Register.this,Login.class);
	      		startActivity(intent);
	      		break;
	      	default:
	      		break;
	      }
	    }
	 }

	public void processRegister() {
		EditText usernameText = (EditText) this.findViewById(R.id.username_edit);
		EditText passwordText = (EditText) this.findViewById(R.id.password_edit);
		EditText repasswordText = (EditText) this.findViewById(R.id.password_reinput_edit);
		EditText mailText = (EditText) this.findViewById(R.id.mail_edit);
		EditText firstNameText = (EditText) this.findViewById(R.id.firstname_edit);
		EditText lastNameText = (EditText) this.findViewById(R.id.lastname_edit);
		RadioGroup sexGroup = (RadioGroup) this.findViewById(R.id.radiogroup_sex);
		EditText phoneNumberText = (EditText) this.findViewById(R.id.phone_edit);
		
		boolean flag = true;
		
		String username = usernameText.getText().toString();
		flag = checkIsInput(username,"用户名未输入");
		if (!flag) return;
		
		String password = passwordText.getText().toString();
		flag = checkIsInput(password,"密码未输入");
		if (!flag) return;
		
		String repassword = repasswordText.getText().toString();
		flag = checkIsInput(repassword,"请确认您的密码");
		if (!flag) return;		
		if (!password.equals(repassword)) {
			Toast.makeText(Register.this, "两次输入密码不一样!",Toast.LENGTH_LONG ).show();
			return;
		}
		
		String mail = mailText.getText().toString();
		flag = checkIsInput(mail,"请输入您的邮箱");
		if (!flag) return;
		
		String firstName = firstNameText.getText().toString();
		flag = checkIsInput(firstName,"请输入您的姓");
		if (!flag) return;
		
		String lastName = lastNameText.getText().toString();
		flag = checkIsInput(lastName,"请输入您的名");
		if (!flag) return;
		
		String sex = "";
		RadioButton checkedRadio = (RadioButton) this.findViewById(sexGroup.getCheckedRadioButtonId());
        if (checkedRadio==null) {
        	Toast.makeText(Register.this, "请输入您的性别",Toast.LENGTH_LONG ).show();
        	return;
        }
		sex = checkedRadio.getText().toString();
		 
		flag = checkIsInput(sex,"请输入您的性别");
		if (!flag) return;
		
		String phoneNumber = phoneNumberText.getText().toString();
		flag = checkIsInput(phoneNumber,"请输入您的手机号码");
		if (!flag) return;
		
		HashMap<String,String> params = new HashMap<String,String>();
		params.put("userid", "1");
		params.put("username", username);
		params.put("password", password);
		params.put("mail", mail);
		params.put("firstName", firstName);
		params.put("lastName", lastName);
		params.put("sex", sex);
		params.put("phoneNumber", phoneNumber);
		
		String url = "http://ilab.tongji.edu.cn/pm25/web/restful/users/logon";
		run(params,url);
		
	}
	
	//检查输入是否有效
	private boolean checkIsInput(String value,String warning) {
		if (value==null||value.equals("")) {
			Toast.makeText(Register.this, warning,Toast.LENGTH_SHORT ).show();
			return false;
		}
		return true;
	}
	
	//上传数据到服务器匹配
	public void run(HashMap<String, String> params, String url) {
		RequestQueue requestQueue = Volley.newRequestQueue(this);
		
		JsonObjectRequest jsonRequest = new JsonObjectRequest(
			    Request.Method.POST, url,
			    new JSONObject(params), new Response.Listener<JSONObject>() {
			        public void onResponse(JSONObject response) {
			        	String status = null;
						try {
							status = response.getString("status");
							Log.e("status","status"+status);
							status = "0";
							Log.e("status","status"+status);
				        	if (status.equals("0")) {
				        		Toast.makeText(Register.this, "注册成功!",Toast.LENGTH_LONG ).show();
				        		Intent intent = new Intent(Register.this,MainActivity.class);
				        		startActivity(intent);
				        	} else {
				        		Toast.makeText(Register.this, "注册失败,用户名被占用！",Toast.LENGTH_LONG ).show();
				        	}
						} catch (JSONException e) {
							e.printStackTrace();
						}
			        }
			    }, new Response.ErrorListener() {
					public void onErrorResponse(VolleyError error) {
						
					}
			    }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Accept", "application/json");
                headers.put("Content-Type", "application/json; charset=UTF-8");
                return headers;
            }
        };
        jsonRequest.setRetryPolicy(new DefaultRetryPolicy(10 * 1000, 2, 1.0f));
		requestQueue.add(jsonRequest);
		requestQueue.start();
	}
		
}
