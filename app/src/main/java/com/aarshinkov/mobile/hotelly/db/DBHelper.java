package com.aarshinkov.mobile.hotelly.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import com.aarshinkov.mobile.hotelly.responses.hotels.HotelGetResponse;

import java.util.ArrayList;
import java.util.List;

public class DBHelper extends SQLiteOpenHelper {

    public static final String DB_NAME = "temp.db";
    public static final int DB_VERSION = 1;
    public static final String MY_ERROR = "MyError";

    public static final String TABLE_HOTELS = "hotels";

    public static final String CREATE_TABLE_HOTELS = "CREATE TABLE " + TABLE_HOTELS +
            "('hotel_id' varchar(100) PRIMARY KEY, " +
            "'name' varchar(200) NOT NULL, " +
            "'description' varchar(2000) NOT NULL, " +
            "'country_code' varchar(5) NOT NULL, " +
            "'city' varchar(400) NOT NULL, " +
            "'street' varchar(300) NOT NULL, " +
            "'number' integer NOT NULL, " +
            "'stars' integer NOT NULL DEFAULT 0," +
            "'main_image' varchar(1000) NOT NULL)";

    public DBHelper(@Nullable Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_HOTELS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_HOTELS);
        onCreate(db);
    }

    public List<HotelGetResponse> getHotels() {

        List<HotelGetResponse> hotels = new ArrayList<>();
        Cursor cursor = null;

        try (SQLiteDatabase db = getReadableDatabase()) {

            String sql = "SELECT hotel_id, name, description, country_code, " +
                    "city, street, number, stars, main_image FROM hotels";

            cursor = db.rawQuery(sql, null);

            while (cursor.moveToNext()) {
                HotelGetResponse hotel = new HotelGetResponse();
                hotel.setHotelId(cursor.getString(0));
                hotel.setName(cursor.getString(1));
                hotel.setDescription(cursor.getString(2));
                hotel.setCountryCode(cursor.getString(3));
                hotel.setCity(cursor.getString(4));
                hotel.setStreet(cursor.getString(5));
                hotel.setNumber(cursor.getInt(6));
                hotel.setStars(cursor.getInt(7));
                hotel.setMainImage(cursor.getString(8));

                hotels.add(hotel);
            }

            return hotels;

        } catch (Exception e) {

        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        return hotels;
    }

    public HotelGetResponse getHotel(String hotelId) {

        Cursor cursor = null;

        try (SQLiteDatabase db = getReadableDatabase()) {

            String sql = "SELECT hotel_id, name, description, country_code, " +
                    "city, street, number, stars, main_image FROM hotels WHERE hotel_id = ?";

            cursor = db.rawQuery(sql, new String[]{hotelId});

            if (cursor.moveToNext()) {
                HotelGetResponse hotel = new HotelGetResponse();
                hotel.setHotelId(cursor.getString(0));
                hotel.setName(cursor.getString(1));
                hotel.setDescription(cursor.getString(2));
                hotel.setCountryCode(cursor.getString(3));
                hotel.setCity(cursor.getString(4));
                hotel.setStreet(cursor.getString(5));
                hotel.setNumber(cursor.getInt(6));
                hotel.setStars(cursor.getInt(7));
                hotel.setMainImage(cursor.getString(8));

                return hotel;
            }

        } catch (Exception e) {

        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        return null;
    }

    public boolean insertHotel(HotelGetResponse hotel) {

        try (SQLiteDatabase db = getWritableDatabase()) {

            ContentValues cv = new ContentValues();
            cv.put("hotel_id", hotel.getHotelId());
            cv.put("name", hotel.getName());
            cv.put("description", hotel.getDescription());
            cv.put("country_code", hotel.getCountryCode());
            cv.put("city", hotel.getCity());
            cv.put("street", hotel.getStreet());
            cv.put("number", hotel.getNumber());
            cv.put("stars", hotel.getStars());
            cv.put("main_image", hotel.getMainImage());

            long result = db.insertOrThrow(TABLE_HOTELS, null, cv);

            if (result != -1) {
                return true;
            }

        } catch (SQLException e) {
            Log.wtf("DB_ERROR", e.getMessage());
        }

        return false;
    }

    public void updateHotel(HotelGetResponse hotel) {

        try (SQLiteDatabase db = getWritableDatabase()) {

            ContentValues cv = new ContentValues();
            cv.put("name", hotel.getName());
            cv.put("description", hotel.getDescription());
            cv.put("country_code", hotel.getCountryCode());
            cv.put("city", hotel.getCity());
            cv.put("street", hotel.getStreet());
            cv.put("number", hotel.getNumber());
            cv.put("stars", hotel.getStars());
            cv.put("main_image", hotel.getMainImage());

            int result = db.update(TABLE_HOTELS, cv, "hotel_id=?", new String[]{hotel.getHotelId()});

//            if (result <= 0) {
//                return true;
//            }

        } catch (SQLException e) {
            Log.wtf("DB_ERROR", e.getMessage());
        }

//        return false;
    }

    public void deleteHotel(String hotelId) {

        try (SQLiteDatabase db = getWritableDatabase()) {

            String where = "hotel_id=?";
            String[] whereArgs = {hotelId};

            db.delete(TABLE_HOTELS, where, whereArgs);

        } catch (SQLException e) {
            Log.wtf(MY_ERROR, e.getMessage());
        }
    }
}
