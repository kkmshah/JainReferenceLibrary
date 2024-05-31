package com.jainelibrary.fragment;

import static android.app.Activity.RESULT_OK;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.appbar.AppBarLayout;
import com.google.gson.Gson;
import com.jainelibrary.IOnBackPressed;
import com.jainelibrary.JRL;
import com.jainelibrary.R;
import com.jainelibrary.activity.FilterMenuActivity;
import com.jainelibrary.activity.HidingScrollListener;
import com.jainelibrary.activity.RelationDetailsActivity;
import com.jainelibrary.activity.SearchReferenceActivity;
import com.jainelibrary.activity.UnitDetailsActivity;
import com.jainelibrary.adapter.SearchHistoryAdapter;
import com.jainelibrary.adapter.UnitWisePramparaSearchListAdapter;
import com.jainelibrary.extraModel.SearchHistoryModel;
import com.jainelibrary.keyboard.CustomKeyboardView;
import com.jainelibrary.keyboard.Util;
import com.jainelibrary.manager.ConnectionManager;
import com.jainelibrary.model.ParamparaFilterDataResModel;
import com.jainelibrary.model.SearchUnitListResModel;
import com.jainelibrary.model.UserDetailsResModel;
import com.jainelibrary.retrofit.ApiClient;
import com.jainelibrary.retrofitResModel.SamvatsBaseOnSamvatTypeDataResModel;
import com.jainelibrary.utils.SharedPrefManager;
import com.jainelibrary.utils.Utils;
import com.paginate.Paginate;
import com.paginate.recycler.LoadingListItemSpanLookup;

import java.util.ArrayList;
import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PramparaFragment extends Fragment implements IOnBackPressed, SearchHistoryAdapter.SearchHistoryInterfaceListener, UnitWisePramparaSearchListAdapter.UnitClickListener, Paginate.Callbacks {
    private static final int KEYWORD_SEARCH = 1;
    private final String TAG = PramparaFragment.this.getClass().getSimpleName();
    private final ArrayList<String> unitTypeList = new ArrayList<>();
    private final ArrayList<String> unitStatusTypeList = new ArrayList<>();
    private final ArrayList<String> unitSectCommTypeList = new ArrayList<>();

    private final ArrayList<String> bioSamvatTypeList = new ArrayList<>();
    private final ArrayList<String> bioSamvatYearList = new ArrayList<>();
    private final ArrayList<String> bioMonthList = new ArrayList<>();
    private final ArrayList<String> bioTithiList = new ArrayList<>();
    private final ArrayList<String> bioDateList = new ArrayList<>();
    private final ArrayList<String> strRelationTypeList = new ArrayList<>();

    CustomKeyboardView mKeyboardView;
    Keyboard mKeyboard;
    ImageView ivClose, ivKeyboard, ivSend;
    RecyclerView rvList;
    LinearLayout llFilter, llClose, llResultFound;
    LinearLayout searchButton;
    String strBookIds = "";
    ArrayList<SearchHistoryModel> mSearchKeyword;
    Paginate paginate;
    View headerView, header2;
    AppBarLayout appBarLayout;
    private UnitWisePramparaSearchListAdapter mSearchListAdapter;
    private EditText etSearchView;

    private TextView tvDatePicker;
    private String strLanguage;
    private String strSearchtext;
    private String PackageName;
    private TextView tvResultFound, tvLensCounts;
    private Spinner spnUnitType;
    private Spinner spnUnitStatusType;
    private Spinner spnUnitSectCommType;


    private ImageView ivDatePickerClear;
    private Spinner spnBioSamvatType;
    private AutoCompleteTextView spnBioSamvatYear;
    private Spinner spnBioMonth;
    private Spinner spnBioTithi;
    private Spinner spnRelationType;
    private int totalCount = 0;
    private String strUsername, strLastSpiItem;
    private String strUId, strUnitId, strUnitTypeName;
    private int spnUnitTypeId = 0;

    private LinearLayout llBtnLens;
    private int spnUnitStatusTypeId = 0;
    private int spnUnitSectCommTypeId = 0;

    private int spnBioSamvatTypeId = 0;
    private int spnBioSamvatYearId = 0;

    private ParamparaFilterDataResModel.Samvat searchBioSamvatYear = null;

    private ParamparaFilterDataResModel.Samvat selectedBioSamvatYear = null;
    private int spnBioMonthId = 0;
    private int spnBioTithiId = 0;
    private  String strBiodate = "";
    private int spnRelationTypeId = 0;


    private ParamparaFilterDataResModel.SamvatType  samvatType;
    ArrayList<ParamparaFilterDataResModel.RelationType> relationTypeList = new ArrayList<ParamparaFilterDataResModel.RelationType>();

    public PramparaFragment() {

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        getFocusLensDialogData();
        //rvList.setVisibility(View.GONE);
        //getSearchUnits();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_prampara, container, false);
        PackageName = getActivity().getPackageName();
        loadUiElements(view);
        setHeader();
        declaration();
        Intent intent = new Intent();
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            totalCount = bundle.getInt("totalCount", 0);
            strSearchtext = bundle.getString("strSearchKeyword");
            if (strSearchtext != null && strSearchtext.length() > 0) {
                etSearchView.setText(strSearchtext);
            }
        }
        rvList.setVisibility(View.GONE);
        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.e("oar", "" + requestCode + " res " + resultCode);
        if (requestCode == KEYWORD_FILTER) {
            if (resultCode == RESULT_OK) {

                getFocusLensDialogData();
                getSearchUnits();
            }
        }

    }

    @Override
    public boolean onBackPressed() {

        if (mKeyboardView.getVisibility() == View.VISIBLE) {
            mKeyboardView.setVisibility(View.GONE);
        } else {
            Intent i = new Intent(getActivity(), SearchReferenceActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(i);
            getActivity().finish();
        }
        return false;
    }

    public void showDatePicker(View view) {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        if(strBiodate !=null && !strBiodate.isEmpty()) {
            String[] parts = strBiodate.split("-");
            year = Integer.parseInt(parts[0]);
            month = Integer.parseInt(parts[1]);
            day = Integer.parseInt(parts[2]);
        }

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                getActivity(),
                R.style.CustomDatePickerDialogTheme,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        String strMonth = String.valueOf(month + 1);
                        if((month + 1) <10) {
                            strMonth = "0" + strMonth;
                        }
                        String strDay = String.valueOf(day);
                        if(day <10) {
                            strDay = "0" + strDay;
                        }
                        // Do something with the selected date
                        String selectedDate = year + "-" + strMonth + "-" + strDay;
                        tvDatePicker.setText(selectedDate);
                        strBiodate = selectedDate;
                        ivDatePickerClear.setVisibility(View.VISIBLE);
                        Log.e(TAG, "Selected date" + selectedDate);
                        // You can use the selectedDate as needed
                        // For example, display it in a TextView
                        // textView.setText(selectedDate);
                    }
                },
                year, month, day);

        datePickerDialog.show();
    }


    private void declaration() {

        mSearchKeyword = SharedPrefManager.getInstance(getActivity()).getSearchkeywordHistory(SharedPrefManager.KEY_KEYWORD_HISTORY);
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        builder.detectFileUriExposure();
        //callKeywordDataApi("", "");
        //  etSearchView.requestFocus();
        strLanguage = SharedPrefManager.getInstance(getActivity()).getStringKeyword(SharedPrefManager.KEY_SELECT_LANGUAGE);
        Log.e(TAG, "strLanguage--" + strLanguage);
        // Util.selectKeyboard(mKeyboardView, mKeyboard, etSearchView, getActivity(), strLanguage, null);
        Gson gson = new Gson();
        String strUserDetails = SharedPrefManager.getInstance(getActivity()).getStringPref(SharedPrefManager.KEY_USER_DETAILS);
        UserDetailsResModel.UserDetailsModel userDetailsModel = new UserDetailsResModel.UserDetailsModel();
        userDetailsModel = gson.fromJson(strUserDetails, UserDetailsResModel.UserDetailsModel.class);


        if (userDetailsModel != null) {
            strUsername = userDetailsModel.getName();
        }
        ivKeyboard.setVisibility(View.VISIBLE);
        ivKeyboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Util.selectKeyboard(mKeyboardView, mKeyboard, etSearchView, getActivity(), strLanguage, null, ivSend);
                Utils.showDefaultKeyboardDialog(getActivity());
            }
        });
        //callKeywordDataApi("");

        etSearchView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Util.hideKeyBoard(getActivity(), etSearchView);
                strLanguage = SharedPrefManager.getInstance(getActivity()).getStringKeyword(SharedPrefManager.KEY_SELECT_LANGUAGE);
                Util.hideKeyBoard(getActivity(), etSearchView);
                Util.selectKeyboard(mKeyboardView, mKeyboard, etSearchView, getActivity(), strLanguage, null, ivSend);//   setSearchHistoryKeywordData();
