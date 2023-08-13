package com.example.credentialmanagermvvm.utils

import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.InputStream
import java.io.OutputStream
import java.security.KeyStore
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.IvParameterSpec

class CryptoManager {

    private val keyStore = KeyStore.getInstance("AndroidKeyStore").apply {
        load(null)
    }

    private val encryptCipher = Cipher.getInstance(TRANSFORMATION).apply {
        init(Cipher.ENCRYPT_MODE,getKey())
    }

    private fun getDecryptCipher(iv:ByteArray):Cipher{
        return Cipher.getInstance(TRANSFORMATION).apply {
            init(Cipher.DECRYPT_MODE,getKey(),IvParameterSpec(iv))
        }

    }

    fun decrypt( bites:ByteArray):ByteArray {
        val inputStream = ByteArrayInputStream(bites)
        val ivSize = inputStream.read()
        val iv = ByteArray(ivSize)
        inputStream.read(iv)

        val encryptedBytesSize = inputStream.read()
        val encryptedBytes = ByteArray(encryptedBytesSize)
        inputStream.read(encryptedBytes)

        val decryptedBytes = getDecryptCipher(iv).doFinal(encryptedBytes)
        inputStream.close()

        return decryptedBytes

    }

    fun encrypt(bytes: ByteArray,outputStream: ByteArrayOutputStream):ByteArray{
        val encryptedBytes = encryptCipher.doFinal(bytes)
        outputStream.use {
            it.write(encryptCipher.iv.size)
            it.write(encryptCipher.iv)
            it.write(encryptedBytes.size)
            it.write(encryptedBytes)
        }
        return encryptedBytes

    }

    private fun getKey():SecretKey{
        val existingKey = keyStore.getEntry("secret",null) as? KeyStore.SecretKeyEntry
        return existingKey?.secretKey ?: createKey()
    }

    private fun createKey():SecretKey{
        return KeyGenerator.getInstance(ALGORITHM).apply {
            init(
                KeyGenParameterSpec.Builder(
                  "secret",
                  KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT
                )
                    .setBlockModes(BLOCK_MODE)
                    .setEncryptionPaddings(PADDING)
                    .setUserAuthenticationRequired(false)
                    .setRandomizedEncryptionRequired(true)
                    .build()
            )
        }.generateKey()
    }
    companion object {
        private const val ALGORITHM = KeyProperties.KEY_ALGORITHM_AES
        private const val BLOCK_MODE = KeyProperties.BLOCK_MODE_CBC
        private const val PADDING = KeyProperties.ENCRYPTION_PADDING_PKCS7
        private const val TRANSFORMATION = "$ALGORITHM/$BLOCK_MODE/$PADDING"

    }



}