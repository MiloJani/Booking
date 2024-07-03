package com.example.booking.constants;

public class Constants {
    // User related constants
    public static final String USER_NOT_FOUND = "User not found";
    public static final String INSUFFICIENT_PRIVILEGES = "User does not have sufficient privileges to add a business";
    public static final String INSUFFICIENT_PRIVILEGES_VIEW_ROOMS = "User does not have sufficient privileges to view available rooms";

    // Room related constants
    public static final String ROOM_NOT_FOUND = "Room not found";
    public static final String ROOM_ALREADY_EXISTS = "Room with the same name already exists under this business";
    public static final String ROOM_PRICING_NOT_FOUND = "Room pricing not found";

    // Business related constants
    public static final String BUSINESS_NOT_FOUND = "Business not found";

    // Role related constants
    public static final String ROLE_NOT_FOUND = "Role not found";

    // File related constants
    public static final String FILE_TOO_LARGE = "File size must be less than or equal to 100KB";
    public static final String FILE_SAVE_FAILED = "Failed to save image file";

    // Data related constants
    public static final String INVALID_DATA = "Invalid data";
    public static final String INVALID_DATE_FORMAT = "Invalid date format. Please provide dates in yyyy-MM-dd format.";
}

