package com.afaqy.avl.webnotifier;

import com.afaqy.avl.core.helper.AtomicBigDecimal;
import com.afaqy.avl.core.helper.AvlConstants;

public class WebNotifierConstants extends AvlConstants {
    public static final String HTTP = "HTTP";
    public final static String KAFKA = "KAFKA";
    public static final String SOLUTION_AIRPORT_TAXI = "airport_taxi";
    public static final String SOLUTION_AVL = "avl";
    public static final String WEB = "WEB";
    public static final String NOTIFICATION_CONSUMER = "NOTIFICATION_CONSUMER";
    public static final String CACHE = "CACHE";
    public static final String WEB_NOTIFICATION = "WEB_NOTIFICATION";
    public static final String EVENT = "EVENT";
    public static final String DELAYED = "DELAYED";
    //KafkaConfigConst
    public static final String KAFKA_POSITION_CONSUMER = "position";
    //need to add to counter AVL
    // cmd
    public static final AtomicBigDecimal TOTAL_CONSUMED_DEVICE_STATUS = new AtomicBigDecimal();
    //common
    public static final AtomicBigDecimal NOTIFICATION_TOTAL_CONSUMED = new AtomicBigDecimal();
    public static final AtomicBigDecimal POSITIONS_TOTAL_CONSUMED = new AtomicBigDecimal();
    // web configurations
    public class Web {
        public static final long ASYNC_TIMEOUT = 10 * 60 * 1000;
    }


    public class Support {
        public static final String SERVER_NAME = "server.name";
        public static final String SERVER_PUBLIC_IP = "server.publicIp";
    }

    public class Jetty {
        public static final String JETTY = "JETTY";
        public static final String PORT = "jetty.ports";
        public static final String SCAN_PACKAGE = "jetty.scan-package";
        public static final String MICRO_SERVICE_NAME = "jetty.micro-service-name";
        public static final String ENABLE_SSL = "jetty.enable-ssl";
        public static final String CERTIFICATE_PATH = "jetty.certificate.path";
        public static final String KEY_STORE_PASSWORD = "jetty.certificate.key-store-password";
        public static final String KEY_MANAGER_PASSWORD = "jetty.certificate.key-manager-password";
        public static final String SERVER_VERSION = "server.version";
        public static final String SERVER_SWAGGER_ENV = "server.swagger.environment";
        public static final String EVENT = "EVENT";
    }
    public class Notifier {
        public static final String ENABLE_WEB = "notifier.web.enable";
    }
    public class Redis {
      //  public static final String PORT = "database.redis.port";
        public static final String MODULE_KEY_PREFIX = "redis.modulePrefix";
    }


}