//d                return false;
            }
        });
        etSearchView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

                strSearchtext = editable.toString();
                if (strSearchtext == null || strSearchtext.length() == 0) {
                } else {
                    ivSend.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.dark_voilet)));
                    llFilter.setVisibility(View.GONE);
                    llClose.setVisibility(View.VISIBLE);
                    if (strSearchtext != null && strSearchtext.length() > 0) {
                        llClose.setVisibility(View.VISIBLE);
                        llFilter.setVisibility(View.GONE);
                    } else {
                        llClose.setVisibility(View.GONE);
                        llFilter.setVisibility(View.VISIBLE);
                    }
                }
            }
        });

        etSearchView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_SEARCH) {
                    getSearchByText();
                    return true;
                }
                return false;
            }
        });

        ivDatePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePicker(view);
            }
        });

        ivDatePickerClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                strBiodate = "";
                tvDatePicker.setText("Select Date");
                ivDatePickerClear.setVisibility(View.GONE);
            }
        });

        ivSend.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                Log.e("search", "search");
                //Util.selectKeyboard(mKeyboardView, mKeyboard, etSearchView, getActivity(), strLanguage, null, ivSend);
                getSearchByText();
                return false;
            }
        });
        etSearchView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                strSearchtext = editable.toString();
                if (strSearchtext == null || strSearchtext.length() == 0) {
                    llFilter.setVisibility(View.VISIBLE);
                    llClose.setVisibility(View.GONE);
                } else {
                    ivSend.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.dark_voilet)));
                    llFilter.setVisibility(View.GONE);
                    llClose.setVisibility(View.VISIBLE);
                    ivClose.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            strSearchtext = null;
                            etSearchView.getText().clear();
                            llResultFound.setVisibility(View.GONE);
                            tvResultFound.setVisibility(View.GONE);
                            llFilter.setVisibility(View.VISIBLE);
                            llClose.setVisibility(View.GONE);

                            if (mKeyboardView.getVisibility() == View.VISIBLE) {
                                mKeyboardView.setVisibility(View.GONE);
                            }
                           /* ivHeaderIcon.setVisibility(View.INVISIBLE);
                            tvHeaderCount.setVisibility(View.INVISIBLE);*/
                        }
                    });
                    if (strSearchtext != null && strSearchtext.length() > 0) {
                        llClose.setVisibility(View.VISIBLE);
                        llFilter.setVisibility(View.GONE);
                        /* filter(strSearchtext);*/
                    } else {
                        llClose.setVisibility(View.GONE);
                        llFilter.setVisibility(View.VISIBLE);
                    }
                }
            }
        });

        ivSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Util.hideKeyBoard(getActivity(), etSearchView);
                Log.e("search", "search");
                //Util.selectKeyboard(mKeyboardView, mKeyboard, etSearchView, getActivity(), strLanguage, null, ivSend);
                getSearchByText();

            }
        });


        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (mKeyboardView.getVisibility() == View.VISIBLE) {
                    mKeyboardView.setVisibility(View.GONE);
                }

                Util.commonKeyboardHide(getActivity());
                Log.e("search", "search");
                if(!canSearch()) {
                    Utils.showInfoDialog(getActivity(), "Please add any one filter.");
                    return;
                }
                //Util.selectKeyboard(mKeyboardView, mKeyboard, etSearchView, getActivity(), strLanguage, null, ivSend);
                getSearchUnits();

            }
        });
        mKeyboardView.setOnKeyboardActionListener(new KeyboardView.OnKeyboardActionListener() {
            @Override
            public void onPress(int i) {
                Log.e("Tag i", i + "");
                if (mKeyboardView.getVisibility() == View.VISIBLE)
                    if (i == 151) {
                        Log.e("Search", "search key Pressed");
                    }
                Log.e("Tag i", i + "");
            }

            @Override
            public void onRelease(int i) {
            }

            @Override
            public void onKey(int i, int[] ints) {
                Log.e("Tag i", i + "");
                if (mKeyboardView.getVisibility() == View.VISIBLE)
                    if (i == 151) {
                        Log.e("Search", "search key Pressed");
                    }
                Log.e("Tag i", i + "");
            }

            @Override
            public void onText(CharSequence charSequence) {
            }

            @Override
            public void swipeLeft() {
            }

            @Override
            public void swipeRight() {
            }

            @Override
            public void swipeDown() {
            }

            @Override
            public void swipeUp() {
            }
        });
        getParamparaFilterData();
        //BattleRun.dismiss();
        Log.d("Item", "Clicked");


    }

    private boolean canSearch() {
        String str = spnBioSamvatYear.getText().toString();
        if (spnBioSamvatYearId == 0 && str != null) {
            spnBioSamvatYear.setText("");
        }
        return spnUnitTypeId > 0 || spnUnitStatusTypeId > 0 || spnUnitSectCommTypeId > 0 || spnBioSamvatYearId > 0 || spnBioMonthId > 0 || spnBioTithiId > 0 || spnRelationTypeId > 0 || !strBiodate.isEmpty() || !etSearchView.getText().toString().isEmpty();
    }

    private void setEnabledEtSearchView() {

        etSearchView.setEnabled(true);
    }

    private void setUnitTypeSpinnerData(ArrayList<ParamparaFilterDataResModel.UnitType> typeList) {
        ArrayAdapter<String> adp = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, unitTypeList);
        spnUnitType.setAdapter(adp);

        spnUnitType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long arg3) {

                spnUnitTypeId = typeList.get(position).getId();
                setEnabledEtSearchView();
            }

            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub
            }
        });
    }


    private void setUnitStatusTypeSpinnerData(ArrayList<ParamparaFilterDataResModel.UnitStatusType> typeList) {
        ArrayAdapter<String> adp = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, unitStatusTypeList);
        spnUnitStatusType.setAdapter(adp);

        spnUnitStatusType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long arg3) {

                spnUnitStatusTypeId = typeList.get(position).getId();
                setEnabledEtSearchView();

            }

            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub
            }
        });
    }


    private void setUnitSectCommTypeSpinnerData(ArrayList<ParamparaFilterDataResModel.UnitSectCommunityType> typeList) {
        ArrayAdapter<String> adp = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, unitSectCommTypeList);
        spnUnitSectCommType.setAdapter(adp);

        spnUnitSectCommType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long arg3) {

                spnUnitSectCommTypeId = typeList.get(position).getId();
                setEnabledEtSearchView();
            }

            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub
            }
        });
    }

    private void setBioSamvatTypeSpinnerData(ArrayList<ParamparaFilterDataResModel.SamvatType> typeList) {
        ArrayAdapter<String> adp = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, bioSamvatTypeList);
        spnBioSamvatType.setAdapter(adp);

        spnBioSamvatType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long arg3) {

                spnBioSamvatYearId = 0;
                spnBioSamvatYear.setText("");

                int prevSpanSamvatTypeId = spnBioSamvatTypeId;
                spnBioSamvatTypeId = typeList.get(position).getId();
                samvatType = typeList.get(position);
                if (samvatType.isOnly_year()) {
                    spnBioMonthId = 0;
                    spnBioTithiId = 0;
                    spnBioMonth.setSelection(0);
                    spnBioTithi.setSelection(0);
                    llBioMonth.setVisibility(View.GONE);
                    llBioTithi.setVisibility(View.GONE);
                } else {
                    llBioMonth.setVisibility(View.VISIBLE);
                    llBioTithi.setVisibility(View.VISIBLE);
                }
                if (prevSpanSamvatTypeId != spnBioSamvatTypeId) {
                    getSamvatsBaseOnSamvatTypeData();
                }
                setEnabledEtSearchView();

            }

            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub
            }
        });
    }

    private void setBioSamvatYearSpinnerData(ArrayList<ParamparaFilterDataResModel.Samvat> typeList) {
        ArrayAdapter<String> adp = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, bioSamvatYearList);
        spnBioSamvatYear.setAdapter(adp);
        spnBioSamvatYear.setThreshold(1);
        adp.notifyDataSetChanged();

