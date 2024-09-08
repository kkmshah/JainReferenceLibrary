package com.jainelibrary.retrofit;

import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.jainelibrary.model.AddDataFeedbackResModel;
import com.jainelibrary.model.AddShelfResModel;
import com.jainelibrary.model.BooksDataModel;
import com.jainelibrary.model.CategoryResModel;
import com.jainelibrary.model.DeleteMyShelfResModel;
import com.jainelibrary.model.FeedbackTypeResModel;
import com.jainelibrary.model.HoldAndSearchResModel;
import com.jainelibrary.model.MarkingBookModel;
import com.jainelibrary.model.MyShelfResModel;
import com.jainelibrary.model.ParamparaFilterDataResModel;
import com.jainelibrary.model.PdfStoreListResModel;
import com.jainelibrary.model.ApiResponseModel;
import com.jainelibrary.model.ReferenceTypeResModel;
import com.jainelibrary.model.SearchUnitListResModel;
import com.jainelibrary.model.SendOtpResModel;
import com.jainelibrary.model.UpdateMyShelfNotesModel;
import com.jainelibrary.model.UploadPDFModel;
import com.jainelibrary.model.UserDetailsResModel;
import com.jainelibrary.model.UserExistsResModel;
import com.jainelibrary.model.UserGuideResModel;
import com.jainelibrary.model.YearResModel;
import com.jainelibrary.model.YearResponseModel;
import com.jainelibrary.model.YearTypeResModel;
import com.jainelibrary.retrofitResModel.AddAllMyShelfResModel;
import com.jainelibrary.retrofitResModel.AppDataAndUsersResModel;
import com.jainelibrary.retrofitResModel.BiodataMemoryDetailsResModel;
import com.jainelibrary.retrofitResModel.BookListResModel;
import com.jainelibrary.retrofitResModel.BookReferenceDetailsResModel;
import com.jainelibrary.retrofitResModel.CheckMyShelfFileNameResModel;
import com.jainelibrary.retrofitResModel.CheckResModel;
import com.jainelibrary.retrofitResModel.ClearHoldReferenceModel;
import com.jainelibrary.retrofitResModel.CountResModel;
import com.jainelibrary.retrofitResModel.CreatePdfFileUrlResModel;
import com.jainelibrary.retrofitResModel.FilterBookResModel;
import com.jainelibrary.retrofitResModel.KeywordSearchModel;
import com.jainelibrary.retrofitResModel.ReferencePageDetailsResModel;
import com.jainelibrary.retrofitResModel.ReferenceResModel;
import com.jainelibrary.retrofitResModel.RelationDetailsResModel;
import com.jainelibrary.retrofitResModel.RelationTypeListResModel;
import com.jainelibrary.retrofitResModel.SamvatsBaseOnSamvatTypeDataResModel;
import com.jainelibrary.retrofitResModel.SaveFilterBooksResModel;
import com.jainelibrary.retrofitResModel.SearchOptionResModel;
import com.jainelibrary.retrofitResModel.SendEmailResModel;
import com.jainelibrary.retrofitResModel.ShareOrDownloadMyShelfResModel;
import com.jainelibrary.retrofitResModel.ShlokGranthSutraResModel;
import com.jainelibrary.retrofitResModel.ShlokSearchResModel;
import com.jainelibrary.retrofitResModel.UnitDetailsResModel;
import com.jainelibrary.retrofitResModel.UnitRelationChartDataResModel;
import com.jainelibrary.retrofitResModel.VersionResModel;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    private static final MediaType MEDIA_TYPE_PNG = MediaType.parse("image/png");
    private static final MediaType MEDIA_TYPE_JPEG = MediaType.parse("image/jpeg");
    public static Retrofit mRetrofit;
    //static String BASE_URL = "http://mywantgroup.com/jts/apis/jrl/";
    public static String BASE_URL = "http://jaintatvagyanshala.org/apis/";

    public static Retrofit getRetrofitInstance() {
        if (null == mRetrofit) {
            okhttp3.OkHttpClient.Builder clientBuilder = new okhttp3.OkHttpClient.Builder();
            clientBuilder.connectTimeout(100, TimeUnit.SECONDS);
            clientBuilder.readTimeout(100, TimeUnit.SECONDS);
            /*client.setConnectTimeout(180, TimeUnit.SECONDS);
			client.setReadTimeout(180, TimeUnit.SECONDS);*/
            clientBuilder.writeTimeout(15, TimeUnit.SECONDS);
            clientBuilder.interceptors().add(new ApiLogInterceptor());
            okhttp3.OkHttpClient client = clientBuilder.build();
            //client.interceptors();
            Retrofit.Builder builder = new Retrofit.Builder();
            Gson gson = new GsonBuilder()
                    .setLenient()
                    .create();
            builder.baseUrl(!TextUtils.isEmpty(BASE_URL) ? BASE_URL : BASE_URL);
            builder.addConverterFactory(GsonConverterFactory.create(gson));
            builder.client(client);
            builder.build();
            mRetrofit = builder.build();
        }
        return mRetrofit;
    }

    private static ApiInterface getApiClient() {
        return getRetrofitInstance().create(ApiInterface.class);
    }

    public static void getVersion(Callback<VersionResModel> listener) {
        getApiClient().getVersion().enqueue(listener);
    }

    public static void sendOtp(String strMobile, Callback<SendOtpResModel> listener) {
        getApiClient().sendOtp(strMobile).enqueue(listener);
    }

    public static void login(String strMobile, String strPassword, Callback<UserDetailsResModel> listener) {
        getApiClient().login(strMobile, strPassword).enqueue(listener);
    }

    public static void verifyOtp(String strMobile, String strOtp, Callback<UserDetailsResModel> listener) {
        getApiClient().verifyOtp(strMobile, strOtp).enqueue(listener);
    }

    public static void signup(String strName, String strMobile, String strEmail, String strUsername, String strPassword, Callback<ApiResponseModel> listener) {
        getApiClient().signup(strName, strMobile, strEmail, strUsername, strPassword).enqueue(listener);
    }

    public static void registerUser(String strName, String strMobile, String strEmail, Callback<ApiResponseModel> listener) {
        getApiClient().registerUser(strName, strMobile, strEmail).enqueue(listener);
    }

    public static void getSearchShlok(String strUid, String strKeyword, String strFlag, Callback<ShlokSearchResModel> listener) {
        getApiClient().getSearchShlok(strUid, strKeyword, strFlag).enqueue(listener);
    }

    public static void getIndexBookList(Callback<BookListResModel> listener) {
        getApiClient().getIndexBookList().enqueue(listener);
    }

    public static void getBookIndex(String strBookId, Callback<BookListResModel> listener) {
        getApiClient().getBookIndex(strBookId).enqueue(listener);
    }

    public static void getShlokGranthSutra(String strShlokGranthId, String strSearch, String strPageNo, Callback<ShlokGranthSutraResModel> listener) {
        getApiClient().getShlokGranthSutra(strShlokGranthId, strSearch, strPageNo).enqueue(listener);
    }

    public static void getShlokBooks(String strUID, String strGSID, Callback<BookListResModel> listener) {
        getApiClient().getShlokBooks(strUID, strGSID).enqueue(listener);
    }

    public static void getKeyword(String searchFor, String pageNo, String strKeyword, String strUid, String strBookId, String lang_code, String types,String bookInfo, Callback<KeywordSearchModel> listener) {
        getApiClient().getKeyword(searchFor, pageNo, strKeyword, strUid, strBookId, lang_code, types, bookInfo).enqueue(listener);
    }

    public static void getKeywordsPdf(String pageNo, String strKeyword, String strUid, String strBookId, String lang_code,String pdfType, String types, Callback<ResponseBody> listener) {
        getApiClient().getKeywordsPdf(pageNo, strKeyword, strUid, strBookId, lang_code,pdfType, types).enqueue(listener);
    }

    public static void createKeywordsPdf(String strKeyword, String strUid, String strBookId, String lang_code,String pdfType, Callback<CreatePdfFileUrlResModel> listener) {
        getApiClient().createKeywordsPdf( strKeyword, strUid, strBookId, lang_code,pdfType).enqueue(listener);
    }

    public static void getKeywordBookDetailsPdf(String strBookDetails, String strBids, Callback<ResponseBody> listener) {
        getApiClient().getKeywordBookDetailsPdf(strBookDetails, strBids).enqueue(listener);
    }

    public static void createKeywordBookDetailsPdf(String strKid, String strBookIds, Callback<CreatePdfFileUrlResModel> listener) {
        getApiClient().createKeywordBookDetailsPdf(strKid, strBookIds).enqueue(listener);
    }

    public static void getShlokGranthDetailsPdf(String strGSId, Callback<ResponseBody> listener) {
        getApiClient().getShlokGranthDetailsPdf(strGSId).enqueue(listener);
    }

    public static void createShlokGranthDetailsPdf(String strGSId, Callback<CreatePdfFileUrlResModel> listener) {
        getApiClient().createShlokGranthDetailsPdf(strGSId).enqueue(listener);
    }

    public static void getKeywordBookDetails(String strUId, String strBookDetails, String strBids, Callback<BookListResModel> listener) {
        getApiClient().getKeywordBookDetails(strUId, strBookDetails, strBids).enqueue(listener);
    }

    public static void getBookPage(String strType, String strTypeId, String strBId, String strPId, String strDirection, String strFlag, String strHighlit, Callback<BooksDataModel> listener) {
        getApiClient().getBookPage(strType, strTypeId, strBId, strPId, strDirection, strFlag, strHighlit).enqueue(listener);
    }

    public static void getMarkingBookPage(String strKId, String strBId, String strPId, Callback<MarkingBookModel> listener) {
        getApiClient().getMarkingBookPage(strKId, strBId, strPId).enqueue(listener);
    }

    public static void getBookAppendixDetails(String strBId, String strFlag, Callback<BookListResModel> listener) {
        getApiClient().getBookAppendixDetails(strBId, strFlag).enqueue(listener);
    }

    public static void getBookReferenceDetails(String strBId, String strFlag, String strPageNo, String strPageLimit, String strReference, String strReferenceType, Callback<BookReferenceDetailsResModel> listener) {
        getApiClient().getBookReferenceDetails(strBId, strFlag, strPageNo, strPageLimit, strReference, strReferenceType).enqueue(listener);
    }

    public static void getBookReferences(String strBId, String strPageNo, String strPageLimit, String strReference, String strReferenceType, Callback<BookReferenceDetailsResModel> listener) {
        getApiClient().getBookReferences(strBId, strPageNo, strPageLimit, strReference, strReferenceType).enqueue(listener);
    }

    public static void getBookReferencePages(String strBId, String strPageNo, String strPageLimit, String strReference, String strReferenceType, String strPageDiff, Callback<BookReferenceDetailsResModel> listener) {
        getApiClient().getBookReferencePages(strBId, strPageNo, strPageLimit, strReference, strReferenceType, strPageDiff).enqueue(listener);
    }

    public static void getBookReferencePageDetails(String strTypeId, String strReferenceId, String strReference, String strReferenceType, Callback<ReferencePageDetailsResModel> listener) {
        getApiClient().getBookReferencePageDetails(strTypeId, strReferenceId, strReference, strReferenceType).enqueue(listener);
    }

    public static void getReferenceTypes(String strType, Callback<ReferenceTypeResModel> listener) {
        getApiClient().getReferenceTypes(strType).enqueue(listener);
    }

    public static void addReference(String strUserId, String strBookId, String strType, String strTypeId, String strTypeValue, String strPDFPageNo, String strPageNo, Callback<ReferenceResModel> listener) {
        getApiClient().addReference(strUserId, strBookId, strType, strTypeId, strTypeValue, strPDFPageNo, strPageNo).enqueue(listener);
    }

    public static void updateReference(String strUserId, String strTypeId, String strReferenceId, String strTypeValue, String strPDFPageNo, String strPageNo, Callback<ReferenceResModel> listener) {
        getApiClient().updateReference(strUserId, strTypeId, strReferenceId, strTypeValue, strPDFPageNo, strPageNo).enqueue(listener);
    }

    public static void updateReferenceData(String strTypeId, String strReferenceId, String x1, String y1, String x2, String y2, Callback<ReferenceResModel> listener) {
        getApiClient().updateReferenceData(strTypeId, strReferenceId, x1, y1, x2, y2).enqueue(listener);
    }

    public static void updateReferenceStatus(String strTypeId, String strReferenceId, int is_checked, Callback<ReferenceResModel> listener) {
        getApiClient().updateReferenceStatus(strTypeId, strReferenceId, is_checked).enqueue(listener);
    }

    public static void deleteReference(String strTypeId, String strReferenceId, Callback<ReferenceResModel> listener) {
        getApiClient().deleteReference(strTypeId, strReferenceId).enqueue(listener);
    }

    public static void getMyShelfList(String strUId, String strFlag, String strTypeRef, Callback<MyShelfResModel> listener) {
        getApiClient().getMyShelfList(strUId, strFlag, strTypeRef).enqueue(listener);
    }

    public static void getSearchMyShelf(String strUId, String strKeyword, String strFlag, String strTypeRef, Callback<MyShelfResModel> listener) {
        getApiClient().getSearchMyShelf(strUId, strKeyword, strFlag, strTypeRef).enqueue(listener);
    }

    public static void getAllCategory(String strUserId, Callback<CategoryResModel> listener) {
        getApiClient().getAllCategory(strUserId).enqueue(listener);
    }

    public static void getCategory(String allBooks, String kid, String strUserId, Callback<CategoryResModel> listener) {
        getApiClient().getCategory(allBooks, kid, strUserId).enqueue(listener);
    }

    public static void addMyShelf(String uid, String bid, String kid, String type, String type_ref, String filename, String mFileList, Callback<AddShelfResModel> listener) {
        JSONObject data = new JSONObject();
        try {
            data.put("uid", uid);
            data.put("book_id", bid);
            data.put("type_id", kid);
            data.put("type", type);
            data.put("type_ref", type_ref);
            data.put("file_name", filename);
            data.put("pdf_file", mFileList);

        } catch (Exception e) {
            Log.e("ApiClient--", "" + e.getMessage());
        }
        getApiClient().addMyShelf(okhttp3.RequestBody.create(JSON, data.toString())).enqueue(listener);
    }

    public static void deleteMyReferenceImage(JSONArray mImageIdList, Callback<DeleteMyShelfResModel> listener) {
        JSONObject data = new JSONObject();
        try {
            data.put("page_Ids", mImageIdList);
        } catch (Exception e) {
            Log.e("ApiClient--", "" + e.getMessage());
        }
        getApiClient().deleteMyReferenceImage(okhttp3.RequestBody.create(JSON, data.toString())).enqueue(listener);
    }

    public static void deleteMyShelf(String strId,Callback<DeleteMyShelfResModel> listener) {
        getApiClient().deleteMyShelf(strId).enqueue(listener);
    }

    public static void addNewHoldSearchKeyword(String strUserId, String strBookId, String strTypeId, String strType,
                                               String strPageNo, String strPdfPageNo, Callback<HoldAndSearchResModel> listener) {
        getApiClient().addNewHoldSearchKeyword(strUserId, strBookId, strTypeId, strType, strPageNo, strPdfPageNo).enqueue(listener);
    }

    /*public static void addHoldSearchKeyword(String strUserId, String strBookId, String strKeyword,
                                            String strPageNo, String strAuthorName, String strTranslatorName,
                                            String strEditorName, Callback<HoldAndSearchResModel> listener) {
        getApiClient().addHoldSearchKeyword(strUserId, strBookId, strKeyword, strPageNo, strAuthorName,
                strTranslatorName, strEditorName).enqueue(listener);
    }*/

    public static void getHolderSearchList(String strUserId, Callback<BookListResModel> listener) {
        getApiClient().getHolderSearchList(strUserId, "0").enqueue(listener);
    }

    public static void deleteHolderSearchList(String strUserId, Callback<HoldAndSearchResModel> listener) {
        getApiClient().deleteHolderSearchList(strUserId).enqueue(listener);
    }

    public static void getUserAdmin(String strUserId, Callback<UserDetailsResModel> listener) {
        getApiClient().getUserAdmin(strUserId).enqueue(listener);
    }

    public static Call<PdfStoreListResModel> getPdfList(String strUserId, String strSearch, String strCatId, String strType, String strPageNo, Callback<PdfStoreListResModel> listener) {
        Call<PdfStoreListResModel> callback = getApiClient().getPdfList(strUserId, strSearch, strCatId, strType, strPageNo);
        callback.enqueue(listener);
        return callback;
    }

    public static void getPaginatewithPDF(String strSearch, String strCatId, String strType, String strpage, String strlimit, Callback<PdfStoreListResModel> listener) {
        getApiClient().getPaginatewithPDF(strSearch, strCatId, strType, strpage, strlimit).enqueue(listener);
    }

    public static void updateMyNotes(String keyword, String notes, Callback<UpdateMyShelfNotesModel> listener) {
        JSONObject data = new JSONObject();
        try {
            data.put("id", keyword);
            data.put("notes", notes);

        } catch (Exception e) {
            Log.e("ApiClient--", "" + e.getMessage());
        }
        getApiClient().updateNotes(okhttp3.RequestBody.create(JSON, data.toString())).enqueue(listener);
    }

    public static void sendEmail(String strName, String strEmail, String strMessage, Callback<SendEmailResModel> listener) {
        getApiClient().sendEmail(strName, strEmail, strMessage).enqueue(listener);
    }

    public static void countKeywords(Callback<CountResModel> listener) {
        getApiClient().countKeywords().enqueue(listener);
    }

    public static void countRegisterUser(Callback<CountResModel> listener) {
        getApiClient().countRegisterUser().enqueue(listener);
    }

    public static void countSearch(Callback<CountResModel> listener) {
        getApiClient().countSearch().enqueue(listener);
    }

    public static void countBooks(Callback<CountResModel> listener) {
        getApiClient().countbooks().enqueue(listener);
    }

    public static void getAppDataandUsers(Callback<AppDataAndUsersResModel> listener) {
        getApiClient().getAppDataAndUsers().enqueue(listener);
    }

    public static void getFilterSearchOption(String strUid, Callback<SearchOptionResModel> listener) {
        getApiClient().getFilterSearchOption(strUid).enqueue(listener);
    }

    public static void saveSearchTypes(String strUserId, String strSearchTypes, Callback<SaveFilterBooksResModel> listener) {
        getApiClient().saveSearchTypes(strUserId, strSearchTypes).enqueue(listener);
    }

    public static void getTotalPages(Callback<CountResModel> listener) {
        getApiClient().getTotalPages().enqueue(listener);
    }

    public static void getYear(Callback<YearResModel> listener) {
        getApiClient().getyear().enqueue(listener);
    }

    public static void CheckUSerName(String strUsername, Callback<CheckResModel> listener) {
        getApiClient().checkusername(strUsername).enqueue(listener);
    }

    public static void saveFilterBooks(String strUserId, String strBookIds, Callback<SaveFilterBooksResModel> listener) {
        getApiClient().saveFilterBooks(strUserId, strBookIds).enqueue(listener);
    }

    public static void getFilterBookList(String strUserId, Callback<FilterBookResModel> listener) {
        getApiClient().getFilterBookList(strUserId).enqueue(listener);
    }

    public static void getUserDetails(String strUid, Callback<UserDetailsResModel> listener) {
        getApiClient().getuserdetails(strUid).enqueue(listener);
    }

    public static void getUserGuide(String strType, Callback<UserGuideResModel> listener) {
        getApiClient().getUserGuide(strType).enqueue(listener);
    }

    public static void addAllMyShelf(String strUid, String strBookId, String strDirection, Callback<AddAllMyShelfResModel> listener) {
        getApiClient().addAllMyShelf(strUid, strBookId, strDirection).enqueue(listener);
    }

    public static void getYearList(String strYear, Callback<BookListResModel> listener) {
        getApiClient().getyearlist(strYear).enqueue(listener);
    }



    public static void clearHoldReferenceApi(String strUserId, Callback<ClearHoldReferenceModel> listener) {
        getApiClient().clearHoldReferenceApi(strUserId).enqueue(listener);
    }

    public static void UploadFile(MultipartBody.Part builder, RequestBody strUID, RequestBody strFile, RequestBody strType, RequestBody typeref, Callback<UploadPDFModel> listener) {
        getApiClient().uploadFile(builder, strUID, strFile, strType, typeref).enqueue(listener);
    }

    public static void addMyShelfs(RequestBody uid, RequestBody bid, RequestBody kid, RequestBody type, RequestBody type_ref,
                                   RequestBody filename, RequestBody strTypeName, RequestBody count,RequestBody file_type, MultipartBody.Part file, Callback<AddShelfResModel> listener) {
        getApiClient().addMyShelfs(uid, bid, kid, type, type_ref, filename, strTypeName,count, file_type, file).enqueue(listener);
    }

    public static void addMyShelfsMultipleFiles(RequestBody uid, RequestBody bid, RequestBody kid, RequestBody type, RequestBody type_ref,
                                                RequestBody filename, RequestBody strTypeName, RequestBody count, RequestBody file_type, List<MultipartBody.Part> files, Callback<AddShelfResModel> listener) {
        getApiClient().addMyShelfsMultipleFiles(uid, bid, kid, type, type_ref, filename, strTypeName,count, file_type, files).enqueue(listener);
    }

    public static void addMyShelfsWithUrl(String uid, String bid, String  kid, String  type, String  type_ref,
                                   String  filename, String strTypeName, String count,String file_type, String file_url, Callback<AddShelfResModel> listener) {
        getApiClient().addMyShelfsWithUrl(uid, bid, kid, type, type_ref, filename, strTypeName,count, file_type, file_url).enqueue(listener);
    }

    public static void addMyShelfWithImagesUrl(String uid, String bid, String  kid, String  type, String  type_ref,
                                               String  filename, String strTypeName, String count, String file_type, String json_images_url, Callback<AddShelfResModel> listener) {
        getApiClient().addMyShelfWithImagesUrl(uid, bid, kid, type, type_ref, filename, strTypeName,count, file_type, json_images_url).enqueue(listener);
    }
    public static void checkMyShelfFileName(String uid, String  filename, Callback<CheckMyShelfFileNameResModel> listener) {
        getApiClient().checkMyShelfFileName(uid, filename).enqueue(listener);
    }

    public static void shareMyShelfs(String strUserId, String type_ref, Callback<ShareOrDownloadMyShelfResModel> listener) {
        getApiClient().shareMyShelfs(strUserId, type_ref).enqueue(listener);
    }

    public static void downloadMyShelfs(String strUserId, String type_ref, Callback<ShareOrDownloadMyShelfResModel> listener) {
        getApiClient().downloadMyShelfs(strUserId, type_ref).enqueue(listener);
    }

    public static void checkUserExists(String strMobile, Callback<UserExistsResModel> listener) {
        getApiClient().checkUserExists(strMobile).enqueue(listener);
    }

    public static void sendOTP(String strUserId, Callback<ApiResponseModel> listener) {
        getApiClient().sendOTP(strUserId).enqueue(listener);
    }

    public static void resetPassword(String strUserId, String otp, String strPassword, Callback<ApiResponseModel> listener) {
        getApiClient().resetPassword(strUserId, otp, strPassword).enqueue(listener);
    }


    public static void getYearTypes( Callback<YearTypeResModel> listener) {
        getApiClient().getYearTypes().enqueue(listener);
    }

    public static void getYearBooks(String strUserId, String strTypeId, String type, String strBookIds, Callback<BookListResModel> listener) {
        getApiClient().getYearBooks(strUserId, strTypeId, type, strBookIds).enqueue(listener);
    }

    public static void getYearBookPdf(String strTypeId, String type, String strBookIds, Callback<ResponseBody> listener) {
        getApiClient().getYearBookPdf(strTypeId, type, strBookIds).enqueue(listener);
    }

    public static void createYearBookPdf(String strTypeId, String type, String strBookIds, Callback<CreatePdfFileUrlResModel> listener) {
        getApiClient().createYearBookPdf(strTypeId, type, strBookIds).enqueue(listener);
    }

    public static void getYear(String strUserId, String strYearType, String strBookIds, Callback<YearResponseModel> listener) {
        getApiClient().getYear(strUserId, strYearType, strBookIds).enqueue(listener);
    }

    public static void getBookIndexPdf(String strBookId, Callback<ResponseBody> listener) {
        getApiClient().getBookIndexPdf(strBookId).enqueue(listener);
    }

    public static void createBookIndexPdf(String strIndexBookId, Callback<CreatePdfFileUrlResModel> listener) {
        getApiClient().createBookIndexPdf(strIndexBookId).enqueue(listener);
    }


    public static void getYearCategory(String allBooks, String yearType, String strUserId, Callback<CategoryResModel> listener) {
        getApiClient().getYearCategory(allBooks, yearType, strUserId).enqueue(listener);
    }

    public static void getFeedbackTypes( Callback<FeedbackTypeResModel> listener) {
        getApiClient().getFeedbackTypes().enqueue(listener);
    }

    public static void getParamparaFilterData( Callback<ParamparaFilterDataResModel> listener) {
        getApiClient().getParamparaFilterData().enqueue(listener);
    }

    public static void getRelationTypes( Callback<RelationTypeListResModel> listener) {
        getApiClient().getRelationTypes().enqueue(listener);
    }


    public static void getSamvatsBaseOnSamvatTypeData(int samvatType, Callback<SamvatsBaseOnSamvatTypeDataResModel> listener) {
        getApiClient().getSamvatsBaseOnSamvatTypeData(samvatType).enqueue(listener);
    }

    public static void getSearchUnitList(Integer pageNo, Integer unitTypeId, Integer unitStatusId, Integer unitSectId, Integer bmSamvatTypeId, Integer bmSamvatId, Integer bmMonthId, Integer bmTithiId, String bmDate, Integer relationId, String search, String lensFilterTypeID, Callback<SearchUnitListResModel> listener) {
        getApiClient().getSearchUnitList(pageNo, unitTypeId, unitStatusId, unitSectId, bmSamvatTypeId, bmSamvatId, bmMonthId, bmTithiId, bmDate, relationId, search, lensFilterTypeID).enqueue(listener);
    }

    public static void getUnitDetails(String strUserId, String strUnitId, Callback<UnitDetailsResModel> listener) {
        getApiClient().getUnitDetails(strUserId, strUnitId).enqueue(listener);
    }

    public static void getUnitDetailPdfFile(String strUnitId, Callback<ResponseBody> listener) {
        getApiClient().getUnitDetailPdfFile(strUnitId).enqueue(listener);
    }

    public static void getBiodataDetailPdfFile(String strBidataId, Callback<ResponseBody> listener) {
        getApiClient().getBiodataDetailPdfFile(strBidataId).enqueue(listener);
    }

    public static void getUnitRelationDetailPdfFile(String strRelationId, Callback<ResponseBody> listener) {
        getApiClient().getUnitRelationDetailPdfFile(strRelationId).enqueue(listener);
    }

    public static void getUnitRelationChartData(String userId, String strUnitId, String relationId, int maxDepth, Callback<UnitRelationChartDataResModel> listener) {
        getApiClient().getUnitRelationChartData(userId, strUnitId, relationId, maxDepth).enqueue(listener);
    }


    public static void getBiodataMemoryDetails(String strUserId, String strBiodataId, Callback<BiodataMemoryDetailsResModel> listener) {
        getApiClient().getBiodataMemoryDetails(strUserId, strBiodataId).enqueue(listener);
    }

    public static void getRelationDetails(String strUserId, String strRelationId, Callback<RelationDetailsResModel> listener) {
        getApiClient().getRelationDetails(strUserId, strRelationId).enqueue(listener);
    }
    public static void addDataFeedback(String userId, String bookId, String page_no, String pdf_page_no, String type, String typeId, int feedbackType, String comments, Callback<AddDataFeedbackResModel> listener) {
        JSONObject data = new JSONObject();
        try {
            data.put("user_id", userId);
            data.put("book_id", bookId);
            data.put("page_no", page_no);
            data.put("pdf_page_no", pdf_page_no);
            data.put("type", type);
            data.put("type_id", typeId);
            data.put("feedback_type", feedbackType);
            data.put("comments", comments);

        } catch (Exception e) {
            Log.e("ApiClient--", "" + e.getMessage());
        }
        getApiClient().addDataFeedback(okhttp3.RequestBody.create(JSON, data.toString())).enqueue(listener);
    }
}





