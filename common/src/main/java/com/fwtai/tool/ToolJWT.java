package com.fwtai.tool;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.JwtParserBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.io.Serializable;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.Date;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.function.Function;

/**
 * jwt(JSON Web Token)令牌工具类,采用非对称公钥密钥
 * @作者 田应平
 * @版本 v1.0
 * @创建时间 2020-02-12 23:53
 * @QQ号码 444141300
 * @Email service@yinlz.com
 * @官网 <url>http://www.yinlz.com</url>
*/
public final class ToolJWT implements Serializable{

    //如设置Token过期时间15分钟，建议更换时间设置为Token前5分钟,通过try catch 获取过期
    private final static long access_token = 1000 * 60 * 45;//当 refreshToken 已过期了，再判断 accessToken 是否已过期,

    /**一般更换新的accessToken小于5分钟则提示需要更换新的accessToken*/
    private final static long refresh_token = 1000 * 60 * 40;//仅做token的是否需要更换新的accessToken标识,小于5分钟则提示需要更换新的accessToken

    private final static long app_access_token = 1000 * 60 * 60 * 24 * 20L;//20天

    private final static long app_refresh_token = 1000 * 60 * 60 * 24 * 7L;//7天

    private final static String issuer = "贵州富翁泰科技有限责任公司";//jwt签发者

    /**2048的密钥位的公钥*/
    private final static String publicKey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAv3gl5RjF9vTHCS2yr0zX3D50CKRiarMX+0qOpHAfcu24gWZ9bL39s48euPQniE2RhGxdMgyYSOePHLzzizGcOH3t9cyHzLU9NGVsY3dPZttbOUmsjdUB8BvUzVoj+uADkTlIRQv+ZBHP0Ze/cBLn2Z+PgAUi/XGRHnDXkjo+NjeFysHjqkUJ5U30gkSCK16I/vlD6c9V3bW+ZrQZ8ahbv3klG3MqYMMf1M7SCUSwZ9ITDgD/z5NdEBin3374/bkXPUpO5bmeX5p5f6nWWDfS2xrEukt5GuvbCAFZb4QbPGCJVP/haXS7X6WgrjwCGtMu1JxmH5Y/SL4mprExeiwGCwIDAQAB";
    /**2048的密钥位的私钥*/
    private final static String privateKey = "MIIEvwIBADANBgkqhkiG9w0BAQEFAASCBKkwggSlAgEAAoIBAQC/eCXlGMX29McJLbKvTNfcPnQIpGJqsxf7So6kcB9y7biBZn1svf2zjx649CeITZGEbF0yDJhI548cvPOLMZw4fe31zIfMtT00ZWxjd09m21s5SayN1QHwG9TNWiP64AOROUhFC/5kEc/Rl79wEufZn4+ABSL9cZEecNeSOj42N4XKweOqRQnlTfSCRIIrXoj++UPpz1Xdtb5mtBnxqFu/eSUbcypgwx/UztIJRLBn0hMOAP/Pk10QGKfffvj9uRc9Sk7luZ5fmnl/qdZYN9LbGsS6S3ka69sIAVlvhBs8YIlU/+FpdLtfpaCuPAIa0y7UnGYflj9IviamsTF6LAYLAgMBAAECggEBAIFiMUlUqkGtOupq5PoslRmRYluMqayshSIcSbO4yKejGDDpDwxyfoPdzMbyxdwHfrPbfkaZ2DY7s6uZy4Fd7tEF181nfsTIHCnR96JnN1KsHELkFCRrsjmMtQpnRTT8N6zk5n8yEZ+RobtiLc0QGXgnqJK75d8Z5RSK8EEx9mQviJvEHw2BjiA1Yxzm46CGTsmUYi8dmpwpuHiHsqStg/5BZYOu5pqflC9NwzDvxYa/8gBrh1kb0k2gS3QKVZZc9Qce7GL/Kxkpa84MPznX4MRmxUAutw6nAW08yNoyl0f1ov1DQ+zAEZ37dQM0HLirlvdCnIc4BiU1s/0PKMazItECgYEA7/LLmTk5BtGoZME3Ry3ESgN9NOgzCBZRnUQaNkWXS19Z77b+Tbbj7F/Wl0Rv4ZTjN8moOIJFG16a5wQjZRokYG7LHqq23xJ4nhZATAezuaE2Jg/efHnEzTsVTrlAAVz2YmBCNjuZhbGXRBxdGCkx6gDpOWRFpHvJZvDMSwlGTj8CgYEAzEchaM2x6udS5xvmUuoHnn1+ydajhQULkR2bD9J+xqmY0oPAE9wL509E0du9c5pbAeEbp90EMujjnOw0IgqqjobjDplyABOue2wMRMMAFsvy1J3NE5UJObtQJl9ORyeh3DUiqRwNU0+eLWhk6KT9Jd9m/M+6kSDYWPwWU0dzbTUCgYALG7hlPYrwT+B12aQPfYTq0EKJon5KT44zs6VrAcnBSYbGXDhlrKPcm4s5sYdkcBocyQ8qno1IeRa+vyhMU+t/M2Hf55NTvG0tFGahnnGplFcHaBCqvpBn1+ZioJzf80fMwhwOmq++yrvwAUgmCDIxXR1u9L9sn/Zw95DdoG5KzQKBgQDL9ompNxhaTzY7g2PpjbqlyO/7X9BNVMhD0KeumKH0ggoQx3PVYHSSNFemohz7fDc6HSWb6dXzbw009bLn+nZpcMztGtD0urIQkcV+MqW4HVMMb7L/xmM7vYc9iRODgFLgqD0/VL8JXAm3lPCMSZBpbDJMYq0Jkx3xW2J/LGxXsQKBgQDLMGZlsQw5ITwLgg8KeuQ0VnYbExr97R6s4fCXhCBPVjMLmUFhSjItWcGR1D8Xg9vJcjs50GrCBxs6+KdeHI1EAmUbqsp8aDJE1AeWu6pqF1bp+u8oTsHxaImF0FT2cyWUgNgULkGCFv3UVK6L74v66jNmamiahlJ1Fi4AcqbJ9w==";

