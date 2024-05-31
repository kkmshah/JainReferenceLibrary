package com.jainelibrary.CheckedList;/*
package com.dhanera.landmarklive.activity.CheckedList;


import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.dhanera.landmarklive.Adapter.AdapterSectionRecycler;
import com.dhanera.landmarklive.Constants;
import com.dhanera.landmarklive.R;
import com.dhanera.landmarklive.app.LandMarkApplication;
import com.dhanera.landmarklive.model.Category;
import com.dhanera.landmarklive.model.Child;
import com.dhanera.landmarklive.model.MemberModel;
import com.dhanera.landmarklive.model.SubCategory2;
import com.dhanera.landmarklive.model.SubCategory3;
import com.dhanera.landmarklive.model.SubCategory4;
import com.dhanera.landmarklive.model.SubCategory5;
import com.dhanera.landmarklive.model.SubCategorys;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.StringTokenizer;

public class MyActivity extends AppCompatActivity implements MultiCheckCityAdapter.onCheckBoxClick {
    public String TAG = "MyActivity";
    List<MultiCheckCity> multiCheckCities;
    private MultiCheckCityAdapter adapter;
    AdapterSectionRecycler sectionRecycler;
    List<MultiCheckCity> multiCheckCitiesTemp;
    private String pfc_No;
    LandMarkApplication application;
    ProgressDialog pDialog;
    String birthdate;
    ArrayList<Child> childListNew, childListTemp;
    Button btn_submit, btn_myrequest;
    private ArrayList<MemberModel> memberList;
    private ArrayList<Category> catagoryList;
    private ArrayList<SubCategorys> subcatagoryList;
    private ArrayList<SubCategory2> subCategory2ArrayList;
    private ArrayList<SubCategory3> subCategory3ArrayList;
    private ArrayList<SubCategory4> subCategory4ArrayList;
    private ArrayList<SubCategory5> subCategory5ArrayList;
    private List<String> catname;
    private ArrayList<String> membername;
    RecyclerView recyclerView;
    boolean isSelected = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sahyog);
        application = (LandMarkApplication) getApplicationContext();
        pfc_No = application.getSharedPreferences().getString(Constants.KEY_PFC_NO, "");
        Log.e(TAG, pfc_No);
        btn_submit = (Button) findViewById(R.id.btn_submit_sahyog);
        btn_myrequest = (Button) findViewById(R.id.btn_request);
        memberList = new ArrayList<>();
        membername = new ArrayList<>();
        multiCheckCities = new ArrayList<>();

        catagoryList = new ArrayList<>();
        subcatagoryList = new ArrayList<>();
        catname = new ArrayList<>();
        subCategory2ArrayList = new ArrayList<>();
        subCategory3ArrayList = new ArrayList<>();
        subCategory4ArrayList = new ArrayList<>();
        subCategory5ArrayList = new ArrayList<>();
        multiCheckCities = new ArrayList<>();
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);
        pDialog.setMessage("Please, wait...");
        pDialog.show();
        getMemberName();
        getCategoryList();
        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i = 0; i < memberList.size(); i++) {
                    if (multiCheckCities.get(i).getGeoTag()) {
                        isSelected = true;
                    }
                }
                if (isSelected) {
                    //   UploadData();
                } else {
                    Toast.makeText(getApplicationContext(), "Select Any Member Or Categoty", Toast.LENGTH_LONG).show();
                }
            }
        });
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
    }

    private void getRecyclerView() {

        //  Log.e(TAG, "catlist" + String.valueOf(catagoryList.size()));
        //  Log.e(TAG, "memberlist" + String.valueOf(memberList.size()));
        membername = new ArrayList<>();
        if (catagoryList.size() != 0) {
            if (memberList.size() != 0) {
                for (int j = 0; j < memberList.size(); j++) {
                    childListNew = new ArrayList<>();
                    for (int i = 0; i < catagoryList.size(); i++) {

                        String date = memberList.get(j).getBirthdate();
                        StringTokenizer st = new StringTokenizer(date, "/");
                        int day = Integer.parseInt(st.nextToken());
                        int month = Integer.parseInt(st.nextToken());
                        int year = Integer.parseInt(st.nextToken());
                        Log.e(TAG, day + " day " + month + " month " + year + " year ");
                        int age = getAge(year, month, day);
                        Log.e(TAG, age + "");
                        Log.e(TAG, "memberList" + memberList.get(j).getName());
                        if ((age > 60) && (i == 0)) {
                            Child child = new Child(catagoryList.get(i).getName(),catagoryList.get(i).getId(), false, j);
                            childListNew.add(child);
                        } else if ((age > 3) && (i == 1)) {
                            Child child = new Child(catagoryList.get(i).getName(), catagoryList.get(i).getId(),false, j);
                            childListNew.add(child);
                        } else if (i == 2) {
                            Child child = new Child(catagoryList.get(i).getName(),catagoryList.get(i).getId(), false, j);
                            childListNew.add(child);
                        } else if (i > 2) {
                            Child child = new Child(catagoryList.get(i).getName(),catagoryList.get(i).getId(), false, j);
                            childListNew.add(child);
                        }
                    }
                    multiCheckCities.add(new MultiCheckCity(memberList.get(j).getName(), childListNew, false));
                }
            }
        }
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        adapter = new MultiCheckCityAdapter(this, multiCheckCities);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);


    }

    @Override
    public void onItemClick(int type, int position, int childPosition, boolean state) {
        if (type == 0) {
            multiCheckCitiesTemp = new ArrayList<>();
            //    sectionHeaderListTemp = sectionHeaderList;
            if (catagoryList.size() != 0) {
                if (memberList.size() != 0) {
                    //     sectionHeaderList.clear();
                    for (int j = 0; j < memberList.size(); j++) {
                        childListTemp = new ArrayList<>();

                        List<Child> childList = multiCheckCities.get(j).getItems();
                        Log.e(TAG, "childList +0" + multiCheckCities.get(j));
                        //   Log.e(TAG, "section size" + sectionHeader.getChildItems().size() + "");
                        for (int i = 0; i < childList.size(); i++) {

                            //    Child child = new Child(catagoryList.get(i).getName(), state, position);
                            if (j == position) {
                                Child child = new Child(childList.get(i).getName(),childList.get(i).getId(), state, position);
                                childListTemp.add(child);
                                Log.e(TAG, state + "");
                            } else {
                                //    Child child = new Child(catagoryList.get(i).getName(), true, j);
                                childListTemp.add(childList.get(i));
                                //      sectionHeaderList.add(new SectionHeader(childListTemp, memberList.get(j).getName(),true));

                            }
                        }
                        if (j == position) {
                            multiCheckCitiesTemp.add(new MultiCheckCity(memberList.get(j).getName(), childListTemp, state));
                        } else {
                            multiCheckCitiesTemp.add(new MultiCheckCity(memberList.get(j).getName(), childListTemp, multiCheckCities.get(j).getGeoTag()));
                        }
                    }
                }
            }
            multiCheckCities = multiCheckCitiesTemp;
            recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
            adapter = new MultiCheckCityAdapter(this, multiCheckCitiesTemp);
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            recyclerView.setAdapter(adapter);
            adapter.notifyDataSetChanged();

        } else if (type == 1) {

            boolean isChecked = false;
            multiCheckCitiesTemp = new ArrayList<>();
            //    sectionHeaderListTemp = sectionHeaderList;
            if (catagoryList.size() != 0) {
                if (memberList.size() != 0) {
                    //     sectionHeaderList.clear();
                    for (int j = 0; j < memberList.size(); j++) {
                        childListTemp = new ArrayList<>();
                        //  List<Child> childList = sectionHeader.getChildItems();
                        List<Child> childList = multiCheckCities.get(j).getItems();
                        Log.e(TAG, "childList+ 1" + childList.size());
                        for (int i = 0; i < childList.size(); i++) {
                            if (j == position) {
                                //    Child child = new Child(catagoryList.get(i).getName(), state, position);
                                if (i == childPosition) {
                                    Child child = new Child(childList.get(i).getName(),childList.get(i).getId(), state, position);
                                    childListTemp.add(child);
                                } else {
                                    boolean b = childList.get(i).isState();
                                    if (b) {
                                        isChecked = true;
                                    }
                                    childListTemp.add(childList.get(i));
                                }
                            } else {
                                //    Child child = new Child(catagoryList.get(i).getName(), true, j);
                                // Log.e(TAG,"multiCheckCities"+multiCheckCities.get(j).getChildItems().get(i));
                                childListTemp.add(childList.get(i));
                                //      sectionHeaderList.add(new SectionHeader(childListTemp, memberList.get(j).getName(),true));
                            }
                        }
                        if (j == position) {
                            if (state || isChecked) {
                                multiCheckCitiesTemp.add(new MultiCheckCity(memberList.get(j).getName(), childListTemp, true));
                            } else {
                                multiCheckCitiesTemp.add(new MultiCheckCity(memberList.get(j).getName(), childListTemp, false));
                            }
                        } else {
                            multiCheckCitiesTemp.add(new MultiCheckCity(memberList.get(j).getName(), childListTemp, multiCheckCities.get(j).getGeoTag()));

                        }
                    }
                }
            }
            multiCheckCities = multiCheckCitiesTemp;
            adapter = new MultiCheckCityAdapter(this, multiCheckCitiesTemp);
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            recyclerView.setAdapter(adapter);

        }
    }


*/
/*    private void UploadData() {

        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);
        pDialog.setMessage("Please, wait...");
        pDialog.show();
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("pfc", pfc_No);
            JSONArray memberArray = new JSONArray();
            for (int i = 0; i < memberList.size(); i++) {
                SectionHeader sectionHeader = sectionHeaderList.get(i);
                if (sectionHeaderList.get(i).isSelected()) {
                    JSONObject jobjsection = new JSONObject();
                    jobjsection.put("member", sectionHeaderList.get(i).getSectionText());
                    List<Child> childList = sectionHeader.getChildItems();
                    JSONArray categoryArray = new JSONArray();
                    for (int j = 0; j < childList.size(); j++) {
                        if (sectionHeader.getChildItems().get(j).isState()) {
                            JSONObject jobjchild = new JSONObject();
                            jobjchild.put("category", sectionHeader.getChildItems().get(j).getName());
                            categoryArray.put(jobjchild);
                        }
                    }
                    memberArray.put(jobjsection);
                    jobjsection.put("category_list", categoryArray);

                }

            }
            jsonObject.put("member_list", memberArray);
            Log.e(TAG, jsonObject.toString());

            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, Constants.URL_SAHYOG_SUBMIT_REQUEST, jsonObject,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject jsonObject) {
                            try {
                                if (pDialog.isShowing())
                                    pDialog.dismiss();
                                Log.e(TAG, "Responce " + jsonObject.toString());
                                VolleyLog.v("Response:%n %s", jsonObject.toString(500));
                                final Dialog dialog = new Dialog(MyActivity.this);
                                //dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                                dialog.setContentView(R.layout.dialog_register);
                                Button dialogButton = (Button) dialog.findViewById(R.id.btnOK);
                                TextView textView = (TextView) dialog.findViewById(R.id.tvDescription); // if button is clicked, close the custom dialog
                                dialog.setCancelable(false);
                                textView.setText(getString(R.string.submitsuccess));
                                dialogButton.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        dialog.dismiss();
                                        finish();
                                    }
                                });
                                dialog.show();
                            } catch (JSONException e) {
                                Log.e(TAG, "JSONException" + e.getMessage());
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    Log.e("Message from server", volleyError.toString());
                }
            }) {
                @Override
                protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
                    try {
                        String jsonString = new String(response.data,
                                HttpHeaderParser.parseCharset(response.headers, PROTOCOL_CHARSET));

                        JSONObject result = null;

                        if (jsonString != null && jsonString.length() > 0)
                            result = new JSONObject(jsonString);

                        return Response.success(result,
                                HttpHeaderParser.parseCacheHeaders(response));
                    } catch (UnsupportedEncodingException e) {
                        return Response.error(new ParseError(e));
                    } catch (JSONException je) {
                        return Response.error(new ParseError(je));
                    }
                }
            };
            jsonObjectRequest.setRetryPolicy(new RetryPolicy() {
                @Override
                public int getCurrentTimeout() {
                    return 5000;
                }

                @Override
                public int getCurrentRetryCount() {
                    return 5000;
                }

                @Override
                public void retry(VolleyError error) throws VolleyError {

                }
            });
            RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
            queue.getCache().clear();
            queue.add(jsonObjectRequest);
        } catch (JSONException e) {
            Log.e(TAG, e.getMessage());
        }
    }*//*


    */
