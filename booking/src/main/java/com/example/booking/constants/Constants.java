package com.example.booking.constants;

public class Constants {

    //General data
    public static final String USER = "USER";
    public static final String ADMIN = "ADMIN";
    public static final String ROOM = "Rooms";
    public static final String BUSINESS = "Businesses";
    public static final int DISCOUNT_THRESHOLD = 10;
    public static final int DISCOUNT_MULTIPLIER = 2;

    // User related constants
    public static final String USER_NOT_FOUND = "User not found";
    public static final String INSUFFICIENT_PRIVILEGES = "User does not have sufficient privileges";
    public static final String UNABLE_TO_LOGIN = "User could not login";
    public static final String LOGOUT = "LOGOUT SUCCESSFUL";

    // Room related constants
    public static final String ROOM_NOT_FOUND = "Room not found";
    public static final String ROOM_ALREADY_EXISTS = "Room with the same name already exists under this business";
    public static final String ROOM_PRICING_NOT_FOUND = "Room pricing not found";

    // Business related constants
    public static final String BUSINESS_NOT_FOUND = "Business not found";
    public static final String BUSINESS_ALREADY_EXISTS = "Business already exists";

    //Booking related
    public static final String BOOKING_NOT_FOUND = "Booking not found";
    public static final String BOOKING_ALREADY_EXISTS = "Booking already exists";

    // File related constants
    public static final String FILE_TOO_LARGE = "File size must be less than or equal to 100KB";
    public static final String FILE_SAVE_FAILED = "Failed to save image file";
    public static final String NO_IMAGE_PROVIDED = "No image provided";


    // Data related constants
    public static final String INVALID_DATA = "Invalid data";
    public static final String INVALID_DATE_FORMAT = "Invalid date format. Please provide dates in yyyy-MM-dd format.";

    //Incorrect data
    public static final String DIFFERENT_USER_CARD_NAME = "Card holder name is different from user name";
    public static final String CHECKED_IN_OUT_DATES_NOT_FILLED = "Check in date and check out date should both be filled";
    public static final String INCORRECT_CHECKED_IN_OUT_DATES = "Check in date and check out dates are either before " +
            "today's date or check out is after check in ";
    public static final String MAXIMUM_CAPACITY = "Maximum capacity exceeded";
}