    /**java生成的私钥是pkcs8格式的公钥是x.509格式*/
    private static PublicKey getPublicKey(){
        try {
            final byte[] keyBytes = Base64.getDecoder().decode(publicKey);
            final X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
            final KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            return keyFactory.generatePublic(keySpec);
        } catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    private static PrivateKey getPrivateKey(){
        try {
            final byte[] keyBytes = Base64.getDecoder().decode(privateKey);
            final PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
            final KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            return keyFactory.generatePrivate(keySpec);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // setSubject 不能和s etClaims() 同时使用,如果用不到 userId() 的话可以把setId的值设为 userName !!!
    private static String createToken(final String userId,final Object value,final long expiryDate){
        final ExecutorService threadPool = Executors.newCachedThreadPool();
        final Future<String> future = threadPool.submit(new Callable<String>(){
            @Override
            public String call() throws Exception{
                final long date = System.currentTimeMillis();
                final JwtBuilder builder = Jwts.builder().signWith(getPrivateKey(),SignatureAlgorithm.RS384);
                if(value !=null){
                    builder.claim(userId,value);
                }
                return builder.setId(userId).setIssuer(issuer).setIssuedAt(new Date(date)).setExpiration(new Date(date + expiryDate)).compact();
            }
        });
        try {
            return future.get();
        } catch (final Exception e) {
            threadPool.shutdown();
            return null;
        }
    }

    public static Claims parser(final String token){
        final JwtParserBuilder builder = Jwts.parserBuilder();
        return builder.requireIssuer(issuer).setSigningKey(getPublicKey()).build().parseClaimsJws(token).getBody();
    }

    /**
     * 验证token是否已失效,返回true已失效,否则有效
     * @param token
     * @作者 田应平
     * @QQ 444141300
     * @创建时间 2020年2月24日 16:19:00
    */
    public static boolean tokenExpired(final String token) {
        try {
            return parser(token).getExpiration().before(new Date());
        } catch (final ExpiredJwtException exp) {
            return true;
        }
    }

    public static boolean validateToken(final String token,final String userId){
        final String uid = extractUserId(token);
        return (uid.equals(userId) && !tokenExpired(token));
    }

    /**仅作为是否需要刷新的accessToken标识,不做任何业务处理*/
    public static String expireRefreshToken(final String userId){
        return createToken(userId,null,refresh_token);
    }

    /**生成带认证实体且有权限的token,最后个参数是含List<String>的角色信息,*/
    public static String expireAccessToken(final String userId){
        return createToken(userId,null,access_token);
    }

    public static String buildRefreshToken(final String userId){
        return createToken(userId,null,app_refresh_token);
    }

    public static String buildAccessToken(final String userId){
        return createToken(userId,null,app_access_token);
    }

    private static <T> T extractObjet(final String token,final Function<Claims,T> claimsResolver){
        final Claims claims = parser(token);
        return claimsResolver.apply(claims);
    }

    public static String extractUserId(final String token){
        return extractObjet(token,Claims::getId);
    }
}