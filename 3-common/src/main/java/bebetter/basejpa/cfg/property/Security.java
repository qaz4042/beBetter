package bebetter.basejpa.cfg.property;

import lombok.Data;

import java.util.*;

public class Security {
    private final static String[] urls = {"/login", "/user2/logout", "/com/**", "/images/**", "/img/**"};

    List<String> defaultsPublicUrl = Arrays.asList(urls);

    private List<String> publicUri = new ArrayList<>();

    public Set<String> getAllPublicUrl() {
        Set<String> allPublicList = new HashSet<>(defaultsPublicUrl);
        allPublicList.addAll(publicUri);
        if (urlConfig.containsKey("permitAll")) {
            List<String> permitAll = urlConfig.get("permitAll");
            allPublicList.addAll(permitAll);
        }
        return allPublicList;
    }


    Map<String, List<String>> urlConfig = new HashMap<>();

    private final RememberMe rememberMe = new RememberMe();

    private final ClientAuthorization clientAuthorization = new ClientAuthorization();

    private final Authentication authentication = new Authentication();

    @Data
    public static class ClientAuthorization {

        private String accessTokenUri;

        private String tokenServiceId;

        private String clientId;

        private String clientSecret;
    }

    @Data
    public static class Authentication {

        private final Oauth oauth = new Oauth();

        private final Jwt jwt = new Jwt();

        @Data
        public static class Oauth {

            private String clientId;

            private String clientSecret;

            private int tokenValidityInSeconds = 86400;
        }

        @Data
        static class Jwt {

            private String secret;

            private long tokenValidityInSeconds = 86400;

            private long tokenValidityInSecondsForRememberMe = 2592000;
        }
    }
    @Data
    public static class RememberMe {
        private String key;
    }
}
