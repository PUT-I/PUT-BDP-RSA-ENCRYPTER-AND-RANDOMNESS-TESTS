package com.gunock.pod.encrypter

import com.gunock.pod.utils.FormUtil


class RsaEncrypter {

    static encryptDecrypt(String source, String key) {
        if (source.length() * 8 > key.length()) {
            FormUtil.showMessage('Encryption',
                    'Key will be reused multiple times to encode/decode given text!')
        }

        StringBuilder result = new StringBuilder()
        for (int i = 0; i < source.length(); i++) {
            final int sourceByte = (int) (source[i] as char)
            final String keyByteStr = getByteFromKey(key, i)
            final int keyByte = Integer.parseInt(keyByteStr, 2)
            char resultCharacter = sourceByte ^ keyByte
            resultCharacter &= 127
            result.append(resultCharacter)
        }
        return result.toString()
    }

    private static String getByteFromKey(String key, int sourceByte) {
        String result = ''
        if (key.length() >= 8) {
            final int keyByteStartIndex = (sourceByte * 8) % key.length()
            final int keyByteEndIndex = (keyByteStartIndex + 8) % key.length()
            if (keyByteEndIndex < keyByteStartIndex) {
                result = key.substring(keyByteStartIndex, key.length()) + key.substring(0, keyByteEndIndex)
            } else {
                result = key.substring(keyByteStartIndex, keyByteEndIndex)
            }
        } else {
            for (int j = 0; j < 8; j++) {
                result += key[(sourceByte + j) % key.length()]
            }
        }
        return result
    }

}
