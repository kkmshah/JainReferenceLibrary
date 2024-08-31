package com.jainelibrary.retrofit;

import androidx.annotation.NonNull;

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
import com.jainelibrary.retrofitResModel.BiodataMemoryDetailsResModel;
import com.jainelibrary.retrofitResModel.CheckMyShelfFileNameResModel;
import com.jainelibrary.retrofitResModel.CreatePdfFileUrlResModel;
import com.jainelibrary.retrofitResModel.RelationDetailsResModel;
import com.jainelibrary.retrofitResModel.RelationTypeListResModel;
import com.jainelibrary.retrofitResModel.SamvatsBaseOnSamvatTypeDataResModel;
import com.jainelibrary.retrofitResModel.UnitDetailsResModel;
import com.jainelibrary.model.SendOtpResModel;
import com.jainelibrary.model.UpdateMyShelfNotesModel;
import com.jainelibrary.model.UploadPDFModel;
import com.jainelibrary.model.UserDetailsResModel;
import com.jainelibrary.model.UserGuideResModel;
import com.jainelibrary.model.UserNameExistsResModel;
import com.jainelibrary.model.YearResModel;
import com.jainelibrary.model.YearResponseModel;
import com.jainelibrary.model.YearTypeResModel;
import com.jainelibrary.retrofitResModel.AddAllMyShelfResModel;
import com.jainelibrary.retrofitResModel.AppDataAndUsersResModel;
import com.jainelibrary.retrofitResModel.BookListResModel;
import com.jainelibrary.retrofitResModel.BookReferenceDetailsResModel;
import com.jainelibrary.retrofitResModel.CheckResModel;
import com.jainelibrary.retrofitResModel.ClearHoldReferenceModel;
import com.jainelibrary.retrofitResModel.CountResModel;
import com.jainelibrary.retrofitResModel.FilterBookResModel;
import com.jainelibrary.retrofitResModel.KeywordSearchModel;
import com.jainelibrary.retrofitResModel.ReferencePageDetailsResModel;
import com.jainelibrary.retrofitResModel.ReferenceResModel;
import com.jainelibrary.retrofitResModel.SaveFilterBooksResModel;
import com.jainelibrary.retrofitResModel.SearchOptionResModel;
import com.jainelibrary.retrofitResModel.SendEmailResModel;
import com.jainelibrary.retrofitResModel.ShareOrDownloadMyShelfResModel;
import com.jainelibrary.retrofitResModel.ShlokGranthSutraResModel;
import com.jainelibrary.retrofitResModel.ShlokSearchResModel;
import com.jainelibrary.retrofitResModel.UnitRelationChartDataResModel;
import com.jainelibrary.retrofitResModel.VersionResModel;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

public interface ApiInterface {

    @GET("jrl/get_version")
    public Call<VersionResModel> getVersion();

    @FormUrlEncoded
    @POST("jrl/user_register")
    public Call<ApiResponseModel> registerUser(@Field("name") String name,
                                               @Field("mobile") String mobile,
                                               @Field("email") String email);

    @FormUrlEncoded
    @POST("jrl/signup")
    public Call<ApiResponseModel> signup(@Field("name") String name,
                                         @Field("mobile") String mobile,
                                         @Field("email") String email,
                                         @Field("username") String username,
                                         @Field("password") String password);

    @FormUrlEncoded
    @POST("jrl/login")
    public Call<UserDetailsResModel> login(@Field("username") String username,
                                           @Field("password") String password);


    @FormUrlEncoded
    @POST("jrl/verify_otp")
    public Call<UserDetailsResModel> verifyOtp(@Field("mobile") String mobile,
                                               @Field("otp") String otp);

    @FormUrlEncoded
    @POST("jrl/send_otp")
    public Call<SendOtpResModel> sendOtp(@Field("mobile") String mobile);

