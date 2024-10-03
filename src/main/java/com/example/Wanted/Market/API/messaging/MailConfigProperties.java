package com.example.Wanted.Market.API.messaging;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "mail")
public class MailConfigProperties {

    private MailServerConfig gmail;
    private MailServerConfig naver;

    // Getters and Setters

    public MailServerConfig getGmail() {
        return gmail;
    }

    public void setGmail(MailServerConfig gmail) {
        this.gmail = gmail;
    }

    public MailServerConfig getNaver() {
        return naver;
    }

    public void setNaver(MailServerConfig naver) {
        this.naver = naver;
    }

    public static class MailServerConfig {
        private String host;
        private int port;
        private String username;
        private String password;
        private MailProperties properties;

        // Getters and Setters

        public String getHost() {
            return host;
        }

        public void setHost(String host) {
            this.host = host;
        }

        public int getPort() {
            return port;
        }

        public void setPort(int port) {
            this.port = port;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public MailProperties getProperties() {
            return properties;
        }

        public void setProperties(MailProperties properties) {
            this.properties = properties;
        }
    }

    public static class MailProperties {
        private Smtp smtp;

        // Getters and Setters

        public Smtp getSmtp() {
            return smtp;
        }

        public void setSmtp(Smtp smtp) {
            this.smtp = smtp;
        }

        public static class Smtp {
            private boolean auth;
            private Starttls starttls;
            private boolean ssl;

            // Getters and Setters

            public boolean isAuth() {
                return auth;
            }

            public void setAuth(boolean auth) {
                this.auth = auth;
            }

            public Starttls getStarttls() {
                return starttls;
            }

            public void setStarttls(Starttls starttls) {
                this.starttls = starttls;
            }

            public boolean isSsl() {
                return ssl;
            }

            public void setSsl(boolean ssl) {
                this.ssl = ssl;
            }

            public static class Starttls {
                private boolean enable;

                // Getters and Setters

                public boolean isEnable() {
                    return enable;
                }

                public void setEnable(boolean enable) {
                    this.enable = enable;
                }
            }
        }
    }
}

