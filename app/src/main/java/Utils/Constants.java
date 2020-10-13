package Utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Eweb on 9/2/2017.
 */

public class Constants {
    public static int LoginAs;
   // public static String Base_URL = "https://phpstack-102119-849492.cloudwaysapps.com/API/";
     public static String Base_URL = "http://phpstack-102119-849492.cloudwaysapps.com/API/";
    //live base url
   // public static String Base_URL = "https://admin.theinfrazone.com/api/";

    public static String FCMID = "diveceid";
    public static String Shared_Pref = "share_pref";
    public static String accesstoken = "sjkagasmdbafadbadsbakdsasdfhskld";


    public static String getDateFromTimestamp(String timestamp, String format) {

        long time = Long.parseLong(timestamp) * 1000;
        try {
            DateFormat sdf = new SimpleDateFormat(format, Locale.getDefault());
            Date netDate = (new Date(time));
            return sdf.format(netDate);
        } catch (Exception ex) {
            return "xx xxxx xxxx";
        }
    }

    @SuppressLint("SimpleDateFormat")
    public static String getCurrentDate() {
        Date cDate = new Date();
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(cDate);
    }


    public static boolean timeComparison(String starTime, String endTime) {
        String pattern = "HH:mm";
        int flag;
        long diff = 0, diffSeconds = 0, diffMinutes = 0, diffHours = 0;
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        try {
            Date date1 = sdf.parse(starTime);
            Date date2 = sdf.parse(endTime);

            // Outputs -1 as date1 is before date2
            System.out.println(date1.compareTo(date2));
            flag = date1.compareTo(date2);
            if (flag == -1) {
                diff = date2.getTime() - date1.getTime();
                diffSeconds = diff / 1000;
                diffMinutes = diff / (60 * 1000);
                diffHours = diff / (60 * 60 * 1000);
            }

            if (flag == -1 && diffMinutes >= 60)
                return true;
            /*// Outputs 1 as date1 is after date1
            System.out.println(date2.compareTo(date1));

            date2 = sdf.parse(starTime);
            // Outputs 0 as the dates are now equal
            System.out.println(date1.compareTo(date2));*/

        } catch (ParseException e) {
            e.printStackTrace();
            return false;
            // Exception handling goes here
        }
        return false;
    }

    public static String timeAfterComparison(String starTime, String endTime) {
        String pattern = "HH:mm";
        String timeComparison = "";
        int flag;
        long diff = 0, diffSeconds = 0, diffMinutes = 0, diffHours = 0, diffDays = 0;
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        try {
            Date date1 = sdf.parse(starTime);
            Date date2 = sdf.parse(endTime);

            // Outputs -1 as date1 is before date2
            System.out.println(date1.compareTo(date2));
            flag = date1.compareTo(date2);
            if (flag == 1) {
                diff = date1.getTime() - date2.getTime();
                diffSeconds = diff / 1000;
                diffMinutes = diff / (60 * 1000);
                diffHours = diff / (60 * 60 * 1000);
                diffDays = diffHours / 24;
            }

            if (diffHours > 24) {
                timeComparison = diffDays + " days ago";
            } else if (diffHours >= 1 && diffHours <= 24) {
                timeComparison = diffHours + " hours ago";
            } else if (diffHours < 1 && diffMinutes >= 1) {
                timeComparison = diffMinutes + " mins ago";
            } else if (diffHours < 1 && diffMinutes < 1) {
                timeComparison = diffSeconds + " secs ago";
            }
            /*if (flag == 1 && diffMinutes > 0)
                return true;*/
            /*// Outputs 1 as date1 is after date1
            System.out.println(date2.compareTo(date1));

            date2 = sdf.parse(starTime);
            // Outputs 0 as the dates are now equal
            System.out.println(date1.compareTo(date2));*/

        } catch (ParseException e) {
            e.printStackTrace();
            return timeComparison;
            // Exception handling goes here
        }
        return timeComparison;
    }

    public static boolean dateBeforeEqualComparison(String starDate, String endDate) {
        String pattern = "MM-yyyy";
        int flag;
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        try {
            Date date1 = sdf.parse(starDate);
            Date date2 = sdf.parse(endDate);

            // Outputs -1 as date1 is before date2
            System.out.println(date1.compareTo(date2));
            flag = date1.compareTo(date2);
            if (flag == -1 || flag == 0)
                return true;
            /*// Outputs 1 as date1 is after date1
            System.out.println(date2.compareTo(date1));

            date2 = sdf.pa0rse(starTime);
            // Outputs 0 as the dates are now equal
            System.out.println(date1.compareTo(date2));*/

        } catch (ParseException e) {
            e.printStackTrace();
            return false;
            // Exception handling goes here
        }
        return false;
    }

