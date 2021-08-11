package by.valvik.musicadvisor.domain.auth;

public class Token {

   private String accessToken;

   private String tokenType;

   private Integer expiresIn;

   private String refreshToken;

   public Token() {

   }

   public String getAccessToken() {

      return accessToken;
   }

   public String getTokenType() {

      return tokenType;

   }

   public Integer getExpiresIn() {

      return expiresIn;

   }

   public String getRefreshToken() {

      return refreshToken;

   }

}
