package com.surkoff.qr_barman

import com.google.zxing.BarcodeFormat
import com.google.zxing.EncodeHintType
import com.google.zxing.WriterException
import com.google.zxing.client.j2se.MatrixToImageWriter
import com.google.zxing.common.BitMatrix
import com.google.zxing.qrcode.QRCodeWriter
import com.vaadin.flow.component.button.Button
import com.vaadin.flow.component.html.Image
import com.vaadin.flow.component.orderedlayout.VerticalLayout
import com.vaadin.flow.component.textfield.TextField
import com.vaadin.flow.router.Route
import com.vaadin.flow.server.InputStreamFactory
import com.vaadin.flow.server.StreamResource
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import java.io.ByteArrayInputStream
import java.nio.file.Files

@Route("")
@Component
class MainView @Autowired constructor(val codeGeneratorService: CodeGeneratorService) : VerticalLayout() {

    init {
        val textField = TextField("Введите текст для QR-кода")
        val generateButton = Button("Создать QR-код")
        val qrCodeImage = Image() // Изображение для отображения QR-кода

        generateButton.addClickListener {
            val text = textField.value
            if (text.isNotBlank()) {
                val qrCodeStream = codeGeneratorService.generateQrCode(text)

                // Явно указываем, что лямбда относится к InputStreamFactory
                val resource = StreamResource("qrcode.png", InputStreamFactory { qrCodeStream })
                qrCodeImage.setSrc(resource)
            }
        }

        add(textField, generateButton, qrCodeImage)
    }
}

//import com.github.mvysny.karibudsl.v10.*
//import com.github.mvysny.kaributools.setPrimary
//import com.vaadin.flow.component.Key
//import com.vaadin.flow.router.Route
//import org.springframework.beans.factory.annotation.Autowired
//
///**
// * A sample Vaadin view class.
// *
// * To implement a Vaadin view just extend any Vaadin component and use @Route
// * annotation to announce it in a URL as a Spring managed bean.
// *
// * A new instance of this class is created for every new user and every browser
// * tab/window.
// *
// * The main view contains a text field for getting the user name and a button
// * that shows a greeting message in a notification.
// * @param service The message service. Automatically injected Spring managed
// * bean.
// */
//@Route
//class MainView(@Autowired service: GreetService) : KComposite() {
//    /**
//     * Construct a new Vaadin view.
//     *
//     * Build the initial UI state for the user accessing the application.
//     */
//    private val root = ui {
//        // Use custom CSS classes to apply styling. This is defined in
//        // styles.css.
//        verticalLayout(classNames = "centered-content") {
//            // Use TextField for standard text input
//            val nameField = textField("Your name") {
//                addClassName("bordered")
//            }
//
//            // Button click listeners can be defined as lambda expressions
//            button("Say hello") {
//                // Theme variants give you predefined extra styles for components.
//                // Example: Primary button has a more prominent look.
//                setPrimary()
//
//                // You can specify keyboard shortcuts for buttons.
//                // Example: Pressing enter in this view clicks the Button.
//                addClickShortcut(Key.ENTER)
//
//                onLeftClick {
//                    this@verticalLayout.p(service.greet(nameField.value))
//                }
//            }
//        }
//    }
//}
