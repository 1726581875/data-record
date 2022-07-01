package yanyu.xmz.recorder.protocol;

import com.github.shyiko.mysql.binlog.network.ClientCapabilities;

/**
 * @author xiaomingzhang
 * @date 2022/6/30
 * 客户端和服务器使用功能标志来指示它们支持和想要使用的功能。 解析mysql数据包需要根据是否支持来解析
 * @see com.github.shyiko.mysql.binlog.network.ClientCapabilities
 * https://dev.mysql.com/doc/internals/en/capability-flags.html
 */
public class CapabilityFlags {

    private static boolean supportsLongPassword;
    private static boolean supportsFoundRows;
    private static boolean supportsLongFlag;
    private static boolean supportsConnectWithDb;
    private static boolean supportsNoSchema;
    private static boolean supportsCompress;
    private static boolean supportsOdbc;
    private static boolean supportsLocalFiles;
    private static boolean supportsIgnoreSpace;
    private static boolean supportsProtocol41;
    private static boolean supportsInteractive;
    private static boolean supportsSsl;
    private static boolean supportsIgnoreSigpipe;
    private static boolean supportsTransactions;
    private static boolean supportsReserved;
    private static boolean supportsSecureConnection;
    private static boolean supportsMultiStatements;
    private static boolean supportsMultiResults;
    private static boolean supportsPsMultiResults;
    private static boolean supportsPluginAuth;
    private static boolean supportsSslVerifyServerCert;
    private static boolean supportsRememberOptions;
    private static boolean supportsSessionTrack;



    private static boolean isInit;

    private static final int CLIENT_SESSION_TRACK = 0x00800000;


    public static synchronized void init(int serverCapabilities) {
        supportsLongPassword = isSupports(ClientCapabilities.LONG_PASSWORD, serverCapabilities);
        supportsFoundRows = isSupports(ClientCapabilities.FOUND_ROWS, serverCapabilities);
        supportsLongFlag = isSupports(ClientCapabilities.LONG_FLAG, serverCapabilities);
        supportsConnectWithDb = isSupports(ClientCapabilities.LONG_PASSWORD, serverCapabilities);
        supportsNoSchema = isSupports(ClientCapabilities.NO_SCHEMA, serverCapabilities);
        supportsCompress = isSupports(ClientCapabilities.COMPRESS, serverCapabilities);
        supportsOdbc = isSupports(ClientCapabilities.ODBC, serverCapabilities);
        supportsLocalFiles = isSupports(ClientCapabilities.LOCAL_FILES, serverCapabilities);
        supportsIgnoreSpace = isSupports(ClientCapabilities.IGNORE_SPACE, serverCapabilities);
        supportsProtocol41 = isSupports(ClientCapabilities.PROTOCOL_41, serverCapabilities);
        supportsInteractive = isSupports(ClientCapabilities.INTERACTIVE, serverCapabilities);
        supportsSsl = isSupports(ClientCapabilities.SSL, serverCapabilities);
        supportsIgnoreSigpipe = isSupports(ClientCapabilities.IGNORE_SIGPIPE, serverCapabilities);
        supportsTransactions = isSupports(ClientCapabilities.TRANSACTIONS, serverCapabilities);
        supportsReserved = isSupports(ClientCapabilities.RESERVED, serverCapabilities);
        supportsSecureConnection = isSupports(ClientCapabilities.SECURE_CONNECTION, serverCapabilities);
        supportsMultiStatements = isSupports(ClientCapabilities.MULTI_STATEMENTS, serverCapabilities);
        supportsMultiResults = isSupports(ClientCapabilities.MULTI_RESULTS, serverCapabilities);
        supportsPsMultiResults = isSupports(ClientCapabilities.PS_MULTI_RESULTS, serverCapabilities);
        supportsPluginAuth = isSupports(ClientCapabilities.PLUGIN_AUTH, serverCapabilities);
        supportsSslVerifyServerCert = isSupports(ClientCapabilities.SSL_VERIFY_SERVER_CERT, serverCapabilities);
        supportsRememberOptions = isSupports(ClientCapabilities.REMEMBER_OPTIONS, serverCapabilities);
        supportsSessionTrack = isSupports(CLIENT_SESSION_TRACK, serverCapabilities);
        isInit = true;
    }