//        spnBioSamvatYear.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            public void onItemSelected(AdapterView<?> parent, View view,
//                                       int position, long arg3) {
//
//                spnBioSamvatYearId = typeList.get(position).getId();
//                setEnabledEtSearchView();
//                Log.e("spnBioSamvatYear", "spnBioSamvatYear onItemSelected" + spnBioSamvatYearId);
//            }
//
//            public void onNothingSelected(AdapterView<?> arg0) {
//                // TODO Auto-generated method stub
//            }
//        });

        selectedBioSamvatYear = null;
        if(samvatType != null && samvatType.isOnly_year()) {
            spnBioMonthId = 0;
            spnBioTithiId = 0;
            spnBioMonth.setSelection(0);
            spnBioTithi.setSelection(0);
            llBioMonth.setVisibility(View.GONE);
            llBioTithi.setVisibility(View.GONE);
        }else if(samvatType !=null) {
            llBioMonth.setVisibility(View.VISIBLE);
            llBioTithi.setVisibility(View.VISIBLE);
        }
        spnBioSamvatYear.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //bioSamvatYearList
                spnBioSamvatYearId = typeList.get(bioSamvatYearList.indexOf(parent.getAdapter().getItem(position))).getId();
                selectedBioSamvatYear = typeList.get(bioSamvatYearList.indexOf(parent.getAdapter().getItem(position)));
                setEnabledEtSearchView();
            }
        });
        spnBioSamvatYear.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                spnBioSamvatYearId = 0;
                selectedBioSamvatYear = null;
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        spnBioSamvatYear.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (!b) {
                    // on focus off
                    String str = spnBioSamvatYear.getText().toString();
                    if (spnBioSamvatYearId == 0 && str != null)
                        spnBioSamvatYear.setText("");

                }
            }
        });
    }

    private void setBioMonthSpinnerData(ArrayList<ParamparaFilterDataResModel.SamvatMonth> typeList) {
        ArrayAdapter<String> adp = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, bioMonthList);
        spnBioMonth.setAdapter(adp);

        spnBioMonth.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long arg3) {

                spnBioMonthId = typeList.get(position).getId();
                setEnabledEtSearchView();
            }

            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub
            }
        });
    }

    private void setBioTithiSpinnerData(ArrayList<ParamparaFilterDataResModel.SamvatTithi> typeList) {
        ArrayAdapter<String> adp = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, bioTithiList);
        spnBioTithi.setAdapter(adp);

        spnBioTithi.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long arg3) {


                spnBioTithiId = typeList.get(position).getId();
                setEnabledEtSearchView();
            }

            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub
            }
        });
    }

    private void setRelationTypeSpinnerData(ArrayList<ParamparaFilterDataResModel.RelationType> typeList) {
        ArrayAdapter<String> adp = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, strRelationTypeList);
        spnRelationType.setAdapter(adp);

        spnRelationType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long arg3) {


                spnRelationTypeId = typeList.get(position).getId();
                setEnabledEtSearchView();
            }

            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub
            }
        });
    }

    private void getSearchByText() {
        //Util.hideKeyBoard(getActivity(), etSearchView);
        final String strValue = etSearchView.getText().toString();
        Util.hideKeyBoard(getActivity(), etSearchView);
        if (mKeyboardView.getVisibility() == View.VISIBLE) {
            mKeyboardView.setVisibility(View.GONE);
            mKeyboard = null;
        }
        strLanguage = SharedPrefManager.getInstance(getActivity()).getStringKeyword(SharedPrefManager.KEY_SELECT_LANGUAGE);

        if (strValue != null && strValue.length() > 0) {
            getSearchUnits();
        } else {
            Utils.showInfoDialog(getActivity(), "Please enter value in search box");
        }
    }


    @Override
    public void onUnitClick(ArrayList<SearchUnitListResModel.Unit> unitModels, int position) {

        strUnitId = unitModels.get(position).getId();
        showUnitDetails(strUnitId, unitModels.get(position));
    }


    private void setUnitListData(ArrayList<SearchUnitListResModel.Unit> unitList) {
        if (unitList == null) {
            return;
        }

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        rvList.setHasFixedSize(true);
        rvList.setLayoutManager(linearLayoutManager);
        rvList.setVisibility(View.VISIBLE);
        mSearchListAdapter = new UnitWisePramparaSearchListAdapter(getActivity(), unitList, this);
        mSearchListAdapter.notifyDataSetChanged();

//        mSearchListAdapter = new YearWiseListAdapter(getActivity(), yearList, this);
        rvList.setAdapter(mSearchListAdapter);
        int scrollToY = 1000; // Set the Y coordinate you want to scroll to
        nestedScrollView.scrollTo(0, nestedScrollView.getHeight());
//        rvList.addOnScrollListener(new RecyclerView.OnScrollListener() {

//            @Override
//            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
//                super.onScrolled(recyclerView, dx, dy);
//
//                int lastVisiblePosition = ((LinearLayoutManager)
//                        recyclerView.getLayoutManager()).findLastCompletelyVisibleItemPosition();
//
//
//                Log.e("Paginate", "onScolll Last posrtions"+ lastVisiblePosition);
//            }
//        });

        //   RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        rvList.addOnScrollListener(new HidingScrollListener() {
            @Override
            public void onHide() {
                headerView.setTranslationY(-80);
                header2.setTranslationY(-40);
                headerView.setVisibility(View.GONE);
                header2.setVisibility(View.GONE);
                appBarLayout.setMinimumHeight(0);
            }

            @Override
            public void onShow() {
                headerView.setTranslationY(0);
                header2.setTranslationY(0);
                header2.setVisibility(View.VISIBLE);
                headerView.setVisibility(View.VISIBLE);
                appBarLayout.setVisibility(View.VISIBLE);
            }
        });

        if (paginate != null) {
            paginate.unbind();
        }
        paginate = Paginate.with(rvList, this)
                .setLoadingTriggerThreshold(0)
                .addLoadingListItem(false)
                //	.setLoadingListItemCreator(customLoadingListItem ? new CustomLoadingListItemCreator() : null)
                .setLoadingListItemSpanSizeLookup(new LoadingListItemSpanLookup() {
                    @Override
                    public int getSpanSize() {
                        return 2;
                    }
                })
                .build();
        paginate.setHasMoreDataToLoad(false);
    }

    private void setHeader() {
        appBarLayout = getActivity().findViewById(R.id.appbar);
        headerView = getActivity().findViewById(R.id.header);
        header2 = getActivity().findViewById(R.id.header2);
    }
    LinearLayout llBioMonth,llBioTithi;

    ImageView ivDatePicker;
    NestedScrollView nestedScrollView;
    private void loadUiElements(View view) {
        appBarLayout = view.findViewById(R.id.appbar);
        strUId = SharedPrefManager.getInstance(getActivity()).getStringPref(SharedPrefManager.KEY_USER_ID);
        if (mKeyboardView != null)
            Util.commonKeyboardHide(getActivity());


        nestedScrollView = view.findViewById(R.id.nestedScrollView);
        spnUnitType = view.findViewById(R.id.spnUnitType);
        spnUnitStatusType = view.findViewById(R.id.spnUnitStatusType);
        spnUnitSectCommType = view.findViewById(R.id.spnUnitSectComType);
        spnBioSamvatType = view.findViewById(R.id.spnSamvatType);
        spnBioSamvatYear = view.findViewById(R.id.spnSamvatYear);
        spnBioMonth = view.findViewById(R.id.spnBioMonth);
        spnBioTithi = view.findViewById(R.id.spnBioTithi);
        llBioMonth = view.findViewById(R.id.llBioMonth);
        llBioTithi = view.findViewById(R.id.llBioTithi);
        spnRelationType = view.findViewById(R.id.spnRelationType);

        tvDatePicker= view.findViewById(R.id.tvDatePicker);
        ivDatePicker = view.findViewById(R.id.ivDatePicker);
        ivDatePickerClear = view.findViewById(R.id.ivDatePickerClear);
        etSearchView = view.findViewById(R.id.etSearchView);
        mKeyboardView = view.findViewById(R.id.keyboardView);
        ivKeyboard = view.findViewById(R.id.ivKeyboard);
        llFilter = view.findViewById(R.id.llFilter);
        llClose = view.findViewById(R.id.llClose);
        ivClose = view.findViewById(R.id.ivClose);
        ivSend = view.findViewById(R.id.ivSend);


        rvList = view.findViewById(R.id.rvList);
        searchButton = view.findViewById(R.id.btnSearch);
        llResultFound = view.findViewById(R.id.llResultFound);
        tvResultFound = view.findViewById(R.id.tvResultFound);
        tvLensCounts = view.findViewById(R.id.tvLensCounts);
        llBtnLens  = view.findViewById(R.id.llBtnLens);
        getFocusLensDialogData();
        llBtnLens.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showFilterDialog("0");
            }
        });
    }


    @Override
    public void onSelectSearchKeyword(SearchHistoryModel SearchHistoryModel, int position) {

        String strKeywordData = SearchHistoryModel.getStrSearchKeywordName();
        if (strKeywordData != null && strKeywordData.length() > 0) {
            etSearchView.setText(strKeywordData);
            if (mKeyboardView.getVisibility() == View.VISIBLE) {
                mKeyboardView.setVisibility(View.GONE);
            }
        }
    }

    ArrayList<SearchUnitListResModel.Unit> allUnitArrayList = new ArrayList<>();
    int currentPage = 0;
    int totalPages = 0;

    Integer limit = 20;
    ArrayList<SearchUnitListResModel.Unit> unitArrayList = new ArrayList<>();

    private  void setCurrentPageUnitListData(Integer page) {

    }

    public void getSearchUnits() {
        currentPage = 0;
        totalPages = 0;
        llResultFound.setVisibility(View.GONE);
        tvResultFound.setVisibility(View.GONE);
        rvList.setVisibility(View.VISIBLE);
        unitArrayList.clear();
        getSearchUnitsApi(currentPage + 1);
        isFirstTime = false;
        if (!canSearch()) {
            return;
        }
        setUnitListData(unitArrayList);


    }

    public void getSamvatsBaseOnSamvatTypeData() {
        if (!ConnectionManager.checkInternetConnection(getActivity())) {
            Utils.showInfoDialog(getActivity(), "Please check internet connection");
            return;
        }

        int bmSamvatTypeId = spnBioSamvatTypeId;

        if (bmSamvatTypeId ==0) {
            return;
        }
        Utils.showProgressDialog(getActivity(), "Please Wait...", false);

        ApiClient.getSamvatsBaseOnSamvatTypeData(bmSamvatTypeId, new Callback<SamvatsBaseOnSamvatTypeDataResModel>() {
            @Override
            public void onResponse(Call<SamvatsBaseOnSamvatTypeDataResModel> call, Response<SamvatsBaseOnSamvatTypeDataResModel> response) {
                Utils.dismissProgressDialog();

                if (response.isSuccessful()) {
                    if (response.body().isStatus()) {

                        ArrayList<ParamparaFilterDataResModel.Samvat> resBioSamvatYearList = response.body().getSamvats();
                        ParamparaFilterDataResModel.Samvat bioSamvatYear = new ParamparaFilterDataResModel.Samvat();
                        bioSamvatYearList.clear();
                        bioSamvatYear.setId(0);
                        bioSamvatYear.setName("Select Samvat Year");
                        resBioSamvatYearList.add(0, bioSamvatYear);
                        //Log.e("error", "theme---" + message);

                        if (resBioSamvatYearList != null && resBioSamvatYearList.size() > 0) {
                            for (int i = 0; i < resBioSamvatYearList.size(); i++) {
                                ParamparaFilterDataResModel.Samvat samvat = resBioSamvatYearList.get(i);
                                String strYear = samvat.getSamvatFormatedName();
                                bioSamvatYearList.add(strYear);
                            }

                            setBioSamvatYearSpinnerData(resBioSamvatYearList);

                        }
                    } else {

                    }
                    isLoading = false;
                }
            }

            @Override
            public void onFailure(Call<SamvatsBaseOnSamvatTypeDataResModel> call, Throwable t) {
                String message = t.getMessage();
                Log.e("error", "theme---" + message);
                Utils.dismissProgressDialog();
            }
        });
    }


    public void getSearchUnitsApi(int pageNo) {
        if (!ConnectionManager.checkInternetConnection(getActivity())) {
            Utils.showInfoDialog(getActivity(), "Please check internet connection");
            return;
        }

        if (!canSearch()) {
            return;
        }
        Utils.showProgressDialog(getActivity(), "Please Wait...", false);
        Integer unitTypeId = spnUnitTypeId;
        Integer unitStatusId = spnUnitStatusTypeId;
        Integer unitSectId = spnUnitSectCommTypeId;
        Integer bmSamvatTypeId = spnBioSamvatTypeId;
        Integer bmSamvatId = spnBioSamvatYearId;
        Integer bmMonthId = spnBioMonthId;
        Integer bmTithiId = spnBioTithiId;
        String bmDate = strBiodate;
        String search =  etSearchView.getText().toString().trim();

        Integer relationId = spnRelationTypeId;

        searchBioSamvatYear = selectedBioSamvatYear;

        String strLensFilterTypeID = SharedPrefManager.getInstance(getActivity()).getStringPref(SharedPrefManager.KEY_FILTER_LENS_IDS);
        ApiClient.getSearchUnitList(pageNo, unitTypeId, unitStatusId, unitSectId, bmSamvatTypeId, bmSamvatId, bmMonthId, bmTithiId, bmDate, relationId, search, strLensFilterTypeID, new Callback<SearchUnitListResModel>() {
            @Override
            public void onResponse(Call<SearchUnitListResModel> call, Response<SearchUnitListResModel> response) {
                Utils.dismissProgressDialog();
                Log.e("Unit Search List :", ""+response.body().isStatus());

                if (response.isSuccessful()) {
                    /*Log.e("responseData :", new GsonBuilder().setPrettyPrinting().create().toJson(response));*/

                    if (response.body().isStatus()) {
                        ArrayList<SearchUnitListResModel.Unit> pageUnitArrayList = response.body().getData();

                        Log.e("Unit Search List :", ""+pageUnitArrayList.size() + "/" + totalPages);


                        if (pageUnitArrayList != null && pageUnitArrayList.size() > 0) {
                            for(Integer i = 0 ; i < pageUnitArrayList.size(); i++) {
                                unitArrayList.add(pageUnitArrayList.get(i));
                            }
                            rvList.setVisibility(View.VISIBLE);
                        }
                        currentPage = pageNo;
                        if (pageNo == 1) {
                            String tvResultFoundStr = response.body().getMeta_data().getResult_founds() + " results founds in " + response.body().getMeta_data().getFound_unit_count() + " units";
                            tvResultFound.setText(tvResultFoundStr);
                            tvResultFound.setVisibility(View.VISIBLE);
                            llResultFound.setVisibility(View.VISIBLE);
                            setUnitListData(unitArrayList);
                            totalPages = response.body().getMeta_data().getTotal_pages();

                        }
                        else if(mSearchListAdapter !=null){
                            mSearchListAdapter.setUnitList(unitArrayList);
                            mSearchListAdapter.notifyDataSetChanged();
                        }else {
                            setUnitListData(unitArrayList);
                        }
                        isFirstTime = true;
                        //tvFilterCounts.setText(totalCount + " Books");
                        /*String strYearFilter = SharedPrefManager.getInstance(getActivity()).getStringPref(SharedPrefManager.KEY_YEAR_FILTER_CAT_IDS);
                        tvFilterCounts.setText(strYearFilter + " Books");*/
                    } else {

                    }
                    isLoading = false;
                }
            }

            @Override
            public void onFailure(Call<SearchUnitListResModel> call, Throwable t) {
                String message = t.getMessage();
                Log.e("error", "theme---" + message);
                Utils.dismissProgressDialog();
            }
        });
    }

    private void getParamparaFilterData() {
        if (!ConnectionManager.checkInternetConnection(getActivity())) {
            Utils.showInfoDialog(getActivity(), "Please check internet connection");
            return;
        }
        Utils.showProgressDialog(getActivity(), "Please Wait...", false);
        ApiClient.getParamparaFilterData(new Callback<ParamparaFilterDataResModel>() {
            @Override
            public void onResponse(Call<ParamparaFilterDataResModel> call, Response<ParamparaFilterDataResModel> response) {
                Utils.dismissProgressDialog();
                if (response.isSuccessful()) {

                    if (response.body().isStatus()) {
                        ArrayList<ParamparaFilterDataResModel.UnitType> resUnitTypesList = response.body().getUnit_types();
                        ParamparaFilterDataResModel.UnitType unitType = new ParamparaFilterDataResModel.UnitType();
                        unitType.setId(0);
                        unitType.setName("Select Unit Type");
                        resUnitTypesList.add(0, unitType);
                        //Log.e("error", "theme---" + message);

                        if (resUnitTypesList != null && resUnitTypesList.size() > 0) {
                            for (int i = 0; i < resUnitTypesList.size(); i++) {
                                String strType = response.body().getUnit_types().get(i).getName();
                                unitTypeList.add(strType);
                            }

                            if (unitTypeList.size() > 0) {
                                setUnitTypeSpinnerData(resUnitTypesList);
                            }
                        }

                        ArrayList<ParamparaFilterDataResModel.UnitStatusType> resUnitStatusTypesList = response.body().getStatus_types();
                        ParamparaFilterDataResModel.UnitStatusType unitStatusType = new ParamparaFilterDataResModel.UnitStatusType();
                        unitStatusType.setId(0);
                        unitStatusType.setName("Select Status Type");
                        resUnitStatusTypesList.add(0, unitStatusType);
                        //Log.e("error", "theme---" + message);

                        if (resUnitStatusTypesList != null && resUnitStatusTypesList.size() > 0) {
                            for (int i = 0; i < resUnitStatusTypesList.size(); i++) {
                                String strType = response.body().getStatus_types().get(i).getName();
                                unitStatusTypeList.add(strType);
                            }

                            if (unitStatusTypeList.size() > 0) {
                                setUnitStatusTypeSpinnerData(resUnitStatusTypesList);
                            }
                        }

                        ArrayList<ParamparaFilterDataResModel.UnitSectCommunityType> resUnitSectCommTypesList = response.body().getSect_community_types();
                        ParamparaFilterDataResModel.UnitSectCommunityType unitSectCommunityType = new ParamparaFilterDataResModel.UnitSectCommunityType();
                        unitSectCommunityType.setId(0);
                        unitSectCommunityType.setName("Select Sect / Community Type");
                        resUnitSectCommTypesList.add(0, unitSectCommunityType);
                        //Log.e("error", "theme---" + message);

                        if (resUnitSectCommTypesList != null && resUnitSectCommTypesList.size() > 0) {
                            for (int i = 0; i < resUnitSectCommTypesList.size(); i++) {
                                String strType = response.body().getSect_community_types().get(i).getName();
                                unitSectCommTypeList.add(strType);
                            }

                            if (unitSectCommTypeList.size() > 0) {
                                setUnitSectCommTypeSpinnerData(resUnitSectCommTypesList);
                            }
                        }

                        ArrayList<ParamparaFilterDataResModel.SamvatType> resBioSamvatTypesList = response.body().getSamvat_types();
//                        ParamparaFilterDataResModel.SamvatType unitBioSamvatype = new ParamparaFilterDataResModel.SamvatType();
//                        unitBioSamvatype.setId(0);
//                        unitBioSamvatype.setName("Select Samvat Type");
//                        resBioSamvatTypesList.add(0, unitBioSamvatype);
                        //Log.e("error", "theme---" + message);

                        if (resBioSamvatTypesList != null && resBioSamvatTypesList.size() > 0) {
                            for (int i = 0; i < resBioSamvatTypesList.size(); i++) {
                                String strType = response.body().getSamvat_types().get(i).getName();
                                if (i == 0) {
                                    spnBioSamvatTypeId = response.body().getSamvat_types().get(i).getId();
                                    samvatType = response.body().getSamvat_types().get(i);
                                }
                                bioSamvatTypeList.add(strType);
                            }

                            if (bioSamvatTypeList.size() > 0) {
                                setBioSamvatTypeSpinnerData(resBioSamvatTypesList);
                            }
                        }


                        ArrayList<ParamparaFilterDataResModel.Samvat> resBioSamvatYearList = response.body().getSamvats();
                        ParamparaFilterDataResModel.Samvat bioSamvatYear = new ParamparaFilterDataResModel.Samvat();
                        bioSamvatYear.setId(0);
                        bioSamvatYear.setName("Select Samvat Year");
                        resBioSamvatYearList.add(0, bioSamvatYear);
                        //Log.e("error", "theme---" + message);

                        if (resBioSamvatYearList != null && resBioSamvatYearList.size() > 0) {
                            for (int i = 0; i < resBioSamvatYearList.size(); i++) {
                                ParamparaFilterDataResModel.Samvat samvat = response.body().getSamvats().get(i);

                                String strYear = samvat.getSamvatFormatedName();
                                bioSamvatYearList.add(strYear);

                            }

                            if (bioSamvatYearList.size() > 0) {
                                setBioSamvatYearSpinnerData(resBioSamvatYearList);
                            }
                        }

                        ArrayList<ParamparaFilterDataResModel.SamvatMonth> resBioMonthList = response.body().getSamvat_months();
                        ParamparaFilterDataResModel.SamvatMonth samvat_month = new ParamparaFilterDataResModel.SamvatMonth();
                        samvat_month.setId(0);
                        samvat_month.setName("Select Month");
                        resBioMonthList.add(0, samvat_month);
                        //Log.e("error", "theme---" + message);

                        if (resBioMonthList != null && resBioMonthList.size() > 0) {
                            for (int i = 0; i < resBioMonthList.size(); i++) {
                                String strYear = response.body().getSamvat_months().get(i).getName();
                                bioMonthList.add(strYear);
                            }

                            if (bioMonthList.size() > 0) {
                                setBioMonthSpinnerData(resBioMonthList);
                            }
                        }

                        ArrayList<ParamparaFilterDataResModel.SamvatTithi> resBioTithiList = response.body().getSamvat_tithis();
                        ParamparaFilterDataResModel.SamvatTithi samvat_tithi = new ParamparaFilterDataResModel.SamvatTithi();
                        samvat_tithi.setId(0);
                        samvat_tithi.setName("Select Tithi");
                        resBioTithiList.add(0, samvat_tithi);
                        //Log.e("error", "theme---" + message);

                        if (resBioTithiList != null && resBioTithiList.size() > 0) {
                            for (int i = 0; i < resBioTithiList.size(); i++) {
                                String strYear = response.body().getSamvat_tithis().get(i).getName();
                                bioTithiList.add(strYear);
                            }

                            if (bioTithiList.size() > 0) {
                                setBioTithiSpinnerData(resBioTithiList);
                            }
                        }


                        ArrayList<ParamparaFilterDataResModel.RelationType> resRelationTypeList = response.body().getRelation_types();
                        relationTypeList = response.body().getRelation_types();
                        ParamparaFilterDataResModel.RelationType relation_Type = new ParamparaFilterDataResModel.RelationType();
                        relation_Type.setId(0);
                        relation_Type.setName("Select Relation Type");
                        resRelationTypeList.add(0, relation_Type);
                        //Log.e("error", "theme---" + message);

                        if (resRelationTypeList != null && resRelationTypeList.size() > 0) {
                            for (int i = 0; i < resRelationTypeList.size(); i++) {
                                String strYear = response.body().getRelation_types().get(i).getName();
                                strRelationTypeList.add(strYear);
                            }

                            if (strRelationTypeList.size() > 0) {
                                setRelationTypeSpinnerData(resRelationTypeList);
                            }
                        }


                    } else {
                        Utils.showInfoDialog(getActivity(), "Year Type not found");
                    }


                }
            }

            @Override
            public void onFailure(Call<ParamparaFilterDataResModel> call, Throwable t) {
                String message = t.getMessage();
                Log.e("error", "theme---" + message);
                Utils.dismissProgressDialog();
            }
        });
    }

    private void showUnitDetails(String strUnitId, SearchUnitListResModel.Unit searchUnit) {

        Intent intent = new Intent(getActivity(), UnitDetailsActivity.class);
        intent.putExtra("strUnitId", strUnitId);
        intent.putExtra("searchUnitModel", searchUnit);
        intent.putExtra("searchBioSamvatYear", searchBioSamvatYear);
        intent.putExtra("relationTypeList", relationTypeList);
        startActivity(intent);

    }


    boolean isFirstTime = false, isLoading = false;


    private int getRVListLastItemPosition() {
        int lastVisibleItemPosition = 0;

        lastVisibleItemPosition = ((LinearLayoutManager)
                rvList.getLayoutManager()).findLastCompletelyVisibleItemPosition();
        return lastVisibleItemPosition;
    }

    private int getRVListTotalItemCount() {
        return rvList.getLayoutManager().getItemCount();
    }

    private boolean canLoadMore() {
        if (rvList == null || rvList.getLayoutManager() == null) {
            return  false;
        }
        Log.e("Paginate", "onLoadMore" + "getRVListLastItemPosition" + getRVListLastItemPosition() +"getRVListTotalItemCount:" + getRVListTotalItemCount() + "totalPages:" + totalPages);

        return (getRVListLastItemPosition() + 2) > getRVListTotalItemCount();
    }
    @Override
    public void onLoadMore() {
        if (isFirstTime && !isLoading && canLoadMore()) {
            isLoading = true;
            Log.e("item", unitArrayList.size() + "");
            getSearchUnitsApi(currentPage + 1);
       }
    }
    @Override
    public boolean isLoading() {
        return isLoading;
    }
    @Override
    public boolean hasLoadedAllItems() {
        isLoading = false;
        Log.e("page total page", (currentPage >= totalPages) + "");
        return currentPage >= totalPages;
    }


    private static final int KEYWORD_FILTER = 2;

    public void showFilterDialog(String tabId) {
      //  strKeyword = etSearchView.getText().toString();

        boolean isLogin = SharedPrefManager.getInstance(getActivity()).getBooleanPreference(SharedPrefManager.IS_LOGIN);
        if (isLogin) {
            JRL.isKeywordLoading = false;
            Intent i = new Intent(getActivity(), FilterMenuActivity.class);
          //  Log.e("keyword", strKeyword);
          //  i.putExtra("KID", strKeyword);
            i.putExtra("SettingPosition", tabId);
            startActivityForResult(i, KEYWORD_FILTER);
        } else {
            askLogin();
        }
    }

    void askLogin() {
        Utils.showLoginDialogForResult(getActivity(), KEYWORD_SEARCH);
    }



    private void getFocusLensDialogData() {
        String strLensCounts = SharedPrefManager.getInstance(getActivity()).getStringPref(SharedPrefManager.KEY_LENS_DATA);

        //Log.e("FocusCount--", strFocusCounts);

        if (strLensCounts != null && strLensCounts.length() > 0) {
            tvLensCounts.setText(strLensCounts);
            tvLensCounts.setVisibility(View.VISIBLE);
        } else {
            tvLensCounts.setVisibility(View.GONE);
        }
    }




}
