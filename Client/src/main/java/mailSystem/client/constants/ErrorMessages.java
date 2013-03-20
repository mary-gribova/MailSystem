package mailSystem.client.constants;

public interface ErrorMessages {
    static final String LOGIN_FAILED_MESSAGE = "Wrong password/login or you need to register";
    static final String DATABASE_ERROR_MESSAGE = "Database error, try again later";
    static final String DUPLICATE_FOLDER_NAME_MESSAGE = "Folder with such name is already exists";
    static final String REMOVE_SYSTEM_FOLDERS_MESSAGE = "Impossible to remove system folders (Send and Inbox)";
    static final String RENAME_SYSTEM_FOLDERS_MESSAGE = "Impossible to rename system folders (Send and Inbox)";
    static final String MOVE_IN_SYSTEM_FOLDERS_MESSAGE = "Impossible to move in system folders (Inbox and Send)";
    static final String NO_SUCH_RECIPIENT_MESSAGE = "No such recipient";
    static final String ERROR_PARSING_DATE_MESSAGE = "Error parsing date";
    static final String DUPLICATE_USER_MESSAGE = "User with such email is already registered";
    static final String NO_REGISTERED_USER_MESSAGE = "There is no user registered in system with such email";
    static final String MESSAGING_EXCEPTION = "Error during messaging";
    static final String UNSUPPORTED_ENCODING_OPERATION = "Unsupported encoding operation";
    static final String NULL_EMAIL = "Null email";

}
