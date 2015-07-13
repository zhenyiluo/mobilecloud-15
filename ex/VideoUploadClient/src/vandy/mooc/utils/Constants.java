package vandy.mooc.utils;

/**
 * Class that contains all the Constants required in our Video Upload
 * client App.
 */
public class Constants {
    /**
     * URL of the VideoWebService.  Please Read the Instructions in
     * README.md to set up the SERVER_URL.
     */
    public static final String SERVER_URL =
        "http://10.0.2.2:8080";
    
    /**
     * Define a constant for 1 MB.
     */
    public static final long MEGA_BYTE = 1024 * 1024;

    /**
     * Maximum size of Video to be uploaded in MB.
     */
    public static final long MAX_SIZE_MEGA_BYTE = 50 * MEGA_BYTE;
    
    public static final String DATA_URL = "dataUrl";
    
    public static final String FILE_EXISTS = "fileExists";
    
    public static final String VIDEO_ID = "videoId";
    
    public static final String PREFIX = "prefix";
    
    public static final String SUFFIX = "suffix";
    
    public static final String BUNDLE = "bundle";
}
