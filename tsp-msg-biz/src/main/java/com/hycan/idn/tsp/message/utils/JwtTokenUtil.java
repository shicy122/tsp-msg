package com.hycan.idn.tsp.message.utils;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import com.alibaba.cloud.commons.lang.StringUtils;
import com.hycan.idn.tsp.message.enums.ResultCodeTypeEnum;
import com.hycan.idn.tsp.message.exception.MsgBusinessException;
import com.hycan.idn.tsp.message.pojo.avntmqtt.JwtTokenDTO;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import lombok.extern.slf4j.Slf4j;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.Collections;
import java.util.Map;
import java.util.Objects;

/**
 * @author shichongying
 */
@Slf4j
public class JwtTokenUtil {

    /**
     * 解码头
     *
     * @param token     令牌
     * @param publicKey 公钥
     * @param clientId  客户机id
     * @return {@link JwtTokenDTO}
     */
    public static JwtTokenDTO decodeToken(String token, String publicKey, String clientId) {
        Map<String, Object> map = JwtTokenUtil.parseAccessJwtToken(token, publicKey);
        if (CollUtil.isEmpty(map) || !isTokenValid(map)) {
            throw new MsgBusinessException("Token非法: Client-ID=" + clientId + ", Authentication=" + token,
                    ResultCodeTypeEnum.TOKEN_FAIL.getCode());
        }

        return BeanUtil.toBeanIgnoreError(map, JwtTokenDTO.class);
    }

    /**
     * 解析Token获取以下信息
     *  iss: 车系编号
     *  iat: JWT加密时间
     *  exp: 过期时间，默认15min
     *  vin: 车架号
     *  sn: 车机SN
     *
     * @param accessToken 用户访问令牌
     * @param publicKey 公钥，用于验证token的签名
     * @return 解析结果
     */
    public static Map<String, Object> parseAccessJwtToken(String accessToken, String publicKey) {
        try {
            if (StringUtils.isEmpty(accessToken)) {
                log.error("Token不能为空!");
                return Collections.emptyMap();
            }

            PublicKey ecPublicKey = getEcPublicKey(publicKey);
            if (ecPublicKey == null) {
                log.error("获取公钥PublicKey失败!");
                return Collections.emptyMap();
            }

            Jws<Claims> jws = Jwts.parser().setSigningKey(ecPublicKey).parseClaimsJws(accessToken);
            return jws.getBody();
        } catch (Exception e) {
            log.error("Token解析异常={}", ExceptionUtil.getBriefStackTrace(e));
            return Collections.emptyMap();
        }
    }

    private static boolean isTokenValid(Map<String, Object> claimsMap) {
        if (Objects.isNull(claimsMap.get("vin"))) {
            log.error("获取MQTT Topic列表接口: Token解析失败, VIN为空值!");
            return false;
        }

        if (Objects.isNull(claimsMap.get("sn"))) {
            log.error("获取MQTT Topic列表接口: Token解析失败, sn为空值!");
            return false;
        }

        if (Objects.isNull(claimsMap.get("source"))) {
            log.error("获取MQTT Topic列表接口: Token解析失败, source为空值!");
            return false;
        }

        if (Objects.isNull(claimsMap.get("date"))) {
            log.error("获取MQTT Topic列表接口: Token解析失败, date为空值!");
            return false;
        }

        if (Objects.isNull(claimsMap.get("iss"))) {
            log.error("获取MQTT Topic列表接口: Token解析失败, iss为空值!");
            return false;
        }
        return true;
    }

    /**
    * 从字符串形式的公钥中获取PublicKey对象
     *
    * @param publicKey 公钥字符串
    * @return 返回解析得到的PublicKey对象，如果解析失败则返回null
    */
    private static PublicKey getEcPublicKey(String publicKey) {
        try {
            Security.addProvider(new BouncyCastleProvider());
            KeyFactory keyFactory = KeyFactory.getInstance("ECDH", "BC");
            publicKey = publicKey.replaceAll("\\-*BEGIN.*KEY\\-*", "")
                    .replaceAll("\\-*END.*KEY\\-*", "")
                    .replaceAll("\r", "")
                    .replaceAll("\n", "");
            byte[] publicKeyBytes = Base64.getDecoder().decode(publicKey);

            X509EncodedKeySpec pubX509 = new X509EncodedKeySpec(publicKeyBytes);
            return keyFactory.generatePublic(pubX509);
        } catch (NoSuchAlgorithmException | NoSuchProviderException | InvalidKeySpecException e) {
            log.error("获取PublicKey对象异常={}", ExceptionUtil.getBriefStackTrace(e));
            return null;
        }
    }
}



