package Service;

import Model.ServerResponeLogin;
import Model.ServerResponeStatusUpdate;
import Model.ServerResponseAcceptRequest;
import Model.ServerResponseCarDetails;
import Model.ServerResponseCarRent;
import Model.ServerResponseDriverDetails;
import Model.ServerResponseGetCard;
import Model.ServerResponseHistory;
import Model.ServerResponseHistoryDetils;
import Model.ServerResponseNearby;
import Model.ServerResponseNoticount;
import Model.ServerResponseNotification;
import Model.ServerResponseOtp;
import Model.ServerResponseProfile;
import Model.ServerResponseRequestcar;
import Model.ServerResponseRequestlist;
import Model.ServerResponseTrackDriver;
import Model.ServerResponseUpdate;
import Model.ServerResponsehomelocation;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface Interface {

    @FormUrlEncoded
    @POST("customer.php/signup")
    Call<ServerResponeLogin> signup (@Field("firstname") String firstname, @Field("email") String email,
                                   @Field("contact") String contact, @Field("accesstoken") String accesstoken, @Field("terms_conditions") String terms_conditions, @Field("roletype") String roletype);
    @FormUrlEncoded
    @POST("customer.php/verifyOTP")
    Call<ServerResponseOtp> verifyOTP (@Field("otp") String otp, @Field("contact") String contact,@Field("device_type") String device_type,@Field("device_token") String device_token ,@Field("accesstoken") String accesstoken);

    @FormUrlEncoded
    @POST("vehicle.php/vehicleFilter")
    Call<ServerResponseCarRent> vehicleFilter (@Field("rentType") String rentType, @Field("vehicleType") String vehicleType, @Field("minPrice") String minPrice,
                                           @Field("maxPrice") String maxPrice,@Field("accesstoken") String accesstoken);


    @FormUrlEncoded
    @POST("customer.php/Login")
    Call<ServerResponeLogin> Login(@Field("contact") String contact,@Field("accesstoken") String accesstoken);

    @FormUrlEncoded
    @POST("vehicle.php/getVehicleList")
    Call<ServerResponseCarRent> getVehicleList(@Field("cid") String cid,@Field("accesstoken") String accesstoken);

    @FormUrlEncoded
    @POST("vehicle.php/searchVehicle")
    Call<ServerResponseCarRent> searchVehicle(@Field("searchtext") String searchtext,@Field("accesstoken") String accesstoken);

    @FormUrlEncoded
    @POST("vehicle.php/getSingleVehicle")
    Call<ServerResponseCarDetails> getSingleVehicle(@Field("vid") String vid,@Field("customerID")String customerID, @Field("accesstoken") String accesstoken);

    @FormUrlEncoded
    @POST("customer.php/getSingleRecord")
    Call<ServerResponseProfile> getcustomer_profile(@Field("id") String id, @Field("accesstoken") String accesstoken);

    @FormUrlEncoded
    @POST("customer.php/getAllRequests")
    Call<ServerResponseRequestlist> getAllRequests(@Field("ownerid") String ownerid, @Field("accesstoken") String accesstoken);

    @FormUrlEncoded
    @POST("customer.php/ownerVehicleList")
    Call<ServerResponseCarRent> ownerVehicleList(@Field("ownerID") String ownerID, @Field("accesstoken") String accesstoken);

    @FormUrlEncoded
    @POST("customer.php/requestAction")
    Call<ServerResponseRequestcar> requestAction(@Field("rid") String rid,@Field("actionID") String actionID,@Field("accesstoken") String accesstoken);

    @FormUrlEncoded
    @POST("customer.php/requestVehicle")
    Call<ServerResponseRequestcar> requestVehicle(@Field("vehicleID") String vehicleID, @Field("customerID") String customerID,@Field("from_location") String from_location,@Field("to_location") String to_location,
                                                  @Field("from_date") String from_date,@Field("end_date") String end_date,@Field("start_time") String start_time,@Field("end_time") String end_time,
                                                  @Field("accesstoken") String accesstoken);

    @Multipart
    @POST("customer.php/update")
    Call<ServerResponseProfile> update_profle(@Part("firstname") RequestBody firstname, @Part("email") RequestBody email, @Part("contact") RequestBody contact,
                                              @Part("lastname") RequestBody lastname, @Part("gender") RequestBody gender, @Part("address") RequestBody address,
                                              @Part("cid") RequestBody cid, @Part("uid") RequestBody uid, @Part("card_no") RequestBody card_no , @Part("paymentmode") RequestBody paymentmode ,
                                              @Part("accesstoken") RequestBody accesstoken, @Part MultipartBody.Part cust_img_name,@Part MultipartBody.Part driving_licence);

    @Multipart
    @POST("vehicle.php/newVehicle")
    Call<ServerResponseRequestcar> newVehicle(@Part("owner") RequestBody owner, @Part("VehicleType") RequestBody VehicleType,@Part MultipartBody.Part VehicleInsurance,
                                              @Part("ModelYear") RequestBody ModelYear, @Part("VehicleCategory") RequestBody VehicleCategory, @Part("RentType") RequestBody RentType,
                                              @Part("FuelType") RequestBody FuelType, @Part("VehiclePrice") RequestBody VehiclePrice, @Part("NumberOfSeats") RequestBody NumberOfSeats , @Part("vlocation") RequestBody VehicleLocation ,
                                              @Part("accesstoken") RequestBody accesstoken, @Part MultipartBody.Part VehicleImage,@Part("vehicleName") RequestBody vehicleName,@Part("vehicleNumber") RequestBody vehicleNumber);
    @FormUrlEncoded
    @POST("vehicle.php/singleVehicle")
    Call<ServerResponseCarDetails> owner_singleVehicle(@Field("vid") String vid,@Field("accesstoken") String accesstoken);

    @Multipart
    @POST("vehicle.php/updateVehicle")
    Call<ServerResponseRequestcar> updateVehicle(@Part("vid") RequestBody vid, @Part("VehicleType") RequestBody VehicleType,@Part MultipartBody.Part VehicleInsurance,
                                              @Part("ModelYear") RequestBody ModelYear, @Part("VehicleCategory") RequestBody VehicleCategory, @Part("RentType") RequestBody RentType,
                                              @Part("FuelType") RequestBody FuelType, @Part("VehiclePrice") RequestBody VehiclePrice, @Part("NumberOfSeats") RequestBody NumberOfSeats , @Part("vlocation") RequestBody VehicleLocation ,
                                              @Part("accesstoken") RequestBody accesstoken, @Part MultipartBody.Part VehicleImage,@Part("vehicleName") RequestBody vehicleName,@Part("vehicleNumber") RequestBody vehicleNumber);
    @FormUrlEncoded
    @POST("vehicle.php/deleteVehicle")
    Call<ServerResponseRequestcar> deleteVehicle(@Field("vid") String vid,@Field("accesstoken") String accesstoken);

    @FormUrlEncoded
    @POST("customer.php/getAcceptedRequests")
    Call<ServerResponseAcceptRequest> getAcceptedRequests(@Field("ownerid") String ownerid, @Field("accesstoken") String accesstoken);

    @FormUrlEncoded
    @POST("customer.php/updateCustomerLocation")
    Call<ServerResponsehomelocation> updateCustomerLocation(@Field("customerid") String customerid,@Field("longitude") String longitude,@Field("latitude") String latitude, @Field("accesstoken") String accesstoken);

    @FormUrlEncoded
    @POST("vehicle.php/getCategories")
    Call<ServerResponseNearby> getNearByVehicles(@Field("accesstoken") String accesstoken,@Field("src_long") String src_long,@Field("src_lati") String src_lati,@Field("dest_long") String dest_long,@Field("dest_lati") String dest_lati);

    @FormUrlEncoded
    @POST("customer.php/reservationRequest")
    Call<ServerResponseUpdate> reservationRequest(@Field("customerID") String customerID,@Field("categoryID") String categoryID,@Field("from_location") String from_location,@Field("to_location") String to_location,
                                                  @Field("src_long") String src_long,@Field("src_lati") String src_lati,
                                                  @Field("dest_long") String dest_long, @Field("dest_lati") String dest_lati, @Field("accesstoken") String accesstoken,@Field("paymentMode") String paymentMode);

    @FormUrlEncoded
    @POST("customer.php/logout")
    Call<ServerResponseUpdate> logout(@Field("customerID") String customerID,@Field("accesstoken") String accesstoken);

    @FormUrlEncoded
    @POST("customer.php/reservationDetail")
    Call<ServerResponseDriverDetails> reservationDetail(@Field("requestID") String requestID, @Field("accesstoken") String accesstoken);

    @FormUrlEncoded
    @POST("customer.php/trackDriverLocation")
    Call<ServerResponseTrackDriver> trackDriverLocation(@Field("requestID") String requestID, @Field("accesstoken") String accesstoken);

    @FormUrlEncoded
    @POST("customer.php/history")
    Call<ServerResponseHistory> history(@Field("cid") String cid, @Field("reservationType") String  reservationType , @Field("accesstoken") String accesstoken);

    @FormUrlEncoded
    @POST("customer.php/ownerRentalHistory")
    Call<ServerResponseHistory> ownerRentalHistory(@Field("ownerID") String ownerID, @Field("reservationType") String  reservationType , @Field("accesstoken") String accesstoken);

    @FormUrlEncoded
    @POST("customer.php/tripDetail")
    Call<ServerResponseHistoryDetils> tripDetail(@Field("rid") String rid, @Field("accesstoken") String accesstoken);

    @FormUrlEncoded
    @POST("customer.php/addDriverRating")
    Call<ServerResponseRequestcar> addDriverRating(@Field("reservationID") String reservationID,@Field("customerID") String customerID,@Field("ratingStars") String ratingStars,
                                                      @Field("comments") String comments ,@Field("accesstoken") String accesstoken);

    @FormUrlEncoded
    @POST("customer.php/getNotifications")
    Call<ServerResponseNotification> getNotifications(@Field("cid") String cid, @Field("accesstoken") String accesstoken);

    @FormUrlEncoded
    @POST("customer.php/cancelReservation")
    Call<ServerResponseRequestcar> cancelReservation(@Field("reservationID") String reservationID, @Field("accesstoken") String accesstoken);

    @FormUrlEncoded
    @POST("customer.php/saveCard")
    Call<ServerResponseRequestcar> saveCard(@Field("customerID") String customerID,@Field("card_no") String card_no,@Field("expiryDate") String expiryDate,@Field("cvv") String cvv,
                                            @Field("striptoken") String striptoken,@Field("brandname") String brandname,@Field("accesstoken") String accesstoken);
    @FormUrlEncoded
    @POST("customer.php/getCards")
    Call<ServerResponseGetCard> getCards(@Field("customerID") String customerID, @Field("accesstoken") String accesstoken);

    @FormUrlEncoded
    @POST("customer.php/notificationCount")
    Call<ServerResponseNoticount> notificationCount(@Field("customerID") String customerID, @Field("accesstoken") String accesstoken);

    @FormUrlEncoded
    @POST("customer.php/notificationRead")
    Call<ServerResponseRequestcar> notificationRead(@Field("customerID") String customerID,@Field("accesstoken") String accesstoken);

    @FormUrlEncoded
    @POST("customer.php/requestStatus")
    Call<ServerResponeStatusUpdate> requestStaus(@Field("requestID") String requestID,@Field("customerID") String customerID ,@Field("accesstoken") String accesstoken);
}

