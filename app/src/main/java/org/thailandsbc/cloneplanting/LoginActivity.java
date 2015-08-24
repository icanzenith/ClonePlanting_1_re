package org.thailandsbc.cloneplanting;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;

import org.thailandsbc.cloneplanting.database.Preference;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends FragmentActivity {

    private AQuery aq;
    private GsonTransformer gsonTransformer;
    private Preference mLoginSession;
    private BaseApplication mApplication;
    private String LOGIN_URL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Check Session Login
        mLoginSession = new Preference(this);
        if (mLoginSession.isLoggedIn()) {
            Intent intent = new Intent(this, Activity_News.class);
            startActivity(intent);
            finish();
        }

        LOGIN_URL = getResources().getString(R.string.URL_LOGIN);

        // SetcontentView
        setContentView(R.layout.fragment_login);

        // get for use Global variable!!!
        mApplication = (BaseApplication)this.getApplication();

        // Call Initialize Widget
        InitailizeWidget();

        bt_Login.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
//				Intent intent = new Intent(LoginActivity.this,PlantingActivity.class);
//				startActivity(intent);
                if (edt_username.getText().toString() != ""
                        && edt_password.getText().toString() != "") {
                    username = edt_username.getText().toString().trim();
                    password = edt_password.getText().toString().trim();
                    StartLogin();
                } else {
                    Toast.makeText(LoginActivity.this,
                            "Please enter Username and Password",
                            Toast.LENGTH_LONG).show();
                }

            }
        });
    }

    private Button bt_Login;
    private EditText edt_username, edt_password;
    private ProgressBar mBar;
    private LinearLayout mContainer;
    private String username, password;

    // Initailize Widget
    public void InitailizeWidget() {
        mContainer = (LinearLayout) findViewById(R.id.loginformcontainer);
        edt_username = (EditText) findViewById(R.id.editText_username);
        edt_password = (EditText) findViewById(R.id.editText_password);
        bt_Login = (Button) findViewById(R.id.bt_login);
        mBar = (ProgressBar) findViewById(R.id.progressbar_loadinglogin);
    }



    private Map<String, Object> params = new HashMap<>();
    private boolean isLoadComplete = false;

    public void StartLogin() {

        // check edt != null
        if (!edt_username.getText().toString().equals("")
                && !edt_password.getText().toString().equals("")) {
            username = edt_username.getText().toString();
            password = edt_password.getText().toString();
        } else {
            Toast.makeText(mApplication, "Please enter Username/Password",
                    Toast.LENGTH_LONG).show();
        }

        mBar.setVisibility(View.VISIBLE);
        mContainer.setVisibility(View.INVISIBLE);
        params.put("Username", username);
        params.put("Password", password);
        System.out.println(params.toString());
        aq = new AQuery(LoginActivity.this);
        gsonTransformer = new GsonTransformer();
        aq.transformer(gsonTransformer).ajax(LOGIN_URL, params,
                LoginTransform.class, new AjaxCallback<LoginTransform>() {
                    @Override
                    public void callback(String url, LoginTransform object,
                                         AjaxStatus status) {
                        super.callback(url, object, status);
                        isLoadComplete = true;
                        // For Checking Async Task
                        boolean isNotError = mApplication
                                .CheckCallBackStatus(status.getCode());

                        if (isNotError) {
                            // Correct Data to Database
                            // Here is User Information Data
//							userID = object.getUserData().get(0).getUserID();

                            if(object.getUserData().get(0).getUserID() == 0){
                                isLoadComplete = true;
                                mBar.setVisibility(View.INVISIBLE);
                                mContainer.setVisibility(View.VISIBLE);
                                Toast.makeText(LoginActivity.this, "รหัสผ่านผิด", Toast.LENGTH_LONG).show();
                            }else{
                                Name = object.getUserData().get(0).getName();
                                Map<String, String> s = mApplication.getSector();
                                Map<String, Integer> ps = mApplication.getPicture_Sector();
                                System.out.println(s.toString());
                                Sector = s.get(object.getUserData().get(0).getSector());
                                Pic_Sector = ps.get(object.getUserData().get(0).getSector());
                                SectorCode = object.getUserData().get(0).getSector();

                                // Here is CaneList Data
                                ContentResolver d = getContentResolver();
                                Uri uri = Uri.parse(getResources().getString(
                                        R.string.URI_CLONEINORDER));
                                if(object.getCaneList()!=null){
                                    for (int i = 0; i < object.getCaneList().size(); i++) {
                                        ContentValues values = new ContentValues();

                                        values.put("familyname", object.getCaneList().
                                                get(i).getBreededCode());
//
//									values.put("TrayNumber", object.getCaneList().get(i).getTrayNumber());

                                        if(object.getCaneList().get(i).getRecieveAmount() == 0){
                                            values.put("receivestatus", 0);
                                        }else{
                                            values.put("receivestatus", 1);
                                        }
                                        if(object.getCaneList().get(i).getRowNumber() == 0){
                                            values.put("plantstatus", 0);
                                        }else{
                                            values.put("plantstatus", 1);
                                        }
                                        values.put("amount_recieve", object.getCaneList().get(i).getRecieveAmount());
                                        values.put("amount_sent", object.getCaneList().get(i).getSendAmount());
                                        values.put("tag_amount", 3);
                                        values.put("syncstatus", 1);
                                        values.put("plantedamount",object.getCaneList().get(i).getPlantedAmount());
                                        values.put("rownumber",object.getCaneList().get(i).getRowNumber());

                                        d.insert(uri, values);
                                    }
                                }

                                SessionCheck(object.getUserData().get(0)
                                        .getUserID());
                            }


                        }

                        while (!isLoadComplete) {

                        }

                    }

                });
    }

    private String Name;
    private String Sector;
    private int Pic_Sector;
    private String SectorCode;
    private void SessionCheck(int userID) {

        // if server return 0 that mean username or password wrong
        if (userID != 0) {
            // when != 0 is logincomplete then create login session
            mLoginSession.CreatedSessionLogin(username, password,
                    userID,Name,Sector,Pic_Sector,SectorCode);

            Toast.makeText(this, "Login สำเร็จ", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(LoginActivity.this, Activity_News.class);
            startActivity(intent);
            // finish Load Data
            isLoadComplete = true;
            finish();

        } else {
            // Login Fail
            Toast.makeText(this, "Username ผิด Password กรุณาตรวจสอบ",
                    Toast.LENGTH_LONG).show();
            mBar.setVisibility(View.INVISIBLE);
            mContainer.setVisibility(View.VISIBLE);
            isLoadComplete = true;
        }
        isLoadComplete = true;

    }

    class LoginTransform {
        private ArrayList<UserData> UserData;
        private ArrayList<CaneList> CaneList;

        public ArrayList<UserData> getUserData() {
            return UserData;
        }

        public void setUserData(ArrayList<UserData> userData) {
            UserData = userData;
        }

        public ArrayList<CaneList> getCaneList() {
            return CaneList;
        }

        public void setCaneList(ArrayList<CaneList> caneList) {
            CaneList = caneList;
        }



    }
    class CaneList{

        private String BreededCode;
        private int SendAmount;
        private int RecieveAmount;
        private int PlantedAmount;
        private int RowNumber;
        private int TrayNumber;




        public int getSendAmount() {
            return SendAmount;
        }
        public void setSendAmount(int sendAmount) {
            SendAmount = sendAmount;
        }
        public int getRowNumber() {
            return RowNumber;
        }
        public void setRowNumber(int rowNumber) {
            RowNumber = rowNumber;
        }
        public int getRecieveAmount() {
            return RecieveAmount;
        }
        public void setRecieveAmount(int recieveAmount) {
            RecieveAmount = recieveAmount;
        }
        public int getPlantedAmount() {
            return PlantedAmount;
        }
        public void setPlantedAmount(int plantedAmount) {
            PlantedAmount = plantedAmount;
        }
        public String getBreededCode() {
            return BreededCode;
        }
        public void setBreededCode(String breededCode) {
            BreededCode = breededCode;
        }
        public int getTrayNumber() {
            return TrayNumber;
        }
        public void setTrayNumber(int trayNumber) {
            TrayNumber = trayNumber;
        }

    }
    class UserData {
        private int UserID;
        private String Name;
        private String Sector;

        public int getUserID() {
            return UserID;
        }

        public void setUserID(int userID) {
            UserID = userID;
        }

        public String getName() {
            return Name;
        }

        public void setName(String name) {
            Name = name;
        }

        public String getSector() {
            return Sector;
        }

        public void setSector(String sector) {
            Sector = sector;
        }

    }
}



