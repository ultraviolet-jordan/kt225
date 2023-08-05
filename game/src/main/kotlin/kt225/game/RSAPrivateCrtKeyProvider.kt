package kt225.game

import com.google.inject.Provider
import com.google.inject.Singleton
import org.bouncycastle.asn1.pkcs.PrivateKeyInfo
import org.bouncycastle.util.io.pem.PemReader
import java.io.File
import java.io.FileReader
import java.security.KeyFactory
import java.security.interfaces.RSAPrivateCrtKey
import java.security.spec.PKCS8EncodedKeySpec

/**
 * @author Jordan Abraham
 */
@Singleton
class RSAPrivateCrtKeyProvider : Provider<RSAPrivateCrtKey> {
    override fun get(): RSAPrivateCrtKey {
        val pemReader = PemReader(FileReader(File(javaClass.getResource("/rsa.pem")!!.toURI())))
        val pemObject = pemReader.readPemObject() ?: throw IllegalArgumentException("Invalid PEM data")
        pemReader.close()

        val encodedKey = PrivateKeyInfo.getInstance(pemObject.content)
        val keySpec = PKCS8EncodedKeySpec(encodedKey.encoded)
        return KeyFactory.getInstance("RSA").generatePrivate(keySpec) as RSAPrivateCrtKey
    }
}