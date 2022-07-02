package yanyu.xmz.recorder.mysql.protocol;

import com.github.shyiko.mysql.binlog.network.ClientCapabilities;

/**
 * @author xiaomingzhang
 * @date 2022/6/30
 * 客户端和服务器使用功能标志来指示它们支持和想要使用的功能。 解析mysql数据包需要根据是否支持来解析
 * @see com.github.shyiko.mysql.binlog.network.ClientCapabilities
 * https://dev.mysql.com/doc/internals/en/capability-flags.html
 */
public class CapabilityFlags {

    private  boolean supportsLongPassword;
    private  boolean supportsFoundRows;
    private  boolean supportsLongFlag;
    private  boolean supportsConnectWithDb;
    private  boolean supportsNoSchema;
    private  boolean supportsCompress;
    private  boolean supportsOdbc;
    private  boolean supportsLocalFiles;
    private  boolean supportsIgnoreSpace;
    private  boolean supportsProtocol41;
    private  boolean supportsInteractive;
    private  boolean supportsSsl;
    private  boolean supportsIgnoreSigpipe;
    private  boolean supportsTransactions;
    private  boolean supportsReserved;
    private  boolean supportsSecureConnection;
    private  boolean supportsMultiStatements;
    private  boolean supportsMultiResults;
    private  boolean supportsPsMultiResults;
    private  boolean supportsPluginAuth;
    private  boolean supportsSslVerifyServerCert;
    private  boolean supportsRememberOptions;
    private  boolean supportsSessionTrack;



    private  boolean isInit;

    private  final int CLIENT_SESSION_TRACK = 0x00800000;


    public CapabilityFlags(int serverCapabilities){
        init(serverCapabilities);
    }

    public void init(int serverCapabilities) {
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

    private  boolean isSupports(int capabilityFlag, int serverCapabilities) {
        return (capabilityFlag & serverCapabilities) != 0;
    }

    public  boolean isSupportsLongPassword() {
        return supportsLongPassword;
    }

    public  boolean isSupportsFoundRows() {
        return supportsFoundRows;
    }

    public  boolean isSupportsLongFlag() {
        return supportsLongFlag;
    }

    public  boolean isSupportsConnectWithDb() {
        return supportsConnectWithDb;
    }

    public  boolean isSupportsNoSchema() {
        return supportsNoSchema;
    }

    public  boolean isSupportsCompress() {
        return supportsCompress;
    }

    public  boolean isSupportsOdbc() {
        return supportsOdbc;
    }

    public  boolean isSupportsLocalFiles() {
        return supportsLocalFiles;
    }

    public  boolean isSupportsIgnoreSpace() {
        return supportsIgnoreSpace;
    }

    public  boolean isSupportsProtocol41() {
        return supportsProtocol41;
    }

    public  boolean isSupportsInteractive() {
        return supportsInteractive;
    }

    public  boolean isSupportsSsl() {
        return supportsSsl;
    }

    public  boolean isSupportsIgnoreSigpipe() {
        return supportsIgnoreSigpipe;
    }

    public  boolean isSupportsTransactions() {
        return supportsTransactions;
    }

    public  boolean isSupportsReserved() {
        return supportsReserved;
    }

    public  boolean isSupportsSecureConnection() {
        return supportsSecureConnection;
    }

    public  boolean isSupportsMultiStatements() {
        return supportsMultiStatements;
    }

    public  boolean isSupportsMultiResults() {
        return supportsMultiResults;
    }

    public  boolean isSupportsPsMultiResults() {
        return supportsPsMultiResults;
    }

    public  boolean isSupportsPluginAuth() {
        return supportsPluginAuth;
    }

    public  boolean isSupportsSslVerifyServerCert() {
        return supportsSslVerifyServerCert;
    }

    public  boolean isSupportsRememberOptions() {
        return supportsRememberOptions;
    }

    public  boolean isSupportsSessionTrack() {
        return supportsSessionTrack;
    }

    @Override
    public String toString() {
        return "CapabilityFlags{" +
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
                ", isInit=" + isInit +
                ", CLIENT_SESSION_TRACK=" + CLIENT_SESSION_TRACK +
                '}';
    }
}