/*private List<MultiCheckCity> createAndReturnRandomList() {
        // creating List of Münster Mensen
        Mensa mensa1 = new Mensa("Mensa am Ring", "65484");
        Mensa mensa2 = new Mensa("Bispinghof", "68435");
        Mensa mensa3 = new Mensa("Mensa am Aasee", "84542");

        MultiCheckCity muenster = new MultiCheckCity("Münster", Arrays.asList(mensa1, mensa2, mensa3), "68351");

        //creating List of Frankfurt Mensen
        Mensa mensa4 = new Mensa("Mensa an der Bank", "84516");
        Mensa mensa5 = new Mensa("Mensa am See", "87546");

        MultiCheckCity frankfurt = new MultiCheckCity("Frankfurt", Arrays.asList(mensa4, mensa5), "94175");

        return Arrays.asList(muenster, frankfurt);
    }*//*


    public int getAge(int year, int month, int day) {

        GregorianCalendar cal = new GregorianCalendar();
        int y, m, d, noofyears;
        int ageCount = 0;
        y = cal.get(Calendar.YEAR);// current year ,
        m = cal.get(Calendar.MONTH);// current month
        d = cal.get(Calendar.DAY_OF_MONTH);// current day
        cal.set(year, month, day);// here ur date
        noofyears = (int) (y - cal.get(Calendar.YEAR));

        if ((m < cal.get(Calendar.MONTH)) || ((m == cal.get(Calendar.MONTH)) && (d < cal.get(Calendar.DAY_OF_MONTH)))) {
            --noofyears;
        }

        if (noofyears != 0) {
            ageCount = noofyears;
        } else {
            ageCount = 0;
        }
        if (noofyears < 0)
            throw new IllegalArgumentException("age < 0");
        return noofyears;
    }

    private void getMemberName() {

        */