    public static boolean dateEqualsComparison(String starDate, String endDate) {
        String pattern = "dd-MM-yyyy";
        int flag;
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        try {
            Date date1 = sdf.parse(starDate);
            Date date2 = sdf.parse(endDate);

            // Outputs -1 as date1 is before date2
            System.out.println(date1.compareTo(date2));
            flag = date1.compareTo(date2);
            if (flag == 0)
                return true;
            /*// Outputs 1 as date1 is after date1
            System.out.println(date2.compareTo(date1));

            date2 = sdf.pa0rse(starTime);
            // Outputs 0 as the dates are now equal
            System.out.println(date1.compareTo(date2));*/

        } catch (ParseException e) {
            e.printStackTrace();
            return false;
            // Exception handling goes here
        }
        return false;
    }

    public static String dateTimeComparison(String starDate, String endDate) {
        String pattern = "yyyy-MM-dd HH:mm:ss";
        String timeComparison = "";
        int flag;
        long diff = 0, diffSeconds = 0, diffMinutes = 0, diffHours = 0, diffDays = 0;
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        try {
            Date date1 = sdf.parse(starDate);
            Date date2 = sdf.parse(endDate);

            // Outputs -1 as date1 is before date2
            System.out.println(date1.compareTo(date2));
            /*// Outputs 1 as date1 is after date1
            System.out.println(date2.compareTo(date1));

            date2 = sdf.pa0rse(starTime);
            // Outputs 0 as the dates are now equal
            System.out.println(date1.compareTo(date2));*/
            flag = date1.compareTo(date2);
            if (flag == 1) {
                diff = date1.getTime() - date2.getTime();
                diffSeconds = diff / 1000;
                diffMinutes = diff / (60 * 1000);
                diffHours = diff / (60 * 60 * 1000);
                diffDays = diffHours / 24;
            }

            if (diffHours > 24) {
                timeComparison = diffDays + " days ago";
            } else if (diffHours >= 1 && diffHours <= 24) {
                timeComparison = diffHours + " hours ago";
            } else if (diffHours < 1 && diffMinutes >= 1) {
                timeComparison = diffMinutes + " mins ago";
            } else if (diffHours < 1 && diffMinutes < 1) {
                timeComparison = diffSeconds + " secs ago";
            }


        } catch (ParseException e) {
            e.printStackTrace();
            return timeComparison;
            // Exception handling goes here
        }
        return timeComparison;
    }

    public static boolean dateBeforeComparison(String starDate, String currentDate) {
        String pattern = "MM-yyyy";
        int flag;
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        try {
            Date date1 = sdf.parse(starDate);
            Date date2 = sdf.parse(currentDate);

            // Outputs -1 as date1 is before date2
            System.out.println(date1.compareTo(date2));
            flag = date1.compareTo(date2);
            if (flag == -1)
                return true;
            /*// Outputs 1 as date1 is after date1
            System.out.println(date2.compareTo(date1));

            date2 = sdf.pa0rse(starTime);
            // Outputs 0 as the dates are now equal
            System.out.println(date1.compareTo(date2));*/

        } catch (ParseException e) {
            e.printStackTrace();
            return false;
            // Exception handling goes here
        }
        return false;
    }

    public static boolean dateAfterComparison(String starDate, String endDate) {
        String pattern = "MM-yyyy";
        int flag;
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        try {
            Date date1 = sdf.parse(starDate);
            Date date2 = sdf.parse(endDate);

            // Outputs -1 as date1 is before date2
            System.out.println(date1.compareTo(date2));
            flag = date1.compareTo(date2);
            if (flag == 1)
                return true;
            /*// Outputs 1 as date1 is after date1
            System.out.println(date2.compareTo(date1));

            date2 = sdf.pa0rse(starTime);
            // Outputs 0 as the dates are now equal
            System.out.println(date1.compareTo(date2));*/

        } catch (ParseException e) {
            e.printStackTrace();
            return false;
            // Exception handling goes here
        }
        return false;
    }

    public static boolean yearsAfterComparison(String starYear, String endYear) {
        String pattern = "yyyy";
        int flag;
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        try {
            Date date1 = sdf.parse(starYear);
            Date date2 = sdf.parse(endYear);

            // Outputs -1 as date1 is before date2
            System.out.println(date1.compareTo(date2));
            flag = date1.compareTo(date2);
            if (flag == 1)
                return true;
            /*// Outputs 1 as date1 is after date1
            System.out.println(date2.compareTo(date1));

            date2 = sdf.pa0rse(starTime);
            // Outputs 0 as the dates are now equal
            System.out.println(date1.compareTo(date2));*/

        } catch (ParseException e) {
            e.printStackTrace();
            return false;
            // Exception handling goes here
        }
        return false;
    }

    /**
     * Description : Check if user is online or not
     *
     * @return true if online else false
     */
    public static boolean isInternetConnected(Activity mActivity) {
        ConnectivityManager cm = (ConnectivityManager) mActivity.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm != null ? cm.getActiveNetworkInfo() : null;
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

}