    @NonNull
    @FormUrlEncoded
    @POST("jrl/get_improve_keywords")
    public Call<KeywordSearchModel> getKeyword(
            @Field("search_for") String search_for,
            @Field("page_no") String page_no,
            @Field("keyword") String strKeyword,
            @Field("uid") String struid,
            @Field("bids") String strBookId,
            @Field("lang_type") String lang_code,
            @Field("types") String types,
            @Field("book_info") String book_info);

    @GET("jrl/get_keywords_pdf")
    public Call<ResponseBody> getKeywordsPdf(
            @Query("page_no") String page_no,
            @Query("keyword") String strKeyword,
            @Query("uid") String struid,
            @Query("bids") String strBookId,
            @Query("lang_type") String lang_code,
            @Query("pdf_type") String pdf_type,
            @Query("types") String types);

    @FormUrlEncoded
    @POST("jrl/create_keywords_pdf")
    public Call<CreatePdfFileUrlResModel> createKeywordsPdf(
            @Field("keyword") String strKeyword,
            @Field("uid") String struid,
            @Field("bids") String strBookId,
            @Field("lang_type") String lang_code,
            @Field("pdf_type") String pdf_type);


    @GET("jrl/get_book_lists_pdf")
    public Call<ResponseBody> getKeywordBookDetailsPdf(@Query("kid") String strKid, @Query("bids") String strBids);


    @FormUrlEncoded
    @POST("jrl/create_book_lists_pdf")
    public Call<CreatePdfFileUrlResModel> createKeywordBookDetailsPdf(
            @Field("kid") String strKid,
            @Field("bids") String strBids);

    @GET("jrl/get_books_pdf")
    public Call<ResponseBody> getShlokGranthDetailsPdf(@Query("gsid") String strGSId);



    @FormUrlEncoded
    @POST("jrl/create_books_pdf")
    public Call<CreatePdfFileUrlResModel> createShlokGranthDetailsPdf(
            @Field("gsid") String gsid);

    @FormUrlEncoded
    @POST("jrl/get_granth")
    public Call<ShlokSearchResModel> getSearchShlok(@Field("uid") String struid,
                                                    @Field("keyword") String strKeyword,
                                                    @Field("flag") String strFlag);

    @FormUrlEncoded
    @POST("jrl/get_granth_sutra")
    public Call<ShlokGranthSutraResModel> getShlokGranthSutra(@Field("gid") String strShlokGranthId,
                                                              @Field("search") String strSearch,
                                                              @Field("page_no") String strPageNo);

    @FormUrlEncoded
    @POST("jrl/get_books")
    public Call<BookListResModel> getShlokBooks(@Field("uid") String strUid, @Field("gsid") String strGranthShlokId);

    @FormUrlEncoded
    @POST("jrl/get_book_lists")
    public Call<BookListResModel> getKeywordBookDetails(@Field("uid") String strUid, @Field("kid") String strKid, @Field("bids") String strBids);

    @FormUrlEncoded
    @POST("jrl/get_book_page")
    public Call<BooksDataModel> getBookPage(@Field("type") String strType,
                                            @Field("type_id") String strTypeId,
                                            @Field("bid") String strBid,
                                            @Field("pid") String strPid,
                                            @Field("direction") String strDirection,
                                            @Field("flag") String strFlag,
                                            @Field("highlight") String strHighlight);

    @FormUrlEncoded
    @POST("jrl/get_marking_book_page")
    public Call<MarkingBookModel> getMarkingBookPage(@Field("kid") String strKid,
                                                     @Field("bid") String strBid,
                                                     @Field("pid") String strPid);


    @FormUrlEncoded
    @POST("jrl/get_book_details")
    public Call<BookListResModel> getBookAppendixDetails(@Field("bid") String strBid,
                                                         @Field("flag") String strFlag);

