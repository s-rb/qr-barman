package com.surkoff.qr_barman

import com.google.zxing.BarcodeFormat
import com.google.zxing.EncodeHintType
import com.google.zxing.WriterException
import com.google.zxing.client.j2se.MatrixToImageWriter
import com.google.zxing.common.BitMatrix
import com.google.zxing.qrcode.QRCodeWriter
import org.springframework.stereotype.Service
import java.io.ByteArrayInputStream
import java.nio.file.Files

const val WIDTH = 200
const val HEIGHT = 200

@Service
class CodeGeneratorService {

    fun generateQrCode(text: String): ByteArrayInputStream {
        return try {
            val qrCodeWriter = QRCodeWriter()

            // Указываем параметры кодирования
            val hints = mapOf(
                EncodeHintType.CHARACTER_SET to "UTF-8", // Кодировка UTF-8 для поддержки кириллицы
                EncodeHintType.MARGIN to 1 // Минимальный отступ вокруг QR-кода
            )

            // Генерируем матрицу QR-кода
            val bitMatrix: BitMatrix = qrCodeWriter.encode(
                text,
                BarcodeFormat.QR_CODE,
                WIDTH,
                HEIGHT,
                hints
            )

            // Создаем временный файл для QR-кода
            val tempFile = Files.createTempFile("qr", ".png")
            MatrixToImageWriter.writeToPath(bitMatrix, "PNG", tempFile)

            // Читаем содержимое файла и возвращаем в виде потока
            ByteArrayInputStream(Files.readAllBytes(tempFile))
        } catch (e: WriterException) {
            throw RuntimeException("Ошибка при создании QR-кода: ${e.message}")
        }
    }
}