package team.redrock.running.secret;

import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;

public class Crypt {

//    token = base64_decode(token) // base64 解码登录后获取的token
//    // @param data 为加密的数据
//    function decrypt(data)
//    {
//        data = base64_decode(data) // 因为 data 是 base64 解码后的数据，所以先进性 base64 解码
//
//        // 解密函数 使用 rijndael-128 算法，token 作为数据加密密钥, 数据是data, 模式为 ecb
//        data = mcrypt_decrypt('rijndael-128', token, data,'ecb')
//
//        // trim() 函数：去除字符串首尾处的空白字符 @http://php.net/manual/zh/function.trim.php
//        data = trim(data)
//
//        // 去除 pkcs7padding 填充模式
//        data = stripPkcs7Padding(data)
//
//        return data
//    }
    public static void decrypt(String token, String data){
        token = Base64.encode(token.getBytes());
        data = Base64.encode(data.getBytes());

    }
}