    @FormUrlEncoded
    @POST("jrl/get_book_reference_details")
    public Call<BookReferenceDetailsResModel> getBookReferenceDetails(@Field("bid") String strBid,
                                                                      @Field("flag") String strFlag,
                                                                      @Field("page_no") String strPageNo,
                                                                      @Field("page_limit") String strPageLimit,
                                                                      @Field("reference") String strReference,
                                                                      @Field("reference_type") String strReferenceType);

    @FormUrlEncoded
    @POST("jrl/get_book_references")
    public Call<BookReferenceDetailsResModel> getBookReferences(@Field("bid") String strBid,
                                                                @Field("page_no") String strPageNo,
                                                                @Field("page_limit") String strPageLimit,
                                                                @Field("reference") String strReference,
                                                                @Field("reference_type") String strReferenceType);

    @FormUrlEncoded
    @POST("jrl/get_book_references")
    public Call<BookReferenceDetailsResModel> getBookReferencePages(@Field("bid") String strBid,
                                                                @Field("page_no") String strPageNo,
                                                                @Field("page_limit") String strPageLimit,
                                                                @Field("reference") String strReference,
                                                                @Field("reference_type") String strReferenceType,
                                                                @Field("page_diff") String strPageDiff);

    @FormUrlEncoded
    @POST("jrl/get_book_reference_page_details")
    public Call<ReferencePageDetailsResModel> getBookReferencePageDetails(@Field("type_id") String strTypeId,
                                                                          @Field("reference_id") String strReferenceId,
                                                                          @Field("reference") String strReference,
                                                                          @Field("reference_type") String strReferenceType);

    @FormUrlEncoded
    @POST("jrl/get_reference_types")
    public Call<ReferenceTypeResModel> getReferenceTypes(@Field("type") String strType);

    @FormUrlEncoded
    @POST("jrl/add_reference")
    public Call<ReferenceResModel> addReference(@Field("user_id") String strUserId,
                                                   @Field("book_id") String strBookId,
                                                   @Field("type") String strType,
                                                   @Field("type_id") String strTypeId,
                                                   @Field("type_value") String strTypeValue,
                                                   @Field("pdf_page_no") String strPdfPageNo,
                                                   @Field("page_no") String strPageNo);

    @FormUrlEncoded
    @POST("jrl/update_reference")
    public Call<ReferenceResModel> updateReference(@Field("user_id") String strUserId,
                                                       @Field("type_id") String strTypeId,
                                                       @Field("reference_id") String strReferenceId,
                                                       @Field("type_value") String strTypeValue,
                                                       @Field("pdf_page_no") String strPdfPageNo,
                                                       @Field("page_no") String strPageNo);

    @FormUrlEncoded
    @POST("jrl/update_reference_data")
    public Call<ReferenceResModel> updateReferenceData(@Field("type_id") String strTypeId,
                                                       @Field("reference_id") String strReferenceId,
                                                       @Field("x1") String x1, @Field("y1") String y1,
                                                       @Field("x2") String x2, @Field("y2") String y2);

    @FormUrlEncoded
    @POST("jrl/update_reference_status")
    public Call<ReferenceResModel> updateReferenceStatus(@Field("type_id") String strTypeId,
                                                       @Field("reference_id") String strReferenceId,
                                                       @Field("is_checked") int is_checked);

    @FormUrlEncoded
    @POST("jrl/delete_reference")
    public Call<ReferenceResModel> deleteReference(@Field("type_id") String strTypeId,
                                                         @Field("reference_id") String strReferenceId);

    @FormUrlEncoded
    @POST("jrl/get_my_shelf")
    public Call<MyShelfResModel> getMyShelfList(@Field("uid") String strBid,
                                                @Field("flag") String strFlag,
                                                @Field("type_ref") String type_ref);

    @FormUrlEncoded
    @POST("jrl/get_search_myshelf")
    public Call<MyShelfResModel> getSearchMyShelf(@Field("uid") String strUid,
                                                  @Field("keyword") String strKeyword,
                                                  @Field("flag") String strFlag,
                                                  @Field("type_ref") String type_ref);

