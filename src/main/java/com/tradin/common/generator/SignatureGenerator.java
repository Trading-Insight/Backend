package com.tradin.common.generator;

import com.tradin.common.exception.TradinException;
import lombok.experimental.UtilityClass;
import org.apache.commons.codec.binary.Hex;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import static com.tradin.common.exception.ExceptionMessage.SIGNATURE_GENERATION_FAIL_EXCEPTION;

@UtilityClass
public class SignatureGenerator {
    public String generateSignature(String data, String key) {
        try {
            Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(new SecretKeySpec(key.getBytes(), "HmacSHA256"));
            byte[] bytes = mac.doFinal(data.getBytes());
            return Hex.encodeHexString(bytes);
        } catch (Exception e) {
            throw new TradinException(SIGNATURE_GENERATION_FAIL_EXCEPTION);
        }
    }
}