/*final String pfcNumber = pfc_No.getText().toString();*//*


        String URL = Constants.URL_HEAD_DETAILS + pfc_No;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response == null) {
                            return;
                        }
                        String str = "<string xmlns=\"http://www.landmarkunity.com/\">";
                        response = response.replace(str, "");
                        response = response.replace("</string>", "");
                        response = response.replace("<?xml version=\"1.0\" encoding=\"utf-8\"?>", "");
                        Log.e("Response", "" + response);
                        if (pDialog.isShowing())
                            pDialog.dismiss();
                        try {
                            JSONObject jObj = new JSONObject(response);
                            Log.e("pfc", "" + jObj.getString("PFC"));
                            pfc_No = (jObj.has("PFC") ? jObj.getString("PFC") : "-1");

                            SharedPreferences.Editor editor = application.getSharedPreferences().edit();
                            editor.putString(Constants.KEY_PFC_NO, pfc_No);
                            Log.e(TAG, pfc_No);
                            editor.apply();

                            JSONArray memberArray = jObj.getJSONArray("FamilyMembers");
                            //now looping through all the elements of the json array
                            for (int i = 0; i < memberArray.length(); i++) {
                                //getting the json object of the particular index inside the array
                                JSONObject memObject = memberArray.getJSONObject(i);
                                //creating a hero object and giving them the values from json object
                                String fName = memObject.getString("FirstName");
                                String lName = memObject.getString("MiddleName");
                                String sname = memObject.getString("FamilySurname"); String fullName = fName + " " + lName+" "+sname;
                                MemberModel memberModel = new MemberModel();
                                memberModel.setName(fullName);
                                birthdate = memObject.getString("DateofBirth");
                                memberModel.setBirthdate(birthdate);
                                memberList.add(memberModel);
                                Log.e(TAG, birthdate);
                                Log.e("TAG", fullName);
                            }
                            if (catagoryList.size() != 0) {
                                getRecyclerView();
                            }
                        } catch (JSONException e) {
                            Log.e(TAG, "json error" + e.getMessage());
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //displaying the error in toast if occurrs
                        Log.e(TAG, "voll " + error.getMessage());
                        Toast.makeText(getApplicationContext().getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

        //creating a request queue
        RequestQueue requestQueue = (RequestQueue) Volley.newRequestQueue(getApplicationContext().getApplicationContext());
        requestQueue.getCache().clear();
        //adding the string request to request queue
        requestQueue.add(stringRequest);

    }

    private void getCategoryList() {

        final String URL = Constants.URL_CATAGORY_DETAILS;
        JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.GET, URL, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        if (response == null) {
                            if (pDialog.isShowing())
                                pDialog.dismiss();
                            Log.e(TAG, "responce null");
                            return;
                        }
                        Log.e("response", "" + response);
                        try {
                            JSONArray memberArray = response.getJSONArray("categories");

                            for (int i = 0; i < memberArray.length(); i++) {

                                JSONObject memObject = memberArray.getJSONObject(i);
                                Category categoryS = new Category();
                                categoryS.setId(memObject.getString("id"));
                                categoryS.setName(memObject.getString("name"));

                                if (memObject.has("sub") && (memObject.getJSONArray("sub") != null)) {

                                    JSONArray subarray = memObject.getJSONArray("sub");
                                    for (int j = 0; j < subarray.length(); j++) {
                                        //getting the json object of the particular index inside the array
                                        JSONObject subobject = subarray.getJSONObject(j);
                                        SubCategorys subCategoryS = new SubCategorys();
                                        subCategoryS.setId(subobject.getString("id"));
                                        subCategoryS.setSubname(subobject.getString("name"));
                                        if (subobject.has("sub") && (subobject.getJSONArray("sub") != null)) {
                                            Log.e(TAG + "sub", "sub not null");
                                            JSONArray sub2array = subobject.getJSONArray("sub");
                                            for (int k = 0; k < sub2array.length(); k++) {
                                                //getting the json object of the particular index inside the array
                                                JSONObject sub2object = sub2array.getJSONObject(k);
                                                //    Log.e(TAG + "subcategory", sub2array.toString());
                                                SubCategory2 subCategory2 = new SubCategory2();
                                                subCategory2.setId(sub2object.getString("id"));
                                                subCategory2.setSub_subCategory(sub2object.getString("name"));
                                                Log.e(TAG + "swsub2 ", (String) sub2object.get("id") + sub2object.getString("name"));
                                                if (sub2object.has("sub") && (sub2object.getJSONArray("sub") != null)) {

                                                    JSONArray sub3array = sub2object.getJSONArray("sub");
                                                    for (int l = 0; l < sub3array.length(); l++) {
                                                        //getting the json object of the particular index inside the array
                                                        JSONObject sub3object = sub3array.getJSONObject(l);

                                                        SubCategory3 subCategory3 = new SubCategory3();
                                                        subCategory3.setId(sub3object.getString("id"));
                                                        subCategory3.setSubcategory3(sub3object.getString("name"));

                                                        //    Log.e(TAG + "subcategory3", subarray.toString());
                                                        if (sub3object.has("sub") && (sub3object.getJSONArray("sub") != null)) {

                                                            JSONArray sub4array = sub3object.getJSONArray("sub");
                                                            for (int m = 0; m < sub4array.length(); m++) {
                                                                //getting the json object of the particular index inside the array
                                                                JSONObject sub4object = sub4array.getJSONObject(m);
                                                                SubCategory4 subCategory4 = new SubCategory4();
                                                                subCategory4.setId(sub4object.getString("id"));
                                                                subCategory4.setSubcategory4(sub4object.getString("name"));
                                                                //    Log.e(TAG + "subcategory4", subarray.toString());
                                                                if (sub4object.has("sub") && (sub4object.getJSONArray("sub") != null)) {

                                                                    JSONArray sub5array = sub4object.getJSONArray("sub");
                                                                    for (int n = 0; n < sub5array.length(); n++) {
                                                                        //getting the json object of the particular index inside the array
                                                                        JSONObject sub5object = sub5array.getJSONObject(n);
                                                                        SubCategory5 subCategory5 = new SubCategory5();
                                                                        subCategory5.setId(sub5object.getString("id"));
                                                                        subCategory5.setSubcategory5(sub5object.getString("name"));
                                                                        //    Log.e(TAG + "subcategory4", subarray.toString());
                                                                        subCategory5ArrayList.add(subCategory5);
                                                                        subCategory4.setSubCategory5ArrayList(subCategory5ArrayList);
                                                                    }

                                                                }

                                                                subCategory4ArrayList.add(subCategory4);
                                                                subCategory3.setSubCategory4ArrayList(subCategory4ArrayList);

                                                            }

                                                        }
                                                        subCategory3ArrayList.add(subCategory3);
                                                        subCategory2.setSubCategory3ArrayList(subCategory3ArrayList);

                                                    }

                                                }
                                                Log.e(TAG + "subcategory2", subarray.toString());


                                                subCategory2ArrayList.add(subCategory2);
                                                subCategoryS.setSub_subCategoryArrayList(subCategory2ArrayList);
                                            }
                                        }
                                        Log.e(TAG + "subcat2 sizea", String.valueOf(subCategory2ArrayList.size()));
                                        subcatagoryList.add(subCategoryS);
                                        categoryS.setSubCategorySArrayList(subcatagoryList);

                                    }

                                }
                                Log.e(TAG + "  subcat size", String.valueOf(subcatagoryList.size()));
                                catagoryList.add(categoryS);
                                Log.e(TAG + "category size", String.valueOf(catagoryList.size()));
                                //getSpinnerdata();

                            }
                            //  catname.add("Select Category");
                            for (int j = 0; j < catagoryList.size(); j++) {

                                Log.e("Secrete Name", " : " + catagoryList.get(j).getName());
                                catname.add(catagoryList.get(j).getName());
                            }

                            Log.e("catname---", "" + catname);

                            if (memberList.size() != 0) {
                                getRecyclerView();
                            }

                        } catch (JSONException e) {
                            Log.e("JSON error", e.getMessage());
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(TAG, "responce error " + error.toString());
                        Toast.makeText(getApplicationContext().getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

        stringRequest.setRetryPolicy(new RetryPolicy() {
            @Override
            public int getCurrentTimeout() {
                return 50000;
            }

            @Override
            public int getCurrentRetryCount() {
                return 50000;
            }

            @Override
            public void retry(VolleyError error) throws VolleyError {

            }
        });
        RequestQueue requestQueue = (RequestQueue) Volley.newRequestQueue(getApplicationContext().getApplicationContext());
        requestQueue.getCache().clear();
        requestQueue.add(stringRequest);

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        adapter.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        adapter.onRestoreInstanceState(savedInstanceState);
    }


}*/