    /*

        @POST("jrl/get_my_shelf")
        public Call<MyShelfResModel> searchMyShlef(@Field("uid") String strUID, @Field("keyword") String strKeyword,
                                                   @Field("type") String strType);
    */
    @POST("jrl/add_my_shelf")
    public Call<AddShelfResModel> addMyShelf(@Body okhttp3.RequestBody data);

    @FormUrlEncoded
    @POST("jrl/delete_my_shelf")
    public Call<DeleteMyShelfResModel> deleteMyShelf(@Field("id") String strId);

    @FormUrlEncoded
    @POST("jrl/get_all_category")
    public Call<CategoryResModel> getAllCategory(@Field("uid") String uid);

    @FormUrlEncoded
    @POST("jrl/get_category")
    public Call<CategoryResModel> getCategory(@Field("all_books") String allBooks, @Field("keyword") String kid, @Field("uid") String uid);

    @FormUrlEncoded
    @POST("jrl/add_search_keyword")
    public Call<HoldAndSearchResModel> addNewHoldSearchKeyword(@Field("user_id") String strUserId,
                                                               @Field("book_id") String strBookId,
                                                               @Field("type_id") String strTypeId,
                                                               @Field("type") String type,
                                                               @Field("page_no") String pageNo,
                                                               @Field("pdf_page_no") String pdf_page_no);

    /*@FormUrlEncoded
    @POST("jrl/add_search_keyword")
    public Call<HoldAndSearchResModel> addHoldSearchKeyword(@Field("user_id") String strUserId,
                                                               @Field("book_id") String strBookId,
                                                               @Field("keyword") String strKeyword,
                                                               @Field("pdf_page_no") String pdf_page_no,
                                                               @Field("author_name") String author_name,
                                                               @Field("translator_name") String translator_name,
                                                               @Field("editor_name") String editor_name);*/

    @FormUrlEncoded
    @POST("jrl/get_search_keyword")
    public Call<BookListResModel> getHolderSearchList(@Field("user_id") String strUserId,
                                                      @Field("flag") String flag);

    @FormUrlEncoded
    @POST("jrl/delete_search_keyword")
    public Call<HoldAndSearchResModel> deleteHolderSearchList(@Field("id") String strUserId);

    @FormUrlEncoded
    @POST("jrl/get_user_admin")
    public Call<UserDetailsResModel> getUserAdmin(@Field("user_id") String strUserId);

    @FormUrlEncoded
    @POST("jrl/get_search_wise_book")
    public Call<PdfStoreListResModel> getPdfList(@Field("user_id") String strUserId,
                                                 @Field("search") String strSearch,
                                                 @Field("category_id") String strCatId,
                                                 @Field("type") String strType,
                                                 @Field("page_no") String strPageNo);

    @FormUrlEncoded
    @POST("jrl/get_search_wise_book_paginate")
    public Call<PdfStoreListResModel> getPaginatewithPDF(@Field("search") String strSearch,
                                                         @Field("category_id") String strCatId,
                                                         @Field("type") String strType,
                                                         @Field("page") String strpage,
                                                         @Field("limit") String strLimit);

    @POST("jrl/update_my_shelf_notes")
    public Call<UpdateMyShelfNotesModel> updateNotes(@Body okhttp3.RequestBody data);


    @POST("jrl/delete_my_shelf_by_ids")
    public Call<DeleteMyShelfResModel> deleteMyReferenceImage(@Body okhttp3.RequestBody data);

    @FormUrlEncoded
    @POST("jrl/sendfeedback")
    public Call<SendEmailResModel> sendEmail(@Field("name") String strName,
                                             @Field("email") String strEmail,
                                             @Field("message") String strMessage);

    @GET("jrl/countbooks")
    public Call<CountResModel> countbooks();

    @GET("jrl/getAppDataAndUsers")
    public Call<AppDataAndUsersResModel> getAppDataAndUsers();