    private static boolean isSupports(int capabilityFlag, int serverCapabilities) {
        return (capabilityFlag & serverCapabilities) != 0;
    }

    public static boolean isSupportsLongPassword() {
        return supportsLongPassword;
    }

    public static boolean isSupportsFoundRows() {
        return supportsFoundRows;
    }

    public static boolean isSupportsLongFlag() {
        return supportsLongFlag;
    }

    public static boolean isSupportsConnectWithDb() {
        return supportsConnectWithDb;
    }

    public static boolean isSupportsNoSchema() {
        return supportsNoSchema;
    }

    public static boolean isSupportsCompress() {
        return supportsCompress;
    }

    public static boolean isSupportsOdbc() {
        return supportsOdbc;
    }

    public static boolean isSupportsLocalFiles() {
        return supportsLocalFiles;
    }

    public static boolean isSupportsIgnoreSpace() {
        return supportsIgnoreSpace;
    }

    public static boolean isSupportsProtocol41() {
        return supportsProtocol41;
    }

    public static boolean isSupportsInteractive() {
        return supportsInteractive;
    }

    public static boolean isSupportsSsl() {
        return supportsSsl;
    }

    public static boolean isSupportsIgnoreSigpipe() {
        return supportsIgnoreSigpipe;
    }

    public static boolean isSupportsTransactions() {
        return supportsTransactions;
    }

    public static boolean isSupportsReserved() {
        return supportsReserved;
    }

    public static boolean isSupportsSecureConnection() {
        return supportsSecureConnection;
    }

    public static boolean isSupportsMultiStatements() {
        return supportsMultiStatements;
    }

    public static boolean isSupportsMultiResults() {
        return supportsMultiResults;
    }

    public static boolean isSupportsPsMultiResults() {
        return supportsPsMultiResults;
    }

    public static boolean isSupportsPluginAuth() {
        return supportsPluginAuth;
    }

    public static boolean isSupportsSslVerifyServerCert() {
        return supportsSslVerifyServerCert;
    }

    public static boolean isSupportsRememberOptions() {
        return supportsRememberOptions;
    }

    public static boolean isSupportsSessionTrack() {
        return supportsSessionTrack;
    }


    public static String supportsToString() {
        return "UUIDTest{" +
                "supportsLongPassword=" + supportsLongPassword +
                ", supportsFoundRows=" + supportsFoundRows +
                ", supportsLongFlag=" + supportsLongFlag +
                ", supportsConnectWithDb=" + supportsConnectWithDb +
                ", supportsNoSchema=" + supportsNoSchema +
                ", supportsCompress=" + supportsCompress +
                ", supportsOdbc=" + supportsOdbc +
                ", supportsLocalFiles=" + supportsLocalFiles +
                ", supportsIgnoreSpace=" + supportsIgnoreSpace +
                ", supportsProtocol41=" + supportsProtocol41 +
                ", supportsInteractive=" + supportsInteractive +
                ", supportsSsl=" + supportsSsl +
                ", supportsIgnoreSigpipe=" + supportsIgnoreSigpipe +
                ", supportsTransactions=" + supportsTransactions +
                ", supportsReserved=" + supportsReserved +
                ", supportsSecureConnection=" + supportsSecureConnection +
                ", supportsMultiStatements=" + supportsMultiStatements +
                ", supportsMultiResults=" + supportsMultiResults +
                ", supportsPsMultiResults=" + supportsPsMultiResults +
                ", supportsPluginAuth=" + supportsPluginAuth +
                ", supportsSslVerifyServerCert=" + supportsSslVerifyServerCert +
                ", supportsRememberOptions=" + supportsRememberOptions +
                ", supportsSessionTrack=" + supportsSessionTrack +
                '}';
    }
}
