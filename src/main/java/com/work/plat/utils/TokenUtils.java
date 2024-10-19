//package com.work.plat.utils;
//
//import cn.hutool.core.date.DateUtil;
//import cn.hutool.core.util.StrUtil;
//import com.auth0.jwt.JWT;
//import com.auth0.jwt.algorithms.Algorithm;
//import com.work.plat.constants.TokenConstant;
//import com.work.plat.entity.bo.UserDO;
//import com.work.plat.entity.TokenInfo;
//import com.work.plat.service.IUserService;
//import io.jsonwebtoken.JwtBuilder;
//import io.jsonwebtoken.Jwts;
//import io.jsonwebtoken.SignatureAlgorithm;
//import kotlin.text.Charsets;
//import org.springframework.stereotype.Component;
//import org.springframework.web.context.request.RequestContextHolder;
//import org.springframework.web.context.request.ServletRequestAttributes;
//
//import javax.annotation.PostConstruct;
//import javax.annotation.Resource;
//import javax.crypto.spec.SecretKeySpec;
//import javax.servlet.http.HttpServletRequest;
//import java.security.Key;
//import java.util.*;
//
//@Component
//public class TokenUtil {
//
//    private static final String BASE64_SECURITY = Base64.getEncoder().encodeToString(TokenConstant.SIGN_KEY.getBytes(Charsets.UTF_8));
//
//
//    private static IUserService staticUserService;
//
//    @Resource
//    private IUserService userService;
//
//    @PostConstruct
//    public void setUserService() {
//        staticUserService = userService;
//    }
//
//    /**
//     * 生成token
//     *
//     * @return
//     */
//    public static String genToken(String userId, String sign) {
//        return JWT.create().withAudience(userId) // 将 user id 保存到 token 里面,作为载荷
//                .withExpiresAt(DateUtil.offsetHour(new Date(), 2)) // 2小时后token过期
//                .sign(Algorithm.HMAC256(sign)); // 以 password 作为 token 的密钥
//    }
//
//    public static TokenInfo createJWT(Map<String, Object> user, String audience, String issuer, String tokenType) {
//
//        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
//
//        long nowMillis = System.currentTimeMillis();
//        Date now = new Date(nowMillis);
//
//        //生成签名密钥
//        byte[] apiKeySecretBytes = Base64.getDecoder().decode(BASE64_SECURITY);
//        Key signingKey = new SecretKeySpec(apiKeySecretBytes, signatureAlgorithm.getJcaName());
//
//        //添加构成JWT的类
//        JwtBuilder builder = Jwts.builder().setHeaderParam("typ", "JWT")
//                .setIssuer(issuer)
//                .setAudience(audience)
//                .signWith(signatureAlgorithm,signingKey);
//
//        //设置JWT参数
//        user.forEach(builder::claim);
//
//
//        //添加Token过期时间
//        long expireMillis = getExpire();;
//
//        long expMillis = nowMillis + expireMillis;
//        Date exp = new Date(expMillis);
//        builder.setExpiration(exp).setNotBefore(now);
//
//        // 组装Token信息
//        TokenInfo tokenInfo = new TokenInfo();
//        tokenInfo.setToken(builder.compact());
//        tokenInfo.setExpire((int) expireMillis / 1000);
//
//        return tokenInfo;
//    }
//
//    /**
//     * 获取过期时间(次日凌晨3点)
//     *
//     * @return expire
//     */
//    public static long getExpire() {
//        Calendar cal = Calendar.getInstance();
//        cal.add(Calendar.DAY_OF_YEAR, 1);
//        cal.set(Calendar.HOUR_OF_DAY, 3);
//        cal.set(Calendar.SECOND, 0);
//        cal.set(Calendar.MINUTE, 0);
//        cal.set(Calendar.MILLISECOND, 0);
//        return cal.getTimeInMillis() - System.currentTimeMillis();
//    }
//
//    /**
//     * 解析jsonWebToken
//     *
//     * @param  token
//     * @return Claims
//     */
//    public static List<String> parseJWT(String token) {
//        try {
//            return JWT.decode(token).getAudience();
//        } catch (Exception ex) {
//            return null;
//        }
//    }
//
//    /**
//     * 获取当前登录的用户信息
//     *
//     * @return user对象
//     */
//    public static UserDO getCurrentUser() {
//        try {
//            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
//            String token = request.getHeader("token");
//            if (StrUtil.isNotBlank(token)) {
//                String userId = JWT.decode(token).getAudience().get(0);
//                return staticUserService.getById(Integer.valueOf(userId));
//            }
//        } catch (Exception e) {
//            return null;
//        }
//        return null;
//    }
//}