    @GET("jrl/countregister_user")
    public Call<CountResModel> countRegisterUser();

    @GET("jrl/count_search")
    public Call<CountResModel> countSearch();

    @GET("jrl/countkeywords")
    public Call<CountResModel> countKeywords();

    @GET("jrl/countpages")
    public Call<CountResModel> getTotalPages();

    @FormUrlEncoded
    @POST("jrl/move_search_keywords_to_my_shelf")
    public Call<AddAllMyShelfResModel> addAllMyShelf(@Field("user_id") String userId,@Field("hold_id") String holdId);

    @FormUrlEncoded
    @POST("jrl/getUserLogDetails")
    public Call<UserDetailsResModel> getuserdetails(@Field("uid") String userId);

    @FormUrlEncoded
    @POST("jrl/get_search_types")
    public Call<SearchOptionResModel> getFilterSearchOption(@Field("uid") String userId);

    @FormUrlEncoded
    @POST("jrl/save_search_type")
    public Call<SaveFilterBooksResModel> saveSearchTypes(@Field("uid") String userId, @Field("search_types") String search_types);

    @FormUrlEncoded
    @POST("jrl/get_user_guide")
    public Call<UserGuideResModel> getUserGuide(@Field("type") String type);

    @FormUrlEncoded
    @POST("jrl/save_filter_books")
    public Call<SaveFilterBooksResModel> saveFilterBooks(@Field("uid") String userId, @Field("bids") String bookIds);

    @FormUrlEncoded
    @POST("jrl/get_filter_book_list")
    public Call<FilterBookResModel> getFilterBookList(@Field("uid") String userId);

    @FormUrlEncoded
    @POST("jrl/check_username")
    public Call<CheckResModel> checkusername(@Field("username") String username);

    @POST("jrl/getyear")
    public Call<YearResModel> getyear();

    @FormUrlEncoded
    @POST("jrl/get_year_book")
    public Call<BookListResModel> getyearlist(@Field("yid") String strYearId);

    @FormUrlEncoded
    @POST("jrl/clear_search_keyword")
    public Call<ClearHoldReferenceModel> clearHoldReferenceApi(@Field("uid") String strUserId);

    @Multipart
    @POST("jrl/upload_keyword_pdf")
    public Call<UploadPDFModel> uploadFile(@Part MultipartBody.Part file,
                                           @Part("uid") RequestBody UserId,
                                           @Part("filename") RequestBody strtitle,
                                           @Part("type") RequestBody strtype,
                                           @Part("type_ref") RequestBody strtyperef
    );

    @Multipart
    @POST("jrl/add_my_shelf")
    public Call<AddShelfResModel> addMyShelfs(@Part("uid") RequestBody UserId,
                                              @Part("book_id") RequestBody bookID,
                                              @Part("type_id") RequestBody Kid,
                                              @Part("type") RequestBody strtype,
                                              @Part("type_ref") RequestBody strtyperef,
                                              @Part("file_name") RequestBody strtitle,
                                              @Part("type_name") RequestBody strTypeName,
                                              @Part("count") RequestBody strCount,
                                              @Part("file_type") RequestBody strFileType,
                                              @Part MultipartBody.Part file);

    @Multipart
    @POST("jrl/add_my_shelf")
    public Call<AddShelfResModel> addMyShelfsMultipleFiles(@Part("uid") RequestBody UserId,
                                                           @Part("book_id") RequestBody bookID,
                                                           @Part("type_id") RequestBody Kid,
                                                           @Part("type") RequestBody strtype,
                                                           @Part("type_ref") RequestBody strtyperef,
                                                           @Part("file_name") RequestBody strtitle,
                                                           @Part("type_name") RequestBody strTypeName,
                                                           @Part("count") RequestBody strCount,
                                                           @Part("file_type") RequestBody strFileType,
                                                           @Part List<MultipartBody.Part> files);



