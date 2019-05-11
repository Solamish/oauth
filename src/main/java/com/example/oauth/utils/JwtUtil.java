package com.example.oauth.utils;

import com.alibaba.fastjson.JSONObject;
import io.jsonwebtoken.*;
import org.apache.commons.codec.binary.Base64;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class JwtUtil {

    private String Solamish;
    private static JwtUtil singleton;

    /**
     * 获得单例
     */
    public static JwtUtil getInstance() {
        if (singleton == null) {
            synchronized (JwtUtil.class) {
                if (singleton == null) {
                    singleton = new JwtUtil();
                }
            }
        }
        return singleton;
    }

    /**
     * 由字符串生成加密key
     *
     * @return
     */
    public SecretKey generalKey() {
        String stringKey = Solamish + Constant.JWT_SECRET;
        byte[] encodedKey = Base64.decodeBase64(stringKey);

        // 根据给定的字节数组使用AES加密算法构造一个密钥，使用 encodedKey中的始于且包含 0 到前 leng 个字节这是当然是所有。
        SecretKey key = new SecretKeySpec(encodedKey, 0, encodedKey.length, "AES");
        return key;
    }

    /**
     * 创建jwt
     *
     * @param id
     * @param subject
     * @param ttlMillis 过期的时间长度
     * @return
     * @throws Exception
     */
    public String createJWT(String id, String subject, long ttlMillis) throws Exception {
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256; //指定签名的时候使用的签名算法，也就是header那部分，jjwt已经将这部分内容封装好了。
        long nowMillis = System.currentTimeMillis();//生成JWT的时间
        Date now = new Date(nowMillis);
        Map<String, Object> claims = new HashMap<String, Object>();//创建payload的私有声明（根据特定的业务需要添加，如果要拿这个做验证，一般是需要和jwt的接收方提前沟通好验证方式的）
        claims.put("uid", "WDAXIWA...");
        claims.put("user_name", "admin");
        claims.put("nick_name", "Solamish");
        SecretKey key = generalKey();//生成签名的时候使用的秘钥secret

        JwtBuilder builder = Jwts.builder() //这里其实就是new一个JwtBuilder，设置jwt的body
                .setClaims(claims)          //如果有私有声明，一定要先设置这个自己创建的私有的声明，这个是给builder的claim赋值，一旦写在标准的声明赋值之后，就是覆盖了那些标准的声明的
                .setId(id)                  //设置jti(JWT ID)：是JWT的唯一标识，根据业务需要，这个可以设置为一个不重复的值，主要用来作为一次性token,从而回避重放攻击。
                .setIssuedAt(now)           //iat: jwt的签发时间
                .setSubject(subject)        //sub(Subject)：代表这个JWT的主体，即它的所有人，这个是一个json格式的字符串，可以存放什么userid，roldid之类的，作为什么用户的唯一标志。
                .signWith(signatureAlgorithm, key);//设置签名使用的签名算法和签名使用的秘钥
        if (ttlMillis >= 0) {
            long expMillis = nowMillis + ttlMillis;
            Date exp = new Date(expMillis);
            builder.setExpiration(exp);     //设置过期时间
        }
        return builder.compact();
    }

    /**
     * 解密jwt
     * @param jwt
     * @return
     * @throws Exception
     */
    public  Claims parseJWT(String jwt) throws Exception{
        SecretKey key = generalKey();  //签名秘钥，和生成的签名的秘钥一模一样
        Claims claims = Jwts.parser()  //得到DefaultJwtParser
                .setSigningKey(key)         //设置签名的秘钥
                .parseClaimsJws(jwt).getBody();//设置需要解析的jwt
        return claims;
    }


    /*public JWTCheckResult validateToken(String token) {
        JWTCheckResult result = new JWTCheckResult();

        if(StringUtils.isEmpty(token)){
            result.setStatus(JWTEnum.EMPTY.getStatus());
            result.setMessage(JWTEnum.EMPTY.getMessage());
            return result;
        }

        try {
            Key key = getKey();
            Jws<Claims> jwt = Jwts.parser().setSigningKey(key).parseClaimsJws(token);

        } catch (SignatureException e) {
            result.setStatus(JWTEnum.ERROR.getStatus());
            result.setMessage(JWTEnum.ERROR.getMessage());

            System.out.println(e);
        }catch(ExpiredJwtException e){
            result.setStatus(JWTEnum.TIMEOUT.getStatus());
            result.setMessage(JWTEnum.TIMEOUT.getMessage());

            System.out.println(e);
        }catch(Exception e){
            result.setStatus(JWTEnum.ERROR.getStatus());
            result.setMessage(JWTEnum.ERROR.getMessage());
            System.out.println(e);
        }
        return result;
    }
*/




}
