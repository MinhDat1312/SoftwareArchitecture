package iuh.fit.pattern01;

public class SystemConfig {

    private static SystemConfig instance;

    private String systemName;
    private String platform;

    private SystemConfig() {
        systemName = "Course Management System";
        platform = "Mobile";
    }

    public static synchronized SystemConfig getInstance() {
        if (instance == null) {
            instance = new SystemConfig();
        }
        return instance;
    }

    public String getSystemName() {
        return systemName;
    }

    public String getPlatform() {
        return platform;
    }
}