    @FormUrlEncoded
    @POST("jrl/add_my_shelf")
    public Call<AddShelfResModel> addMyShelfsWithUrl(@Field("uid") String UserId,
                                                     @Field("book_id") String bookID,
                                              @Field("type_id") String Kid,
                                              @Field("type") String strtype,
                                                     @Field("type_ref") String strtyperef,
                                              @Field("file_name") String strtitle,
                                              @Field("type_name") String strTypeName,
                                              @Field("count") String strCount,
                                              @Field("file_type") String strFileType,
                                              @Field("file_url") String strFileUrl);


    @FormUrlEncoded
    @POST("jrl/add_my_shelf")
    public Call<AddShelfResModel> addMyShelfWithImagesUrl(@Field("uid") String UserId,
                                                     @Field("book_id") String bookID,
                                                     @Field("type_id") String Kid,
                                                     @Field("type") String strtype,
                                                     @Field("type_ref") String strtyperef,
                                                     @Field("file_name") String strtitle,
                                                     @Field("type_name") String strTypeName,
                                                     @Field("count") String strCount,
                                                     @Field("file_type") String strFileType,
                                                     @Field("images_url") String jsonImagesUrl);


    @FormUrlEncoded
    @POST("jrl/check_my_shelf_file_name")
    public Call<CheckMyShelfFileNameResModel> checkMyShelfFileName(@Field("uid") String userId,
                                                                   @Field("file_name") String strFileName);

    /*@POST("jrl/api/Register")
    public Call<SignResModel> sign(@Body RequestBody data);

    @Multipart
    @POST("jrl/api/UpdatePost")
    public Call<AddPostResModel> updatepost(@Part MultipartBody.Part[] profile,
                                            @Part("user_id") RequestBody UserId,
                                            @Part("title") RequestBody strtitle,
                                            @Part("description") RequestBody description,
                                            @Part("disease_id") RequestBody disease_id,
                                            @Part("isanonymous") RequestBody isanonymous,
                                            @Part("post_id") RequestBody PostId);*/


    @FormUrlEncoded
    @POST("jrl/share_my_shelf")
    public Call<ShareOrDownloadMyShelfResModel> shareMyShelfs(@Field("uid") String userId, @Field("type_ref") String type_ref);

    @FormUrlEncoded
    @POST("jrl/download_my_shelf")
    public Call<ShareOrDownloadMyShelfResModel> downloadMyShelfs(@Field("uid") String userId, @Field("type_ref") String type_ref);

    @FormUrlEncoded
    @POST("jrl/check_username_exists")
    public Call<UserNameExistsResModel> checkUserNameExists(@Field("username") String username);

    @FormUrlEncoded
    @POST("jrl/send_otp_user")
    public Call<ApiResponseModel> sendOTP(@Field("user_id") String userId);

    @FormUrlEncoded
    @POST("jrl/reset_password")
    public Call<ApiResponseModel> resetPassword(@Field("user_id") String userId,
                                                      @Field("otp") String otp,
                                                      @Field("password") String password);

    @GET("jrl/get_year_types")
    public Call<YearTypeResModel> getYearTypes();

    @FormUrlEncoded
    @POST("jrl/get_year_books")
    public Call<BookListResModel> getYearBooks(@Field("uid") String userId,
                                               @Field("year_type") String yearType,
                                               @Field("year") String year,
                                               @Field("bids") String bookIds);

    @GET("jrl/get_year_books_pdf")
    public Call<ResponseBody> getYearBookPdf(@Query("year_type") String yearType,
                                                 @Query("year") String year,
                                             @Query("bids") String bookIds);

    @FormUrlEncoded
    @POST("jrl/create_year_books_pdf")
    public Call<CreatePdfFileUrlResModel> createYearBookPdf(
            @Field("year_type") String yearType,
            @Field("year") String year,
            @Field("bids") String strBookId);


