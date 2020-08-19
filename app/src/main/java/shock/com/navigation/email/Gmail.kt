package shock.com.navigation.email

import java.io.ByteArrayInputStream
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream
import java.security.Security
import java.util.*
import javax.activation.DataHandler
import javax.activation.DataSource
import javax.mail.*
import javax.mail.internet.InternetAddress
import javax.mail.internet.MimeMessage


class Gmail: Authenticator {
    private val mailhost = "smtp.gmail.com"
    private var user: String? = null
    private var password: String? = null
    private var session: Session? = null

    init
    {
        Security.addProvider(JSSEProvider())
    }

    constructor(user: String?, password: String?) {
        this.user = user
        this.password = password
        val props = Properties()
        props.setProperty("mail.transport.protocol", "smtp")
        props.setProperty("mail.host", mailhost)
        props.put("mail.smtp.auth", "true")
        props.put("mail.smtp.port", "465")
        props.put("mail.smtp.socketFactory.port", "465")
        props.put(
            "mail.smtp.socketFactory.class",
            "javax.net.ssl.SSLSocketFactory"
        )
        props.put("mail.smtp.socketFactory.fallback", "false")
        props.setProperty("mail.smtp.quitwait", "false")
        session = Session.getDefaultInstance(props, this)
    }

    override fun getPasswordAuthentication(): PasswordAuthentication? {
        return PasswordAuthentication(user, password)
    }

    @Synchronized
    @Throws(Exception::class)
    fun sendMail(
        subject: String?,
        body: String,
        sender: String?,
        recipients: String?
    ) {
        try {
            val message = MimeMessage(session)
            val handler =
                DataHandler(ByteArrayDataSource(body.toByteArray(), "text/plain"))
            message.sender = InternetAddress(sender)
            message.subject = subject
            message.dataHandler = handler
            if (recipients?.indexOf(',')!! > 0) message.setRecipients(
                Message.RecipientType.TO,
                InternetAddress.parse(recipients)
            ) else message.setRecipient(Message.RecipientType.TO, InternetAddress(recipients))
            Transport.send(message)
        } catch (e: Exception) {
        }
    }

    class ByteArrayDataSource : DataSource {
        private var data: ByteArray
        private var type: String? = null

        constructor(data: ByteArray, type: String?) : super() {
            this.data = data
            this.type = type
        }

        constructor(data: ByteArray) : super() {
            this.data = data
        }

        fun setType(type: String?) {
            this.type = type
        }

        override fun getName(): String {
            return "ByteArrayDataSource";
        }

        override fun getOutputStream(): OutputStream {
            throw IOException("Not Supported");
        }

        override fun getInputStream(): InputStream {
            return ByteArrayInputStream(data);
        }

        override fun getContentType(): String {
            if (type == null)
                return "application/octet-stream";
            else
                return type as String;
        }
    }
}