    @FormUrlEncoded
    @POST("jrl/get_years")
    public Call<YearResponseModel> getYear(@Field("uid") String userId,
                                           @Field("year_type") String yearType,
                                           @Field("bids") String strBookIds);


    @GET("jrl/get_index_books")
    public Call<BookListResModel> getIndexBookList();

    @FormUrlEncoded
    @POST("jrl/get_index_book_ini")
    public Call<BookListResModel> getBookIndex(@Field("index_book_id") String strBookId);

    @GET("jrl/get_index_book_ini_pdf")
    public Call<ResponseBody> getBookIndexPdf(@Query("index_book_id") String bookId);

    @FormUrlEncoded
    @POST("jrl/create_index_book_ini_pdf")
    public Call<CreatePdfFileUrlResModel> createBookIndexPdf(
            @Field("index_book_id") String indexBookId);

    @FormUrlEncoded
    @POST("jrl/get_year_category")
    public Call<CategoryResModel> getYearCategory(@Field("all_books") String allBooks, @Field("year_type") String yType, @Field("uid") String uid);

    @GET("jrl/get_feedback_types")
    public Call<FeedbackTypeResModel> getFeedbackTypes();


    @GET("data/get_parampara_filter_data")
    public Call<ParamparaFilterDataResModel> getParamparaFilterData();


    @FormUrlEncoded

    @POST("data/get_samvat_list")
    public Call<SamvatsBaseOnSamvatTypeDataResModel> getSamvatsBaseOnSamvatTypeData(@Field("type") int samvatType);


    @FormUrlEncoded
    @POST("data/search_units")
    public Call<SearchUnitListResModel> getSearchUnitList(@Field("page_no") Integer pageNo,
                                                          @Field("unit_type_id") Integer unitTypeId,
                                                          @Field("unit_status_id") Integer unitStatusId,
                                                          @Field("unit_sect_id") Integer unitSectId,
                                                          @Field("bm_samvat_type") Integer bmSamvatTypeId,
                                                          @Field("bm_samvat_id") Integer bmSamvatId,
                                                          @Field("bm_month_id") Integer bmMonthId,
                                                          @Field("bm_tithi_id") Integer bmTithiId,
                                                          @Field("bm_date") String bmDate,
                                                          @Field("relation_id") Integer relationId,
                                                          @Field("search") String search,
                                                          @Field("lense_filter_type_ids") String lensFilterTypeID);


    @GET("data/view_unit_details")
    public Call<UnitDetailsResModel> getUnitDetails(@Query("uid") String userId,
                                               @Query("id") String strUnitId);


    @GET("data/unit_relation_chart_data")
    public Call<UnitRelationChartDataResModel> getUnitRelationChartData(@Query("uid") String userId, @Query("id") String strUnitId, @Query("relation_id") String strRelationId, @Query("max_depth") int maxDepth );
    @GET("data/view_bm_details")
    public Call<BiodataMemoryDetailsResModel> getBiodataMemoryDetails(@Query("uid") String userId,
                                                                      @Query("id") String strBiodataId);


    @GET("data/view_relation")
    public Call<RelationDetailsResModel> getRelationDetails(@Query("uid") String userId,
                                                            @Query("id") String strRelationId);

    @GET("data/get_relation_types")
    public Call<RelationTypeListResModel> getRelationTypes();



    @GET("data/export_unit_details")
    public Call<ResponseBody> getUnitDetailPdfFile(@Query("id") String unitId);

    @GET("data/export_bm_details")
    public Call<ResponseBody> getBiodataDetailPdfFile(@Query("id") String bmId);


    @GET("data/export_unit_relation_details")
    public Call<ResponseBody> getUnitRelationDetailPdfFile(@Query("id") String relationId);


    @POST("jrl/add_data_feedback")
    public Call<AddDataFeedbackResModel> addDataFeedback(@Body okhttp3.RequestBody data);

}